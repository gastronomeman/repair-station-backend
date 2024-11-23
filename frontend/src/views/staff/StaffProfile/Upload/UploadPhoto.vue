<script setup>
import { useRoute, useRouter } from 'vue-router'
import { ref } from 'vue'
import { CloseOne } from '@icon-park/vue-next'
import { successMsg, warningMsg } from '@/utils/SendMsgUtils.js'
import { uploadsService } from '@/api/common.js'
import Compressor from 'compressorjs'

const router = useRouter()
const route = useRoute()
const onClick = () => router.push('/staff/profile/uploader')

const radio1 = ref('')
const radio2 = ref('')
const radio3 = ref('')

//文件存入FormData
let formData = new FormData()
const fileList0 = ref([])
const fileList1 = ref([])
const fileList2 = ref([])
const fileList3 = ref([])

const submitUpload = async () => {
  if (radio1.value === '' || radio2.value === '' || radio3.value === '') {
    warningMsg('单选选项不能为空')
    return
  }

  if (!fileList0.value.length > 0) {
    warningMsg('签名照片没有上传！')
    return
  }

  if (!confirm(`是否确认提交：“${route.query.name}”的图片？`)) return

  const toast = showLoadingToast({
    message: '上传中...',
    forbidClick: true,
    loadingType: 'spinner',
    duration: 0
  })

  try {
    // 使用 Promise.all 等待所有压缩任务完成
    await Promise.all([afterRead0(), afterRead1(), afterRead2(), afterRead3()])

    // 压缩任务完成后再提交表单
    fileList0.value.forEach((file) => {
      formData.append('files', file.file)
    })
    fileList1.value.forEach((file) => {
      formData.append('files', file.file)
    })
    fileList2.value.forEach((file) => {
      formData.append('files', file.file)
    })
    fileList3.value.forEach((file) => {
      formData.append('files', file.file)
    })

    const resp = await uploadsService(formData, route.query.id)
    if (resp.code === 1) {
      successMsg('提交图片成功')
      await router.push('/staff/profile/uploader')
    }
    toast.close()
  } catch (error) {
    console.error(error)
    toast.close()
    warningMsg('上传失败，请重试')
  }
}

const clearUpload = () => {
  fileList0.value = []
  fileList1.value = []
  fileList2.value = []
  fileList3.value = []
  formData.delete('files')
  successMsg('图片清除成功')
}
const onOversize = () => {
  warningMsg('照片大小不能超过20MB!')
}
const afterRead0 = async () => {
  return compressAndAppend(fileList0.value, 0).then((result) => {
    fileList0.value = result
  })
}
const afterRead1 = async () => {
  return compressAndAppend(fileList1.value, 1).then((result) => {
    fileList1.value = result
  })
}
const afterRead2 = async () => {
  return compressAndAppend(fileList2.value, 2).then((result) => {
    fileList2.value = result
  })
}
const afterRead3 = async () => {
  return compressAndAppend(fileList3.value, 3).then((result) => {
    fileList3.value = result
  })
}
const compressAndAppend = async (fileList, index) => {
  const compressedFiles = [] // 创建一个数组用于存储压缩后的文件
  let i = 0
  for (const item of fileList) {
    if (item.file.size > 1024 * 1024) {
      item.file = await new Promise((resolve) => {
        new Compressor(item.file, {
          quality: 0.5, // 压缩质量
          success(result) {
            // 创建一个 File 对象
            const file = new File([result], result.name, {
              type: result.type,
              lastModified: Date.now()
            })
            resolve(file) // 返回压缩后的文件
          },
          error(err) {
            console.log(err)
            resolve(item.file) // 如果压缩失败，返回原始文件
          }
        })
      })
    }

    // 获取文件的后缀名
    const extension =
      item.file.name.substring(item.file.name.lastIndexOf('.')) || ''
    // 生成新的文件名：index-顺序号.后缀
    const newFileName = `${index}_${i}${extension}`
    // 文件小于1MB，直接使用新的文件名并添加到文件列表
    item.file = new File([item.file], newFileName, {
      type: item.file.type,
      lastModified: Date.now()
    })
    compressedFiles.push(item) // 添加到压缩文件列表
    i++
  }

  return compressedFiles // 返回压缩后的文件数组
}
</script>

<template>
  <nut-sticky>
    <nut-navbar title="上传拆机照片" left-show @click-back="onClick">
      <template #left>
        <div>返回</div>
      </template>
    </nut-navbar>
  </nut-sticky>
  <div class="upload">
    <h4>1.确认电脑有无保修（拆机前确认）</h4>
    <p>*有保修除加装固态，其余非必要不拆机</p>
    &nbsp;&nbsp;
    <el-radio-group v-model="radio1">
      <el-radio value="0" size="large">没保修</el-radio>
      <el-radio value="1" size="large">有保修</el-radio>
    </el-radio-group>
    <h4>2.机主签名截图</h4>
    <van-uploader
      v-model="fileList0"
      @oversize="onOversize"
      :max-size="1024 * 1024 * 20"
      :max-count="1"
      preview-size="100px"
      accept="image/jpeg, image/png"
      :after-read="afterRead0"
      multiple
    >
      <template #preview-delete>
        <close-one
          theme="outline"
          size="25"
          fill="#ffffff"
          strokeLinejoin="miter"
        />
      </template>
    </van-uploader>
    <h4>3.修单前确认电脑状态</h4>
    <p>
      *1.合页朝下竖起来看b面和c面的松紧程度(拍出松的缝隙)<br />
      *2.触摸板下摁，两边(拍出凹陷程度)<br />
      *3.检查全机有无缝隙<br />
      *4.屏幕是否开机后无破损，显卡是否正常存在
    </p>
    <van-uploader
      v-model="fileList1"
      @oversize="onOversize"
      :max-size="1024 * 1024 * 20"
      :max-count="6"
      preview-size="100px"
      accept="image/jpeg, image/png"
      :after-read="afterRead1"
      multiple
    >
      <template #preview-delete>
        <close-one
          theme="outline"
          size="25"
          fill="#ffffff"
          strokeLinejoin="miter"
        />
      </template>
    </van-uploader>
    <h4>4.和机主确认“修单前确认电脑状态”的拍照确认电脑状态有无问题</h4>
    <el-radio-group v-model="radio2">
      <el-radio value="0" size="large">有问题</el-radio>
      <el-radio value="1" size="large">确认无疑</el-radio>
    </el-radio-group>
    <h4>5.维修过程中</h4>
    <p>
      *1.拆机拍下主板等<br />
      *2.安装软件的记录
    </p>
    <van-uploader
      v-model="fileList2"
      @oversize="onOversize"
      :max-size="1024 * 1024 * 20"
      :max-count="6"
      preview-size="100px"
      accept="image/jpeg, image/png"
      :after-read="afterRead2"
      multiple
    >
      <template #preview-delete>
        <close-one
          theme="outline"
          size="25"
          fill="#ffffff"
          strokeLinejoin="miter"
        />
      </template>
    </van-uploader>
    <h4>6.修完后再三和机主确定机子有无问题</h4>
    <p>*多次开机测试后再询问</p>
    <el-radio-group v-model="radio3">
      <el-radio value="0" size="large">有问题</el-radio>
      <el-radio value="1" size="large">没问题</el-radio>
    </el-radio-group>
    <h4>7.维修完成后</h4>
    <p>
      *1.电脑bc面<br />
      *2.触摸板<br />
      *3.缝隙<br />
      *4.电脑屏幕<br />
      拍一遍
    </p>
    <van-uploader
      v-model="fileList3"
      @oversize="onOversize"
      :max-size="1024 * 1024 * 20"
      :max-count="6"
      preview-size="100px"
      accept="image/jpeg, image/png"
      :after-read="afterRead3"
      multiple
    >
      <template #preview-delete>
        <close-one
          theme="outline"
          size="25"
          fill="#ffffff"
          strokeLinejoin="miter"
        />
      </template>
    </van-uploader>
    <div style="text-align: center; margin: 15px auto">
      <nut-button type="success" size="small" @click="submitUpload">
        提交核对图片
      </nut-button>
      &nbsp;
      <nut-button type="danger" size="small" @click="clearUpload">
        手动清空全部
      </nut-button>
    </div>
    <p style="color: red">
      *图片都要提交，否则后期与机主发生纠纷，全由社员自身负责。<br />
      *选项是提醒作用不会保存在数据库，但照片会上传服务器。
    </p>
  </div>
</template>

<style scoped>
.upload {
  max-width: 800px;
  width: 80%;
  margin: 20px auto;
  h4 {
    margin: 5px 0 0;
  }
  p {
    color: #7a7374;
    font-size: 12px;
    margin: 0 0 5px;
  }
}
</style>
