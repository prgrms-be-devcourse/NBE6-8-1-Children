package com.gridcircle.domain.member.member.entity;


import com.gridcircle.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

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


    public Member(String email, String name, String password, String address, String role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.address = address;
        this.role = role;
    }
}
