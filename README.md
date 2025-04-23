# weatherSystemWithNaverMap 기술 명세서

## 📌 프로젝트 개요
네이버 지도 API와 기상청 날씨 API를 활용한 위치 기반 날씨 정보 시스템입니다. 사용자가 지역을 검색하면 해당 지역의 날씨 정보(기온, 강수확률, 습도, 풍속)를 조회할 수 있습니다.

## 🔍 주요 기능
- 네이버 지도를 통한 지역 시각화
- 위치 검색 및 지도 이동
- 다양한 날씨 정보 조회 (기온, 강수확률, 습도, 풍속)
- URL 기반 날씨 타입 전환

## 🛠️ 기술 스택

### 프론트엔드

#### 프레임워크 및 주요 라이브러리
- **Vue.js 3**: 프론트엔드 프레임워크
- **TypeScript**: 타입 안정성을 위한 JavaScript 확장
- **Vue Router**: 클라이언트 사이드 라우팅
- **Axios**: HTTP 요청 처리
- **네이버 지도 API**: 지도 시각화

#### 주요 컴포넌트
- **NaverMapComponent**: 네이버 지도 표시 및 위치 검색 기능
- **SideBarInfoComponent**: 선택된 위치의 날씨 정보 표시
- **WeatherInfoContentsComponent**: 지도와 사이드바 컴포넌트 통합
- **HeadBarComponent**: 헤더 표시

#### 데이터 흐름
- 사용자가 위치 검색 또는 URL 변경
- NaverMapComponent에서 위치 변경 이벤트 발생
- SideBarInfoComponent에서 해당 위치 및 날씨 타입에 대한 API 요청
- 날씨 정보 수신 및 화면 표시

### 백엔드

#### 프레임워크 및 주요 라이브러리
- **Spring Boot**: 백엔드 애플리케이션 개발
- **Spring MVC**: RESTful API 구현
- **Jackson**: JSON 처리
- **Lombok**: 보일러플레이트 코드 감소
- **Swagger/OpenAPI**: API 문서화

#### 주요 컴포넌트
- **WeatherController**: 날씨 정보 API 엔드포인트 제공
- **WeatherService**: 기상청 API 호출 및 데이터 처리
- **WeatherUtil**: 좌표 변환 및 날씨 데이터 변환 유틸리티
- **ApiResponseDto**: 표준화된 API 응답 형식

#### API 엔드포인트
- `GET /api/weather`: 위치와 날씨 타입에 따른 날씨 정보 조회
- `GET /api/weather/locations`: 모든 위치 정보 조회

## 📂 프로젝트 구조

### 프론트엔드 구조
```
front-weather-system/
├── public/                  # 정적 파일
├── src/
│   ├── assets/              # 이미지, 폰트 등 리소스
│   │   └── location.json    # 위치 정보 데이터
│   ├── components/
│   │   ├── base/            # 기본 컴포넌트
│   │   ├── common/          # 공통 컴포넌트
│   │   ├── composite/       # 복합 컴포넌트
│   │   └── features/
│   │       └── map/         # 지도 관련 컴포넌트
│   ├── router/              # Vue Router 설정
│   ├── utils/               # 유틸리티 함수
│   └── views/               # 페이지 뷰
├── package.json             # 의존성 관리
└── vue.config.js            # Vue 설정
```

### 백엔드 구조
```
backend-weather-system/
├── src/main/
│   ├── java/io/Yoo_SH/backend_weather_system/
│   │   ├── config/          # 애플리케이션 설정
│   │   ├── controller/      # API 컨트롤러
│   │   ├── model/           # 데이터 모델
│   │   │   └── dto/         # Data Transfer Objects
│   │   ├── service/         # 비즈니스 로직
│   │   └── util/            # 유틸리티 클래스
│   └── resources/
│       ├── application.properties  # 애플리케이션 설정
│       └── logback-spring.xml      # 로깅 설정
└── pom.xml                         # 의존성 관리
```

## 🔄 데이터 흐름

1. 사용자가 프론트엔드 애플리케이션에서 위치 검색
2. 프론트엔드가 선택된 위치와 현재 URL에 기반한 날씨 타입으로 백엔드 API 호출
3. 백엔드는 위치명을 좌표로 변환하고 기상청 API에 요청
4. 기상청 API 응답을 처리하여 필요한 데이터 추출
5. 가공된 날씨 정보를 프론트엔드로 반환
6. 프론트엔드는 받은 데이터를 사용자 인터페이스에 표시

## 🔧 설정 및 실행 방법

### 프론트엔드
```bash
# 의존성 설치
cd front-weather-system
npm install

# 개발 서버 실행
npm run dev

# 빌드
npm run build
```

### 백엔드
```bash
# 빌드 및 실행
cd backend-weather-system
./mvnw spring-boot:run
```

## 📡 외부 API 연동

### 네이버 지도 API
- 지도 표시, 위치 검색 및 이동 기능 제공
- `NaverMapComponent.vue`에서 구현

### 기상청 API
- 단기예보 API를 사용하여 날씨 정보 조회
- 일자별, 지역별 다양한 날씨 데이터 제공
- `WeatherService.java`에서 HTTP 요청 처리

## 📋 개선 사항 및 확장 가능성
- 실시간 날씨 업데이트 기능
- 사용자 위치 기반 자동 날씨 정보 제공
- 날씨 예측 그래프 추가
- 다국어 지원
- PWA(Progressive Web App) 구현
