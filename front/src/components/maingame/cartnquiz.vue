<template>
    <div class="conv">
        <RouterView name="customer" class="background":customerA="customerA" :dialog="dialog" :quizDialog="quizDialog" :quizNum="quizNum"
                    :reliability="reliability" @quizTime="quizTime" @customer="customer"></RouterView>
        <RouterView name="counter" :quizNum="quizNum" :quizAnswer="quizAnswer" :cart="cart" :interval="interval"
                    :timeleft="timeleft" :noclick="noclick" @result="result" @revertprod="revertprod" @submit="submit"></RouterView>
        <!-- <QuizMain :quizDialog="dialog" :quizNum="quizNum" class="background" @quizTime="quizTime"/> -->
        <!-- <QuizChoice v-show="quiz" :quizNum="quizNum" :quizAnswer="quizAnswer" @result="result"/> -->
    </div>
</template>
<script>
import QuizChoice from '../quiz/quizChoice.vue';
import QuizMain from '../quiz/quizMain.vue';
import { quiz, quizAnswer, quizComment, rewardDialog } from '@/assets/data/prodNquiz.js';   // 배포할 때 @/assets 빼기!!!!!!
import { revenueStore, productStore } from '@/assets/pinia/maingame';     // 배포할 때 @/assets 빼기!!!!!!!!!!!!!!

export default {
    data(){
        return{
            dialog:'상품이 없어요',
            quizDialog:quiz[this.quizNum],
            day:Math.floor(Math.random()*10),   // 임의의 일수로 설정(1~10)
            quizMan:Math.floor(Math.random()*6)+6,
            meetQuizMan:false,
            customerCount:1,
            customerWant:[   // 수정하기
                {'딸기':2,'파인애플':1},
                {'딸기':1,'파인애플':2},
            ],  // 일단 두개만
            currentWant:{}, // 수정하기
            reliability:50,
            quiz:-1,
            revenue:revenueStore(),
            product:productStore(),
        }
    },
    methods:{
        result(ans){
            clearInterval(this.interval);
            this.$emit('notclick',true);
            if(quizAnswer[this.quizNum]==ans){
                this.quizDialog='정답입니다.';
                this.revenue.qeezeYN='Y';
                // 사용자가 클릭하면 넘어갈지 일정 시간 뒤 넘어갈지 결정하기 일단 후자로
                setTimeout(() => {
                    this.quizDialog=rewardDialog[this.day].dialog;
                    // this.quiz=rewardDialog[this.day].reward;
                    // if(rewardDialog[this.day].reward==0){
                    //     // 수익에 +30000원 추가. 내가 추가할 게 있나요?
                    // }else if(rewardDialog[this.day].reward>=1 && rewardDialog[this.day].reward<=3){
                    //     // 영업 종료 후 총 수익에서 n% 증가, 정산서에 표시. 내가 추가할 게 있나요?
                    // }else if(rewardDialog[this.day].reward==4){
                    //     // 뉴스 내용 전달. 내가 추가할 게 있나요?
                    // }
                }, 3500);
            }else{
                this.quizDialog='오답입니다.';
                setTimeout(()=>{
                    this.quizDialog=quizComment[this.quizNum];
                },3500);
            }
            setTimeout(()=>{
                this.$emit('notclick',false);
                if(this.customerCount>=10){
                    this.$emit('bgmstop');
                    this.product.saveState();
                    this.revenue.saveState();
                    console.log("cartnquiz",localStorage.getItem('product'));
                    this.$router.push({
                        name:'calculation',
                        state:{
                            profit:this.profit,
                            moneyhave:this.moneyhave,
                            product:this.product
                        }
                    }); // 나중에 결과화면 이동으로 바꾸기
                }else{
                    this.$emit('bgmchange','customer');
                    this.$router.push('/maingame/'+ ++this.customerCount);
                }
            },7000);
        },
        quizTime(){
            this.$emit('quizTime');
        },
        customer(){
            this.currentWant=this.customerWant[Math.floor(Math.random()*2)];    // 수정하기기
            const key = Object.keys(this.currentWant);
            this.dialog='';
            key.forEach((p,i)=>{
                this.dialog+=p + ' ' + this.currentWant[p] + '개';
                if((i+1)!=key.length) this.dialog+=', ';
            });
            this.dialog+=' 주세요';
            const value = Object.values(this.currentWant);
            value.forEach(v=>console.log(v));
            this.$emit('customer');
        },
        revertprod(m,t){
            this.$emit('revertprod',m,t);
        },
        submit(){
            this.$emit('notclick',true);
            const prodcount=[];
            const key = Object.keys(this.currentWant);
            for(let i=0;i<key.length;i++){
                console.log(i,key[i]);
                prodcount.push(0);
                this.product.cart.forEach(c=>{
                    if(c.goodsName.includes(key[i])){
                        prodcount[i]+=c.orderQuantity;
                    }
                });
            }
            const prodwant = Object.values(this.currentWant);
            let loss = 0;
            let perfect = true;
            let nothing = true;
            let over = 0;
            let under = 0;
            for(let i=0;i<prodcount.length;i++){
                console.log('prod',prodcount[i]);
                console.log('want',prodwant[i]);

                const prod_50 = this.product.cart.find(c=>c.goodsName==key[i] && c.expDate==1);
                const prod = this.product.cart.find(c=>c.goodsName==key[i]);

                if(prod_50!=null){
                    this.revenue.salesMount += prod_50.orderQuantity*prod_50.salePrice;
                }
                if(prod!=null){
                    this.revenue.salesMount += prod.orderQuantity * prod.salePrice;
                }
                
                if(prodcount[i]==0 && nothing){
                    perfect = false;
                    under += prodwant[i];
                }else if(prodcount[i]==prodwant[i]){
                    nothing = false;
                }else if(prodcount[i]<prodwant[i]){
                    nothing = false;
                    perfect = false;
                    under += prodwant[i]-prodcount[i];
                }else if(prodcount[i]>prodwant[i]){
                    nothing = false;
                    perfect = false;
                    over += prodcount[i]-prodwant[i];
                    if(prod_50!=null){
                        if(prod_50.orderQuantity>=(prodcount[i]-prodwant[i])){
                            loss += prod_50.salePrice*(prodcount[i]-prodwant[i]);
                        }else if(prod_50.orderQuantity<(prodcount[i]-prodwant[i])){
                            loss += prod_50.salePrice*prod_50.orderQuantity + prod*(prodcount[i]-prodwant[i]-prod_50.orderQuantity);
                        }
                    }else{
                        loss += prod.salePrice*(prodcount[i]-prodwant[i]);
                    }
                    this.revenue.salesMount -= loss;
                }
                console.log(this.revenue.salesMount);

                this.product.decreaseQuantity(prod,prod_50);
            }

            let timeout = 0;
            if(nothing){
                this.dialog='손님이 화났습니다! ';
                if(this.reliability<100){
                    this.dialog+='신뢰도 -5';
                    this.reliability-=5;
                }
            }else if(perfect){
                this.dialog='손님이 만족했습니다 ';
                if(this.reliability<100){
                    this.dialog+='신뢰도 +5';
                    this.reliability+=5;
                }
                if(this.reliability>=100){
                    this.revenue.feverYN='Y';
                }
            }else{
                if(over>0 && under>0){
                    timeout = 3500;
                    this.dialog=under + '개 덜 판매했습니다. ';
                    if(this.reliability<100){
                        this.dialog+='신뢰도 -2';
                        this.reliability-=2;
                    }
                    setTimeout(()=>{
                        this.dialog=over + '개 더 판매했습니다. ';
                        this.dialog+='-' + loss + '원 ';
                    },3500)
                }else if(under>0){
                    this.dialog=under + '개 덜 판매했습니다. ';
                    if(this.reliability<100){
                        this.dialog+='신뢰도 -2';
                        this.reliability-=2;
                    }
                }else if(over>0){
                    this.dialog=over + '개 더 판매했습니다. ';
                    this.dialog+='-' + loss + '원 ';
                    if(this.reliability<100){
                        this.dialog+='신뢰도 -2';
                        this.reliability-=2;
                    }
                }
            }
            setTimeout(()=>{
                this.$emit('notclick',false);
                if(this.reliability==0){
                    this.$emit('bgmstop');
                    this.$router.push('/poorending1');
                }else if((this.customerCount+1)==this.quizMan){
                    this.$emit('bgmchange','quiz');
                    this.$router.push('/maingame/quiz');
                }else if(this.customerCount>=10){
                    this.$emit('bgmstop');
                    this.product.saveState();
                    this.revenue.saveState();
                    console.log("cartnquiz",localStorage.getItem('product'));
                    this.$router.push({
                        name:'calculation',
                        state:{
                            profit:this.profit,
                            moneyhave:this.moneyhave,
                            product:this.product
                        }
                    }); // 나중에 결과화면 이동으로 바꾸기
                }else{
                    this.$router.push('/maingame/'+ ++this.customerCount);
                    this.customer();
                }
            },3500+timeout);
        }
    },
    watch:{
        timeleft(curVal, oriVal){
            if(curVal<=0){
                clearInterval(this.interval);
                this.$emit('notclick',true);
                if((this.customerCount+1)==this.quizMan && this.meetQuizMan){
                    this.quizDialog='시간을 초과하였습니다.';
                    setTimeout(()=>{
                        this.quizDialog=quizComment[this.quizNum];
                    },3500);
                    setTimeout(()=>{
                        this.$emit('notclick',false);
                        if(this.customerCount>=10){
                            this.$emit('bgmstop');
                            this.product.saveState();
                            this.revenue.saveState();
                            console.log("cartnquiz",localStorage.getItem('product'));
                            this.$router.push({
                                name:'calculation',
                                state:{
                                    profit:this.profit,
                                    moneyhave:this.moneyhave,
                                    product:this.product
                                }
                            }); // 나중에 결과화면 이동으로 바꾸기
                        }else{
                            this.$emit('bgmchange','customer');
                            this.$router.push('/maingame/'+ ++this.customerCount);
                        }
                    },7000);
                }else{
                    this.dialog='손님이 화났습니다! ';
                    if(this.reliability<100){
                        this.dialog+='신뢰도 -5';
                        this.reliability-=5;
                    }
                    if(this.reliability<=0){
                        setTimeout(()=>{
                            this.$emit('bgmstop');
                            this.$router.push('/poorending1');
                        },3500);
                    }else if((this.customerCount+1)==this.quizMan){
                        setTimeout(()=>{
                            this.meetQuizMan=true;
                            // this.$emit('rollback');
                            this.$emit('notclick',false);
                            this.$emit('bgmchange','quiz');
                            this.$router.push('/maingame/quiz');
                        },3500);
                    }else{
                        setTimeout(()=>{
                            // this.$emit('rollback');
                            this.$emit('notclick',false);
                            if(this.customerCount>=10){
                                this.$emit('bgmstop');
                                this.product.saveState();
                                this.revenue.saveState();
                                console.log("cartnquiz",localStorage.getItem('product'));
                                this.$router.push({
                                    name:'calculation',
                                    state:{
                                        profit:this.profit,
                                        moneyhave:this.moneyhave,
                                        product:this.product
                                    }
                                }); // 나중에 결과화면 이동으로 바꾸기
                            }else{
                                this.$router.push('/maingame/'+ ++this.customerCount);
                                this.customer();
                            }
                        },3500);
                    }
                }
            }
        },
        revertprod(modal,target){
            console.log("2!");
            this.$emit('revertprod',modal,target);
        },
        product:{
            handler(curval,orival){
                console.log(this.product);
            },
            deep:true,
        },
        reliability:{
            handler(curVal,oriVal){
                this.revenue.reliability=curVal;
            }
        }
    },
    mounted(){
        if(this.revenue.reliability==100){
            this.revenue.reliability=50;
        }
        this.reliability = this.revenue.reliability;
    },
    components:{
        QuizMain,QuizChoice,
    },
    props:['quizNum','interval','timeleft','customerA','cart','noclick','moneyhave','product']
}
</script>
<style scoped>
    .conv{
        width:52vw;
        height:82.5vh;
    }
    .conv p{
        margin:0;
    }
    .background{
        width:52vw;
        height:53.5vh;
    }
    .counter{
        width:52vw;
        text-wrap:nowrap;
    }
    .amount{
        display:inline-block;
        width:26vw;
        text-align:center;
    }
    .sell{
        display:inline-block;
        width:26vw;
    }
    .amount *, .sell *{
        width:52vw;
    }
</style>