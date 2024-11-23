<script setup>
import { ref } from 'vue'
import {
  getSubStatusService,
  updateSubStatusService
} from '@/api/sub_status.js'
import { successMsg } from '@/utils/SendMsgUtils.js'

const subStatus = ref({
  notice: '',
  number: '',
  pass: '',
  isOpen: false
})

const getSubStatus = async () => {
  const resp = await getSubStatusService()
  if (resp.code === 1) {
    subStatus.value = resp.data
    subStatus.value.isOpen = subStatus.value.isOpen === 0
  }
}
getSubStatus()

const updateSubStatus = async () => {
  const resp = await updateSubStatusService({
    id: 0,
    notice: subStatus.value.notice,
    number: subStatus.value.number,
    pass: subStatus.value.pass,
    isOpen: subStatus.value.isOpen === true ? 0 : 1
  })
  if (resp.code === 1) {
    successMsg('更新成功')
    subStatus.value = resp.data
    subStatus.value.isOpen = subStatus.value.isOpen === 0
  }
}
</script>

<template>
  <el-form class="content" label-width="auto">
    <el-form-item label="活动规则：">
      <el-input
        :maxlength="100"
        show-word-limit
        v-model="subStatus.notice"
        style="width: 200px"
        :rows="8"
        type="textarea"
        placeholder="注意哦”<br>“是换行"
      />
    </el-form-item>
    <el-form-item label="答题数量：">
      <el-input
        v-model.number="subStatus.number"
        style="width: 200px"
        placeholder="谨慎发言"
      />
    </el-form-item>
    <el-form-item label="及格题数：">
      <el-input
        v-model.number="subStatus.pass"
        style="width: 200px"
        placeholder="谨慎发言"
      />
    </el-form-item>
    <el-form-item label="开启答题：">
      <nut-switch v-model="subStatus.isOpen" />
    </el-form-item>
    <div class="setting-button">
      <nut-button type="info" size="small" @click.prevent="updateSubStatus">
        更新状态
      </nut-button>
    </div>
  </el-form>
</template>

<style scoped>
.content {
  width: 300px;
  margin: 20px auto;
  text-align: center;
  .setting-button {
    margin: 10px auto;
  }
}
</style>
