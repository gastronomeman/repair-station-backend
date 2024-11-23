<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import AnswerItem from '@/components/answer/AnswerItem.vue'
import { useExamState } from '@/stores/index.js'
import { useRouter } from 'vue-router'
import { getRandomSubListService } from '@/api/sub.js'
import { confirmDialog } from '@/utils/DialogUtils.js'

const router = useRouter()

const examState = useExamState()
const subList = ref([])

const getRandomSubList = async () => {
  const resp = await getRandomSubListService()
  if (resp.code === 1) {
    subList.value = resp.data
    examState.setResultLength(subList.value.length)
  }
}
getRandomSubList()

const swiperRef = ref()
const handlePrev = () => {
  swiperRef.value?.prev()
}
const handleNext = () => {
  swiperRef.value?.next()
}

let isSu = false
const progress = computed(() => {
  let i = 0
  examState.result.forEach((item) => {
    if (item !== null) i++
  })
  if (i === subList.value.length && !isSu) {
    isSu = true
    submit()
  }
  return ((i / subList.value.length) * 100).toFixed(2)
})

const time = ref(0) // 计时的秒数
const timer = ref(null)

const formattedTime = computed(() => {
  const minutes = Math.floor(time.value / 60)
  const seconds = time.value % 60
  return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`
})
onMounted(() => {
  timer.value = setInterval(() => {
    time.value++
  }, 1000) // 每秒增加1秒
})
onBeforeUnmount(() => {
  clearInterval(timer.value) // 清理计时器
})

const submit = async () => {
  if (await confirmDialog('提示', '是否确定提交？')) {
    examState.setTime(time.value)
    await router.push('/exam/settlement')
  }
}
</script>

<template>
  <div class="swiper-demo">
    <div class="title">
      <div class="time">
        {{ formattedTime }}
      </div>
      <div class="content">欢迎你！{{ examState.student.name }}同学</div>
      <div class="submit">
        <nut-button
          size="mini"
          shape="round"
          type="info"
          @click.prevent="submit"
        >
          提交
        </nut-button>
      </div>
    </div>

    <nut-swiper
      :isPreventDefault="false"
      :loop="false"
      ref="swiperRef"
      :init-page="0"
    >
      <nut-swiper-item v-for="(item, index) in subList" :key="index">
        <div class="progress">
          <nut-progress
            :percentage="progress"
            stroke-color="linear-gradient(270deg, rgba(18,126,255,1) 0%,rgba(32,147,255,1) 32.815625%,rgba(13,242,204,1) 100%)"
            status="active"
          />
        </div>
        <AnswerItem
          @choice="handleNext"
          class="answer-item"
          :sub="item"
          :index="index"
        ></AnswerItem>
      </nut-swiper-item>
    </nut-swiper>

    <div class="swiper-btns">
      <div class="swiper-btn">
        <nut-button shape="round" type="info" @click.prevent="handlePrev">
          上一题
        </nut-button>
        &nbsp;
        <nut-button shape="round" type="info" @click.prevent="handleNext">
          下一题
        </nut-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.title {
  width: 88%;
  max-width: 500px;
  margin: 8px auto 5px;
  color: #7a7374;
  font-size: 14px;
  font-weight: bold;
  display: flex; /* 使用 Flexbox 布局 */
  justify-content: space-between; /* 左中右排列 */
  align-items: center; /* 垂直居中 */
  height: 24px;

  .time {
    flex: 1; /* 使每个子元素在主轴方向上平分可用空间 */
    text-align: left; /* 文字居中 */
  }
  .content {
    flex: 2; /* 使每个子元素在主轴方向上平分可用空间 */
    text-align: center; /* 文字居中 */
  }
  .submit {
    flex: 1; /* 使每个子元素在主轴方向上平分可用空间 */
    text-align: right; /* 文字居中 */
  }
}
.progress {
  width: 88%;
  max-width: 500px;
  margin: 0 auto;
}
.swiper-demo {
  .swiper-btns {
    text-align: center;
    max-width: 500px;
    width: 88%;
    margin: 0 auto;

    .icon {
      height: 100%;
      display: inline-flex;
      align-items: center;
      justify-content: center;
    }
  }
}
</style>
