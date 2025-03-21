<template>
    <div id="layContainer">
        <section>
            <article>
                <slot name="article">article내용</slot>
            </article>
        </section>
    </div>
</template>
<script>

const model ={
  style:{
      main:{
  
      }
  }
}
export default {
    name:"layoutComponent"
    ,data(){
        return model;
    }
}
</script>
<style scoped>
    header{
        width:100%;
        display: flex;
        height:80px;
        background-color: lightcoral;
        align-items: center;
        
    }
    section{
        width:100%;
        height:500px;
        display: flex;
        background-color: lightgray;
    }

    section>article{
        width:100%;
        background-color: lightblue;
        background-image: url('@/assets/background/convenientbg.png');
    }
    footer{
        width:100%;
        height:80px;
        background-color: lightgoldenrodyellow;
        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>