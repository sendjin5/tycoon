<template>
    <div class="counter" style="display:flex;padding-top:1.5vw;min-width:90vw;">
        <div id="cartzone" style="width:38vw;display:flex;overflow-x:auto;">
            <!-- 카트의 상품 나열 -->
            <div v-for="c in cart.cart" v-show="c.orderQuantity>0" @click="revertprod($event)">
                <div style="display:flex;justify-content:flex-end;align-items:center;"><p style="color:white;background-color:black;border-radius:1vh;min-width:1vw;min-height:2vh;font-size:2vh;">{{ c.orderQuantity }}</p></div>
                <img class="cart" :src="`${c.image}`" alt="상품" style="width:10vh;height:10vh;" :id="`cart${c.goodsNo}${c.expDate==1?'_50':''}`" :name="c.goodsName"><br>
                <p style="width:4vw;height:2vh;margin-top:1.5vh;font-size:2vh;">{{ c.goodsName }}</p>
            </div>
        </div>
        <div style="width:14vw">
            <img class="counter_cal" src="/element/counter.png" @click="submit">
        </div>
    </div>
    <div v-show="timeleft==0 || noclick" style="width:100vw;height:30.5vh;position:relative;bottom:30.5vh;"></div>
</template>
<script>
import { productStore } from '@/assets/pinia/maingame';

export default {
    data(){
        return{
            cart:productStore(),
        }
    },
    methods:{
        revertprod(e){
            let prod;
            if(e.target.className=='cart'){
                prod=e.target;
            }else if(e.target.parentElement.className=='cart'){
                prod=e.target.parentElement;
            }

            if(prod!=null){
                const target={...this.cart.cart.find(c=>("cart"+c.goodsNo+(c.expDate==1?'_50':''))==(prod.id)),sell:0};
                target.sell=target.orderQuantity;
                console.log("1!");
                this.$emit('revertprod',true,target);
            }
        },
        submit(){
            clearInterval(this.interval);
            this.$emit('submit');
        }
    },
    props:['interval','timeleft','noclick']
}
</script>
<style scoped>
    .counter p{
        margin:0;
    }
    #cartzone>div{
        display:inline-block;
        text-align:center;
        width:4vw;
        min-width:4vw;
        height:10.5vh;
        min-height:10.5vh;
        margin:1vh 0.5vw;
    }
    .counter_cal{
        width:11.5vw;
        height:23vh;
        position:relative;
        top:-9vh;
    }
</style>