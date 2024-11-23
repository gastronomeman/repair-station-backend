import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useExamState = defineStore(
  'exam',
  () => {
    const agreed = ref(false)
    const setAgreed = (newValue) => {
      agreed.value = newValue
    }

    const result = ref([])
    const setResultLength = (length) => {
      result.value = new Array(length).fill(null)
    }
    const setResult = (index, value) => {
      result.value[index] = value
    }

    const student = ref(null)
    const setStudent = (newStudent) => {
      student.value = newStudent
    }

    const status = ref('')
    const setStatus = (newStatus) => {
      status.value = newStatus
    }

    const time = ref('')
    const setTime = (newTime) => {
      time.value = newTime
    }

    const clear = () => {
      agreed.value = false
      result.value = []
      student.value = null
      status.value = ''
    }
    return {
      agreed,
      setAgreed,
      result,
      setResultLength,
      setResult,
      student,
      setStudent,
      status,
      setStatus,
      time,
      setTime,
      clear
    }
  },
  { persist: true }
)
