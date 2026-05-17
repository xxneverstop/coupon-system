package com.honortech.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.honortech.coupon.common.exception.BizException;
import com.honortech.coupon.domain.dto.CouponActivityVO;
import com.honortech.coupon.domain.dto.CreateCouponActivityRequest;
import com.honortech.coupon.domain.entity.CouponActivity;
import com.honortech.coupon.domain.entity.CouponTemplate;
import com.honortech.coupon.mapper.CouponActivityMapper;
import com.honortech.coupon.mapper.CouponTemplateMapper;
import com.honortech.coupon.service.ActivityService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final CouponActivityMapper couponActivityMapper;
    private final CouponTemplateMapper couponTemplateMapper;

    public ActivityServiceImpl(CouponActivityMapper couponActivityMapper, CouponTemplateMapper couponTemplateMapper) {
        this.couponActivityMapper = couponActivityMapper;
        this.couponTemplateMapper = couponTemplateMapper;
    }

    @Override
    public Long createActivity(CreateCouponActivityRequest request) {
        CouponTemplate template = couponTemplateMapper.selectById(request.getTemplateId());
        if (template == null) {
            throw new BizException(400, "券模板不存在");
        }
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new BizException(400, "活动结束时间不能早于开始时间");
        }

        CouponActivity activity = new CouponActivity();
        activity.setActivityName(request.getActivityName());
        activity.setTemplateId(request.getTemplateId());
        activity.setTotalQuantity(request.getTotalQuantity());
        activity.setRemainingQuantity(request.getTotalQuantity());
        activity.setStartTime(request.getStartTime());
        activity.setEndTime(request.getEndTime());
        activity.setStatus(request.getStatus());
        couponActivityMapper.insert(activity);
        return activity.getId();
    }

    @Override
    public List<CouponActivityVO> listActivities() {
        return couponActivityMapper.selectList(new LambdaQueryWrapper<CouponActivity>()
                        .orderByDesc(CouponActivity::getId))
                .stream()
                .map(this::toVO)
                .toList();
    }

    private CouponActivityVO toVO(CouponActivity activity) {
        CouponActivityVO vo = new CouponActivityVO();
        vo.setId(activity.getId());
        vo.setActivityName(activity.getActivityName());
        vo.setTemplateId(activity.getTemplateId());
        vo.setTotalQuantity(activity.getTotalQuantity());
        vo.setRemainingQuantity(activity.getRemainingQuantity());
        vo.setStartTime(activity.getStartTime());
        vo.setEndTime(activity.getEndTime());
        vo.setStatus(activity.getStatus());
        return vo;
    }
}
