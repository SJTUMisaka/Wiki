<template>
  <a-layout id="components-layout-demo-top-side-2">
    <the-header></the-header>
    <router-view/>
    <the-footer></the-footer>
  </a-layout>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import TheHeader from '@/components/the-header.vue'; // @ is an alias to /src
  import TheFooter from '@/components/the-footer.vue'; // @ is an alias to /src
  export default defineComponent({
    name: 'app',
    components: {
      TheHeader,
      TheFooter,
    },
  });
</script>
<style>
  #components-layout-demo-top-side-2 .logo {
    float: left;
    width: 120px;
    height: 31px;
    margin: 16px 24px 16px 0;
    background: rgba(255, 255, 255, 0.2);
  }
</style>
