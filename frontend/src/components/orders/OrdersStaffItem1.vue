<script setup>
import { ref, defineEmits } from 'vue'
import { takingOrdersService } from '@/api/orders.js'
import { successMsg } from '@/utils/SendMsgUtils.js'
import { getNameByIdService } from '@/api/staff.js'
import { confirmDialog } from '@/utils/DialogUtils.js'

const emit = defineEmits(['refresh'])
const props = defineProps({
  order: {
    type: Object,
    required: true
  }
})
const order = ref(props.order)
const handleClick = async () => {
  if (await confirmDialog('(๑•̀ㅂ•́)و✧', '是否确认接单？')) {
    const resp = await takingOrdersService(order.value)
    if (resp.code === 1) {
      successMsg(resp.data)
      emit('refresh')
    }
  }
}

const assignor = ref('')
const getAssignorName = async () => {
  const resp = await getNameByIdService(order.value.assignor)
  assignor.value = resp.data
}
if (order.value.assignor !== null) getAssignorName()
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
              v-if="order.status === 1 && order.assignor !== null"
              class="body-right"
              style="color: #f86b1d"
            >
              接受转单
            </span>
            <span
              v-else-if="order.status === 1"
              class="body-right"
              style="color: #ee3f4d"
            >
              抢单
            </span>
          </div>
        </nut-col>
      </nut-row>
      <nut-divider dashed />
      <p
        v-if="order.status === 1 && order.assignor !== null"
        class="body-right"
        style="
          color: #f86b1d;
          margin: 0;
          text-align: right;
          font-size: 12px;
          font-weight: bold;
        "
      >
        ---由{{ assignor }}转出
      </p>
      <p class="order-describe">{{ order.orderDescribe }}</p>
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

    .order-describe {
      text-align: center;
      word-wrap: break-word;
      overflow-wrap: break-word;
      margin: 8px auto;
      font-size: 18px;
    }
  }
}
</style>
