<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
  created() {
  this.$store.dispatch('initializeAuth')
}
}
</script>

<style>
/* 全局样式 */
:root {
  --primary: #00d4ff;
  --secondary: #0072ff;
  --dark: #0a192f;
  --light: #ccd6f6;
  --success: #4cb944;
  --warning: #ff6b35;
  --danger: #e74c3c;
}

body {
  background-color: var(--dark);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: var(--light);
  margin: 0;
  padding-top: 90px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.btn {
  padding: 10px 20px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary {
  background: linear-gradient(135deg, var(--primary) 0%, var(--secondary) 100%);
  border: none;
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 212, 255, 0.4);
}

.btn-outline {
  background: transparent;
  border: 1px solid var(--primary);
  color: var(--primary);
}

.btn-outline:hover {
  background: rgba(0, 212, 255, 0.1);
}

/* 其他全局样式... */
</style>