package com.honortech.coupon.controller;

import com.honortech.coupon.common.response.Result;
import com.honortech.coupon.domain.dto.CreateCouponTemplateRequest;
import com.honortech.coupon.service.TemplateService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/template")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping("/create")
    public Result<Long> createTemplate(@Valid @RequestBody CreateCouponTemplateRequest request) {
        log.info("createTemplate...");
        return Result.success(templateService.createTemplate(request));
    }
}
