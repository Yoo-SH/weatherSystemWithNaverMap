package io.Yoo_SH.backend_weather_system.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * API 요청 및 응답을 인터셉트하여 로깅하는 인터셉터
 */
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Autowired
    private APILogger apiLogger;

    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * API 요청이 컨트롤러에 도달하기 전에 실행됩니다.
     * 요청 정보를 로깅하고 처리 시작 시간을 기록합니다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        startTime.set(System.currentTimeMillis());
        apiLogger.logRequest(request, null);
        return true;
    }

    /**
     * 컨트롤러가 요청을 처리한 후, 뷰가 렌더링되기 전에 실행됩니다.
     * 응답 정보와 처리 시간을 로깅합니다.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        long executionTime = System.currentTimeMillis() - startTime.get();
        apiLogger.logResponse(request, response, modelAndView, executionTime);
    }

    /**
     * 요청 처리가 완전히 완료된 후 실행됩니다.
     * ThreadLocal 변수를 정리합니다.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
        startTime.remove(); // ThreadLocal 변수 정리
        if (ex != null) {
            apiLogger.logException(request, ex);
        }
    }
} 