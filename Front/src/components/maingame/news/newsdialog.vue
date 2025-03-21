<template>
  <div class="newsdialog">
    <h1>{{ dialog }}</h1>
    <img src="/icons/nextdialog.png">
  </div>
</template>
<script>
export default {
  data(){
    return{
      dialInterval:'',
      dialog:'',
      // 일수마다 news dialog 가져오기
      dialex:[
        '어느 대학을 갈지, 어떤 직업을 선택할지 고민해 본 적 있나요? 금융에서도 비슷한 고민이 필요합니다.',
        '기회비용(Opportunity Cost)이란 한 가지 선택을 했을 때 포기해야 하는 다른 선택의 가치를 의미합니다.',
        '예를 들어 100만 원을 정기예금에 넣으면 안전하지만 같은 돈을 주식에 투자하면 더 큰 이익을 얻을 수도 있습니다.',
      ],
      newsdialnum:0,
      dialpoint:0,
    }
  },
  methods:{
    printdialog(){
      this.dialInterval = setInterval(()=>{
        if(this.dialpoint<this.dialex[this.newsdialnum].length){
          console.log(":)");
          this.dialog+=this.dialex[this.newsdialnum][this.dialpoint];
        }else{
          clearInterval(this.dialInterval);
        }
        this.dialpoint++;
      },50);
    }
  },
  mounted(){
    console.log("하이");
    this.printdialog();
  },
  props:['newsCount'],
  watch:{
    newsCount(curVal,oriVal){
      console.log("헉 클릭함!");
      if(oriVal<this.dialex.length){
        if(curVal>oriVal && this.dialog!=this.dialex[oriVal]){
          this.dialog=this.dialex[oriVal];
          this.dialpoint=this.dialex[oriVal].length;
          this.$emit('notnextdial');
        }else if(curVal>oriVal && this.dialog==this.dialex[oriVal] && curVal<this.dialex.length){
          this.dialog='';
          this.newsdialnum=curVal;
          this.dialpoint=0;
          this.printdialog();
        }else if(curVal<oriVal){}
        else{
          this.$router.push('/mainMenu');
        }
      }
    }
  }
}
</script>
<style>
  .newsdialog{
    display:flex;
    justify-content:space-around;
    width:97.2vw;
    height:27vh;
    margin:auto;
    background-color:#F9F8F2;
    border-top:0.5vh solid #6F3533;
    border-bottom-left-radius:3vw;
    border-bottom-right-radius:3vw;
  }
  .newsdialog>h1{
    min-width:83.5vw;
    max-width:83.5vw;
    font-size:4.5vh;
    margin-top:2.5vh;
    margin-left:2.5vh;
    margin-right:2.5vh;
  }
  .newsdialog>img{
    width:3.5vh;
    height:3.5vh;
    margin-top:10vh;
    margin-right:2.5vh;
  }
</style>