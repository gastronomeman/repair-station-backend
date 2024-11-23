<script setup>
import { ref, computed } from 'vue'
import { getAdminOrderService } from '@/api/orders.js'
import {
  changeOrderNoService,
  changeStaffNoService,
  changeStopNoService,
  changStatusService
} from '@/api/repairStationStatus.js'
import { successMsg } from '@/utils/SendMsgUtils.js'

const adminOrder = ref({
  orderStatus1: 0,
  orderStatus2: 0,
  orderStatus3: 0,
  totalOrder: 0,
  lastMonthOrderCount: 0,
  currentMonthOrderCount: 0,
  currentWeekOrderCount: 0,
  yesterdayOrderCount: 0,
  todayOrderCount: 0,
  historyOrderType1: 0,
  historyOrderType2: 0,
  historyOrderType3: 0,
  historyOrderType4: 0,
  orderType1: 0,
  orderType2: 0,
  orderType3: 0,
  orderType4: 0,
  serverStatus: true,
  orderNotice: '',
  staffNotice: '',
  stopNotice: ''
})

const totalOrder = computed(() => {
  return (
    adminOrder.value.historyOrderType1 +
    adminOrder.value.historyOrderType2 +
    adminOrder.value.historyOrderType3 +
    adminOrder.value.historyOrderType4 +
    adminOrder.value.totalOrder
  )
})
const orderStatus3 = computed(() => {
  return (
    adminOrder.value.historyOrderType1 +
    adminOrder.value.historyOrderType2 +
    adminOrder.value.historyOrderType3 +
    adminOrder.value.historyOrderType4 +
    adminOrder.value.orderStatus3
  )
})
const loading = ref(false)
const getAdminOrder = async () => {
  loading.value = true
  const resp = await getAdminOrderService()
  adminOrder.value = resp.data
  loading.value = false
}

const change = async () => {
  if (!confirm('确认要切换维修状态？')) {
    adminOrder.value.serverStatus = !adminOrder.value.serverStatus
    return
  }
  const resp = await changStatusService()
  if (resp.code === 1) {
    successMsg(resp.data)
  }
}

const changeOrderNotice = async () => {
  const resp = await changeOrderNoService(adminOrder.value.orderNotice)
  if (resp.code === 1) {
    successMsg(resp.data)
  }
}
const changeStopNotice = async () => {
  const resp = await changeStopNoService(adminOrder.value.stopNotice)
  if (resp.code === 1) {
    successMsg(resp.data)
  }
}
const changeStaffNotice = async () => {
  const resp = await changeStaffNoService(adminOrder.value.staffNotice)
  if (resp.code === 1) {
    successMsg(resp.data)
  }
}

const onRefresh = async () => {
  await getAdminOrder()
  loading.value = false
}
onRefresh()
</script>

<template>
  <van-pull-refresh
    v-model="loading"
    success-text="刷新成功"
    @refresh="onRefresh"
  >
    <nut-navbar title="状态监控"> </nut-navbar>
    <div class="brief-row">
      <nut-row>
        <nut-col :span="4" :offset="4">
          <div class="content">
            <p>待接单</p>
            <p>{{ adminOrder.orderStatus1 }}</p>
          </div>
        </nut-col>
        <nut-col :span="4">
          <div class="content">
            <p>维修中</p>
            <p>{{ adminOrder.orderStatus2 }}</p>
          </div>
        </nut-col>
        <nut-col :span="4">
          <div class="content">
            <p>已完成</p>
            <p>{{ orderStatus3 }}</p>
          </div>
        </nut-col>
        <nut-col :span="3">
          <div class="content">
            <p>累计</p>
            <p>{{ totalOrder }}</p>
          </div>
        </nut-col>
      </nut-row>
    </div>
    <nut-navbar title="数据分析"> </nut-navbar>
    <div class="brief-row">
      <nut-row>
        <nut-col :span="4" :offset="3">
          <div class="content">
            <p>上月</p>
            <p>{{ adminOrder.lastMonthOrderCount }}</p>
          </div>
        </nut-col>
        <nut-col :span="3">
          <div class="content">
            <p>本月</p>
            <p>{{ adminOrder.currentMonthOrderCount }}</p>
          </div>
        </nut-col>
        <nut-col :span="4">
          <div class="content">
            <p>本周</p>
            <p>{{ adminOrder.currentWeekOrderCount }}</p>
          </div>
        </nut-col>
        <nut-col :span="3">
          <div class="content">
            <p>昨天</p>
            <p>{{ adminOrder.yesterdayOrderCount }}</p>
          </div>
        </nut-col>
        <nut-col :span="3">
          <div class="content">
            <p>今天</p>
            <p>{{ adminOrder.todayOrderCount }}</p>
          </div>
        </nut-col>
      </nut-row>
      <h5>本学期类别统计</h5>
      <nut-row>
        <nut-col :span="4" :offset="4">
          <div class="content">
            <p>软件类</p>
            <p>{{ adminOrder.orderType1 }}</p>
          </div>
        </nut-col>
        <nut-col :span="4">
          <div class="content">
            <p>硬件类</p>
            <p>{{ adminOrder.orderType2 }}</p>
          </div>
        </nut-col>
        <nut-col :span="4">
          <div class="content">
            <p>网络类</p>
            <p>{{ adminOrder.orderType3 }}</p>
          </div>
        </nut-col>
        <nut-col :span="4">
          <div class="content">
            <p>手机类</p>
            <p>{{ adminOrder.orderType4 }}</p>
          </div>
        </nut-col>
      </nut-row>
      <h5>历史类别统计</h5>
      <nut-row>
        <nut-col :span="4" :offset="4">
          <div class="content">
            <p>软件类</p>
            <p>{{ adminOrder.historyOrderType1 }}</p>
          </div>
        </nut-col>
        <nut-col :span="4">
          <div class="content">
            <p>硬件类</p>
            <p>{{ adminOrder.historyOrderType2 }}</p>
          </div>
        </nut-col>
        <nut-col :span="4">
          <div class="content">
            <p>网络类</p>
            <p>{{ adminOrder.historyOrderType3 }}</p>
          </div>
        </nut-col>
        <nut-col :span="4">
          <div class="content">
            <p>手机类</p>
            <p>{{ adminOrder.historyOrderType4 }}</p>
          </div>
        </nut-col>
      </nut-row>
    </div>
    <nut-navbar title="网站基本设置"> </nut-navbar>
    <div class="setting-row">
      <span class="setting-switch">
        服务状态：
        <nut-switch v-model="adminOrder.serverStatus" @click="change" />
      </span>
      <p class="setting-p">*关闭就是暂停维修服务了</p>

      <div class="content" v-if="adminOrder.serverStatus">
        <span>报修页公告：</span>
        <el-input
          :maxlength="50"
          show-word-limit
          v-model="adminOrder.orderNotice"
          style="width: 180px"
          :rows="4"
          type="textarea"
          placeholder="谨慎发言"
        />
        <div class="setting-button">
          <nut-button type="info" size="small" @click="changeOrderNotice"
            >更新公告</nut-button
          >
        </div>
      </div>
      <div class="content" v-else>
        <span>停修页公告：</span>
        <el-input
          :maxlength="50"
          show-word-limit
          v-model="adminOrder.stopNotice"
          style="width: 180px"
          :rows="4"
          type="textarea"
          placeholder="谨慎发言"
        />
        <div class="setting-button">
          <nut-button type="info" size="small" @click="changeStopNotice"
            >更新公告</nut-button
          >
        </div>
      </div>
      <div class="content">
        <span>社员页公告：</span>
        <el-input
          :maxlength="50"
          show-word-limit
          v-model="adminOrder.staffNotice"
          style="width: 180px"
          :rows="4"
          type="textarea"
          placeholder="谨慎发言"
        />
        <div class="setting-button">
          <nut-button type="info" size="small" @click="changeStaffNotice"
            >更新公告</nut-button
          >
        </div>
      </div>
      <p class="setting-p">*发布公告请谨慎发言</p>
    </div>
  </van-pull-refresh>
</template>

<style scoped>
.title-right {
  font-size: 13px;

  p:first-child {
    margin: 0 0 5px;
  }

  p:last-child {
    margin: 0;
  }
}

.brief-row {
  background-color: white;

  .content {
    margin-top: 10px;
    text-align: center;
  }

  p {
    margin: 2px 0;
  }

  h5 {
    text-align: center;
    margin: 20px 0 0;
  }
}
.setting-row {
  width: 90%;
  margin: 15px auto;
  text-align: center;
  .setting-switch {
    display: inline-flex;
    align-items: center;
    justify-content: center;
  }
  .setting-p {
    margin: 5px 0;
    color: #7a7374;
    font-size: 15px;
  }
  .content {
    margin: 15px 0 0;
    span {
      line-height: 52px;
    }
  }
  .content:last-child {
    margin-bottom: 150px;
  }
  .setting-button {
    margin: 5px 0 15px;
  }
}
</style>
