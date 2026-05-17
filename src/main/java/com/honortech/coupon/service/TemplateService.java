package com.honortech.coupon.service;

import com.honortech.coupon.domain.dto.CreateCouponTemplateRequest;

public interface TemplateService {

    Long createTemplate(CreateCouponTemplateRequest request);
}
