<template>
  <div id="maincontainer">
    <div id="titlebox">
      <button id="backbtn" @click="goToLogin"></button>
      <h1 id="maintitle">Id / Password 찾기</h1>
    </div>
    <main id="mainbox">
      <div id="main1">
        <h2><span>*</span>ID (아이디) 찾기</h2>
        <input
          class="firstinput"
          type="text"
          name="userNick"
          v-model="userNick"
          placeholder="닉네임을 입력하세요."
        />
        <input
          type="email"
          name="userEmail"
          v-model="email"
          placeholder="Email(이메일)을 입력하세요."
        />
        <button class="findbtn" id="idfindbtn" @click.prevent="findId">아이디 찾기</button>
      </div>

      <div id="main2">
        <h2><span>*</span>PW (비밀번호) 찾기</h2>
        <input
          class="firstinput"
          type="text"
          name="userId"
          v-model="userId"
          placeholder="Id(아이디)를 입력하세요."
        />
        <input type="text" name="userNick" v-model="nick" placeholder="닉네임을 입력하세요." />
        <input
          type="email"
          name="userEmail"
          v-model="userEmail"
          placeholder="Email(이메일)을 입력하세요."
        />
        <button class="findbtn" id="pwfindbtn" @click.prevent="findPwd">비밀번호 찾기</button>
      </div>
      <div v-show="popup" class="popup-overlay" @click="closePopup">
        <div class="popup-content" @click.stop>
          <div class="popup-header">
            <p>알림</p>
          </div>
          <div class="popup-body">
            <p>{{ popupMessage }}</p>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>
<script>
export default {
  name: "findVue",
  data() {
    return {
      userNick: "",
      userEmail: "",
      userId: "",
      nick: "",
      email: "",
      popup: false,
    };
  },
  methods: {
    goToLogin() {
      this.$router.push("/login"); // 로그인 페이지로 이동
    },
    findId: function () {
      console.log("debbue");
      fetch(__apiUrl__ + "/api/findId", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          nick: this.userNick,
          email: this.email,
        }),
      })
        .then((res) => {
          if (res.ok) {
            return res.json();
          } else {
            alert("회원을 찾을 수 없습니다.");
          }
        })
        .then((data) => {
          console.log(data.userId);

          this.popup = true;
          this.popupMessage = "아이디는 " + data.userId + " 입니다.";
        });
    },
    findPwd: function () {
      fetch(__apiUrl__ + "/api/findPwd", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          userId: this.userId,
          nick: this.nick,
          email: this.userEmail,
        }),
      })
        .then((res) => {
          if (res.ok) {
            return res.json();
          } else {
            alert("회원을 찾을 수 없습니다.");
          }
        })
        .then((data) => {
          console.log(data.password);

          this.popup = true;
          this.popupMessage = "비밀번호는 " + data.password + " 입니다.";
        });
    },
    closePopup() {
      this.popup = false;
      this.storage = false;
      this.popupMessage = "";
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
#maincontainer {
  width: 100%;
  height: auto;
  min-height: 100vh;
  background-image: url("@/assets/background/whitebg.png");
  background-size: 100% 100%;
  padding: 5%;
}
#backbtn {
  all: unset;
  width: 2vw;
  height: 2vw;
  background-image: url("@/assets/icons/back.png");
  background-repeat: no-repeat;
  background-size: contain;
  cursor: pointer;
}
#titlebox {
  display: flex;
}
#maintitle {
  margin: 0 auto;
  font-family: rk;
}
h1 {
  font-size: 2.2vw;
}
h2 {
  font-size: 1.3vw;
  font-family: rk;
}
#mainbox {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  height: 75vh;
  width: 100%;
  flex-grow: 1;
  gap: 8%;
}
#main1 {
  background-color: #ffefca;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 5%;
  border: 0.3vw solid #56174f;
  border-radius: 2vw;
  width: 30vw;
  height: 40vh;
}
#main2 {
  background-color: #ffefca;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 5%;
  border: 0.3vw solid #56174f;
  border-radius: 2vw;
  width: 30vw;
  height: 40vh;
}
span {
  color: red;
}
input {
  border: 0.2vw solid #56174f;
  border-radius: 0.8vw;
  width: 24vw;
  height: 5vh;
  padding-left: 1vw;
}
input::placeholder {
  font-size: 1vw;
  font-family: prebold;
}
.firstinput {
  margin-top: 0;
}
.findbtn {
  font-family: rk;
  background-color: #56174f;
  color: #ffffff;
  font-size: 1.1vw;
  font-weight: bold;
  padding: 2% 10%;
  border: #56174f;
  border-radius: 0.5vw;
  cursor: pointer;
}
#idfindbtn {
  margin-top: 3vh;
}
#pwfindbtn {
  margin-top: -0.5vh;
}

.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
}

.popup-content {
  background-color: #f2f1ec;
  width: 400px;
  border-radius: 30px;
  overflow: hidden;
}

.popup-header {
  background-color: #6a396c;
  padding: 15px;
  text-align: center;
  color: white;
  font-weight: bold;
}

.popup-header p {
  margin: 0;
}

.popup-body {
  padding: 20px;
  text-align: center;
}
</style>
