package io.Yoo_SH.backend_weather_system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SwaggerConfig {
    
    @Value("${server.port}")
    private String serverPort;
    
    @Bean
    public OpenAPI openAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:" + serverPort);
        localServer.setDescription("로컬 서버");
        
        // 응답 예제 구성
        Components components = new Components();
        
        // 성공 응답 예제
        Map<String, Example> successExamples = new HashMap<>();
        successExamples.put("성공_응답", new Example()
                .summary("성공 응답")
                .description("API 호출 성공 시 응답 형식")
                .value(Map.of(  
                    "status", "success",
                    "statusCode", "2xx",
                    "message", "성공메시지",
                    "data", List.of()
                ))
        );
        
        // 오류 응답 예제
        Map<String, Example> errorExamples = new HashMap<>();
        errorExamples.put("오류_응답", new Example()
                .summary("오류 응답")
                .description("API 호출 실패 시 응답 형식")
                .value(Map.of(
                    "status", "error",
                    "statusCode", "4xx/5xx",
                    "message", "오류 메시지",
                    "data", List.of()
                ))
        );
        
        // 페이지네이션이 포함된 성공 응답 예제
        Map<String, Example> paginationExamples = new HashMap<>();
        paginationExamples.put("페이지네이션_응답", new Example()
                .summary("페이지네이션 응답")
                .description("페이지네이션이 포함된 API 호출 성공 시 응답 형식")
                .value(Map.of(
                    "status", "success",
                    "statusCode", "2xx",
                    "message", "성공 메시지",
                    "data", List.of(),
                    "pagination", Map.of(
                        "currentPage", "x",
                        "totalPages", "x",
                        "totalItems", "x"
                    )
                ))
        );
        // API 응답 컴포넌트 등록
        components.addResponses("SuccessResponse", new ApiResponse()
                .description("성공 응답")
                .content(new Content().addMediaType("application/json", 
                        new MediaType().examples(successExamples))));
        
        components.addResponses("ErrorResponse", new ApiResponse()
                .description("오류 응답")
                .content(new Content().addMediaType("application/json", 
                        new MediaType().examples(errorExamples))));
        
        components.addResponses("PaginationResponse", new ApiResponse()
                .description("페이지네이션 응답")
                .content(new Content().addMediaType("application/json", 
                        new MediaType().examples(paginationExamples))));
        
        return new OpenAPI()
                .servers(List.of(localServer))
                .components(components)
                .info(new Info()
                        .title("날씨 시스템 API 문서")
                        .description("네이버 지도와 기상청 API를 활용한 날씨 시스템 API 문서입니다.\n\n" +
                                "## 공통 응답 형식\n\n" +
                                "### 성공 응답\n" +
                                "```json\n" +
                                "{\n" +
                                "  \"status\": \"success\",\n" +
                                "  \"statusCode\": 200,\n" +
                                "  \"message\": \"성공 메시지\",\n" +
                                "  \"data\": [...]\n" +
                                "}\n" +
                                "```\n\n" +
                                "### 오류 응답\n" +
                                "```json\n" +
                                "{\n" +
                                "  \"status\": \"error\",\n" +
                                "  \"statusCode\": 4xx/5xx,\n" +
                                "  \"message\": \"오류 메시지\",\n" +
                                "  \"data\": []\n" +
                                "}\n" +
                                "```\n\n" +
                                "### 성공 페이지네이션 포함 응답\n" +
                                "```json\n" +
                                "{\n" +
                                "  \"status\": \"success\",\n" +
                                "  \"statusCode\": 2xx,\n" +
                                "  \"message\": \"성공 메시지\",\n" +
                                "  \"data\": [...]\n" +
                                "  \"pagination\": {\n" +
                                "    \"currentPage\": x,\n" +
                                "    \"totalPages\": x,\n" +
                                "    \"totalItems\": x\n" +
                                "  }\n" +
                                "}\n" +
                                "```")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Yoo_SH")
                                .email("example@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
