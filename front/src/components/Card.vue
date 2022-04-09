<template>
    <div class="card" :style="{width: width}">
        <h2>
            <el-icon>
                <calendar      v-if="name == 'date_number'" />
                <tickets       v-if="name == 'string'     " />
                <medal         v-if="name == 'custom'     " />
                <data-analysis v-if="name == 'sample'     " />
                <setting       v-if="name == 'meta'       " />
            </el-icon>
            <span>{{ title }} </span>
        </h2>

        <hr />
        <slot></slot>
    </div>
</template>

<script setup>
import { Calendar, DataAnalysis, Medal, Tickets, Setting} from '@element-plus/icons-vue'
defineProps(['name', 'title', 'width'])

</script>

<style lang="less">
.card {
    border: 3px solid tomato;

    h2 {
        font-size: 16px;
        text-align: center;

        i {
            vertical-align: -15%;
        }

        span {
            margin-left: 3px;
        }
    }

    hr {
        margin: 5px 0px;
        background-color: #e8eaec;
        height: 2px;
        border: none;
    }
}


</style>