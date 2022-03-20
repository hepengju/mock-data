<template>
  <div id="app">
    <!-- 顶部栏 -->
    <div id="title">
      <TitleCard icon="ios-calendar-outline"  title="日期和数字"                 width="200px" :gens="dataList.date_number"/>
      <TitleCard icon="ios-construct-outline" title="字符生成器"  type="success" width="230px" :gens="dataList.string"/>
      <TitleCard icon="ios-construct"         title="定制生成器"  type="info"    width="340px" :gens="dataList.custom"/>
      <TitleSample />
      <TitleMeta />
    </div>

    <!-- 中间行 -->
    <CenterLine/>

    <!-- 底部表格 -->
    <FooterTable/>
  </div>
</template>

<script>
import {getGenMap} from './apis'

import TitleCard from './components/TitleCard'
import TitleSample from './components/TitleSample'
import TitleMeta from './components/TitleMeta'
import CenterLine from './components/CenterLine'
import FooterTable from './components/FooterTable'

export default {
  name: 'App',
  components: {
      TitleCard, TitleSample, TitleMeta, CenterLine, FooterTable
  },
  data () {
    return {
      dataList:{
        date_number: [],
        string:[],
        custom:[]
      }
    }
  },
  methods: {
    init() {
      getGenMap().then(data => {
        this.dataList = data
        // 添加生成器的颜色
        for (const genType in this.dataList) {
          this.dataList[genType].forEach(gen => {
            if      (genType == 'string') gen.color = '#19be6b'         // 字符: 按钮 success 的颜色
            else if (genType == 'custom') gen.color = '#2db7f5'         // 定制: 按钮 info    的颜色
            else if (gen.name.startsWith('date')) gen.color = '#f16643' // 日期: 按钮 error   的颜色
            else gen.color = '#ff9900'                                  // 数字: 按钮 warning 的颜色
          });
        }
      })
    }
  },
  mounted: function () {
      this.init();
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

.table {
  border: 5px blue solid;
  width: 100%;
}

</style>
