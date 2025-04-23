<template>
  <HeadBarComponent />
  <div class="about">
    <h1>기온 정보</h1>
    <WeatherInfoContentsComponent />
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import WeatherInfoContentsComponent from '@/components/composite/WeatherInfoContentsComponent.vue';
import HeadBarComponent from '@/components/common/HeadBarComponent.vue';
export default defineComponent({
  name: 'TemperatureView',
  components: {
    WeatherInfoContentsComponent,
    HeadBarComponent,
  },
});
</script>
