<!-- 윤상님 이 페이지 스타일 바꿔줘요 -->
<template>
    <div v-show="soundsetting" class="settings-container"> <!-- v-show=soundsetting으로 바꾸기 -->
        <div class="settings-modal">
            <div style="width:90%;text-align:right;">
                <img src="@/assets/icons/purpleclose.png" @click="$emit('closesound');">
            </div>
            <div class="sound-settings">
                <div  style="display:flex;justify-content:space-around;align-items:center;width:16vw;">
                    <img src="@/assets/icons/bgm.png" alt="bgm">
                    <div style="height:100%;display:flex;align-items:center;">
                        <input id="bgm" type="range" max="10" step="1" class="sound-bar" :value="bgmsound" @change="changevol($event)">
                    </div>
                    <p>{{ bgmsound }}</p>
                </div>
                <div style="display:flex;justify-content:space-around;align-items:center;width:16vw;">
                    <img src="@/assets/icons/effect.png" alt="sound">
                    <div style="height:100%;display:flex;align-items:center;">
                        <input id="effect" type="range" max="10" step="1" class="sound-bar" :value="effectsound" @change="changevol($event)">
                    </div>
                    <p>{{ effectsound }}</p>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    data(){
        return{
            bgmsound:10,
            effectsound:10,
            dragid:'',
        }
    },
    methods:{
        changevol(e){
            if(e.target.id=='bgm'){
                this.bgmsound=Math.round(e.target.value);
                this.$emit('changebgmvol',this.bgmsound);
            }else if(e.target.id=='effect'){
                this.effectsound=Math.round(e.target.value);
                this.$emit('changeeffectvol',this.effectsound);
            }
        }
    },
    props:['soundsetting'],
}
</script>
<style scoped>
    .settings-container{
        width:100vw;
        height:100vh;
        position:absolute;
        display:flex;
        justify-content:center;
        align-items:center;
        z-index:15;
        background-color:rgba(0,0,0,0.5);
    }
    .settings-modal{
        width:20vw;
        height:33vh;
        background-color:white;
        border:7px solid #56174F;
        border-radius:20px;
        display:flex;
        flex-direction:column;
        justify-content:space-around;
        align-items:center;
    }
    .sound-settings{
        width:16vw;
        height:20vh;
        background-color:#F0EBE9;
        border-radius:20px;
        display:flex;
        flex-direction:column;
        justify-content:space-around;
        align-items:center;
    }
    .sound-bar{
        appearance:unset;
        width:10vw;
        height:1vh;
        background-color:#56174F;
        border-radius:5px;
    }
    .sound-bar::-webkit-slider-thumb{
        appearance:unset;
        width:2vh;
        height:2vh;
        background-image:url("@/assets/element/soundcircle.png");
        background-size:100% 100%;
    }
</style>