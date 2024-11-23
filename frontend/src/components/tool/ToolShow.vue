<script setup>
import { ref, computed, watch } from 'vue'

const props = defineProps({
  appList: {
    type: Object,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  show: {
    type: Boolean,
    required: true
  }
})

const s = computed(() => {
  // 按每3个一组划分appList
  const grouped = []
  for (let i = 0; i < props.appList.length; i += 3) {
    grouped.push(props.appList.slice(i, i + 3))
  }
  return grouped
})

const onClick = (url) => {
  window.open(url)
}

const show = ref(props.show)
watch(
  () => props.show,
  async (newValue) => {
    show.value = newValue
  }
)
</script>

<template>
  <div class="view">
    <div class="title">
      {{ props.title }}&nbsp;
      <nut-button v-if="show" type="info" size="mini" @click="show = !show">
        收起
      </nut-button>
      <nut-button v-else type="info" size="mini" @click="show = !show">
        展开
      </nut-button>
    </div>
    <div v-show="show">
      <van-row :gutter="[10, 10]" v-for="(group, index) in s" :key="index">
        <van-col v-for="(item, idx) in group" :key="idx" span="8">
          <div class="container" @click="onClick(item.url)">
            <img class="icon" :src="item.photo" alt="" />
            <p class="title">{{ item.name }}</p>
          </div>
        </van-col>
      </van-row>
    </div>
  </div>
</template>

<style scoped>
.view {
  width: 90%;
  margin: 0 auto;
  .title {
    margin: 25px 0 5px;
    font-weight: bold;
    font-size: 18px;
  }
  .container {
    width: 90%;
    height: 70%;
    display: inline-flex;
    align-items: center;
    margin: 8px auto;
    cursor: pointer;
    background: #dad4cb;
    padding: 5px;
    border-radius: 8px;
    font-weight: bold;

    .icon {
      background-color: white;
      width: 30%;
      margin-right: 5px;
      border-radius: 5px;
    }
    .title {
      width: 60%;
      margin: 5px 0;
      word-break: break-all;
      font-size: 15px;
    }
  }
}
</style>
