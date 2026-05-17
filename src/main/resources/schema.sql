USE coupon_db_test;

CREATE TABLE IF NOT EXISTS coupon_template
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '模板ID',
    name             VARCHAR(64) NOT NULL COMMENT '券名称',
    discount_type    TINYINT     NOT NULL COMMENT '券类型 1满减 2折扣 3无门槛',
    threshold_amount BIGINT       DEFAULT 0 COMMENT '使用门槛，单位分',
    discount_amount  BIGINT       DEFAULT 0 COMMENT '优惠金额，单位分',
    discount_rate    DECIMAL(3, 2) DEFAULT NULL COMMENT '折扣率，例如0.80',
    status           TINYINT       DEFAULT 1 COMMENT '状态 1启用 0禁用',
    create_time      DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time      DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT ='优惠券模板表';

CREATE TABLE IF NOT EXISTS coupon_activity
(
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '活动ID',
    activity_name      VARCHAR(64) NOT NULL COMMENT '活动名称',
    template_id        BIGINT      NOT NULL COMMENT '券模板ID',
    total_quantity     INT         NOT NULL COMMENT '总库存',
    remaining_quantity INT         NOT NULL COMMENT '剩余库存',
    start_time         DATETIME    NOT NULL COMMENT '活动开始时间',
    end_time           DATETIME    NOT NULL COMMENT '活动结束时间',
    status             TINYINT  DEFAULT 1 COMMENT '状态 1未开始 2进行中 3结束 0禁用',
    create_time        DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time        DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT ='发券活动表';

CREATE TABLE IF NOT EXISTS coupon_receive_record
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '领取记录ID',
    user_id        BIGINT  NOT NULL COMMENT '用户ID',
    activity_id    BIGINT  NOT NULL COMMENT '活动ID',
    template_id    BIGINT  NOT NULL COMMENT '模板ID',
    receive_status TINYINT NOT NULL COMMENT '领取结果 1成功 0失败',
    fail_reason    VARCHAR(128) DEFAULT NULL COMMENT '失败原因',
    create_time    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间'
) COMMENT ='优惠券领取记录表';

CREATE TABLE IF NOT EXISTS user_coupon
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户券ID',
    coupon_code       VARCHAR(64) NOT NULL COMMENT '券编码',
    user_id           BIGINT      NOT NULL COMMENT '用户ID',
    template_id       BIGINT      NOT NULL COMMENT '券模板ID',
    activity_id       BIGINT      DEFAULT NULL COMMENT '来源活动ID',
    receive_record_id BIGINT      DEFAULT NULL COMMENT '领取记录ID',
    status            TINYINT     DEFAULT 1 COMMENT '状态 1未使用 2已使用 3已过期',
    valid_start_time  DATETIME    NOT NULL COMMENT '生效时间',
    valid_end_time    DATETIME    NOT NULL COMMENT '失效时间',
    used_time         DATETIME DEFAULT NULL COMMENT '使用时间',
    create_time       DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_activity (user_id, activity_id)
) COMMENT ='用户优惠券表';
