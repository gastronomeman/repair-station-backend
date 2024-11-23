<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  getOrdersListService,
  getOrdersWithNameListService
} from '@/api/orders.js'
import OrdersStaffItem1 from '@/components/orders/OrdersStaffItem1.vue'
import OrdersStaffItem2 from '@/components/orders/OrdersStaffItem2.vue'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const value = ref(1)
const title = ref('')
const { orderType } = route.query

if (orderType === '1') title.value = '软件类'
else if (orderType === '2') title.value = '硬件类'
else if (orderType === '3') title.value = '网络类'
else if (orderType === '4') title.value = '手机类'

const onClick = () => {
  router.push('/staff/order-acceptance')
}

const ordersList1 = ref([])
const ordersList2 = ref([])

const getOrdersList = async () => {
  loading.value = true
  try {
    const resp1 = await getOrdersListService(orderType, 1)
    if (resp1.code === 1) {
      ordersList1.value = resp1.data
    }
    const resp2 = await getOrdersWithNameListService(orderType, 2)
    if (resp2.code === 1) {
      ordersList2.value = resp2.data
    }
  } catch (error) {
    console.error('Error fetching orders:', error)
    // Handle error as needed
  } finally {
    loading.value = false
  }
}

const refreshOrdersList = async () => getOrdersList()

const onRefresh = async () => {
  await getOrdersList()
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
    <nut-navbar :title="title" left-show @click-back="onClick">
      <template #left>
        <div>返回</div>
      </template>
    </nut-navbar>
    <nut-tabs type="smile" v-model="value" v-loading="loading">
      <nut-tab-pane title="待接单" :pane-key="1">
        <div class="br">&nbsp;</div>
        <div class="tab-pane-left" v-for="order in ordersList1" :key="order.id">
          <orders-staff-item1
            @refresh="refreshOrdersList"
            :order="order"
          ></orders-staff-item1>
          <div class="br">&nbsp;</div>
        </div>
        <el-empty v-if="ordersList1.length === 0" style="background: #f0f0f0">
          <template #description>
            <p style="text-align: center; font-size: 20px">
              接单页面空空如也！<br />˚‧º·(˚ ˃̣̣̥᷄⌓˂̣̣̥᷅ )‧º·˚
              <br />去提升一下宣传力度吧
            </p>
          </template>
        </el-empty>
      </nut-tab-pane>
      <nut-tab-pane title="维修中" :pane-key="2">
        <div class="br">&nbsp;</div>
        <div class="tab-pane-left" v-for="order in ordersList2" :key="order.id">
          <orders-staff-item2 :order="order"></orders-staff-item2>
          <div class="br">&nbsp;</div>
        </div>
        <el-empty v-if="ordersList2.length === 0" style="background: #f0f0f0">
          <template #description>
            <p style="text-align: center; font-size: 20px">
              维修页面空空如也！<br />˚‧º·(˚ ˃̣̣̥᷄⌓˂̣̣̥᷅ )‧º·˚
            </p>
          </template>
        </el-empty>
      </nut-tab-pane>
    </nut-tabs>
  </van-pull-refresh>
</template>

<style scoped>
.nut-tab-pane {
  padding: 0 !important;
  .tab-pane-left {
    width: 100%;
    height: 100%;
    margin: 0 auto;
    .nut-divider {
      margin: 10px 0 0;
    }
    .br {
      width: 100%;
      background-color: #f0f0f0;
    }
  }
  .br {
    width: 100%;
    background-color: #f0f0f0;
  }
}
</style>
