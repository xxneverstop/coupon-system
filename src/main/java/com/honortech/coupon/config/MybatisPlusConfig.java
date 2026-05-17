package com.honortech.coupon.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.honortech.coupon.mapper")
public class MybatisPlusConfig {
}
