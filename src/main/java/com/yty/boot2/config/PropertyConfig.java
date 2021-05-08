package com.yty.boot2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yangtianyu
 */
@Component
@ConfigurationProperties(prefix = "image.path")
public class PropertyConfig {

    /**
     * 头像地址（固定地址+图片名称）
     */
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
