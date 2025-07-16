package com.gridcircle.domain.member.member.entity;


import com.gridcircle.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {
    @Column(unique = true)
    String email;
    String name;
    String password;
    String address;
    String role;
    @Column(unique = true)
    private String apiKey;
    public Member(int id, String email, String name) {
        setId(id);
        this.email = email;
        setName(name);
    }

    public Member(String email, String name, String password, String address, String role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.address = address;
        this.role = role;
        this.apiKey = UUID.randomUUID().toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthoritiesAsStringList()
                .stream()
                .map(auth -> new SimpleGrantedAuthority("ROLE_" + auth))
                .toList();
    }

    private List<String> getAuthoritiesAsStringList() {
        List<String> authorities = new ArrayList<>();

        // 관리자면 ROLE_ADMIN, 아니면 ROLE_USER
        if (isAdmin()) {
            authorities.add("ADMIN");
        } else {
            authorities.add("USER");
        }

        return authorities;
    }
}
