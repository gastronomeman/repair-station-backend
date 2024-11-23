import request from '@/utils/request.js'

export const uploadsService = (formData, id) =>
  request.post(`/common/uploads/${id}`, formData, {
    headers: {
      'content-type': 'multipart/form-data'
    }
  })

export const checkPhotoService = () => request.post(`/common/check-dir`)

export const getPhotoListService = (dirName) =>
  request.get(`/common/getPhotoList?dirName=${dirName}`)

export const deletePhotoService = (name) =>
  request.delete(`/common/del?name=${name}`)

export const backupDBService = (s) =>
  request.get('/common/backup-db?status=' + s)
