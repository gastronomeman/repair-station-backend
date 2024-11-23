<script setup>
import { useRouter } from 'vue-router'
import { ref, onBeforeMount } from 'vue'
import { getSubCodeService } from '@/api/sub.js'

const router = useRouter()
const onClick = () => {
  router.push('/staff/profile')
}

// 用来显示进度的变量
const progress = ref(0)

const v = ref({
  code: '',
  time: ''
})

const parts = ref([])
const getVerificationCode = async () => {
  try {
    // 发起请求获取验证码信息
    const resp = await getSubCodeService()

    if (resp.code === 1 && resp.data) {
      v.value = resp.data
      // 按逗号分割字符串
      parts.value = v.value.code.split(',')
      parts.value[0] = parts.value[0].split('').join(' ')

      // 计算剩余的有效时间（秒）
      let remainingTime = (v.value.time - Date.now()) / 1000
      progress.value = (remainingTime / 70) * 100

      // 每秒更新进度条
      const interval = setInterval(async () => {
        remainingTime = remainingTime - 1
        if (remainingTime <= 0) {
          clearInterval(interval) // 时间到，停止更新
          progress.value = 100
          await getVerificationCode()
        } else {
          // 计算剩余时间的进度条百分比
          progress.value = (remainingTime / 70) * 100
        }
      }, 1000) // 每秒更新一次
    } else {
      console.log('验证码获取失败或数据不正确')
    }
  } catch (error) {
    console.error('获取验证码失败', error)
  }
}
// 在组件挂载之前执行
onBeforeMount(() => {
  getVerificationCode() // 获取验证码
})

const loading = ref(false)
const onRefresh = async () => {
  await getVerificationCode()
  loading.value = false
}
</script>

<template>
  <van-pull-refresh
    v-model="loading"
    success-text="刷新成功"
    @refresh="onRefresh"
  >
    <nut-sticky>
      <nut-navbar title="活动验证码" left-show @click-back="onClick">
        <template #left>
          <div>返回</div>
        </template>
      </nut-navbar>
    </nut-sticky>
    <div class="progress">
      <nut-space>
        <!-- 显示验证码进度 -->
        <nut-circle-progress :radius="180" :progress="progress" color="skyblue">
          <p style="text-align: center; font-size: 22px">
            <span style="color: red">{{ parts[0] }}</span>
            <br />
            {{ parts[1] }}<br />
            <span style="font-size: 13px"> *红字为验证码 </span>
          </p>
        </nut-circle-progress>
      </nut-space>
    </div>
  </van-pull-refresh>
</template>

<style scoped>
.progress {
  text-align: center;
  margin: 20px 0;
}
</style>
