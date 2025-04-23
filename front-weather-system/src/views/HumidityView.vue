<template>
    <HeadBarComponent />
    <h1>습도 정보</h1>
    <WeatherInfoContentsComponent />
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import HeadBarComponent from '@/components/common/HeadBarComponent.vue';
import WeatherInfoContentsComponent from '@/components/composite/WeatherInfoContentsComponent.vue';
export default defineComponent({
  name: 'HumidityView',
  components: {
    HeadBarComponent,
    WeatherInfoContentsComponent,
 },
  setup() {
    // 컴포넌트 로직 작성

    return {
      // 템플릿에서 사용할 변수나 함수 반환
    }
  }
})
</script>

<style scoped>

</style> 