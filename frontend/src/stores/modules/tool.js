import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useToolState = defineStore(
  'toolStaff',
  () => {
    const webItem = ref({})
    const setWebItem = (newValues) => {
      webItem.value = newValues
    }

    const linkItem = ref({})
    const setLinkItem = (newValues) => {
      linkItem.value = newValues
    }

    const clear = () => {
      webItem.value = ''
      linkItem.value = ''
    }
    return {
      webItem,
      setWebItem,
      linkItem,
      setLinkItem,
      clear
    }
  },
  {
    persist: true
  }
)
