<script setup>
import { successMsg } from '@/utils/SendMsgUtils.js'
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import { useStaffState } from '@/stores/index.js'

const router = useRouter()
const staffState = useStaffState()

const onClick = () => {
  router.push('/staff/accepted-orders')
}
const order = ref({})
order.value = staffState.order

const signatureBoard = ref(false)
const imgData = ref('')
const clickToSignatureBoard = () => {
  signatureBoard.value = true
}
const confirm = (canvas, data) => {
  successMsg('请交给工作人员截图保存')
  if (data) {
    // 创建一个新的 canvas 元素来绘制旋转后的图像
    const rotationCanvas = document.createElement('canvas')
    const ctx = rotationCanvas.getContext('2d')

    // 创建一个新的 Image 对象并加载 data 图像
    const image = new Image()
    image.src = data

    image.onload = () => {
      // 设置新的 canvas 的宽度和高度
      rotationCanvas.width = +image.height // 宽度设置为图像的高度
      rotationCanvas.height = +image.width // 高度设置为图像的宽度

      // 旋转图像
      ctx.translate(rotationCanvas.width / 2, rotationCanvas.height / 2)
      ctx.rotate(-(90 * Math.PI) / 180) // 旋转 90 度
      ctx.drawImage(image, -image.width / 2, -image.height / 2)

      // 获取旋转后的图像数据
      imgData.value = rotationCanvas.toDataURL()
    }
  }
  signatureBoard.value = false
}
const clear = () => {
  imgData.value = ''
}
</script>

<template>
  <div v-if="!signatureBoard">
    <nut-navbar title="协议签订" left-show @click-back="onClick">
      <template #left>
        <div>返回</div>
      </template>
    </nut-navbar>
    <div class="agreement-text">
      <h1 style="text-align: center">拆机协议</h1>
      <p>
        <span>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**在进行维修服务前，请仔细阅读并同意以下协议内容：
        </span>
        <br />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.请确认设备是否在保修期内。若在保修期内，我们建议不拆机维修，因为可能导致失去保修资格。若执意拆机，我们不承担任何责任。<br /><br />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.拆机存在风险，很感谢你选择了我们，我们将尽力操作，但可能出现损坏。若因我们的操作导致损坏，我们承担至多60%的维修费用。。<br /><br />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.拆机维修服务虽免费，但我们将认真仔细地进行操作。<br /><br />
        <span>
          **请您在确认同意上述内容，并确认下方信息无误后，签署此协议。
        </span>
        <br />
        <span style="color: #7a7374"> 订单号：{{ order.id }} </span><br />
        <span style="color: #7a7374"> 姓名：{{ order.name }} </span><br />
        <span style="color: #7a7374"> 学号：{{ order.studentId }} </span><br />
        <span style="color: #7a7374"> 报修时间：{{ order.createTime }} </span
        ><br />
        <span style="color: #7a7374"> 报修详情：{{ order.orderDescribe }}</span
        ><br />
      </p>
    </div>
    <img
      v-if="imgData"
      :src="imgData"
      class="imgData"
      style="width: 100%"
      alt="签名"
    />
    <nut-button
      v-if="!imgData"
      size="large"
      type="primary"
      @click="clickToSignatureBoard"
    >
      同意并进行签名
    </nut-button>
    <nut-row v-else>
      <nut-col :span="1">&nbsp;</nut-col>
      <nut-col :span="11">
        <nut-button size="large" @click="clickToSignatureBoard">
          重新签名
        </nut-button>
      </nut-col>
      <nut-col :span="11">
        <nut-button type="warning" size="large" @click="clear">
          清除签名
        </nut-button>
      </nut-col>
    </nut-row>

    <div class="tip">
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**请维修人员等待机主签名后进行截图保存
    </div>
  </div>
  <div class="signature" v-else>
    <div style="text-align: right; margin-top: 3px; font-size: 18px">
      <span>：</span>
      <span>名</span>
      <span>签</span>
      <span>行</span>
      <span>进</span>
      <span>向</span>
      <span>正</span>
      <span>为</span>
      <span>向</span>
      <span>方</span>
      <span>此</span>
      <span>按</span>
    </div>
    <nut-signature @confirm="confirm" @clear="clear"></nut-signature>
    <div style="text-align: right; margin-top: 3px; font-size: 18px">
      <span>&nbsp;</span>
      <span>名</span>
      <span>签</span>
      <span>行</span>
      <span>进</span>
      <span>向</span>
      <span>正</span>
      <span>为</span>
      <span>向</span>
      <span>方</span>
      <span>此</span>
      <span>按</span>
    </div>
  </div>
</template>

<style>
.agreement-text {
  width: 95%;
  margin: 0 auto;
  h1 {
    margin: 5px;
  }
  p {
    word-wrap: break-word;
    overflow-wrap: break-word;
    margin: 5px;
    span {
      font-size: 13px;
      color: red;
    }
  }
}
.tip {
  font-size: 14px;
  color: red;
  margin-top: 10px;
  margin-bottom: 20px;
}
.signature {
  height: 100vh;
  .nut-signature {
    height: 90%;
    text-align: center;
    .nut-signature-inner {
      height: 90%;
      margin-bottom: 2%;
    }
  }
  span {
    color: red;
    display: inline-block;
    transform: rotate(90deg);
    transform-origin: left top;
  }
}
</style>
