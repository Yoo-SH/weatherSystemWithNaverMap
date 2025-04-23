package io.Yoo_SH.backend_weather_system.controller.Weather;

import io.Yoo_SH.backend_weather_system.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    void testGetWeatherData() throws Exception {
        // WeatherService 모킹
        when(weatherService.getWeather(anyInt(), anyInt(), eq("POP"))).thenReturn("60");

        mockMvc.perform(get("/api/weather")
                .param("type", "POP")
                .param("location", "서울")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data[0].value").value("60"))
                .andExpect(jsonPath("$.data[0].type").value("POP"))
                .andExpect(jsonPath("$.data[0].location").value("서울"));
    }

    @Test
    void testGetWeatherData_NonExistentLocation() throws Exception {
        mockMvc.perform(get("/api/weather")
                .param("type", "POP")
                .param("location", "존재하지않는지역")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("지역명 '존재하지않는지역'에 대한 좌표를 찾을 수 없습니다")));
    }

    @Test
    void testGetWeatherData_APIError() throws Exception {
        // API 오류 시뮬레이션
        when(weatherService.getWeather(anyInt(), anyInt(), eq("POP"))).thenThrow(new IOException("API 연결 오류"));

        mockMvc.perform(get("/api/weather")
                .param("type", "POP")
                .param("location", "서울")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("API 연결 오류")));
    }

    @Test
    void testGetAllLocations() throws Exception {
        mockMvc.perform(get("/api/weather/locations")
                .param("pageNo", "1")
                .param("numOfRows", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("위치 정보 조회 성공"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.pagination").exists())
                .andExpect(jsonPath("$.pagination.currentPage").value(1));
    }
} 