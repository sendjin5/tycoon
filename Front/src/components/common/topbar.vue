<template>
<div style="height:10vh;overflow:visible;text-align:right;">
    <div class="topbar">    <!-- 시간 될 때 떼어내서 common에 넣어놓기 -->
        <p style="font-size:2vh; font-family: prebold;">D-{{ 30-revenue.salesDay+1 }}</p>
        <div v-if="gamestart" style="display:flex;">
            <!-- <img src="@/assets/timer.png" width="10" height="52"> -- -->
            <div class="timebar-container">
                <div class="timerbar">
                    <div v-show="timebar>0" class="timeleft" :style="`width:${timebar}vw`"></div>
                </div>
                <p class="time">{{ timeleft }}초</p>
            </div>
        </div>
        <div v-if="gamestart" style="display:flex;align-items:center;">
            <img src="/icons/person.png" style="width:2vw;height:3.5vh;">
            <p style="margin:0;font-size:2vh;">{{ customerCount }}/10</p>
        </div>
        <div style="display:flex;align-items:center;">
            <div class="moneybar">
                <img src="/icons/money.png" style="width:2.5vw;height:4.5vh;">
                <div class="line"></div>
                <div class="money"><p style="font-size:2vh;">{{ (revenue.cash*1).toLocaleString() }}원</p></div>
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
</template>
<script>
import { revenueStore } from '@/assets/pinia/maingame';

export default {
  data(){
    return{
      gamestart:false,
      moneyhave:500000,
      opensettings:false,
      revenue:revenueStore(),
    }
  }
}
</script>
<style scoped>
@font-face {
  font-family: 'prebold';
  src: url('/fonts/Pretendard-bold.woff') format('woff');
}
@font-face {
  font-family: 'rk';
  src: url('/fonts/Recipekorea-FONT.ttf') format('truetype');
}
  .topbar{
      display:flex;
      justify-content:space-between;
      align-items:center;
      width:90vw;
      height:8vh;
      padding:3.5vh 2vw;
      /* margin:auto; */
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
    .settings{
        display:inline-block;
        width:14vw;
        height:28vh;
        position:relative;
        background-color:#F9F8F2;
        border:0.7vh solid #6F3533;
        border-radius:2vh;
        z-index:3;
    }
    .settingslist{
        display:flex;
        flex-direction:column;
        justify-content:space-around;
        align-items:center;
        width:14vw;
        height:28vh;
        text-align:center;
    }
    .settingslist p{
        font-family: rk;
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
</style>