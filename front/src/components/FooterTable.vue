<template>
  <div class="footer-table">
    <div class="prompt" v-if="showHint">
        <span>请选择生成器生成所需数据...</span>
    </div>
    <div class="content" v-else>
        <Table :columns="columns" :data="data" size="small" stripe border></Table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FooterTable',
  data(){
    return {
      showHint: true,
      columns: [],
      data: []
    }
  }
}
</script>

<style>

</style>