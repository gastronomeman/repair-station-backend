<script setup>
import { ref } from 'vue'
import { checkPhotoService, deletePhotoService } from '@/api/common.js'
import { errorMsg, successMsg } from '@/utils/SendMsgUtils.js'
import { useRoute, useRouter } from 'vue-router'

const router = useRouter()
const route = useRoute()
const loading = ref(true)

const dirList = ref('')

const getPhotoDir = async () => {
  loading.value = true
  const resp = await checkPhotoService()
  console.log(resp)
  if (resp.code === 1) {
    dirList.value = resp.data
  }
  loading.value = false
}

const showPhoto = async (str) => {
  if (route.fullPath.includes('admin')) {
    await router.push('/admin/profile/show-photo?dir=' + str.name)
  } else {
    await router.push('/staff/profile/show-photo?dir=' + str.name)
  }
}
getPhotoDir()

const deletePhoto = async (name) => {
  const userInput = window.prompt(`如确认删除${name}，请输入“确认删除”`)
  if (userInput === null) {
    return
  } else if (userInput !== '确认删除') {
    errorMsg('输入文字不正确')
    return
  }

  const resp = await deletePhotoService(name)
  if (resp.code === 1) {
    successMsg(resp.data)
    await getPhotoDir()
  }
}
</script>

<template>
  <nut-divider v-if="route.fullPath.includes('staff')">
    拆机照片文件夹
  </nut-divider>
  <el-empty
    style="background-color: #f0f0f0"
    v-if="dirList.length === 0"
    description="列表空空如也。。。"
    v-loading="loading"
  />
  <div class="table" v-for="(item, index) in dirList" :key="index">
    <nut-row>
      <nut-col :span="24" @click="showPhoto(item)">
        <span style="cursor: pointer">{{ item.name }}</span>
      </nut-col>
    </nut-row>
    <nut-row style="margin: 10px 0">
      <nut-col :span="18" @click="showPhoto(item)">
        <div style="color: #7a7374; cursor: pointer; margin: 5px 0">
          {{ item.date }}
        </div>
      </nut-col>
      <nut-col :span="6" v-if="route.fullPath.includes('admin')">
        <div style="text-align: right">
          <nut-button type="warning" size="mini" @click="deletePhoto(item.name)"
            >删除
          </nut-button>
        </div>
      </nut-col>
    </nut-row>
    <nut-divider style="margin: 5px" dashed />
  </div>
</template>

<style scoped>
.table {
  width: 80%;
  margin: 0 auto;
}
</style>
