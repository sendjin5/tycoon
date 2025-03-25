<template>
    <div style="display:flex;padding-top:15px;">
        <div class="choice" :class="quizClass[i-1]" v-for="(quiz,i) in quizChoice" @click="selected($event)" :id="i">
            <div class="number" :id="i"><p>{{ i }}</p></div>
            <p style="text-align:center;" :id="i">{{ quiz }}</p>
        </div>
        <img class="counter" src="/element/quizcounter.png" @click="submit">
    </div>
    <div v-show="timeleft==0 || noclick" style="width:100%;height:330px;position:relative;bottom:330px;"></div>
</template>
<script>
import { quizAnswer, quizSelect } from '../../assets/data/prodNquiz.js';

export default {
    data(){
        return{
            quizChoice:quizSelect[this.quizNum],
            quizClass:[
                {'select':false,'correct':false,'incorrect':false},
                {'select':false,'correct':false,'incorrect':false},
                {'select':false,'correct':false,'incorrect':false},
            ],
            submitAnswer:false,
        }
    },
    methods:{
        selected(e){
            if(!this.submitAnswer){
                if(this.quizClass[e.target.id-1].select==true){
                    this.quizClass[e.target.id -1].select=false;
                }else{
                    for(let i=0;i<this.quizClass.length;i++){
                        this.quizClass[i].select=false;
                    }
                    this.quizClass[e.target.id -1].select=true;
                }
            }
        },
        submit(){
            if(!this.submitAnswer){
                let answer = 0;
                // 시간초과 로직 나중에 추가하기. 여기 말고 다른데 추가해야 할 수도 있음.
                for(let i=0;i<this.quizClass.length;i++){
                    if(this.quizClass[i].select==true){
                        answer = i+1;
                        i = this.quizClass.length;     // break 안쓰기(?)
                    }
                }
                if(answer!=0){
                    if(quizAnswer[this.quizNum]==answer){
                        this.quizClass[answer-1].select=false;
                        this.quizClass[answer-1].correct=true;
                    }else{
                        this.quizClass[answer-1].select=false;
                        this.quizClass[answer-1].incorrect=true;
                    }
                    this.$emit('result',answer);

                    this.submitAnswer=true;

                    setTimeout(()=>{
                        this.quizClass[answer-1].select=false;
                        this.quizClass[answer-1].correct=false;
                        this.quizClass[answer-1].incorrect=false;
                        this.submitAnswer=false;
                    },7500);
                }
            }
        }
    },
    props:['quizNum','timeleft','noclick'],
}
</script>
<style scoped>
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
    p{
        font-family: 'Pretendard-Regular';
        font-weight:600;
        font-style: normal;
        text-align:center;
        font-size:24px;
        margin:0;
    }
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
</style>