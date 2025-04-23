package io.Yoo_SH.backend_weather_system.model.dto.request.Weather;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetWeatherDataDto {
    
    // 기본값 설정
    @Builder.Default
    @Schema(description = "날씨 유형", example = "POP, REH, TMP, UUU")
    private String type = "POP";
    
    @Builder.Default
    @Schema(description = "지역명", example = "서울, 대구, 부산 ...")
    private String location = "서울";
} 