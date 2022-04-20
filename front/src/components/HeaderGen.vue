<template>
    <Card :name="name" :title="title" :width="width">
        <ul>
            <li v-for="gen in gens" :key="gen.name">
                <el-button
                    :style="{ backgroundColor: gen.color, color: '#fff' }"
                    @click="addColumn(gen)"
                    @mouseenter="hoverGen(gen)"
                >{{ gen.columnTitle }}</el-button>
            </li>
        </ul>
    </Card>
</template>

<script setup>
import bus from '../plugins/bus';
import Card from './Card.vue';

defineProps(['name', 'title', 'width', 'gens'])

function addColumn(gen) {
    bus.emit('addColumn', gen)
}

function hoverGen(gen) {
    bus.emit('hoverGen', gen)
}
</script>

<style lang="less" scoped>
// 按钮列表 弹性布局, 允许换行, 空白均分
ul {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-evenly;
}

// 按钮宽度和间距
button {
    width: 90px;
    margin: 4px;
}

// 生成器添加悬浮效果
button:hover {
    outline: 1px solid black;
}
</style>