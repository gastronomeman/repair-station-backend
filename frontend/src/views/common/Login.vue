<script setup>
import { ref } from 'vue'
import { isNotBlank } from '@/utils/StringUtils.js'
import md5 from 'js-md5'
import { successMsg, warningMsg } from '@/utils/SendMsgUtils.js'
import { staffLoginService } from '@/api/staff.js'
import { useStaffState } from '@/stores/index.js'
import { useRouter } from 'vue-router'
import { User, Key } from '@icon-park/vue-next'

import { jwtDecode } from 'jwt-decode'

const router = useRouter()
const isLoading = ref(false)

const staffState = useStaffState()
const staff = ref({
  studentId: '',
  password: ''
})

const checkPassword = () => {
  if (isNotBlank(staff.value.password)) {
    staff.value.password = ''
    return
  }
  staff.value.password = md5(staff.value.password)
}

const submit = async () => {
  isLoading.value = true
  if (isNotBlank(staff.value.studentId) || isNotBlank(staff.value.password)) {
    warningMsg('账号密码不能为空！')
    staff.value.password = ''
    isLoading.value = false
    return
  }
  const resp = await staffLoginService(staff.value)
  if (resp.code === 1) {
    staffState.setToken(resp.data)
    staffState.setStudentId(staff.value.studentId)
    staffState.setName(jwtDecode(resp.data).name)
    staffState.setId(jwtDecode(resp.data).id)

    let name = jwtDecode(resp.data).name

    if (staff.value.studentId === 'admin') {
      successMsg('登陆成功！')
      await router.push('/admin')
    } else {
      if (name.length === 3) name = name.slice(-2)

      successMsg(`Hi,${name}同学 ^o^ !`)

      await router.push('/staff')
    }
  } else {
    staff.value.studentId = ''
    staff.value.password = ''
  }
  isLoading.value = false
}

const checkJWT = () => {
  if (staffState.token !== '' && staffState.name !== 'admin') {
    router.push('/staff')
  } else if (staffState.token !== '') {
    router.push('/admin')
  }
}
checkJWT()
</script>

<template>
  <div class="login-container">
    <div class="login-img-logo">
      <img src="@/assets/logos.png" alt="Logo" />
    </div>
    <form class="login-input">
      <h2>ITeam维修站后台系统</h2>
      <p
        style="margin: 8px; font-size: 12px; color: #7a7374; text-align: center"
      >
        维修站永远欢迎各位师兄师姐，师弟师妹哦！<br />
        o(^▽^)o
      </p>
      <el-input
        clearable
        size="large"
        v-model="staff.studentId"
        style="width: 260px"
        placeholder="管理员学号："
      >
        <template #prefix>
          <el-icon class="input-icon"><user /></el-icon>
        </template>
      </el-input>
      <br />
      <el-input
        clearable
        type="password"
        size="large"
        v-model="staff.password"
        style="width: 260px; margin-top: 5px"
        placeholder="管理员密码："
        @blur="checkPassword"
      >
        <template #prefix>
          <el-icon class="input-icon"><key /></el-icon>
        </template>
      </el-input>
      <div class="btn">
        <nut-button
          :loading="isLoading"
          shape="round"
          type="info"
          @click="submit"
          >登录</nut-button
        >
        <p>*仅限ITeam基地维修站成员使用，请妥善保管个人账号和密码。(*^o^*)</p>
      </div>
    </form>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 80vh;
  width: 100%;
  margin: 6vh auto 50px;

  .login-img-logo {
    width: 250px;
  }

  .login-img-logo img {
    width: 100%;
    height: auto;
  }

  .login-input {
    text-align: center;
    h2 {
      margin: 0;
    }

    .el-input:first-child {
      margin-bottom: 10px;
    }
    .btn {
      margin: 15px auto 0;
      text-align: center;
      width: 240px;

      p {
        color: #7a7374;
      }
    }
  }
}
</style>
