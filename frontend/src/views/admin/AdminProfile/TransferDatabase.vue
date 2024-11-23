<script setup>
import { useRoute, useRouter } from 'vue-router'
import { changSqlService } from '@/api/orders.js'
import { successMsg } from '@/utils/SendMsgUtils.js'
import { backupDBService } from '@/api/common.js'
import { DatabaseSync, ExchangeThree } from '@icon-park/vue-next'
import { baseURL } from '@/utils/request.js'
import { ref } from 'vue'

const router = useRouter()
const route = useRoute()

const active = ref(0)

const onClick = () => {
  if (route.fullPath.includes('admin')) router.push('/admin/profile')
  else router.push('/staff/profile')
}

const change = async () => {
  if (!confirm('是否确认进行转换？')) return
  const resp = await changSqlService()
  if (resp.code === 1) {
    successMsg(resp.data)

    showDialog({
      allowHtml: true,
      message: '师弟师妹，数据已转到历史记录。<br/>╰(*´︶`*)╯ ——22届师兄',
      theme: 'round-button'
    })

    if (active.value === 1) active.value = 2
  }
}

const loading = ref(false)

const downloadBackup = async (status) => {
  loading.value = true
  const resp = await backupDBService(status)
  if (resp.code === 1) {
    successMsg(resp.data)
    if (active.value === 0) active.value = 1
  }
  window.location.href = baseURL + '/common/download-zip?status=' + status

  loading.value = false
}
</script>

<template>
  <nut-navbar title="转存数据库" left-show @click-back="onClick">
    <template #left>
      <div>返回</div>
    </template>
  </nut-navbar>
  <p style="text-align: center; color: red">建议在电脑浏览器打开！！！</p>
  <div class="base">
    <div class="steps">
      <van-steps :active="active">
        <van-step>备份</van-step>
        <van-step>转换</van-step>
        <van-step>再次备份</van-step>
      </van-steps>
    </div>

    <div class="t-sql">
      <div v-show="active === 0" class="btn">
        <p>
          这个备份的是现在状态的数据库，里面有这个学期大家上传的全部照片，请妥善保管。<br />
          (´･ᴗ･`)
        </p>
        <nut-button :loading="loading" type="info" @click="downloadBackup(1)">
          <template #icon>
            <database-sync
              class="step-icon"
              theme="two-tone"
              size="24"
              :fill="['#333', '#2F88FF']"
            />
          </template>
          数据库备份
        </nut-button>
        <p style="color: red; font-size: 12px">
          注意！下载文件要在1个半小时内下载完，不然接口会自动关闭
        </p>
      </div>
      <div v-show="active === 1" class="btn">
        <p>
          这个功能是把所有的订单转换到历史订单并删除现在的订单表 <br />(｡◕ˇ∀ˇ◕)
        </p>
        <nut-button type="info" @click="change">
          <template #icon>
            <exchange-three
              class="step-icon"
              theme="two-tone"
              size="24"
              :fill="['#333', '#2F88FF']"
            />
          </template>
          切换数据库状态
        </nut-button>
      </div>
      <div v-show="active === 2" class="btn">
        <p>
          这个备份是把转换过的数据库进行备份，下个学期部署上线就是用这一份数据库
          <br />(⌒_⌒;)
        </p>
        <nut-button :loading="loading" type="info" @click="downloadBackup(2)">
          <template #icon>
            <database-sync
              class="step-icon"
              theme="two-tone"
              size="24"
              :fill="['#333', '#2F88FF']"
            />
          </template>
          再次备份数据库
        </nut-button>
        <p style="color: red; font-size: 12px">
          注意！下载文件要在1个半小时内下载完，不然接口会自动关闭
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.base {
  border-radius: 15px;
  background-color: white;
  width: 80%;
  max-width: 800px;
  margin: 20px auto;
  .steps {
    width: 80%;
    max-width: 800px;
    margin: 0 auto;
    .van-steps {
      border-radius: 15px;
    }
  }
  .t-sql {
    text-align: center;
    max-width: 800px;
    width: 80%;
    margin: 0 auto;
    padding: 0 0 1px;
    p {
      color: #7a7374;
      font-weight: bold;
      font-size: 14px;
    }
    .btn {
      margin-bottom: 15px;
      .step-icon {
        display: inline-flex;
        align-items: center;
        justify-content: center;
      }
    }
  }
}
</style>
