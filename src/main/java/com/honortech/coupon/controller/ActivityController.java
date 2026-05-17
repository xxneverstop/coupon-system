package com.honortech.coupon.controller;

import com.honortech.coupon.common.response.Result;
import com.honortech.coupon.domain.dto.CouponActivityVO;
import com.honortech.coupon.domain.dto.CreateCouponActivityRequest;
import com.honortech.coupon.service.ActivityService;
import jakarta.validation.Valid;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/create")
    public Result<Long> createActivity(@Valid @RequestBody CreateCouponActivityRequest request) {
        return Result.success(activityService.createActivity(request));
    }

    @GetMapping("/list")
    public Result<List<CouponActivityVO>> listActivities() {
        return Result.success(activityService.listActivities());
    }
}
