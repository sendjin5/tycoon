<template>
  <div>
      <router-view></router-view>
  </div>
</template>
<script>

export default {
  name:"RouterApp",

}
</script>
<style>
  *{
      user-select:none;
  }
  body,html {
    margin: 0;
    padding: 0;
  }

</style>