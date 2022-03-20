<template>
  <div id="app">
    <!-- 标题 -->
    <div id="title">
      <TitleCard icon="ios-calendar-outline"  title="日期和数字"  type="warning" width="200px" :gens="dataList.date_number"/>
      <TitleCard icon="ios-construct-outline" title="字符生成器"  type="success" width="230px" :gens="dataList.string"/>
      <TitleCard icon="ios-construct"         title="定制生成器"  type="info"    width="340px" :gens="dataList.custom"/>
      <TitleSample />
      <TitleMeta />
    </div>

    <!-- 按钮 -->
    <div class="center">
      <CenterLine/>
    </div>

    <!-- 表格 -->
  </div>
</template>

<script>
import TitleCard from './components/TitleCard'
import TitleSample from './components/TitleSample'
import TitleMeta from './components/TitleMeta'
import CenterLine from './components/CenterLine'

export default {
  name: 'App',
  components: {
      TitleCard, TitleSample, TitleMeta, CenterLine
  },
  data () {
    return {
      dataList:{
        date_number: [],
        string:[],
        custom:[]
      },
    }
  },
  methods: {
    getGenMap() {
      this.$axios.get('getGenMap')
        .then(res => {
            this.dataList = res.data.data;
        })
    }
  },
  mounted: function () {
      this.getGenMap();
  },
}
</script>

<style lang="less">
* {
    margin: 0;
    padding: 0;
    list-style: none;
}

#title {
    height: 290px;
    display: flex;
    justify-content: space-evenly;
}

</style>
