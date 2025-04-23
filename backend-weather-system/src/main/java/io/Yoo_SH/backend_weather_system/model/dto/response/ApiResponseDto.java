package io.Yoo_SH.backend_weather_system.model.dto.response;

import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
public class ApiResponseDto<T> {
    private String status; // "success" or "error"
    private Integer statusCode; // 코드
    private String message; // 메시지
    private T data; // 실제 데이터
    private Pagination pagination; // 페이지네이션 정보 (옵션)

    @Data
    @Builder
    public static class Pagination {
        private int currentPage;
        private int totalPages;
        private long totalItems;
    }
}