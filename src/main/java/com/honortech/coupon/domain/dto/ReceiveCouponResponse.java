package com.honortech.coupon.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReceiveCouponResponse {

    private Long userCouponId;
    private String couponCode;
}
