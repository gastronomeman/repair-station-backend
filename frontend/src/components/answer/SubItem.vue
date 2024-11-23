<script setup>
import { ref } from 'vue'
import { useAdminState } from '@/stores/index.js'
import { useRouter } from 'vue-router'

const router = useRouter()
const props = defineProps({
  sub: {
    type: Object,
    required: true
  }
})

const adminState = useAdminState()
const sub = ref(props.sub)

const setStateSub = () => {
  adminState.setSub(sub.value)
  router.push('/admin/profile/exam/set')
}
</script>

<template>
  <nut-row>
    <nut-col :span="20">
      <div @click="setStateSub" class="content">{{ sub.topic }}</div>
    </nut-col>
    <nut-col :span="4">
      <div class="btn">
        <nut-button size="mini" type="warning" @click="setStateSub">
          修改
        </nut-button>
      </div>
    </nut-col>
  </nut-row>
  <nut-divider />
</template>

<style scoped>
.content {
  cursor: pointer;
}
.btn {
  text-align: right;
}
.nut-divider {
  margin: 2px auto 15px;
}
</style>
