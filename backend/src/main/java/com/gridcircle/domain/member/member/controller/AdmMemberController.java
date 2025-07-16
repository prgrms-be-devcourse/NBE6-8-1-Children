package com.gridcircle.domain.member.member.controller;

import com.gridcircle.domain.member.member.dto.MemberWithUserEmailDto;
import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.member.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/grid/admin/members")
@RequiredArgsConstructor
public class AdmMemberController {
    private final MemberService memberService;


    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "관리자용 회원 목록 조회")
    public List<MemberWithUserEmailDto> getMembers() {
        List<Member> members = memberService.findAll();

        return members.stream()

                .map(MemberWithUserEmailDto::new)
                .toList();
    }


}
