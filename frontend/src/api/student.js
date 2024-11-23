import request from '@/utils/request.js'

export const addStudentService = (data) => request.post('/stu', data)

export const checkCodeService = (data) => request.post('/stu/check-code', data)
