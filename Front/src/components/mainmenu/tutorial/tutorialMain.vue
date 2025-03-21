<template>
  <div class="blind" @click="nextPage"></div>
  <div class="main-container">
    <div class="topbar">
      <p class="left-section">D-30</p>
      <div style="display:flex;align-items:center;">
          <div class="moneybar">
              <img src="/icons/money.png" style="width:3.5vh;height:3.5vh;">
              <div class="line"></div>
              <div class="money"><p>{{ money }}원</p></div>
          </div>
          <img src="/icons/gameoption.png" style="width:3vh;height:3vh;" class="settings-icon" @click="opensettings=!opensettings">
      </div>
    </div>

    <!-- <div class="speech-bubble">
      <div class="speech-bubble-content">
        <p>이 화면은 게임 시작전 준비공간입니다.</p>
        <p>편의점 편의점 운영중에는 다시 방문할 수 없습니다.</p>
        <p>신중하게 결정하고 오픈해주세요</p>
      </div>
      <div class="arrow-down"></div>
    </div> -->
  
    
    <div class="menu-container">
      <div class="menu-button" :class="{'tutoClick':this.tutoPage.tutopage=='ordering'}" @click="linkOrdering">
        <span class="button-text">
          <img src="/tutorial/button/orderingbutton.png">
        </span>
      </div>
      
      <div class="menu-button" :class="{'tutoClick':this.tutoPage.tutopage=='storage'}" @click="linkStorage">
        <span class="button-text">
          <img src="/tutorial/button/storagebutton.png">
        </span>
      </div>
      
      <div class="menu-button" :class="{'tutoClick':this.tutoPage.tutopage=='bank'}" @click="linkBank">
        <span class="button-text"><img src="/tutorial/button/bankbutton.png"></span>
      </div>
    </div>
    
    <div class="open-button-container">
      <button class="open-button" :class="{'tutoClick':this.tutoPage.tutopage=='maingame'}" @click="this.tutoPage.pagenum=0;$router.push('/linkGameTutorial');"></button>  <!-- 실험용 이벤트 -->
    </div>
  </div>
  <div v-show="tutoPage.pagenum==0" class="tuto win1">
    <p>이 화면은 게임 시작 전 준비 공간입니다.</p>
    <p>편의점 운영 중에는 다시 방문할 수 없습니다.</p>
    <p>신중하게 결정하고 오픈해주세요.</p>
  </div>
  <div v-show="tutoPage.pagenum==1" class="tuto win2">
    <p>은행을 눌러보세요.</p>
  </div>
  <div v-show="tutoPage.pagenum==2" class="tuto win3">
    <p>발주를 눌러보세요.</p>
  </div>
  <div v-show="tutoPage.pagenum==3" class="tuto win4">
    <p>발주한 물건을 확인해보세요.</p>
  </div>
  <div v-show="tutoPage.pagenum==4" class="tuto win5">
    <p>이제 OPEN 버튼을 눌러</p>
    <p>편의점을 운영해봅시다!</p>
  </div>
</template>

<script>
import { curTutoPage } from '@/assets/pinia/tutorial';  // @/assets 뺴기!!!!!

export default {
  name: 'KoreanMenuInterface',
  data(){
    return{
      tutoPage:curTutoPage(),
      storageSize:50,
      money: 500000,
    }
  },
  methods:{
    linkOrdering(){
      this.$router.push('/linkOrderingTutorial');
    }
    ,linkStorage(){
      this.$router.push('/linkStorageTutorial');
    }
    ,linkBank(){
      this.$router.push('/linkBankTutorial')
    },
    nextPage(){
      if(this.tutoPage.tutopage=='main'){
        this.tutoPage.tutopage='bank';
        this.tutoPage.pagenum++;
      }
    }
  },
}
</script>

<style scoped>
.main-container {
  width: 100%;
  height: 100vh;
  position: relative;
  overflow: hidden;

  font-family: RecipekoreaOTF;
  font-size: 24px;
  background-color: #f5f5f5;

  text-align: center;
  max-width: 100%;
  min-height: 90%;


  display: flex;
  flex-direction: column;
  align-items: center; 


  background-image: url('/background/whitebg.png');
  background-size: 100% 100%;

}
.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.6vw 1.7vw;
  margin-bottom: 1vh;
  border: 0.25vw solid #8B4513;
  border-radius: 9999px;
  width: 90vw;
  margin-top: 4vh;
  margin-left: 3vw;
  height: 5vh;
}
.left-section {
  font-size: 1.5vw;
  font-weight: bold;
  text-align: left;
}
.moneybar{
  display:flex;
  justify-content:space-around;
  align-items:center;
  width: 15vw;
  height:7vh;
  margin-right:1.5vw;
  background-image:url(/element/moneybar.png);
  background-size: 100% 100%;
}
.line{
  min-height:2.5vh;
  border-left:0.2vw dashed rgba(256,256,256,0.2);
}
.money{
  display:flex;
  justify-content:flex-end;
  width:6vw;
  font-size: 1.3vw;
}
.speech-bubble {
position: flex;
/* bottom: 80px; */
align-items: center;
transform: translateX(-50%);
width: 90%;
max-width: 900px;
background-color: white;
border-radius: 20px;
box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
margin-left:50%;
}

.speech-bubble-content {
width: 100%;
text-align: left;
font-size: 18px;
line-height: 1.5;
font-family: 'Noto Sans KR', sans-serif;
}

.speech-bubble-content p {
margin: 10px 0;
}


.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.6vw 1.7vw;
  margin-bottom: 11vh;
  border: 0.25vw solid #8B4513;
  border-radius: 9999px;
  min-width: 90vw;
  margin-top: 3vh;
  height: 5vh;
}

.left-section {
  font-size: 1.5vw;
  font-weight: bold;
  text-align: left;
}
  .money-bag {
  display: flex;
  align-items: center;
  background-color: #5D2906;
  color: white;
  padding: 0.6vw 1.4vw;
  border-radius: 9999px;
  gap:3.5vw;
  height: 3.3vh;
  width: 13vw;
}

.money-bag {
font-size: 1.5vw;
}

.right-section {
  display: flex;
  align-items: center;
  gap: 1vw;
  text-align: right;
  margin-left: auto;
}

.bag-icon {
  margin-right: 0.3vw;
}

.settings-icon {
  font-size: 2vw;
}

.notification {
  position: relative;
}

.notification-badge {
  position: absolute;
  top: -10px;
  right: -10px;
  background-color: red;
  color: white;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
}

.menu-container {
  width: 80%;
  display: flex;
  justify-content: space-between;
  margin-top: 14vh;
  margin-bottom: 20vh;
}

.button-text > img{
  width: 20vw;
  height: 15vh;
  /* background-color: #FFF8DC;
  border: 2px solid #8B4513; */
  border-radius: 2vw;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
 
}


.open-button-container {
  display: flex;
  justify-content: center;
  /* background-image: url('/tutorial/button/openbutton.png'); */
}

.open-button {
  width: 14.5vw;
  height: 9vh;
  background-image: url('/tutorial/button/openbutton.png');
  background-size: contain;
  background-repeat: no-repeat;
  
  border: none;
  border-radius: 1vw;
  cursor: pointer;
}
.tutoClick{
  position:relative;
  z-index:10;
}
.blind{
  position:absolute;
  width:100vw;
  height:100vh;
  z-index:10;
}
.tuto{
  position:absolute;
  background-color:#FFEDDE;
  font-size:2vh;
}
.tuto p{
  margin:1vh 2vw;
}
.win1{
  top:12vh;
  left:33vw;
}
.win2{
  top:18vh;
  right:15vw;
}
.win3{
  top:18vh;
  left:15vw;
}
.win4{
  top:18vh;
  left:40vw;
}
.win5{
  bottom:33vh;
  right:22vw;
  z-index:15;
}
</style>