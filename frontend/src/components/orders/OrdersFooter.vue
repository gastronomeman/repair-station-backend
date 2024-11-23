<script setup>
import Footer from '@/components/Footer.vue'
</script>

<template>
  <br />
  <nut-divider>
    <p style="text-align: center; max-width: 500px; width: 65%; margin: 0">
      关注我们获取更多维修信息，加入我们进入一个温暖社团
    </p>
  </nut-divider>
  <div class="order-footer">
    <img src="@/assets/qr_code.jpg" alt="" />
    <Footer></Footer>
  </div>
</template>

<style scoped>
.order-footer {
  margin: 0 auto 15px;
  max-width: 450px;
  img {
    width: 100%;
  }
}
</style>
