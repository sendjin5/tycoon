<!-- 윤상님 이 페이지 스타일 바꿔줘요 -->
<template>
  <div v-show="savegame" class="savegame">
    <div class="popup-content" @click.stop>
        <div class="popup-header">
          <p>알림</p>
          <span class="close-button" @click="$emit('closeSaveModal');">×</span>
        </div>
        <div class="popup-body">
          <p>저장하시겠습니까?</p>
          <p>운영이 자동 종료됩니다.</p>
          <div class="savebutton">
            <button class="expansionButton" @click="saveAndQuit">네</button>
            <button class="expansionButton" @click="$emit('closeSaveModal');">아니요</button>
          </div>
        </div>
      </div>
  </div>
</template>
<script>
export default {
  data(){
    return{
      manual:[
        '/usermanual-1.png',
        '/usermanual-2.png',
        '/usermanual-3.png',
        '/usermanual-4.png',
        '/usermanual-5.png',
      ],
      page:0,
    }
  },
  methods:{
    next(){
      if(this.page<this.manual.length-1){
        this.page++;
      }
    },
    back(){
      if(this.page>0){
        this.page--;
      }
    },
    close(){
      this.page=0;
      this.$emit('manualoff');
    },
    saveAndQuit(){
      this.$router.push({
        name:'calculation',
        state:{
          gameEnd:true
        }
      });
    }
  },
  props:['manualon','savegame'],
}
</script>
<style scoped>
  .savegame{
    width:100vw;
    height:100vh;
    position:absolute;
    display:flex;
    justify-content:center;
    align-items:center;
    z-index:15;
    background-color:rgba(0,0,0,0.5);
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
  .popup-body {
    display:flex;
    flex-direction:column;
    justify-content:space-around;
    height: 332px;
    padding: 20px;
    text-align: center;
  }
  .savebutton{
    display:flex;
    justify-content:space-around;
    align-items:center;
  }
  .expansionButton {
    width: 180px;
    height: 65px;
    background-color: rgba(0, 0, 0, 0);
    border: 0;
    background-image: url("/src/assets/element/increasestorage.png");
    background-size: 100% 100%;
  }
</style>