<template>
    <div class="blindAll" @click="nextPage"></div>
    <div v-show="quiztime" class="quiztime">
        <img src="/element/quiztime.png">
    </div>
    <Settings :soundsetting="soundsetting" @closesound="closesound" @changebgmvol="changebgmvol" @changeeffectvol="changeeffectvol"/>
    <Usermanual :manualon="manualon" @manualoff="manualoff"/>
    <div class="maingame">
        <div style="height:10vh;overflow:visible;text-align:right;">
            <div class="topbar">    <!-- 시간 될 때 떼어내서 common에 넣어놓기 -->
                <p style="font-size:2vh;">D-30</p>
                <div style="display:flex;">
                    <!-- <img src="/timer.png" width="10" height="52"> -- -->
                    <div class="timebar-container">
                        <div class="timerbar">
                            <div v-show="timebar>0" class="timeleft" style="width:20vw"></div>
                        </div>
                        <p class="time">30초</p>
                    </div>
                </div>
                <div style="display:flex;align-items:center;">
                    <img src="/icons/person.png" style="width:2vw;height:3.5vh;">
                    <p style="margin:0;font-size:2vh;">{{ customerCount }}/10</p>
                </div>
                <div style="display:flex;align-items:center;">
                    <div class="moneybar">
                        <img src="/icons/money.png" style="width:2.5vw;height:4.5vh;">  <!-- @/assets 빼기!!!!! -->
                        <div class="line"></div>
                        <div class="money"><p style="font-size:2vh;">{{ moneyhave }}원</p></div>
                    </div>
                    <img src="/icons/gameoption.png" style="width:2vw;height:3.5vh;" @click="opensettings=!opensettings">
                </div>
            </div>
            <div v-show="opensettings" class="settings">
                <div class="settingslist">
                    <p @click="soundsetting=true;">사운드</p>
                    <p @click="manualon=true;">초보자메뉴얼</p>
                    <p style="background-color:#56174F;color:#FFFFFF;">저장하기</p>
                </div>
            </div>
        </div>
        <div class="product-container">
            <div class="cusconv background">
                <div style="display:flex;">
                    <div class="relability_bar">
                        <div class="bar_inside">
                            <p v-show="relability==100" class="fever">FEVER<br>TIME</p>
                            <p v-show="relability<=90" class="figure">{{ relability }}</p>
                            <div class="figure_bar" :class="{'lowrela':relability<=40,'rela':relability>40 && relability<100,'maxrela':relability==100}"
                                    :style="`min-height:${0.37*relability}vh`">
                                <p v-show="relability>90 && relability<100" class="figure" style="margin-top:3px;">{{ relability }}</p>
                            </div>
                        </div>
                    </div>
                    <!-- <img class="relability_bar" src="/relability_bar.png" width="74px" height="410px"> -->
                    <img class="relability" src="/element/relability.png" style="width:4vw;height:6.5vh;">
                    <p class="balloon">{{ dialog }}</p>
                    <img class="customer" :src="src[customerA]" style="width:21vw;height:46vh;">   <!-- 이미지가 약 4:5 비율-->
                </div>
                <div style="display:flex;padding-top:15px;">
                    <div id="cartzone" style="width:38vw;display:flex;overflow-x:auto;">
                        <div v-show="this.tutoPage.pagenum==3" class="qchoice" v-for="(quiz,i) in quizChoice" @click="selected($event)" :id="i">
                            <div class="number" :id="i"><p>{{ i }}</p></div>
                            <p style="text-align:center;" :id="i">{{ quiz }}</p>
                        </div>
                    </div>
                    <div style="width:14vw">
                        <img class="counter_cal" :src="calsrc[calquiz]" @click="submit">
                    </div>
                </div>
                <div v-show="timeleft==0 || noclick" style="width:100%;height:330px;position:relative;bottom:330px;"></div>
            </div>
            <div class="prodconv">
                <div class="categorybutton">
                    <p @click="getcategory($event,'all')">전체</p>
                    <div></div>
                    <p @click="getcategory($event,'a')">신선식품</p>
                    <div></div>
                    <p @click="getcategory($event,'b')">즉석식품</p>
                    <div></div>
                    <p @click="getcategory($event,'c')">전자제품</p>
                </div>
                <div class="category">
                    <p style="text-align:center;font-size:2vh;">{{ categ }} 4 / 50</p>
                </div>
                <!-- dragover, drop 이벤트가 있어야 drag & drop 가능 -->
                <div id="prodzone">
                    <!-- 가지고 있는 상품 나열 -->
                    <div class="product" :id="`prod${p.orderingNo}`" v-for="p in product" v-show="p.amount>0" @click="sellprod($event)">
                        <div class="amount">
                            <p v-if="p.expDate==1" style="display:inline-block;width:3vw;height:2vh;text-align:left;font-size:2vh;">D-1</p>
                            <p v-else style="display:inline-block;width:3vw;height:2vh;text-align:right;"></p>
                            <p style="display:inline-block;width:3vw;height:2vh;text-align:right;font-size:2vh;">{{ p.orderQuantity }}</p>
                        </div>
                        <!-- draggable로 드래그 가능, dragstart 이벤트가 필요. -->
                        <img :src="`${p.src}`" alt="상품" style="width:8vh;height:8vh;" :name="p.name">
                        <p style="margin-top:1vh;font-size:1.8vh;height:1.8vh;">{{ p.name }}</p>
                        <p style="margin-top:1vh;font-size:1.8vh;height:1.8vh;">{{ p.price }}원</p>
                    </div>
                </div>
                <div v-show="modal || countermodal || timeleft==0 || noclick || quizblind || this.tutoPage.pagenum==1" class="blind">
                    <div v-if="modal && timeleft!=0 && !noclick || this.tutoPage.pagenum==1" class="modalwin">
                        <div class="modaltop">
                            <p>판매하기</p>
                            <img src="/icons/close.png" width="1.5vw;" @click="modal=false">
                        </div>
                        <div>
                            <div class="modalcontent">
                                <img :src="target.src">
                                <div style="text-align:center;">
                                    <p>{{ target.name }}</p>
                                    <p>{{ target.price }}원</p>
                                </div>
                                <div style="display:flex;justify-content:space-between;width:8vw">
                                    <p @click="target.sell>0?target.sell--:''">-</p>
                                    <p>{{ target.sell }}</p>
                                    <p @click="target.sell<target.amount?target.sell++:''">+</p>
                                </div>
                            </div>
                            <div style="display:flex;justify-content:flex-end;padding-right:1vw;">
                                <button class="okbutton" @click="moveprod"></button>
                            </div>
                        </div>
                    </div>
                    <div v-else-if="countermodal && timeleft!=0 && !noclick" class="modalwin">
                        <div class="modaltop">
                            <p>회수하기</p>
                            <img src="/icons/close.png" width="1.5vw;" @click="$emit('closemodal')">
                        </div>
                        <div>
                            <div class="modalcontent">
                                <img :src="countertarget.src">
                                <div style="text-align:center;">
                                    <p>{{ countertarget.name }}</p>
                                    <p>{{ countertarget.price }}원</p>
                                </div>
                                <div style="display:flex;justify-content:space-between;width:8vw;">
                                    <p @click="countertarget.sell>0?countertarget.sell--:''">-</p>
                                    <p>{{ countertarget.sell }}</p>
                                    <p @click="countertarget.sell<countertarget.amount?countertarget.sell++:''">+</p>
                                </div>
                            </div>
                            <div style="display:flex;justify-content:flex-end;padding-right:2vh;">
                                <button class="okbutton" @click="moveprod"></button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div v-show="tutoPage.pagenum==0" class="tuto win1"><p>손님은 매일 10명씩 방문합니다.</p></div>
    <div v-show="tutoPage.pagenum==0" class="tuto win2">
        <p>30초 안에</p>
        <p>손님이 주문한 상품을 판매해야 합니다.</p>
    </div>
    <div v-show="tutoPage.pagenum==1" class="tuto win3">
        <p>1. 해당 상품을 클릭하여 -+버튼으로</p>
        <p>수량을 맞게 선택하세요.</p>
    </div>
    <div v-show="tutoPage.pagenum==1" class="tuto win4">
        <p>2. 계산하기 버튼을 눌러</p>
        <p>계산을 완료하세요</p>
    </div>
    <div v-show="tutoPage.pagenum==2" class="tuto win5">
        <p>정확하게 판매했을 경우</p>
        <p>신뢰도가 상승합니다.</p>
        <p>실수하면 신뢰도가 감소하니 주의해주세요!</p>
    </div>
    <div v-show="tutoPage.pagenum==3" class="tuto win6">
        <p>금융과 경제생활 교과서 내용의 퀴즈입니다.</p>
        <p>정답을 맞추면 게임 혜택이 제공됩니다.</p>
    </div>
</template>
<script scoped>
import ConvenientLove from '/bgm/[suno]ConvenientLove.mp3';
import QuizmanOnConvenient from '/bgm/[suno]QuizmanOnConvenient.mp3';
import { curTutoPage } from '@/assets/pinia/tutorial';
import { quizSelect } from '@/assets/data/prodNquiz';

export default {
    data(){
        return{
            tutoPage:curTutoPage(),
            quizblind:false,
            timebar:30,
            quiztime:false,
            quizNum:Math.floor(Math.random()*10),
            timeleft:30,
            interval:'',
            customerCount:1,
            customerA:Math.floor(Math.random()*9),
            // 구매, 판매 시에는 product만 수정, 하루가 끝날 때 DB에 저장
            product:[
                { id:"strawberry",name:"딸기",amount:10,src:"/items/fresh/strawberry.png",sell:0,price:1000,type:'a', },
                { id:"pineapple",name:"파인애플",amount:3,src:"/items/fresh/pineapple.png",sell:0,price:1000,type:'b', },
                { id:"strawberry_50",name:"딸기",amount:3,src:"/items/fresh/strawberry50.png",sell:0,price:1000,type:'a', },
                { id:"pineapple_50",name:"파인애플",amount:3,src:"/items/fresh/pineapple50.png",sell:0,price:1000,type:'b', },
            ],
            // 판매 시 cart 초기화
            cart:[],
            // product -> product / cart -> cart를 막기 위한 값. 'prod','cart'
            dragtarget:'',
            countermodal:false,
            target:{ id:"strawberry",name:"딸기",amount:10,src:"/items/fresh/strawberry.png",sell:0,price:1000,type:'a', },
            countertarget:{ id:"strawberry",name:"딸기",amount:10,src:"/items/fresh/strawberry.png",sell:0,price:1000,type:'a', },
            noclick:false,
            opensettings:false,
            bgm:new Audio(ConvenientLove),
            quizbgm:new Audio(QuizmanOnConvenient),
            soundsetting:false,
            moneyhave:500000,
            manualon:false,
            src:[
                "/person/customer1.png",
                "/person/customer2.png",
                "/person/customer3.png",
                "/person/customer4.png",
                "/person/customer5.png",
                "/person/customer6.png",
                "/person/customer7.png",
                "/person/customer8.png",
                "/person/customer9.png",
                "/person/quizman.png"
            ],
            calsrc:[
                "/element/counter.png",
                "/element/quizcounter.png"
            ],
            relability:50,
            dialog:'상품이 없어요',
            quizChoice:quizSelect[8],
            calquiz:0,
        }
    },
    methods:{
        quizTime(){
            this.quiztime=true;
            this.timebar=30;
            this.timeleft=30;   // 빠른 진행 : 3초 설정
            console.log(this.customerCount);
            setTimeout(()=>{
                this.quiztime=false;
                this.timer();
            },3000);
        },
        customer(){
            this.cart=[];
            this.timebar=30;
            this.timeleft=30;   // 빠른 진행 : 3초 설정
            this.customerA=Math.floor(Math.random()*9)
            this.timer();
        },
        timer(){
            const quizstart = new Date();
            let quizend = new Date();
            this.interval = setInterval(()=>{
                if(this.timebar<=0){
                    this.timebar=0;
                    this.timeleft=0;
                    clearInterval(this.interval);
                }
                quizend = new Date();
                this.timebar = 30/30*(30-(quizend-quizstart)/1000);           // 빠른 진행 : 3초 설정
                this.timeleft = 30-Math.floor((quizend-quizstart)/1000);       // 빠른 진행 : 3초 설정
            },50)
        },
        moveprod(container,prodid){
            const prod = this.product.find(p=>p.id==prodid);
            const cartprod = this.cart.find(c=>c.id==prodid);
            if(container=='prod'){
                if(prod.sell>0){
                    if(cartprod==null){
                        this.cart.push({...prod,amount:prod.sell,sell:0});
                    }else{
                        cartprod.amount+=prod.sell;
                    }
                    prod.amount-=prod.sell;
                    prod.sell=0;
                }
            }else if(container=='cart'){
                if(cartprod.sell>0){
                    if(prod==null){
                        this.product.push({...cartprod,amount:cartprod.sell,sell:0});
                    }else{
                        prod.amount+=cartprod.sell;
                    }
                    cartprod.amount-=cartprod.sell;
                    cartprod.sell=0;
                }
                this.countermodal=false;
            }
        },
        revertprod(modal,target){
            console.log("3!");
            this.countermodal=modal;
            this.countertarget=target;
            console.log(this.countermodal);
            console.log(this.countertarget);
        },
        closemodal(){
            this.countermodal=false;
        },
        rollback(){
            this.cart.forEach(c=>{
                const prod = this.product.find(p=>p.id==c.id);
                console.log("이전 : " + prod.amount);
                prod.amount+=c.amount;
                console.log("이후 : " + prod.amount);
            })
        },
        notclick(yes){
            this.noclick=yes;
        },
        bgmstop(){
            this.bgm.pause();
            this.bgm.load();
            this.quizbgm.pause();
            this.quizbgm.load();
        },
        bgmchange(type){
            if(type=='quiz'){
                this.bgm.pause();
                this.bgm.load();
                this.quizbgm.play();
            }else if(type=='customer'){
                this.quizbgm.pause();
                this.quizbgm.load();
                this.bgm.play();
            }
        },
        closesound(){
            this.soundsetting=false;
        },
        changebgmvol(vol){
            this.bgm.volume=vol/10;
            this.quizbgm.volume=vol/10;
        },
        changeeffectvol(vol){
            // effect 생기면 추가하기
        },
        manualoff(){
            this.manualon=false;
        },
        nextPage(){
            console.log(this.tutoPage.pagenum);
            if(this.tutoPage.pagenum==1){
                this.dialog='손님이 만족했습니다. +5';
                this.tutoPage.pagenum++;
            }else if(this.tutoPage.pagenum==2){
                this.dialog='Q. 고령화 사회에서 금융 상품의 변화로 예상되는 것은?';
                this.customerA=9;
                this.calquiz=1;
                this.tutoPage.pagenum++;
            }else if(this.tutoPage.pagenum==3){
                this.tutoPage.pagenum=0;
                this.tutoPage.tutopage='main';
                this.$router.push("/linkCalculationTutorial");
            }
            else{
                this.tutoPage.pagenum++;
            }
            
        },
    },
    mounted(){
        // this.bgm.play();
        // this.bgm.loop=true;
        // this.quizbgm.loop=true;
    },
    watch:{
        '$route.params.customerCount':{
            handler:function(curVal,oriVal){
                console.log(this.customerCount);
                console.log(curVal);
                if(curVal!=null){
                    console.log("와!");
                    this.customerCount=curVal;
                    this.quizblind=false;
                }else{
                    console.log("우");
                    this.quizblind=true;
                }
            }
        }
    },
}
</script>
<style scoped>
    .maingame{
        width: 100vw;
        height: 100vh;
        min-width:100vw;
        min-height:100vh;
        background-image:url('/background/convenientbg.png');

        background-size: 100% 100%;
        background-position: center;
        overflow:hidden;
        position:fixed;

    }
    .topbar{
        display:flex;
        justify-content:space-between;
        align-items:center;
        width:90vw;
        height:8vh;
        padding:3.5vh 2vw;
        margin:auto;
        background-image:url(/element/gametopbar.png);
        background-position:center;
        background-repeat:no-repeat;
        background-size: contain;
    }
    .timebar-container{
        display:flex;
        align-items:center;
        width:33vw;
        max-height: 0.5vh;
    }
    .timerbar{
        width:30vw;
        height:3.5vh;
        border:0.4vh solid #6F3533;
        border-radius:2.5vh;
        overflow:hidden;
    }
    .timeleft{
        width:30vw;
        height:3.5vh;
        background-color:#5E395A;
    }
    .time{
        position:relative;
        right:3.5vw;
        font-size:2vh;
    }
    .moneybar{
        display:flex;
        justify-content:space-around;
        align-items:center;
        width: 15vw;
        height:5vh;
        margin-right:1vw;
        background-image:url(/element/moneybar.png);
        background-size: 100% 100%;
    }
    .line{
        min-height:2.5vh;
        border-left:0.2vw dashed rgba(256,256,256,0.2);
    }
    .money{
        display:flex;
        justify-content:flex-end;
        min-width:8.5vw;
    }
    .money p{
        color:#FFFFFF;
    }
    .product-container{
        width:95vw;
        height:90vh;
        display:flex;
        justify-content:center;
        margin:auto;
        margin-top:2vh;
    }
    .quiztime{
        width:100vw;
        height:100vh;
        position:absolute;
        display:flex;
        justify-content:center;
        align-items:center;
        z-index:10;
        background-color:rgba(0,0,0,0.3);
    }
    .settings{
        display:inline-block;
        width:19vw;
        height:28vh;
        position:relative;
        right:5vw;
        background-color:#F5F5F5;
        border:0.5vh solid #6F3533;
        border-radius:2vh;
        z-index:3;
    }
    .settingslist{
        display:flex;
        flex-direction:column;
        justify-content:space-around;
        align-items:center;
        width:19vw;
        height:28vh;
        text-align:center;
    }
    .settingslist p{
        display:flex;
        justify-content:center;
        align-items:center;
        width:10vw;
        height:6vh;
        margin:0;
        background-color:#F5F5F5;
        border:0.5vh solid #56174F;
        border-radius:2vh;
    }
    .prodconv{
        width:42.5vw;
        height:82.5vh;
        border:0.6vh solid #4C1B0B;
        border-top-left-radius:5vh;
        border-top-right-radius:5vh;
        border-bottom-left-radius:3vh;
        border-bottom-right-radius:3vh;
        overflow:hidden;
    }
    .prodconv p{
        margin:0;
    }
    .categorybutton{
        display:flex;
        justify-content:center;
        align-items:center;
        height:13vh;
        background-color:#FFEFCA;
    }
    .categorybutton p{
        font-size:3vh;
        margin:0 2vw;
    }
    .categorybutton div{
        width:0.3vw;
        height:7vh;
        background-color:#4C1B0B;
    }
    .category{
        display:flex;
        justify-content:center;
        align-items:center;
        height:4.5vh;
        border-top:0.6vh solid #4C1B0B;
        border-bottom:0.6vh solid #4C1B0B;
        background-color:#FFEFCA;
    }
    #prodzone{
        width:42.5vw;
        height:66vh;
        overflow-y:auto;
        flex-wrap:wrap;
        background-color:#4C1B0B;
        scrollbar-color:#FFEFCA #4C1B0B;    /* 브라우저에 따라 적용 안됨 🤔 */
    }
    .product{
        display:inline-block;
        text-align:center;
        width:9vw;
        height:18vh;
        background-color:white;
        border-radius:3vh;
        margin:0.9vh 0.7vw;
    }
    .amount{
        width:9vw;
        height:2vh;
        margin-top:0.5vh;
    }
    .blind{
        display:flex;
        justify-content:center;
        align-items:center;
        position:relative;
        width:42.5vw;
        height:74vh;
        bottom:74vh;
        background-color:rgba(256,256,256,0.5);
        z-index:0;
    }
    .modalwin{
        width:34vw;
        height:34vh;
        background-image:url("/element/prodmodal.png");
        background-size:100% 100%;
        border-radius:3vh;
        overflow:hidden;
    }
    .modaltop{
        display:flex;
        justify-content:center;
        align-items:center;
        width:34vw;
        height:5vh;
        background-color:#5E395A;
        margin-top:0.5vh;
    }
    .modaltop p{
        width:29vw;
        text-align:center;
    }
    .modalcontent{
        display:flex;
        height:20vh;
        justify-content:space-around;
        align-items:center;
    }
    .okbutton{
        background-image:url('/element/ok.png');
        width:9.5vw;
        height:5.5vh;
        background-color:rgba(0,0,0,0);
        border:0;
    }
    .balloon{
        display:flex;
        justify-content:center;
        align-items:center;
        width:28.5vw;
        height:22vh;
        min-height:22vh;
        background-image:url("/images/speechballoon.png"); /* 약 21:10 비율 */
        background-size: 100% 100%;
        position:relative;
        /* top:160px; */
        left:17.5vw;
        font-family:"Pretendard";
        font-size:2.5vh;
        padding-left:5vw;
        padding-right:3vw;
        z-index:0;
    }
    .customer{
        position:relative;
        right:22vw;
        top:8vh;
        z-index:-1;
    }
    .relability_bar{
        position:relative;
        top:6.5vh;
        left:0.5vw;
        width:4vw;
        height:37vh;
        border:0.3vh solid #FFFFFF;
        border-radius:2vh;
        background-color:#FFFFFF;
    }
    .bar_inside{
        display:flex;
        flex-direction:column;
        justify-content:flex-end;
        width:3vw;
        height:36.5vh;
        border:0.3vh solid #6F3533;
        border-radius:2vh;
        background-color:#F9F8F2;
        overflow:hidden;
    }
    .relability{
        position:relative;
        top:40.5vh;
        right:3vw;
    }
    .figure{
        font-size:2vh;
        margin:0;
        text-align:center;
    }
    .figure_bar{
        border-width:0.3vh 0;
        border-style:solid;
        border-color:#6F3533;
    }
    .lowrela{
        background-color:#E42200;
    }
    .rela{
        background-color:#E7C5B9;
    }
    .maxrela{
        background-color:#FFEFCA;
    }
    .fever{
        position:relative;
        top:5.5vh;
        margin:0;
        text-align:center;
    }
    .counter p{
        margin:0;
    }
    .counter_cal{
        width:11.5vw;
        height:23vh;
        position:relative;
        top:-9vh;
    }
    .cusconv{
        width:52vw;
        height:82.5vh;
    }
    .cusconv p{
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
    .tuto{
    position:absolute;
    background-color:#FFEDDE;
    font-size:2vh;
    }
    .tuto p{
    margin:1vh 2vw;
    }
    .win1{
        top:12vh;
        left:27vw;
    }
    .win2{
        bottom:20vh;
        left:10vw;
    }
    .win3{
        top:40vh;
        left:30vw;
    }
    .win4{
        top:80vh;
        left:10vw;
    }
    .win5{
        top:28vh;
        right:35vw;
        z-index:15;
    }
    .win6{
        top:10vh;
        left:20vw;
        z-index:15;
    }
    .counter p{
        margin:0;
    }
    .counter_cal{
        width:11.5vw;
        height:23vh;
        position:relative;
        top:-9vh;
    }
    .choice{
        width:200px;
        height:100px;
        min-width:200px;
        min-height:100px;
        border:3px solid #6F3533;
        border-radius:20px;
        background-color:#FFEFCA;
        margin:0 20px;
    }
    .number{
        display:flex;
        justify-content:center;
        align-items:center;
        width:28px;
        min-width:28px;
        height:28px;
        min-height:28px;
        background-color:#6F3533;
        border-radius:15px;
        margin:7px;
    }
    .number p{
        display:flex;
        justify-content:center;
        align-items:center;
        min-width:28px;
        min-height:28px;
        color:white;
    }
    /* p{
        font-family: 'Pretendard-Regular';
        font-weight:600;
        font-style: normal;
        text-align:center;
        font-size:24px;
        margin:0;
    } */
    .select{
        background-color:#F9F8F2;
    }
    .counter{
        position:relative;
        top:-100px;
    }
    .correct{
        background-color:#DBECFF;
    }
    .incorrect{
        background-color:#FFBABA;
    }
    .counter p{
        margin:0;
    }
    .counter_cal{
        width:11.5vw;
        height:23vh;
        position:relative;
        top:-9vh;
    }
    .background{
        width:52vw;
        height:53.5vh;
    }
    .blindAll{
        width:100vw;
        height:100vh;
        position:absolute;
        z-index:20;
    }
    .qchoice{
        width:200px;
        height:100px;
        min-width:200px;
        min-height:100px;
        border:3px solid #6F3533;
        border-radius:20px;
        background-color:#FFEFCA;
        margin:0 20px;
    }
</style>