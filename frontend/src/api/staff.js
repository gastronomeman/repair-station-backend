import request from '@/utils/request.js'

export const staffLoginService = (staff) => request.post('/staff/login', staff)

export const staffLeaderboardService = () => request.get('/staff/leaderboard')

export const changPasswordService = (data) =>
  request.put('/staff/change-password', data)

export const logoutService = () => request.get('/staff/logout')

export const orderCountService = (startTime, endTime) =>
  request.get(`/staff/order-counts?startTime=${startTime}&endTime=${endTime}`)

export const getStaffListService = (page, pageSize, name) =>
  request.get(
    `/staff/staff-list?page=${page}&pageSize=${pageSize}&name=${name}`
  )

export const addStaffService = (data) => request.post('/staff/addStaff', data)

export const resetStaffService = (data) => request.put('/staff/reset', data)

export const getInfoService = (id) => request.get(`/staff/info?id=${id}`)

export const completeInfoService = (data) =>
  request.put('/staff/complete-info', data)

export const removeStaffService = (id) => request.delete(`/staff/delete/${id}`)

export const getNameByIdService = (id) =>
  request.get('/staff/getNameById?id=' + id)

export const getStaffOnlineCountService = () =>
  request.get('/staff/staff-online')

export const getStaffOnlineNameService = () => request.get('/staff/online-name')

export const addStaffListService = (data) =>
  request.post('/staff/addStaff-list', data)
