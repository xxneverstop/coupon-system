package com.honortech.coupon.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("coupon_template")
public class CouponTemplate {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer discountType;
    private Long thresholdAmount;
    private Long discountAmount;
    private BigDecimal discountRate;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
