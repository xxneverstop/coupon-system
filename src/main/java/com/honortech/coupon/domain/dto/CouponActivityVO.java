package com.honortech.coupon.domain.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CouponActivityVO {

    private Long id;
    private String activityName;
    private Long templateId;
    private Integer totalQuantity;
    private Integer remainingQuantity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
}
