<script setup>
import { useRouter, useRoute } from 'vue-router'
import {
  getStaffOnlineCountService,
  getStaffOnlineNameService,
  logoutService
} from '@/api/staff.js'
import { useCommonState, useStaffState } from '@/stores/index.js'
import { CameraOne, Right, Logout } from '@icon-park/vue-next'
import { ref } from 'vue'
import { successMsg } from '@/utils/SendMsgUtils.js'

const router = useRouter()
const route = useRoute()
const staffState = useStaffState()
const commonState = useCommonState()

const logoutUser = async () => {
  await logoutService()
  staffState.clear()
  commonState.clear()
  successMsg('退出成功！')
  await router.push('/staff/login')
}

const nameList = ref()
const getOnlineName = async () => {
  const resp = await getStaffOnlineNameService()
  if (resp.code === 1) {
    nameList.value = resp.data.join(', ')
  }
}
const onlineCount = ref(0)
const getStaffOnlineCount = async () => {
  const resp = await getStaffOnlineCountService()
  if (resp.code === 1) {
    onlineCount.value = resp.data
  }
}
getStaffOnlineCount()
getOnlineName()
</script>

<template>
  <div v-if="route.fullPath === '/admin/profile'">
    <nut-navbar title="我的"></nut-navbar>
    <div class="info">
      <div class="onlineTitle">
        <camera-one theme="two-tone" size="24" :fill="['#9b9b9b', '#0de38a']" />
        <br />
        <span>当前在线社员:</span><br />
        <span>{{ onlineCount }} 人</span>
      </div>
      <span class="onlineName">
        {{ nameList }}
      </span>
    </div>
    <nut-navbar
      style="cursor: pointer"
      @click="router.push('/admin/profile/order-list')"
    >
      <template #left><span class="nav-title">所有维修单</span></template>
      <template #right
        ><right
          class="icon-center"
          theme="two-tone"
          size="25"
          strokeLinejoin="miter"
      /></template>
    </nut-navbar>
    <nut-navbar
      style="cursor: pointer"
      @click="router.push('/admin/profile/add-member')"
    >
      <template #left><span class="nav-title">添加社员</span></template>
      <template #right
        ><right
          class="icon-center"
          theme="two-tone"
          size="25"
          strokeLinejoin="miter"
      /></template>
    </nut-navbar>
    <nut-navbar
      style="cursor: pointer"
      @click="router.push('/admin/profile/photo-dir')"
    >
      <template #left><span class="nav-title">拆机照片文件夹</span></template>
      <template #right
        ><right
          class="icon-center"
          theme="two-tone"
          size="25"
          strokeLinejoin="miter"
      /></template>
    </nut-navbar>
    <nut-navbar
      style="cursor: pointer"
      @click="router.push('/admin/profile/transfer-database')"
    >
      <template #left>
        <span class="nav-title">转存数据库（每一学年末，必使用）</span>
      </template>
      <template #right
        ><right
          class="icon-center"
          theme="two-tone"
          size="25"
          strokeLinejoin="miter"
      /></template>
    </nut-navbar>
    <nut-navbar
      style="cursor: pointer"
      @click="router.push('/admin/profile/change-password')"
    >
      <template #left><span class="nav-title">修改登录密码</span></template>
      <template #right
        ><right
          class="icon-center"
          theme="two-tone"
          size="25"
          strokeLinejoin="miter"
      /></template>
    </nut-navbar>
    <nut-navbar style="cursor: pointer" @click="router.push('/')">
      <template #left><span class="nav-title">报修页</span></template>
      <template #right
        ><right
          class="icon-center"
          theme="two-tone"
          size="25"
          strokeLinejoin="miter"
      /></template>
    </nut-navbar>
    <nut-navbar
      style="cursor: pointer"
      @click="router.push('/admin/profile/toolbox')"
    >
      <template #left><span class="nav-title">软件工具箱</span></template>
      <template #right
        ><right
          class="icon-center"
          theme="two-tone"
          size="25"
          strokeLinejoin="miter"
      /></template>
    </nut-navbar>
    <nut-navbar
      style="cursor: pointer; border: 0"
      @click="router.push('/admin/profile/exam/status')"
    >
      <template #left><span class="nav-title">题库系统</span></template>
      <template #right
        ><right
          class="icon-center"
          theme="two-tone"
          size="25"
          strokeLinejoin="miter"
      /></template>
    </nut-navbar>
    <nut-navbar
      style="cursor: pointer; border: 0"
      @click="router.push('/admin/profile/help')"
    >
      <template #left><span class="nav-title">帮助</span></template>
      <template #right
        ><right
          class="icon-center"
          theme="two-tone"
          size="25"
          strokeLinejoin="miter"
      /></template>
    </nut-navbar>
    <nut-navbar class="footer-navbar" @click="logoutUser" title-icon>
      <template #content>
        <span
          style="
            font-weight: bold;
            font-size: 15px;
            color: black;
            margin-right: 5px;
          "
          >退出</span
        >
        <logout
          class="icon-center"
          theme="filled"
          size="20"
          fill="#000000"
          strokeLinejoin="miter"
        />
      </template>
    </nut-navbar>
  </div>
  <router-view></router-view>
</template>

<style scoped>
.nut-navbar--placeholder {
  border-bottom: 1px solid #f0f0f0;
  .nav-title {
    margin-left: 8px;
    color: black;
    font-size: 15px;
  }
}
.title {
  margin: 0;
  border: 0;
}
.info {
  background-color: white;
  padding: 10px 10px;
  margin-bottom: 18px;
  display: flex;
  align-items: center;
  .onlineTitle {
    margin: 5px;
    text-align: center;
    width: 85px;
    font-size: 10px;
    color: #939393;
  }
  .onlineName {
    margin: 5px;
    font-size: 12px;
    font-weight: bold;
    color: #302f4b;
  }
}
.icon-center {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
.footer-navbar {
  border: 0;
  margin-top: 18px;
}
</style>
