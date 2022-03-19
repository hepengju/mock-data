<template>
  <div class="gen-sample">
    <H2Title icon="md-clipboard" title="样例数据"/>
    <ul :style="{color: color}">
      <li v-for="(data,index) in arrs" :key="index">{{data}}</li>
    </ul>
  </div>
</template>

<script>
import H2Title from './H2Title'

export default {
  name: "GenSample",
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
    this.$bus.$on("hoverGen", (gen, color) => {
      this.arrs = gen.sampleData;
      this.color = color
    });
  },
  beforeDestroy() {
    this.$bus.$off("hoverGen");
  },
};
</script>

<style lang="less">

.gen-sample {
  width: 250px;
  // background-color: tomato;

  &:hover {
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
  }

  li {
    margin-left: 20%;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}
</style>