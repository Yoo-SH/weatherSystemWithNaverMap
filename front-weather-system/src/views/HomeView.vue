<template>
  <HeadBarComponent />
  <div class="home">
    <div class="weather-title">
      <h2>날씨 정보 시스템</h2>
    </div>

    <div class="description-card">
      <h3><i class="fas fa-cloud-sun"></i> 날씨 정보 시스템 소개</h3>
      <p class="intro-text">
        이 시스템은 네이버 지도 API와 기상청 API를 활용하여 사용자에게 지역별 날씨 정보를 제공합니다.
        정확한 위치 기반으로 강수확률, 습도, 기온 등 다양한 날씨 데이터를 시각적으로 확인할 수 있습니다.
      </p>

      <h4><i class="fas fa-map-marked-alt"></i> 사용 방법</h4>
      <div class="usage-guide">
        <ul class="feature-list">
          <li><i class="fas fa-check-circle"></i> 위치 선택: 왼쪽 메뉴에서 원하는 지역을 클릭하세요.</li>
          <li>
            <i class="fas fa-check-circle"></i> 날씨 유형 선택: 
            <span class="highlight-btn">기온</span>,
            <span class="highlight-btn">강수확률</span>, 
            <span class="highlight-btn">습도</span>, 
            <span class="highlight-btn">풍속</span>
            중 하나를 선택하세요.
          </li>
          <li><i class="fas fa-check-circle"></i> 사이드바의 수치로 해당 지역의 날씨 정보를 확인하실 수 있습니다.</li>
          <li><i class="fas fa-check-circle"></i> 지역 목록에서 검색하거나 지도를 직접 확대/축소하여 특정 지역을 탐색할 수 있습니다.</li>
        </ul>
        
        <div class="weather-tip">
          <p><i class="fas fa-lightbulb"></i> <strong>알아두세요:</strong> 날씨 정보는 기상청에서 제공하는 최신 데이터를 기반으로 합니다.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted } from 'vue';
import HeadBarComponent from '@/components/common/HeadBarComponent.vue';
import WeatherInfoContentsComponent from '@/components/composite/WeatherInfoContentsComponent.vue';

export default defineComponent({
  name: 'HomeView',
  components: {
    HeadBarComponent,
  },
  setup() {
    onMounted(() => {
      console.log('HomeView mounted');
    });
  }
});
</script>

<style scoped>
.home {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.weather-title {
  text-align: center;
  margin-bottom: 20px;
}

.weather-title h2 {
  color: #2c3e50;
  font-size: 28px;
}

.description-card {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.description-card h3 {
  color: #3498db;
  margin-bottom: 15px;
  font-size: 22px;
}

.description-card h4 {
  color: #2c3e50;
  margin: 15px 0 10px;
  font-size: 18px;
}

.intro-text {
  color: #555;
  line-height: 1.6;
  margin-bottom: 15px;
}

.usage-guide {
  padding: 10px;
  background-color: #ffffff;
  border-radius: 6px;
}

.feature-list {
  list-style-type: none;
  padding-left: 0;
}

.feature-list li {
  margin-bottom: 10px;
  padding-left: 5px;
  position: relative;
}

.feature-list li i {
  color: #27ae60;
  margin-right: 8px;
}

.highlight-btn {
  display: inline-block;
  padding: 3px 8px;
  background-color: #e3f2fd;
  color: #1976d2;
  border-radius: 4px;
  margin: 0 2px;
  font-weight: 500;
}

.weather-tip {
  margin-top: 15px;
  padding: 10px;
  background-color: #fff8e1;
  border-left: 4px solid #ffd54f;
  border-radius: 4px;
}

.weather-tip i {
  color: #f39c12;
}

.map-container {
  width: 100%;
  height: 600px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

/* 모바일 반응형 스타일 */
@media (max-width: 768px) {
  .home {
    padding: 10px;
  }
  
  .description-card {
    padding: 15px;
  }
  
  .map-container {
    height: 400px;
  }
}
</style>

