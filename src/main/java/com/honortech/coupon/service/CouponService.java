package com.honortech.coupon.service;

import com.honortech.coupon.domain.dto.ReceiveCouponRequest;
import com.honortech.coupon.domain.dto.ReceiveCouponResponse;

public interface CouponService {

    ReceiveCouponResponse receiveCoupon(ReceiveCouponRequest request) throws InterruptedException;
}
