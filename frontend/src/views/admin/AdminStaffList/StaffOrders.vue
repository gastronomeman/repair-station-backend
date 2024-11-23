<script setup>
import { useRouter, useRoute } from 'vue-router'
import { ref } from 'vue'
import { staffListService } from '@/api/orders.js'
import OrdersStaffItemSee from '@/components/orders/OrdersStaffItemSee.vue'

const router = useRouter()
const route = useRoute()
const loading = ref(false)

const onClick = () => {
  router.back()
}
const id = route.query.id
const starTime = route.query.start
const endTime = route.query.end

const orderList = ref([])
const getOrdersList = async () => {
  loading.value = true
  const resp = await staffListService(id, starTime, endTime)
  if (resp.code === 1) {
    orderList.value = resp.data
  }
  loading.value = false
}

getOrdersList()
</script>

<template>
  <nut-navbar title="成员历史" left-show @click-back="onClick">
    <template #left>
      <div>返回</div>
    </template>
  </nut-navbar>
  <div v-for="orders in orderList" :key="orders.value" v-loading="loading">
    <br />
    <orders-staff-item-see :order="orders"></orders-staff-item-see>
  </div>
</template>

<style scoped></style>
