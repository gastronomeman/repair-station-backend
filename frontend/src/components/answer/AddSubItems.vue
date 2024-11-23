<script setup>
import { ref } from 'vue'
import * as XLSX from 'xlsx'
import { DatabaseSuccess, UploadOne } from '@icon-park/vue-next'
import imagePath from '@/assets/exam/saveBatch.jpg'
import { successMsg } from '@/utils/SendMsgUtils.js'
import { addSubListService } from '@/api/sub.js'

const jsonData = ref([])

const handleChange = (fileList) => {
  if (fileList.length === 0) {
    console.log('未选择文件')
    return
  }

  const file = fileList.raw // 获取第一个文件
  if (!file) {
    console.log('文件未找到')
    return
  }

  const reader = new FileReader()
  reader.onload = (ev) => {
    const data = new Uint8Array(ev.target.result) // 将结果转换为 Uint8Array
    const workbook = XLSX.read(data, { type: 'array' }) // 使用 array 类型解析文件
    const worksheet = workbook.Sheets[workbook.SheetNames[0]] // 获取第一个工作表
    jsonData.value = XLSX.utils.sheet_to_json(worksheet) // 转换为 JSON 格式
  }

  // 读取文件为 ArrayBuffer 格式
  reader.readAsArrayBuffer(file) // 确保使用 readAsArrayBuffer
}

const imagePreview = () => {
  showImagePreview({
    images: [imagePath],
    closeable: true
  })
}

const saveBatchStaff = async () => {
  const resp = await addSubListService(jsonData.value)
  if (resp.code === 1) {
    successMsg(resp.data)
  }
}
</script>

<template>
  <el-upload
    class="upload-demo"
    drag
    :auto-upload="false"
    accept=".xlsx"
    :on-change="handleChange"
    :multiple="false"
  >
    <el-icon class="el-icon--upload">
      <upload-one theme="outline" size="24" fill="#5e616d" />
    </el-icon>
    <div class="el-upload__text"><em>拖入或点击</em>框内进行上传.xlsx文件</div>
    <template #tip>
      <div class="el-upload__tip">传输文件要求要.xlsx文件</div>
    </template>
  </el-upload>
  <div class="format-text">
    <nut-button
      @click="saveBatchStaff"
      v-if="jsonData.length > 0"
      type="primary"
      style="margin-bottom: 20px"
    >
      <template #icon>
        <database-success
          class="step-icon"
          theme="outline"
          size="24"
          fill="#fff"
        />
      </template>
      导入数据库！
    </nut-button>
    <br />
    <img
      width="100%"
      @click="imagePreview()"
      src="@/assets/exam/saveBatch.jpg"
      alt="展示模板"
    />
    <div v-if="jsonData.length > 0">
      <h3>解析结果:</h3>
      <pre>{{ jsonData }}</pre>
    </div>
  </div>
</template>

<style scoped>
.upload-demo {
  width: 85%;
  max-width: 800px;
  margin: 20px auto;
}
.format-text {
  text-align: center;
  width: 85%;
  max-width: 800px;
  margin: 0 auto;
  .step-icon {
    display: inline-flex;
    align-items: center;
    justify-content: center;
  }
  div {
    text-align: left;
  }
}
</style>
