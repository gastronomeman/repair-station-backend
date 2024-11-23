<script setup>
import { ref, watch } from 'vue'
import { Search, Announcement, Tool } from '@icon-park/vue-next'
import { useRouter, useRoute } from 'vue-router'

import '@/assets/orders/orders.scss'
import OrdersFooter from '@/components/orders/OrdersFooter.vue'
import { getOrderTitleService } from '@/api/repairStationStatus.js'
import { isNotBlank } from '@/utils/StringUtils.js'

const router = useRouter()
const route = useRoute()

const text = ref('')
const getText = async () => {
  const resp = await getOrderTitleService()
  if (resp.code === 1) {
    text.value = resp.data
  }
}
getText()

const title = ref('电脑报修')

const search = ref({
  input: '',
  backgroundColor: '#f0f0f0'
})

const goToQuery = () => {
  router.push({
    path: '/orders/query',
    query: { studentId: search.value.input }
  })
}

watch(
  () => route.fullPath,
  async (newPath) => {
    if (newPath.includes('/orders/query')) title.value = '报修查询'
    else if (
      newPath === '/orders/notice' ||
      newPath === '/orders/from' ||
      newPath === '/orders/wait'
    )
      title.value = '电脑报修'
    else title.value = '维修公告'
  },
  { immediate: true }
)
</script>

<template>
  <nut-sticky>
    <nut-noticebar :text="text" scrollable v-if="!isNotBlank(text)">
      <template #left-icon>
        <announcement theme="outline" size="20" />
      </template>
    </nut-noticebar>
  </nut-sticky>
  <div class="orders-head">
    <h1>{{ title }}</h1>
    <h3>- Repair Station -</h3>
    <el-input
      clearable
      size="large"
      v-model="search.input"
      style="max-width: 780px; margin: 10px auto; width: 92%"
      placeholder="输入学号或姓名查询报修情况"
      class="input-with-select"
    >
      <template #append>
        <el-button @click="goToQuery" :icon="Search" />
      </template>
    </el-input>
    <div
      v-show="
        !(
          route.fullPath === '/orders/from' || route.fullPath === '/orders/wait'
        )
      "
      class="orders-img"
    >
      <div v-if="title !== '维修公告'" class="text-box">
        <nut-row>
          <nut-col :span="12">
            <div class="text first-text" @click="router.push('/')">
              报修页面
              <tool class="search-icon" theme="outline" size="20" fill="#333" />
            </div>
          </nut-col>
          <nut-col :span="12">
            <div
              class="text last-text"
              @click="router.push('/orders/query?studentId=')"
            >
              <span>查询页面</span>
              <Search class="search-icon" theme="outline" size="20" />
            </div>
          </nut-col>
        </nut-row>
      </div>
      <img src="@/assets/orders/images/adv_repair.jpg" alt="图标" />
    </div>
  </div>
  <router-view></router-view>
  <OrdersFooter></OrdersFooter>
</template>

<style scoped></style>
