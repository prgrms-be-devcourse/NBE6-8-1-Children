package com.gridcircle.domain.member.member.dto;

import com.gridcircle.domain.member.member.entity.Member;

import java.time.LocalDateTime;

public class MemberWithUserEmailDto {
    int id;
    String name;
    String email;

    LocalDateTime createdDate;
    LocalDateTime modifiedDate;

    public MemberWithUserEmailDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.createdDate = member.getCreatedDate();
        this.modifiedDate = member.getModifiedDate();

    }
}
