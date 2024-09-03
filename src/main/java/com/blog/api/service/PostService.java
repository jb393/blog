package com.blog.api.service;

import com.blog.api.domain.Post;
import com.blog.api.domain.PostEditor;
import com.blog.api.repository.PostRepository;
import com.blog.api.request.PostCreate;
import com.blog.api.request.PostEdit;
import com.blog.api.request.PostSearch;
import com.blog.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();
        //클라이언트에서 수정할 데이터 + 수정안할 데이터도 넘길 때
        PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();
        post.edit(postEditor);
        //클라이언트에서 수정안할 데이터는 넘기지 않을 때
//        if (postEdit.getTitle() != null) {
//            editorBuilder.title(postEdit.getTitle());
//        }
//        if (postEdit.getContent() != null) {
//            editorBuilder.content(postEdit.getContent());
//        }
//        post.edit(editorBuilder.build());
    }



}
