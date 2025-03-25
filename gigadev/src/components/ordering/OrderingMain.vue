<template>
    <div class="main-container">
      <Topbar />
    <!-- Header -->
    <!-- <div class="header">
      <div class="left-section">D-{{ this.playday }}</div>
      <div class="right-section">
        <div class="money-bag">
          <span class="bag-icon">💰</span>
          <span class="amount">{{ (revenue.cash*1).toLocaleString() }}원</span>
        </div>
        <span class="settings-icon">⚙️</span>
        
      </div>  
    </div> -->

    <div class="main-content">
      <div class="back-button" @click="backToMain">
        <img id="backbtn" src="/common/Vector.png" alt="back" />
      </div>

      <div class="title-section">
        <h2 class="delivery-title">발주</h2>
      </div>

      <div class="product-section" style="font-family: rk;">
        <div style="margin-top:1vh;">
          <div class="product-table">
            <div class="table-header">
              <div class="category-header">분류</div>
              <div class="name-header">상품명</div>
              <div class="quantity-header">수량</div>
              <div class="price-header">발주 가격</div>
              <div class="stock-header">보유 수량</div>
            </div>

            <div style="display:flex;align-items:flex-start;height:55vh;">
              <div style="width:20%;height:55vh;">
                <div class="categories-menu">
                  <div 
                    class="category-item" 
                    :class="{ active: selectedCategory === '신선식품' }"
                    @click="selectedCategory = '신선식품'"
                  >
                    <div class="category-name">신선식품</div>
                  </div>
                  <div 
                    class="category-item" 
                    :class="{ active: selectedCategory === '즉석식품' }"
                    @click="selectedCategory = '즉석식품'"
                  >
                    <div class="category-name">즉석식품</div>
                  </div>
                  <div 
                    class="category-item" 
                    :class="{ active: selectedCategory === '전자제품' }"
                    @click="selectedCategory = '전자제품'"
                  >
                    <div class="category-name">전자제품</div>
                  </div>
                </div>
              </div>
              <div style="width:80%;height:55vh;">
                <div class="product-items-container">
                  
                  <div class="product-items">
                    <div v-if="selectedCategory=='신선식품' || selectedCategory=='즉석식품' && this.days>=5 || selectedCategory=='전자제품' && this.days>=15 "
                      v-for="product in filteredProducts" :key="product.goodsno" class="product-row">
                      
                      <div class="product-image-container">
                        <img class="product-image" :src="product.image" :alt="product.goodsname">
                      </div>
                      
                      <div class="quantity-control">
                        <button class="decrease-button" @click="decreaseQuantity(product)">−</button>
                        <span class="quantity-display">{{ product.ordercount || 0 }}</span>
                        <button class="increase-button" @click="increaseQuantity(product)">+</button>
                      </div>
                      <div class="price-display">{{ product.orderprice.toLocaleString() }}원</div>
                      <!-- 창고 보유 수량 표시 -->
                      <div class="stock-display">{{ product.orderquantity || 0 }}</div>
                    </div>
                    <div v-else-if="selectedCategory=='즉석식품'" class="comingsoon">
                      <h1>5일차 오픈 예정</h1>
                    </div>
                    <div v-else-if="selectedCategory=='전자제품'" class="comingsoon">
                      <h1>15일차 오픈 예정</h1>
                    </div>
                  </div> 
                </div>
              </div>
            </div>
          </div>
          <div class="total-products-fake">창고 총 상품 개수: {{ getTotalProductCount() }}/{{ this.storagelevel }}</div>
        </div>
        <div style="text-align:center;">
          <div class="cart-section">
            <div class="cart-title">장바구니</div>
            <div class="cart-items">
              <div v-for="(item, index) in cart" :key="index" class="cart-item">
                <div class="cart-item-name">{{ item.goodsname }} {{ item.ordercount }}개</div>
              </div>
            </div>
            <div class="cart-total">
              총 {{ getTotalItems() }}개
              <br>
              총 {{ getTotalPrice().toLocaleString() }}원
            </div>
          </div>
          <button class="order-button" @click="placeOrder"></button>
        </div>
      </div>

      <div class="footer">
        <!-- <div class="total-products">총 상품 개수: {{ getTotalProductCount() }}/50</div> -->
      </div>
    </div>
    
    <div v-show="popup" class="popup-overlay" @click="closePopup">
      <div class="popup-content" @click.stop>
        <div class="popup-header">
          <p>알림</p>
        </div>
        <div class="popup-body">
          <p v-if="popupMessage" class="popup-message">{{ popupMessage }}</p>
          <div v-if="popupMessage === '발주완료'" class="ordered-items">
            <p v-for="(item, index) in orderCompleteItems" :key="index" class="ordered-item">
              {{ item.goodsname }} +{{ item.ordercount }}개
            </p>
          </div>
          <button v-if="storage" class="storagebutton" @click="gotostorage">확장하러 가기</button>
        </div>
      </div>
    </div>
  </div> 
</template>

<script>
import { revenueStore } from '@/assets/pinia/maingame';
import Topbar from '../common/topbar.vue';

export default {
  components: { Topbar},
  data() {
  return {
    popupMessage: '',
    cartList: [],
    itemPrice: 0,
    popup: false,
    money: 500000,
    selectedCategory: '신선식품',
    storageCount: 0,
    storage: false,
    products: [],
    cart: [],
    revenue: revenueStore(),
    playday: 30,
    storagelevel: 50,
    days: 1,
    orderCompleteItems: [] // 발주 완료 아이템 목록 추가
  }
},
  computed: {
    filteredProducts() {
      return this.products.filter(product => product.goodstype === this.selectedCategory);
    }
  },
  mounted() {
    this.getProductData(); 
    const gameNo = sessionStorage.getItem("gameNo");
    // 그냥 돈만 가져와야지
    fetch("http://3.38.185.252:8080/spring/maingame/moneydata?gameNo="+gameNo)
    .then(response=>response.text())
    .then(data=>this.revenue.cash = data)
  },
  methods: {
    getProductData(){
      const gameNo = sessionStorage.getItem("gameNo");
      // 상품 데이터 가져오기
      fetch('http://3.38.185.252:8080/spring/ordering/selectAllPrd?gameNo='+gameNo, {
        method: 'GET'
      })
      .then(response => response.json())
      .then(data => {
        // 상품 데이터를 저장 (DB에서 가져온 원래 orderquantity 값 유지)
        this.products = data;

          console.log(data[0]);

          this.playday = this.playday - (data[0].playday-1); 
          this.storagelevel = data[0].storagelevel;
          this.days = data[0].playday;
         
        // 재고 데이터 초기화
        this.initializeStockData();
        //this.storageLevel  =
        // 서버에서 가져온 데이터 로깅
        console.log('서버에서 가져온 상품 데이터:', this.products);
        
        for(let i = 0; i < this.products.length; i++) {
          console.log(this.products[i].goodsno);
          console.log(this.products[i].orderprice);
          console.log(this.products[i].image);
          console.log(this.products[i].orderquantity);
          console.log(this.products[i].expdate);
          console.log(this.products[i].goodsname);
          console.log(this.products[i].goodstype);
        }
      })
      .catch(error => {
        console.error('상품 데이터 가져오기 오류:', error);
        this.popupMessage = '상품 데이터를 가져오는 중 오류가 발생했습니다.';
        this.popup = true;
      });
    },
    // 임시 재고 데이터 초기화 (실제로는 DB에서 가져와야 함)
    initializeStockData() {
      // 예시: 모든 상품에 임의의 재고 수량 할당 (1-5 사이의 값)
      this.stockData = {}; // 객체 초기화
      this.products.forEach(product => {
        // 정수 값으로 명확하게 저장 (0-5 사이의 값)
        this.stockData[product.goodsno] = Math.floor(Math.random() * 6); 
      });
      console.log('초기화된 재고 데이터:', this.stockData);
    },
    
    // 재고 데이터 가져오기 (실제 서버 API에 맞게 수정 필요)
    fetchStockData() {
      // 예시: 서버에서 재고 데이터 가져오기
      // 실제 API 엔드포인트로 교체 필요
      fetch('http://3.38.185.252:8080/spring/storage/getStock', {
        method: 'GET'
      })
      .then(response => response.json())
      .then(data => {
        // 재고 데이터를 상품 번호를 키로 하는 객체로 변환
        data.forEach(item => {
          this.stockData[item.goodsno] = item.quantity || 0;
        });
      })
      .catch(error => {
        console.error('재고 데이터 가져오기 오류:', error);
        // 오류 발생 시 임시 데이터 사용
        this.initializeStockData();
      });
    },
    
    // 상품 재고 가져오기
    getStock(goodsno) {
      // 재고 데이터에서 해당 상품의 재고 반환 (숫자로 확실하게 변환)
      const stock = this.stockData[goodsno];
      return (typeof stock === 'number' && !isNaN(stock)) ? stock : 0;
    },
    
    increaseQuantity(product) {
      // ordercount 필드가 없으면 초기화
      if (product.ordercount === undefined) {
        product.ordercount = 0;
      }
      product.ordercount++;
      this.updateCart(product);
    },
    
    decreaseQuantity(product) {
      // ordercount 필드가 없으면 초기화
      if (product.ordercount === undefined) {
        product.ordercount = 0;
      }
      
      if (product.ordercount > 0) {
        product.ordercount--;
        this.updateCart(product);
      }
    },
    
    updateCart(product) {
      const existingItem = this.cart.find(item => item.goodsno === product.goodsno);
      if (existingItem) {
        if (product.ordercount > 0) {
          existingItem.ordercount = product.ordercount;
        } else {
          // 수량이 0이면 장바구니에서 제거
          const index = this.cart.findIndex(item => item.goodsno === product.goodsno);
          if (index !== -1) {
            this.cart.splice(index, 1);
          }
        }
      } else if (product.ordercount > 0) {
        // 새 상품을 장바구니에 추가할 때 마지막에 추가
        this.cart.push({
          goodsno: product.goodsno,
          goodsname: product.goodsname,
          ordercount: product.ordercount,
          orderprice: product.orderprice
        });
      }
    },
    
    // 장바구니의 총 아이템 수 계산
    getTotalItems() {
      if (this.cart.length === 0) return 0;
      return this.cart.reduce((total, item) => {
        return total + (item.ordercount || 0);
      }, 0);
    },

    // 장바구니의 총 가격 계산
    getTotalPrice() {
      if (this.cart.length === 0) return 0;
      return this.cart.reduce((total, item) => {
        return total + ((item.orderprice || 0) * (item.ordercount || 0));
      }, 0);
    },
    
    // 총 창고 상품 개수 계산
    getTotalProductCount() {
      return this.products.reduce((total, product) => total + (product.orderquantity || 0), 0);
    },
    
    // 발주 처리
    placeOrder() {
      const totalPrice = this.getTotalPrice();
      
      // 현재 창고에 있는 재고의 총량 계산
      const currentStockTotal = this.getTotalProductCount();
      
      // 장바구니 총 수량 계산
      const cartTotal = this.getTotalItems();

      // 잔액 체크
      if (totalPrice > this.revenue.cash) {
        this.popupMessage = '잔액이 부족합니다.';
        this.popup = true;
        return;
      }
      
      // 장바구니 체크
      if (this.cart.length === 0) {
        this.popupMessage = '주문할 상품을 선택해주세요.';
        this.popup = true;
        return;
      }

      // 창고 용량 체크 (현재 재고 + 발주 수량이 50을 초과하는지)
      if (currentStockTotal + cartTotal > 50) {
        this.popupMessage = '창고가 가득 찼습니다 창고를 정리하거나 확장해주세요';
        this.storage = true;
        this.popup = true;
        return;
      }

      // Storage 클래스에 맞는 데이터 구조로 변환
      const orderItems = this.cart.map(item => {
        return {
          // orderingno는 자동 생성될 것으로 가정하여 null 또는 생략
          orderingno: null,
          goodsno: item.goodsno,
          playno: sessionStorage.getItem("gameNo"), // 게임 번호  // 플레이어 번호?
          expdate: this.getExpDate(item),
          orderquantity: item.ordercount,
          saleprice: item.orderprice, // 판매 가격으로 발주 가격을 사용
          saledgree: this.days || 0,  // 현재 게임 일수
          disposalyn: 'N'  // 초기값은 폐기되지 않음
        };
      });
      
      console.log("서버로 전송할 데이터:", JSON.stringify(orderItems));
      
      // fetch API를 사용하여 서버에 데이터 전송
      fetch('http://3.38.185.252:8080/spring/ordering/insertOrdering', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(orderItems)
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('서버 응답이 올바르지 않습니다.');
        }
        return response.json();
      })
      .then(data => {
  console.log('주문 성공:', data);
  
  // 발주 완료 아이템 목록 저장 (장바구니 복사)
  this.orderCompleteItems = JSON.parse(JSON.stringify(this.cart));
  
  // 주문 성공 후 로직 실행
  // 잔액 차감
  this.revenue.cash -= totalPrice;
  this.revenue.orderPrice -= totalPrice;

  fetch("http://3.38.185.252:8080/spring/maingame/expense?price="+(-totalPrice)+
        "&gameNo="+sessionStorage.getItem("gameNo"))
  .then(response=>console.log(response));
  
  this.revenue.saveState();
  
  // 팝업 메시지 표시
  this.popupMessage = '발주완료';
  this.popup = true;
  
  // 장바구니 비우기
  this.cart = [];
  
  // 상품 발주 수량 초기화
  this.products.forEach(product => {
    product.ordercount = 0;
  });

  // 페이지 새로고침 - 서버에서 최신 데이터 가져오기
  this.getProductData();
})
    },
    
    // 상품 유형에 따른 유통기한 반환
    getExpDate(item) {
      // 이미 expdate가 있으면 그 값을 사용
      if (item.expdate) return item.expdate;
      
      // 상품 찾기
      const product = this.products.find(p => p.goodsno === item.goodsno);
      
      if (!product) return 999; // 상품을 찾지 못한 경우 기본값
      
      // 상품 타입에 따라 유통기한 설정
      if (product.goodstype === '신선식품') return 3;
      if (product.goodstype === '즉석식품') return 4;
      return 999; // 기본값 (전자제품 등)
    },
    
    closePopup() {
      this.popup = false;
      this.storage = false;
      this.popupMessage = '';
    },

    gotostorage() {
      this.$router.push({
        name: "storageMain",
        state: {
          popup: true,
        }
      });
    },
    
    backToMain() {
      this.$router.push("/mainMenu");
    }
  },
  components:{ Topbar }
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


    background-image: url('/background/whitebg.png');
    background-size: 100% 100%;

}
.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0.6vw 1.7vw;
    margin-bottom: 0;
    border: 0.25vw solid #8B4513;
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
    background-color: #5D2906;
    color: white;
    padding: 0.6vw 1.4vw;
    border-radius: 9999px;
    gap:3.5vw;
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
  width: 90%;
  display: flex;
  align-items: center;
  padding: 8px 16px;
  margin-bottom: 0;
  margin-top: 1vh; /* Adjusted to account for fixed header */
  gap:41vw;
}
.main-content {
  padding: 15px;
  position: relative;
}

.back-button {
  position: absolute;
  top:5vh;
  left: -7vw;
  width: 2vw;
  height: 2vh;
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  cursor: pointer;
}

.back-button img {
  width: 2vw;
  height: 2vh;
}

.title-section {
  text-align: center;
  margin: 1.5vh 0;
}

.delivery-title {
  font-family: rk;
  margin: 0;
  display: inline-block;
  position: relative;
}


.product-section {
  display: flex;
  justify-content:space-evenly;
  align-items:center;
  gap: 20px;
}

.product-table {
  /* flex: 3; */
  width:54vw;
  height:61vh;
  border: 7px solid #5e2813;
  border-top-left-radius: 52px;
  border-radius: 52px;
  overflow: hidden;
}

.table-header {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr 1fr;
  background-color: #f5e9d0;
  text-align: center;
  font-weight: bold;
  border-bottom: 7px solid #5e2813;
  height:6vh;
}
.table-header div{
  display:flex;
  justify-content:center;
  align-items:center;
  font-size:1.2vw;
  min-height:6vh;
  border-right:7px solid #5e2813;
}
.table-header>.stock-header{
  border-right:0;
}

.categories-menu {
  display: flex;
  flex-direction: column;
  height:55vh;
  border-right:7px solid #5e2813;
}

.comingsoon{
  width:100%;
  height:48vh;
  display:flex;
  justify-content:center;
  align-items:center;
}

.comingsoon>h1{
  font-size:4vh;
}

.category-item {
  display:flex;
  justify-content:center;
  align-items:center;
  padding: 1vh;
  border-bottom:7px solid #5e2813;
  cursor: pointer;
  height:11vh;
}

.category-item.active {
  background-color: #5e2813;
  color: white;
}

.product-items-container {
  border-left: 1px solid #d0bc95;
  /* max-height: 400px; */
  overflow-y: auto;
}

/* Custom scrollbar styles */
.product-items-container::-webkit-scrollbar {
  width: 8px;
}

.product-items-container::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.product-items-container::-webkit-scrollbar-thumb {
  background: #5d4037;
  border-radius: 4px;
}

.product-items-container::-webkit-scrollbar-thumb:hover {
  background: #4a332c;
}

.product-items {
  overflow:auto;
  max-height:55vh;
  scrollbar-width:none;
}

.product-row {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;
  border-bottom:7px solid #5e2813;
  align-items: center;
  text-align: center;
  height:13vh;
}

.product-row>div{
  height:13vh;
  border-right:7px solid #5e2813;
}

.product-row>.stock-display{
  display:flex;
  justify-content:center;
  align-items:center;
  border-right:0;
}

.product-row>.price-display{
  display:flex;
  justify-content:center;
  align-items:center;
}

.product-image-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.product-image {
  width: 150px;
  height: 150px;
  object-fit: contain;
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

.product-items {
  overflow-y: scroll;
  max-height: 55vh;
  pointer-events: auto; /* 마우스 이벤트 활성화 */
}

/* 스크롤바 스타일 */
.product-items::-webkit-scrollbar {
  width: 10px;
}

.product-items::-webkit-scrollbar-track {
  background: #f5f5f5;
  cursor: pointer;
}

.product-items::-webkit-scrollbar-thumb {
  background: #5e2813;
  border-radius: 5px;
  cursor: grab; /* 드래그 가능한 커서 표시 */
}

.product-items::-webkit-scrollbar-thumb:hover {
  background: #4a2010;
}

.product-items::-webkit-scrollbar-thumb:active {
  cursor: grabbing; /* 드래그 중 커서 변경 */
}

/* Firefox용 스크롤바 스타일 */
.product-items {
  scrollbar-width: thin;
  scrollbar-color: #5e2813 #f5f5f5;
}

.quantity-display {
  width: 30px;
  text-align: center;
}

.cart-section {
  /* flex: 1; */
  width:15vw;
  height:56vh;
  border: 7px solid #5e2813;
  border-radius: 52px;
  padding: 1%;
  display: flex;
  flex-direction: column;
  background-color:rgba(94,40,19,0.3);
}

.cart-title {
  font-family: rk;
  font-weight: bold;
  text-align: center;
  font-size: 18px;
  margin: 15px;
}
.cart-items {
  font-family: prebold;
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-left: 0.5vw;
  overflow-y: auto;
  max-height: 35vh;
  width: 100%;
  justify-content: flex-start; /* 항목들을 위에서부터 차례대로 배치 */
}

.cart-item {
  margin-bottom: 10px;
  width: 100%;
  text-align: left;
  display: block; /* 블록 요소로 만들어 확실히 개행되도록 */
}

.cart-item-name {
  text-align: left;
  display: block;
  width: 100%;
}

.cart-total {
  margin-top: auto;
  text-align: right;
  font-weight: bold;
  padding: 1vh 1vw;
}

.order-button {
  width:9vw;
  height:6.5vh;
  color: white;
  border: none;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
  background-image:url('/src/assets/element/orderbutton.png');
  background-color:rgba(0,0,0,0);
  background-size:100% 100%;
  margin-top:1vh;
}

.footer {
  margin-top: 20px;
  padding: 10px;
  text-align: center;
}

.total-products {
  display: inline-block;
  border: 1px solid #e74c3c;
  padding: 5px 15px;
  border-radius: 20px;
  font-size: 14px;
  position: relative;
}

.total-products-fake{
  font-family: rk;
  text-align:right;
  padding-right:0.5vw;
  padding-top:1vh;
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
  background-color: #F2F1EC;
  width: 400px;
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
  font-family: prebold;
}

.popup-body p {
  margin: 5px 0;
  font-family: prebold;
}

.popup-message {
  font-family: prebold;
  margin-bottom: 0; /* 메시지 아래 마진 제거 */
}

.ordered-items {
  margin-top: 0; /* 물품 목록 위 마진 제거 */
  max-height: 200px;
  overflow-y: auto;
  text-align: center;
  padding: 0 15px;
  font-family: rk;
}

.ordered-item {
  padding: 5px 0;
  border-bottom: 1px solid #e0e0e0;
  font-family: rk;
  margin: 0;
  text-align: center;
}

.ordered-item:first-child {
  padding-top: 0; /* 첫 번째 아이템의 상단 패딩 제거 */
}

.storagebutton{
  width:12.5vw;
  height:5.5vh;
  background-color:rgba(0,0,0,0);
  border:0;
  background-image:url("/element/increasestorage.png");
  background-size:100% 100%;
}

.timerbar{
  width:30vw;
  height:40px;
  border:3px solid #6F3533;
  border-radius:30px;
  overflow:hidden;
}
.timeleft{
  width:30vw;
  height:40px;
  background-color:#5E395A;
}
.time{
  position:relative;
  right:70px;
}
.moneybar{
  display:flex;
  justify-content:space-around;
  align-items:center;
  width: 15vw;
  height:7vh;
  margin-right:1.5vw;
  background-image:url(/element/moneybar.png);
  background-size: 100% 100%;
}
.line{
  min-height:28px;
  border-left:2px dashed rgba(256,256,256,0.2);
}
.money{
  display:flex;
  justify-content:flex-end;
  width:6vw;
  font-size: 1.3vw;
}
.money p{
  color:#FFFFFF;
}
#backbtn {
  width: 2vw;
  height: 3vh;
  margin-top: 0;
}
.ordered-items {
  margin-top: 10px;
  max-height: 200px;
  overflow-y: auto;
  text-align: left;
  padding: 0 15px;
}

.ordered-item {
  padding: 5px 0;
  border-bottom: 1px solid #e0e0e0;
  font-family: rk;
  margin: 0;
}

.ordered-item:last-child {
  border-bottom: none;
}




</style>