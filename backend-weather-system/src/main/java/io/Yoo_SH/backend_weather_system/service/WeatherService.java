package io.Yoo_SH.backend_weather_system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;

@Service
public class WeatherService {

    @Value("${weather.api.encoding-service-key}")
    private String encodingServiceKey;
    
    @Value("${weather.api.decoding-service-key}")
    private String decodingServiceKey;
    
    @Value("${weather.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    private String baseDate;
    private String baseTime;
    private String fcstDate;
    private String fcstTime;
    
    public WeatherService() {
        this.restTemplate = new RestTemplate();
        this.restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        this.objectMapper = new ObjectMapper();
        
        // 하루 전 날짜를 사용하여 baseDate 설정
        LocalDate yesterday = LocalDate.now().minusDays(1);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        this.baseDate = yesterday.format(dateFormatter);
        
        // 현재 시간을 기준으로 baseTime 설정 (예보 시간은 그대로 유지)
        this.baseTime = "0500"; // 일단 기본값 유지
        this.fcstDate = this.baseDate; // 예보 날짜도 어제로 설정
        this.fcstTime = "0600"; // 기본값 유지
        
        System.out.println("설정된 날짜(어제): " + this.baseDate);
    }
    
    // 날짜 시간 설정 메서드
    public void setDateAndTime(String baseDate, String baseTime, String fcstDate, String fcstTime) {
        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.fcstDate = fcstDate;
        this.fcstTime = fcstTime;
    }
    
    // 날씨 데이터 조회 메서드 - 날씨 유형(category)을 매개변수로 받음
    public String getWeather(int nx, int ny, String category) throws IOException {
        try {
            // 공식 예제 코드를 기반으로 HttpURLConnection 사용
            StringBuilder urlBuilder = new StringBuilder(baseUrl);
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + encodingServiceKey);
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(nx), "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(ny), "UTF-8"));
            
            System.out.println("API 요청 URL: " + urlBuilder.toString());
            
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36");
            
            System.out.println("응답 코드: " + conn.getResponseCode());
            
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            }
            
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            
            String responseString = sb.toString();
            System.out.println("API 응답 시작 부분: " + responseString.substring(0, Math.min(responseString.length(), 200)));
            
            // 이하는 기존 코드와 동일하게 유지 (응답 파싱 부분)
            if (responseString == null || responseString.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "API 응답이 없습니다");
            }
            
            // XML 응답 체크
            if (responseString.trim().startsWith("<")) {
                System.out.println("XML 응답을 받았습니다. 응답 전체 내용: " + responseString);
                
                if (responseString.contains("SERVICE_KEY_IS_NOT_REGISTERED_ERROR")) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                        "등록되지 않은 서비스 키 오류. 서비스 키를 확인하거나 갱신하세요.");
                }
                
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                        "XML 응답을 받았습니다. 데이터 형식 처리에 문제가 있습니다.");
            }
            
            // JSON 파싱
            Map<String, Object> response;
            try {
                response = objectMapper.readValue(responseString, Map.class);
                System.out.println("JSON 파싱 성공. Response: " + response.keySet());
            } catch (JsonProcessingException e) {
                System.out.println("JSON 파싱 실패. 오류: " + e.getMessage() + ", 응답: " + responseString);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                        "JSON 파싱 실패: " + e.getMessage(), e);
            }
            
            Map<String, Object> responseData = (Map<String, Object>) response.get("response");
            if (responseData == null) {
                System.out.println("응답에 'response' 키가 없습니다. 응답: " + response);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "응답 형식이 잘못되었습니다: response 필드 없음");
            }
            
            Map<String, Object> body = (Map<String, Object>) responseData.get("body");
            if (body == null) {
                System.out.println("응답에 'body' 키가 없습니다. 응답: " + responseData);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "응답 형식이 잘못되었습니다: body 필드 없음");
            }
            
            Map<String, Object> items = (Map<String, Object>) body.get("items");
            if (items == null) {
                System.out.println("응답에 'items' 키가 없습니다. 응답: " + body);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "응답 형식이 잘못되었습니다: items 필드 없음");
            }
            
            List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");
            if (itemList == null || itemList.isEmpty()) {
                System.out.println("응답에 'item' 리스트가 없거나 비어 있습니다. 응답: " + items);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "데이터가 없습니다: item 리스트 없음");
            }
            
            // 요청한 카테고리(날씨 유형)에 해당하는 데이터 필터링
            List<Map<String, Object>> filteredItems = itemList.stream()
                    .filter(item -> category.equals(item.get("category")))
                    .collect(Collectors.toList());
            
            System.out.println(category + " 항목 수: " + filteredItems.size());
            
            if (filteredItems.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "데이터가 없습니다: " + category + " 데이터 없음");
            }
            
            return (String) filteredItems.get(0).get("fcstValue");
        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw e;
            }
            System.err.println("날씨 데이터 조회 실패: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "데이터 조회 실패. 오류: " + e.getMessage(), e);
        }
    }
    
    // 기존 메서드는 새 메서드를 호출하도록 변경 (호환성 유지)
    public String getWeather(int nx, int ny) throws IOException {
        return getWeather(nx, ny, "POP");
    }
}