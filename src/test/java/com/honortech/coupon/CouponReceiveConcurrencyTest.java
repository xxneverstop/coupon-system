package com.honortech.coupon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntToLongFunction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 运行说明：
 * 1. 先手动启动应用，并确认服务监听在 http://localhost:19022
 * 2. 先准备一个 activityId，并在测试前修改 ACTIVITY_ID
 * 3. 同一用户并发测试前，请清理该 userId + activityId 的历史 user_coupon 数据
 * 4. 不同用户并发测试前，请准备足够库存，并清理对应测试用户的历史数据
 */
public class CouponReceiveConcurrencyTest {

    private static final String BASE_URL = "http://localhost:19022";
    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/coupon_db_test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Xrw2001724..";

    private static final long ACTIVITY_ID = 7L;

    private static final int REQUEST_COUNT = 100;
    private static final int STOCK = 100;
    private static final long START_USER_ID = 1000L;

    private static final int SAME_USER_REQUEST_COUNT = 50;
    private static final long SAME_USER_ID = 300011L;

    private static final int THREAD_POOL_SIZE = 20;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReceiveCouponConcurrently() throws Exception {
        int beforeRemainingStock = queryRemainingStock(ACTIVITY_ID);

        ConcurrencyTestResult result = runConcurrentReceiveTest(
                "Different Users Concurrent Receive Test",
                REQUEST_COUNT,
                ACTIVITY_ID,
                STOCK,
                index -> START_USER_ID + index
        );

        int afterRemainingStock = queryRemainingStock(ACTIVITY_ID);
        int actualStockDeduction = beforeRemainingStock - afterRemainingStock;

        Assertions.assertTrue(result.successCount() <= STOCK, "成功数不应超过库存");
        Assertions.assertEquals(REQUEST_COUNT, result.successCount() + result.failureCount(), "成功和失败总数应等于请求总数");
        Assertions.assertEquals(result.successCount(), actualStockDeduction, "库存扣减量应与成功数一致");
        Assertions.assertTrue(afterRemainingStock >= 0, "库存不能扣成负数");
    }

    @Test
    void testSameUserConcurrentReceive() throws Exception {
        int beforeRemainingStock = queryRemainingStock(ACTIVITY_ID);

        ConcurrencyTestResult result = runConcurrentReceiveTest(
                "Same User Concurrent Receive Test",
                SAME_USER_REQUEST_COUNT,
                ACTIVITY_ID,
                1,
                index -> SAME_USER_ID
        );

        int afterRemainingStock = queryRemainingStock(ACTIVITY_ID);
        int actualStockDeduction = beforeRemainingStock - afterRemainingStock;
        int userCouponCount = queryUserCouponCount(SAME_USER_ID, ACTIVITY_ID);

        Assertions.assertEquals(
                SAME_USER_REQUEST_COUNT,
                result.successCount() + result.failureCount(),
                "成功和失败总数应等于请求总数"
        );
        Assertions.assertEquals(1, result.successCount(), "同一用户并发领取只应成功 1 次");
        Assertions.assertEquals(1, userCouponCount, "同一 userId + activityId 最终只能有 1 条 user_coupon");
        Assertions.assertEquals(1, actualStockDeduction, "同一用户并发领取时库存只应减少 1");
        Assertions.assertTrue(afterRemainingStock >= 0, "库存不能扣成负数");
    }

    private ConcurrencyTestResult runConcurrentReceiveTest(
            String scenarioName,
            int requestCount,
            long activityId,
            Integer expectedMaxSuccess,
            IntToLongFunction userIdProvider) throws Exception {

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();
        List<String> failureResponses = new CopyOnWriteArrayList<>();

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(requestCount);
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try {
            for (int i = 0; i < requestCount; i++) {
                final int index = i;
                final long userId = userIdProvider.applyAsLong(index);
                executorService.submit(() -> {
                    try {
                        startLatch.await();
                        String responseBody = sendReceiveRequest(httpClient, userId, activityId);
                        int code = extractCode(responseBody);
                        if (code == 0) {
                            successCount.incrementAndGet();
                        } else {
                            failureCount.incrementAndGet();
                            failureResponses.add("userId=" + userId + ", response=" + responseBody);
                        }
                    } catch (Exception ex) {
                        failureCount.incrementAndGet();
                        failureResponses.add("userId=" + userId + ", exception=" + ex.getMessage());
                    } finally {
                        endLatch.countDown();
                    }
                });
            }

            startLatch.countDown();
            boolean finished = endLatch.await(60, TimeUnit.SECONDS);
            Assertions.assertTrue(finished, "并发请求未在预期时间内完成");
        } finally {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        }

        printSummary(scenarioName, activityId, requestCount, expectedMaxSuccess, successCount.get(), failureCount.get(), failureResponses);
        return new ConcurrencyTestResult(successCount.get(), failureCount.get());
    }

    private void printSummary(
            String scenarioName,
            long activityId,
            int requestCount,
            Integer expectedMaxSuccess,
            int successCount,
            int failureCount,
            List<String> failureResponses) {

        System.out.println("========== " + scenarioName + " ==========");
        System.out.println("activityId = " + activityId);
        System.out.println("requestCount = " + requestCount);
        if (expectedMaxSuccess != null) {
            System.out.println("expectedMaxSuccess = " + expectedMaxSuccess);
        }
        System.out.println("successCount = " + successCount);
        System.out.println("failureCount = " + failureCount);

        if (!failureResponses.isEmpty()) {
            System.out.println("---------- Sample Failure Responses ----------");
            failureResponses.stream().limit(10).forEach(System.out::println);
        }
    }

    private String sendReceiveRequest(HttpClient httpClient, long userId, long activityId) throws Exception {
        String requestBody = """
                {
                  "userId": %d,
                  "activityId": %d
                }
                """.formatted(userId, activityId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/coupon/receive"))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private int extractCode(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);
        return root.path("code").asInt(-1);
    }

    private int queryRemainingStock(long activityId) throws Exception {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select remaining_quantity from coupon_activity where id = ?")) {
            statement.setLong(1, activityId);
            try (ResultSet resultSet = statement.executeQuery()) {
                Assertions.assertTrue(resultSet.next(), "测试 activityId 不存在");
                return resultSet.getInt(1);
            }
        }
    }

    private int queryUserCouponCount(long userId, long activityId) throws Exception {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select count(*) from user_coupon where user_id = ? and activity_id = ?")) {
            statement.setLong(1, userId);
            statement.setLong(2, activityId);
            try (ResultSet resultSet = statement.executeQuery()) {
                Assertions.assertTrue(resultSet.next(), "查询 user_coupon 数量失败");
                return resultSet.getInt(1);
            }
        }
    }

    private Connection openConnection() throws Exception {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    private record ConcurrencyTestResult(int successCount, int failureCount) {
    }
}
