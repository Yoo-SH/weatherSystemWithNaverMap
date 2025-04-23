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
public class GetAllLocationsDto {
    
    @Builder.Default
    @Schema(description = "페이지 번호", example = "1") 
    private int pageNo = 1;
    
    @Builder.Default
    @Schema(description = "페이지당 항목 수", example = "10") 
    private int numOfRows = 10;
} 