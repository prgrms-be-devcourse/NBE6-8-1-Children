package com.gridcircle.domain.member.member.controller;

import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.member.member.service.MemberService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberControllerTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("회원가입")
    void t1() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        post("/grid/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "email": "usernew@gmail.com",
                                            "name": "사용자1",
                                            "password": "1234568910",
                                            "address": "서울시 강남구 역삼동",
                                            "role": "ROLE_USER"
                                        }
                                        """.stripIndent())
                )
                .andDo(print());

        Member member = memberService.findByEmail("usernew@gmail.com").get();

        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultCode").value("201-1"))
                .andExpect(jsonPath("$.msg").value("%s님 환영합니다. 회원가입이 완료되었습니다.".formatted(member.getName())))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").value(member.getId()))
                .andExpect(jsonPath("$.data.createdDate").value(Matchers.startsWith(member.getCreatedDate().toString().substring(0, 20))))
                .andExpect(jsonPath("$.data.modifiedDate").value(Matchers.startsWith(member.getModifiedDate().toString().substring(0, 20))))
                .andExpect(jsonPath("$.data.name").value(member.getName()));
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 형식 아님")
    void t2() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        post("/grid/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "email": "invalidemail",
                                "password": "1234567890",
                                "name": "홍길동",
                                "address": "서울시",
                                "role": "ROLE_USER"
                            }
                            """.stripIndent())
                )
                .andDo(print());
        resultActions
                .andExpect(status().isBadRequest()) // 또는 .is4xxClientError()
                .andExpect(jsonPath("$.resultCode").value("400-1")) // 예외 처리 방식에 따라 조정
                .andExpect(jsonPath("$.msg").value(containsString("유효한 이메일 형식이어야 합니다.")));


    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void t3() throws Exception {
        t1();
        ResultActions resultActions = mvc
                .perform(
                        post("/grid/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "email": "usernew@gmail.com",
                                "password": "12345678910",
                                "name": "홍길동",
                                "address": "서울시",
                                "role": "ROLE_USER"
                            }
                            """.stripIndent())
                )
                .andDo(print());
        resultActions
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.resultCode").value("409-1")) // 예외 처리
                .andExpect(jsonPath("$.msg").value("이미 존재하는 이메일입니다."));

    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 10자 미만")
    void t4() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        post("/grid/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "email": "user2@example.com",
                                "password": "1234",
                                "name": "홍길동",
                                "address": "서울시",
                                "role": "ROLE_USER"
                            }
                            """.stripIndent())
                )
                .andDo(print());
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value(containsString("비밀번호는 최소 10자 이상이어야 합니다.")));
    }

    @Test
    @DisplayName("회원가입 실패 - 이름이 2자 미만")
    void t5() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        post("/grid/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "email": "user4@example.com",
                                "password": "1234567890",
                                "name": "길",
                                "address": "서울시",
                                "role": "ROLE_USER"
                            }
                            """.stripIndent())
                )
                .andDo(print());
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value(containsString("이름은 최소 2자 이상이어야 합니다.")));
    }

    @Test
    @DisplayName("회원가입 실패 - 주소가 null")
    void t6() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        post("/grid/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "email": "user5@example.com",
                                            "password": "1234567890",
                                            "name": "홍길동",
                                            "address": null,
                                            "role": "ROLE_USER"
                                        }
                                        """.stripIndent())
                )
                .andDo(print());
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value(containsString("주소는 필수 입력값입니다.")));
    }
}
