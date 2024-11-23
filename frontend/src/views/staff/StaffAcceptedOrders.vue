<script setup>
import { ref } from 'vue'
import { takingListService } from '@/api/orders.js'
import OrdersTaskItem from '@/components/orders/OrdersTaskItem.vue'

const ordersList = ref([])
const loading = ref(false)

const getOrdersList = async () => {
  loading.value = true
  const resp = await takingListService()
  if (resp.code === 1) {
    ordersList.value = resp.data
  }
  loading.value = false
}

const onRefresh = async () => {
  await getOrdersList()
  loading.value = false
}
onRefresh()
const refreshOrdersList = async () => getOrdersList()
</script>

<template>
  <nut-navbar title="报修订单"></nut-navbar>
  <van-pull-refresh
    v-model="loading"
    success-text="刷新成功"
    @refresh="onRefresh"
  >
    <div class="loading" v-loading="loading">
      <div v-for="order in ordersList" :key="order.id">
        <div class="br">&nbsp;</div>
        <orders-task-item
          @refresh="refreshOrdersList"
          :order="order"
        ></orders-task-item>
      </div>
      <el-empty style="background: #f0f0f0" v-if="ordersList.length === 0">
        <template #description>
          <p>
            修单其实并不难哦，快去尝试一下吧！<br />遇到困难可以去寻求师兄师姐的帮助哦！<br />
            (*๓´╰╯`๓)♡
          </p>
        </template>
      </el-empty>
    </div>
  </van-pull-refresh>
</template>

<style scoped>
.loading {
  height: 80vh;
  margin-bottom: 50px;
}
</style>
