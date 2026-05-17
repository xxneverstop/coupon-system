# FlashCoupon MVP

## 重复领取说明

当前 MVP 阶段，用户重复领取优惠券先靠业务查询控制：

- 在领券时先查询 `user_coupon`，判断同一 `user_id + activity_id` 是否已经存在记录
- 如果已存在，则直接返回“用户已领取该活动优惠券”

这只是最小可运行方案，后续为了真正兜底并发重复领取，应该在数据库层增加唯一索引：

- `unique key uk_user_activity (user_id, activity_id)`

当前阶段暂不修改表结构，先保持代码简单清晰。
