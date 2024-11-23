export const isNotBlank = (str) => {
  const regex = /\S+/
  return !regex.test(str)
}

export const checkPassword = (str) => {
  const regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,18}$/
  return !regex.test(str)
}

//手机号没用就返回true
export const checkPhoneNone = (str) => {
  const regex =
    /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[189]))\d{8}$/

  return !regex.test(str)
}
