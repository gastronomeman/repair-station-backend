import request from '@/utils/request.js'

export const getSubStatusService = () => request.get(`/sub-status`)

export const updateSubStatusService = (data) => request.put(`/sub-status`, data)
