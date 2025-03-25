<template>
    <div style="display:flex;">
        <div class="reliability_bar">
            <div class="bar_inside">
                <p v-show="reliability==100" class="fever">FEVER<br>TIME</p>
                <p v-show="reliability<=90" class="figure">{{ reliability }}</p>
                <div class="figure_bar" :class="{'lowrela':reliability<=40,'rela':reliability>40 && reliability<100,'maxrela':reliability==100}"
                        :style="`min-height:${0.37*reliability}vh`">
                    <p v-show="reliability>90 && reliability<100" class="figure" style="margin-top:3px;">{{ reliability }}</p>
                </div>
            </div>
        </div>
        <p class="balloon">{{ dialog }}</p>
        <img class="customer" :src="src[customerA]" style="width:21vw;height:46vh;">   <!-- 이미지가 약 4:5 비율-->
    </div>
</template>
<script>

export default {
    
    name:'quizcomponent',
    data(){
        return{
            src:[
                "/person/customer1.png",
                "/person/customer2.png",
                "/person/customer3.png",
                "/person/customer4.png",
                "/person/customer5.png",
                "/person/customer6.png",
                "/person/customer7.png",
                "/person/customer8.png",
                "/person/customer9.png",
            ],
        }
    }
    ,mounted(){

        this.$emit('customer');

        // for(let i=0;i<this.dialog.length;i++){
        //     setTimeout(()=>{
        //         inputData+=this.dialog[i];
        //     },i*100);
        //



    },
    props:['quizNum','dialog','customerA','reliability'],
    emit:['answer'],
}
</script>
<style scoped>
    .balloon{
        display:flex;
        justify-content:center;
        align-items:center;
        width:28.5vw;
        height:22vh;
        min-height:22vh;
        background-image:url("/images/speechballoon.png"); /* 약 21:10 비율 */
        background-size: 100% 100%;
        position:relative;
        /* top:160px; */
        left:17.5vw;
        font-family:"Pretendard";
        font-size:2.5vh;
        padding-left:5vw;
        padding-right:3vw;
        z-index:0;
    }
    .customer{
        position:relative;
        right:22vw;
        top:8vh;
        z-index:-1;
    }
    .reliability_bar{
        position:relative;
        top:6.5vh;
        left:0.5vw;
        width:4vw;
        height:37vh;
        border:0.3vh solid #FFFFFF;
        border-radius:2vh;
        background-color:#FFFFFF;
    }
    .bar_inside{
        display:flex;
        flex-direction:column;
        justify-content:flex-end;
        width:3vw;
        height:36.5vh;
        border:0.3vh solid #6F3533;
        border-radius:2vh;
        background-color:#F9F8F2;
        overflow:hidden;
    }
    .reliability{
        position:relative;
        top:40.5vh;
        right:3vw;
    }
    .figure{
        font-size:2vh;
        margin:0;
        text-align:center;
    }
    .figure_bar{
        border-width:0;
        border-top-width:0.3vh;
        border-style:solid;
        border-color:#6F3533;
    }
    .lowrela{
        background-color:#E42200;
    }
    .rela{
        background-color:#E7C5B9;
    }
    .maxrela{
        background-color:#FFEFCA;
    }
    .fever{
        position:relative;
        top:5.5vh;
        margin:0;
        text-align:center;
    }
</style>