package com.honortech.coupon.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CreateCouponActivityRequest {

    @NotBlank(message = "activityName must not be blank")
    private String activityName;

    @NotNull(message = "templateId must not be null")
    private Long templateId;

    @NotNull(message = "totalQuantity must not be null")
    @Min(value = 1, message = "totalQuantity must be greater than 0")
    private Integer totalQuantity;

    @NotNull(message = "startTime must not be null")
    private LocalDateTime startTime;

    @NotNull(message = "endTime must not be null")
    private LocalDateTime endTime;

    @NotNull(message = "status must not be null")
    private Integer status;
}
