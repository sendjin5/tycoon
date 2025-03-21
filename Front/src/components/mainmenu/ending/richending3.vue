<template>
  <div class="main-container">
    <div class="dialog-box">
      <div class="dialog-text" v-html="inputText"></div>
      <div class="dialog-arrow" @click="movePage">▼</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ending3',
  data() {
    return {
      inputText: ''
    };
  },
  methods:{
    movePage(){
      this.$router.push('/homeMenu')
    }
  },
  mounted() {
    let innerText = '성공은 어쩌면 멀리 있지 않는 것 같아\n 익숙한 거리에서 나는 누구보다 빛나고 있어!! 이제 다음 목표를 향해 가보자!!!';

    for (let i = 0; i < innerText.length; i++) {
      setTimeout(() => {
        if (innerText[i] === '\n') {
          this.inputText += '<br>';
        } else {
          this.inputText += innerText[i];
        }
      }, i * 50);
    }
  }
};
</script>
<style scoped>
@font-face {
  font-family: 'prebold';
  src: url('/fonts/Pretendard-bold.woff') format('woff');
}
  .main-container {
    width: 100%;
    height: 100vh;
    position: relative;
    overflow: hidden;

    font-family: pretendard;
    background-color: #f5f5f5;
  
    max-width: 100%;
    min-height: 90%;

    display: flex;

    background-image: url('/common/ending3.png');
    background-size: 100% 100%;
}
.dialog-text {
  font-family: prebold;
  position: absolute;
  left: 10%;
  bottom: 12%;
  font-size: 2vw;
  font-weight: bold;
}
.dialog-arrow{
  position: absolute;
  bottom: 13%;
  right: 5%;
  font-size: 1.4vw;
}
</style>