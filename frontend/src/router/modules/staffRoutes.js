import StaffLayout from '@/views/layout/StaffLayout.vue'
import StaffOrderBrief from '@/views/staff/StaffOrderAcceptance/StaffOrderBrief.vue'
import StaffOrderList from '@/views/staff/StaffOrderAcceptance/StaffOrderList.vue'
import StaffAcceptedOrders from '@/views/staff/StaffAcceptedOrders.vue'
import StaffProfileNavigation from '@/views/staff/StaffProfile/StaffProfileNavigation.vue'
import OrderHistory from '@/views/common/OrderListShow.vue'
import Leaderboard from '@/views/staff/StaffProfile/Leaderboard.vue'
import ChangePassword from '@/views/common/ChangePassword.vue'
import Help from '@/views/common/Help.vue'
import OrderDetails from '@/views/common/OrderDetails.vue'
import Signature from '@/views/staff/SignaturePgae.vue'
import UploadNavigation from '@/views/staff/StaffProfile/Upload/UploadNavigation.vue'
import UploadPhoto from '@/views/staff/StaffProfile/Upload/UploadPhoto.vue'
import ShowPhoto from '@/views/common/ShowPhoto.vue'
import StaffInfo from '@/views/staff/StaffProfile/StaffInfo.vue'
import Code from '@/views/staff/StaffProfile/VerificationCode.vue'

export default [
  {
    path: '/staff',
    component: StaffLayout,
    redirect: '/staff/order-acceptance',
    children: [
      {
        path: 'login',
        meta: { title: '社员登录' },
        component: () => import('@/views/common/Login.vue')
      },
      {
        path: 'order-acceptance',
        meta: { title: '首页', requiresAuth: true },
        component: StaffOrderBrief
      },
      {
        path: 'order-acceptance/list',
        meta: { title: '待接单列表', requiresAuth: true },
        component: StaffOrderList
      },
      {
        path: 'accepted-orders',
        meta: { title: '报修订单', requiresAuth: true },
        component: StaffAcceptedOrders
      },

      {
        path: 'profile',
        component: StaffProfileNavigation,
        meta: { title: '我的', requiresAuth: true },
        children: [
          {
            path: 'order-history',
            component: OrderHistory
          },
          {
            path: 'leaderboard',
            component: Leaderboard
          },
          {
            path: 'change-password',
            component: ChangePassword
          },
          {
            path: 'help',
            component: Help
          },
          {
            path: 'orders-details',
            component: OrderDetails
          },
          {
            path: 'uploader',
            component: UploadNavigation
          },
          {
            path: 'uploader-photo',
            component: UploadPhoto
          },
          {
            path: 'staff-info',
            component: StaffInfo
          },
          {
            path: 'show-photo',
            component: ShowPhoto
          },
          {
            path: 'v-code',
            component: Code
          }
        ]
      }
    ]
  },
  {
    path: '/signature',
    component: Signature
  }
]
