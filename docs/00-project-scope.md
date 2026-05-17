项目名：FlashCoupon 高并发优惠券营销系统

第一阶段目标：
实现 MySQL 单体基础版，跑通创建券模板、创建活动、用户领取、查询券包。

第一阶段不做：
Redis
RabbitMQ
微服务
前端页面
分库分表
复杂规则引擎

核心表：
coupon_template
coupon_activity
user_coupon
coupon_receive_record

核心接口：
POST /api/template
POST /api/activity
POST /api/coupon/receive
GET /api/user/coupons