package com.blog.api.exception;
/**
 * status -> 401
 */
public class Unautohrized extends BlogException {

    private static final String MESSAGE = "인증이 필요합니다.";

    public Unautohrized() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
