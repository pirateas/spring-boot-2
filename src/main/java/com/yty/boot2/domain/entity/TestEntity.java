package com.yty.boot2.domain.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yangtianyu
 */
public class TestEntity implements Serializable {

    private Long id;
    private String name;
    private Date createTime;

    public TestEntity() {
    }

    public TestEntity(String name) {
        this.name = name;
        this.createTime = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
