<template>
  <button @click="fetchWeatherData" class="weather-update-button" :disabled="isLoading">
    {{ isLoading ? '데이터 로딩 중...' : '최신 날씨 데이터' }}
  </button>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import axios from 'axios';
import { getCurrentPath } from '@/utils/common';

export default defineComponent({
  name: 'WeatherUpdateButtonComponent',
  emits: ['weather-updated'],
  setup(props, { emit }) {
    const isLoading = ref(false);

    const fetchWeatherData = async () => {
      const currentPath = getCurrentPath();
      const currentPathToWeatherEndpoint = `${currentPath}/weather`;

      try {
        isLoading.value = true;
        // 현재 날씨 데이터 가져오기
        const response = await axios.get(currentPathToWeatherEndpoint);
        console.log('최신 날씨 데이터 로딩 완료:', response.data);
        
        // 부모 컴포넌트에 데이터가 업데이트되었음을 알림
        emit('weather-updated', response.data);
      } catch (error) {
        console.error('날씨 데이터 로딩 실패:', error);
        alert('날씨 데이터를 불러오는데 실패했습니다.');
      } finally {
        isLoading.value = false;
      }
    };

    return {
      isLoading,
      fetchWeatherData
    };
  }
});
</script>

<style scoped>
.weather-update-button {
  background-color: #4a7bd0;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
  min-width: 180px;
}

.weather-update-button:hover {
  background-color: #3a6ac0;
}

.weather-update-button:active {
  background-color: #2a59b0;
}

.weather-update-button:disabled {
  background-color: #a0a0a0;
  cursor: not-allowed;
}
</style>
