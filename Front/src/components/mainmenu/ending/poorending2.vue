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
  name: 'poorending',
  data() {
    return {
      inputText: ''
    };
  },
  methods:{
    movePage(){
      this.$router.push('/poorending3')
    }
  },
  mounted() {
    let innerText = '근데... 아니더라 대출은 끝내 갚지도 못했어\n 빚은 눈덩이처럼 불어나더니 어느새 나를 삼켜버렸고 결국 남은 건 아무것도 없었어';

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

    background-image: url('/common/ending1.png');
    background-size: 100% 100%;
}
.dialog-text {
  font-family: prebold;
  position: absolute;
  left: 10%;
  bottom: 11%;
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