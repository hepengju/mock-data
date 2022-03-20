<template>
  <div class="footer-table">
    <span  class="prompt" v-if="columns.length == 0">请点击上方生成器，生成所需模拟数据...</span>
    <Table class="table"  v-else :columns="columns" :data="rows" stripe border :max-height="360"></Table>
  </div>
</template>

<script>
import { nanoid } from "nanoid";
import { getData } from "@/apis";

export default {
  name: "FooterTable",
  data() {
    return {
      columns: [],
      rows: [{},{},{},{},{},   {},{},{},{},{}],
    };
  },

  methods: {
    addGen(gen) {
      getData(gen.name).then((sampleList) => {
        const key = nanoid()
        // 标题
        this.columns.push({
          key: key,
          title: gen.columnTitle,
          minWidth: 100,
          ellipsis: true
        })

        // 数据
        for (let i = 0; i < 10; i++) {
          this.rows[i][key] = sampleList[i]
        }

      });
    },
  },

  mounted() {
    this.$bus.$on("addGen", this.addGen);
  },
  beforeDestroy(){
    this.$bus.$off("addGen")
  }
};
</script>

<style lang="less">
.footer-table {

  .prompt {
    display: block;
    text-align: center;
    padding-top: 100px;
    color: #aaaaaa;
    font-size: 18px;
    font-style: italic;
  }

  .table {
    width: 100%;

    // 上面加入scoped则不生效!! 不知为何, 优先级? 
    .ivu-table td{
      height: 32px;
    }
  }

}
</style>