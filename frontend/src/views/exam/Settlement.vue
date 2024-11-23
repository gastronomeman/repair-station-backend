<script setup>
import { useExamState } from '@/stores/index.js'
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { Classroom } from '@icon-park/vue-next'
import { checkCodeService } from '@/api/student.js'
import { successMsg } from '@/utils/SendMsgUtils.js'

const router = useRouter()
const examState = useExamState()
if (examState.student === null) router.push('/exam')

const right = computed(() => {
  let i = 0
  examState.result.forEach((item) => {
    if (item === true) i++
  })

  return i
})

const checkCode = async () => {
  examState.student.score = examState.time
  const resp = await checkCodeService(examState.student)
  if (resp.code === 1) {
    successMsg(resp.data)

    examState.clear()
    await router.push('/')
  }
}
</script>

<template>
  <div class="result">
    恭喜你答对了{{ right }}题<br />
    花了{{ examState.time }}秒
  </div>
  <div style="text-align: center">
    <nut-button
      v-show="+examState.status.pass > right"
      type="warning"
      size="small"
      @click="router.push('/exam/answer')"
    >
      重新回答
    </nut-button>
    <div class="enter" v-show="+examState.status.pass <= right">
      <p>*去找工作人员索要验证码录入加分吧</p>
      <el-input
        v-model="examState.student.code"
        clearable
        style="width: 250px; margin: 5px auto 10px"
      >
        <template #prefix>
          <el-icon class="input-icon">
            <classroom />
          </el-icon>
        </template>
      </el-input>
      <br />
      <nut-button type="info" @click.prevent="checkCode"> 提交 </nut-button>
    </div>
  </div>
</template>

<style scoped>
.result {
  text-align: center;
  width: 80%;
  margin: 20px auto;
  color: #3a3a3c;
}
.enter {
  color: #3a3a3c;
  font-size: 12px;
  p {
    margin: 5px auto;
  }
}
</style>
