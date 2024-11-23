<script setup>
import icon from '@/assets/logos.png'
import { School, User, Classroom, Book } from '@icon-park/vue-next'
import { ref } from 'vue'
import { addStudentService } from '@/api/student.js'
import { useExamState } from '@/stores/index.js'
import { useRouter } from 'vue-router'
import { successMsg } from '@/utils/SendMsgUtils.js'
import { dialog } from '@/utils/DialogUtils.js'

const examState = useExamState()
const router = useRouter()

const student = ref({
  name: '',
  id: '',
  college: '',
  classId: ''
})

const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    {
      pattern: /^(?:[\u4e00-\u9fa5·]{2,16})$/,
      message: '姓名格式不正确',
      trigger: 'blur'
    }
  ],
  id: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  college: [{ required: true, message: '请输入学院', trigger: 'blur' }],
  classId: [{ required: true, message: '请输入班级', trigger: 'blur' }]
}
const formRef = ref()

const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      const toast = showLoadingToast({
        message: '跳转中...',
        forbidClick: true,
        loadingType: 'spinner',
        duration: 0
      })

      const resp = await addStudentService(student.value)
      if (resp.code === 1) {
        examState.setStudent(resp.data)
        await router.push('/exam/answer')
        successMsg('录入成功！')
        dialog(
          `<p style="text-align: left">姓名：${examState.student.name}<br />学号：${examState.student.id}<br /><span style="color: red">请核对好信息，如遇信息不对请第一时间联系工作人员！</span></p>`
        )
      }
      toast.close()
    } else {
      console.log('提交失败')
      return false
    }
  })
}
</script>

<template>
  <div class="image">
    <van-image width="100%" :src="icon" />
  </div>
  <el-form
    ref="formRef"
    :model="student"
    :rules="rules"
    label-width="auto"
    class="from"
  >
    <el-form-item label="姓名：" prop="name">
      <el-input v-model="student.name" clearable>
        <template #prefix>
          <el-icon class="input-icon">
            <user />
          </el-icon>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item label="学号：" prop="id">
      <el-input v-model.number="student.id" clearable>
        <template #prefix>
          <el-icon class="input-icon">
            <school />
          </el-icon>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item label="学院：" prop="college">
      <el-input v-model="student.college" clearable>
        <template #prefix>
          <el-icon class="input-icon">
            <classroom />
          </el-icon>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item label="班级：" prop="classId">
      <el-input v-model="student.classId" clearable>
        <template #prefix>
          <el-icon class="input-icon">
            <book />
          </el-icon>
        </template>
      </el-input>
    </el-form-item>
    <div style="text-align: center">
      <nut-button shape="round" type="info" @click.prevent="submitForm">
        提交
      </nut-button>
    </div>
  </el-form>
</template>

<style scoped>
.image {
  width: 80%;
  max-width: 180px;
  text-align: center;
  margin: 1vh auto;
}
.from {
  width: 90%;
  max-width: 320px;
  margin: 20px auto;

  .btn {
    margin: 15px auto 0;
    text-align: center;
    width: 85%;

    p {
      font-size: 14px;
      color: red;
    }
  }
}
</style>
