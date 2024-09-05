package com.blog.api.request;

import com.blog.api.exception.InvalidRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter
@ToString
public class PostCreate {

    @NotEmpty(message = "타이틀을 입력해주세요.") //null값도 체크
    private String title;

    @NotEmpty(message = "콘텐츠를 입력해주세요.")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if (title.contains("바보")) {
            throw new InvalidRequest("title", " 제목에 바보를 포함할 수 없습니다.");
        }
    }
}
