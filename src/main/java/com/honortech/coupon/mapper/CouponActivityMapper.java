package com.honortech.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honortech.coupon.domain.entity.CouponActivity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface CouponActivityMapper extends BaseMapper<CouponActivity> {

    @Update("""
            update coupon_activity
            set remaining_quantity = remaining_quantity - 1
            where id = #{activityId}
              and remaining_quantity > 0
              and status = 2
              and start_time <= now()
              and end_time >= now()
            """)
    int deductStockIfAvailable(@Param("activityId") Long activityId);
}
