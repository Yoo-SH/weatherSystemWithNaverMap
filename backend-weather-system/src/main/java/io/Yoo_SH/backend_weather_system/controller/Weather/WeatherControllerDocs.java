package io.Yoo_SH.backend_weather_system.controller.Weather;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.Yoo_SH.backend_weather_system.model.dto.request.Weather.GetWeatherDataDto;
import io.Yoo_SH.backend_weather_system.model.dto.request.Weather.GetAllLocationsDto;
import io.Yoo_SH.backend_weather_system.model.dto.response.ApiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "날씨 API", description = "날씨 정보 조회 관련 API")
public interface WeatherControllerDocs {
    
    @Operation(
        summary = "날씨 데이터 조회",
        description = "지역명과 날씨 유형(POP, REH, TMP 등)을 기반으로 날씨 데이터를 조회합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "조회 성공", 
            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)),
            ref = "SuccessResponse"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "지역명을 찾을 수 없음", 
            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)),
            ref = "ErrorResponse"
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "서버 오류 또는 API 연결 오류", 
            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)),
            ref = "ErrorResponse"
        )
    })
    @GetMapping
    ResponseEntity<ApiResponseDto> getWeatherData(
        @Parameter(description = "날씨 데이터 요청 정보 (지역명, 날씨 유형)", required = true) 
        GetWeatherDataDto requestDto
    );
    
    @Operation(
        summary = "모든 위치 정보 조회",
        description = "시스템에서 제공하는 모든 위치 정보를 페이징하여 조회합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "조회 성공", 
            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)),
            ref = "PaginationResponse"
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "서버 오류", 
            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)),
            ref = "ErrorResponse"
        )
    })
    @GetMapping("/locations")
    ResponseEntity<ApiResponseDto> getAllLocations(
        @Parameter(description = "페이지 정보 (페이지 번호, 페이지당 항목 수)", required = true) 
        GetAllLocationsDto requestDto
    );
}
