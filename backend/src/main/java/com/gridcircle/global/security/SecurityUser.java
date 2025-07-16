package com.gridcircle.global.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class SecurityUser extends User {
    @Getter
    private int id;

    @Getter
    private String name;

    @Getter
    private String email;

    public SecurityUser(
            int id,
            String email,
            String name,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(email, password, authorities);
        this.id = id;
        this.name = name;
        this.email = email;
    }
}