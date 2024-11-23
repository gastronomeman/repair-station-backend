import { createRouter, createWebHistory } from 'vue-router'
import staffRoutes from './modules/staffRoutes.js'
import adminRoutes from './modules/adminRoutes.js'
import ordersRoutes from './modules/ordersRoutes.js'
import examRoutes from '@/router/modules/examRoutes.js'
import { useExamState, useStaffState } from '@/stores/index.js'
import {
  handleExamLogic,
  handleOrderLogic,
  handleStaffLogic
} from '@/utils/RouteGuardUtils.js'

const routes = [
  {
    path: '/:pathMatch(.*)*', // 捕获所有未匹配路径
    name: 'NotFound',
    component: () => import('@/views/error/NotFound.vue')
  },
  {
    path: '/tool',
    component: () => import('@/views/tool/Tool.vue')
  },
  ...ordersRoutes, // 导入 Orders 模块路由配置
  ...staffRoutes, // 导入 Staff 模块路由配置
  ...adminRoutes, // 导入 Admin 模块路由配置
  ...examRoutes //导入Exam 模块路由配置
]
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

router.beforeEach(async (to) => {
  // 在导航之前设置页面标题
  document.title = to.meta.title || 'ITeam维修站'

  // 处理订单逻辑
  if (to.path.startsWith('/orders')) {
    const redirectPath = await handleOrderLogic(to)
    if (redirectPath) return redirectPath
  }

  const staffState = useStaffState()
  // 处理员工逻辑
  if (to.path.startsWith('/staff') || to.path.startsWith('/admin')) {
    const redirectPath = await handleStaffLogic(to, staffState)
    if (redirectPath) return redirectPath
  }

  const examState = useExamState()
  // 处理考试逻辑
  if (to.path.startsWith('/exam')) {
    const redirectPath = await handleExamLogic(to, examState)
    if (redirectPath) return redirectPath
  }
})
export default router
