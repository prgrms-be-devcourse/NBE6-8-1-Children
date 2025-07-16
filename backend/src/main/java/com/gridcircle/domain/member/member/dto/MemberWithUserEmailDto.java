package com.gridcircle.domain.member.member.dto;

import com.gridcircle.domain.member.member.entity.Member;

import java.time.LocalDateTime;
public record MemberWithUserEmailDto(
        int id,
        String name,
        String email,
        String address,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate
) {
    public MemberWithUserEmailDto(Member member) {
        this(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getAddress(),
                member.getCreatedDate(),
                member.getModifiedDate()
        );
    }
}
