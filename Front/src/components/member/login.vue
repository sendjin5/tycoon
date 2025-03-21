<template>
  <div id="maincontainer">
    <form>
      <main id="mainbox">
        <input id="inputId" type="text" name="userId" v-model="inputId" placeholder="ID" /><br />
        <input
          id="inputPw"
          type="password"
          name="userPw"
          v-model="inputPw"
          placeholder="Password"
        /><br />
        <div id="checkbox">
          <input id="inputcheck" type="checkbox" name="remember" />
          <span id="remembertext">아이디 기억하기</span>
        </div>
        <br />
        <div id="btns">
          <button id="loginbtn" type="button" @click="loginAccess">로그인</button>
          <button id="resetbtn" type="reset" @click="goToMain">취소</button>
        </div>
        <div id="findbox">
          <button id="findbtn" type="button" @click="goToFind">Id / Password 찾기</button>
        </div>
      </main>
    </form>
  </div>
  <router-view></router-view>
</template>
<script>
import { revenueStore } from "@/assets/pinia/maingame";

export default {
  name: "loginVue",
  data() {
    return {
      inputId: "",
      inputPw: "",
      revenue: revenueStore(),
    };
  },
  methods: {
    loginAccess: function () {
      console.log("로그인 요청:", this.inputId, this.inputPw);
      // fetch('http://3.38.185.252:8080/spring/api/loginMember', {
      fetch("http://localhost:8080/spring/api/loginMember", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },

        body: JSON.stringify({
          userId: this.inputId,
          password: this.inputPw,
        }),
      })
        .then((response) => {
          alert("로그인 요청 성공" + response.status);
          console.log(response.status);

          if (!response.ok) {
            if (response.status === 404) {
              alert("회원을 찾을 수 없습니다.");
            } else {
              alert("회원을 찾을 수 없습니다.");
            }
            return null;
          } else {
            return response.json(); // 응답 데이터 파싱
          }
        })
        .then((data) => {
          if (data) {
            this.revenue.userId = data.userId;
            // 세션 스토리지에 로그인 정보 저장
            sessionStorage.setItem("loginUser", JSON.stringify(data));
            sessionStorage.setItem("isLoggedIn", "true");
            console.log("로그인 성공:", data);
            this.goToHome();
          }
        })
        .catch((error) => {
          console.error("로그인 요청 실패:", error);
          alert("로그인 처리 중 오류가 발생했습니다.");
        });
    },

    goToHome() {
      this.$router.push("/homeMenu");
    },
    goToMain() {
      this.$router.push("/"); //메인페이지로 이동
    },
    goToFind() {
      this.$router.push("/find"); //id,pw찾기 페이지로 이동
    },
  },
};
</script>
<style scoped>
* {
  box-sizing: border-box;
}
@font-face {
  font-family: "rk";
  src: url("/fonts/Recipekorea-FONT.ttf") format("truetype");
}
@font-face {
  font-family: "prebold";
  src: url("/fonts/Pretendard-bold.woff") format("woff");
}
#inputId {
  font-family: prebold;
  width: 29.4vw;
  height: 8vh;
  border: 0.3vw solid #5e395a;
  border-radius: 1.2vw;
  padding: 1vw 1vw;
  margin-top: 6vh;
  margin-bottom: 3vh;
  font-size: 1.5vw;
  font-weight: bold;
}
#inputPw {
  font-family: prebold;
  width: 29.4vw;
  height: 8vh;
  border: 0.3vw solid #5e395a;
  border-radius: 1.2vw;
  padding: 1vw 1vw;
  margin-bottom: 2vh;
  font-size: 1.5vw;
  font-weight: bold;
}

#maincontainer {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100vw;
  height: 100vh;
  background-image: url(/background/convenientbg2.png);
  background-size: 100% 100%;
  min-height: 100vh;
}
#mainbox {
  display: block;
  justify-items: center;
  width: 37vw;
  height: 53vh;
  border: 0.3vw solid #5e395a;
  border-radius: 2.2vw;
  background-color: #ffefca;
  margin-top: -10vh;
}
#loginbtn {
  font-family: rk;
  width: 12vw;
  height: 7.6vh;
  border: 0.3vw solid #6f3533;
  border-radius: 1.1vw;
  background-color: #6f3533;
  color: #ffffff;
  padding: 1vw 2vw;
  font-size: 1.4vw;
  margin-right: 1.7vw;
  cursor: pointer;
}
#resetbtn {
  font-family: rk;
  width: 12vw;
  height: 7.6vh;
  border: 0.4vw solid #6f3533;
  color: #6f3533;
  padding: 1vw 2vw;
  border-radius: 1.1vw;
  background-color: #ffefca;
  font-size: 1.5vw;
  margin-left: 1.7vw;
  cursor: pointer;
}
#remembertext {
  margin-bottom: -0.4vw;
}
#checkbox {
  font-family: rk;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 0.4vw;
  margin-left: 9vw;
  width: 100%;
  font-size: 1.3vw;
  height: 2vh;
  accent-color: #6f3533;
  cursor: pointer;
}

#inputcheck {
  width: 1.9vw;
  height: 3vh;
  border: 4px solid #6f3533; /* 테두리 두께 & 색상 변경 */
  appearance: none; /* 브라우저 기본 스타일 제거 */
  -webkit-appearance: none;
  -moz-appearance: none;
  outline: none; /* 클릭 시 기본 포커스 제거 */
  background-color: #ffffff;
  cursor: pointer;
}
#inputcheck:checked {
  background-color: #ffffff; /* 체크되었을 때 배경색 */
}
#inputcheck:checked::before {
  content: "✔";
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  color: black;
}
#btns {
  margin-top: 4vh;
}
#findbtn {
  all: unset;
  font-family: rk;
  margin-top: 5vh;
  font-weight: bold;
  font-size: 1vw;
  cursor: pointer;
}
</style>
