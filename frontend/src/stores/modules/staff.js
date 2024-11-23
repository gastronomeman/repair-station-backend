import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useStaffState = defineStore(
  'repairStaff',
  () => {
    const token = ref('')
    const setToken = (newValues) => {
      token.value = newValues
    }

    const id = ref('')
    const setId = (newValues) => {
      id.value = newValues
    }

    const studentId = ref('')
    const setStudentId = (newValues) => {
      studentId.value = newValues
    }

    const name = ref('')
    const setName = (newValues) => {
      name.value = newValues
    }

    const order = ref({})
    const setOrder = (newValues) => {
      order.value = newValues
    }

    const clear = () => {
      token.value = ''
      studentId.value = ''
      name.value = ''
      order.value = ''
      id.value = ''
    }
    return {
      token,
      setToken,
      id,
      setId,
      studentId,
      setStudentId,
      name,
      setName,
      order,
      setOrder,

      clear
    }
  },
  {
    persist: true
  }
)
