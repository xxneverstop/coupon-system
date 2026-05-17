package com.honortech.coupon.service.impl;

import com.honortech.coupon.common.constant.CouponConstants;
import com.honortech.coupon.common.exception.BizException;
import com.honortech.coupon.domain.dto.ReceiveCouponRequest;
import com.honortech.coupon.domain.dto.ReceiveCouponResponse;
import com.honortech.coupon.domain.entity.CouponActivity;
import com.honortech.coupon.domain.entity.CouponReceiveRecord;
import com.honortech.coupon.domain.entity.UserCoupon;
import com.honortech.coupon.mapper.CouponActivityMapper;
import com.honortech.coupon.mapper.CouponReceiveRecordMapper;
import com.honortech.coupon.mapper.UserCouponMapper;
import com.honortech.coupon.service.CouponReceiveRecordWriteService;
import com.honortech.coupon.service.CouponService;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CouponServiceImpl implements CouponService {

    private final CouponActivityMapper couponActivityMapper;
    private final UserCouponMapper userCouponMapper;
    private final CouponReceiveRecordMapper couponReceiveRecordMapper;
    private final CouponReceiveRecordWriteService couponReceiveRecordWriteService;

    public CouponServiceImpl(
            CouponActivityMapper couponActivityMapper,
            UserCouponMapper userCouponMapper,
            CouponReceiveRecordMapper couponReceiveRecordMapper,
            CouponReceiveRecordWriteService couponReceiveRecordWriteService) {
        this.couponActivityMapper = couponActivityMapper;
        this.userCouponMapper = userCouponMapper;
        this.couponReceiveRecordMapper = couponReceiveRecordMapper;
        this.couponReceiveRecordWriteService = couponReceiveRecordWriteService;
    }

    @Override
    @Transactional
    public ReceiveCouponResponse receiveCoupon(ReceiveCouponRequest request) throws InterruptedException {
        CouponActivity activity = couponActivityMapper.selectById(request.getActivityId());
        if (activity == null) {
            throw new BizException(400, "活动不存在");
        }

        // 扣减库存
        int updatedRows = couponActivityMapper.deductStockIfAvailable(request.getActivityId());
        if (updatedRows == 0) {
            throwReceiveFailure(request, activity);
        }

        // 创建用户券，唯一索引避免重复领取
        LocalDateTime now = LocalDateTime.now();
        UserCoupon userCoupon = buildUserCoupon(request, activity, now);
        try {
            userCouponMapper.insert(userCoupon);
        } catch (DuplicateKeyException ex) {
            log.warn("Duplicate coupon receive request, userId={}, activityId={}",
                    request.getUserId(), request.getActivityId(), ex);

            // 重复领取，写记录
            recordFailure(request, activity, "该用户已领取过该活动优惠券");
            throw new BizException(400, "该用户已领取过该活动优惠券");
        }

        // 领券成功，写记录
        CouponReceiveRecord receiveRecord = new CouponReceiveRecord();
        receiveRecord.setUserId(request.getUserId());
        receiveRecord.setActivityId(activity.getId());
        receiveRecord.setTemplateId(activity.getTemplateId());
        receiveRecord.setReceiveStatus(CouponConstants.RECEIVE_STATUS_SUCCESS);
        receiveRecord.setFailReason(null);
        receiveRecord.setCreateTime(now);
        couponReceiveRecordMapper.insert(receiveRecord);

        userCoupon.setReceiveRecordId(receiveRecord.getId());
        userCouponMapper.updateById(userCoupon);

        return new ReceiveCouponResponse(userCoupon.getId(), userCoupon.getCouponCode());
    }

    private UserCoupon buildUserCoupon(ReceiveCouponRequest request, CouponActivity activity, LocalDateTime now) {
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setCouponCode(UUID.randomUUID().toString().replace("-", ""));
        userCoupon.setUserId(request.getUserId());
        userCoupon.setTemplateId(activity.getTemplateId());
        userCoupon.setActivityId(activity.getId());
        userCoupon.setStatus(CouponConstants.USER_COUPON_STATUS_UNUSED);
        userCoupon.setValidStartTime(now);
        userCoupon.setValidEndTime(activity.getEndTime());
        return userCoupon;
    }

    private void throwReceiveFailure(ReceiveCouponRequest request, CouponActivity activity) {
        String failReason = resolveReceiveFailureReason(activity.getId());
        recordFailure(request, activity, failReason);
        throw new BizException(400, failReason);
    }

    private String resolveReceiveFailureReason(Long activityId) {
        CouponActivity latestActivity = couponActivityMapper.selectById(activityId);
        if (latestActivity == null) {
            return "活动不存在";
        }

        LocalDateTime now = LocalDateTime.now();
        if (latestActivity.getStatus() == null
                || latestActivity.getStatus() != CouponConstants.ACTIVITY_STATUS_RUNNING) {
            return "活动不可用";
        }
        if (latestActivity.getStartTime().isAfter(now) || latestActivity.getEndTime().isBefore(now)) {
            return "活动不可用";
        }
        if (latestActivity.getRemainingQuantity() == null || latestActivity.getRemainingQuantity() <= 0) {
            return "库存不足";
        }
        return "领取失败";
    }

    private void recordFailure(ReceiveCouponRequest request, CouponActivity activity, String failReason) {
        if (activity == null) {
            return;
        }
        couponReceiveRecordWriteService.saveFailureRecord(
                request.getUserId(),
                request.getActivityId(),
                activity.getTemplateId(),
                failReason);
    }
}
