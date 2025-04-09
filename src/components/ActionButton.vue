<template>
  <div 
    class="action-btn"
    :class="buttonClass"
    @click="handleClick"
  >
    <i :class="iconClass"></i>
    <h3>{{ title }}</h3>
    <p>{{ description }}</p>
  </div>
</template>

<script>
export default {
  name: 'ActionButton',
  props: {
    type: {
      type: String,
      required: true,
      validator: value => ['monitor', 'predict', 'control', 'user'].includes(value)
    },
    title: {
      type: String,
      required: true
    },
    description: {
      type: String,
      required: true
    },
    icon: {
      type: String,
      required: true
    }
  },
  computed: {
    buttonClass() {
      return `btn-${this.type}`
    },
    iconClass() {
      return `fas ${this.icon}`
    }
  },
  methods: {
    handleClick() {
      if (this.type === 'monitor') {
        const fullUrl = window.location.origin + this.$router.resolve({
          name: 'monitor'
        }).href
        window.open(fullUrl, '_blank', 'noopener,noreferrer')
      } else {
        this.$emit('click', this.type)
      }
    }
  }
}
</script>

<style scoped>
/* 保持原有样式不变 */
.action-btn {
  padding: 30px 20px;
  border-radius: 12px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
  position: relative;
  overflow: hidden;
  z-index: 1;
}

.action-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 100%);
  z-index: -1;
}

.action-btn:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.3);
}

.action-btn i {
  font-size: 40px;
  margin-bottom: 15px;
  display: block;
}

.action-btn h3 {
  font-size: 22px;
  margin-bottom: 10px;
}

.action-btn p {
  font-size: 14px;
  color: #ccd6f6;
}

/* 按钮颜色保持原有定义 */
.btn-monitor {
  background: linear-gradient(135deg, #00d4ff 0%, #0072ff 100%);
}

.btn-predict {
  background: linear-gradient(135deg, #6e45e2 0%, #88d3ce 100%);
}

.btn-control {
  background: linear-gradient(135deg, #ff758c 0%, #ff7eb3 100%);
}

.btn-user {
  background: linear-gradient(135deg, #a8ff78 0%, #78ffd6 100%);
}
</style>