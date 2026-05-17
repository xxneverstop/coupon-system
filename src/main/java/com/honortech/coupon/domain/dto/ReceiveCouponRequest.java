package com.honortech.coupon.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReceiveCouponRequest {

    @NotNull(message = "userId must not be null")
    private Long userId;

    @NotNull(message = "activityId must not be null")
    private Long activityId;
}
