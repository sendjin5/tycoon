<!-- 윤상님 이 페이지 스타일 바꿔줘요 -->
<template>
  <div v-show="manualon" class="usermanual" :style="`background-image:url('${manual[page]}');background-size:100% 100%;`">
    <div class="smallbutton">
      <button class="backbtn" @click.prevent="back"/>
    </div>
    <div class="smallbutton right">
      <button class="closebtn" @click.prevent="close"/>
      <button class="nextbtn" @click.prevent="next"/>
    </div>
  </div>
</template>
<script>
export default {
  data(){
    return{
      manual:[
        '/manual/usermanual-1.png',
        '/manual/usermanual-2.png',
        '/manual/usermanual-3.png',
        '/manual/usermanual-4.png',
        '/manual/usermanual-5.png',
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
    }
  },
  props:['manualon'],
}
</script>
<style scoped>
  .usermanual{
    display:flex;
    width:100vw;
    height:100vh;
    overflow:hidden;
  }
  .smallbutton{
    display:flex;
    align-items:center;
    width:50vw;
    height:100vh;
  }
  .right{
    justify-content:flex-end;
  }
  .smallbutton>button{
    width:3vw;
    height:5.5vh;
    background-color:rgba(0,0,0,0);
    border:0;
  }
  .closebtn{
    position:relative;
    bottom:43vh;
    right:0.2vw;
  }
  .nextbtn{
    position:relative;
    right:2vw;
  }
  .backbtn{
    position:relative;
    left:3.3vw;
  }
</style>