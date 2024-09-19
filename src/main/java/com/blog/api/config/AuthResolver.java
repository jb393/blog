package com.blog.api.config;

import com.blog.api.config.data.UserSession;
import com.blog.api.domain.Session;
import com.blog.api.exception.Unautohrized;
import com.blog.api.repository.SessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }
    //supportsParameter true일 때 resolveArgument 실행
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        if(servletRequest == null) {
            log.error("servletRequest is null");
            throw new Unautohrized();
        }

        Cookie[] cookies = servletRequest.getCookies();

        if (cookies.length == 0) {
            log.error("no cookie");
            throw new Unautohrized();
        }
        String accessToken = cookies[0].getValue();

        //db 사용자 확인 작업
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(Unautohrized::new);

        return new UserSession(session.getUser().getId());
    }
}
