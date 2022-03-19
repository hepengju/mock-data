<template>
  <div class="gen-card" :style="{ width: width }">
    <H2Title :icon="icon" :title="computedTitle" />

    <!-- 注意此处使用mouseenter, 而不是mourseover(这个在按钮内部滑动时会触发多次(有子元素)) -->
    <ul>
      <li v-for="gen in gens" :key="gen.name">
        <Button :type="type" @click="genClick(gen)" @mouseenter.native="hoverGen(gen, $event)">{{ gen.columnTitle }}</Button>
      </li>
    </ul>
  </div>
</template>

<script>
import H2Title from "./H2Title";

export default {
  name: "GenCard",
  components: {
    H2Title,
  },
  props: {
    title: {
      type: String,
      default: "默认标题",
    },
    icon: {
      type: String,
      default: "ios-car",
    },
    type: {
      type: String,
      default: "info",
    },
    width: {
      type: String,
      default: "200px",
    },
    gens: {
      tyep: Array,
      required: true,
    },
  },
  computed: {
    computedTitle() {
      return this.title + (this.gens instanceof Array ? "(" + this.gens.length + ")" : "");
    },
  },
  methods: {
    genClick(gen) {
      this.$bus.$emit("clickGen", gen);
    },
    hoverGen(gen, e) {
      let color = "black";
      try {
        color = window.getComputedStyle(e.target, null).backgroundColor;
      } catch (e) {}
      this.$bus.$emit("hoverGen", gen, color);
    },
  },
};
</script>

<style lang="less" scoped>
.gen-card {
  &:hover {
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
  }

  ul {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-evenly;
  }

  button {
    width: 100px;
    margin: 5px;
  }
}
</style>
