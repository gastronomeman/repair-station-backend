<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  addItemService,
  saveService,
  updateItemService,
  updateService
} from '@/api/webLink.js'
import { successMsg } from '@/utils/SendMsgUtils.js'
import { useToolState } from '@/stores/index.js'

const toolState = useToolState()
const router = useRouter()
const route = useRoute()

const o = ref(route.query.o)
const name = ref(route.query.name)

const back = () => {
  router.go(-1)
}

const webLink = ref(toolState.webItem)

const linkItem = ref(toolState.linkItem)

const add = async () => {
  webLink.value.id = null
  const resp = await saveService(webLink.value)
  if (resp.code === 1) {
    successMsg(resp.data)
    router.go(-1)
  }
}

const addItem = async () => {
  const resp = await addItemService(webLink.value.id, linkItem.value)
  if (resp.code === 1) {
    successMsg(resp.data)
    router.go(-1)
  }
}

const update = async () => {
  const resp = await updateService(webLink.value)
  if (resp.code === 1) {
    successMsg(resp.data)
    router.go(-1)
  }
}
const updateItem = async () => {
  const resp = await updateItemService(webLink.value.id, linkItem.value)
  if (resp.code === 1) {
    successMsg(resp.data)
    router.go(-1)
  }
}
</script>

<template>
  <nut-sticky>
    <nut-navbar title="工具箱操作" left-show @click-back="back">
      <template #left>
        <div>返回</div>
      </template>
    </nut-navbar>
  </nut-sticky>
  <div class="login-img-logo">
    <img src="@/assets/rs_logo.png" alt="Logo" />
  </div>
  <div class="base" v-if="o === 'add'">
    <el-input
      clearable
      size="large"
      v-model="webLink.title"
      style="width: 260px; margin-bottom: 15px"
      placeholder="输入要创建集合的名字"
    /><br />
    <nut-button type="primary" @click="add">添加集合名</nut-button>
  </div>
  <div class="base" v-if="o === 'addItem'">
    网站名称:
    <el-input
      clearable
      size="large"
      v-model="linkItem.name"
      style="width: 260px; margin-bottom: 15px"
      placeholder="网站名称"
    /><br />
    网站链接:
    <el-input
      clearable
      size="large"
      v-model="linkItem.url"
      style="width: 260px; margin-bottom: 15px"
      placeholder="网站链接"
    /><br />
    图标链接:
    <el-input
      clearable
      size="large"
      v-model="linkItem.photo"
      style="width: 260px; margin-bottom: 15px"
      placeholder="图标链接"
    /><br />
    <nut-button type="primary" @click="addItem">创建网页</nut-button>
  </div>
  <div class="base" v-if="o === 'update'">
    <p>{{ name }}</p>
    <el-input
      clearable
      size="large"
      v-model="webLink.title"
      style="width: 260px; margin-bottom: 15px"
      placeholder="输入要修改集合的名字"
    /><br />
    <nut-button type="primary" @click="update">修改</nut-button>
  </div>
  <div class="base" v-if="o === 'updateItem'">
    网站名称:
    <el-input
      clearable
      size="large"
      v-model="linkItem.name"
      style="width: 260px; margin-bottom: 15px"
      placeholder="网站名称"
    /><br />
    网站链接:
    <el-input
      clearable
      size="large"
      v-model="linkItem.url"
      style="width: 260px; margin-bottom: 15px"
      placeholder="网站链接"
    /><br />
    图标链接:
    <el-input
      clearable
      size="large"
      v-model="linkItem.photo"
      style="width: 260px; margin-bottom: 15px"
      placeholder="图标链接"
    /><br />
    <nut-button type="primary" @click="updateItem">修改网页</nut-button>
  </div>
</template>

<style scoped>
.login-img-logo {
  width: 130px;
  margin: 80px auto 20px;
  img {
    width: 100%;
    height: auto;
  }
}

.base {
  text-align: center;
}
</style>
