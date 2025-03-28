<template>
  <div></div>
  <div class="game-container">
    <img id="secondimg" src="/images/tycoonname.png" alt="타이쿤 이름 이미지" />
    <div class="action-buttons">
      <button class="load-button" @click="loadGameData">불러오기</button>
      <button class="start-button" @click="startgame">게임시작</button>
    </div>

    <!-- <div class="ranking-board">


      <div class="ranking-header">
        <span></span>
        <span></span>
        <span></span>
      </div>
      <div class="ranking-list">
        <div 
          v-for="(player, index) in rankings" 
          :key="index" 
          :class="['ranking-item', getTopPlayerClass(index)]"
        >
          
          <span>{{ player.nickname }}</span>
          <span>{{ formatCurrency(player.profit) }}</span>
        </div>
      </div> 
    </div> -->
    <div class="ranking-board">
      <div class="ranking-title-rayout">
        <div class="ranking-title">
          <div class="ranking-info">
            <img src="/lanking/crown.png" class="crown-icon" />
            <span class="ranking-text">게임랭킹</span>
          </div>
        </div>
      </div>

      <div class="ranking-header">
        <div class="rank-icon">순위</div>
        <div class="nickname" style="margin-right: 0%">닉네임</div>
        <div class="profit" style="margin-right: 2%">수익</div>
      </div>
      <div class="ranking-list">
        <div v-for="(player, index) in rankings" :key="index" class="ranking-item">
          <!-- 트로피 추가 -->
          <div class="rank-icon">
            <img class="" v-if="index === 0" src="/lanking/gold-trophy.png" alt="Gold Trophy" />
            <img
              class=""
              v-else-if="index === 1"
              src="/lanking/silver-trophy.png"
              alt="Silver Trophy"
            />
            <img
              class=""
              v-else-if="index === 2"
              src="/lanking/bronze-trophy.png"
              alt="Bronze Trophy"
            />
            <span class="" v-else>{{ index + 1 }}</span>
          </div>
          <div class="nickname">{{ player.nickname }}</div>
          <div class="profit">{{ formatCurrency(player.profit) }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { revenueStore } from "@/assets/pinia/maingame"; // 나중에 사용해야지

// const modal={
//   'playNo':this.revenue.playNo,
//   'memberNo':0,
//   'userId':this.revenue.userId,
//   'cash':this.revenue.cash,
//   'loan':this.revenue.loan,
//   'playDay':this.revenue.salesDay,
//   'storageLevel':this.revenue.storagelevel,
//   'state':this.revenue.state,
// };

export default {
  name: "ConvenienceStoreTycoon",
  data() {
    return {
      revenue: revenueStore(),
      rankings: [],
      userData: {},
    };
  },
  methods: {
    loadGameData() {
      console.log("로드 함수 실행됨"); // 이게 실행되는지 확인!

      const loginUser = sessionStorage.getItem("loginUser");
      const userData = JSON.parse(loginUser);
      console.log("userId", userData.userId); // 이게 실행되는지 확인!

      fetch(__apiUrl__ + "/userdata/getUserData?userId=" + userData.userId)
        .then((response) => {
          console.log(response.status);
          if (!response.ok) {
            alert("불러오기가 실행되지 않습니다.");
          } else {
            return response.json(); // 응답 데이터 파싱
          }
        })
        .then((data) => {
          console.log("받아온 유저 데이터:", data);
          this.revenue.cash = data.cash;
          this.revenue.loan = data.loan;
          this.revenue.playNo = data.playNo;
          this.storageLevel = data.storageLevel;
          this.revenue.saveState(); // 로컬에다 revenue store있는걸 저장함
          sessionStorage.setItem("gameNo", data.playNo);
          return this.$router.push("/mainMenu");
        })
        .catch((error) => console.error("유저 데이터 불러오기 실패:", error));
    },

    formatCurrency(value) {
      if (value == null) return "0원";
      return Number(value).toLocaleString() + "원";
    },
    getTopPlayerClass(index) {
      return index < 3 ? `top-${index + 1}` : "";
    },
    startgame() {
      this.revenue.salesDay = 1; // N일차
      this.revenue.cash = 500000;
      this.revenue.loan = 1000000;
      this.revenue.storagelevel = 1;
      this.revenue.state = 0;
      console.log(this.revenue);
      console.log(JSON.parse(sessionStorage.getItem("loginUser")).userId);
      fetch(__apiUrl__ + "/maingame/newgame", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          playNo: 0,
          memberNo: JSON.parse(sessionStorage.getItem("loginUser")).memberNo,
          userId: JSON.parse(sessionStorage.getItem("loginUser")).userId,
          cash: this.revenue.cash,
          loan: this.revenue.loan,
          playDay: this.revenue.salesDay,
          storageLevel: this.revenue.storagelevel,
          state: this.revenue.state,
          reliability: this.revenue.reliability,
        }),
      })
        .then((response) => response.text())
        .then((data) => {
          console.log("gameNo:", data);
          sessionStorage.setItem("gameNo", data);
        })
        .catch((e) => console.error(e));

      this.revenue.saveState();
      return this.$router.push("/introstart");
    },
  },

  mounted() {
    fetch(__apiUrl__ + "/userdata/getRankings")
      .then((response) => response.json())
      .then((data) => {
        console.log("받아온 랭킹 데이터:", data); // 서버 응답 확인
        // 필드명이 `totalRevenue`라면 매핑해줌
        this.rankings = data.map((player) => ({
          nickname: player["nickname"] ?? "이름 없음",
          profit: player["totalRevenue"] ? Number(player["totalRevenue"]) : 0,
        }));

        console.log("업데이트된 rankings:", this.rankings); // Vue 상태 업데이트 확인
      })
      .catch((error) => console.error("랭킹 데이터 불러오기 실패:", error));

    // sk_랭킹 작업. 팀원과의 패키지 conflict를 막기 위해 userdata 패키지에서 진행했습니다.

    // fetch('http://localhost:3000/rankings')

    // for(let i =0;i<innerText.length;i++){
    //   setTimeout(()=>{
    //     this.inputText+=innerText[i];
    //   },i*50)
    // }
  },
};
</script>

<style scoped>
@font-face {
  font-family: "rk";
  src: url("/fonts/Recipekorea-FONT.ttf") format("truetype");
}
@font-face {
  font-family: "prebold";
  src: url("/fonts/Pretendard-bold.woff") format("woff");
}
.game-container {
  background-color: #f5f5f5;
  text-align: center;
  max-width: 100%;
  min-height: 100vh;
  background-image: url("/common/homeMenu.png");
  background-size: 100% 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
  font-size: 1.25em;
  /* font-family: rk; */
}

#secondimg {
  min-width: 500px;
  height: 15vh;
  position: absolute;
  top: 10vh;
}

.action-buttons {
  margin-top: 21vh;
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 30px;
}

.load-button,
.start-button {
  font-family: rk;
  height: 8vh;
  width: 180px;
  padding: 15px 25px 8px;
  border: none;
  border-radius: 1vw;
  font-size: 1em;
  cursor: pointer;
}

.load-button {
  background-color: #ffefca;
  border: 0.4vw solid #6f3533;
  color: #6f3533;
}
.load-button:hover {
  background-color: #6f3533;
  color: white;
}
.start-button {
  background-color: #ffefca;
  border: 0.4vw solid #56174f;
  color: #56174f;
}
.start-button:hover {
  background-color: #56174f;
  color: white;
}
.ranking-board {
  font-family: "rk";
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
  min-width: 550px;
  margin-top: 3vh;
}

.ranking-title {
  display: flex;
  width: 150px;
  align-items: center;
  justify-content: center;
  background-color: #6f3533;
  color: white;
  /* font-family: RecipekoreaOTF; */
  /* font-weight: bold; */
  padding: 10px;
  border-radius: 2vw 2vw 0 0;
  position: relative;
  gap: 10px; /* 아이콘과 텍스트 사이 간격 */
}

.crown-icon {
  width: 30px;
  height: auto;
  margin-right: 0.5vw;
  margin-bottom: -5px;
}

.ranking-text {
  font-size: 1.25rem;
}

.nickname {
  flex: 1.5;
  text-align: center;
}

.ranking-title-rayout {
  display: flex;
  justify-content: center;
}

.ranking-header {
  display: flex;
  align-items: center;
  background-color: #6f3533;
  border-radius: 30px 30px 0 0;
  color: white;
  padding: 20px 20px 5px;
  text-align: center;
}

.ranking-header span {
  flex: 1;
  text-align: center;
}

.ranking-list {
  text-align: center;
  border: 0.5vw solid #6f3533;
  max-height: 300px; /* 🔹 적절한 높이 설정 (6개 정도 표시) */
  overflow-y: auto; /* 🔹 수직 스크롤 활성화 */
  background-color: #6f3533;
}

.ranking-list::-webkit-scrollbar {
  width: 10px; /* 스크롤바 너비 */
}

.ranking-list::-webkit-scrollbar-thumb {
  background-color: #56174f; /* 스크롤바 색상 */
  border-radius: 0.5vh; /* 모서리 둥글게 */
}

.ranking-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  border-bottom: 1px solid #ddd;
  font-size: 18px;
}

.ranking-item:nth-child(2n + 1) {
  background-color: #ffefca;
}

.ranking-item:nth-child(2n) {
  background-color: #f0ddb9;
}

.rank-icon {
  display: flex;
  min-width: 55px;
}

.margin-left {
  margin-left: 25%;
}

.rank-icon span {
  width: 25px;
  height: 25px;
}

.rank-icon img {
  width: 25px;
  height: 25px;
  margin-right: 5px;
}

.profit {
  flex: 1; /* 기존과 동일한 비율 유지 */
  text-align: right; /* 수익을 중앙 정렬 */
}
</style>
