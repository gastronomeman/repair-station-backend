<script setup>
import { useRouter } from 'vue-router'
import { User, DegreeHat, School, PhoneTelephone } from '@icon-park/vue-next'
import { ref } from 'vue'
import { useStaffState } from '@/stores/index.js'
import { completeInfoService, getInfoService } from '@/api/staff.js'
import { successMsg, warningMsg } from '@/utils/SendMsgUtils.js'
import { checkPhoneNone, isNotBlank } from '@/utils/StringUtils.js'

const router = useRouter()
const staffState = useStaffState()

const onClick = () => {
  router.push('/staff/profile')
}

const staff = ref({
  major: '',
  phone: '',
  politicalStatus: ''
})

const loading = ref(false)
const completeInfo = async () => {
  if (
    isNotBlank(staff.value.major) ||
    isNotBlank(staff.value.politicalStatus) ||
    isNotBlank(staff.value.phone)
  ) {
    warningMsg('填写的信息不能为空')
    return
  }

  if (checkPhoneNone(staff.value.phone)) {
    warningMsg('手机号错误！')
    return
  }

  loading.value = true
  staff.value.id = staffState.id
  const resp = await completeInfoService(staff.value)
  if (resp.code === 1) {
    successMsg(resp.data)
  }
  loading.value = false
}

const getInfo = async () => {
  const resp = await getInfoService(staffState.id)
  if (resp.code === 1) {
    staff.value.major = resp.data.major
    staff.value.politicalStatus = resp.data.politicalStatus
    staff.value.phone = resp.data.phone
  }
}
getInfo()
</script>

<template>
  <nut-sticky>
    <nut-navbar title="个人信息" left-show @click-back="onClick">
      <template #left>
        <div>返回</div>
      </template>
    </nut-navbar>
  </nut-sticky>

  <el-form class="from" label-width="auto">
    <el-form-item label="姓名：">
      <el-input v-model="staffState.name" clearable disabled>
        <template #prefix>
          <el-icon class="input-icon">
            <user />
          </el-icon>
        </template>
      </el-input>
    </el-form-item>

    <el-form-item label="学号：">
      <el-input v-model="staffState.studentId" clearable disabled>
        <template #prefix>
          <el-icon class="input-icon">
            <degree-hat />
          </el-icon>
        </template>
      </el-input>
    </el-form-item>

    <el-form-item required label="班级：">
      <el-input
        v-model="staff.major"
        clearable
        placeholder="例：22Java1，22嵌入式2"
      >
        <template #prefix>
          <el-icon class="input-icon">
            <school />
          </el-icon>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item required label="手机号：">
      <el-input v-model="staff.phone" clearable placeholder="请输入手机号">
        <template #prefix>
          <el-icon class="input-icon">
            <phone-telephone />
          </el-icon>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item required label="政治面貌：">
      <el-select v-model="staff.politicalStatus" placeholder="请点击选择">
        <el-option :key="1" :value="1" label="群众" />
        <el-option :key="2" :value="2" label="党员" />
        <el-option :key="3" :value="3" label="共青团员" />
        <el-option :key="4" :value="4" label="其他" />
      </el-select>
    </el-form-item>
    <div class="btn">
      <nut-button
        :loading="loading"
        shape="round"
        type="info"
        @click="completeInfo"
      >
        保存
      </nut-button>
      <p>
        *请按照示例来填写，班级的填写时不要带“班”字在后面<br />
        例：22Java1，22嵌入式2 <br />
        所有信息请认真填写，否则可能录不上分。<br />
        ^ ̳ᴗ ̫ ᴗ ̳^
      </p>
    </div>
  </el-form>
</template>

<style scoped>
.from {
  width: 90%;
  max-width: 320px;
  margin: 20px auto;

  .btn {
    margin: 15px auto 0;
    text-align: center;
    width: 85%;

    p {
      font-size: 14px;
      color: red;
    }
  }
}
</style>
