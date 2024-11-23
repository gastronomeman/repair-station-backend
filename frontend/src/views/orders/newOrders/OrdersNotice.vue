<script setup>
import { useRouter } from 'vue-router'
import { useOrderState } from '@/stores/index.js'
import { ref } from 'vue'

const orderState = useOrderState()

const router = useRouter()

const handleAgree = async () => {
  if (orderState.agreed) {
    const toast = showLoadingToast({
      message: '跳转中...',
      forbidClick: true,
      loadingType: 'spinner',
      duration: 0
    })
    await router.push('/orders/from')
    toast.close()
  } else {
    alert('请先认真阅读，服务须知')
  }
}

const show = ref(false)
</script>

<template>
  <van-overlay
    :show="show"
    @click="show = false"
    z-index="999"
    :lock-scroll="false"
  >
    <div class="wrapper">
      <div class="orders-notice" @click.stop>
        <el-scrollbar
          ref="scrollbarRef"
          max-height="50vh"
          :always="true"
          class="text"
        >
          <div ref="innerRef">
            <h2 style="margin: 10px 0 5px">服务须知</h2>
            <p style="font-size: 12px; color: #7a7374; margin: 2px">
              *须知共有七条，若显示不全，请下滑继续阅读
            </p>
            <p class="notice">
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;一、本服务仅提供广东农工商职业技术学院师生。<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;二、在使用本服务之前，表示你已同意承担在处理过程中可能存在的一定风险。<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;三、我们“ITeam基地-维修站”的所有成员，本着一颗服务同学们的心，在实践中提升自己的专业技能，
              <span style="color: red">公益服务从不收取一分钱</span>。<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;四、正常情况下，我们会根据报修的时间顺序进行维修，请耐心等候社团成员与您进行短信联系。（为避免纠纷，我们不会以任何方式添加您的微信）<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;五、我们既不销售任何电子产品，也不供提任何购买意见。<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;六、如遇紧急情况，需要维修电脑可添加维修站QQ咨询群：790445318进行询问。<br />
              <span style="color: red">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;七、如有任何纠纷或需进行投诉，请扫描下方二维码与本公众号取得联系！
              </span>
              <br />
            </p>
          </div>
        </el-scrollbar>

        <div class="read-service">
          <label>
            <input type="checkbox" v-model="orderState.agreed" />
            已仔细阅读，并了解其风险
          </label>
          <p style="margin: 5px; font-size: 12px; color: #7a7374">
            *同意须知后方可以进行报修
          </p>
        </div>
        <nut-button size="small" shape="round" type="info" @click="handleAgree"
          >同意须知
        </nut-button>
      </div>
    </div>
  </van-overlay>
  <div class="start">
    <nut-button
      v-show="!show"
      size="large"
      shape="round"
      type="info"
      @click="show = true"
      >开始报修
    </nut-button>
  </div>
</template>

<style scoped>
.start {
  padding: 20px 30px;
  text-align: center;
}
.wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;

  .orders-notice {
    text-align: center;
    width: 85%;
    margin: 0 auto;
    border-radius: 10px;
    padding: 20px;
    background-color: #f0f0f0;

    .text {
      border-radius: 10px;
      background-color: white;
      border: 1px solid #7a7374;

      .notice {
        padding: 0 12px 15px;
        text-align: left;
        margin: 0 auto;
      }
    }

    .read-service {
      margin-top: 5px;
      .read-service label {
        cursor: pointer;
      }
    }

    .nut-button {
      margin: 5px 0 0 0;
      width: 80%;
      height: 45px;
    }
  }
}
</style>
