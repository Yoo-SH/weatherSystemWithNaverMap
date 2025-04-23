package io.Yoo_SH.backend_weather_system.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * API 요청 및 응답을 로깅하기 위한 유틸리티 클래스
 */
@Component
public class APILogger {

    private static final Logger logger = LoggerFactory.getLogger(APILogger.class);

    /**
     * API 요청 정보를 로깅합니다.
     *
     * @param request HTTP 요청 객체
     * @param body 요청 본문 (있는 경우)
     */
    public void logRequest(HttpServletRequest request, Object body) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n===== API 요청 로그 =====\n");
        logMessage.append("요청 URI: ").append(request.getRequestURI()).append("\n");
        logMessage.append("요청 메서드: ").append(request.getMethod()).append("\n");
        logMessage.append("클라이언트 IP: ").append(request.getRemoteAddr()).append("\n");
        
        // 요청 파라미터 로깅
        Map<String, String[]> paramMap = request.getParameterMap();
        if (!paramMap.isEmpty()) {
            logMessage.append("요청 파라미터: ");
            String params = paramMap.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
                    .collect(Collectors.joining(", "));
            logMessage.append(params).append("\n");
        }
        
        // 요청 헤더 로깅 
        logMessage.append("요청 헤더:\n");
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            logMessage.append("    ").append(headerName).append(": ")
                    .append(request.getHeader(headerName)).append("\n");
        });
        
        // 요청 본문 로깅 (있는 경우)
        if (body != null) {
            logMessage.append("요청 본문: ").append(body).append("\n");
        }
        
        logMessage.append("=======================");
        logger.info(logMessage.toString());
    }

    /**
     * API 응답 정보를 로깅합니다.
     *
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param body 응답 본문
     * @param executionTime API 처리 시간 (밀리초)
     */
    public void logResponse(HttpServletRequest request, HttpServletResponse response, 
                            Object body, long executionTime) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n===== API 응답 로그 =====\n");
        logMessage.append("요청 URI: ").append(request.getRequestURI()).append("\n");
        logMessage.append("요청 메서드: ").append(request.getMethod()).append("\n");
        logMessage.append("응답 상태: ").append(response.getStatus()).append("\n");
        logMessage.append("실행 시간: ").append(executionTime).append("ms\n");
        
        // 응답 헤더 로깅
        logMessage.append("응답 헤더:\n");
        response.getHeaderNames().forEach(headerName -> {
            logMessage.append("    ").append(headerName).append(": ")
                    .append(response.getHeader(headerName)).append("\n");
        });
        
        // 응답 본문 로깅 (있는 경우)
        if (body != null) {
            logMessage.append("응답 본문: ").append(body).append("\n");
        }
        
        logMessage.append("=======================");
        logger.info(logMessage.toString());
    }

    /**
     * 예외 정보를 로깅합니다.
     *
     * @param request HTTP 요청 객체
     * @param ex 발생한 예외
     */
    public void logException(HttpServletRequest request, Exception ex) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n===== API 예외 로그 =====\n");
        logMessage.append("요청 URI: ").append(request.getRequestURI()).append("\n");
        logMessage.append("요청 메서드: ").append(request.getMethod()).append("\n");
        logMessage.append("클라이언트 IP: ").append(request.getRemoteAddr()).append("\n");
        logMessage.append("예외 메시지: ").append(ex.getMessage()).append("\n");
        logMessage.append("=======================");
        
        logger.error(logMessage.toString(), ex);
    }
} 