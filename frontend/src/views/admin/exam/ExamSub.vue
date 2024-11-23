<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import SubItem from '@/components/answer/SubItem.vue'
import { getSubListService } from '@/api/sub.js'

const router = useRouter()

const addSub = (n) => {
  router.push('/admin/profile/exam/add?add=' + n)
}

const page = ref({
  currentPage: 1,
  pageSize: 6,
  total: 0
})
const search = ref('')
const subList = ref([])

const getSubList = async () => {
  const resp = await getSubListService(
    page.value.currentPage,
    page.value.pageSize,
    search.value
  )
  if (resp.code === 1) {
    subList.value = resp.data.records
    page.value.total = resp.data.total
  }
}

getSubList()
const change = async () => {
  await getSubList()
}
const searchSub = async () => {
  await getSubList()
}

const resetList = async () => {
  search.value = ''
  await getSubList()
}
const toTop = async () => {
  page.value.currentPage = 1
  search.value = ''
  await getSubList()
}

const toEnd = async () => {
  page.value.currentPage = Math.ceil(page.value.total / page.value.pageSize)
  search.value = ''
  await getSubList()
}
const totalPage = computed(() => {
  return Math.ceil(page.value.total / page.value.pageSize)
})
</script>

<template>
  <div class="search">
    <el-input
      v-model="search"
      clearable
      style="width: 150px"
      placeholder="请输入题目以查询"
    />&nbsp;
    <nut-button type="info" size="small" @click="searchSub">查询</nut-button>
    &nbsp;
    <nut-button type="info" size="small" @click="resetList">重置</nut-button>
  </div>
  <div style="text-align: center">
    <nut-button type="info" size="small" @click="addSub(1)">
      批量添加
    </nut-button>
    &nbsp;
    <nut-button type="info" size="small" @click="addSub(0)">
      单个添加
    </nut-button>
  </div>
  <div class="subList">
    <div v-for="item in subList" :key="item.id">
      <sub-item :sub="item"></sub-item>
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
  </div>
</template>

<style scoped>
.search {
  width: 90%;
  margin: 8px auto 15px;
  text-align: center;
}
.subList {
  width: 90%;
  margin: 10px auto 15px;
  max-width: 500px;

  .page {
    width: 90%;
    margin: 10px auto;
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
  }
}
</style>
