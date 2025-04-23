<template>
    <nav>
    <router-link to="/">Home</router-link> |
    <router-link to="/temperature">Temperature</router-link> |
    <router-link to="/rain-probability">Rain Probability</router-link> |
    <router-link to="/humidity">Humidity</router-link> |
    <router-link to="/wind-speed">Wind Speed</router-link>
  </nav>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
    name: 'HeadBarComponent',
});
</script>

<style scoped>

</style>


