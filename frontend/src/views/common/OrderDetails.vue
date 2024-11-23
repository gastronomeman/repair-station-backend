<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useStaffState, useCommonState } from '@/stores/index.js'
import { ref } from 'vue'
import { getNameByIdService } from '@/api/staff.js'
import { cancelService, changStatusService } from '@/api/orders.js'
import { successMsg } from '@/utils/SendMsgUtils.js'
import { confirmDialog } from '@/utils/DialogUtils.js'

const router = useRouter()
const route = useRoute()
const staffState = useStaffState()
const commonState = useCommonState()

const order = ref({})
order.value = staffState.order

const onClick = () => {
  router.go(-1)
}

const getNameById = async () => {
  if (!route.fullPath.includes('admin')) return
  if (order.value.status === 4) order.value.staffName = '作废订单'
  if (order.value.staffId == null) return

  const resp = await getNameByIdService(order.value.staffId)
  if (resp.code === 1) {
    order.value.staffName = resp.data
  }
}
getNameById()

const changeStatus = async (n, id) => {
  const resp = await changStatusService(n, id)
  if (resp.code === 1) {
    router.go(-1)
    successMsg(resp.data)
  }
}

const cancel = async () => {
  if (!(await confirmDialog('是否要作废此订单？'))) return

  const resp = await cancelService(order.value)
  if (resp.code === 1) {
    successMsg(resp.data)
    router.go(-1)
  }
}
</script>

<template>
  <nut-navbar title="修单详情" left-show @click-back="onClick">
    <template #left>
      <div>返回</div>
    </template>
  </nut-navbar>

  <div class="order-details">
    <nut-row>
      <nut-col :span="12">
        <div class="content-left">姓名：</div>
      </nut-col>
      <nut-col :span="12">
        <div class="content-right">{{ order.name }}</div>
      </nut-col>
    </nut-row>
    <nut-divider dashed />
    <div v-if="route.fullPath.includes('admin')">
      <nut-row v-if="route.fullPath.includes('admin')">
        <nut-col :span="12">
          <div class="content-left">学号：</div>
        </nut-col>
        <nut-col :span="12">
          <div class="content-right">{{ order.studentId }}</div>
        </nut-col>
      </nut-row>
      <nut-divider dashed />
    </div>
    <nut-row>
      <nut-col :span="12">
        <div class="content-left">机主身份：</div>
      </nut-col>
      <nut-col :span="12" v-if="order.identity === 1">
        <div class="content-right">学生</div>
      </nut-col>
      <nut-col :span="12" v-if="order.identity === 0">
        <div class="content-right">教师</div>
      </nut-col>
    </nut-row>
    <nut-divider dashed />
    <nut-row>
      <nut-col :span="12">
        <div class="content-left">机主电话：</div>
      </nut-col>
      <nut-col :span="12">
        <div class="content-right">{{ order.phone }}</div>
      </nut-col>
    </nut-row>
    <nut-divider dashed />
    <nut-row>
      <nut-col :span="12">
        <div class="content-left">报修时间：</div>
      </nut-col>
      <nut-col :span="12">
        <div class="content-right">{{ order.createTime }}</div>
      </nut-col>
    </nut-row>
    <nut-divider dashed />
    <nut-row>
      <nut-col :span="12">
        <div class="content-left">结束时间：</div>
      </nut-col>
      <nut-col :span="12">
        <div class="content-right">{{ order.completionTime }}</div>
      </nut-col>
    </nut-row>
    <nut-divider dashed />
    <nut-row>
      <nut-col :span="12">
        <div class="content-left">报修地址：</div>
      </nut-col>
      <nut-col :span="12">
        <div class="content-right">
          {{ order.building }}#{{ order.dormitory }}
        </div>
      </nut-col>
    </nut-row>
    <nut-divider dashed />
    <nut-row>
      <nut-col :span="12">
        <div class="content-left">报修类型：</div>
      </nut-col>
      <nut-col :span="12" v-if="order.orderType === 1">
        <div class="content-right">软件类</div>
      </nut-col>
      <nut-col :span="12" v-if="order.orderType === 2">
        <div class="content-right">硬件类</div>
      </nut-col>
      <nut-col :span="12" v-if="order.orderType === 3">
        <div class="content-right">网络类</div>
      </nut-col>
      <nut-col :span="12" v-if="order.orderType === 4">
        <div class="content-right">手机类</div>
      </nut-col>
    </nut-row>
    <nut-divider dashed />
    <div v-if="route.fullPath.includes('admin')">
      <nut-row>
        <nut-col :span="12">
          <div class="content-left">维修人员：</div>
        </nut-col>
        <nut-col :span="12">
          <div class="content-right">{{ order.staffName }}</div>
        </nut-col>
      </nut-row>
      <nut-divider dashed />
    </div>
    <div v-if="route.fullPath.includes('admin')">
      <nut-row>
        <nut-col :span="12">
          <div class="content-left">报修状态：</div>
        </nut-col>
        <nut-col :span="12" v-if="order.status === 1">
          <div class="content-right">待接单</div>
        </nut-col>
        <nut-col :span="12" v-if="order.status === 2">
          <div class="content-right">维修中</div>
        </nut-col>
        <nut-col :span="12" v-if="order.status === 3">
          <div class="content-right">已完成</div>
        </nut-col>
        <nut-col :span="12" v-if="order.status === 4">
          <div class="content-right">已作废</div>
        </nut-col>
      </nut-row>
      <nut-divider dashed />
    </div>
    <nut-row>
      <nut-col :span="12">
        <div class="content-left">报修描述：</div>
      </nut-col>
      <nut-col :span="12">
        <div class="content-right">{{ order.orderDescribe }}</div>
      </nut-col>
    </nut-row>
    <nut-divider dashed />
    <div
      v-show="route.fullPath.includes('admin') && +commonState.tabs === 1"
      style="text-align: center"
    >
      <nut-button @click="cancel" type="danger"> 作废订单 </nut-button>&nbsp;
      <nut-button @click="changeStatus(1, order.id)" type="warning">
        恢复待接单 </nut-button
      >&nbsp;
      <nut-button @click="changeStatus(2, order.id)" type="primary">
        恢复维修中
      </nut-button>
    </div>
  </div>
</template>

<style scoped>
.order-details {
  max-width: 800px;
  margin: 30px auto;
  width: 80%;
  .nut-divider {
    margin: 5px;
  }
  .content-left {
    text-align: right;
    height: 21px;
  }
  .content-right {
    line-height: 21px;
    height: auto;
  }
}
</style>
