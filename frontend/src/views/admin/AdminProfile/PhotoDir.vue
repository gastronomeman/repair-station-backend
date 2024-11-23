<script setup>
import OrderPhotoList from '@/components/orders/OrderPhotoList.vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const onClick = () => {
  router.go(-1)
}
</script>

<template>
  <nut-sticky>
    <nut-navbar title="拆机照片文件夹" left-show @click-back="onClick">
      <template #left>
        <div>返回</div>
      </template>
    </nut-navbar>
  </nut-sticky>
  <div class="list">
    <OrderPhotoList></OrderPhotoList>
  </div>
</template>

<style scoped>
.list {
  margin-top: 30px;
}
</style>
