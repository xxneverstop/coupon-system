package com.honortech.coupon.controller;

import com.honortech.coupon.common.response.Result;
import com.honortech.coupon.domain.dto.UserCouponVO;
import com.honortech.coupon.service.UserCouponService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/user/coupons")
public class UserCouponController {

    private final UserCouponService userCouponService;

    public UserCouponController(UserCouponService userCouponService) {
        this.userCouponService = userCouponService;
    }

    @GetMapping("list")
    public Result<List<UserCouponVO>> listUserCoupons(@RequestParam @NotNull(message = "userId must not be null") Long userId) {
        return Result.success(userCouponService.listUserCoupons(userId));
    }
}
