<template>
  <div class="main-container">
    <Topbar />

    <div class="navigation">
      <div class="back-button" @click="goBack">
        <img class="back-button" src="/common/Vector.png" alt="back" />
      </div>
      <div class="title">창고</div>
    </div>

    <div>
      <h5 class="storageCount">창고 개수 : {{ totalQuantity }}/{{ storageSize }}</h5>
    </div>

    <!-- Tab Menu -->
    <div class="tab-container">
      <div class="tab-menu">
        <div
          class="tab-item"
          :class="{ active: selectedTab === '신선식품' }"
          @click="selectTab('신선식품')"
        >
          신선식품
        </div>
        <div class="tab-divider"></div>
        <div
          class="tab-item"
          :class="{ active: selectedTab === '즉석식품' }"
          @click="selectTab('즉석식품')"
        >
          즉석식품
        </div>
        <div class="tab-divider"></div>
        <div
          class="tab-item"
          :class="{ active: selectedTab === '전자제품' }"
          @click="selectTab('전자제품')"
        >
          전자제품
        </div>
      </div>

      <!-- Inventory Content -->
      <div class="fruit-container" :style="containerStyle">
        <div class="fruit-row" v-for="(row, rowIndex) in filteredFruitRows" :key="rowIndex">
          <div class="fruit-item" v-for="(fruit, fruitIndex) in row" :key="fruitIndex">
            <img
              :src="fruit.image"
              :alt="fruit.goodsname"
              :id="fruit.goodsno"
              class="fruit-image"
              @click="disposePopup($event)"
            />
            <div class="fruit-quantity">x{{ fruit.orderquantity }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- Confirm Button -->
    <div class="button-container">
      <button class="confirm-button" @click="placeOrder">
        <span class="plus-icon">+</span>
        <span>확장하기</span>
      </button>
    </div>

    <div v-show="popup" class="popup-overlay" @click="closePopup">
      <div class="popup-content" @click.stop>
        <div class="popup-header">
          <p>{{ popupTitle }}</p>
          <span class="close-button" @click="closePopup">×</span>
        </div>

        <div v-if="storage && storageSize < 150" class="popup-body">
          <p>{{ storageSize }} >> {{ storageSize + 20 }}</p>
          <p>필요금액</p>
          <p>{{ 30000 + ((storageSize - 50) / 20) * 10000 }}</p>
          <button class="expansionButton" @click="expansionStorage">확장하기</button>
        </div>

        <div v-if="storage && storageSize >= 150" class="popup-body oneExplan">
          <p>창고 상한에 도달하였습니다.</p>
          <div style="text-align: right">
            <button class="expansionButton" @click="closePopup">확인</button>
          </div>
        </div>

        <div v-if="dispose" class="popup-body">
          <div style="display: flex; justify-content: space-around; align-items: center">
            <div v-html="disproduct"></div>
            <div>
              <h4>{{ disfruit.goodsname }}</h4>
              <h4>{{ 2000 }}원</h4>
            </div>
            <div class="quantity-control">
              <button class="decrease-button" @click="decreaseQuantity">−</button>
              <span class="quantity-display">{{ disquantity }}</span>
              <button class="increase-button" @click="increaseQuantity">+</button>
            </div>
          </div>
          <div style="display: flex; justify-content: space-around; align-items: center">
            <div></div>
            <div>
              <h5 style="color: #ff5353">물품의 20% 가격으로 판매</h5>
            </div>
            <div>
              <button class="disposeButton" :disabled="disquantity == 0" @click="disposeAction">
                폐기하기
              </button>
            </div>
          </div>
        </div>

        <div v-if="realdispose" class="popup-body oneExplan">
          <p>정말 폐기하시겠습니까?</p>
          <div style="text-align: right">
            <button class="disposeButton" @click="disposeNow">폐기</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Topbar from "../common/topbar.vue";
import { revenueStore } from "@/assets/pinia/maingame";

const model = {
  popup: false,
  popupTitle: "알림",
  money: 500000,
  storageSize: 50,
  storage: false,
  dispose: false,
  realdispose: false,
  disproduct: "",
  disquantity: 0,
  disfruit: {},
  disposeProfit: 0,
  fruits: [],
  itemsPerRow: 5,
  maxVisibleRows: 3,
  rowHeight: 150, // Reverted row height to original
  selectedTab: "신선식품",
  popupMessage: "",
  revenue: {},
  amount: 0,
  playday: 30,
  storagelevel: 0,
};

export default {
  name: "KoreanInventoryInterface",
  components: { Topbar },
  data() {
    return model;
  },
  computed: {
    totalQuantity() {
      // 모든 상품의 orderquantity 합계 계산
      return this.fruits.reduce((total, fruit) => total + fruit.orderquantity, 0);
    },
    fruitRows() {
      const rows = [];
      for (let i = 0; i < this.fruits.length; i += this.itemsPerRow) {
        rows.push(this.fruits.slice(i, i + this.itemsPerRow));
      }
      return rows;
    },
    filteredFruitRows() {
      // 선택된 탭과 goodstype이 일치하는 항목만 필터링
      const filteredFruits = this.fruits.filter((fruit) => fruit.goodstype === this.selectedTab);
      const rows = [];
      for (let i = 0; i < filteredFruits.length; i += this.itemsPerRow) {
        rows.push(filteredFruits.slice(i, i + this.itemsPerRow));
      }
      return rows;
    },
    containerStyle() {
      if (this.filteredFruitRows.length > this.maxVisibleRows) {
        return {
          height: `${this.maxVisibleRows * this.rowHeight}px`,
          overflowY: "scroll",
        };
      }
      return {};
    },
  },
  methods: {
    selectTab(tab) {
      this.selectedTab = tab;
    },
    goBack() {
      this.$router.push({
        name: "mainmenu",
        state: {
          disposeProfit: this.disposeProfit,
        },
      });
    },
    placeOrder() {
      this.storage = true;
      this.popupMessage =
        "" +
        this.storageSize +
        ">>" +
        this.storageSize +
        "필요금액:" +
        30000 +
        ((this.storageSize - 50) / 20) * 10000;
      this.popup = true;
    },
    closePopup() {
      this.storage = false;
      this.dispose = false;
      this.realdispose = false;
      this.popup = false;
    },
    disposePopup(e) {
      this.disquantity = 0;
      this.disproduct = e.target.parentElement.innerHTML;
      this.disfruit = this.fruits.find((f) => f.goodsno == e.target.id);
      console.log(this.disfruit);
      this.popup = true;
      this.dispose = true;
    },
    increaseQuantity() {
      if (this.disfruit.orderquantity > this.disquantity) {
        this.disquantity++;
      }
    },
    decreaseQuantity() {
      if (this.disquantity > 0) {
        this.disquantity--;
      }
    },
    disposeAction() {
      this.dispose = false;
      this.realdispose = true;
    },
    disposeNow() {
      // goodsprice가 없을 경우 기본 가격 설정
      const price = 2000; // 기본 가격 설정
      this.revenue.cash += price * this.disquantity;
      this.disfruit.orderquantity -= this.disquantity;
      this.disposeProfit += price * this.disquantity;
      this.revenue.disposePrice += price * this.disquantity;

      fetch(
        __apiUrl__ +
          "/maingame/expense?price=" +
          price * this.disquantity +
          "&gameNo=" +
          sessionStorage.getItem("gameNo")
      ).then((response) => console.log(response));

      this.revenue.saveState();

      if (this.disfruit.orderquantity == 0) {
        const index = this.fruits.findIndex((f) => this.disfruit.goodsno == f.goodsno);
        console.log(index);
        this.fruits.splice(index, 1);
      }
      this.realdispose = false;
      this.popup = false;
    },
    // expansionStorage 메소드 수정
    expansionStorage() {
      if (this.storageSize < 150) {
        const expansionSize = 20;
        const expansionCost = 30000 + ((this.storageSize - 50) / 20) * 10000;
        console.log("확장 레벨: " + this.storagelevel);
        console.log("사이즈: " + this.storageSize);
        // 서버에 창고 확장 요청
        fetch(__apiUrl__ + "/storage/expandStorage", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            storagelevel: this.storagelevel,
          }),
        })
          .then((response) => {
            if (!response.ok) {
              throw new Error("서버 응답 오류: " + response.status);
            }
            return response.json();
          })
          .then((data) => {
            console.log("창고 확장 성공:", data);

            // DB 업데이트 후 프론트엔드 상태 업데이트
            this.money -= expansionCost;
            this.storageSize += expansionSize;

            // 팝업 닫기
            this.storage = false;
            this.popup = false;

            // 성공 메시지 (필요시)
            this.popupTitle = "알림";
            this.popupMessage = "창고 확장이 완료되었습니다.";
          })
          .catch((error) => {
            console.error("창고 확장 중 오류 발생:", error);

            // 오류 메시지 표시
            this.popupTitle = "오류";
            this.popupMessage = "창고 확장 중 오류가 발생했습니다. 다시 시도해주세요.";
            this.popup = true;
          });
      }
    },
  },
  mounted() {
    if (history.state.popup != null) {
      console.log(history.state.popup);
      this.popup = history.state.popup;
      this.storage = history.state.popup;
    }

    if (history.state.disposeProfit != null && history.state.disposeProfit > 0) {
      this.disposeProfit = history.state.disposeProfit;
    }

    // 발주(창고부족) -> 창고로 넘어온 뒤 다시 자동으로 뜨지 않게 하기(실험실)
    history.replaceState(null, document.title, window.location.href);

    this.revenue = revenueStore();

    const gameNo = sessionStorage.getItem("gameNo");

    fetch(__apiUrl__ + "/storage/findStorageAll?gameNo=" + gameNo)
      .then((response) => response.json())
      .then((data) => {
        console.log("서버에서 받은 데이터:", data);
        // 서버에서 받은 데이터를 그대로 fruits에 저장
        this.fruits = data;
        // this.playday = this.playday - (data[0].playday-1);
        // this.storageSize = data[0].storagelevel;
        // this.amount = data[0].amount;
        console.log("적용된 데이터:", this.fruits);
      })
      .catch((error) => {
        console.error("상품 데이터 가져오기 오류:", error);
        this.popupMessage = "상품 데이터를 가져오는 중 오류가 발생했습니다.";
        this.popup = true;
      });

    // 그냥 돈만 가져와야지
    fetch(__apiUrl__ + "/maingame/moneydata?gameNo=" + gameNo)
      .then((response) => response.text())
      .then((data) => (this.revenue.cash = data));

    // 그냥 돈만 가져와야지
    fetch(__apiUrl__ + "/storage/gameInfo?gameNo=" + gameNo)
      .then((response) => response.json())
      .then((data) => {
        this.storageSize = data.storagelevel;
        this.storagelevel = data.storagecount;
      });
  },
  components: { Topbar },
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
.main-container {
  width: 100%;
  height: 100vh;
  position: relative;
  overflow: hidden;

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

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.6vw 1.7vw;
  margin-bottom: 0;
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

.navigation {
  font-family: rk;
  width: 90%;
  display: flex;
  align-items: center;
  padding: 8px 16px;
  margin-bottom: 0;
  margin-top: 4vh;
  /* Adjusted to account for fixed header */
  gap: 41vw;
}

.back-button {
  color: #5d2906;
  cursor: pointer;
  width: 2vw;
  height: 2vw;
}

.title {
  font-family: rk;
  font-size: 2vw;
  font-weight: bold;
  text-align: center;
}

.storageCount {
  font-family: rk;
  width: 50vw;
  height: 3vh;
  text-align: right;
  margin: 2vh 0;
  font-size: 1.5vw;
  font-weight: bold;
}

.double-arrow {
  font-family: rk;
  font-size: 16px;
}

.inventory-status-bar {
  font-family: rk;
  display: flex;
  justify-content: space-between;
  padding: 0 16px;
  margin-bottom: 16px;
}

.info-text {
  font-family: rk;
  font-size: 14px;
}

.capacity-text {
  font-family: rk;
  font-size: 14px;
  font-weight: bold;
}

.tab-container {
  font-family: rk;
  width: 50vw;
  height: 50vh;
  border: 8px solid #6f3533;
  border-radius: 30px;
  overflow: hidden;
  margin-bottom: 20px;
  background-color: #eae5de;
}

.tab-menu {
  display: flex;
  background-color: #fff3d4;
  border-bottom: 4px solid #6f3533;
}

.tab-item {
  height: 5vh;
  flex: 1;
  text-align: center;
  line-height: 5vh;
  padding: 1vw;
  cursor: pointer;
}

.tab-item.active {
  background-color: #713528;
  color: white;
}

.tab-divider {
  width: 1px;
  background-color: #8b4513;
}

.inventory-content {
  padding: 16px;
}

.info-row {
  background-color: #ffe8c8;
  padding: 12px 16px;
  margin-bottom: 12px;
  border-radius: 8px;
}

.items-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
}

.item {
  width: 80px;
  height: 100px;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  padding: 8px;
}

.discount-badge {
  position: absolute;
  top: -8px;
  left: -8px;
  background-color: red;
  color: white;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
}

.item-image {
  font-size: 28px;
  margin-bottom: 4px;
}

.item-name {
  font-size: 12px;
  margin-bottom: 4px;
}

.item-count {
  font-size: 12px;
}

.button-container {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}

.confirm-button {
  font-family: prebold;
  color: #6f3533;
  background-color: #fff3d4;
  border: 0.25vw solid #8b4513;
  border-radius: 16px;
  padding: 8px 24px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  cursor: pointer;
  margin-top: 0;
}

.plus-icon {
  font-weight: bold;
  font-size: 18px;
}

.fruit-container {
  border: none;
  border-radius: 10px;
  background-color: #f5f5dc;
  width: 100%;
  overflow-y: auto;
  max-height: 400px;
  flex-wrap: wrap;
}

.fruit-row {
  display: flex;
  justify-content: flex-start;
  margin-bottom: 15px;
  padding-left: 10px;
}

.fruit-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  width: 160px;
  cursor: pointer;
  margin: 5px 15px 5px 0;
}

.fruit-image {
  width: 100%;
  /* Reverted width */
  height: 100%;
  /* Reverted height */
}

.fruit-discount {
  position: absolute;
  bottom: 40px;
  left: 10px;
  background-color: #ff6b6b;
  color: white;
  border-radius: 50%;
  padding: 2px 5px;
  font-size: 12px;
}

.fruit-name {
  margin-top: 5px;
  font-size: 14px;
}

.fruit-quantity {
  font-size: 12px;
  margin-top: 3px;
}

/* Webkit 브라우저용 스크롤바 스타일링 */
.fruit-container::-webkit-scrollbar {
  width: 8px;
}

.fruit-container::-webkit-scrollbar-track {
  background: #f0f0f0;
}

.fruit-container::-webkit-scrollbar-thumb {
  background-color: #523629;
  border-radius: 10px;
  border: 2px solid #f0f0f0;
}

.fruit-container::-webkit-scrollbar-track-piece {
  background-color: #f5f5dc;
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
  width: 789px;
  height: 392px;
  border-radius: 30px;
  overflow: hidden;
}

.popup-header {
  background-color: #6a396c;
  padding: 15px;
  text-align: center;
  color: white;
  font-weight: bold;
  position: relative;
}

.popup-header p {
  margin: 0;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 15px;
  font-size: 28px;
  font-weight: bold;
  cursor: pointer;
  color: white;
}

.close-button:hover {
  color: #f0f0f0;
}

.popup-body {
  height: 332px;
  padding: 20px;
  text-align: center;
}

.storageCount {
  width: 50vw;
  height: 3vh;
  margin: 2vh 0;
  font-size: 1.5vw;
  font-weight: bold;
}

.quantity-control {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.decrease-button,
.increase-button {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  border: 1px solid #ccc;
  background-color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.quantity-display {
  width: 30px;
  text-align: center;
}

.disposeButton {
  width: 181px;
  height: 59px;
  background-color: rgba(0, 0, 0, 0);
  border: 0;
  background-image: url("/src/assets/element/increasestorage.png");
  background-size: 100% 100%;
}

.oneExplan {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}

.expansionButton {
  width: 9.5vw;
  height: 6vh;
  background-color: rgba(0, 0, 0, 0);
  border: 0;
  background-image: url("/src/assets/element/increasestorage.png");
  background-size: 100% 100%;
}

/* Webkit 브라우저용 스크롤바 스타일링 */
.fruit-container::-webkit-scrollbar {
  width: 12px; /* 스크롤바 너비 증가 */
}

.fruit-container::-webkit-scrollbar-track {
  background: #f5f5dc; /* 트랙 배경색 변경 */
  border-radius: 10px;
}

.fruit-container::-webkit-scrollbar-thumb {
  background-color: #8b4513; /* 테디베어 갈색으로 변경 */
  border-radius: 10px;
  border: 2px solid #f5f5dc;
  cursor: grab; /* 드래그 가능 커서 표시 */
}

.fruit-container::-webkit-scrollbar-thumb:hover {
  background-color: #a0522d; /* 호버 시 약간 밝은 갈색 */
}

.fruit-container::-webkit-scrollbar-thumb:active {
  background-color: #5d2906; /* 클릭 시 더 어두운 갈색 */
  cursor: grabbing; /* 드래그 중 커서 변경 */
}

/* Firefox를 위한 스크롤바 스타일링 */
.fruit-container {
  scrollbar-width: thin;
  scrollbar-color: #8b4513 #f5f5dc;
}

/* 스크롤바 영역 배경색 제거 */
.fruit-container::-webkit-scrollbar-track-piece {
  background-color: transparent;
}
</style>
