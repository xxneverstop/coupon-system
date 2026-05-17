package com.honortech.coupon.controller;

import com.honortech.coupon.common.response.Result;
import com.honortech.coupon.domain.dto.ReceiveCouponRequest;
import com.honortech.coupon.domain.dto.ReceiveCouponResponse;
import com.honortech.coupon.service.CouponService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/receive")
    public Result<ReceiveCouponResponse> receiveCoupon(@Valid @RequestBody ReceiveCouponRequest request) throws InterruptedException {
        return Result.success(couponService.receiveCoupon(request));
    }
}
