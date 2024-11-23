<script setup>
import { useRouter } from 'vue-router'
import { ref, computed } from 'vue'
import {
  addStaffService,
  getStaffListService,
  removeStaffService,
  resetStaffService
} from '@/api/staff.js'
import { successMsg } from '@/utils/SendMsgUtils.js'
import { baseURL } from '@/utils/request.js'
import { confirmDialog } from '@/utils/DialogUtils.js'

const router = useRouter()

const onClick = () => {
  router.push('/admin/profile')
}

const staff = ref({
  studentId: '',
  name: ''
})

const page = ref({
  currentPage: 1,
  pageSize: 15,
  total: 0
})
const search = ref('')
const staffList = ref([])
const getStaffList = async () => {
  const resp = await getStaffListService(
    page.value.currentPage,
    page.value.pageSize,
    search.value
  )
  if (resp.code === 1) {
    staffList.value = resp.data.records
    page.value.total = resp.data.total
  }
}
getStaffList()

const addStaff = async () => {
  const resp = await addStaffService(staff.value)
  if (resp.code === 1) {
    successMsg(resp.data)
    staff.value.studentId = ''
    staff.value.name = ''
    await getStaffList()
  }
}

const resetStaff = async (staffI) => {
  if (!(await confirmDialog(' (⊙_⊙)? ', '是否要重置此账户？'))) return
  const resp = await resetStaffService(staffI)
  if (resp.code === 1) {
    successMsg(resp.data)
    await getStaffList()
  }
}

const removeStaff = async (id) => {
  if (!(await confirmDialog(' (⊙_⊙)? ', '是否要删除此账户？'))) return

  const resp = await removeStaffService(id)
  if (resp.code === 1) {
    successMsg(resp.data)
    await getStaffList()
  }
}

const change = async () => {
  await getStaffList()
}

const searchStaff = async () => {
  page.value.currentPage = 1
  await getStaffList()
}

const resetList = async () => {
  search.value = ''
  await getStaffList()
}

const toTop = async () => {
  page.value.currentPage = 1
  search.value = ''
  await getStaffList()
}

const toEnd = async () => {
  page.value.currentPage = Math.ceil(page.value.total / page.value.pageSize)
  search.value = ''
  await getStaffList()
}

const totalPage = computed(() => {
  return Math.ceil(page.value.total / page.value.pageSize)
})

const getCsv = async () => {
  window.location.href = baseURL + '/staff/csv'
}
</script>

<template>
  <nut-sticky>
    <nut-navbar title="添加新社员" left-show @click-back="onClick">
      <template #left>
        <div>返回</div>
      </template>
    </nut-navbar>
  </nut-sticky>
  <div class="staff-from">
    <el-input
      v-model="staff.studentId"
      style="width: 110px"
      placeholder="学号"
    />
    <el-input v-model="staff.name" style="width: 110px" placeholder="姓名" />
    <nut-button type="info" size="small" @click="addStaff">添加</nut-button>
  </div>
  <div style="text-align: center">
    <nut-button
      type="info"
      size="small"
      @click="router.push('/admin/profile/save-batch')"
      >批量添加</nut-button
    >&nbsp;
    <nut-button type="info" size="small" @click="getCsv">批量导出</nut-button>
  </div>

  <div class="search">
    <el-input
      v-model="search"
      clearable
      style="width: 150px"
      placeholder="输入学号名字查询"
    />&nbsp;
    <nut-button type="info" size="small" @click="searchStaff">查询</nut-button>
    &nbsp;
    <nut-button type="info" size="small" @click="resetList">重置</nut-button>
    <nut-divider />
  </div>
  <div class="table" v-for="(item, index) in staffList" :key="index">
    <nut-row
      :style="{
        color:
          item.major !== null && item.politicalStatus !== null
            ? '#000'
            : '#bbb5ac'
      }"
    >
      <nut-col :span="8">
        <div class="content">{{ item.studentId }}</div>
      </nut-col>
      <nut-col :span="6">
        <div class="content">{{ item.name }}</div>
      </nut-col>
      <nut-col :span="10">
        <div class="content">
          <nut-button type="primary" size="mini" @click="resetStaff(item)"
            >重置 </nut-button
          >&nbsp;
          <nut-button type="warning" size="mini" @click="removeStaff(item.id)"
            >删除
          </nut-button>
        </div>
      </nut-col>
    </nut-row>
    <nut-divider dashed />
  </div>
  <div class="page">
    <nut-pagination
      v-model="page.currentPage"
      :total-items="page.total"
      :items-per-page="page.pageSize"
      @change="change"
    />
  </div>
  <div class="page-footer">
    <p>{{ page.currentPage }}/{{ totalPage }}页</p>
    <nut-button size="small" type="default" @click="toTop">首页</nut-button>
    &nbsp;
    <nut-button size="small" type="default" @click="toEnd">尾页</nut-button>
  </div>
</template>

<style scoped>
.staff-from {
  width: 90%;
  margin: 20px auto 8px;
  text-align: center;

  .el-input {
    margin-right: 10px;
  }
}

.search {
  width: 90%;
  margin: 8px auto 15px;
  text-align: center;
  .nut-divider {
    margin: 15px 0 0;
  }
}

.table {
  width: 90%;
  margin: 0 auto;
  text-align: right;

  .content {
    height: 100%;
    font-weight: bold;
  }
  .nut-divider {
    margin: 6px 0;
  }
}

.page {
  width: 90%;
  margin: 0 auto;
  display: flex;
  justify-content: center;
  align-items: center;
}
.page-footer {
  text-align: center;
  p {
    color: #7a7374;
    margin: 0 0 5px;
  }
  margin: 10px 0 60px;
}
</style>
