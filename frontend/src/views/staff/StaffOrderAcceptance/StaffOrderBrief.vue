<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { People } from '@icon-park/vue-next'
import {
  getOrderTotalService,
  getStaffOrderTotalService
} from '@/api/orders.js'
import { getStaffOnlineCountService } from '@/api/staff.js'
import { setToolBoxService, toolBoxService } from '@/api/repairStationStatus.js'
import { successMsg } from '@/utils/SendMsgUtils.js'

const router = useRouter()

const colors = [
  { color: '#f56c6c', percentage: 100 },
  { color: '#e6a23c', percentage: 80 },
  { color: '#5cb87a', percentage: 60 },
  { color: '#1989fa', percentage: 40 },
  { color: '#6f7ad3', percentage: 20 }
]

const goToOrderList = (orderType) => {
  //1.软件类 2.硬件类 3.网络类 4.手机类
  router.push({
    path: '/staff/order-acceptance/list',
    query: { orderType }
  })
}

const staffOrder = ref({
  semesterOrderCount: '',
  lastMonthOrderCount: '',
  currentMonthOrderCount: '',
  currentWeekOrderCount: '',
  yesterdayOrderCount: '',
  todayOrderCount: '',
  orderType11: '',
  orderType12: '',
  orderType21: '',
  orderType22: '',
  orderType31: '',
  orderType32: '',
  orderType41: '',
  orderType42: ''
})
const loading = ref(false)
const getStaffOrder = async () => {
  loading.value = true
  const resp = await getOrderTotalService()
  staffOrder.value = resp.data
  loading.value = false
}

const onlineCount = ref(0)
const getStaffOnlineCount = async () => {
  const resp = await getStaffOnlineCountService()
  if (resp.code === 1) {
    onlineCount.value = resp.data
  }
}

const staffTotal = ref({
  orderTotal: '',
  weekOrderTotal: ''
})
const getStaffOrderTotal = async () => {
  const resp = await getStaffOrderTotalService()
  if (resp.code === 1) {
    staffTotal.value = resp.data
  }
}

const toolBox = ref('')
const showToolI = ref(false)
const getToolBox = async () => {
  const tool = await toolBoxService()
  if (tool.code === 1) {
    toolBox.value = tool.data
  }
}

const setToolBox = async () => {
  const tool = await setToolBoxService(toolBox.value)
  if (tool.code === 1) {
    successMsg(tool.data)
    showToolI.value = false
  }
}

const onRefresh = async () => {
  await getStaffOrder()
  await getStaffOrderTotal()
  await getStaffOnlineCount()
  await getToolBox()
  loading.value = false
}
onRefresh()
</script>

<template>
  <van-pull-refresh
    class="box"
    v-model="loading"
    success-text="刷新成功"
    @refresh="onRefresh"
  >
    <nut-navbar title="站内维修统计">
      <template #right>
        <div class="title-right">
          <p>当前在线人数</p>
          <p>
            <people
              style="margin-right: 2px"
              theme="outline"
              size="10"
              fill="#333"
            />
            {{ onlineCount }}人
          </p>
        </div>
      </template>
    </nut-navbar>
    <div class="brief-row">
      <nut-row>
        <nut-col :span="4" :offset="1">
          <div class="content">
            <p>本学期</p>
            <p>{{ staffOrder.semesterOrderCount }}</p>
          </div>
        </nut-col>
        <nut-col :span="4">
          <div class="content">
            <p>上个月</p>
            <p>{{ staffOrder.lastMonthOrderCount }}</p>
          </div>
        </nut-col>
        <nut-col :span="3">
          <div class="content">
            <p>本月</p>
            <p>{{ staffOrder.currentMonthOrderCount }}</p>
          </div>
        </nut-col>
        <nut-col :span="4">
          <div class="content">
            <p>本周</p>
            <p>{{ staffOrder.currentWeekOrderCount }}</p>
          </div>
        </nut-col>
        <nut-col :span="3">
          <div class="content">
            <p>昨天</p>
            <p>{{ staffOrder.yesterdayOrderCount }}</p>
          </div>
        </nut-col>
        <nut-col :span="3">
          <div class="content">
            <p>今天</p>
            <p>{{ staffOrder.todayOrderCount }}</p>
          </div>
        </nut-col>
      </nut-row>
    </div>
    <div class="brief-orders-type">
      <div class="up">
        <el-progress
          class="first-up"
          type="dashboard"
          :percentage="(staffOrder.orderType11 + staffOrder.orderType12) * 10"
          :color="colors"
          @click="goToOrderList(1)"
        >
          <template #default>
            <p
              class="percentage-value"
              v-if="staffOrder.orderType11 > 0"
              style="color: red"
            >
              {{ staffOrder.orderType11 }} / {{ staffOrder.orderType12 }}
            </p>
            <p class="percentage-value" v-else>
              {{ staffOrder.orderType11 }} / {{ staffOrder.orderType12 }}
            </p>
            <span
              class="percentage-label"
              v-if="staffOrder.orderType11 > 0"
              style="color: red"
              >软件类
            </span>
            <span class="percentage-label" v-else>软件类</span>
          </template>
        </el-progress>
        <el-progress
          type="dashboard"
          :percentage="(staffOrder.orderType21 + staffOrder.orderType22) * 10"
          :color="colors"
          @click="goToOrderList(2)"
        >
          <template #default>
            <p
              class="percentage-value"
              v-if="staffOrder.orderType21 > 0"
              style="color: red"
            >
              {{ staffOrder.orderType21 }} / {{ staffOrder.orderType22 }}
            </p>
            <p class="percentage-value" v-else>
              {{ staffOrder.orderType21 }} / {{ staffOrder.orderType22 }}
            </p>
            <span
              class="percentage-label"
              v-if="staffOrder.orderType21 > 0"
              style="color: red"
              >硬件类
            </span>
            <span class="percentage-label" v-else>硬件类 </span>
          </template>
        </el-progress>
      </div>
      <div class="down">
        <el-progress
          class="first-up"
          type="dashboard"
          :percentage="(staffOrder.orderType31 + staffOrder.orderType32) * 10"
          :color="colors"
          @click="goToOrderList(3)"
        >
          <template #default>
            <p
              class="percentage-value"
              v-if="staffOrder.orderType31 > 0"
              style="color: red"
            >
              {{ staffOrder.orderType31 }} / {{ staffOrder.orderType32 }}
            </p>
            <p class="percentage-value" v-else>
              {{ staffOrder.orderType31 }} / {{ staffOrder.orderType32 }}
            </p>
            <span
              class="percentage-label"
              v-if="staffOrder.orderType31 > 0"
              style="color: red"
              >网络类
            </span>
            <span class="percentage-label" v-else>网络类 </span>
          </template>
        </el-progress>
        <el-progress
          type="dashboard"
          :percentage="(staffOrder.orderType41 + staffOrder.orderType42) * 10"
          :color="colors"
          @click="goToOrderList(4)"
        >
          <template #default>
            <p
              class="percentage-value"
              v-if="staffOrder.orderType41 > 0"
              style="color: red"
            >
              {{ staffOrder.orderType41 }} / {{ staffOrder.orderType42 }}
            </p>
            <p class="percentage-value" v-else>
              {{ staffOrder.orderType41 }} / {{ staffOrder.orderType42 }}
            </p>
            <span
              class="percentage-label"
              v-if="staffOrder.orderType41 > 0"
              style="color: red"
              >手机类
            </span>
            <span class="percentage-label" v-else>手机类</span>
          </template>
        </el-progress>
      </div>
      <div class="tool-box">
        <div
          style="cursor: pointer"
          v-if="!showToolI"
          @dblclick="showToolI = true"
        >
          <span v-html="toolBox"></span>
        </div>
        <div v-else>
          <el-input
            autofocus
            @blur="showToolI = false"
            size="small"
            v-model="toolBox"
            style="width: 200px"
            type="textarea"
            placeholder="输入工具箱所在人姓名"
          />&nbsp;
          <nut-button size="mini" type="info" @click="setToolBox">
            修改
          </nut-button>
        </div>
      </div>
    </div>
    <div class="staff-orders-count">
      <nut-divider dashed> 我的单量统计</nut-divider>
      <nut-row>
        <nut-col :span="12">
          <div class="content">
            <p>本学期已处理</p>
            <p>{{ staffTotal.orderTotal }}单</p>
          </div>
        </nut-col>
        <nut-col :span="12">
          <div class="content">
            <p>本周已处理</p>
            <p>{{ staffTotal.weekOrderTotal }}单</p>
          </div>
        </nut-col>
      </nut-row>
    </div>
  </van-pull-refresh>
</template>

<style scoped>
.box{
  height: 90vh;
}
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
}

.brief-orders-type {
  width: 85%;
  margin: 0 auto;
  text-align: center;

  .el-progress {
    cursor: pointer;
  }

  .up {
    margin-top: 60px;

    .first-up {
      margin: 0 35px 0 0;
    }

    p {
      margin: 8px 0;
    }
  }

  .down {
    margin-top: 25px;

    .first-up {
      margin: 0 35px 0 0;
    }

    p {
      margin: 8px 0;
    }
  }
  .tool-box {
    margin: 10px auto;
    text-align: center;
    font-size: 12px;
    color: #7a7374;
  }
}

.staff-orders-count {
  width: 85%;
  max-height: 800px;
  margin: 20px auto 0;

  .nut-divider {
    margin-bottom: 0;
  }

  .content {
    text-align: center;
  }
}
</style>
