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
      this.$router.push('/richending3')
    }
  },
  mounted() {
    let innerText = '사람들이 끊이질 않는 내 가게 매출은 꾸준하게 오르고 있고\n 어느새 동네 사람들이나 친구들은 나를 부러워해';

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