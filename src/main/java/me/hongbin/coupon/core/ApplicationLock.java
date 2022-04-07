package me.hongbin.coupon.core;

public interface ApplicationLock {
    boolean isLocked(Long userId);

    void lock(Long userId);

    void wake(Long userId);
}
