<template>
    <div class="title" >
        <HeaderGen name="date_number" title="日期和数字" width="200px" :gens="dataList.date_number" />
        <HeaderGen name="string"      title="字符生成器" width="260px" :gens="dataList.string" />
        <HeaderGen name="custom"      title="定制生成器" width="500px" :gens="dataList.custom" />

        <HeaderSample name="sample" title="样例数据"/>
        <HeaderMeta   name="meta"   title="详细配置"/>
    </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue';
import { getGenMap } from './apis';
import HeaderGen from './components/HeaderGen.vue';
import HeaderSample from './components/HeaderSample.vue';
import HeaderMeta from './components/HeaderMeta.vue';

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
                     if (genType == 'string') gen.color = '#f56c6c'         // 字符: 按钮 danger  的颜色
                else if (genType == 'custom') gen.color = '#409eff'         // 定制: 按钮 success 的颜色
                else if (gen.name.startsWith('date')) gen.color = '#909399' // 日期: 按钮 info    的颜色
                else gen.color = '#e6a23c'                                  // 数字: 按钮 warning 的颜色
            });
        }
    })
})

</script>

<style lang="less">
* {
  margin: 0;
  padding: 0;
  list-style: none;
}

.title {
    margin-top: 5px;
    display: flex;
    justify-content: space-evenly;

    &>div {
        margin: 0 5px;
    }
}
</style>


