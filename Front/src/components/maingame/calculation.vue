<template>
    <div class="calculation-container">
        <div style="text-align:center;">
            <div class="calculation-paper">
                <div class="calculation-title"><p style="color:white;font-size:24px;margin:0.8vh 0;">{{ revenue.salesDay }}일차 매출 정산</p></div>
                <div style="display:flex;flex-direction:column;justify-content:space-around;height:22vh;background-color:rgba(140,64,41,0.1);border-radius:20px;">
                    <div>
                        <div class="block-left"><h3>수익</h3></div>
                        <div class="block-right"><h3>{{ calcul+revenue.disposePrice }}원</h3></div>
                    </div>
                    <div>
                        <div class="block-left"><p>판매 수익</p></div>
                        <div class="block-right"><p>{{ revenue.salesMount }}원</p></div>
                    </div>
                    <div>
                        <div v-show="revenue.qeezeYN=='Y'" class="block-left"><p style="position:relative;left:2vw;">*퀴즈 혜택</p></div>
                        <div v-show="revenue.qeezeYN=='Y'" class="block-right"><p>{{ reward }}</p></div>
                    </div>
                    <div>
                        <div v-show="revenue.feverYN=='Y'" class="block-left"><p style="position:relative;left:2vw;">*FEVER DAY</p></div>
                        <div v-show="revenue.feverYN=='Y'" class="block-right"><p>X 1.2</p></div>
                    </div>
                    <div>
                        <div class="block-left"><p>폐기 수익(20% 상품 판매 수익)</p></div>
                        <div class="block-right"><p>{{ revenue.disposePrice }}원</p></div>
                    </div>
                </div>
                <div style="display:flex;flex-direction:column;justify-content:space-around;height:14.5vh;background-color:rgba(140,64,41,0.1);border-radius:20px;">
                    <div>
                        <div class="block-left"><h3>지출</h3></div>
                        <div class="block-right"><h3>{{ revenue.orderPrice-20000 }}원</h3></div>
                    </div>
                    <div>
                        <div class="block-left"><p>발주 비용</p></div>
                        <div class="block-right"><p>{{ revenue.orderPrice }}원</p></div>
                    </div>
                    <div>
                        <div class="block-left"><p>운영비</p></div>
                        <div class="block-right"><p>-20,000원</p></div>
                    </div>
                </div>
                <div style="border-top:3px dashed #4C1B0B;"></div>
                <div style="height:3vh;">
                    <div>
                        <div class="block-left"><h3>총계</h3></div>
                        <div class="block-right"><h3>{{ calcul+revenue.disposePrice+revenue.orderPrice-20000 }}원</h3></div>
                    </div>
                </div>
                <div style="height:5.5vh;background-color:#4C1B0B;border-radius:20px;">
                    <div>
                        <div class="block-left" style="color:white;margin:1.5vh 0;"><h3>내 잔고</h3></div>
                        <div class="block-right" style="color:white;margin:1.5vh 0;">
                            <h3>{{ revenue.cash }}원</h3>
                        </div>
                    </div>
                </div>
            </div>
            <button class="dayend" @click="convClose">퇴근하기</button>
        </div>
    </div>
</template>
<script>
import { revenueStore, productStore } from '@/assets/pinia/maingame';     // @/assets 빼기! 라고 썼는데 pinia는 왜 되지?
import { quizReward, rewardDialog } from '@/assets/data/prodNquiz';

export default {
    data(){
        return{
            revenue:revenueStore(),
            product:productStore(),
            reward:'',
            calcul:0,
            gameEnd:false,
        }
    },
    methods:{
        convEnd(){
            // 게임정보, 수익, 창고 업데이트 하기
            fetch('http://3.38.185.252:8080/spring/maingame/gameend',{
                method:'POST',
                headers:{
                    'Content-Type':'application/json'
                },
                body:JSON.stringify({
                    // 보내야 할 데이터
                    // revenue db insert
                    // 일차, 판매 수익, 퀴즈 보너스, 피버타임(구현시), 발주 비용, 잔고, 신뢰도(구현시)
                    // storage db update
                    // 창고 물품.
                    revenue:{
                        'playNo':sessionStorage.getItem('gameNo'),
                        'salesDay':this.revenue.salesDay,
                        'salesMount':this.revenue.salesMount,
                        'qeezeYN':this.revenue.qeezeYN, // db:char, this:number 수정이 필요
                        "feverYN":this.revenue.feverYN,   // db:char, this:number 수정이 필요
                        'disposePrice':this.revenue.disposePrice,
                        'orderPrice':this.revenue.orderPrice,
                        'dayendcash':this.revenue.cash,
                    },
                    product:this.product.product
                })
            }).then(response=>console.log(response))
            .then(data=>{
                console.log(data);
                
            }).catch(e=>console.log(e));
            // 다음으로 넘겨야 할 데이터 추가하기
            // 잔고
            // 신뢰도(구현시), 창고 물품
        },
        convClose(){
            if(this.revenue.salesDay>=30){
                // this.$router.push("/endsummary"); // 엔딩 직전 화면으로 이동시키기
                // this.$router.push("/finalcalculation")   // 엔딩 직전 화면 이후 최종 정산. /ending vue에서 사용하기
                // this.$router.push("/normal")     // 최종 정산 화면에서 엔딩 보기를 누르면 분기에 따라 4가지의 엔딩 출력
                // this.$router.push("/poor")       // 그냥 일단 화면이 있다고 치고 만들까?
                // this.$router.push("/rich")
                // this.$router.push("/superrich")
            }else{
                this.revenue.salesDay++;
                this.revenue.salesMount=0;
                this.revenue.qeezeYN='N';
                this.revenue.feverYN='N';
                this.revenue.disposePrice=0;
                this.revenue.orderPrice=0;
                this.revenue.saveState();
                // this.product.saveState();
            }
            if(this.gameEnd){
                this.$router.push('/homeMenu');
            }else{
                this.$router.push('/mainMenu');
            }
        }
    },
    mounted(){
        this.revenue.loadState();
        this.product.loadState();

        console.log(this)
        this.calcul = this.revenue.salesMount;
        console.log(this.calcul);
        this.reward = quizReward[rewardDialog[this.revenue.salesDay-1].reward]
        if(this.revenue.qeezeYN=='Y'){
            if(this.reward==30000){
                this.calcul+=this.reward;
            }else if(this.reward==1.05 || this.reward==1.1 || this.reward==2){
                this.calcul*=this.reward;
            }
        }
        if(this.revenue.feverYN=='Y'){
            this.calcul = this.calcul*1.2;
        }
        this.revenue.cash=this.revenue.cash*1+this.calcul+this.revenue.disposePrice+this.revenue.orderPrice-20000;

        this.convEnd();
        this.revenue.saveState();

        if(history.state.gameEnd!=null){
            this.gameEnd = history.state.gameEnd;
            history.replaceState(null, document.title, window.location.href);
        }
    }
}
</script>
<style scoped>
    .calculation-container{
        width:100vw;
        height:100vh;
        background-image:url("/background/bankbg.png");
        background-size:100% 100%;
        display:flex;
        justify-content:center;
        align-items:center;
    }
    .calculation-paper{
        display:flex;
        flex-direction:column;
        justify-content:space-around;
        align-items:center;
        width:50vw;
        height:80vh;
        background-color:#F5F5F5;
        border-radius:20px;
    }
    .dayend{
        width:23vw;
        height:9vh;
        background-image:url("/element/emptydayendbtn.png");
        background-size:100% 100%;
        background-color:rgba(0,0,0,0);
        border:0;
        font-size:24px;
        margin-top:2.2vh;
    }
    .calculation-title{
        max-width:18vw;
        height:5vh;
        background-image:url("/element/Ribbon.png");
        background-size:100% 100%;
    }
    .calculation-paper>div{
        width:40vw;
        margin:auto;
    }
    .calculation-paper>div>div{
        min-height:2.5vh;
    }
    .calculation-paper p,h3{
        margin:0 1vw;
    }
    .block-left{
        display:inline-block;
        width:50%;
        height:2vh;
        text-align:left;
    }
    .block-right{
        display:inline-block;
        width:50%;
        height:2vh;
        text-align:right;
    }
</style>