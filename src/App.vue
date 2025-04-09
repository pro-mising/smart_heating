<template>
  <div id="app">
    <router-view></router-view> <!-- 用于加载路由视图 -->
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
.app {
  height: 100%;
  margin: 0;
  padding: 0;
}

.app {
  display: flex;
  flex-direction: column;
  width: 100%;
}
</style>
