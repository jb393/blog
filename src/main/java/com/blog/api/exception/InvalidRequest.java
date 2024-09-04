package com.blog.api.exception;

/**
 * status -> 400
 */
public class InvalidRequest extends BlogException {
    //ex) postCreate에서 어노테이션으로만 검증할 수 잆는 추가적인 검증이 필요한 경우

    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequest() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
