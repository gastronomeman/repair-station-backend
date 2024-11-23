<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
const router = useRouter()
const route = useRoute()

const onClick = () => {
  if (route.fullPath.includes('admin')) {
    router.push('/admin/profile')
  } else {
    router.push('/staff/profile')
  }
}

const activeName = ref(null)
const scrollToTop = (index) => {
  if (activeName.value === '') return
  window.scrollTo({
    top: 75 + 44 * index,
    behavior: 'smooth'
  })
}
</script>

<template>
  <nut-sticky>
    <nut-navbar title="帮助" left-show @click-back="onClick">
      <template #left>
        <div>返回</div>
      </template>
    </nut-navbar>
  </nut-sticky>
  <div class="helpPage">
    <h2 align="center">维修站报修系统操作说明</h2>
    <van-collapse v-model="activeName" accordion>
      <van-collapse-item title="首页介绍" name="1" @click="scrollToTop(0)">
        <div class="text">
          <p>首页，查看当前故障报修数量、个人信息、公告以及更多。</p>
          <img src="@/assets/help/help1.jpg" width="250px" alt="" /><br />
          <nut-divider dashed />
        </div>
      </van-collapse-item>
      <van-collapse-item title="如何接单" name="2" @click="scrollToTop(1)">
        <div class="text">
          <p>点击按钮进来，点击“抢单”为接手处理此故障订单。</p>
          <img src="@/assets/help/help2.1.png" width="250px" alt="" /><br />
          <img src="@/assets/help/help2.jpg" width="250px" alt="" /><br />
          <nut-divider dashed />
        </div>
      </van-collapse-item>
      <van-collapse-item
        title="接单后的各项操作"
        name="3"
        @click="scrollToTop(2)"
      >
        <div class="text">
          <p>点击上方“维修中”可以查看已接单</p>
          <img src="@/assets/help/help3.jpg" width="250px" alt="" />
          <img src="@/assets/help/help4.jpg" width="250px" alt="" /><br />
          <nut-divider dashed />
          <p>这里是自己接的单。维修完成后点击黄字”维修中“结束维修单。</p>
          <img src="@/assets/help/help5.jpg" width="250px" alt="" /><br />
          <nut-divider dashed />
        </div>
      </van-collapse-item>
      <van-collapse-item
        title="拆机维修流程（特殊）"
        name="4"
        @click="scrollToTop(3)"
      >
        <div class="text">
          <p>
            如果有“拆机”单在修单前，需要点击“签订协议”，同时需要给笔记本细节进行拍照。机主签完名之后记得自己截图保存<br />
            <span style="color: red">
              如若自己没有保存照片及截图，则后期纠纷社员自己承担
            </span>
          </p>
          <img src="@/assets/help/help6.jpg" width="250px" alt="" />
          <img src="@/assets/help/help7.jpg" width="250px" alt="" />
          <br />
          <nut-divider dashed />
          <p>点击此处对拆机过程的图片进行上传<br /></p>
          <img src="@/assets/help/help8.jpg" width="250px" alt="" /><br />
          <nut-divider dashed />
          <p>在此处选择要上传的订单<br /></p>
          <img src="@/assets/help/help9.jpg" width="250px" alt="" /><br />
          <nut-divider dashed />
          <p>按照所描述的规则进行上传填写<br /></p>
          <img src="@/assets/help/help10.jpg" width="250px" alt="" /><br />
          <nut-divider dashed />
        </div>
      </van-collapse-item>
      <van-collapse-item title="个人中心" name="5" @click="scrollToTop(4)">
        <div class="text">
          <p>个人中心，可查看历史处理记录，修改登陆密码等等。</p>
          <img src="@/assets/help/help11.jpg" width="250px" alt="" /><br />
          <nut-divider dashed />
        </div>
      </van-collapse-item>
    </van-collapse>
    <p style="width: 90%; margin: 20px auto; text-align: center; color: red">
      此帮助页面由23届隽隽师兄编写
    </p>
    <nut-divider class="foot-line" />
    <p style="width: 90%; margin: 0 auto; text-align: center">
      如果觉得此系统不完善，或者是想继续改进，需要源码，请联系我。微信：L18218644665。
    </p>
    <p style="text-align: right; width: 90%; margin: 0 auto">—— 22届师兄</p>
  </div>
</template>

<style scoped>
p {
  color: #666366;
  display: block;
  margin-block-start: 1em;
  margin-block-end: 1em;
  margin-inline-start: 0;
  margin-inline-end: 0;
}
.helpPage {
  margin: 0 auto;
  .text {
    text-align: center;
    width: 95%;
    margin: 0 auto;
    font-size: 16px;
    img {
      border: 1px solid #7a7374;
      border-radius: 10px;
    }
  }
}
.foot-line {
  margin: 20px 0 10px;
}
</style>
