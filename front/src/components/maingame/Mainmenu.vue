<template>
  <div class="main-container">
    <Topbar />
    <!-- <div class="header">
      <div class="left-section">D-30</div>
      <div class="right-section">
        <div class="money-bag">
          <span class="bag-icon">💰</span>
          <span class="amount">500,000원</span>
        </div>
        <div class="settings-icon">⚙️</div>
      </div>
    </div> -->

    <!-- <div class="speech-bubble">
      <div class="speech-bubble-content">
        <p>이 화면은 게임 시작전 준비공간입니다.</p>
        <p>편의점 편의점 운영중에는 다시 방문할 수 없습니다.</p>
        <p>신중하게 결정하고 오픈해주세요</p>
      </div>
      <div class="arrow-down"></div>
    </div> -->

    <div class="menu-container">
      <div class="custom-button" @click="linkOrdering">
        <span class="button-text">발주</span>
      </div>

      <div class="custom-button" @click="linkStorage">
        <span class="button-text">창고</span>
      </div>

      <div class="custom-button" @click="linkBank">
        <span class="button-text">은행</span>
      </div>
    </div>

    <div class="open-button-container">
      <button class="open-button" @click="convOpen"></button>
      <!-- 실험용 이벤트 -->
    </div>
  </div>
</template>

<script>
import Topbar from "../common/topbar.vue";
import { revenueStore } from "@/assets/pinia/maingame";

export default {
  name: "KoreanMenuInterface",
  data() {
    return {
      revenue: revenueStore(),
    };
  },
  methods: {
    linkOrdering() {
      this.$router.push("/orderingMain");
    },
    linkStorage() {
      if (history.state.disposeProfit != null) {
        this.$router.push({
          name: "storageMain",
          state: {
            disposeProfit: history.state.disposeProfit,
          },
        });
      } else {
        this.$router.push("/storageMain");
      }
    },
    linkBank() {
      this.$router.push("/bank");
    },
    convOpen() {
      // fetch("",{
      //   method: 'POST',
      //   headers: {
      //     'Content-Type': 'application/json'
      //   },
      //   body: JSON.stringify({
      //     // 근데 뭐 넣지?
      //   })
      // });
      //this.$router.push('/maingame/1');
      this.$router.push("realnews1");
    },
  },
  components: { Topbar },
  mounted() {
    console.log("mainmenu mounted");
    const gameNo = sessionStorage.getItem("gameNo");
    this.revenue.loadState();
    // 그냥 돈만 가져와야지
    fetch(__apiUrl__ + "/spring/maingame/moneydata?gameNo=" + gameNo)
      .then((response) => response.text())
      .then((data) => (this.revenue.cash = data));

    this.revenue.qeezeYN = "N";
    this.revenue.feverYN = "N";
  },
};
</script>

<style scoped>
@font-face {
  font-family: "rk";
  src: url("/fonts/Recipekorea-FONT.ttf") format("truetype");
}
.main-container {
  width: 100vw;
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

  background-image: url("/background/whitebg.png");
  background-size: 100% 100%;
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
  margin-left: 50%;
}

.speech-bubble-content {
  width: 100%;
  text-align: left;
  font-size: 18px;
  line-height: 1.5;
  font-family: "Noto Sans KR", sans-serif;
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
  border: 0.25vw solid #8b4513;
  border-radius: 9999px;
  min-width: 90vw;
  margin-top: 4vh;
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
  background-color: #5d2906;
  color: white;
  padding: 0.6vw 1.4vw;
  border-radius: 9999px;
  gap: 3.5vw;
  height: 3.3vh;
  width: 14vw;
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
  font-family: rk;
  width: 80%;
  display: flex;
  justify-content: space-between;
  margin-top: 25vh;
  margin-bottom: 20vh;
}

/* 여기서부터 버튼 스타일 변경 */
/* 커스텀 버튼 스타일 */
.custom-button {
  width: 20vw;
  height: 12vh;
  border-radius: 2vw;
  box-shadow: 0 0.5vw 1px #713528;
  background-color: #ffefca; /* 기본 배경색: 연한 베이지 */
  border: 0.25vw solid #713528; /* 테두리 색상: 갈색 */
  color: #333; /* 기본 텍스트 색상: 검은색 */
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

/* 호버 효과 */
.custom-button:hover {
  background-color: #713528; /* 호버 시 배경색: 갈색 */
  color: white; /* 호버 시 텍스트 색상: 흰색 */
}

.button-text {
  font-size: 2.5vw;
  font-weight: bold;
}
/* 여기까지 버튼 스타일 변경 */

.open-button-container {
  margin-top: 4vh;
  display: flex;
  justify-content: center;
  /* background-image: url('/tutorial/button/openbutton.png'); */
}

.open-button {
  width: 14.5vw;
  height: 9vh;
  background-image: url("/tutorial/button/openbutton.png");
  background-size: contain;
  background-repeat: no-repeat;

  border: none;
  border-radius: 1vw;
  cursor: pointer;
}
</style>
