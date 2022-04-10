<template>
    <div class="header" >
        <HeaderGen name="date_number" title="日期和数字" width="200px" :gens="dataList.date_number" />
        <HeaderGen name="string"      title="字符生成器" width="260px" :gens="dataList.string" />
        <HeaderGen name="custom"      title="定制生成器" width="500px" :gens="dataList.custom" />

        <HeaderSample name="sample" title="样例数据"/>
        <HeaderMeta   name="meta"   title="详细配置"/>
    </div>

    <FooterTable/>
</template>

<script setup>
import { onMounted, reactive } from 'vue';
import { getGenMap } from './apis';
import FooterTable from './components/FooterTable.vue';
import HeaderGen from './components/HeaderGen.vue';
import HeaderMeta from './components/HeaderMeta.vue';
import HeaderSample from './components/HeaderSample.vue';

let dataList = reactive({
    date_number: [],
    string: [],
    custom: []
})

onMounted(() => {
    getGenMap().then(data => {
        // 添加生成器的颜色
        for (const genType in data) {
            dataList[genType] = data[genType]
            dataList[genType].forEach(gen => {
                     if (genType == 'string') gen.color = '#19be6b'         // 字符: 按钮 danger  的颜色
                else if (genType == 'custom') gen.color = '#409eff'         // 定制: 按钮 success 的颜色
                else if (gen.name.startsWith('date')) gen.color = '#909399' // 日期: 按钮 info    的颜色
                else gen.color = '#ff9900'                                  // 数字: 按钮 warning 的颜色
            });
        }
    })
})

</script>

<style lang="less">
// 清除默认样式
* {
  margin: 0;
  padding: 0;
  list-style: none;
}

// 弹性布局, 空白均分, 两侧边距
.header {
    display: flex;
    justify-content: space-evenly;
    overflow: hidden;
    max-height: 300px;

    &>div {
        margin: 0 5px;
        min-width: 120px;
    }
}
</style>


