import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import TemperatureView from '../views/TemperatureView.vue'
import RainProbabilityView from '../views/RainProbabilityView.vue'
import HumidityView from '../views/HumidityView.vue'
import WindSpeedView from '../views/WindSpeedView.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/temperature',
    name: 'temperature',
    component: TemperatureView
  },
  {
    path: '/rain-probability',
    name: 'rainProbability',
    component: RainProbabilityView
  },
  {
    path: '/humidity',
    name: 'humidity',
    component: HumidityView
  },
  {
    path: '/wind-speed',
    name: 'windSpeed',
    component: WindSpeedView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
