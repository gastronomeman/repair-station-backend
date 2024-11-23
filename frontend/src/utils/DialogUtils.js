export const dialog = (str) => {
  showDialog({
    allowHtml: true,
    title: ' `(*^▽^*)′ ',
    message: str
  })
}

export const htmlDialog = (str) => {
  showDialog({
    allowHtml: true,
    theme: 'round-button',
    title: ' o(>﹏<)o ',
    message: str
  })
}

export const confirmDialog = (title, str) => {
  return new Promise((resolve) => {
    showConfirmDialog({
      allowHtml: true,
      title: title,
      message: str
    })
      .then(() => {
        resolve(true)
      })
      .catch(() => {
        resolve(false)
      })
  })
}
