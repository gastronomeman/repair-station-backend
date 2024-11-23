import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAdminState = defineStore(
  'repairAdmin',
  () => {
    const startTime = ref('')
    const setStartTime = (newValue) => {
      startTime.value = newValue
    }

    const endTime = ref('')
    const setEndTime = (newValue) => {
      endTime.value = newValue
    }

    const subTabs = ref('1')
    const setSubTabs = (newValue) => {
      subTabs.value = newValue
    }

    const sub = ref({})
    const setSub = (newValues) => {
      sub.value = newValues
    }

    const clear = () => {
      startTime.value = ''
      endTime.value = ''
      subTabs.value = ''
      sub.value = ''
    }
    return {
      startTime,
      setStartTime,
      endTime,
      setEndTime,
      subTabs,
      setSubTabs,
      sub,
      setSub,
      clear
    }
  },
  {
    persist: true
  }
)
