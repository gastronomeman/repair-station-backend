<script setup>
import { useRouter, useRoute } from 'vue-router'
import { useAdminState } from '@/stores/index.js'
import { ref, watch } from 'vue'

const adminState = useAdminState()

const router = useRouter()
const route = useRoute()

const onClick = () => {
  if (
    route.fullPath.startsWith('/admin/profile/exam/sub') ||
    route.fullPath.startsWith('/admin/profile/exam/status')
  ) {
    router.push('/admin/profile')
    return
  }
  router.go(-1)
}

watch(
  () => adminState.subTabs,
  async (t) => {
    if (
      route.fullPath.startsWith('/admin/profile/exam/sub') ||
      route.fullPath.startsWith('/admin/profile/exam/status')
    ) {
      if (t === '1') {
        await router.push('/admin/profile/exam/status')
      } else if (t === '2') {
        await router.push('/admin/profile/exam/sub')
      }
    }
  },
  { immediate: true }
)

const show = ref(true)
watch(
  () => route.fullPath,
  async (t) => {
    show.value = !!(
      t.startsWith('/admin/profile/exam/sub') ||
      t.startsWith('/admin/profile/exam/status')
    )
  },
  { immediate: true }
)
</script>

<template>
  <nut-sticky>
    <nut-navbar title="维修站答题系统" left-show @click-back="onClick">
      <template #left>
        <div>返回</div>
      </template>
    </nut-navbar>
  </nut-sticky>
  <nut-tabs v-show="show" v-model="adminState.subTabs" type="smile">
    <nut-tab-pane title="答题设置" pane-key="1"> </nut-tab-pane>
    <nut-tab-pane title="题目设置" pane-key="2"> </nut-tab-pane>
  </nut-tabs>
  <router-view></router-view>
</template>

<style scoped>
.nut-tab-pane {
  padding: 0 !important;
  background-color: #f0f0f0;
}
</style>
