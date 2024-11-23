<script setup>
import { useRouter, useRoute } from 'vue-router'
import { ref } from 'vue'
import md5 from 'js-md5'
import { successMsg, warningMsg } from '@/utils/SendMsgUtils.js'
import { isNotBlank, checkPassword } from '@/utils/StringUtils.js'
import { useStaffState } from '@/stores/index.js'
import { changPasswordService, logoutService } from '@/api/staff.js'
import { dialog } from '@/utils/DialogUtils.js'

const staffState = useStaffState()
const router = useRouter()
const route = useRoute()

const onClick = () => {
  if (route.fullPath.includes('admin')) router.push('/admin/profile')
  else router.push('/staff/profile')
}

const password = ref({
  oldPassword: '',
  newPassword: '',
  newPasswordConfirm: ''
})

const hashPasswordField = (field) => {
  if (checkPassword(password.value[field])) {
    warningMsg('密码格式不正确，请重新输入！')
    password.value[field] = ''
    return
  }
  if (isNotBlank(password.value[field])) {
    password.value[field] = ''
  } else {
    password.value[field] = md5(password.value[field])
  }
}

const checkOldPassword = () => hashPasswordField('oldPassword')
const checkNewPassword = () => hashPasswordField('newPassword')
const checkNewPasswordConfirm = () => hashPasswordField('newPasswordConfirm')

const updateClick = async () => {
  if (
    isNotBlank(password.value.newPassword) ||
    isNotBlank(password.value.newPasswordConfirm) ||
    isNotBlank(password.value.oldPassword)
  ) {
    warningMsg('密码框不能为空！')
    return
  }
  if (password.value.newPassword !== password.value.newPasswordConfirm) {
    warningMsg('新密码两次不一致，请重新尝试')
    return
  }

  const resp = await changPasswordService(password.value)
  if (resp.code === 1) {
    successMsg(resp.data)
    dialog('修改成功，请重新登录')
    await logoutService()
    staffState.clear()
    await router.push('/staff/login')
  }
}
</script>

<template>
  <nut-sticky>
    <nut-navbar title="修改密码" left-show @click-back="onClick">
      <template #left>
        <div>返回</div>
      </template>
    </nut-navbar>
  </nut-sticky>
  <div class="chang-password-img">
    <img src="@/assets/rs_logo.png" alt="Logo" />
  </div>

  <div class="chang-password">
    <div class="chang-password-title">旧密码：</div>
    <el-input
      type="password"
      v-model="password.oldPassword"
      style="width: 260px"
      placeholder="请输入旧密码"
      clearable
      @blur="checkOldPassword"
    />
    <div class="chang-password-title">新密码：</div>
    <el-input
      type="password"
      v-model="password.newPassword"
      style="width: 260px"
      placeholder="新密码要由数字，字母组成"
      clearable
      @blur="checkNewPassword"
    />
    <div class="chang-password-title">再次输入：</div>
    <el-input
      type="password"
      v-model="password.newPasswordConfirm"
      style="width: 260px"
      placeholder="新密码不能少于6位，多于18位"
      clearable
      @blur="checkNewPasswordConfirm"
    />
  </div>
  <div class="change-password-button">
    <nut-button @click="updateClick" shape="round" type="info">修改</nut-button>
    <p>*为了您的账号安全，至少更改一次密码，请妥善保管个人账号。</p>
  </div>
</template>

<style scoped>
.chang-password-img {
  width: 30%;
  margin: 20px auto 0;
  text-align: center;
  img {
    width: 100%;
  }
}
.chang-password {
  width: 80%;
  margin: 0 auto;
  text-align: center;
  .chang-password-title:first-child {
    margin: 30px auto 5px;
    width: 260px;
    text-align: left;
  }
  .chang-password-title {
    margin: 15px auto 5px;
    width: 260px;
    text-align: left;
  }
}

.change-password-button {
  width: 80%;
  margin: 20px auto 150px;
  text-align: center;
  p {
    width: 80%;
    margin: 8px auto;
    font-size: 12px;
    color: #7a7374;
  }
}
</style>
