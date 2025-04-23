package io.Yoo_SH.backend_weather_system.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        // 필요한 속성 설정
        ReflectionTestUtils.setField(weatherService, "encodingServiceKey", "testEncodingKey");
        ReflectionTestUtils.setField(weatherService, "decodingServiceKey", "testDecodingKey");
        ReflectionTestUtils.setField(weatherService, "baseUrl", "http://test-api.example.com/weather");
        
        // 날짜 및 시간 설정
        weatherService.setDateAndTime("20240504", "0500", "20240504", "0600");
    }

    @Test
    void testSetDateAndTime() {
        // 테스트할 날짜 및 시간
        String baseDate = "20240505";
        String baseTime = "0600";
        String fcstDate = "20240505";
        String fcstTime = "0700";
        
        // 메서드 호출
        weatherService.setDateAndTime(baseDate, baseTime, fcstDate, fcstTime);
        
        // 결과 확인 (private 필드이므로 ReflectionTestUtils 사용)
        assertEquals(baseDate, ReflectionTestUtils.getField(weatherService, "baseDate"));
        assertEquals(baseTime, ReflectionTestUtils.getField(weatherService, "baseTime"));
        assertEquals(fcstDate, ReflectionTestUtils.getField(weatherService, "fcstDate"));
        assertEquals(fcstTime, ReflectionTestUtils.getField(weatherService, "fcstTime"));
    }

    @Test
    void testGetWeatherWithDefaultCategory() throws IOException {
        // getWeather(nx, ny)가 getWeather(nx, ny, "POP")을 호출하는지 확인
        WeatherService spyService = Mockito.spy(weatherService);
        
        // getWeather(nx, ny, category)가 호출될 때 임의의 값 반환하도록 설정
        doReturn("60").when(spyService).getWeather(anyInt(), anyInt(), eq("POP"));
        
        // 메서드 호출
        String result = spyService.getWeather(60, 127);
        
        // 결과 확인
        assertEquals("60", result);
        verify(spyService).getWeather(60, 127, "POP");
    }
    
    // 더 복잡한 테스트는 모킹(Mocking)을 통해 외부 의존성을 대체해야 합니다.
    // 다음은 HttpURLConnection을 모킹하는 방법에 대한 예시입니다.
    // 실제로는 PowerMockito 같은 라이브러리가 필요할 수 있으나, 여기서는 개념만 제시합니다.
} 