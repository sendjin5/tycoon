<template>
  <div class="newsimage">
    <img src="/images/stockinvestment.png">
  </div>
</template>
<script>
export default {
  // 일수마다 news image 가져오기
}
</script>
<style scoped>
  .newsimage{
    display:flex;
    align-items:center;
    width:50vw;
    height:70vh;
  }
  .newsimage>img{
    width:35vh;
    height:35vh;
    margin-left:10vw;
  }
</style>