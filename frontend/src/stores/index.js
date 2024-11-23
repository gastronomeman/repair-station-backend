import { createPinia } from 'pinia'
import persist from 'pinia-plugin-persistedstate'

const pinia = createPinia()
pinia.use(persist)

export default pinia

export * from './modules/staff.js'
export * from './modules/admin.js'
export * from './modules/order.js'
export * from './modules/tool.js'
export * from './modules/exam.js'
export * from './modules/common.js'
