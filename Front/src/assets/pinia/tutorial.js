import { defineStore } from 'pinia';

export const curTutoPage = defineStore('tutorial',{
    state:()=>({
        tutopage:'main',
        pagenum:0,
    })
})