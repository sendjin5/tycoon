<template>
    <div>
        <ul>
            <li v-for="link in links" :key="link.link">
                <router-link :to="link.link">
                    {{ link.label }}
                </router-link>
            </li>
        </ul>
        
        <body>
            <div>
                <!-- <img :src="defaultImg"> -->

            </div>
        </body>

    </div>
</template>
<script>




const model={
    links:[
        {label:"메인화면",link:"/"},
        {label:"상품창고발주",link:"/product"},
        {label:"퀴즈",link:"/quiz"},
        {label:"로그인회원가입",link:"/main"},
        {label:"은행",link:"/bank"},
        {label:"튜토리얼",link:"/tutorial"},
        
    ],
    historyNum : 1,
  
}
export default {
    name:"NavComponent",
    data(){
        return model;
    },
    methods:{
        moveForward(){
            this.$router.forward();
        },
        moveBack(){
            this.$router.back();
        },
        moveGo(){
            console.log(window.history);
            console.log(this.$router.options.history.state);
            this.$router.go(this.historyNum);
        },
        movePush(e){
            const path=e.target.value;
            this.$router.push(path);
        }
    }
}
</script>
<style>
  
</style>