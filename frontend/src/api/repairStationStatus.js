import request from '@/utils/request.js'

export const getServerStatusService = () => request.get('/wxzStatus')

export const getOrderTitleService = () => request.get('/wxzStatus/orderTitle')

export const getStaffTitleService = () => request.get('/wxzStatus/staffTitle')

export const getStopTitleService = () => request.get('/wxzStatus/stopTitle')

export const changStatusService = () => request.get('/wxzStatus/chang-status')

export const changeOrderNoService = (str) =>
  request.put('/wxzStatus/chang-order-notice', { orderNotice: str })

export const changeStaffNoService = (str) =>
  request.put('/wxzStatus/chang-staff-notice', { staffNotice: str })

export const changeStopNoService = (str) =>
  request.put('/wxzStatus/chang-stop-notice', { stopNotice: str })

export const toolBoxService = () => request.get('/wxzStatus/tool-box')

export const setToolBoxService = (str) =>
  request.put('/wxzStatus/tool-box', { toolBox: str })
