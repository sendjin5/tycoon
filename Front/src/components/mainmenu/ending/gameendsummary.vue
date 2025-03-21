<template>
    <div class="main-container">
        
        <div @click="quickStart">
            <img class="skip-button" src="/tutorial/button/skip.png">
        </div>
    
        <div id="bankmanbox">
            <img id="bankman" src="/person/introwoman.png">
        </div>
        <div class="dialog-box">
            <div class="dialog-text">{{ inputText }}</div>
            <div class="dialog-arrow"  @click="movePage">▼</div>
        </div>
    
    </div>
</template>

<script>
import { revenueStore } from '@/assets/pinia/maingame';

export default {
    name: 'ConvenienceStoreDialog'
    ,data(){
    return {
        revenue:revenueStore(),
        inputText:''
    }
    },methods:{
    quickStart(){
        this.$router.push('/mainMenu')
    },
    movePage(){
        if(this.revenue.cash>=3500000){
            this.$router.push('/superrichending1')
        }else if(this.revenue.cash>=2500000 && this.revenue.cash<3500000){
            this.$router.push('/richending1')
        }else if(this.revenue.cash>=0 && this.revenue.cash<2500000){
            this.$router.push('/nomalending1')
        }else if(this.revenue.cash<0){
            this.$router.push('/poorending1')
        }
    }
    }
    ,mounted(){
    
    
    
        let innerText = `오늘은 빌리신 대출금을 상환해야 하는 날입니다! 준비 되셨나요?`;
        
        for(let i =0;i<innerText.length;i++){
            setTimeout(()=>{
            this.inputText+=innerText[i];
            },i*50)
        }
    
    }

}
</script>

<style scoped>
@font-face {
  font-family: 'prelight';
  src: url('/fonts/Pretendard-Light.woff') format('woff');
}
    .main-container {
        width: 100%;
        height: 100vh;
        position: relative;
        overflow: hidden;
        
    
    
        font-family: pretendard;
        background-color: #f5f5f5;
        
        text-align: center;
        max-width: 100%;
        min-height: 90%;
    
    
        display: flex;
        flex-direction: column;
        justify-content: center; 
        align-items: center; 
    
        background-image: url('/tutorial/tutorialMain.png');
        background-size: 100% 100%;
    
    }

    .skip-button {
        position: absolute;
        top: 16vh;
        right: 4vw;
        width: 3vw;
    }

    .dialog-box {
        position: absolute;
        margin-top: 68.5vh;
        width: 70%;
        background-color: #F2F1EC;
        border-radius: 2vw;
        padding: 1vw 1.5vw;
        margin-bottom: 5vh;
        height: 20vh; 
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 0 1vw 2vw rgba(0, 0, 0, 0.1);
    }

    .dialog-text {
        font-family: prelight;
        text-align: left;
        font-size: 1.8vw;
        margin: 0;
        flex-grow: 1;
        color: #333;
        font-family: RecipekoreaOTF;
    }

    .dialog-arrow {
        font-size: 1.6vw;
        bottom: 8px;
        right: 16px;
        color: #666;
    }

    .bottom-indicator {
        position: absolute;
        bottom: 10px;
        color: #333;
        font-size: 18px;
    }
    #bankmanbox {
        display: flex;
        justify-content: center;
        align-items: center;
    }
    #bankman {
        position: absolute;
        width: calc(35%);
        width: 17vw;
        height: 55%;
    }
</style>