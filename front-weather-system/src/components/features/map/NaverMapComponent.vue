<template>
  <div class="map-container">
    <div class="buttons-container">
      <div class="search-container">
        <input v-model="searchLocation" placeholder="위치를 입력하세요" class="search-input" />
        <button @click="moveToLocation" class="map-button">이동</button>
      </div>
      <WeatherUpdateButtonComponent @click="updateWeather" class="weather-update-button" />
    </div>

    <div id="map" ref="mapRef" style="width: 100%; height: 500px;"></div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from "vue";
import WeatherUpdateButtonComponent from '@/components/base/WeatherUpdateButtonComponent.vue';
import locationData from '@/assets/location.json';

// 위치 데이터 타입 정의
interface LocationData {
  name: string;
  lat: number;
  lon: number;
}

// 타입스크립트에서 window.naver 접근을 위한 선언
declare global {
  interface Window {
    naver: any;
  }
}

export default defineComponent({
  name: 'NaverMapComponent',
  components: {
    WeatherUpdateButtonComponent
  },
  emits: ['location-changed'],
  setup(props, { emit }) {
    const mapRef = ref<HTMLElement | null>(null);
    const naverMap = ref<any>(null);
    const isLoading = ref(false);
    const currentLocation = ref('서울');

    // 위치 정의
    const searchLocation = ref('');
    
    // 검색한 위치로 이동하는 함수
    const moveToLocation = () => {
      if (naverMap.value && window.naver && searchLocation.value) {
        // 검색어 처리 - 공백 제거 및 소문자 변환
        const searchTerm = searchLocation.value.trim();
        
        // 첫 번째: 정확히 일치하는 위치 찾기
        let searchLocationData = (locationData as LocationData[]).find(
          (location) => location.name === searchTerm
        );
        
        // 정확한 일치 결과가 없으면, 포함된 이름 찾기
        if (!searchLocationData) {
          searchLocationData = (locationData as LocationData[]).find(
            (location) => location.name.includes(searchTerm)
          );
        }
        
        if (searchLocationData) {
          const locationLatLng = new window.naver.maps.LatLng(searchLocationData.lat, searchLocationData.lon);
          naverMap.value.setCenter(locationLatLng);
          console.log(`${searchLocationData.name}(으)로 이동했습니다.`);
          
          // 위치 변경 이벤트 발생 - SideBarInfoComponent에서 날씨 데이터를 가져오도록 함
          currentLocation.value = searchLocationData.name;
          emit('location-changed', searchLocationData.name);
        } else {
          console.log('해당 위치를 찾을 수 없습니다:', searchTerm);
          alert(`'${searchTerm}' 위치를 찾을 수 없습니다. 다른 검색어를 입력해주세요.`);
        }
      }
    };
        
    const updateWeather = () => {
      console.log('현재 위치 날씨 데이터 업데이트 요청');
      // 현재 위치의 날씨 정보 갱신을 위해 동일한 위치 이벤트 다시 발생
      emit('location-changed', currentLocation.value);
    };

    onMounted(() => {
      if (window.naver && window.naver.maps) {
        console.log('네이버 지도 초기화 시작');
        naverMap.value = new window.naver.maps.Map(mapRef.value, {
          center: new window.naver.maps.LatLng(37.5666805, 126.9784147),
          zoom: 12,
          mapTypeId: window.naver.maps.MapTypeId.NORMAL,
          zoomControl: true,
          zoomControlOptions: {
            position: window.naver.maps.Position.TOP_RIGHT
          }
        });
        
        console.log('네이버 지도 초기화 완료', naverMap.value);
        
        // 초기 위치 설정 - 서울
        emit('location-changed', '서울');
      } else {
        console.error('네이버 지도 API가 로드되지 않았습니다.');
      }
    });

    return {
      mapRef,
      naverMap,
      updateWeather,
      searchLocation,
      moveToLocation,
      isLoading,
      currentLocation
    };
  }
});
</script>

<style scoped>
.map-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.buttons-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 10px;
}

.search-container {
  display: flex;
  gap: 5px;
}

.search-input {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  min-width: 200px;
}

.map-button {
  background-color: #03cf5d;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s;
}

.map-button:hover {
  background-color: #02bd54;
}

.map-button:disabled {
  background-color: #9e9e9e;
  cursor: not-allowed;
}

#map {
  border: 1px solid #ddd;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}
</style> 