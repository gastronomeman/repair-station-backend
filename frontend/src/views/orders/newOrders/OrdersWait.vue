<script setup>
import { useOrderState } from '@/stores/index.js'
import { useRouter } from 'vue-router'
import { successMsg } from '@/utils/SendMsgUtils.js'
import { dialog } from '@/utils/DialogUtils.js'

const router = useRouter()
const orderState = useOrderState()
const checkAgreed = () => {
  if (!orderState.agreed) {
    router.push('/')
  }
  successMsg('报修成功！！！')
  window.scrollTo(0, 0)
  dialog(
    '报修成功！请留意手机短信哦！<br />' +
      '<span style="color: red; font-weight: bold">我们会以短信的方式联系你！</span>'
  )
}
checkAgreed()
</script>

<template>
  <div class="order-success">
    <h2>
      报修成功！ ᖰ⌯'▾'⌯ᖳ <br />请等待工作人员
      <span style="color: red"> 短信联系 </span>
    </h2>
    <p>
      &nbsp;&nbsp;&nbsp;&nbsp;我们是公益服务，不收群众一针一线，
      如遇讨要钱财，纠纷或需要投诉 请在下方扫描二维码，在公众号进行留言！<br />
      &nbsp;&nbsp;&nbsp;&nbsp;想获取报修状态可以在上方搜索框输入学号或姓名查找<br />
      &nbsp;&nbsp;&nbsp;&nbsp;如果想要在工具箱添加你喜欢的工具网站，欢迎公众号直接留言哦<br />
      &nbsp;&nbsp;&nbsp;&nbsp;如遇紧急情况，需要维修电脑可添加维修站QQ咨询群：790445318进行咨询。
    </p>
    <p style="text-align: center; margin: 20px 0 10px; font-size: 15px">
      ╭☞( ￣ ▽￣)╭☞&nbsp;如果有空的话，可以看看我们的软件工具箱哦<br />↓↓↓↓↓
    </p>
  </div>
</template>

<style scoped></style>
