<template>
  <HeadBarComponent />
  <div class="rain-probability">
    <h1>강수 확률 정보</h1>
    <WeatherInfoContentsComponent />
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import WeatherInfoContentsComponent from '@/components/composite/WeatherInfoContentsComponent.vue';
import HeadBarComponent from '@/components/common/HeadBarComponent.vue';
export default defineComponent({
  name: 'RainProbabilityView',
  components: {
    WeatherInfoContentsComponent,
    HeadBarComponent,
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