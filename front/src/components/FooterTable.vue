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
                <el-form-item label="格式" style="width: 160px;">
                    <el-select v-model="config.fileFormat">
                        <el-option label="Excel" value="excel" />
                        <el-option label="SQL" value="sql" />
                        <el-option label="CSV" value="csv" />
                        <el-option label="TSV" value="tsv" />
                    </el-select>
                </el-form-item>

                <el-button type="primary" @click="downData" style="width: 80px;">
                    <span>下载</span>
                    <span v-if="config.timerCount > 0">({{ config.timerCount }})</span>
                </el-button>
            </el-form>
        </div>
    </el-row>

    <div class="prompt" v-if="columns.length == 0">请点击上方生成器，生成所需模拟数据...</div>
    <el-table v-else :data="rows" border stripe>
        <el-table-column type="index" label="#" align="center" fixed="left" />
        <el-table-column v-for="col in columns" :prop="col.key" :label="col.label">
            <template #default="scope">
                <span :style="{ color: col.color }">{{ scope.row[col.key] }}</span>
            </template>

            <template #header>
                <div class="title">
                    <div class="name" @mouseenter="hoverColumn(col)">
                        <span :style="{ color: col.color, marginLeft: '10px' }">{{ col.label }}</span>
                    </div>
                    <div class="icon" @click="deleteColumn(col)">
                        <close-bold style="width: 1em; height: 1em; margin: 0 10px" />
                    </div>
                </div>
            </template>
        </el-table-column>
    </el-table>
</template>

<script setup>
import { CloseBold } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { nanoid } from "nanoid";
import { reactive } from 'vue';
import { getData, refreshTable, downTable } from '../apis';
import bus from '../plugins/bus';

const config = reactive({
    sampleSize: 100,
    fileName: '测试表',
    fileFormat: 'excel',
    timerCount: 0,
    rowCount: 10
})
const columns = reactive([])
const rows = reactive([])

// 添加列
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
        label: gen.columnTitle,
        color: gen.color,
        meta: { ...gen, isModified: true, columnKey: key },
    })
})

// 删除列
function deleteColumn(col) {
    const key = col.key
    for (let i = 0; i < columns.length; i++) {
        const col = columns[i];
        if (col.key == key) {
            columns.splice(i, 1)
            break
        }
    }
    rows.forEach(r => delete r[key])
    return false
}

// 鼠标划过列标题
function hoverColumn(col) {
    bus.emit('hoverGen', col.meta)
    return false
}

// 详细配置后, 保存更新列数据
bus.on('updateMeta', meta => {
    const params = { sampleSize: 10, ...meta }

    getData(params).then(data => {
        const key = meta.columnKey
        columns.forEach(c => {
            if (c.key != meta.columnKey) return
            c.meta = meta
            c.label = meta.columnTitle
        })

        for (let i = 0; i < config.rowCount; i++) {
            rows[i][key] = data[i]
        }

        ElMessage({
            message: '保存并获取数据成功',
            type: 'success',
        })
    })
})

// 删除所有列
function deleteAll() {
    ElMessageBox.confirm(
        '确认删除全部列吗？',
        '系统提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    ).then(() => {
        columns.splice(0)
        rows.splice(0)
    }).catch(() => { })

}

// 刷新和下载表格共用的数据处理
function getMetaList() {
    return columns.map(c => {
        let m = { ...c.meta }
        delete m.sampleData // 减少参数的体积
        return m
    })
}

// 刷新表格数据
function refreshData() {
    refreshTable({
        metaList: getMetaList(),
        sampleSize: 10
    }).then(data => {
        for (let i = 0; i < config.rowCount; i++) {
            rows[i] = data[i];
        }

        ElMessage({
            message: '刷新成功',
            type: 'success',
        })
    })
}

// 下载数据
function downData() {
    const params = {
        sampleSize: config.sampleSize,
        fileName: config.fileName,
        fileFormat: config.fileFormat,
        metaList: getMetaList()
    }

    config.timerCount = 5
    downTable(params).then(res => {
        let timer = setInterval(() => {
            config.timerCount--
            if (config.timerCount <= 0) {
                clearInterval(timer)
            }
        }, 1000)
    })
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

// 默认没有数据时显示文字样式
.prompt {
    text-align: center;
    font-size: 18px;
    font-style: italic;
    padding-top: 200px;
}

// 表格的默认颜色改为黑色 ==> 序号列显示为黑色
.cell {
    color: black;
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