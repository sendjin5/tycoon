<template>
  <div class="main-container">
    <div class="date-box">
      <div class="date-text">DAY-{{ gameDay }} NEWS</div>
    </div>
    <div class="dialog-box">
      <div class="dialog-text" v-html="inputText"></div>
    </div>
    <div class="arrowbox">
      <div class="dialog-arrow" @click="movePage">▼</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'poorending',
  data() {
    return {
      inputText: '',
      userId: sessionStorage.getItem("loginUser") ? JSON.parse(sessionStorage.getItem("loginUser")).userId : "",
      dialInterval: '',
      dialog: '',
      // 기본 텍스트 배열 (예시로 3개의 텍스트)
      dialex: [],
      newsdialnum: 0,
      dialpoint: 0,
      gameDay: null,
    };
  },
  async mounted() {
    try {
      // ✅ 백엔드에서 뉴스 데이터 가져오기
      const response = await fetch(`http://3.38.185.252:8080/spring/maingame/news?userId=${this.userId}`);
      if (!response.ok) throw new Error("뉴스 데이터를 불러오는데 실패했습니다.");
      const data = await response.json();

      this.gameDay=data;

      // ✅ 게임 회차에 맞는 텍스트 배열을 설정
      this.setNewsText(data);

      // ✅ 타이핑 애니메이션 적용
      this.animateText(this.dialex[this.newsdialnum]); // 첫 번째 텍스트부터 애니메이션
    } catch (error) {
      console.error("에러 발생:", error);
    }
  },
  methods: {
    movePage() {
    // dialpoint를 증가시켜서 다음 텍스트를 보여줌
    if (this.dialpoint < this.dialex.length - 1) {
      this.dialpoint++;
      this.animateText(this.dialex[this.dialpoint]);
    } else {
      // 모든 텍스트를 다 표시한 후 추가로 할 작업 (예: 게임 진행 등)
      console.log("모든 뉴스 텍스트가 출력되었습니다.");
    }
  },
    // switch 문을 이용하여 게임 회차에 따라 dialex 배열 업데이트
    setNewsText(data) {
      switch (data) {
        case 1:
          this.dialex = [
            '최근 딸기 가격이 급등하면서 품귀 현상이 일어나고 있습니다.',
            '불규칙한 기온으로 인해 딸기의 생산량이 부족하여 공급이 제한되고 있습니다.',
            '소비자들은 가격이 더 오를 것을 우려해 대량 구매에 나서고 있다고 전해졌습니다.',
            '전문가들은 "현재 가격이 가장 저렴한 가격대일 수 있다" 라고 경고를 하며 소비자들에게 딸기를 미리 구매할 기회를 놓치지 말라고 조언을 하고 있습니다.',
            '이상으로 오늘의 뉴스 마치겠습니다.'
          ];
          break;
        case 2:
          this.dialex = [
            '최근 소고기 가격이 상승하면서 국내 공급이 부족하다고 합니다.',
            '소비자들은 수입산 소고기를 찾고 있지만 운송비와 물류비 상승으로 인해 수입의 가격도 오르고 있습니다.',
            '소비자들은 가격이 오르기 전에 소고기를 많이 사야겠다라는 반응을 보이고 있으며, 소고기의 대량 구매가 활발히 이루어지고 있습니다.',
            '이상으로 오늘의 뉴스 마치겠습니다.'
          ];
          break;
        case 3:
          this.dialex = [
            '세 번째 게임 회차의 뉴스입니다.',
            '당신의 선택에 따라 결과가 달라집니다.',
            '세 번째 회차에서는 더 어려운 결정들이 기다리고 있습니다.',
            '이상으로 오늘의 뉴스 마치겠습니다.'
          ];
          break;
          case 4:
          this.dialex = [
            '조류인푸루엔자의 확산으로 계란 생산량이 급감하면서 가격이 급등 하였습니다.',
            '주요 산란계 농장에서 감염 사례가 발생하여 닭의 사육 규모가 줄어들고 공급이 원활하지 않은 상황입니다.',
            '이에 따라 일부 대형 마트에서는 계란이 품절되는 현상도 나타나고 있으며 “앞으로도 가격이 더 오를 가능성이 있다”는 전망이 나타나고 있습니다.',
            '이상으로 오늘의 뉴스 마치겠습니다.'
          ];
          break;
          case 5:
          this.dialex = [
            '기후 변화로 인한 해수 온도 상승으로 새우의 어획량이 크게 줄어들어 가격이 급등하고 있습니다.',
            '어획지의 수온이 상승하며 새우의 서식 환경이 변화해 공급향이 예년보다 25%이상 감소한 것으로 조사됐습니다.',
            '전문가들은 기후 변화로 인해 당분간 어획량 회복이 쉽지 않아 가격 상승이 지속될 것 이라고 분석하고 있습니다.',
            '이상으로 오늘의 뉴스 마치겠습니다.'
          ];
          break;
          case 6:
          this.dialex = [
            '속보입니다. 현재 연어 수출량 제한으로 국내 공급이 부족하다고 합니다.',
            '주요 수출국인 노르웨이에서 수출량을 제한하여 국내 공급이 줄어들고 있다고 합니다.',
            '전문가들은 연어 가격이 현재보다 더 오를 것이라고 전망하고 있으며 이에 따라 소비자들은 미리 대량 구매를 시작했다고 합니다.',
            '이상으로 오늘의 뉴스 마치겠습니다.'
          ];
          break;
        default:
          this.dialex = [
            '기본 뉴스입니다. 게임을 진행하세요.',
            '무언가 중요한 결정을 내릴 차례입니다.',
            '당신의 미래를 결정할 중요한 순간입니다.',
            '이상으로 오늘의 뉴스 마치겠습니다.'
          ];
          break;
      }
    },
    animateText(text) {
      this.inputText = '';
      for (let i = 0; i < text.length; i++) {
        setTimeout(() => {
          if (text[i] === '\n') {
            this.inputText += '<br>';
          } else {
            this.inputText += text[i];
          }
        }, i * 50);
      }
    },
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

    background-image: url('/common/news.png');
    background-size: 100% 100%;
}
.dialog-box {
  position: absolute;
  left: 5%;
  bottom: 12%;
  background-color: #F9F8F2;
  height: 18%;
  width: 80%;
}
.dialog-text {
  font-family: prebold;
  position: absolute;
  left: 8%;
  bottom: 13%;
  font-size: 2vw;
  font-weight: bold;
}
.dialog-arrow{
  position: absolute;
  bottom: 15.7%;
  right: 6.2%;
  font-size: 2.4vw;
}
.arrowbox {
  position: absolute;
  bottom: 14%;
  right: 6.2%;
  width: 10%;
  height: 10%;
  background-color: #F9F8F2;
  cursor: pointer;
}
.date-box {
  position: absolute;
  top:10%;
  left: 4%;
  height: 10%;
  width: 30%;
  font-size: 2.7vw;
  font-weight: bold;
  background-color: #FFEFCA;
  color:#6F3533;
}
</style>