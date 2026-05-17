package com.honortech.coupon.domain.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserCouponVO {

    private Long id;
    private String couponCode;
    private Long templateId;
    private Long activityId;
    private Integer status;
    private LocalDateTime validStartTime;
    private LocalDateTime validEndTime;
    private LocalDateTime usedTime;
}
