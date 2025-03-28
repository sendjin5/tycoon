<template>
  <div>
    <div id="maincontainer">
      <img
        id="firstimg"
        src="/images/tycoon.png"
        alt="지붕있는 타이쿤 이미지"
        width="340"
        height="220"
      />
      <img
        id="secondimg"
        src="/images/tycoonname.png"
        alt="타이쿤 이름 이미지"
        width="400"
        height="120"
      />
      <div class="button-container">
        <button class="firstbtn" id="loginbtn" @click="goToLogin">로그인</button>
        <button class="firstbtn" id="joinbtn" @click="goToJoin">회원가입</button>
      </div>
      <router-view></router-view>
    </div>
  </div>
</template>
<script>
export default {
  name: "mainVue",
  methods: {
    goToLogin() {
      this.$router.push("/login"); //로그인 페이지로 이동
    },
    goToJoin() {
      this.$router.push("/join"); //회원가입 페이지로 이동
    },
  },
};
</script>
<style scoped>
@font-face {
  font-family: "rk";
  src: url("/fonts/Recipekorea-FONT.ttf") format("truetype");
}
.firstbtn {
  font-family: rk;
  font-size: 1.8em;
  text-align: center;
  border-radius: 30px;
  min-width: 200px;
  width: 11vw;
  min-height: 80px;
  height: 4vw;
  padding: 18px 30px 10px;
  cursor: pointer;
}
#loginbtn {
  border: 6px solid #5e395a;
  background-color: #ffefca;
  color: #5e395a;
}
#joinbtn {
  border: 6px solid #873511;
  background-color: #ffefca;
  color: #873511;
}
#loginbtn:hover {
  background-color: #5e395a;
  color: white;
}
#joinbtn:hover {
  background-color: #873511;
  color: white;
}
#maincontainer {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100vh;
  gap: 3vw;
  width: 100vw;
  background-image: url("/background/convenientbg.png");
  background-size: 100% 100%;
  min-height: 100vh;
}
.button-container {
  display: flex;
  flex-direction: column;
  gap: 0.6vw;
}
#firstimg {
  margin-top: 3vw;
  width: 25vw;
  min-width: 500px;
  height: 33vh;
}
#secondimg {
  width: 25vw;
  min-width: 500px;
  height: 15vh;
}
</style>
