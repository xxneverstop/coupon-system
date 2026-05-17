package com.honortech.coupon.service;

import com.honortech.coupon.domain.dto.UserCouponVO;
import java.util.List;

public interface UserCouponService {

    List<UserCouponVO> listUserCoupons(Long userId);
}
