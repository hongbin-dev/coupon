package me.hongbin.coupon.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import me.hongbin.coupon.core.ApplicationCounter;
import me.hongbin.coupon.core.ApplicationLock;
import me.hongbin.coupon.domain.Coupon;
import me.hongbin.coupon.domain.CouponRepository;
import me.hongbin.coupon.domain.Template;
import me.hongbin.coupon.domain.TemplateRepository;

@Service
public class CouponIssueService {

    private final CouponRepository couponRepository;
    private final TemplateRepository templateRepository;
    private final ApplicationLock applicationLock;
    private final ApplicationCounter applicationCounter;

    public CouponIssueService(
        CouponRepository couponRepository,
        TemplateRepository templateRepository,
        ApplicationLock applicationLock,
        ApplicationCounter applicationCounter
    ) {
        this.couponRepository = couponRepository;
        this.templateRepository = templateRepository;
        this.applicationLock = applicationLock;
        this.applicationCounter = applicationCounter;
    }

    public void publishWithLock(Long userId, Long templateId) {
        applicationLock.lock(userId);

        Long limitByUser = findLimitByUser(templateId);

        long count = couponRepository.countByUserIdAndTemplateId(userId, templateId);

        if (Objects.nonNull(limitByUser) && limitByUser <= count) {
            throw new IllegalArgumentException("이미 최대수량을 넘게 발급받으셨습니다.");
        }

        Coupon coupon = new Coupon("쿠폰", templateId, userId);
        couponRepository.save(coupon);

        applicationLock.wake(userId);
    }

    public void publishWithCounter(Long userId, Long templateId) {
        Long limitByUser = findLimitByUser(templateId);

        long count = applicationCounter.increment(userId);

        if (Objects.nonNull(limitByUser) && limitByUser < count) {
            throw new IllegalArgumentException("이미 최대수량을 넘게 발급받으셨습니다.");
        }

        Coupon coupon = new Coupon("쿠폰", templateId, userId);
        couponRepository.save(coupon); // TODO 실패했을 때
    }

    private Long findLimitByUser(Long templateId) {
        Template template = templateRepository.findById(templateId).stream()
            .findAny()
            .orElseThrow(IllegalArgumentException::new);

        return template.getLimitByUser();
    }
}
