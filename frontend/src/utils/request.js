import axios from 'axios'
import { useStaffState } from '@/stores'
import router from '@/router'
import { errorMsg } from '@/utils/SendMsgUtils.js'
import { dialog, htmlDialog } from '@/utils/DialogUtils.js'

// 这里配置 baseUrl
const baseURL = window.config.baseUrl

const instance = axios.create({
  // 1. 基础地址，超时时间
  baseURL,
  timeout: 50000,
  withCredentials: true
})

// 请求拦截器
instance.interceptors.request.use(
  (config) => {
    // 2. 携带 token
    const staffState = useStaffState()
    if (staffState.token) {
      config.headers.Authorization = staffState.token
    }
    return config
  },
  (err) => Promise.reject(err)
)

// 响应拦截器
instance.interceptors.response.use(
  async (res) => {
    const { code, msg } = res.data

    if (code === 1) return res.data // 返回成功的数据

    // 登录失效处理
    if (msg === 'not_login') {
      await handleLoginRedirect('登录验证失效，请重新登录')
      return Promise.reject(msg)
    }

    // 账号在其他设备登录处理
    if (msg.startsWith('检测到账号已在别的设备登录')) {
      await handleLoginRedirect(msg, true)
      return Promise.reject(msg)
    }

    // 其他业务错误处理
    if (code === 0) errorMsg(msg)

    return res.data
  },
  (err) => {
    console.log(err)
    errorMsg('网站发生异常，请稍后尝试！<br/>╥﹏╥')
    return Promise.reject(err)
  }
)

// 封装统一的处理逻辑
// 登录处理逻辑
const handleLoginRedirect = async (message, useHtml = false) => {
  const staffState = useStaffState()
  const hasShownAlert = sessionStorage.getItem('hasShownAlert') === 'true'

  if (!hasShownAlert) {
    if (useHtml) {
      await htmlDialog(`<strong>${message}</strong>`)
    } else {
      await dialog(message)
    }

    sessionStorage.setItem('hasShownAlert', 'true')
  }

  staffState.clear()
  router.push('/staff/login').then(() => {
    sessionStorage.removeItem('hasShownAlert')
  })

  throw new Error(message) // 抛出错误供调用方捕获
}

export default instance
export { baseURL }
