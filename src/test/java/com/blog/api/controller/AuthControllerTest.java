package com.blog.api.controller;

import com.blog.api.domain.Session;
import com.blog.api.domain.User;
import com.blog.api.repository.SessionRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.request.Login;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName(("로그인 성공"))
    void test () throws Exception {
        //given
        userRepository.save(User.builder()
                .name("aaa")
                .email("aaa@naver.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("aaa@naver.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName(("로그인 성공 후 세션 1개 생성"))
    void test2 () throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("aaa")
                .email("aaa@naver.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("aaa@naver.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertEquals(1L, user.getSessions().size());
    }


    @Test
    @DisplayName(("로그인 성공 후 session 응답"))
    void test3() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("aaa")
                .email("aaa@naver.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("aaa@naver.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지에 접속한다 /foo")
    void test4() throws Exception {
        //given
        User user = User.builder()
                .name("aaa")
                .email("aaa@naver.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);

        Login login = Login.builder()
                .email("aaa@naver.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다.")
    void test5() throws Exception {
        //given
        User user = User.builder()
                .name("aaa")
                .email("aaa@naver.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);

        Login login = Login.builder()
                .email("aaa@naver.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken()+"-a")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}