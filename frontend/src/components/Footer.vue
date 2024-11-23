<script setup>
import { dialog } from '@/utils/DialogUtils.js'

const icp = window.config.icp

const onClick = () => dialog('邮箱：1305573134@qq.com')
</script>

<template>
  <footer class="footer">
    <div>
      <a
        href="https://beian.miit.gov.cn"
        target="_blank"
        style="text-decoration: none"
        rel="noopener"
      >
        <span style="color: #939393; font-size: 12px; vertical-align: middle">
          {{ icp }}
        </span>
      </a>
    </div>
    <div>
      <a @click="onClick">
        <span style="color: #939393; font-size: 12px; vertical-align: middle">
          Copyright © 2024 gastronome | 联系我
        </span>
      </a>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  text-align: center;
  margin-bottom: 10px;
}
</style>
