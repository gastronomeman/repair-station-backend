import request from '@/utils/request.js'

export const addSubService = (sub) => request.post(`/sub`, sub)

export const updateSubService = (sub) => request.put(`/sub`, sub)

export const delSubService = (id) => request.delete(`/sub/${id}`)

export const addSubListService = (subList) => request.post('/sub/list', subList)

export const getSubListService = (page, pageSize, name) =>
  request.get(`/sub/list?page=${page}&pageSize=${pageSize}&name=${name}`)

export const getRandomSubListService = () => request.get(`/sub/stu`)

export const getSubCodeService = () => request.get(`/sub/v-code`)
