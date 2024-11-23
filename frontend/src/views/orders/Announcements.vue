<script setup>
import { ref } from 'vue'
import { getStopTitleService } from '@/api/repairStationStatus.js'
import { Toolkit } from '@icon-park/vue-next'

const stopTitle = ref('')

const getStopTitle = async () => {
  const resp = await getStopTitleService()
  if (resp.code === 1) {
    stopTitle.value = resp.data
  }
}
getStopTitle()
</script>

<template>
  <div class="new-order-container">
    <div class="new-order-head">
      <img src="@/assets/orders/images/news_top.png" alt="图标" />
      <div class="new-order-from">
        <h2>{{ stopTitle }}&nbsp;</h2>
        <p>
          &nbsp;&nbsp;&nbsp;&nbsp;报修服务暂时关闭。具体开放通知，可关注下面公众号获取最新消息。<br />
          &nbsp;&nbsp;&nbsp;&nbsp;如若关闭时间截至仍在此页面，可以刷新重新尝试一下。<br />
          &nbsp;&nbsp;&nbsp;&nbsp;如遇紧急情况，需要维修电脑可添加维修站QQ咨询群：790445318。
        </p>
        <div class="tool">
          <toolkit
            @click="router.push('/tool')"
            class="icon-center"
            theme="two-tone"
            size="20"
            :fill="['#9b9b9b', '#4a90e2']"
            strokeLinejoin="miter"
          />
          <span @click="router.push('/tool')"> 软件工具箱</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.new-order-container {
  .new-order-from {
    h2 {
      color: #7a7374;
      text-align: center;
      margin: 0;
      padding: 0 20px;
    }
    p {
      font-size: 18px;
      padding: 0 8px 10px;
      word-wrap: break-word;
      overflow-wrap: break-word;
      color: #7a7374;
    }
  }
}
</style>
