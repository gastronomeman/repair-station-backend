<script setup>
import OrdersStaffItemSee from '@/components/orders/OrdersStaffItemSee.vue'
import { computed, ref } from 'vue'
import { useCommonState } from '@/stores/index.js'
import { getHistoryListService } from '@/api/orders.js'

const commonState = useCommonState()

const loading = ref(false)

const search = ref('')
const ordersList = ref('')
const getOrderList = async () => {
  loading.value = true
  const resp = await getHistoryListService(
    commonState.pageH.currentPage,
    commonState.pageH.pageSize,
    search.value
  )
  if (resp.code === 1) {
    ordersList.value = resp.data.records
    commonState.pageH.total = resp.data.total
  }
  loading.value = false
}
getOrderList()

const change = async () => {
  await getOrderList()
}
const searchStaff = async () => {
  await getOrderList()
}

const resetList = async () => {
  search.value = ''
  await getOrderList()
}

const toTop = async () => {
  commonState.pageH.currentPage = 1
  search.value = ''
  await getOrderList()
}

const toEnd = async () => {
  commonState.pageH.currentPage = Math.ceil(
    commonState.pageH.total / commonState.pageH.pageSize
  )
  search.value = ''
  await getOrderList()
}
const totalPage = computed(() => {
  return Math.ceil(commonState.pageH.total / commonState.pageH.pageSize)
})
</script>

<template>
  <el-empty
    style="background: #f0f0f0"
    v-if="ordersList.length === 0"
    v-loading="loading"
  >
    <template #description>
      <p>
        期待你在维修站留下一笔痕迹哦！<br />
        ៷&gt;ᴗ&lt;៷
      </p>
    </template>
  </el-empty>
  <div v-else>
    <div class="search">
      <el-input
        v-model="search"
        clearable
        style="width: 150px"
        placeholder="输入学号名字查询"
      />&nbsp;
      <nut-button type="info" size="small" @click="searchStaff"
        >查询</nut-button
      >
      &nbsp;
      <nut-button type="info" size="small" @click="resetList">重置</nut-button>
      <nut-divider />
    </div>

    <div v-for="orders in ordersList" :key="orders.id" class="br">
      <orders-staff-item-see :order="orders"></orders-staff-item-see>
      <div class="br">&nbsp;</div>
    </div>
    <div class="page">
      <nut-pagination
        v-model="commonState.pageH.currentPage"
        :total-items="commonState.pageH.total"
        :items-per-page="commonState.pageH.pageSize"
        @change="change"
      />
    </div>
    <div class="page-footer">
      <p>{{ commonState.pageH.currentPage }}/{{ totalPage }}页</p>
      <nut-button size="small" type="default" @click="toTop">首页</nut-button>
      &nbsp;
      <nut-button size="small" type="default" @click="toEnd">尾页</nut-button>
    </div>
  </div>
</template>

<style scoped>
.search {
  width: 80%;
  margin: 15px auto;
  text-align: center;
}
.page {
  width: 90%;
  margin: 10px auto;
  display: flex;
  justify-content: center;
  align-items: center;
}
.page-footer {
  text-align: center;
  p {
    color: #7a7374;
    margin: 0 0 5px;
  }
}
</style>
