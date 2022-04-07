package me.hongbin.coupon.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Long templateId;

    private Long userId;

    private Coupon() {}

    public Coupon(String title, Long templateId, Long userId) {
        this.title = title;
        this.templateId = templateId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setTest(String asdf) {
        title = asdf;
    }
}
