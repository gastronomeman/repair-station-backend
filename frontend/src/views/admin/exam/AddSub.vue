<script setup>
import { useRoute } from 'vue-router'
import AddSubItem from '@/components/answer/AddSubItem.vue'
import AddSubItems from '@/components/answer/AddSubItems.vue'

const route = useRoute()
</script>

<template>
  <div v-if="+route.query.add === 0" class="add0">
    <add-sub-item></add-sub-item>
  </div>
  <div v-else>
    <add-sub-items></add-sub-items>
  </div>
</template>

<style scoped></style>
