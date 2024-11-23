export const warningMsg = (text) => {
  ElMessage({
    message: text,
    type: 'warning',
    center: true
  })
}

export const successMsg = (text) => {
  ElMessage({
    dangerouslyUseHTMLString: true,
    message: `<strong>${text}</strong>`,
    duration: 5000,
    type: 'success',
    center: true
  })
}

export const errorMsg = (text) => {
  ElMessage({
    dangerouslyUseHTMLString: true,
    message: `<strong>${text}</strong>`,
    type: 'error',
    center: true
  })
}
