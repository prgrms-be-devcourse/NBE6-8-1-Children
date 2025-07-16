package com.gridcircle.domain.member.member.controller;

import com.gridcircle.domain.member.member.dto.MemberWithUserEmailDto;
import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.member.member.service.MemberService;
import com.gridcircle.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grid/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    record MemberJoinReqBody(
            @Email(message = "유효한 이메일 형식이어야 합니다.")
            @NotBlank
            String email,
            @NotBlank
            @Size(min = 10, max = 30, message = "비밀번호는 최소 10자 이상이어야 합니다.")
            String password,
            @NotBlank
            @Size(min = 2, max = 30, message = "이름은 최소 2자 이상이어야 합니다.")
            String name,
            @NotBlank(message = "주소는 필수 입력값입니다.")
            @Size(min = 2, max = 30)
            String address,
            @Size(min = 2, max = 30)
            String role
    ) {
        public MemberJoinReqBody {
            if (role == null || role.isBlank()) {
                role = "ROLE_USER"; // 기본값 설정
            }
        }
    }
    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "가입")
    public RsData<MemberWithUserEmailDto> join(
            @Valid @RequestBody MemberController.MemberJoinReqBody reqBody
    ) {
        Member member = memberService.join(
                reqBody.email(),
                reqBody.name(),
                reqBody.password(),
                reqBody.address(),
                reqBody.role()
        );

        return new RsData<>(
                "201-1",
                "%s님 환영합니다. 회원가입이 완료되었습니다.".formatted(member.getName()),
                new MemberWithUserEmailDto(member)
        );
    }
}
