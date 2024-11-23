<script setup>
import icon from '@/assets/logos.png'
import { ref } from 'vue'
import { useExamState } from '@/stores/index.js'
import { useRouter } from 'vue-router'
import { getSubStatusService } from '@/api/sub_status.js'

const router = useRouter()

const examState = useExamState()
const show = ref(false)

const handleAgree = async () => {
  if (examState.agreed) {
    const toast = showLoadingToast({
      message: '跳转中...',
      forbidClick: true,
      loadingType: 'spinner',
      duration: 0
    })
    await router.push('/exam/stuInfo')
    toast.close()
  } else {
    alert('请先认真阅读，服务须知')
  }
}
const subStatus = ref({
  notice: '',
  number: '',
  isOpen: ''
})
const getSubStatus = async () => {
  const resp = await getSubStatusService()
  if (resp.code === 1) {
    subStatus.value = resp.data
  }
}
getSubStatus()
</script>

<template>
  <div class="image">
    <van-image width="100%" :src="icon" />
  </div>
  <van-overlay
    :show="show"
    @click="show = false"
    z-index="999"
    :lock-scroll="false"
  >
    <div class="wrapper">
      <div class="orders-notice" @click.stop>
        <el-scrollbar
          ref="scrollbarRef"
          max-height="50vh"
          :always="true"
          class="text"
        >
          <div ref="innerRef">
            <h2 style="margin: 10px 0 5px">活动公告</h2>
            <p class="notice" v-html="subStatus.notice"></p>
          </div>
        </el-scrollbar>

        <div class="read-service">
          <label>
            <input type="checkbox" v-model="examState.agreed" />
            已仔细阅读，并了解其活动规则
          </label>
          <p style="margin: 5px; font-size: 12px; color: #7a7374">
            *了解活动规则后方可以进行答题
          </p>
        </div>
        <nut-button size="small" shape="round" type="info" @click="handleAgree"
          >同意须知
        </nut-button>
      </div>
    </div>
  </van-overlay>
  <div class="image">
    <nut-button size="large" type="info" @click="show = true">
      开始答题
    </nut-button>
  </div>
</template>

<style scoped>
.image {
  width: 80%;
  max-width: 450px;
  text-align: center;
  margin: 5vh auto;
}
.wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;

  .orders-notice {
    text-align: center;
    width: 85%;
    margin: 0 auto;
    border-radius: 10px;
    padding: 20px;
    background-color: #f0f0f0;

    .text {
      border-radius: 10px;
      background-color: white;
      border: 1px solid #7a7374;

      .notice {
        padding: 0 12px 15px;
        text-align: left;
        margin: 0 auto;
      }
    }

    .read-service {
      margin-top: 5px;
      .read-service label {
        cursor: pointer;
      }
    }

    .nut-button {
      margin: 5px 0 0 0;
      width: 80%;
      height: 45px;
    }
  }
}
</style>
