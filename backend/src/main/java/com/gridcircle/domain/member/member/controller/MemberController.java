package com.gridcircle.domain.member.member.controller;

import com.gridcircle.domain.member.member.dto.MemberWithUserEmailDto;
import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.member.member.service.MemberService;
import com.gridcircle.global.exception.ServiceException;
import com.gridcircle.global.rq.Rq;
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
@RequestMapping("/grid")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

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

    @PostMapping("/members")
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
    record MemberLoginReqBody(
            @NotBlank
            @Size(min = 2, max = 30)
            String email,
            @NotBlank
            @Size(min = 2, max = 30)
            String password
    ) {
    }
    record MemberLoginResBody(
            MemberWithUserEmailDto item,
            String apiKey,
            String accessToken
    ) {
    }
    @PostMapping("/login")
    @Transactional(readOnly = true)
    @Operation(summary = "로그인")
    public RsData<MemberLoginResBody> login(
            @Valid @RequestBody MemberLoginReqBody reqBody
    ) {
        Member member = memberService.findByEmail(reqBody.email())
                .orElseThrow(() -> new ServiceException("401-1", "존재하지 않는 아이디입니다."));

        memberService.checkPassword(
                member,
                reqBody.password()
        );

        String accessToken = memberService.genAccessToken(member);

        rq.setCookie("apiKey", member.getApiKey());
        rq.setCookie("accessToken", accessToken);

        return new RsData<>(
                "200-1",
                "%s님 환영합니다.".formatted(member.getName()),
                new MemberLoginResBody(
                        new MemberWithUserEmailDto(member),
                        member.getApiKey(),
                        accessToken
                )
        );
    }


    @DeleteMapping("/logout")
    @Operation(summary = "로그아웃")
    public RsData<Void> logout() {
        rq.deleteCookie("apiKey");

        return new RsData<>(
                "200-1",
                "로그아웃 되었습니다."
        );
    }

}
