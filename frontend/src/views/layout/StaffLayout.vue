<script setup>
import { Announcement, Order, People, Home } from '@icon-park/vue-next'
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getStaffTitleService } from '@/api/repairStationStatus.js'
import Footer from '@/components/Footer.vue'

const route = useRoute()

const active = ref(0)

const title = ref('')
const getTitle = async () => {
  const resp = await getStaffTitleService()
  if (resp.code === 1) {
    title.value = resp.data
  }
}

const frame = ref(true)
watch(
  () => route.fullPath,
  async (newPath) => {
    if (newPath.includes('/staff/order-acceptance')) active.value = 0
    else if (newPath.includes('/staff/accepted-orders')) active.value = 1
    else if (newPath.includes('/staff/profile')) active.value = 2

    if (!newPath.includes('/staff/login')) await getTitle()

    frame.value = !newPath.includes('/staff/login')
  },
  { immediate: true }
)
</script>

<template>
  <nut-noticebar :text="title" scrollable v-if="frame">
    <template #left-icon>
      <announcement theme="outline" size="20" />
    </template>
  </nut-noticebar>
  <router-view></router-view>
  <div class="footer"><Footer /></div>
  <nut-backtop el-id="elId1" :distance="100" :bottom="60"></nut-backtop>
  <nut-tabbar
    v-if="frame"
    v-model="active"
    :bottom="true"
    :safe-area-inset-bottom="true"
  >
    <nut-tabbar-item tab-title="首页" to="/staff/order-acceptance">
      <template #icon>
        <home theme="outline" size="25" :strokeWidth="3" />
      </template>
    </nut-tabbar-item>
    <nut-tabbar-item tab-title="报修订单" to="/staff/accepted-orders">
      <template #icon>
        <order theme="outline" size="25" :strokeWidth="3" />
      </template>
    </nut-tabbar-item>
    <nut-tabbar-item tab-title="我的" to="/staff/profile">
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
