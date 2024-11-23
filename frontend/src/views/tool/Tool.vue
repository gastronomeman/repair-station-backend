<script setup>
import ToolShow from '@/components/tool/ToolShow.vue'
import Footer from '@/components/Footer.vue'
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import { errorMsg, successMsg } from '@/utils/SendMsgUtils.js'
import { linkFindAllService } from '@/api/webLink.js'

const router = useRouter()

const appList = ref([])

const getAppList = async () => {
  const resp = await linkFindAllService()
  if (resp.code === 1) {
    appList.value = resp.data
  }
}
getAppList()

const back = () => {
  router.go(-1)
}

const textColor = ref('black')

const copyToClipboard = () => {
  if (navigator.clipboard) {
    navigator.clipboard
      .writeText(window.location.href)
      .then(() => {
        textColor.value = '#939393'
        successMsg('复制成功！')
      })
      .catch(() => {
        errorMsg('复制失败')
      })
  } else {
    alert('不支持剪贴板功能')
  }
}

const allShow = ref(true)
</script>

<template>
  <nut-sticky>
    <nut-navbar title="软件工具箱" left-show @click-back="back">
      <template #left>
        <div>返回</div>
      </template>
    </nut-navbar>
  </nut-sticky>
  <div style="max-width: 800px; margin: 0 auto">
    <div class="title">
      **此网页的工具是为了帮助同学们更好的找到各个软件的官网，用作学习之用，如遇不会安装可以点击<a
        style="color: #2c68ff; font-weight: bold"
        href="/"
        >报修</a
      >，让我们帮助你！<br />
      <p @click="copyToClipboard" :style="{ color: textColor }">
        点击此处，复制网址<br />（复制网址到电脑浏览更佳哦）
      </p>
    </div>
    <div style="text-align: center; margin: 5px auto">
      <nut-button type="info" @click="allShow = !allShow"
        >收起 / 展开</nut-button
      >
    </div>

    <div v-for="app in appList" :key="app.id">
      <ToolShow
        :show="allShow"
        :app-list="app.list"
        :title="app.title"
      ></ToolShow>
      <nut-divider dashed style="width: 90%; margin: 5px auto" />
    </div>
    <p style="font-size: 11px; width: 80%; margin: 10px auto; color: #939393">
      &nbsp;&nbsp;&nbsp;&nbsp; 本站所发布的全部内容源于互联网搬运，
      仅限于小范围内传播学习和文献参考， 请在下载后24小时内删除，
      如果有侵权之处请第一时间联系我们删除。敬请谅解! E-mail：1305573134@qq.com
    </p>
  </div>
  <nut-backtop el-id="elId1" :distance="100" :bottom="60"></nut-backtop>
  <Footer></Footer>
  <br />
</template>

<style scoped>
.title {
  color: red;
  text-align: center;
  width: 80%;
  margin: 20px auto 0;
  p {
    margin: 0;
    padding: 3px;
    cursor: pointer;
    color: black;
    border-radius: 4px;
    border: 1px solid #939393;
  }
}
</style>
