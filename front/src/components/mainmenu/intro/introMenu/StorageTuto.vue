<template>
  <div class="blind" @click="nextPage"></div>
  <div class="main-container">
    <!-- Header -->
    <div class="header">
      <div class="left-section">D-30</div>
      <div class="right-section">
        <div class="money-bag">
          <span class="bag-icon">💰</span>
          <span class="amount">500,00원</span>
        </div>
        <span class="settings-icon">⚙️</span>
        
      </div>
    </div>
    
    <!-- Navigation -->
    <div class="navigation">
      <div class="back-button" @click="goBack">
        <img class="back-button" src="/common/Vector.png" alt="back" />
      </div>
      <div class="title">창고</div>
      <img src="/tutorial/button/skip.png" width="35vw">
    </div>

    <div>
      <h5 class="storageCount">창고 개수 : 110/150</h5>
    </div>

    <!-- Tab Menu -->
    <div class="tab-container">
      <div class="tab-menu">
        <div class="tab-item" :class="{ active: selectedTab === '신선식품' }" @click="selectTab('신선식품')">신선식품</div>
        <div class="tab-divider"></div>
        <div class="tab-item" :class="{ active: selectedTab === '즉석식품' }" @click="selectTab('즉석식품')">즉석식품</div>
        <div class="tab-divider"></div>
        <div class="tab-item" :class="{ active: selectedTab === '전자제품' }" @click="selectTab('전자제품')">전자제품</div>
      </div>

      <!-- Inventory Content -->
      <div class="fruit-container" :style="containerStyle">
        <div class="fruit-row" v-for="(row, rowIndex) in filteredFruitRows" :key="rowIndex">
          <div class="fruit-item" v-for="(fruit, fruitIndex) in row" :key="fruitIndex">
            <img :src="fruit.image" :alt="fruit.name" :id="fruit.id" class="fruit-image" @click="disposePopup($event)">
            <div class="fruit-discount" v-if="fruit.discount">{{ fruit.discount }}</div>
            <div class="fruit-quantity">x{{ fruit.quantity }}</div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Confirm Button -->
    <div class="button-container">
      <button class="confirm-button" @click="placeOrder">
        <span class="plus-icon">+</span>
        <span @click="">확장하기</span>
      </button>
    </div>


    <div v-show="popup" class="popup-overlay" @click="closePopup">
      <div class="popup-content" @click.stop>
        <div class="popup-header">
          <p>{{ popupTitle }}</p>
        </div>
        <div v-if="storage" class="popup-body">
          <p>50 >> 70</p>
          <p>필요금액</p>
          <p>30,000</p>
        </div>
        <div v-if="dispose" class="popup-body">
          <div style="display:flex;justify-content:space-around;align-items:center;">
            <div>
              <div class="fruit-item">
                <img :src="fruits[0].image" :alt="fruits[0].name" :id="fruits[0].id" class="fruit-image">
                <div class="fruit-discount" v-if="fruits[0].discount">{{ fruits[0].discount }}</div>
                <div class="fruit-quantity">x{{ fruits[0].quantity }}</div>
              </div>
            </div>
            <div>
              <h4>딸기</h4>
              <h4>800원</h4>
            </div>
            <div class="quantity-control">
              <button class="decrease-button" @click="decreaseQuantity">−</button>
              <span class="quantity-display">1</span>
              <button class="increase-button" @click="increaseQuantity">+</button>
            </div>
          </div>
          <div style="display:flex;justify-content:space-around;align-items:center;">
            <div></div>
            <div><h5 style="color:#FF5353;">물품의 20% 가격으로 판매</h5></div>
            <div><button class="disposeButton">폐기하기</button></div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div v-show="tutoPage.pagenum==0" class="tuto win1">
    <p>발주된 상품은 다음과 같이</p>
    <p>창고에 표시 됩니다.</p>
  </div>
  <div v-show="tutoPage.pagenum==0" class="tuto win2">
    <p>1. 유통기한 하루 전(D-1)</p>
    <p>상품 가격이 50% 할인됩니다.</p>
  </div>
  <div v-show="tutoPage.pagenum==0" class="tuto win3">
    <p>2. 유통기한 임박 상품은 모두 판매되지 않으면</p>
    <p>다음 날 자동 폐기됩니다.</p>
  </div>
  <div v-show="tutoPage.pagenum==0" class="tuto win4">
    <p>3. 자동 폐기를 대비해 상품을 20%로</p>
    <p>조기 판매할 수 있습니다.</p>
  </div>
  <div v-show="tutoPage.pagenum==1" class="tuto win5">
    <p>유통기한 임박 상품을 클릭하면</p>
    <p>조기 판매할 수 있습니다.</p>
  </div>
  <div v-show="tutoPage.pagenum==2" class="tuto win6">
    <p>창고가 부족할 경우</p>
    <p>일정 금액을 지불하여 확장할 수 있습니다.</p>
  </div>
</template>

<script>
import { curTutoPage } from '@/assets/pinia/tutorial';

const model = {
  tutoPage:{},
  image: '/common/fruit/strawberry.png',
  popup:false,
  popupTitle:'알림',
  storage:false,
  dispose:false,
  disproduct:'',
  disquantity:0,
  disfruit:{},
  fruits: [
    { id:1, name: '딸기', image: 'src/assets/common/fruit/strawberry.png', quantity: 1, discount: '50%', category: '신선식품' },
    { id:2, name: '파인애플', image: 'src/assets/common/fruit/fineapple_s.png', quantity: 3, discount: '50%', category: '신선식품' },
    { id:3, name: '사과', image: 'src/assets/common/fruit/apple.png', quantity: 1, discount: null, category: '신선식품' },
    { id:4, name: '라면', image: 'src/assets/common/food/ramen.png', quantity: 5, discount: '20%', category: '즉석식품' },
    { id:5, name: '햄버거', image: 'src/assets/common/food/burger.png', quantity: 2, discount: null, category: '즉석식품' },
    { id:6, name: '노트북', image: 'src/assets/common/electronics/laptop.png', quantity: 1, discount: '10%', category: '전자제품' },
    { id:7, name: '스마트폰', image: 'src/assets/common/electronics/phone.png', quantity: 1, discount: null, category: '전자제품' },
    { id:8, name: '딸기', image: 'src/assets/common/fruit/strawberry.png', quantity: 1, discount: '50%', category: '신선식품' },
    { id:9, name: '파인애플', image: 'src/assets/common/fruit/fineapple_s.png', quantity: 3, discount: '50%', category: '신선식품' },
    { id:10, name: '사과', image: 'src/assets/common/fruit/apple.png', quantity: 1, discount: null, category: '신선식품' },
    { id:11, name: '라면', image: 'src/assets/common/food/ramen.png', quantity: 5, discount: '20%', category: '즉석식품' },
    { id:12, name: '햄버거', image: 'src/assets/common/food/burger.png', quantity: 2, discount: null, category: '즉석식품' },
    { id:13, name: '노트북', image: 'src/assets/common/electronics/laptop.png', quantity: 1, discount: '10%', category: '전자제품' },
    { id:14, name: '스마트폰', image: 'src/assets/common/electronics/phone.png', quantity: 1, discount: null, category: '전자제품' },
    // Add more items as needed
    { id: 15, name: '딸기', image: 'src/assets/common/fruit/strawberry.png', quantity: 1, discount: '50%', category: '신선식품' },
    { id: 16, name: '파인애플', image: 'src/assets/common/fruit/fineapple_s.png', quantity: 3, discount: '50%', category: '신선식품' },
    { id: 17, name: '사과', image: 'src/assets/common/fruit/apple.png', quantity: 1, discount: null, category: '신선식품' },
    { id: 18, name: '라면', image: 'src/assets/common/food/ramen.png', quantity: 5, discount: '20%', category: '즉석식품' },
    { id: 19, name: '햄버거', image: 'src/assets/common/food/burger.png', quantity: 2, discount: null, category: '즉석식품' },
    { id: 20, name: '노트북', image: 'src/assets/common/electronics/laptop.png', quantity: 1, discount: '10%', category: '전자제품' },
    { id: 21, name: '스마트폰', image: 'src/assets/common/electronics/phone.png', quantity: 1, discount: null, category: '전자제품' },
    // Add more items as needed
    { id: 22, name: '딸기', image: 'src/assets/common/fruit/strawberry.png', quantity: 1, discount: '50%', category: '신선식품' },
    { id: 23, name: '파인애플', image: 'src/assets/common/fruit/fineapple_s.png', quantity: 3, discount: '50%', category: '신선식품' },
    { id: 24, name: '사과', image: 'src/assets/common/fruit/apple.png', quantity: 1, discount: null, category: '신선식품' },
    { id: 25, name: '라면', image: 'src/assets/common/food/ramen.png', quantity: 5, discount: '20%', category: '즉석식품' },
    { id: 26, name: '햄버거', image: 'src/assets/common/food/burger.png', quantity: 2, discount: null, category: '즉석식품' },
    { id: 27, name: '노트북', image: 'src/assets/common/electronics/laptop.png', quantity: 1, discount: '10%', category: '전자제품' },
    { id: 28, name: '스마트폰', image: 'src/assets/common/electronics/phone.png', quantity: 1, discount: null, category: '전자제품' },
    // Add more items as needed
    { id: 29, name: '딸기', image: 'src/assets/common/fruit/strawberry.png', quantity: 1, discount: '50%', category: '신선식품' },
    { id: 30, name: '파인애플', image: 'src/assets/common/fruit/fineapple_s.png', quantity: 3, discount: '50%', category: '신선식품' },
    { id: 31, name: '사과', image: 'src/assets/common/fruit/apple.png', quantity: 1, discount: null, category: '신선식품' },
    { id: 32, name: '라면', image: 'src/assets/common/food/ramen.png', quantity: 5, discount: '20%', category: '즉석식품' },
    { id: 33, name: '햄버거', image: 'src/assets/common/food/burger.png', quantity: 2, discount: null, category: '즉석식품' },
    { id: 34, name: '노트북', image: 'src/assets/common/electronics/laptop.png', quantity: 1, discount: '10%', category: '전자제품' },
    { id: 35, name: '스마트폰', image: 'src/assets/common/electronics/phone.png', quantity: 1, discount: null, category: '전자제품' },
    // Add more items as needed
    { id: 36, name: '딸기', image: 'src/assets/common/fruit/strawberry.png', quantity: 1, discount: '50%', category: '신선식품' },
    { id: 37, name: '파인애플', image: 'src/assets/common/fruit/fineapple_s.png', quantity: 3, discount: '50%', category: '신선식품' },
    { id: 38, name: '사과', image: 'src/assets/common/fruit/apple.png', quantity: 1, discount: null, category: '신선식품' },
    { id: 39, name: '라면', image: 'src/assets/common/food/ramen.png', quantity: 5, discount: '20%', category: '즉석식품' },
    { id: 40, name: '햄버거', image: 'src/assets/common/food/burger.png', quantity: 2, discount: null, category: '즉석식품' },
    { id: 41, name: '노트북', image: 'src/assets/common/electronics/laptop.png', quantity: 1, discount: '10%', category: '전자제품' },
    { id: 42, name: '스마트폰', image: 'src/assets/common/electronics/phone.png', quantity: 1, discount: null, category: '전자제품' },
    // Add more items as needed
    { id: 43, name: '딸기', image: 'src/assets/common/fruit/strawberry.png', quantity: 1, discount: '50%', category: '신선식품' },
    { id: 44, name: '파인애플', image: 'src/assets/common/fruit/fineapple_s.png', quantity: 3, discount: '50%', category: '신선식품' },
    { id: 45, name: '사과', image: 'src/assets/common/fruit/apple.png', quantity: 1, discount: null, category: '신선식품' },
    { id: 46, name: '라면', image: 'src/assets/common/food/ramen.png', quantity: 5, discount: '20%', category: '즉석식품' },
    { id: 47, name: '햄버거', image: 'src/assets/common/food/burger.png', quantity: 2, discount: null, category: '즉석식품' },
    { id: 48, name: '노트북', image: 'src/assets/common/electronics/laptop.png', quantity: 1, discount: '10%', category: '전자제품' },
    { id: 49, name: '스마트폰', image: 'src/assets/common/electronics/phone.png', quantity: 1, discount: null, category: '전자제품' },
    // Add more items as needed
    { id: 50, name: '딸기', image: 'src/assets/common/fruit/strawberry.png', quantity: 1, discount: '50%', category: '신선식품' },
    { id: 51, name: '파인애플', image: 'src/assets/common/fruit/fineapple_s.png', quantity: 3, discount: '50%', category: '신선식품' },
    { id: 52, name: '사과', image: 'src/assets/common/fruit/apple.png', quantity: 1, discount: null, category: '신선식품' },
    { id: 53, name: '라면', image: 'src/assets/common/food/ramen.png', quantity: 5, discount: '20%', category: '즉석식품' },
    { id: 54, name: '햄버거', image: 'src/assets/common/food/burger.png', quantity: 2, discount: null, category: '즉석식품' },
    { id: 55, name: '노트북', image: 'src/assets/common/electronics/laptop.png', quantity: 1, discount: '10%', category: '전자제품' },
    { id: 56, name: '스마트폰', image: 'src/assets/common/electronics/phone.png', quantity: 1, discount: null, category: '전자제품' },
    // Add more items as needed
    { id: 57, name: '딸기', image: 'src/assets/common/fruit/strawberry.png', quantity: 1, discount: '50%', category: '신선식품' },
    { id: 58, name: '파인애플', image: 'src/assets/common/fruit/fineapple_s.png', quantity: 3, discount: '50%', category: '신선식품' },
    { id: 59, name: '사과', image: 'src/assets/common/fruit/apple.png', quantity: 1, discount: null, category: '신선식품' },
    { id: 60, name: '라면', image: 'src/assets/common/food/ramen.png', quantity: 5, discount: '20%', category: '즉석식품' },
    { id: 61, name: '햄버거', image: 'src/assets/common/food/burger.png', quantity: 2, discount: null, category: '즉석식품' },
    { id: 62, name: '노트북', image: 'src/assets/common/electronics/laptop.png', quantity: 1, discount: '10%', category: '전자제품' },
    { id: 63, name: '스마트폰', image: 'src/assets/common/electronics/phone.png', quantity: 1, discount: null, category: '전자제품' },
    // Add more items as needed
    { id: 64, name: '딸기', image: 'src/assets/common/fruit/strawberry.png', quantity: 1, discount: '50%', category: '신선식품' },
    { id: 65, name: '파인애플', image: 'src/assets/common/fruit/fineapple_s.png', quantity: 3, discount: '50%', category: '신선식품' },
    { id: 66, name: '사과', image: 'src/assets/common/fruit/apple.png', quantity: 1, discount: null, category: '신선식품' },
    { id: 67, name: '라면', image: 'src/assets/common/food/ramen.png', quantity: 5, discount: '20%', category: '즉석식품' },
    { id: 68, name: '햄버거', image: 'src/assets/common/food/burger.png', quantity: 2, discount: null, category: '즉석식품' },
    { id: 69, name: '노트북', image: 'src/assets/common/electronics/laptop.png', quantity: 1, discount: '10%', category: '전자제품' },
    { id: 70, name: '스마트폰', image: 'src/assets/common/electronics/phone.png', quantity: 1, discount: null, category: '전자제품' },
    // Add more items as needed
  ],
  itemsPerRow: 5,
  maxVisibleRows: 3,
  rowHeight: 150, // Reverted row height to original
  selectedTab: '신선식품'
  ,  popupMessage: ''
};

export default {
  name: 'KoreanInventoryInterface',
  data() {
    return model;
  },
  computed: {
    fruitRows() {
      const rows = [];
      for (let i = 0; i < this.fruits.length; i += this.itemsPerRow) {
        rows.push(this.fruits.slice(i, i + this.itemsPerRow));
      }
      return rows;
    },
    filteredFruitRows() {
      const filteredFruits = this.fruits.filter(fruit => fruit.category === this.selectedTab);
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
          overflowY: 'scroll'
        };
      }
      return {};
    }
  },
  methods: {
    selectTab(tab) {
      this.selectedTab = tab;
    },
    goBack(){
      this.$router.push('/tutorialStart');
    },
    placeOrder() {
      this.storage = true;
      this.popupMessage = '50 >> 70 필요금액 30,000';
      this.popup = true;
    },
    closePopup(){
      this.storage = false;
      this.dispose = false;
      this.popup = false;
    },
    disposePopup(e){
      this.disquantity = 0;
      this.disproduct = e.target.parentElement.innerHTML;
      this.disfruit = this.fruits.find(f=>f.id==e.target.id);
      this.popup = true;
      this.dispose = true;
    },
    increaseQuantity() {
      if(this.disfruit.quantity>this.disquantity){
        this.disquantity++;
      }
    },
    decreaseQuantity() {
      if (this.disquantity > 0) {
        this.disquantity--;
      }
    },
    nextPage(){
      if(this.tutoPage.pagenum>=2 || this.tutoPage.pagenum<0){
        this.tutoPage.tutopage='maingame';
        this.tutoPage.pagenum=4;
        this.$router.push('/tutorialMain');
      }else if(this.tutoPage.pagenum==0){
        this.popup=true;
        this.dispose=true;
        this.tutoPage.pagenum++;
      }else if(this.tutoPage.pagenum==1){
        this.dispose=false;
        this.storage=true;
        this.tutoPage.pagenum++;
      }
    },
  },
  mounted(){
    this.tutoPage=curTutoPage();
    this.tutoPage.pagenum=0;
    if(history.state.popup!=null){
      console.log(history.state.popup);
      this.popup = history.state.popup;
    }
  }
};
</script>

<style scoped>
.main-container {
  width: 100%;
  height: 100vh;
  position: relative;
  overflow: hidden;
  background-repeat: no-repeat;
  font-family: RecipekoreaOTF;
  font-size: 24px;
  background-color: #f5f5f5;
  text-align: center;
  max-width: 100%;
  min-height: 90%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-image: url('/background/whitebg.png');
  background-size: 100% 100%;
}

.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0.6vw 1.7vw;
    margin-bottom: -11vh;
    border: 0.25vw solid #8B4513;
    border-radius: 9999px;
    min-width: 90vw;
    margin-top: -2vh;
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
    background-color: #5D2906;
    color: white;
    padding: 0.6vw 1.4vw;
    border-radius: 9999px;
    gap:3.5vw;
    height: 3.3vh;
    width: 13vw;
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
  width: 90%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 16px;
  margin-bottom: 0;
  margin-top: 12vh; /* Adjusted to account for fixed header */
}

.back-button {
  color: #5d2906;
  cursor: pointer;
  width: 2vw;
  height: 2vw;
}

.title {
  font-size: 2vw;
  font-weight: bold;
  text-align: center;
}

.storageCount{
  width:808px;
  height:30px;
  text-align:right;
}

.skip-button {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #5d2906;
  cursor: pointer;
}

.double-arrow {
  font-size: 16px;
}

.inventory-status-bar {
  display: flex;
  justify-content: space-between;
  padding: 0 16px;
  margin-bottom: 16px;
}

.info-text {
  font-size: 14px;
}

.capacity-text {
  font-size: 14px;
  font-weight: bold;
}

.tab-container {
  width: 50vw;
  height: 50vh;
  border: 4px solid #6F3533;
  border-radius: 16px;
  overflow: hidden;
  margin-bottom: 20px;
  background-color: #EAE5DE;
}

.tab-menu {
  display: flex;
  background-color: #fff3d4;
  border-bottom: 4px solid #6F3533;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 12px;
  font-weight: bold;
  cursor: pointer;
}

.tab-item.active {
  background-color: #fff3d4;
  color: #5d2906;
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

}

.fruit-row {
  display: flex;
  justify-content: space-around;
  margin-bottom: 15px;
}

.fruit-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  width: 160px; /* Reverted width */
  margin: 5px; /* Reverted margin */
}

.fruit-image {
  width: 100%; /* Reverted width */
  height: 100%; /* Reverted height */
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

.fruit-container::-webkit-scrollbar-track-piece{
  background-color:#f5f5dc;
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
  z-index: 10;
}

.popup-content {
  background-color: #F2F1EC;
  width: 789px;
  height: 392px;
  border-radius: 30px;
  overflow: hidden;
}

.popup-header {
  background-color: #6A396C;
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

.decrease-button, .increase-button {
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

.disposeButton{
  width:181px;
  height:59px;
}

.expansionButton{
  width:9.5vw;
  height:5.5vh;
  background-color:rgba(0, 0, 0, 0);
  border:0;
  background-image:url("/src/assets/element/increasestorage.png");
  background-size:100% 100%;
}
.blind{
  width:100vw;
  height:100vh;
  position:absolute;
  z-index:20;
}
.tuto{
  position:absolute;
  background-color:#FFEDDE;
  font-size:2vh;
}
.tuto p{
  margin:1vh 2vw;
}
.win1{
  top:22vh;
  left:40vw;
}
.win2{
  top:55vh;
  left:10vw;
}
.win3{
  top:65vh;
  left:8vw;
}
.win4{
  top:75vh;
  left:15vw;
}
.win5{
  top:35vh;
  left:10vw;
  z-index:15;
}
.win6{
  top:33vh;
  left:20vw;
  z-index:15;
}

</style>