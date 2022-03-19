<template>
  <div class="gen-title">
    <h2>
      <Icon :type="icon" />
      {{ title }}
    </h2>
    <hr />
  </div>
</template>

<script>
export default {
  name: "H2Title",
  props: ["title", "icon"]
};
</script>

<style scoped>
h2 {
  font-size: 16px;
  text-align: center;
}

hr {
  margin: 10px 0px;
  background-color: #e8eaec;
  height: 2px;
  border: none;
}
</style>