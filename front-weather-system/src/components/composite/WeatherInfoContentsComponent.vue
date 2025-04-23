<template>
  <div class="weather-info-contents">
    <div class="content-container">
      <NaverMapComponent class="map-component" @location-changed="handleLocationChange" />
      <SideBarInfoComponent class="sidebar-component" :selectedLocation="selectedLocation" />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import SideBarInfoComponent from '@/components/common/SideBarInfoComponent.vue';
import NaverMapComponent from '@/components/features/map/NaverMapComponent.vue';

export default defineComponent({
  name: 'WeatherInfoContentsComponent',
  components: {
    SideBarInfoComponent,
    NaverMapComponent
  },
  setup() {
    const selectedLocation = ref('서울');

    const handleLocationChange = (location: string) => {
      console.log('위치 변경:', location);
      selectedLocation.value = location;
    };

    return {
      selectedLocation,
      handleLocationChange
    };
  }
});
</script>

<style scoped>
.weather-info-contents {
  width: 100%;
  height: 100%;
}

.content-container {
  display: flex;
  gap: 20px;
  margin-top: 20px;
  align-items: flex-start;
}

.map-component {
  flex: 3;
}

.sidebar-component {
  flex: 1;
  margin-top: 10%;
}
</style>
