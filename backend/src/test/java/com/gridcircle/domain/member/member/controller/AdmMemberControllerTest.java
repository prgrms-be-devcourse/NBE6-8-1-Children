package com.gridcircle.domain.member.member.controller;


import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.member.member.service.MemberService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AdmMemberControllerTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Admin - 회원 목록 다건 조회")
    @WithUserDetails("admin@gmail.com")
    void t1() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/grid/admin/members")
                )
                .andDo(print());

        List<Member> members = memberService.findAll();

        resultActions
                .andExpect(handler().handlerType(AdmMemberController.class))
                .andExpect(handler().methodName("getMembers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(members.size()));

        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            resultActions
                    .andExpect(jsonPath("$[%d].id".formatted(i)).value(member.getId()))
                    .andExpect(jsonPath("$[%d].createdDate".formatted(i)).value(Matchers.startsWith(member.getCreatedDate().toString().substring(0, 20))))
                    .andExpect(jsonPath("$[%d].modifiedDate".formatted(i)).value(Matchers.startsWith(member.getModifiedDate().toString().substring(0, 20))))
                    .andExpect(jsonPath("$[%d].name".formatted(i)).value(member.getName()))
                    .andExpect(jsonPath("$[%d].email".formatted(i)).value(member.getEmail()))
                    .andExpect(jsonPath("$[%d].address".formatted(i)).value(member.getAddress()));
        }
    }


    @Test
    @DisplayName("Admin - 회원 목록 단건 조회")
    @WithUserDetails("admin@gmail.com")
    void t2() throws Exception {
        int id =1;
        ResultActions resultActions = mvc
                .perform(
                        get("/grid/admin/member/" + id)
                )
                .andDo(print());

        Member member = memberService.findById(id).get();

        resultActions
                .andExpect(handler().handlerType(AdmMemberController.class))
                .andExpect(handler().methodName("getMember"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(member.getId()))
                .andExpect(jsonPath("$.createdDate").value(Matchers.startsWith(member.getCreatedDate().toString().substring(0, 20))))
                .andExpect(jsonPath("$.modifiedDate").value(Matchers.startsWith(member.getModifiedDate().toString().substring(0, 20))))
                .andExpect(jsonPath("$.name").value(member.getName()))
                .andExpect(jsonPath("$.email").value(member.getEmail()))
                .andExpect(jsonPath("$.address").value(member.getAddress()));


    }

    @Test
    @DisplayName("user - 회원 목록 다건 조회")
    @WithUserDetails("admin1@gmail.com")
    void t3() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/grid/admin/members")
                )
                .andDo(print());

        resultActions
                .andExpect(status().isForbidden());
    }
}


