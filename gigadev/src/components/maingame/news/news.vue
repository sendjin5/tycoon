<template>
  <div class="news-background" @click="nextdialog">
    <div class="news-page">
      <NewsMain :day="day"/>
      <NewsImage/>
    </div>
    <NewsDialog :newsCount="newsCount" @notnextdial="notnextdial"/>
  </div>
</template>
<script>
import NewsMain from './newsman.vue';
import NewsImage from './newsimage.vue';
import NewsDialog from './newsdialog.vue';

export default {
  data(){
    return{
      day:1,
      newsCount:0,
    }
  },
  methods:{
    nextdialog(){
      console.log("클릭함");
      this.newsCount++;
      console.log(this.newsCount);
    },
    notnextdial(){
      this.newsCount--;
    }
  },
  components:{ NewsMain,NewsImage,NewsDialog }
}
</script>
<style scoped>
  .news-background{
    width:100vw;
    height:100vh;
    background-image:url("/src/assets/background/yellowbg.jpg");
    /* background-size:contain; */
    background-size:100% 100%;
    background-repeat:no-repeat;
  }
  .news-page{
    display:flex;
    width:100vw;
    height:70vh;
  }
</style>