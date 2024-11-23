<script setup>
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import { uploaderListService } from '@/api/orders.js'
import { isNotBlank } from '@/utils/StringUtils.js'
import { warningMsg } from '@/utils/SendMsgUtils.js'
import OrderPhotoList from '@/components/orders/OrderPhotoList.vue'

const router = useRouter()
const onClick = () => router.push('/staff/profile')

const ordersList = ref([])
const orders = ref('')
const loading = ref(false)

const getOrdersList = async () => {
  loading.value = true
  const resp = await uploaderListService()
  if (resp.code === 1) {
    ordersList.value = resp.data
  }
  loading.value = false
}

const goToUPhoto = () => {
  if (isNotBlank(orders.value)) {
    warningMsg('没有选择订单号')
    return
  }
  router.push(
    `/staff/profile/uploader-photo?id=${orders.value.id}&name=${orders.value.orderDescribe}`
  )
}
</script>

<template>
  <nut-navbar title="上传拆机照片" left-show @click-back="onClick">
    <template #left>
      <div>返回</div>
    </template>
  </nut-navbar>
  <div class="upload">
    <h4>选择维修单</h4>
    <el-select
      @click.prevent="getOrdersList"
      v-model="orders"
      placeholder="选择需要拆机的订单"
      size="large"
      style="width: 240px"
    >
      <el-option
        v-for="item in ordersList"
        :key="item.id"
        :label="`${item.name}：${item.studentId}`"
        :value="item"
      />
    </el-select>
    <p style="color: red">
      *图片都需要提交，切勿弄虚作假，否则后期与机主发生纠纷，全由社员自身负责
    </p>
    <nut-button @click="goToUPhoto" size="large" type="primary"
      >上传图片</nut-button
    >
  </div>
  <div class="list">
    <OrderPhotoList></OrderPhotoList>
  </div>
</template>

<style scoped>
.upload {
  max-width: 800px;
  width: 80%;
  margin: 20px auto 0;
  text-align: center;
  h4 {
    margin: 5px 0;
  }
  p {
    color: #7a7374;
    font-size: 12px;
    margin: 8px 0;
  }
  .nut-button {
    margin-bottom: 15px;
  }
}
</style>
