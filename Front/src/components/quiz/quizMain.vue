<template>
    <div style="display:flex;">
        <!-- 일단 프론트엔드만 만들어놓는 중
        <p class="balloon">{{ quizDialog }}</p>
        <img class="manInBlack" src="/man_in_black.png" width="400px" height="500px"> -->
        <div class="reliability_bar">
            <div class="bar_inside">
                <p v-show="reliability==100" class="fever">FEVER<br>TIME</p>
                <p v-show="reliability<=90" class="figure">{{ reliability }}</p>
                <div class="figure_bar" :class="{'lowrela':reliability<=40,'rela':reliability>40 && reliability<100,'maxrela':reliability==100}"
                        :style="`min-height:${4*reliability}px`">
                    <p v-show="reliability>90 && reliability<100" class="figure" style="margin-top:3px;">{{ reliability }}</p>
                </div>
            </div>
        </div>
        <p class="balloon">{{ quizDialog }}</p>
        <img class="manInBlack" src="/person/quizman.png" width="400px" height="500px">
    </div>
</template>

<script>
import quizChoice from './quizChoice.vue';
import { quiz } from '../../assets/data/prodNquiz.js';

export default {
    name: 'quizcomponent',
    mounted() {
        this.$emit('quizTime');
    },
    props: ['quizNum', 'quizDialog', 'reliability'],
    emit: ['answer'],
}
</script>

<style scoped>
div {
    background-repeat: no-repeat;
}

.balloon {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 550px;
    height: 240px;
    min-height: 250px;
    background-image: url("/images/speechballoon.png"); /* 약 21:10 비율 */
    background-size: 100% 100%;
    position: relative;
    /* top: 160px; */
    left: 340px;
    font-family: "Pretendard";
    font-size: 28px;
    padding-left: 100px;
    padding-right: 60px;
    z-index: 0;
}

.manInBlack {
    position: relative;
    right: 420px;
    top: 85px;
    z-index: -1;
    background-repeat: no-repeat;
}

.reliability_bar {
    position: relative;
    top: 70px;
    left: 15px;
    width: 74px;
    height: 402px;
    border: 4px solid #FFFFFF;
    border-radius: 20px;
    background-color: #FFFFFF;
}

.bar_inside {
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    width: 60px;
    height: 394px;
    border: 3px solid #6F3533;
    border-radius: 20px;
    background-color: #F9F8F2;
    overflow: hidden;
}

.reliability {
    position: relative;
    top: 440px;
    right: 61px;
}

.figure {
    font-size: 20px;
    margin: 0;
    text-align: center;
}

.figure_bar {
    border-width: 0;
    border-top-width: 0.3vh;
    border-style: solid;
    border-color: #6F3533;
}

.lowrela {
    background-color: #E42200;
}

.rela {
    background-color: #E7C5B9;
}

.maxrela {
    background-color: #FFEFCA;
}

.fever {
    position: relative;
    top: 60px;
    margin: 0;
    text-align: center;
}
</style>