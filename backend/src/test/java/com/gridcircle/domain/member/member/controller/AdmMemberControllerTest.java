package com.gridcircle.domain.member.member.controller;


import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.member.member.service.MemberService;
import com.gridcircle.domain.product.product.entity.Product;
import com.gridcircle.domain.product.product.service.ProductService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    @Autowired
    private ProductService productService;


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

    @Test
    @DisplayName("Admin - 상품 등록")
    @WithUserDetails("admin@gmail.com")
    void t4() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        post("/grid/admin/createProduct")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "productName": "starbucks",
                                            "description": "스타벅스 커피",
                                            "productImage": "https://example.com/starbucks.jpg",
                                            "price": 5000,
                                            "stock": 100
                                        }
                                        """.stripIndent())
                )
                .andDo(print());

        Product product = productService.findByProductName("starbucks");

        resultActions
                .andExpect(handler().handlerType(AdmMemberController.class))
                .andExpect(handler().methodName("createProduct"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultCode").value("201-1"))
                .andExpect(jsonPath("$.msg").value("%s 상품이 등록되었습니다.".formatted(product.getProductName())))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").value(product.getId()))
                .andExpect(jsonPath("$.data.createdDate").value(Matchers.startsWith(product.getCreatedDate().toString().substring(0, 20))))
                .andExpect(jsonPath("$.data.modifiedDate").value(Matchers.startsWith(product.getModifiedDate().toString().substring(0, 20))))
                .andExpect(jsonPath("$.data.productName").value(product.getProductName()))
                .andExpect(jsonPath("$.data.description").value(product.getDescription()))
                .andExpect(jsonPath("$.data.price").value(product.getPrice()));
    }


    @Test
    @DisplayName("Admin - 상품 수정")
    @WithUserDetails("admin@gmail.com")
    void t5() throws Exception {
        int id = 1;

        ResultActions resultActions = mvc
                .perform(
                        put("/grid/admin/product/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "productName": "new starbucks",
                                            "description": "스타벅스 커피",
                                            "productImage": "https://example.com/starbucks.jpg",
                                            "price": 5000,
                                            "stock": 10
                                        }
                                        """)
                )
                .andDo(print());

        Product product = productService.findByProductName("new starbucks");

        resultActions
                .andExpect(handler().handlerType(AdmMemberController.class))
                .andExpect(handler().methodName("updateProduct"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("%d번 상품이 수정되었습니다.".formatted(id)))
                .andExpect(jsonPath("$.data.productName").value(product.getProductName()))
                .andExpect(jsonPath("$.data.description").value(product.getDescription()));
    }

}


