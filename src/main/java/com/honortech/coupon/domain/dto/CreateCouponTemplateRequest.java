package com.honortech.coupon.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateCouponTemplateRequest {

    @NotBlank(message = "name must not be blank")
    private String name;

    @NotNull(message = "discountType must not be null")
    private Integer discountType;

    private Long thresholdAmount;
    private Long discountAmount;
    private BigDecimal discountRate;

    @NotNull(message = "status must not be null")
    private Integer status;
}
