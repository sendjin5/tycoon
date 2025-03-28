<template>
  <div style="display: flex">
    <div class="reliability_bar">
      <div class="bar_inside">
        <p v-show="reliability == 100" class="fever">FEVER<br />TIME</p>
        <p v-show="reliability <= 90" class="figure">{{ reliability }}</p>
        <div
          class="figure_bar"
          :class="{
            lowrela: reliability <= 40,
            rela: reliability > 40 && reliability < 100,
            maxrela: reliability == 100,
          }"
          :style="`min-height:${0.37 * reliability}vh`"
        >
          <p v-show="reliability > 90 && reliability < 100" class="figure" style="margin-top: 3px">
            {{ reliability }}
          </p>
        </div>
      </div>
    </div>
    <p class="balloon">{{ dialog }}</p>
    <img class="customer" :src="src[customerA]" />
    <!-- 이미지가 약 4:5 비율-->
  </div>
</template>
<script>
export default {
  name: "quizcomponent",
  data() {
    return {
      src: [
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
    };
  },
  mounted() {
    this.$emit("customer");

    // for(let i=0;i<this.dialog.length;i++){
    //     setTimeout(()=>{
    //         inputData+=this.dialog[i];
    //     },i*100);
    //
  },
  props: ["quizNum", "dialog", "customerA", "reliability"],
  emit: ["answer"],
};
</script>
<style scoped>
.balloon {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 20%;
  height: 100%;
  top: -22%;
  left: 32%;
  font-size: 100%;
  z-index: 0;
  margin: 0;
  padding-left: 20px;
  font-family: "Pretendard";
  background-image: url(/images/speechballoon.png);
  background-repeat: no-repeat;
  background-size: contain;
  background-position: center;
  position: absolute;
}

.customer {
  position: absolute;
  left: 14%;
  bottom: 20%;
  z-index: -1;
  width: 400px;
  height: 500px;
}
@media (min-width: 1930px) {
  .balloon {
    width: 25%;
    left: 28%;
    font-size: 150%;
  }
  .customer {
    width: 500px;
    height: 600px;
  }
}
.reliability_bar {
  position: relative;
  top: 6.5vh;
  left: 0.5vw;
  width: 4vw;
  height: 37vh;
  border-radius: 2vh;
  background-color: transparent;
}
.bar_inside {
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  width: 3vw;
  height: 36.5vh;
  border: 0.3vh solid #6f3533;
  border-radius: 2vh;
  background-color: #f9f8f2;
  overflow: hidden;
}
.reliability {
  position: relative;
  top: 40.5vh;
  right: 3vw;
}
.figure {
  font-size: 2vh;
  margin: 0;
  text-align: center;
}
.figure_bar {
  border-width: 0;
  border-top-width: 0.3vh;
  border-style: solid;
  border-color: #6f3533;
}
.lowrela {
  background-color: #e42200;
}
.rela {
  background-color: #e7c5b9;
}
.maxrela {
  background-color: #ffefca;
}
.fever {
  position: relative;
  top: 5.5vh;
  margin: 0;
  text-align: center;
}
</style>
