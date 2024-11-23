<script setup>
import { ref, defineEmits } from 'vue'
import { useExamState } from '@/stores/index.js'

const examState = useExamState()

const props = defineProps({
  index: {
    type: Number,
    required: true
  },
  sub: {
    type: Object,
    required: true
  }
})

const sub = ref(props.sub)
const index = ref(props.index)

const radio = ref('')

const emit = defineEmits(['choice'])

const onChange = (s) => {
  if (sub.value.result == s) {
    examState.setResult(+index.value, true)
  } else {
    examState.setResult(+index.value, false)
  }

  let i = 0
  examState.result.forEach((item) => {
    if (item === null) i++
  })
  if (i === 0) return

  //设置延时增加体验
  setTimeout(() => {
    emit('choice')
  }, 200)
}
</script>

<template>
  <div>
    <div class="content">
      <p>
        <span class="index">&nbsp;{{ index + 1 }}.&nbsp;</span>
        {{ sub.topic }}
      </p>
    </div>
    <div class="radio">
      <nut-radio-group v-model="radio" @change="onChange">
        <nut-radio class="tag" label="1"> A. {{ sub.option1 }} </nut-radio>
        <nut-radio class="tag" label="2"> B. {{ sub.option2 }} </nut-radio>
        <nut-radio class="tag" label="3"> C. {{ sub.option3 }} </nut-radio>
        <nut-radio class="tag" label="4"> D. {{ sub.option4 }} </nut-radio>
      </nut-radio-group>
    </div>
  </div>
</template>

<style scoped>
.content {
  width: 88%;
  max-width: 500px;
  margin: 15px auto 0;
  p {
    margin: 0;
    .index {
      background-color: #93d5dc;
      border-radius: 10px;
      padding: 1px;
      margin: 3px;
    }
  }
}

.radio {
  touch-action: manipulation;
  max-width: 500px;
  width: 88%;
  margin: auto;
  z-index: 999;
  .tag {
    margin: 15px 0;
  }
}
</style>
