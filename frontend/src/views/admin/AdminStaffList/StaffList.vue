<script setup>
import { ref } from 'vue'
import { orderCountService } from '@/api/staff.js'
import { useRouter } from 'vue-router'
import { useAdminState } from '@/stores/index.js'
import { baseURL } from '@/utils/request.js'

const router = useRouter()
const adminState = useAdminState()

const loading = ref(false)
const value1 = ref(adminState.startTime)
const value2 = ref(adminState.endTime)

const leaderboard = ref([])
const getOrdersList = async () => {
  loading.value = true
  adminState.setStartTime(value1.value)
  adminState.setEndTime(value2.value)
  const resp = await orderCountService(value1.value, value2.value)
  if (resp.code === 1) {
    leaderboard.value = resp.data
  }
  loading.value = false
}
getOrdersList()

const toStaffOrders = (id) => {
  router.push(
    `/admin/orders?id=${id}&start=${value1.value}&end=${value2.value}`
  )
}

const resetList = () => {
  value1.value = ''
  value2.value = ''
  getOrdersList()
}

const getCountCsv = async () => {
  window.location.href =
    baseURL +
    `/staff/count-csv?startTime=${value1.value}&endTime=${value2.value}`
}
</script>

<template>
  <nut-navbar title="维修统计"></nut-navbar>
  <div class="date-picker">
    <span class="demonstration">开始日期：</span>
    <el-date-picker
      v-model="value1"
      type="datetime"
      :editable="false"
      placeholder="开始日期"
      value-format="YYYY-MM-DD HH:mm:ss"
    />
    <div class="br"></div>
    <span class="demonstration">结束日期：</span>
    <el-date-picker
      v-model="value2"
      :editable="false"
      type="datetime"
      placeholder="结束日期"
      value-format="YYYY-MM-DD HH:mm:ss"
    />
    <div class="list-button">
      <nut-button type="info" @click="getCountCsv">导出数据</nut-button>
      &nbsp;&nbsp;
      <nut-button type="info" @click="resetList">重置</nut-button>
      &nbsp;&nbsp;
      <nut-button type="info" @click="getOrdersList">查找</nut-button>
    </div>
    <nut-divider dashed></nut-divider>
  </div>
  <div
    v-for="(l, index) in leaderboard"
    :key="index"
    class="leaderboard"
    v-loading="loading"
  >
    <nut-navbar @click="toStaffOrders(l.id)" style="cursor: pointer">
      <template #left>
        <span class="nav-title">{{ l.name }}</span>
      </template>
      <template #right>
        <span class="nav-title">{{ l.orderCount }}单</span>
      </template>
    </nut-navbar>
    <nut-divider dashed />
  </div>
</template>

<style scoped>
.date-picker {
  width: 80%;
  margin: 20px auto;
  text-align: center;
  .br {
    margin: 10px 0;
  }
  .list-button {
    margin: 8px 0;
  }
  .nut-divider {
    margin: 5px 0;
  }
}
.leaderboard {
  width: 80%;
  margin: 0 auto;
  span {
    color: black;
  }
  .nut-divider {
    margin: 0;
  }
}
</style>
