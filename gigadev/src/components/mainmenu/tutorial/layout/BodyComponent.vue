<template>
    <div class="container" :style="style.main.background">
      <div class="top-bar">
        <div class="d-30">D-30</div>
        <div class="balance">500,000원</div>
      </div>
  
      <div class="main-content">
        <div class="instruction-box">
          <span class="instruction-text">발주를 눌러보세요.</span>
          <div class="red-notification">1</div>
        </div>
  
        <div class="action-buttons">
          <button class="primary-button">발주</button>
          <button class="secondary-button">창고</button>
          <button class="secondary-button">은행</button>
        </div>
  
        <button class="open-button">OPEN</button>
      </div>
    </div>
  </template>
<script>

const model ={
  style:{
      main:{
        background:"@/assets/tutorialMain.jpg"
      }
  }
}

export default {
    name:'tutorialVue'
  ,data(){
    return model;
  }

}
</script>

<style scoped>
.container {
  width: 100%;
  max-width: 375px;
  margin: 0 auto;
  font-family: Arial, sans-serif;
  
}

.top-bar {
  display: flex;
  justify-content: space-between;
  padding: 10px;
  background-color: white;
}

.d-30 {
  font-weight: bold;
}

.balance {
  color: brown;
  font-weight: bold;
}

.main-content {   
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.instruction-box {
  position: relative;
  background-color: #f5f5f5;
  padding: 15px;
  border-radius: 10px;
  width: 100%;
  text-align: center;
  margin-bottom: 20px;
}

.red-notification {
  position: absolute;
  top: -10px;
  right: -10px;
  background-color: red;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-buttons {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.primary-button {
  background-color: #5d4037;
  color: white;
  border: none;
  padding: 10px 30px;
  border-radius: 10px;
}

.secondary-button {
  background-color: #f5e6d3;
  color: black;
  border: none;
  padding: 10px 30px;
  border-radius: 10px;
}

.open-button {
  background-color: #4a148c;
  color: white;
  border: none;
  padding: 15px 100px;
  border-radius: 10px;
  font-weight: bold;
}
</style>