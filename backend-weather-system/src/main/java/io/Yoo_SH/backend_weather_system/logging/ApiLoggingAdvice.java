package io.Yoo_SH.backend_weather_system.logging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * API 요청 및 응답 본문을 로깅하는 Advice 클래스
 */
@ControllerAdvice
public class ApiLoggingAdvice implements RequestBodyAdvice, ResponseBodyAdvice<Object> {

    @Autowired
    private APILogger apiLogger;

    private static final ThreadLocal<Object> requestBody = new ThreadLocal<>();

    // RequestBodyAdvice 메서드 구현
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                           Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, 
                                          Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, 
                               Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        requestBody.set(body);
        
        if (inputMessage instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) inputMessage;
            apiLogger.logRequest(servletRequest.getServletRequest(), body);
        }
        
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                 Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    // ResponseBodyAdvice 메서드 구현
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                 Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                 ServerHttpRequest request, ServerHttpResponse response) {
        if (request instanceof ServletServerHttpRequest && response instanceof ServletServerHttpResponse) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) response;
            
            long executionTime = 0; // 실제 시간 측정은 인터셉터에서 처리
            apiLogger.logResponse(servletRequest.getServletRequest(), servletResponse.getServletResponse(), body, executionTime);
        }
        
        // ThreadLocal 변수 정리
        requestBody.remove();
        
        return body;
    }
} 