import { getServerStatusService } from '@/api/repairStationStatus.js'
import { errorMsg, successMsg } from '@/utils/SendMsgUtils.js'
import { dialog } from '@/utils/DialogUtils.js'
import { getSubStatusService } from '@/api/sub_status.js'

export const handleOrderLogic = async (to) => {
  const resp = await getServerStatusService()
  if (resp.data.serverStatus === 0 && to.path === '/orders/announcements') {
    successMsg('欢迎前来报修<br />(ฅ´ω`ฅ)')
    return '/'
  } else if (
    resp.data.serverStatus === 1 &&
    to.path !== '/orders/announcements'
  ) {
    errorMsg('非常抱歉，接单暂时停止！<br /> (,,•́ . •̀,,)')
    return '/orders/announcements'
  }
}

export const handleStaffLogic = async (to, staffState) => {
  if (
    (to.path.startsWith('/staff') && to.path !== '/staff/login') ||
    to.path.startsWith('/admin')
  ) {
    if (staffState.token === '') {
      await dialog('请先登录后再尝试吧！')
      return '/staff/login'
    }
  }

  if (!to.path.includes('/staff/login') && to.path.startsWith('/staff')) {
    if (staffState.studentId === 'admin') {
      await dialog('管理员不能使用接单页面哦')
      return '/staff/login'
    }
  }

  if (to.path.startsWith('/admin') && staffState.studentId !== 'admin') {
    await dialog('成员不能进管理员页面哦')
    return '/staff/login'
  }
}

export const handleExamLogic = async (to, examState) => {
  if (!examState.agreed && to.path !== '/exam/notice') {
    await dialog('出现了某些错误...')
    return '/exam/notice'
  }

  const resp = await getSubStatusService()
  examState.setStatus(resp.data)
  if (resp.data.isOpen === 1) {
    await dialog('现在还不是答题时间哦')
    return '/'
  }
}
