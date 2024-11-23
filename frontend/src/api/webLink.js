import request from '@/utils/request.js'

export const linkFindAllService = () => request.get('/link')

export const deleteService = (id) => request.delete('/link/' + id)

export const deleteItemService = (id, itemId) =>
  request.delete(`/link/${id}/${itemId}`)

export const saveService = (data) => request.post('/link', data)

export const addItemService = (id, data) => request.post('/link/' + id, data)

export const updateService = (data) => request.put('/link', data)

export const updateItemService = (id, data) => request.put('/link/' + id, data)

export const getItemByNameService = (name) =>
  request.get(`/link/by-name`, {
    params: { name }
  })
