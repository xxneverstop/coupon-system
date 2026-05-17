package com.honortech.coupon.service.impl;

import com.honortech.coupon.common.constant.CouponConstants;
import com.honortech.coupon.domain.entity.CouponReceiveRecord;
import com.honortech.coupon.mapper.CouponReceiveRecordMapper;
import com.honortech.coupon.service.CouponReceiveRecordWriteService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponReceiveRecordWriteServiceImpl implements CouponReceiveRecordWriteService {

    private final CouponReceiveRecordMapper couponReceiveRecordMapper;

    public CouponReceiveRecordWriteServiceImpl(CouponReceiveRecordMapper couponReceiveRecordMapper) {
        this.couponReceiveRecordMapper = couponReceiveRecordMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailureRecord(Long userId, Long activityId, Long templateId, String failReason) {
        CouponReceiveRecord receiveRecord = new CouponReceiveRecord();
        receiveRecord.setUserId(userId);
        receiveRecord.setActivityId(activityId);
        receiveRecord.setTemplateId(templateId);
        receiveRecord.setReceiveStatus(CouponConstants.RECEIVE_STATUS_FAILED);
        receiveRecord.setFailReason(failReason);
        receiveRecord.setCreateTime(LocalDateTime.now());
        couponReceiveRecordMapper.insert(receiveRecord);
    }
}
