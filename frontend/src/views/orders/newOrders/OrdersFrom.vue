<script setup>
import { ref } from 'vue'
import { checkRepeatService, ordersNewService } from '@/api/orders.js'
import { useOrderState } from '@/stores/index.js'
import { useRouter } from 'vue-router'
import { dialog } from '@/utils/DialogUtils.js'

const router = useRouter()
const orderState = useOrderState()

const show = ref(false)
const val = ref()

const order = ref({
  name: '',
  studentId: '',
  building: '点击选择宿舍号',
  dormitory: '',
  phone: '',
  orderType: '',
  orderDescribe: '',
  identity: '1'
})

const columns = ref([
  { text: '13A栋', value: '13A栋' },
  { text: '13B栋', value: '13B栋' },
  { text: '14A栋', value: '14A栋' },
  { text: '14B栋', value: '14B栋' },
  { text: '18栋', value: '18栋' },
  { text: '19栋', value: '19栋' },
  { text: '20栋', value: '20栋' },
  { text: '21栋', value: '21栋' },
  { text: '22栋', value: '22栋' },
  { text: '23栋', value: '23栋' },
  { text: '24栋', value: '24栋' },
  { text: '25栋', value: '25栋' },
  { text: '26栋', value: '26栋' },
  { text: '27栋', value: '27栋' }
])

const formRef = ref(null)
const formRules = ref({
  name: [
    { required: true, message: '请填写姓名' },
    { regex: /^(?:[\u4e00-\u9fa5·]{2,16})$/, message: '姓名格式不正确' }
  ],
  studentId: [{ required: true, message: '学号不能为空' }],
  dormitory: [{ required: true, message: '地点不能为空' }],
  phone: [
    { required: true, message: '手机号不能为空' },
    {
      regex:
        /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[189]))\d{8}$/,
      message: '手机号码格式不正确'
    }
  ],
  orderType: [{ required: true, message: '维修类型要进行选择' }],
  orderDescribe: [
    { required: true, message: '问题描述不能为空' },
    { regex: /^.{5,}$/, message: '请详细描述问题，字数不能少于5个哦' }
  ]
})

const customBlurValidate = (prop) => {
  formRef.value?.validate(prop)
}

const confirmShow = ({ selectedValue }) => {
  order.value.building = selectedValue[0]
  show.value = false
}

const loading = ref(false)
const submit = () => {
  formRef.value?.validate().then(async ({ valid }) => {
    if (valid) {
      if (
        order.value.identity === '1' &&
        order.value.building === '点击选择宿舍号'
      ) {
        alert('宿舍栋数不能为空！')
        return
      }

      const id = await checkRepeatOrder()
      if (id) {
        dialog(
          '检测到你有订单还在维修哦！<br/ > 如果有如何疑问可以加入QQ群：790445318询问哦'
        )
        await router.push(`/orders/query?studentId=${id}`)
        return
      }

      loading.value = true

      const resp = await ordersNewService(order.value)

      if (resp.code === 1) {
        console.log(order.value)
        //重置状态
        order.value = {
          name: '',
          studentId: '',
          building: '点击选择宿舍号',
          dormitory: '',
          phone: '',
          orderType: '',
          orderDescribe: '',
          identity: '1'
        }
      }
      await router.push('/orders/wait')
      loading.value = false
    } else {
      console.warn('出现错误')
    }
  })
}

const checkRepeatOrder = async () => {
  const resp = await checkRepeatService(order.value)
  if (resp.code === 1) {
    if (resp.data == null) return false
    else return resp.data
  }
}

const checkAgreed = () => {
  if (!orderState.agreed) {
    router.push('/')
  }
}
checkAgreed()
</script>

<template>
  <div class="order-from">
    <nut-form
      :rules="formRules"
      ref="formRef"
      :model-value="order"
      v-loading="loading"
    >
      <nut-form-item label="姓名" prop="name" error-message-align="left">
        <input
          v-model="order.name"
          class="nut-input-text"
          placeholder="请输入姓名"
          type="text"
          @blur="customBlurValidate('name')"
        />
      </nut-form-item>
      <nut-form-item label="身份" prop="identity" required>
        <nut-radio-group v-model="order.identity" direction="horizontal">
          <nut-radio label="1">学生</nut-radio>
          <nut-radio label="0">教师 / 职工</nut-radio>
        </nut-radio-group>
      </nut-form-item>
      <nut-form-item
        v-show="order.identity !== '1'"
        label="预约地点"
        prop="dormitory"
        error-message-align="left"
      >
        <nut-textarea
          limit-show
          :max-length="10"
          :rows="4"
          v-model="order.dormitory"
          text-align="left"
          class="nut-input-text"
          placeholder="尽量详细填写，或者填写”待定“等待同学电话联系"
          type="text"
          @blur="customBlurValidate('dormitory')"
        />
      </nut-form-item>
      <nut-form-item
        v-if="order.identity === '1'"
        label="学号"
        prop="studentId"
        error-message-align="left"
      >
        <input
          v-model="order.studentId"
          class="nut-input-text"
          placeholder="请输入学号"
          type="text"
          @blur="customBlurValidate('studentId')"
        />
      </nut-form-item>
      <nut-form-item
        label="宿舍栋数"
        required
        prop="building"
        v-show="order.identity === '1'"
      >
        <div @click="show = true" style="cursor: pointer; color: black">
          {{ order.building }}&nbsp;
        </div>
      </nut-form-item>
      <nut-popup v-model:visible="show" position="bottom">
        <nut-picker
          v-model="val"
          :title="`你的栋数是${String(val)}`"
          :columns="columns"
          @confirm="confirmShow"
          @cancel="show = false"
        />
      </nut-popup>
      <nut-form-item
        v-show="order.identity === '1'"
        label="宿舍号"
        prop="dormitory"
        error-message-align="left"
      >
        <input
          v-model="order.dormitory"
          class="nut-input-text"
          placeholder="请输入宿舍号"
          type="text"
          @blur="customBlurValidate('dormitory')"
        />
      </nut-form-item>
      <nut-form-item label="手机号" prop="phone" error-message-align="left">
        <input
          v-model="order.phone"
          class="nut-input-text"
          placeholder="请输入手机号"
          type="text"
          @blur="customBlurValidate('phone')"
        />
      </nut-form-item>
      <nut-form-item
        label="报修类型"
        prop="orderType"
        error-message-align="left"
      >
        <nut-radio-group
          v-model="order.orderType"
          @click.prevent="customBlurValidate('orderType')"
        >
          <nut-radio label="1">
            软件类：<br />
            <span style="color: #465ff0">
              软件安装、软件错误、电脑内存清理、系统问题、重装系统等
            </span>
          </nut-radio>
          <nut-radio label="2">
            硬件类 <br />
            <span style="color: #465ff0">
              电脑清灰、台式电脑组装、换硬盘/内存条、开机没反应、笔记本进水等
            </span>
          </nut-radio>
          <nut-radio label="3">
            网络类<br />
            <span style="color: #465ff0">
              认证网页不显示、找不到网卡，网络卡顿等
            </span>
          </nut-radio>
          <nut-radio label="4">
            手机类<br />
            <span style="color: #465ff0"> 手机贴膜，清理内存，更换屏幕 </span>
          </nut-radio>
        </nut-radio-group>
      </nut-form-item>
      <nut-form-item
        label="问题描述"
        prop="orderDescribe"
        error-message-align="left"
      >
        <nut-textarea
          limit-show
          :max-length="100"
          :rows="5"
          v-model="order.orderDescribe"
          text-align="left"
          placeholder="详细描述能更好了解问题哦"
          type="text"
          @blur="customBlurValidate('orderDescribe')"
        />
      </nut-form-item>
    </nut-form>

    <nut-button
      shape="round"
      size="large"
      type="primary"
      @click.prevent="submit"
      >报修
    </nut-button>
  </div>
</template>

<style scoped>
.order-from {
  padding: 10px;
  border-radius: 10px;
  .nut-input-text {
    width: 100%;
    outline: 0 none;
    border: 0;
    color: black;
  }
  .nut-form-item__body__tips {
    text-align: left !important;
  }
  .nut-button {
    margin: 10px 0 0;
  }
}
</style>
