<template>
  <div class="title-card" :style="{ width: width }">
    <H2Title :icon="icon" :title="computedTitle" />

    <!-- 注意此处使用mouseenter, 而不是mourseover(这个在按钮内部滑动时会触发多次(有子元素)) -->
    <ul>
      <li v-for="gen in gens" :key="gen.name">
        <Button :style="{backgroundColor: gen.color, color: '#fff'}" @click="addColumn(gen)" @mouseenter.native="hoverGen(gen)">
          {{ gen.columnTitle }}
        </Button>
      </li>
    </ul>
  </div>
</template>

<script>
import H2Title from "./H2Title";

export default {
  name: "TitleCard",
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
    }
  },
  methods: {
    addColumn(gen) {
      this.$bus.$emit("addColumn", gen);
    },
    hoverGen(gen) {
      this.$bus.$emit("hoverGen", gen);
    },
  },
};
</script>

<style lang="less" scoped>
.title-card {

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
