package io.Yoo_SH.backend_weather_system.controller.Weather;

import io.Yoo_SH.backend_weather_system.model.dto.request.Weather.GetWeatherDataDto;
import io.Yoo_SH.backend_weather_system.model.dto.request.Weather.GetAllLocationsDto;
import io.Yoo_SH.backend_weather_system.model.dto.response.ApiResponseDto;
import io.Yoo_SH.backend_weather_system.service.WeatherService;
import io.Yoo_SH.backend_weather_system.util.PaginationUtil;
import io.Yoo_SH.backend_weather_system.util.PaginationUtil.PaginationResult;
import io.Yoo_SH.backend_weather_system.util.WeatherUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController implements WeatherControllerDocs {

    private final WeatherService weatherService;

    @Override
    public ResponseEntity<ApiResponseDto> getWeatherData(GetWeatherDataDto requestDto) {
        String type = requestDto.getType();
        String location = requestDto.getLocation();
        
        try {
            // 위치명으로 좌표 검색
            Optional<int[]> coords = WeatherUtil.findCoordinatesByLocation(location);
            if (!coords.isPresent()) {
                log.warn("지역명 '{}'에 대한 좌표를 찾을 수 없습니다.", location);
                return ResponseEntity.status(404).body(
                    ApiResponseDto.builder()
                        .status("error")
                        .statusCode(404)
                        .message("지역명 '" + location + "'에 대한 좌표를 찾을 수 없습니다.")
                        .data(Collections.emptyList())
                        .build()
                );
            }
            
            int[] coordinates = coords.get();
            int nx = coordinates[0];
            int ny = coordinates[1];
            
            log.info("날씨 데이터 조회: 지역={}, 타입={}, 좌표=[{}, {}]", location, type, nx, ny);
            
            // 날씨 데이터 조회
            String weatherValue = weatherService.getWeather(nx, ny, type);
            
            // 날씨 타입에 따른 설명 추가
            String description = WeatherUtil.getWeatherTypeDescription(type, weatherValue);
            
            // Map을 사용하여 날씨 데이터 구성
            Map<String, Object> dataItem = new HashMap<>();
            dataItem.put("value", weatherValue);
            dataItem.put("type", type);
            dataItem.put("location", location);
            
            Map<String, Integer> coordMap = new HashMap<>();
            coordMap.put("nx", nx);
            coordMap.put("ny", ny);
            dataItem.put("coordinates", coordMap);
            
            ApiResponseDto responseDto = ApiResponseDto.builder()
                .status("success")
                .statusCode(200)
                .data(Collections.singletonList(dataItem))
                .message(description)  // 설명을 message로 설정
                .build();
            
            return ResponseEntity.ok(responseDto);
        } catch (IOException e) {
            log.error("API 연결 오류: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(
                ApiResponseDto.builder()
                    .status("error")
                    .statusCode(500)
                    .message("API 연결 오류: " + e.getMessage())
                    .data(Collections.emptyList())
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                ApiResponseDto.builder()
                    .status("error")
                    .statusCode(500)
                    .message(e.getMessage())
                    .data(Collections.emptyList())
                    .build()
            );
        }
    }
    
    @Override
    @GetMapping("/all-locations")
    public ResponseEntity<ApiResponseDto> getAllLocations(GetAllLocationsDto requestDto) {
        log.info("위치 정보 조회: 페이지={}, 페이지당 항목 수={}", requestDto.getPageNo(), requestDto.getNumOfRows());
        
        // 모든 위치 정보 가져오기
        List<Map<String, Object>> allLocations = WeatherUtil.getAllLocations();
        
        // Map을 사용하여 위치 정보 구성 (allLocations를 그대로 활용)
        List<Map<String, Object>> locationItems = allLocations.stream()
            .map(loc -> {
                Map<String, Object> item = new HashMap<>();
                item.put("name", loc.get("name"));
                item.put("lat", Double.parseDouble(loc.get("lat").toString()));
                item.put("lon", Double.parseDouble(loc.get("lon").toString()));
                return item;
            })
            .collect(Collectors.toList());
        
        // 페이지네이션 유틸 사용하여 데이터 페이징 처리
        PaginationResult<Map<String, Object>> paginationResult = PaginationUtil.paginate(
            locationItems, 
            requestDto.getPageNo(), 
            requestDto.getNumOfRows()
        );
        
        // ApiResponseDto.Pagination 객체 생성
        ApiResponseDto.Pagination pagination = ApiResponseDto.Pagination.builder()
            .currentPage(paginationResult.getPageNo())
            .totalItems(paginationResult.getTotalCount())
            .totalPages((int) Math.ceil((double) paginationResult.getTotalCount() / paginationResult.getNumOfRows()))
            .build();

        // ApiResponseDto 생성 및 반환
        ApiResponseDto responseDto = ApiResponseDto.builder()
            .status("success")
            .statusCode(200)
            .message("위치 정보 조회 성공")
            .data(paginationResult.getItems())
            .pagination(pagination)
            .build();
            
        return ResponseEntity.ok(responseDto);
    }
}