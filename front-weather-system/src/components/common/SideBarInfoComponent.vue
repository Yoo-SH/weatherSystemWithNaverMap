<template>
    <div class="sidebar-container">
      <div class="weather-card">
        <div class="data-box">{{ date }}</div>
        <div class="data-box">{{ time }}</div>
        <div class="data-box">{{ weatherValue }}</div>
        <div class="data-box">{{ location }}</div>
        <div class="weather-type-info">{{ weatherTypeLabel }}</div>
      </div>
    </div>
  </template>
  
  <script lang="ts">
  import { defineComponent, ref, onMounted, watch, computed } from 'vue';
  import axios from 'axios';
  import { useRoute } from 'vue-router';
  
  // 날씨 타입 인터페이스 정의
  interface WeatherType {
    code: string;
    name: string;
    path: string;
  }

  export default defineComponent({
    name: 'SideBarComponent',
    props: {
      selectedLocation: {
        type: String,
        default: '서울'
      }
    },
    setup(props) {
      // 라우터 경로 가져오기
      const route = useRoute();
      
      const date = ref(new Date().toLocaleDateString('ko-KR', { month: 'long', day: 'numeric' }));
      const time = ref(new Date().toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' }));
      const weatherValue = ref('로딩 중...');
      const location = ref(props.selectedLocation);
      const isLoading = ref(false);
      
      // 날씨 타입 정의
      const weatherTypes: WeatherType[] = [
        { code: 'TMP', name: '기온', path: '/temperature' },
        { code: 'POP', name: '강수확률', path: '/rain-probability' },
        { code: 'REH', name: '습도', path: '/humidity' },
        { code: 'UUU', name: '풍속', path: '/wind-speed' }
      ];
      
      // 현재 경로에 기반한 날씨 타입 코드 계산
      const selectedType = computed(() => {
        const currentPath = route.path;
        
        // 경로에 따라 날씨 타입 코드 반환
        const weatherType = weatherTypes.find(type => type.path === currentPath);
        
        // 경로에 해당하는 날씨 타입이 없으면 기본값(TMP)으로 설정
        return weatherType ? weatherType.code : 'TMP';
      });
      
      // 현재 선택된 날씨 타입의 이름
      const weatherTypeLabel = computed(() => {
        const type = weatherTypes.find(t => t.code === selectedType.value);
        return type ? type.name : '';
      });

      // 날씨 정보 가져오기
      const fetchWeatherData = async () => {
        try {
          isLoading.value = true;
          
          console.log(`날씨 데이터 요청: 타입=${selectedType.value}, 위치=${location.value}`);
          
          const response = await axios.get('http://localhost:3000/api/weather', {
            params: {
              type: selectedType.value,
              location: location.value
            }
          });
          
          if (response.data && response.data.status === 'success' && response.data.data && response.data.data.length > 0) {
            const weatherData = response.data.data[0];
            
            // 날씨 값과 설명 업데이트
            const value = weatherData.value;
            
            // 날씨 타입에 따라 단위 표시
            switch (selectedType.value) {
              case 'TMP':
                weatherValue.value = `${value}°C`;
                break;
              case 'POP':
                weatherValue.value = `${value}%`;
                break;
              case 'REH':
                weatherValue.value = `${value}%`;
                break;
              case 'UUU':
                weatherValue.value = `${value}m/s`;
                break;
              default:
                weatherValue.value = value;
            }
            
            console.log('날씨 데이터 가져오기 성공:', weatherData);
          } else {
            console.error('날씨 데이터 형식 오류:', response.data);
            weatherValue.value = '데이터 없음';
          }
        } catch (error) {
          console.error('날씨 데이터 가져오기 실패:', error);
          weatherValue.value = '데이터를 가져올 수 없습니다';
        } finally {
          isLoading.value = false;
        }
      };

      // 시간 업데이트 함수
      const updateTime = () => {
        const now = new Date();
        date.value = now.toLocaleDateString('ko-KR', { month: 'long', day: 'numeric' });
        time.value = now.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' });
      };

      // 위치가 변경될 때 데이터 다시 가져오기
      watch(() => props.selectedLocation, (newLocation) => {
        location.value = newLocation;
        fetchWeatherData();
      });
      
      // 경로(URL)가 변경될 때 데이터 다시 가져오기
      watch(() => route.path, () => {
        fetchWeatherData();
      });

      onMounted(() => {
        // 컴포넌트 마운트 시 날씨 데이터 가져오기
        fetchWeatherData();
        
        // 1분마다 시간 업데이트
        setInterval(updateTime, 60000);
        
        // 초기 시간 설정
        updateTime();
      }); 

      return {
        date,
        time,
        weatherValue,
        location,
        selectedType,
        weatherTypeLabel,
        isLoading
      };
    }
  });
  </script>
  
  <style scoped>
  .sidebar-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
  }
  
  .weather-card {
    background-color: #4a7bd0;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    flex-direction: column;
    gap: 20px;
    width: 300px;
  }
  
  .data-box {
    background-color: white;
    border: 2px solid #c9e165;
    padding: 15px;
    text-align: center;
    font-size: 24px;
    border-radius: 4px;
  }

  .weather-type-info {
    background-color: #c9e165;
    color: #4a7bd0;
    font-weight: bold;
    padding: 10px;
    text-align: center;
    border-radius: 4px;
    font-size: 18px;
  }
  </style>