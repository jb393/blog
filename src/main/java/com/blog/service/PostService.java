package com.blog.service;

import com.blog.domain.Post;
import com.blog.repository.PostRepository;
import com.blog.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


    public void write(PostCreate postCreate) {
        //postCreate -> Entity 형태(Post)로 변환

        Post post = Post.builder().
                title(postCreate.getTitle()).
                content(postCreate.getContent()).
                build();

        //return postRepository.save(post);
        postRepository.save(post);
    }
}
