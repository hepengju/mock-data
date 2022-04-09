<template>
    <Card :name="name" :title="title" class="sample">
        <ul :style="{ color: data.color }">
            <li v-for="(sample, index) in data.arrs" :key="index">{{ sample }}</li>
        </ul>
    </Card>
</template>

<script setup>
import { reactive } from 'vue';
import bus from '../plugins/bus';
import Card from './Card.vue';

defineProps(['title', 'name', 'gens'])
const data = reactive({
    arrs: ['请鼠标划入生成器按钮', '此处就会展现样例数据'],
    color: '#75799d'
})

bus.on('hoverGen', gen => {
    data.arrs = gen.sampleData
    data.color = gen.color
})

</script>

<style lang="less">
.sample {
    flex: 0 0 250px;
    overflow: hidden;

    li {
        font-size: 14px;
        margin-left: 10px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
}
</style>