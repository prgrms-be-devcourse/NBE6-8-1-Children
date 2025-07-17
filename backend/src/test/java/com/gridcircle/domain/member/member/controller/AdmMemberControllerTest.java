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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    @DisplayName("user - 회원 목록 다건 조회( 권한 없음 )")
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
    @DisplayName("Admin - 상품 등록 실패 (유효성 검사)")
    @WithUserDetails("admin@gmail.com")
    void t9() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        post("/grid/admin/createProduct")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "productName": "ss",
                                            "description": "",
                                            "productImage": "",
                                            "price": 0,
                                            "stock": -1
                                        }
                                        """.stripIndent())
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AdmMemberController.class))
                .andExpect(handler().methodName("createProduct"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value("400-1"))
                .andExpect(jsonPath("$.msg").value("""
                        description-NotBlank-must not be blank
                        description-Size-상품 설명은 최소 5자 이상 100자 미만이어야 합니다.
                        price-Min-상품 가격은 최소 1000원 이상 입니다.
                        productImage-NotBlank-must not be blank
                        stock-Min-상품 재고는 최소 2개 이상 1000개 미만이어야 합니다.
                        """.stripIndent().trim()));
    }

    @Test
    @DisplayName("User - 상품 등록 실패 (권한 없음)")
    @WithUserDetails("user1@gmail.com")
    void t11() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        post("/grid/admin/createProduct")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "productName": "Coffee",
                                    "description": "커피",
                                    "productImage": "https://example.com/img.jpg",
                                    "price": 1000,
                                    "stock": 10
                                }
                                """.stripIndent())
                )
                .andDo(print());

        resultActions
                .andExpect(status().isForbidden());
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

    @Test
    @DisplayName("Admin - 상품 수정 실패 (존재하지 않는 상품)")
    @WithUserDetails("admin@gmail.com")
    void t12() throws Exception {
        int nonExistentId = 9999;

        ResultActions resultActions = mvc
                .perform(
                        put("/grid/admin/product/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "productName": "new name",
                                "description": "설명",
                                "productImage": "https://img.jpg",
                                "price": 5000,
                                "stock": 10
                            }
                            """))
                .andDo(print());
        resultActions
                .andExpect(status().isBadRequest()) // or 404 depending on exception handler
                .andExpect(jsonPath("$.resultCode").value("400-1"));
    }


    @Test
    @DisplayName("Admin - 상품 목록 다건 조회")
    @WithUserDetails("admin@gmail.com")
    void t6() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/grid/admin/products")
                )
                .andDo(print());

        List<Product> products = productService.findAllProducts();

        resultActions
                .andExpect(handler().handlerType(AdmMemberController.class))
                .andExpect(handler().methodName("getProducts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(products.size()));

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            resultActions
                    .andExpect(jsonPath("$[%d].id".formatted(i)).value(product.getId()))
                    .andExpect(jsonPath("$[%d].createdDate".formatted(i)).value(Matchers.startsWith(product.getCreatedDate().toString().substring(0, 20))))
                    .andExpect(jsonPath("$[%d].modifiedDate".formatted(i)).value(Matchers.startsWith(product.getModifiedDate().toString().substring(0, 20))))
                    .andExpect(jsonPath("$[%d].productName".formatted(i)).value(product.getProductName()))
                    .andExpect(jsonPath("$[%d].description".formatted(i)).value(product.getDescription()))
                    .andExpect(jsonPath("$[%d].productImage".formatted(i)).value(product.getProductImage()))
                    .andExpect(jsonPath("$[%d].price".formatted(i)).value(product.getPrice()))
                    .andExpect(jsonPath("$[%d].stock".formatted(i)).value(product.getStock()));
        }
    }

    @Test
    @DisplayName("Admin - 상품 목록 단건 조회")
    @WithUserDetails("admin@gmail.com")
    void t7() throws Exception {
        int id = 1;
        ResultActions resultActions = mvc
                .perform(
                        get("/grid/admin/product/" + id)
                )
                .andDo(print());

        Product product = productService.getProductById(id);

        resultActions
                .andExpect(handler().handlerType(AdmMemberController.class))
                .andExpect(handler().methodName("getProduct"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.createdDate").value(Matchers.startsWith(product.getCreatedDate().toString().substring(0, 20))))
                .andExpect(jsonPath("$.modifiedDate").value(Matchers.startsWith(product.getModifiedDate().toString().substring(0, 20))))
                .andExpect(jsonPath("$.productName").value(product.getProductName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.productImage").value(product.getProductImage()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.stock").value(product.getStock()));
    }

    @Test
    @DisplayName("Admin - 상품 삭제")
    @WithUserDetails("admin@gmail.com")
    void t8() throws Exception {
        int id = 1;

        ResultActions resultActions = mvc
                .perform(
                        delete("/grid/admin/product/" + id)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AdmMemberController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("%d번 상품이 삭제되었습니다.".formatted(id)));
    }

    @Test
    @DisplayName("Admin - 상품 삭제 실패 (존재하지 않는 상품)")
    @WithUserDetails("admin@gmail.com")
    void t10() throws Exception {
        int id = 9999;

        ResultActions resultActions = mvc
                .perform(
                        delete("/grid/admin/product/" + id)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AdmMemberController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value("400-1"))
                .andExpect(jsonPath("$.msg").value("등록되지 않은 상품입니다."));
    }

}


