<template>
    <div>
        <div id="maincontainer">
            <img id="firstimg" src="/images/tycoon.png" alt="지붕있는 타이쿤 이미지" width="340" height="220">
            <img id="secondimg" src="/images/tycoonname.png" alt="타이쿤 이름 이미지" width="400" height="120">
            <div class="button-container">
                <button id="loginbtn" @click="goToLogin">로그인</button>
                <button id="joinbtn" @click="goToJoin">회원가입</button>
            </div>
            <router-view></router-view>
        </div>
    </div>
</template>
<script>
export default {
    name:'mainVue',
    methods: {
        goToLogin() {
            this.$router.push('/login'); //로그인 페이지로 이동
        },
        goToJoin() {
            this.$router.push('/join'); //회원가입 페이지로 이동
        }
    }
}
</script>
<style scoped>
@font-face {
  font-family: 'rk';
  src: url('/fonts/Recipekorea-FONT.ttf') format('truetype');
}
#loginbtn {
    font-family: rk;
    font-size: 1.5vw;
    text-align: center;
    border: 0.4vw solid #5E395A;
    border-radius: 1.5vw;
    width: 11vw;
    height: 4.8vw;
    padding: 1vw 2vw;
    background-color: #FFEFCA;  
    color: #5E395A;
    cursor: pointer;
}
#joinbtn {
    font-family: rk;
    font-size: 1.5vw;
    text-align: center;
    border: 0.4vw solid #873511;
    border-radius: 1.5vw;
    width: 11vw;
    height: 4.8vw;
    padding: 1vw 2vw;
    background-color: #FFEFCA;
    color: #873511;
    cursor: pointer;
}
#maincontainer {
  display: flex;
  flex-direction: column;
  align-items: center; 
  height: 100vh;
  gap:3vw   ;
  width: 100vw;
  background-image: url('/background/convenientbg.png');
  background-size: 100% 100%;
  min-height: 100vh;
}
.button-container {
  display: flex;
  flex-direction: column;
  gap: 0.6vw;
}
#firstimg {
   margin-top: 6vw;
   width: 25vw;
   height: 28vh;
}
#secondimg {
    width: 25vw;
    height: 15vh;
}
</style>