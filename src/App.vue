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

.login-container {
  width: 400px;
  margin: 0 auto;
  padding: 40px;
  background: rgba(16, 36, 69, 0.8);
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #00d4ff;
}

.form-control {
  width: 100%;
  padding: 12px;
  background: rgba(10, 25, 47, 0.5);
  border: 1px solid #1f3a68;
  border-radius: 6px;
  color: white;
}

.btn {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, #00d4ff, #0072ff);
  border: none;
  border-radius: 6px;
  color: white;
  cursor: pointer;
}

.footer-links {
  margin-top: 20px;
  text-align: center;
}

.footer-links a {
  color: rgba(204, 214, 246, 0.7);
  margin: 0 10px;
}
/* 其他全局样式... */

</style>