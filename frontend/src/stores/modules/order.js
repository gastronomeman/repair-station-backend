import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useOrderState = defineStore('repairOrder', () => {
  const agreed = ref(false)
  const setAgreed = (newValue) => {
    agreed.value = newValue
  }

  const clear = () => {
    agreed.value = false
  }
  return {
    agreed,
    setAgreed,
    clear
  }
})
