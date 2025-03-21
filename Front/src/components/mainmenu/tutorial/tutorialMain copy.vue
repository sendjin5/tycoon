<template>
    <div>
        <LayoutComponent>
            <template v-slot:header>
                <HeaderComponent/>
            </template>
            <template v-slot:article>
                <BodyComponent/>
            </template>
        </LayoutComponent>
    </div>

  </template>
<script>
import BodyComponent from './layout/BodyComponent.vue';
import HeaderComponent from './layout/HeaderComponent.vue';
import LayoutComponent from './layout/LayoutComponent.vue';




export default {
    name:'tutorialVue'
    ,components:{BodyComponent,HeaderComponent,LayoutComponent}
}
</script>

<style scoped>
.container {
  width: 100%;
  max-width: 375px;
  margin: 0 auto;
  font-family: Arial, sans-serif;
  background-image: './tutorial/tutorialIMG/image.png';
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