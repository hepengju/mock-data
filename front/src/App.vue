<template>
  <div id="app">
    <!-- 标题 -->
    <div id="title">
      <TitleCard icon="ios-calendar-outline"  title="日期和数字"                 width="200px" :gens="dataList.date_number"/>
      <TitleCard icon="ios-construct-outline" title="字符生成器"  type="success" width="230px" :gens="dataList.string"/>
      <TitleCard icon="ios-construct"         title="定制生成器"  type="info"    width="340px" :gens="dataList.custom"/>
      <TitleSample />
      <TitleMeta />
    </div>

    <!-- 按钮 -->
    <div id="center">
      <CenterLine/>
    </div>

    <!-- 表格 -->
    <div id="footer">

    </div>
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

            // 添加生成器的颜色
            for (const genType in this.dataList) {
              this.dataList[genType].forEach(gen => {
                if      (genType == 'string') gen.color = '#19be6b' // 字符: 按钮 success 的颜色
                else if (genType == 'custom') gen.color = '#2db7f5' // 定制化: 按钮 info 的颜色
                else if (gen.name.startsWith('date')) gen.color = '#f16643' // 日期: 按钮 error 的颜色
                else gen.color = '#ff9900' // 数字: 按钮 warning 的颜色
              });
            }
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
  overflow: hidden;
  display: flex;
  justify-content: space-evenly;
}

#center {
  height: 33px;
  overflow: hidden;
}
</style>
