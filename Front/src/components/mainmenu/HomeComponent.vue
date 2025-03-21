<template>
  <div></div>
  <div class="game-container">  

    <img id="secondimg" src="/images/tycoonname.png" alt="타이쿤 이름 이미지">
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
            <img src="/lanking/crown.png" class="crown-icon">
            <span class="ranking-text">게임랭킹</span>
          </div>
        </div>
      </div>

      <div class="ranking-header">
        <span>순위</span>
        <span>닉네임</span>
        <span>수익</span>
      </div>
      <div class="ranking-list">
        <div 
          v-for="(player, index) in rankings" 
          :key="index" 
          class="ranking-item"
        >
          <!-- 트로피 추가 -->
          <div class="rank-icon">
            <img v-if="index === 0" src="/lanking/gold-trophy.png" alt="Gold Trophy">
            <img v-else-if="index === 1" src="/lanking/silver-trophy.png" alt="Silver Trophy">
            <img v-else-if="index === 2" src="/lanking/bronze-trophy.png" alt="Bronze Trophy">
            <span v-else>{{ index + 1 }}</span>
          </div>
          <div class="nickname">{{ player.nickname }}</div>
          <div class="profit">{{ formatCurrency(player.profit) }}</div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import { revenueStore } from '@/assets/pinia/maingame';   // 나중에 사용해야지


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
  name: 'ConvenienceStoreTycoon',
  data() {
    return {
      revenue:revenueStore(),
      rankings: [
        // { nickname: '도토리뚜껑', profit: 1000000 },
        // { nickname: '멋지당', profit: 980000 },
        // { nickname: '방구벨트', profit: 970000 },
        // { nickname: '소잉냥', profit: 970000 },
        // { nickname: '보리', profit: 970000 },
        // { nickname: '지냥', profit: 970000 }
      ],
      userData:{}
    }
  },
  methods: {



    loadGameData() {
      console.log("로드 함수 실행됨"); // 이게 실행되는지 확인!

      // userId는 차후 세션에 id 받아서 진행할 예정입니다!!!!!!!!!!!!!!!!
      const loginUser = sessionStorage.getItem('loginUser');
      const userData = JSON.parse(loginUser);

      fetch('http://3.38.185.252:8080/spring/userdata/getUserData?userId='+userData.userId)
        .then(response => response.json())
        .then(data => {
          console.log("받아온 유저 데이터:", data);
          this.revenue.cash=data.cash;
          this.revenue.loan=data.loan;
          this.revenue.playNo=data.playNo;
          // this.
          this.storageLevel=data.storageLevel;
          // this.userData = data;

          this.revenue.saveState(); // 로컬에다 revenue store있는걸 저장함
          return this.$router.push('/mainMenu');

        })
        .catch(error => console.error("유저 데이터 불러오기 실패:", error));
    },






    formatCurrency(value) {
      if (value == null) return "0원";
      return Number(value).toLocaleString() + '원';
    },
    getTopPlayerClass(index) {
      return index < 3 ? `top-${index + 1}` : ''
    }
    ,startgame(){
      this.revenue.salesDay=1;         // N일차
      this.revenue.cash=500000;
      this.revenue.loan=1000000;
      this.revenue.storagelevel=1;
      this.revenue.state=0;
      console.log(this.revenue);
      console.log(JSON.parse(sessionStorage.getItem('loginUser')).userId);
      fetch("http://3.38.185.252:8080/spring/maingame/newgame",{
        method:'POST',
        headers:{
          'Content-Type':'application/json'
        },
        body:JSON.stringify({
          'playNo':0,
          'memberNo':0,
          'userId':JSON.parse(sessionStorage.getItem('loginUser')).userId,
          'cash':this.revenue.cash,
          'loan':this.revenue.loan,
          'playDay':this.revenue.salesDay,
          'storageLevel':this.revenue.storagelevel,
          'state':this.revenue.state,
          'reliability':this.revenue.reliability,
        })
      }).then(response=>response.text())
      .then(data=>sessionStorage.setItem("gameNo",data))
      .catch(e=>console.error(e))
      
      this.revenue.saveState();
      return this.$router.push('/introstart');
    }
  },


  mounted(){

    fetch('http://3.38.185.252:8080/spring/userdata/getRankings')
        .then(response => response.json())
        .then(data => {
            console.log("받아온 랭킹 데이터:", data); // 서버 응답 확인
             // 필드명이 `totalRevenue`라면 매핑해줌
            this.rankings = data.map(player => ({
              nickname: player["nickname"] ?? "이름 없음",
              profit: player["totalRevenue"] ? Number(player["totalRevenue"]) : 0

        }));

            console.log("업데이트된 rankings:", this.rankings); // Vue 상태 업데이트 확인
        })
        .catch(error => console.error("랭킹 데이터 불러오기 실패:", error));
    
      // sk_랭킹 작업. 팀원과의 패키지 conflict를 막기 위해 userdata 패키지에서 진행했습니다. 

    
      // fetch('http://localhost:3000/rankings')
      
      // for(let i =0;i<innerText.length;i++){
      //   setTimeout(()=>{
      //     this.inputText+=innerText[i];
      //   },i*50)
      // }
  },
  
}
</script>

<style scoped>
@font-face {
  font-family: 'rk';
  src: url('/fonts/Recipekorea-FONT.ttf') format('truetype');
}
@font-face {
  font-family: 'prebold';
  src: url('/fonts/Pretendard-bold.woff') format('woff');
}
.game-container {
  font-size: 2vw;
  background-color: #f5f5f5;
  text-align: center;
  max-width: 100%;
  min-height: 100vh;
  background-image: url('/common/homeMenu.png');
  background-size: 100% 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
}

#secondimg {
  width: 32vw;
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
  width: 12vw;
  padding: 15px 25px;
  border: none;
  border-radius: 1vw;
  font-size: 1.3vw;
}

.load-button {
  background-color: #FFEFCA;
  border: 0.4vw solid #6F3533;
  color: #6F3533;
}

.start-button {
  background-color: #56174F;
  color: white;
}

.ranking-board {
  font-family: 'rk';
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  text-align: center; 
  width: 46vw;
  margin-top: 3vh;
  padding: 20px;
}

.ranking-title {
  display: flex;
  justify-content: center; /* 중앙 정렬 */
  align-items: center;
  background-color: #6F3533;
  color: white;
  font-family: RecipekoreaOTF;
  font-size: 1.8vw;
  font-weight: bold;
  padding: 15px;
  border-radius: 2vw 2vw 0 0;
  position: relative;
  gap: 10px; /* 아이콘과 텍스트 사이 간격 */
}

.crown-icon {
  width: 2.5vw;
  height: auto;
}

.ranking-text {
  font-size: 1.5vw;
}

.nickname {
  flex: 2;
  text-align: center;
}

.ranking-title-rayout{
  display: flex;
  justify-content: center;
}

.ranking-title {
  display: flex;
  width: 10vw;
  align-items: center;
  justify-content: center;
  background-color: #6F3533;
  color: white;
  font-family: RecipekoreaOTF;
  font-size: 1.5vw;
  font-weight: bold;
  padding: 10px;
  border-radius: 2vw 2vw 0 0;
  position: relative;
}

/* 왕관 아이콘 스타일 */
.crown-icon {
  width: 2vw;
  height: auto;
  margin-right: 0.5vw;
}

.ranking-header {
  display: flex;
  justify-content: space-between;
  background-color: #6F3533;
  border-radius: 3vw 3vw 0 0;
  color: white;
  padding: 10px;
  font-weight: bold;
  text-align: center;
}

.ranking-header span {
  flex: 1;
  text-align: center;
}

.ranking-list {
  text-align: center;
  border: 0.5vw solid #6F3533;
  border-radius: 0 0 3vw 3vw;
  /* 🔥 스크롤바 추가 */
  max-height: 300px; /* 🔹 적절한 높이 설정 (6개 정도 표시) */
  overflow-y: auto; /* 🔹 수직 스크롤 활성화 */
}

.ranking-list::-webkit-scrollbar {
  width: 10px; /* 스크롤바 너비 */
}

.ranking-list::-webkit-scrollbar-thumb {
  background-color: #56174F; /* 스크롤바 색상 */
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

.ranking-item:nth-child(2n+1){
  background-color: #FFEFCA;
}

.ranking-item:nth-child(2n){
  background-color: #F0DDB9;
}

.rank-icon{
  display: flex;
  
}

.rank-icon img {
  width: 25px;
  height: 25px;
  margin-right: 5px;
}

.profit {
  flex: 1;  /* 기존과 동일한 비율 유지 */
  text-align: center; /* 수익을 중앙 정렬 */
}
</style>