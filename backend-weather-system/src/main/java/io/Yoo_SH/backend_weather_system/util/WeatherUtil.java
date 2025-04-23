package io.Yoo_SH.backend_weather_system.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WeatherUtil {
    
    private static List<Map<String, Object>> locations;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    static {
        loadLocations();
    }
    
    /**
     * 위치 정보를 로드하는 메서드
     */
    private static void loadLocations() {
        try {
            Resource resource = new ClassPathResource("static/location.json");
            InputStream inputStream = resource.getInputStream();
            locations = objectMapper.readValue(inputStream, List.class);
            System.out.println("로드된 위치 정보: " + locations.size() + "개");
        } catch (Exception e) {
            System.err.println("위치 정보 로드 실패: " + e.getMessage());
            e.printStackTrace();
            locations = List.of(); // 빈 리스트로 초기화
        }
    }
    
    /**
     * 위도/경도를 기상청 좌표(nx, ny)로 변환하는 메서드
     */
    public static int[] convertToNxNy(double lat, double lon) {
        // 기상청 좌표 변환 로직 (위도/경도 -> nx, ny)
        // 간단한 예시로 대략적인 변환만 수행
        int nx = (int) Math.round(lon - 120);  // 대략적인 변환, 실제로는 더 정확한 계산 필요
        int ny = (int) Math.round(lat - 30);   // 대략적인 변환, 실제로는 더 정확한 계산 필요
        return new int[] {nx, ny};
    }
    
    /**
     * 지역명으로 nx, ny 좌표 찾기
     */
    public static Optional<int[]> findCoordinatesByLocation(String locationName) {
        if (locations == null) {
            loadLocations();
        }
        
        return locations.stream()
                .filter(loc -> locationName.equals(loc.get("name")))
                .findFirst()
                .map(loc -> {
                    double lat = Double.parseDouble(loc.get("lat").toString());
                    double lon = Double.parseDouble(loc.get("lon").toString());
                    return convertToNxNy(lat, lon);
                });
    }
    
    /**
     * 모든 위치 정보 가져오기
     */
    public static List<Map<String, Object>> getAllLocations() {
        if (locations == null) {
            loadLocations();
        }
        return locations;
    }
    
    /**
     * 날씨 타입별 설명 제공
     */
    public static String getWeatherTypeDescription(String type, String value) {
        switch (type) {
            case "POP":
                return "강수확률: " + value + "%";
            case "PTY":
                return "강수형태: " + getPrecipitationTypeDescription(value);
            case "REH":
                return "습도: " + value + "%";
            case "TMP":
                return "온도: " + value + "°C";
            case "UUU":
                return "동서바람성분: " + value + "m/s";
            case "VVV":
                return "남북바람성분: " + value + "m/s";
            case "WSD":
                return "풍속: " + value + "m/s";
            case "SKY":
                return "하늘상태: " + getSkyStateDescription(value);
            default:
                return type + ": " + value;
        }
    }
    
    /**
     * 하늘상태 코드에 대한 설명
     */
    public static String getSkyStateDescription(String code) {
        switch (code) {
            case "1": return "맑음";
            case "3": return "구름많음";
            case "4": return "흐림";
            default: return "알 수 없음 (코드: " + code + ")";
        }
    }
    
    /**
     * 강수형태 코드에 대한 설명
     */
    public static String getPrecipitationTypeDescription(String code) {
        switch (code) {
            case "0": return "없음";
            case "1": return "비";
            case "2": return "비/눈";
            case "3": return "눈";
            case "4": return "소나기";
            default: return "알 수 없음 (코드: " + code + ")";
        }
    }
} 