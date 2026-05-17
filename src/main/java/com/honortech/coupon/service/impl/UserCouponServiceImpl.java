package com.honortech.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.honortech.coupon.domain.dto.UserCouponVO;
import com.honortech.coupon.domain.entity.UserCoupon;
import com.honortech.coupon.mapper.UserCouponMapper;
import com.honortech.coupon.service.UserCouponService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserCouponServiceImpl implements UserCouponService {

    private final UserCouponMapper userCouponMapper;

    public UserCouponServiceImpl(UserCouponMapper userCouponMapper) {
        this.userCouponMapper = userCouponMapper;
    }

    @Override
    public List<UserCouponVO> listUserCoupons(Long userId) {
        return userCouponMapper.selectList(new LambdaQueryWrapper<UserCoupon>()
                        .eq(UserCoupon::getUserId, userId)
                        .orderByDesc(UserCoupon::getId))
                .stream()
                .map(this::toVO)
                .toList();
    }

    private UserCouponVO toVO(UserCoupon userCoupon) {
        UserCouponVO vo = new UserCouponVO();
        vo.setId(userCoupon.getId());
        vo.setCouponCode(userCoupon.getCouponCode());
        vo.setTemplateId(userCoupon.getTemplateId());
        vo.setActivityId(userCoupon.getActivityId());
        vo.setStatus(userCoupon.getStatus());
        vo.setValidStartTime(userCoupon.getValidStartTime());
        vo.setValidEndTime(userCoupon.getValidEndTime());
        vo.setUsedTime(userCoupon.getUsedTime());
        return vo;
    }
}
