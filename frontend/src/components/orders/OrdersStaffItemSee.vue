<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useStaffState } from '@/stores'

const router = useRouter()
const route = useRoute()

const staffState = useStaffState()

const props = defineProps({
  order: {
    type: Object,
    required: true
  }
})
const order = ref(props.order)
const handleClick = () => {
  staffState.setOrder(order.value)
  if (route.fullPath.includes('admin')) {
    if (route.fullPath.includes('/admin/orders')) {
      router.push('/admin/orders-details')
      return
    }
    router.push('/admin/profile/orders-details')
    return
  }
  router.push('/staff/profile/orders-details')
}
</script>

<template>
  <div class="orders-item">
    <div class="orders-row" @click="handleClick">
      <nut-row>
        <nut-col :span="24">
          <div class="content-head">
            {{ order.createTime }}
            &nbsp;/&nbsp; 编号:
            {{ order.id }}
          </div>
        </nut-col>
      </nut-row>
      <nut-row>
        <nut-col :span="24">
          <div class="content-body">
            <span
              class="body-left"
              v-if="order.identity === 0"
              style="color: #f1939c"
            >
              {{ order.building }}#{{ order.dormitory }}
              &nbsp;
              {{ order.name }}
            </span>
            <span class="body-left" v-else style="color: #5698c3">
              {{ order.building }}#{{ order.dormitory }}
              &nbsp;
              {{ order.name }}
            </span>
            <span
              v-if="order.status === 1"
              class="body-right"
              style="color: #de2a18"
            >
              待接单
            </span>
            <span
              v-else-if="order.status === 2"
              class="body-right"
              style="color: #fecc11"
              >维修中
            </span>
            <span
              v-else-if="order.status === 3"
              class="body-right"
              style="color: #41b349"
            >
              已完成
            </span>
            <span
              v-else-if="order.status === 4"
              class="body-right"
              style="color: #bbb5ac"
            >
              作废订单
            </span>
          </div>
        </nut-col>
      </nut-row>
      <nut-divider dashed />
      <p style="margin-bottom: 0">{{ order.orderDescribe }}</p>
    </div>
  </div>
</template>

<style scoped>
.orders-item {
  margin: 0 auto;
  max-width: 800px;
  padding: 8px 0;
  background-color: white;

  .orders-row {
    margin: 0 auto;
    max-width: 320px !important;
    cursor: pointer;

    .content-head {
      font-size: 12px;
      color: #a7a8bd;
    }

    .content-body {
      font-size: 16px;
      margin: 5px 0;
      display: flex;
      justify-content: space-between;
      color: #7a7374;
      font-weight: bold;

      .body-left {
        flex: 1;
      }
      .body-right {
        flex-shrink: 0;
      }
    }

    .nut-divider {
      margin: 0;
    }

    p {
      text-align: center;
      word-wrap: break-word;
      overflow-wrap: break-word;
      margin: 15px auto;
      font-size: 18px;
    }
  }
}
</style>
