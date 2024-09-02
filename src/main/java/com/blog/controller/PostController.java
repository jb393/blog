package com.blog.controller;

import com.blog.domain.Post;
import com.blog.request.PostCreate;
import com.blog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    public final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        //case1. 저장한 데이터 entity -> respoonse로 응답하기
        //return postService.write(request); Post 리턴
        //case2. 저장한 데이터의 primary_id -> response로 응답하기
        //       client에서는 수신한 id를 글조회 api를 통해서 데이터를 수신 받음
        //case3. 응답 필요 없음 -> 클라이언트에서 모든 post(글) 데이터 context를 잘 관리함
        //Bad Case : 서버에서 반드시 이렇게 할껍니다 fix
        //          -> 서버에서 차라리 유연하게 대응하는게 좋음 -> 코드를 잘 짜야됨 ㅎ
        postService.write(request);
    }

}
