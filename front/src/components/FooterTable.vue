<template>
    <el-row v-show="columns.length > 0" class="btn-line">
        <div class="btn">
            <el-button type="primary" @click="refreshData">刷新数据</el-button>
            <el-button type="danger" @click="deleteAll">删除所有</el-button>
        </div>
        <div class="config">
            <el-form ref="formRef" :model="config" label-width="70px" :inline="true">
                <el-form-item label="下载行数">
                    <el-input-number
                        v-model="config.sampleSize"
                        :min="10"
                        :max="100000"
                        :step="100"
                    />
                </el-form-item>
                <el-form-item label="文件名称" for="fileName">
                    <el-input id="fileName" v-model="config.fileName" />
                </el-form-item>
                <el-form-item label="格式">
                    <el-select v-model="config.fileFormat">
                        <el-option value="Excel"></el-option>
                        <el-option value="SQL"></el-option>
                        <el-option value="CSV"></el-option>
                        <el-option value="TSV"></el-option>
                    </el-select>
                </el-form-item>

                <el-button type="primary" @click="downData">下载</el-button>
            </el-form>
        </div>
    </el-row>

    <el-table
        v-show="columns.length > 0"
        :data="rows"
        border
        stripe
        empty-text="请点击上方生成器，生成所需模拟数据..."
    >
        <el-table-column type="index" label="#" align="center" fixed="left"/>
        <el-table-column v-for="gen in columns" :prop="gen.key" :label="gen.columnTitle">
            <template #default="scope">
                <span :style="{ color: gen.color }">{{ scope.row[gen.key] }}</span>
            </template>

            <template #header>
                <div class="title">
                    <div class="name">
                        <span :style="{ color: gen.color, marginLeft: '10px' }">{{ gen.columnTitle }}</span>
                    </div>
                    <div class="icon">
                        <close-bold style="width: 1em; height: 1em; margin: 0 10px" />
                    </div>
                </div>
            </template>
        </el-table-column>
    </el-table>
</template>

<script setup>
import { CloseBold } from '@element-plus/icons-vue';
import { nanoid } from "nanoid";
import { reactive } from 'vue';
import bus from '../plugins/bus';

const config = reactive({
    sampleSize: 100,
    fileName: '测试表',
    fileFormat: 'Excel',
    processing: false,
    processingNumber: 0,
    rowCount: 10
})
const columns = reactive([])
const rows = reactive([])

bus.on('addColumn', gen => {
    const key = nanoid()

    if (rows.length == 0) {
        for (let i = 0; i < config.rowCount; i++) {
            rows.push({})
        }
    }

    for (let i = 0; i < config.rowCount; i++) {
        rows[i][key] = gen.sampleData[i]
    }

    columns.push({
        key,
        columnTitle: gen.columnTitle,
        color: gen.color
    })
})

function refreshData() {
    console.log('refreshData')
}

function deleteAll() {
    console.log('deleteAll')
}

function downData() {
    console.log('downData')
}

</script>

<style lang="less">
// 上下有点外边距, 左右和上方外边距一致
.btn-line {
    margin: 10px 5px;
}

// flex布局, 左边按钮, 右边配置
// margin-right 不设置的话默认是0，父容器width 定宽之后，margin-right取值为 auto ，则自动占据了剩余的全部宽度
.btn {
    margin-right: auto;
}

// 按钮右侧的表单样式, 并定制第三个input(select)的宽度
.el-form--inline .el-form-item {
    margin-bottom: 0;
    margin-right: 20px;
    width: 220px;
}

.el-form-item:nth-child(3) {
    width: 160px;
}

// 默认没有数据时显示文字样式
.el-table__empty-text {
    font-size: 18px;
    font-style: italic;
    padding-top: 100px;
}

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

// 表格数据
.el-table__body-wrapper {
    // 减少行高
    .el-table__cell {
        padding: 5px 0;
        height: 32px;
    }

    // 文字多余省略显示
    .cell {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
}
</style>