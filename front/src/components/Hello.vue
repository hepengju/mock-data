<template>
    <el-table :data="tableData" style="width: 100%" border>
        <el-table-column prop="date" label="Date" width="180">
            <template #header>
                <div class="title">
                    <div class="name">
                        <span style="margin-left: 10px;">DATE</span>
                    </div>
                    <div class="icon">
                        <close-bold style="width: 1em; height: 1em; margin: 0 10px" />
                    </div>
                </div>
            </template>
        </el-table-column>
        <el-table-column prop="name" label="Name" width="180" />
        <el-table-column prop="address" label="Address" />
    </el-table>
</template>

<script setup>
import { CloseBold } from '@element-plus/icons-vue';

const tableData = [
    {
        date: '2016-05-03',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-02',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-04',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-01',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
]
</script>


<style lang="less">
// 表格标题
.el-table__header-wrapper {
    .el-table__cell,
    .cell {
        padding: 0 !important;
    }

    .title {
        display: flex;
        height: 40px;
        line-height: 40px;

        // 列名占据左侧部分, 且弹性增长
        .name {
            margin-right: auto; 
            flex-grow: 1;
        }

        // 图标显示合适大小并肉眼调整居中
        .icon {
            font-size: 20px;
            padding-top: 3px;
        }

        .name:hover,
        .icon:hover {
            background-color: #bfa;
        }
    }
}
</style>