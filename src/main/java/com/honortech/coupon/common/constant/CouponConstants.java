package com.honortech.coupon.common.constant;

public final class CouponConstants {

    private CouponConstants() {
    }

    public static final int ACTIVITY_STATUS_DISABLED = 0;
    public static final int ACTIVITY_STATUS_NOT_STARTED = 1;
    public static final int ACTIVITY_STATUS_RUNNING = 2;
    public static final int ACTIVITY_STATUS_ENDED = 3;

    public static final int USER_COUPON_STATUS_UNUSED = 1;
    public static final int USER_COUPON_STATUS_USED = 2;
    public static final int USER_COUPON_STATUS_EXPIRED = 3;

    public static final int RECEIVE_STATUS_FAILED = 0;
    public static final int RECEIVE_STATUS_SUCCESS = 1;
}
