<template>
  <div class="bank-container">
    <div class="background">
      <div class="finalcal">
        <div class="calcpage">
          <div class="calculation-title">
            <p style="color: white; font-size: 24px; margin: 0.8vh 0">최종 정산서</p>
          </div>
          <div class="calcul-block">
            <div>
              <div class="block-left"><h3>전체 잔고</h3></div>
              <div class="block-right">
                <p>{{ finalcash }}원</p>
              </div>
            </div>
            <div>
              <div class="block-left"><h3>폐기 수익</h3></div>
              <div class="block-right">
                <p>{{ finaldispose }}원</p>
              </div>
            </div>
          </div>
          <div class="calcul-block">
            <div>
              <div class="block-left"><h3>대출금</h3></div>
              <div class="block-right"><p>1000000원</p></div>
            </div>
            <div>
              <div class="block-left"><h3>중간 대출금</h3></div>
              <div class="block-right">
                <p>{{ finalloan - 1000000 }}원</p>
              </div>
            </div>
          </div>
          <div style="width: 37.5vw; border-top: 0.3vh dashed #4c1b0b"></div>
          <div style="width: 37.5vw; height: 3vh">
            <div>
              <div class="block-left"><h3>최종 잔고</h3></div>
              <div class="block-right">
                <h3>{{ finalcash + finaldispose - finalloan }}원</h3>
              </div>
            </div>
          </div>
        </div>
        <div class="ranking">
          <div class="ranking-box">
            <p class="box">게임랭킹</p>
          </div>
          <div class="ranking-board">
            <div class="ranking-header">
              <span>순위</span>
              <span>닉네임</span>
              <span>수익</span>
            </div>
            <div class="ranking-list">
              <div v-for="(player, index) in rankings" :key="index" class="ranking-item">
                <!-- 트로피 추가 -->
                <div class="rank-icon">
                  <img v-if="index === 0" src="/lanking/gold-trophy.png" alt="Gold Trophy" />
                  <img
                    v-else-if="index === 1"
                    src="/lanking/silver-trophy.png"
                    alt="Silver Trophy"
                  />
                  <img
                    v-else-if="index === 2"
                    src="/lanking/bronze-trophy.png"
                    alt="Bronze Trophy"
                  />
                  <span v-else>{{ index + 1 }}</span>
                </div>
                <div class="nickname">{{ player.nickname }}</div>
                <div class="profit">{{ formatCurrency(player.profit) }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="finalquiz">
        <div class="quizpage">
          <div class="quiztitle">
            <h3>퀴즈 결과</h3>
          </div>
          <div class="quizresult">
            <div class="quiz-block">
              <div class="block-left"><h3>맞춘 개수</h3></div>
              <div class="block-right">
                <p>{{ finalquizresult }}개</p>
              </div>
            </div>
            <div class="quiz-block">
              <div class="block-left"><h3>틀린 개수</h3></div>
              <div class="block-right">
                <p>{{ finaldays - finalquizresult }}개</p>
              </div>
            </div>
          </div>
        </div>
        <div class="ending">
          <button class="endingbtn">엔딩 보기</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { revenueStore } from "@/assets/pinia/maingame";

export default {
  data() {
    return {
      rankings: [],
      userData: {},
      revenue: revenueStore(),
      finalcash: 0,
      finaldispose: 0,
      finalloan: 1000000,
      finalquizresult: 0,
      finaldays: 0,
    };
  },
  methods: {
    formatCurrency(value) {
      if (value == null) return "0원";
      return Number(value).toLocaleString() + "원";
    },
  },
  mounted() {
    fetch(__apiUrl__ + "/spring/userdata/getRankings")
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

    fetch(__apiUrl__ + "/spring/maingame/gamefinal?gameNo=" + sessionStorage.getItem("playNo")) // this.revenue.playNo는 나중에 사용하겠음. 일단 테스트용으로 playNo만 넣어둠. 무조건 바꿔야 함.
      .then((response) => response.json())
      .then((data) => {
        this.finalcash = data.cashloan.cash;
        this.finalloan = data.cashloan.loan;
        this.finaldays = data.cashloan.playDay;
        data.quizdisposal.forEach((d) => {
          if (d.qeezeYN == "Y") {
            this.finalquizresult++;
          }
          this.finaldispose += d.disposePrice;
        });
      })
      .catch((error) => console.error("최종 정산서 불러오기 실패:", error));
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
.bank-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image: url("/background/bankbg.png");
  background-size: 100% 100%;
  position: relative;
}
.background {
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-evenly;
  background-color: rgba(0, 0, 0, 0.5);
  position: relative;
}
.finalcal {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  width: 94vw;
  height: 55vh;
}
.finalquiz {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  width: 94vw;
  height: 19vh;
}
.calcpage {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  width: 47vw;
  height: 55vh;
  background-color: #f5f5f5;
  border-radius: 20px;
}
.calculation-title {
  font-family: rk;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 18vw;
  height: 5.5vh;
  background-image: url("/element/Ribbon.png");
  background-size: 100% 100%;
}
.calcul-block {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  width: 37.5vw;
  height: 14.5vh;
  background-color: rgba(140, 64, 41, 0.1);
  border-radius: 20px;
}
.ranking {
  width: 41.5vw;
  height: 55vh;
}
.ranking-box {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 41.5vw;
  height: 4.5vh;
}
.ranking-box > p {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 9.5vw;
  height: 4.5vh;
  background-color: #6f3533;
  color: white;
  border-top-left-radius: 2vh;
  border-top-right-radius: 2vh;
}
.ranking-board {
  font-family: rk;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 40.5vw;
  min-height: 48.5vh;
  background-color: #ffefca;
  border-radius: 4vw;
  border: 0.5vw solid #6f3533;
}
.rank-icon {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}
.rank-icon img {
  width: 32px;
  height: 32px;
}
.nickname {
  flex: 2;
  text-align: center;
}
.ranking-header {
  display: flex;
  justify-content: space-between;
  background-color: #6f3533;
  border-radius: 3vw 3vw 0 0;
  color: white;
  padding: 10px;
  font-weight: bold;
  text-align: center;
}
.ranking-header span {
  font-family: rk;
  flex: 1;
  text-align: center;
}
.ranking-list {
  text-align: center;
  /* border: 0.5vw solid #6F3533; */
  /* border-radius: 0 0 3vw 3vw; */
  /* 🔥 스크롤바 추가 */
  max-height: 300px; /* 🔹 적절한 높이 설정 (6개 정도 표시) */
  overflow-y: auto; /* 🔹 수직 스크롤 활성화 */
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
.rank-icon img {
  width: 25px;
  height: 25px;
  margin-right: 5px;
}
.profit {
  flex: 1; /* 기존과 동일한 비율 유지 */
  text-align: center; /* 수익을 중앙 정렬 */
}
.quizpage {
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  align-items: center;
  width: 75.5vw;
  height: 19vh;
  background-color: #f5f5f5;
  border-radius: 20px;
}
.quiztitle {
  width: 70vw;
}
.quizresult {
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 75.5vw;
}
.quiz-block {
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 32vw;
  height: 5.5vh;
  background-color: rgba(140, 64, 41, 0.1);
  border-radius: 20px;
}
.ending {
  width: 14.5vw;
  height: 9vh;
}
.endingbtn {
  font-family: rk;
  width: 14.5vw;
  height: 9vh;
  background-color: #ffefca;
  border: 0.6vh solid #6f3533;
  border-radius: 2vh;
  font-size: 2vh;
}
.background p,
h3 {
  margin: 0 1vw;
}
.block-left {
  display: inline-block;
  width: 50%;
  height: 2vh;
  text-align: left;
}
.block-right {
  display: inline-block;
  width: 50%;
  height: 2vh;
  text-align: right;
}
.box {
  font-family: rk;
}
</style>
