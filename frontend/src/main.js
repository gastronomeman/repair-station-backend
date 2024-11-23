import { createApp } from 'vue'
import pinia from '@/stores'
import App from './App.vue'
import router from './router'
import '@nutui/touch-emulator'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import '@/assets/main.scss'

const app = createApp(App)

app.use(pinia)
app.use(router)
app.use(ElementPlus, {
  locale: zhCn
})
app.mount('#app')
