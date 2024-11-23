import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useCommonState = defineStore('common', () => {
  const tabs = ref(1)
  const setTabs = (newValues) => {
    tabs.value = newValues
  }

  const page = ref({
    currentPage: 1,
    pageSize: 5,
    total: 0
  })
  const setPage = (newValues) => {
    page.value = newValues
  }

  const pageH = ref({
    currentPage: 1,
    pageSize: 5,
    total: 0
  })
  const setPageH = (newValues) => {
    pageH.value = newValues
  }

  const clear = () => {
    tabs.value = 1
    page.value = { currentPage: 1, pageSize: 5, total: 0 }
    pageH.value = { currentPage: 1, pageSize: 5, total: 0 }
  }
  return {
    tabs,
    setTabs,
    page,
    setPage,
    pageH,
    setPageH,
    clear
  }
})
