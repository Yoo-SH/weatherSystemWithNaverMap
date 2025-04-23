import { createApp } from 'vue'
import { createNaverMap } from "vue3-naver-maps";
import App from './App.vue'
import router from './router'

const app = createApp(App);

console.log('네이버 지도 초기화 시작');

// 네이버 지도 설정
app.use(createNaverMap, {
  clientId: "w6gya09f2f", // 네이버 클라우드 플랫폼에서 발급받은 클라이언트 ID
  category: "ncp", // 기본값: ncp (네이버 클라우드 플랫폼)
  subModules: ["geocoder", "panorama"],
  libraries: ["places"],
  useCamelCase: true,
  useBeta: true
});

console.log('네이버 지도 설정 완료');

app.use(router).mount('#app')
