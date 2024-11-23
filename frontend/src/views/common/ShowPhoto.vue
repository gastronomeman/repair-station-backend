<script setup>
import { useRouter, useRoute } from 'vue-router'
import { getPhotoListService } from '@/api/common.js'
import { ref } from 'vue'
import { baseURL } from '@/utils/request.js'

const router = useRouter()
const dir = useRoute().query.dir

const dirList = ref([])
const list0 = ref([])
const list1 = ref([])
const list2 = ref([])
const list3 = ref([])

const loading = ref(true)

const onClick = () => {
  router.go(-1)
}
const getPhotoList = async () => {
  loading.value = true
  const resp = await getPhotoListService(dir)
  if (resp.code === 1) {
    dirList.value = resp.data
  }
  dirList.value.forEach((item) => {
    const starIndex = item.indexOf('*')
    if (starIndex !== -1 && starIndex + 1 < item.length) {
      const firstNumber = item.charAt(starIndex + 1)

      // 根据某种条件，将数字放入不同的列表
      if (firstNumber === '0') {
        list0.value.push(item)
      } else if (firstNumber === '1') {
        list1.value.push(item)
      } else if (firstNumber === '2') {
        list2.value.push(item)
      } else if (firstNumber === '3') {
        list3.value.push(item)
      }
    }
  })
  loading.value = false
}
const url = baseURL + '/common/download-photo?name='
getPhotoList()

//点击放大展示图片
const imagePreview = (x) => {
  showImagePreview([x])
}
</script>

<template>
  <nut-sticky>
    <nut-navbar title="查看照片" left-show @click-back="onClick">
      <template #left>
        <div>返回</div>
      </template>
    </nut-navbar>
  </nut-sticky>
  <el-empty
    style="background-color: #f0f0f0"
    v-if="dirList.length === 0"
    description="列表空空如也。。。"
    v-loading="loading"
  />

  <div class="photo-box" v-if="dirList.length > 0">
    <h3>1.机主签名截图</h3>
    <div v-for="dir in list0" :key="dir">
      <van-image
        @click="imagePreview(url + dir)"
        width="150"
        height="150"
        radius="4"
        :src="url + dir"
      />
    </div>
    <nut-divider style="margin: 5px" dashed />
    <h3>2.修单前确认电脑状态</h3>
    <div v-for="dir in list1" :key="dir">
      <van-image
        @click="imagePreview(url + dir)"
        width="150"
        height="150"
        radius="4"
        :src="url + dir"
      />
    </div>
    <nut-divider style="margin: 5px" dashed />
    <h3>3.维修过程</h3>
    <div v-for="dir in list2" :key="dir">
      <van-image
        @click="imagePreview(url + dir)"
        width="150"
        height="150"
        radius="4"
        :src="url + dir"
      />
    </div>
    <nut-divider style="margin: 5px" dashed />
    <h3>4.修完拍照</h3>
    <div v-for="dir in list3" :key="dir">
      <van-image
        @click="imagePreview(url + dir)"
        width="150"
        height="150"
        radius="4"
        :src="url + dir"
      />
    </div>
    <nut-divider style="margin: 5px" dashed />
  </div>
</template>

<style scoped>
.photo-box {
  max-width: 800px;
  width: 80%;
  margin: 20px auto;
  h3 {
    margin: 8px 0;
  }
}
</style>
