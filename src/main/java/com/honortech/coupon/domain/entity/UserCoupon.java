package com.honortech.coupon.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("user_coupon")
public class UserCoupon {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String couponCode;
    private Long userId;
    private Long templateId;
    private Long activityId;
    private Long receiveRecordId;
    private Integer status;
    private LocalDateTime validStartTime;
    private LocalDateTime validEndTime;
    private LocalDateTime usedTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
