package com.honortech.coupon.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("coupon_receive_record")
public class CouponReceiveRecord {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long activityId;
    private Long templateId;
    private Integer receiveStatus;
    private String failReason;
    private LocalDateTime createTime;
}
