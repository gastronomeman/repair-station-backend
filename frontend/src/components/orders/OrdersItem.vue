<script setup>
import { ref } from 'vue'
import { dialog } from '@/utils/DialogUtils.js'

const props = defineProps({
  order: {
    type: Object,
    required: true
  }
})
const order = ref(props.order)

const handleClick = async () => {
  console.log(order.value)
  if (order.value.status === 3) {
    dialog(`已在${order.value.completionTime}<br />完成了您的维修任务哦！`)
  } else if (order.value.status === 2) {
    dialog(
      `我们的工作人员 ${order.value.staffName} 已经接单，<br />请留意手机短信哦！<br />工作人员电话是：${order.value.staffPhone}`
    )
  } else if (order.value.status === 1) {
    dialog(
      '请耐心等待，<br />工作人员正在赶来...<br />如果有如何疑问可以加入QQ群：790445318询问哦。'
    )
  } else {
    dialog('订单异常 <br />如果有如何疑问可以加入QQ群：790445318询问哦。')
  }
}
</script>

<template>
  <div class="orders-item" @click="handleClick">
    <div class="orders-row">
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
              style="color: #a7a8bd"
              >订单失效
            </span>
          </div>
        </nut-col>
      </nut-row>
      <nut-divider dashed />
      <p>{{ order.orderDescribe }}</p>
    </div>
  </div>
</template>

<style scoped>
.orders-item {
  margin: 0 auto;
  max-width: 800px;
  padding: 10px 0 0;
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
      margin: 2px auto;
      padding: 0 0 10px;
      font-size: 18px;
    }
  }
}
</style>
