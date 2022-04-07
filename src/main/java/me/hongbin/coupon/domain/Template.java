package me.hongbin.coupon.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long limitByUser;

    private Template() {

    }

    public Template(Long limitByUser) {
        this.limitByUser = limitByUser;
    }

    public Long getId() {
        return id;
    }

    public Long getLimitByUser() {
        return limitByUser;
    }
}
