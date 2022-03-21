package com.rainbow.auth.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author yanzhihao
 * @since 2022/3/21
 */
public class CustomGrantedAuthority implements GrantedAuthority {

    private String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
