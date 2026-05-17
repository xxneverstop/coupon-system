package com.honortech.coupon.service.impl;

import com.honortech.coupon.domain.dto.CreateCouponTemplateRequest;
import com.honortech.coupon.domain.entity.CouponTemplate;
import com.honortech.coupon.mapper.CouponTemplateMapper;
import com.honortech.coupon.service.TemplateService;
import org.springframework.stereotype.Service;

@Service
public class TemplateServiceImpl implements TemplateService {

    private final CouponTemplateMapper couponTemplateMapper;

    public TemplateServiceImpl(CouponTemplateMapper couponTemplateMapper) {
        this.couponTemplateMapper = couponTemplateMapper;
    }

    @Override
    public Long createTemplate(CreateCouponTemplateRequest request) {
        CouponTemplate template = new CouponTemplate();
        template.setName(request.getName());
        template.setDiscountType(request.getDiscountType());
        template.setThresholdAmount(defaultLong(request.getThresholdAmount()));
        template.setDiscountAmount(defaultLong(request.getDiscountAmount()));
        template.setDiscountRate(request.getDiscountRate());
        template.setStatus(request.getStatus());
        couponTemplateMapper.insert(template);
        return template.getId();
    }

    private Long defaultLong(Long value) {
        return value == null ? 0L : value;
    }
}
