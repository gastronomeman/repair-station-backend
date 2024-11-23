<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useCommonState } from '@/stores/index.js'
import { ref } from 'vue'
import OrderList from '@/components/orders/OrderList.vue'
import OrderHistoryList from '@/components/orders/OrderHistoryList.vue'

const router = useRouter()
const route = useRoute()

const onClick = () => {
  router.go(-1)
}
const commonState = useCommonState()

const title = ref('我的历史')
if (route.fullPath.includes('admin')) title.value = '所有维修单'
</script>

<template>
  <nut-sticky>
    <nut-navbar :title="title" left-show @click-back="onClick">
      <template #left>
        <div>返回</div>
      </template>
    </nut-navbar>
  </nut-sticky>
  <nut-tabs v-model="commonState.tabs" auto-height>
    <nut-tab-pane title="本学期" :pane-key="1">
      <OrderList></OrderList>
    </nut-tab-pane>
    <nut-tab-pane title="历史" :pane-key="2">
      <order-history-list></order-history-list>
    </nut-tab-pane>
  </nut-tabs>
</template>

<style scoped>
.nut-tab-pane {
  padding: 0 !important;
  background-color: #f0f0f0 !important;
}
</style>
