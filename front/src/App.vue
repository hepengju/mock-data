<template>
    <div class="header">
        <HeaderGen title="日期和数字" width="200px" :gens="dataList.date_number" name="date_number" />
        <HeaderGen title="字符生成器" width="260px" :gens="dataList.string" name="string" />
        <HeaderGen title="定制生成器" width="500px" :gens="dataList.custom" name="custom" />

        <HeaderSample name="sample" title="样例数据" />
        <HeaderMeta name="meta" title="详细配置" />
    </div>

    <FooterTable />

    <div class="widget">
        <a href='https://gitee.com/hepengju/mock-data' target="_blank">
            <img src='https://gitee.com/hepengju/mock-data/widgets/widget_1.svg?color=c71d23'
                alt='Fork me on Gitee' />
        </a>
    </div>
</template>

<script setup>
import { reactive } from 'vue';
import { getGenMap } from './apis';
import _shuffleSelf from 'lodash/_shuffleSelf';
import FooterTable from './components/FooterTable.vue';
import HeaderGen from './components/HeaderGen.vue';
import HeaderMeta from './components/HeaderMeta.vue';
import HeaderSample from './components/HeaderSample.vue';
import { ADD_COLUMNS, RANDOM_COLS } from './consts';
import bus from './plugins/bus';

// 生成器(后台已经分组返回)
const dataList = reactive({
    date_number: [],
    string: [],
    custom: []
})

// 生成器Map, 初始化的时候会写入
const gensMap = {}
getGenMap().then(data => {
    // 添加生成器的颜色
    for (const genType in data) {
        dataList[genType] = data[genType]
        dataList[genType].forEach(gen => {
            if (genType == 'string') gen.color = '#19be6b'         // 字符: 按钮 danger  的颜色
            else if (genType == 'custom') gen.color = '#409eff'         // 定制: 按钮 success 的颜色
            else if (gen.name.startsWith('date')) gen.color = '#909399' // 日期: 按钮 info    的颜色
            else gen.color = '#ff9900'                                  // 数字: 按钮 warning 的颜色

            // 记录所有生成器
            gensMap[gen.name] = gen
        });
    }
})

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// 随机选择生成器
bus.on(RANDOM_COLS, () => {
    const gens = []
    // 确保随机后gens不会空
    while (gens.length == 0) {
        for (let name in gensMap) {
            // 目前总共31个生成器, 预计生成10个, 那么遍历所有, 30%概率差不多
            if (Math.random() > 0.7) {
                gens.push(gensMap[name])
            }
        }
    }

    // 上面的随机选择的gens总是按照Object.keys的顺序的, 此处添加lodash随机打乱下
    _shuffleSelf(gens)
    bus.emit(ADD_COLUMNS, { gens, msg: `随机添加${gens.length}列成功` })
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
    // max-height: 350px;
    margin-right: 20px;

    &>div {
        margin: 0 5px;
        min-width: 120px;
    }
}

.widget {
    position: absolute;
    right: 0;
    top: 0;
}
</style>


