<script setup>
import { Home, People, Order } from '@icon-park/vue-next'
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import Footer from '@/components/Footer.vue'

const route = useRoute()

const active = ref(0)
watch(
  () => route.fullPath,
  (newPath) => {
    if (newPath.includes('/admin/monitor-staff')) active.value = 0
    else if (newPath.includes('/admin/orders')) active.value = 1
    else if (newPath.includes('/admin/profile')) active.value = 2
  },
  { immediate: true }
)
</script>

<template>
  <router-view></router-view>
  <div class="footer"><Footer /></div>
  <nut-backtop el-id="elId1" :distance="100" :bottom="60"></nut-backtop>

  <nut-tabbar v-model="active" :bottom="true" :safe-area-inset-bottom="true">
    <nut-tabbar-item tab-title="首页" to="/admin">
      <template #icon>
        <home theme="outline" size="25" :strokeWidth="3" />
      </template>
    </nut-tabbar-item>
    <nut-tabbar-item tab-title="维修统计" to="/admin/orders-list">
      <template #icon>
        <order theme="outline" size="25" :strokeWidth="3" />
      </template>
    </nut-tabbar-item>
    <nut-tabbar-item tab-title="我的" to="/admin/profile">
      <template #icon>
        <people theme="outline" size="25" :strokeWidth="3" />
      </template>
    </nut-tabbar-item>
  </nut-tabbar>
</template>

<style scoped>
.footer {
  margin-top: 25px;
  margin-bottom: 60px;
}
</style>
