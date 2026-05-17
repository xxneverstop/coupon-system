package com.honortech.coupon.service;

public interface CouponReceiveRecordWriteService {

    void saveFailureRecord(Long userId, Long activityId, Long templateId, String failReason);
}
