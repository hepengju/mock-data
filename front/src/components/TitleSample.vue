<template>
  <div class="title-sample">
    <H2Title icon="md-clipboard" title="样例数据"/>
    <ul :style="{color: color}">
      <li v-for="(data,index) in arrs" :key="index">{{data}}</li>
    </ul>
  </div>
</template>

<script>
import H2Title from './H2Title'

export default {
  name: "TitleSample",
  components: {
    H2Title
  },
  data() {
    return {
      arrs: ['请鼠标划入生成器按钮', '此处就会展现样例数据'],
      color: '#75799d'
    };
  },
  mounted() {
    this.$bus.$on("hoverGen", gen => {
      this.arrs = gen.sampleData;
      this.color = gen.color
    });
  },
  beforeDestroy() {
    this.$bus.$off("hoverGen");
  },
};
</script>

<style lang="less" scoped>

.title-sample {
  width: 250px;

  li {
    margin-left: 20%;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}
</style>