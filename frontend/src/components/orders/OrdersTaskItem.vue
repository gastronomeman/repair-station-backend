<script setup>
import { ref } from 'vue'
import { finishOrderService, transferOrderService } from '@/api/orders.js'
import { errorMsg, successMsg } from '@/utils/SendMsgUtils.js'
import { useStaffState } from '@/stores/index.js'
import { useRouter } from 'vue-router'
import { confirmDialog } from '@/utils/DialogUtils.js'

const staffState = useStaffState()
const router = useRouter()

const emit = defineEmits(['refresh'])
const props = defineProps({
  order: {
    type: Object,
    required: true
  }
})
const order = ref(props.order)
const handleClick = async () => {
  if (await confirmDialog('ฅ( ̳• ◡ • ̳)ฅ', '是否要结束修单？')) {
    const resp = await finishOrderService(order.value)
    if (resp.code === 1) {
      successMsg(resp.data)
      emit('refresh')
    }
  }
}
const callPhone = () => {
  window.location.href = `tel:${order.value.phone}`
}
const sendSMS = () => {
  window.location.href = `sms:${order.value.phone}`
}
const signature = () => {
  staffState.setOrder(order.value)
  router.push('/signature')
}

const transferOrder = async () => {
  if (order.value.assignor !== null) {
    errorMsg('此订单为转单，不可再次转出！')
    return
  }
  await showConfirmDialog({
    title: 'ฅ( ̳• ◡ • ̳)ฅ',
    message: '是否确认转出订单？'
  })
    .then(async () => {
      const resp = await transferOrderService(order.value.id)
      if (resp.code === 1) {
        successMsg(resp.data)
        emit('refresh')
      }
    })
    .catch(() => {
      // on cancel
    })
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
              v-if="order.status === 2"
              class="body-right"
              style="color: #e8b004"
              >维修中
            </span>
          </div>
        </nut-col>
      </nut-row>
      <nut-divider dashed />
      <p>{{ order.orderDescribe }}</p>
    </div>
    <nut-row>
      <nut-col :span="12" @click="signature">
        <div class="orders-row-footer2">协议签订（拆机用）</div>
      </nut-col>
      <nut-col :span="12" @click="transferOrder"
        ><div class="orders-row-footer2">转单</div>
      </nut-col>
    </nut-row>
    <nut-row>
      <nut-col :span="12" @click="callPhone">
        <div class="orders-row-footer1">拨打电话</div>
      </nut-col>
      <nut-col :span="12">
        <div class="orders-row-footer1" @click="sendSMS">发送短信</div>
      </nut-col>
    </nut-row>
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
      font-size: 18px;
    }
  }
  .orders-row-footer1 {
    background-color: #5e616d;
    color: white;
    font-weight: bold;
    padding: 6px 0;
    margin: 2px 1px;
    text-align: center;
    cursor: pointer;
  }
  .orders-row-footer2 {
    background-color: #74787a;
    color: white;
    font-weight: bold;
    padding: 6px 0;
    margin: 5px 1px 0;
    text-align: center;
    cursor: pointer;
  }
}
</style>
