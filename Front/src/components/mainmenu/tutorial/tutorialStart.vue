<template>
 <div class="main-container" @click="linkMainMenu">
    
    <div @click="movePage">
      <img class="skip-button" src="/tutorial/button/skip.png">
    </div>
  
    <div id="bankmanbox">
      <img id="bankman" src="/person/mainbankman.png">
    </div>
    <div class="dialog-box">
      <div class="dialog-text">{{ inputText }}</div>
      <div class="dialog-arrow">▼</div>
    </div>
  
  </div>
</template>

  
<script>
export default {
  name: 'intro',
  data(){
    return {
      text:'안녕하세요. CK 본사에서 나왔습니다. 편의점 시스템을 알려드리겠습니다.',
      inputText:'',
      textread:0,
      interval:'',
    }
  },
  methods:{
      linkMainMenu(){
        if(this.inputText!=this.text){
          clearInterval(this.interval);
          this.inputText = this.text;
          this.textread = 0;
        }else if(this.inputText==this.text){
          return this.$router.push('/tutorialMain');
        }
      },
      movePage() {
        this.$router.push('/')
      }
  },
  mounted(){
    this.interval = setInterval(()=>{
      this.inputText += this.text[this.textread];
      this.textread++;
      if(this.inputText == this.text){
        clearInterval(this.interval);
        this.textread = 0;
      }
    },50)
    setTimeout(()=>{
      this.horror=true;
    },5000)
  }
}
</script>

<style scoped>
  @font-face {
  font-family: 'prelight';
  src: url('/fonts/Pretendard-Light.woff') format('woff');
}
.main-container {
    width: 100%;
    height: 100vh;
    position: relative;
    overflow: hidden;
    


    font-family: pretendard;
  background-color: #f5f5f5;
  
  text-align: center;
  max-width: 100%;
  min-height: 90%;


  display: flex;
  flex-direction: column;
  justify-content: center; 
  align-items: center; 

  background-image: url('/tutorial/tutorialMain.png');
  background-size: 100% 100%;

}

.skip-button {
  position: absolute;
  top: 16vh;
  right: 4vw;
  width: 3vw;
}

.dialog-box {
  position: absolute;
  margin-top: 63vh;
  width: 70%;
  background-color: #F2F1EC;
  border-radius: 2vw;
  padding: 1vw 1.5vw;
  margin-bottom: 5vh;
  height: 20vh; 
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 1vw 2vw rgba(0, 0, 0, 0.1);
}

.dialog-text {
  text-align: left;
  font-size: 1.8vw;
  margin: 0;
  flex-grow: 1;
  color: #333;
  font-family: prelight;
}

.dialog-arrow {
  font-size: 1.6vw;
  bottom: 8px;
  right: 16px;
  color: #666;
}

.bottom-indicator {
  position: absolute;
  bottom: 10px;
  color: #333;
  font-size: 18px;
}
#bankmanbox {
  display: flex;
  justify-content: center;
  align-items: center;
}
#bankman {
  position: absolute;
  width: calc(35%);
  width: 17vw;
  height: 55%;
}
</style>