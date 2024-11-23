import request from '@/utils/request.js'

export const ordersNewService = (data) => request.post('/orders', data)

export const getOrderListByStudentIdService = (studentId) =>
  request.get('/orders?name=' + studentId)

export const getOrderTotalService = () => request.get('/orders/total')

export const getStaffOrderTotalService = () =>
  request.get('/orders/staff-total')

export const getOrdersListService = (type, status) =>
  request.get(`/orders/type-list?type=${type}&status=${status}`)

export const getOrdersWithNameListService = (type, status) =>
  request.get(`/orders/type-list-with-name?type=${type}&status=${status}`)

export const takingOrdersService = (data) => request.put(`/orders/taking`, data)

export const takingListService = () => request.get(`/orders/taking-list`)

export const uploaderListService = () => request.get(`/orders/uploader-list`)

export const finishOrderService = (data) =>
  request.put(`/orders/finish-order`, data)

export const staffListService = (id, startTime, endTime) =>
  request.get(
    `/orders/staff-list?id=${id}&startTime=${startTime}&endTime=${endTime}`
  )

export const getAdminOrderService = () => request.get('/orders/admin-orders')

export const getOrderListService = (page, pageSize, name) =>
  request.get(
    `/orders/order-list?page=${page}&pageSize=${pageSize}&name=${name}`
  )

export const getHistoryListService = (page, pageSize, name) =>
  request.get(
    `/orders/history-list?page=${page}&pageSize=${pageSize}&name=${name}`
  )

export const changSqlService = () => request.get(`/orders/change-sql`)

export const transferOrderService = (id) =>
  request.put(`/orders/transfer-order/${id}`)

export const changStatusService = (n, id) =>
  request.put(`/orders/chang-status${n}`, id)

export const cancelService = (data) => request.put('/orders/cancel', data)

export const checkRepeatService = (data) =>
  request.post('/orders/check-repeat', data)
