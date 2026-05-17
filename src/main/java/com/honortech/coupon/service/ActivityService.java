package com.honortech.coupon.service;

import com.honortech.coupon.domain.dto.CouponActivityVO;
import com.honortech.coupon.domain.dto.CreateCouponActivityRequest;
import java.util.List;

public interface ActivityService {

    Long createActivity(CreateCouponActivityRequest request);

    List<CouponActivityVO> listActivities();
}
