<template>
  <div class="newsman">
    <h3>DAY-{{ day }} NEWS</h3>
    <img src="/person/newsman.png">
  </div>
</template>
<script>
export default {
  props:['day',]
}
</script>
<style scoped>
  .newsman{
    width:50vw;
    height:70vh;
  }
  .newsman>h3{
    display:flex;
    align-items:center;
    width:25vw;
    height:25vh;
    font-size:5vh;
    margin:0;
    margin-left:5vw;
  }
  .newsman>img{
    width:45vh;
    height:45vh;
    margin-left:5vw;
  }
</style>