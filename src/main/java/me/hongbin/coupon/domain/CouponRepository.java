package me.hongbin.coupon.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Long countByUserIdAndTemplateId(Long userId, Long templateId);
}
