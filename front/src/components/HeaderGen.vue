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
import Card from './Card.vue'
import bus from '../plugins/bus'

defineProps(['name', 'title', 'width', 'gens'])

function addColumn(gen) {
    console.log('点击列', gen.name)
}

function hoverGen(gen) {
    bus.emit('hoverGen', gen)
}
</script>

<style lang="less" scoped>
ul {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-evenly;
}

button {
    width: 90px;
    margin: 4px;
}
</style>