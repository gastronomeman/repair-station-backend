export default [
  {
    path: '/',
    component: () => import('@/views/layout/OrdersLayout.vue'),
    redirect: '/orders',
    children: [
      {
        path: 'orders',
        meta: { title: '电脑报修' },
        component: () => import('@/views/orders/newOrders/NewOrderLayout.vue'),
        redirect: '/orders/notice',
        children: [
          {
            path: 'notice',
            component: () => import('@/views/orders/newOrders/OrdersNotice.vue')
          },
          {
            path: 'from',
            component: () => import('@/views/orders/newOrders/OrdersFrom.vue')
          },
          {
            path: 'wait',
            component: () => import('@/views/orders/newOrders/OrdersWait.vue')
          }
        ]
      },
      {
        path: 'orders/announcements',
        meta: { title: '维修公告' },
        component: () => import('@/views/orders/Announcements.vue')
      },
      {
        path: 'orders/query',
        meta: { title: '报修查询' },
        component: () => import('@/views/orders/OrderQuery.vue')
      }
    ]
  }
]
