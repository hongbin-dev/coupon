package me.hongbin.coupon.core;

public interface ApplicationLock {
    void lock(Long userId);

    void wake(Long userId);
}
