package io.Yoo_SH.backend_weather_system.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WeatherUtilTest {

    @Test
    void testGetAllLocations() {
        List<Map<String, Object>> locations = WeatherUtil.getAllLocations();
        assertNotNull(locations);
        assertFalse(locations.isEmpty());
        
        // 서울이 위치 목록에 포함되어 있는지 확인
        boolean hasSeoul = locations.stream()
                .anyMatch(loc -> "서울".equals(loc.get("name")));
        assertTrue(hasSeoul, "위치 목록에 '서울'이 포함되어 있어야 합니다");
    }
    
    @Test
    void testFindCoordinatesByLocation() {
        // 존재하는 위치에 대한 좌표 조회
        Optional<int[]> seoulCoords = WeatherUtil.findCoordinatesByLocation("서울");
        assertTrue(seoulCoords.isPresent(), "'서울'의 좌표를 찾을 수 있어야 합니다");
        
        int[] coords = seoulCoords.get();
        assertEquals(2, coords.length, "좌표는 [nx, ny] 형식의 길이 2 배열이어야 합니다");
        
        // 존재하지 않는 위치에 대한 좌표 조회
        Optional<int[]> nonExistentCoords = WeatherUtil.findCoordinatesByLocation("존재하지않는지역");
        assertFalse(nonExistentCoords.isPresent(), "존재하지 않는 지역의 좌표는 찾을 수 없어야 합니다");
    }
    
    @Test
    void testConvertToNxNy() {
        // 서울의 좌표 (위도, 경도)
        double lat = 37.58374456287517;
        double lon = 126.9838009154029;
        
        int[] coords = WeatherUtil.convertToNxNy(lat, lon);
        assertEquals(2, coords.length, "좌표는 [nx, ny] 형식의 길이 2 배열이어야 합니다");
        
        // 좌표 범위 확인 (정확한 값은 아니지만 범위는 확인 가능)
        assertTrue(coords[0] > 0, "nx는 양수여야 합니다");
        assertTrue(coords[1] > 0, "ny는 양수여야 합니다");
    }
    
    @ParameterizedTest
    @CsvSource({
        "POP, 60, 강수확률: 60%",
        "REH, 80, 습도: 80%",
        "TMP, 25, 온도: 25°C",
        "WSD, 3.5, 풍속: 3.5m/s"
    })
    void testGetWeatherTypeDescription(String type, String value, String expected) {
        String description = WeatherUtil.getWeatherTypeDescription(type, value);
        assertEquals(expected, description);
    }
    
    @ParameterizedTest
    @CsvSource({
        "1, 맑음",
        "3, 구름많음",
        "4, 흐림",
        "9, 알 수 없음 (코드: 9)"
    })
    void testGetSkyStateDescription(String code, String expected) {
        String description = WeatherUtil.getSkyStateDescription(code);
        assertEquals(expected, description);
    }
    
    @ParameterizedTest
    @CsvSource({
        "0, 없음",
        "1, 비",
        "2, 비/눈",
        "3, 눈",
        "4, 소나기",
        "9, 알 수 없음 (코드: 9)"
    })
    void testGetPrecipitationTypeDescription(String code, String expected) {
        String description = WeatherUtil.getPrecipitationTypeDescription(code);
        assertEquals(expected, description);
    }
} 