<script setup>
import { Agreement, EditOne, Tool, Toolkit } from '@icon-park/vue-next'
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
const route = useRoute()
const router = useRouter()

const current = ref(1)
const onClickStep = (index) => {
  if (index < current.value && index !== 3) {
    current.value = index
    if (index === 1) router.push('/')
  }
}

watch(
  () => route.fullPath, // 监视的属性
  (newPath) => {
    if (newPath.includes('/orders/notice')) current.value = 1
    else if (newPath.includes('/orders/from')) current.value = 2
    else if (newPath.includes('/orders/wait')) current.value = 3
  },
  { immediate: true }
)
</script>

<template>
  <div class="new-order-container">
    <div class="new-order-head">
      <img src="@/assets/orders/images/news_top.png" alt="图标" />
      <div class="new-order-from">
        <nut-steps :current="current" @click-step="onClickStep">
          <nut-step title="服务须知">
            <template #icon>
              <agreement class="step-icon" theme="outline" size="12" />
            </template>
          </nut-step>
          <nut-step title="信息填写">
            <template #icon>
              <edit-one class="step-icon" theme="outline" size="12" />
            </template>
          </nut-step>
          <nut-step title="等待修单">
            <template #icon>
              <tool class="step-icon" theme="outline" size="12" />
            </template>
          </nut-step>
        </nut-steps>
        <router-view></router-view>
        <div class="tool">
          <toolkit
            @click="router.push('/tool')"
            class="icon-center"
            theme="two-tone"
            size="20"
            :fill="['#9b9b9b', '#4a90e2']"
            strokeLinejoin="miter"
          />
          <span @click="router.push('/tool')"> 软件工具箱</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
