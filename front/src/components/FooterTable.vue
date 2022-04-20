<template>
    <el-row v-show="columns.length > 0" class="btn-line">
        <div class="btn">
            <el-button type="primary" @click="refreshData">刷新表格</el-button>
            <el-button type="danger" @click="deleteAll">清空表格</el-button>
            <el-button type="info" @click="saveHis">保存表格</el-button>
        </div>
        <div class="config">
            <el-form ref="formRef" :model="config" label-width="70px" :inline="true">
                <el-form-item label="下载行数">
                    <el-input-number v-model="config.sampleSize" :min="10" :max="100000" :step="100" />
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

    <div v-show="columns.length == 0">
        <div class="hint">请点击上方生成器，生成所需模拟数据...</div>
        <div class="history" v-show="tables.length > 0">
            <div>
                <el-tooltip content="点击此处清空历史记录，点击下面列表打开对应表格" placement="top-start">
                    <div @click="clearHis">历史记录</div>
                </el-tooltip>
            </div>
            <ul>
                <li v-for="(tab, index) in tables" :key="index">
                    <span @click="openHis(tab)">
                        {{ index + 1 }}. {{ tab.name }}：{{ tab.hint }}
                    </span>
                    <el-icon @click="deleteHis(index)">
                        <close />
                    </el-icon>
                </li>
            </ul>
        </div>
    </div>

    <el-table v-show="columns.length > 0" :data="data" border ref="table" table-layout="auto"
        :cell-class-name="cellClassName" :header-cell-class-name="headerCellClassName">
        <el-table-column type="index" label="#" align="center" fixed="left" />
        <el-table-column v-for="(col, index) in columns" :prop="col.key" :label="col.label">
            <template #default="scope">
                <span :style="{ color: col.color }" @mousemove="mouseMove(index)">{{ scope.row[col.key] }}</span>
            </template>

            <template #header>
                <div class="title">
                    <div class="name" @mouseenter="hoverColumn(col)" @mousemove="mouseMove(index)"
                        @mousedown="mouseDown(index)">
                        <span :style="{ color: col.color, marginLeft: '10px' }">{{ col.label }}</span>
                    </div>
                    <div class="icon" @click="deleteColumn(col)">
                        <close-bold style="width: 1em; height: 1em; margin: 0 10px" />
                    </div>
                    <div class="virtual"></div>
                </div>
            </template>
        </el-table-column>
    </el-table>
</template>

<script setup>
import { CloseBold, Close } from '@element-plus/icons-vue';
import dayjs from 'dayjs';
import { ElMessage } from 'element-plus';
import { nanoid } from 'nanoid';
import { computed, getCurrentInstance, onMounted, onUnmounted, onUpdated, reactive } from 'vue';
import { downTable, getData, refreshTable } from '../apis';
import bus from '../plugins/bus';

const config = reactive({
    sampleSize: 100,
    fileName: '测试表',
    fileFormat: 'excel',
    timerCount: 0,
    rowCount: 10
})
const columns = reactive([])
const tables = reactive([])

// 行数据, 可以由列数据计算出来(因为列上面已经保存了生成器的样例数据)
const data = computed(() => {
    const rows = []
    if (columns.length == 0) return rows

    for (let i = 0; i < config.rowCount; i++) {
        const row = {}
        for (let j = 0; j < columns.length; j++) {
            const col = columns[j]
            row[col.key] = col.meta.sampleData[i]

        }
        rows.push(row)
    }

    return rows
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
    return false
}

// 鼠标划过列标题
function hoverColumn(col) {
    // 20220420 拖拽中不触发
    if (dragState.dragging) return false

    bus.emit('hoverGen', col.meta)
    return false
}

// 添加列
bus.on('addColumn', gen => {
    const key = nanoid()
    columns.push({
        key,
        label: gen.columnTitle,
        color: gen.color,
        meta: { ...gen, isModified: true, columnKey: key },
    })
})

// 详细配置后, 保存更新列数据
bus.on('updateMeta', meta => {
    const params = { sampleSize: 10, ...meta }

    // 发送请求前删除多余数据
    delete params.sampleData
    delete params.color
    delete params.isModified

    getData(params).then(data => {
        const col = columns.filter(c => c.key == meta.columnKey)
        col.label = meta.columnTitle
        col.meta = meta

        for (let i = 0; i < config.rowCount; i++) {
            col.meta.sampleData[i] = data[i]
        }

        ElMessage({
            message: '保存并获取数据成功',
            type: 'success',
        })
    })
})

// 刷新表格数据
function refreshData() {
    refreshTable({
        metaList: getMetaList(),
        sampleSize: 10
    }).then(data => {

        for (let i = 0; i < config.rowCount; i++) {
            for (let j = 0; j < columns.length; j++) {
                const col = columns[j];
                col.meta.sampleData[i] = data[i][col.key]
            }
        }

        ElMessage({
            message: '刷新成功',
            type: 'success',
        })
    })
}

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
    }).catch(() => { })

    initHis()
}

// 刷新和下载表格共用的数据处理
function getMetaList() {
    return columns.map(c => {
        let m = { ...c.meta }
        delete m.sampleData // 减少参数的体积
        return m
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

        // 下载成功后, 保存到历史记录
        saveHis()
    })
}

// !!! 防止动态删除列时的抖动 !!!
onUpdated(() => {
    if (columns.length > 0) {
        getCurrentInstance().proxy.$refs.table.doLayout()
    }
})

// 卸载时去掉事件监听
onUnmounted(() => {
    bus.off('addColumn')
    bus.off('updateMeta')
})

//#region
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~拖动效果~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// 参考: https://www.cnblogs.com/wisewrong/p/8820508.html
let dragState = reactive({
    start: -9, // 起始元素的 index
    end: -9,  // 结束元素的 index
    dragging: false, // 是否正在拖动

    hoverColor: null // 拖动时选中行会遮挡掉虚线, 因此处理下
})

// 鼠标按下开始拖动
function mouseDown(index) {
    dragState.dragging = true
    dragState.start = index
    const table = document.getElementsByClassName('el-table')[0]
    const virtual = document.getElementsByClassName('virtual')
    for (let item of virtual) {
        item.style.height = table.clientHeight - 1 + 'px'
    }

    // 20220420 避免拖动时同时hover行, 引起的虚线被遮住一部分
    dragState.hoverColor = getComputedStyle(table).getPropertyValue('--el-table-row-hover-bg-color')
    table.style.setProperty('--el-table-row-hover-bg-color', 'transparent')
    document.addEventListener('mouseup', mouseUp)
}

// 鼠标抬起, 移动列, 拖拽状态恢复初始值, 并移出监听
function mouseUp() {
    const { start, end } = dragState
    if (start >= 0 && end >= 0 && start != end) {
        const tmp = columns.splice(start, 1) // tmp是一个代理对象 Proxy, 因此序列化一下
        const arr = JSON.parse(JSON.stringify(tmp))
        columns.splice(end, 0, ...arr)
    }

    // 恢复初始状态
    dragState.start = -9
    dragState.end = -9
    dragState.dragging = false

    const table = document.getElementsByClassName('el-table')[0]
    table.style.setProperty('--el-table-row-hover-bg-color', dragState.hoverColor)
    document.removeEventListener('mouseup', mouseUp)
}

// 拖动中, 一直记录着最后的索引位置(并不是抬起的时候再记录最后位置的)
function mouseMove(index) {
    if (dragState.dragging) {
        dragState.end = index
    }
}

function headerCellClassName({ columnIndex }) {
    if (!dragState.dragging) return ''
    let active = ''
    if (dragState.start < dragState.end) {
        active = columnIndex - 2 === dragState.end ? 'drag_active' : ''
    } else if (dragState.start > dragState.end) {
        active = columnIndex - 1 === dragState.end ? 'drag_active' : ''
    }
    let start = columnIndex - 1 === dragState.start ? 'drag_start' : ''
    return `${active} ${start}`
}

function cellClassName({ columnIndex }) {
    if (!dragState.dragging) return ''
    return (columnIndex - 1 === dragState.start ? 'drag_start' : '')
}
//#endregion

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~本地保存~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
const ITEM_KEY = 'tables'
// 挂载恢复表格
onMounted(() => {
    initHis()
})

// 初始化历史记录
function initHis() {
    tables.splice(0, tables.length)
    const arrs = JSON.parse(localStorage.getItem(ITEM_KEY)) || []
    tables.push(...arrs)
}

// 保存历史记录
function saveHis() {
    tables.unshift({
        name: config.fileName + dayjs().format('_YYYYMMDD_HHmmss'),
        hint: columns.map(c => c.label).join('、'),
        config,
        columns,
    })

    if (tables.length >= 9) {
        tables.pop()
    }

    localStorage.setItem(ITEM_KEY, JSON.stringify(tables))
    ElMessage({
        message: '保存成功',
        type: 'success',
    })
}

// 打开某条历史
function openHis({ config: tabConfig, columns: tabColumns }) {
    config.sampleSize = tabConfig.sampleData
    config.fileName = tabConfig.fileName
    config.fileFormat = tabConfig.fileFormat
    config.timerCount = 0
    config.rowCount = 10

    columns.splice(0, columns.length)
    columns.push(...tabColumns)
}

// 清空历史记录
function clearHis() {
    ElMessageBox.confirm(
        '确认清空历史记录吗？',
        '系统提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    ).then(() => {
        tables.splice(0, tables.length)
        localStorage.setItem(ITEM_KEY, JSON.stringify(tables))
    }).catch(() => { })
}

// 删除某一条
function deleteHis(index) {
    tables.splice(index, 1)
    localStorage.setItem(ITEM_KEY, JSON.stringify(tables))
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

// 表格的默认颜色改为黑色 ==> 序号列显示为黑色
.cell {
    color: black;
}

// 表格标题
// .el-table__header-wrapper { ==> table-layout为fixed时是这样的
thead {
    // ==> table-layout为auto时是这样的

    .el-table__cell,
    .cell {
        padding: 0 !important;
    }

    .title {
        display: flex;
        height: 40px;
        line-height: 40px;

        &:hover {
            cursor: move;
            background-color: var(--el-fill-color-light); // 参考: --el-table-row-hover-bg-color
        }

        // 列名占据左侧部分, 且弹性增长
        .name {
            flex-grow: 1;
        }

        // 图标显示合适大小并肉眼调整居中
        .icon {
            font-size: 20px;
            padding-top: 3px;

            &:hover {
                cursor: pointer;
                color: red;
            }
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

//~~~~~~~~~~~~~~拖拽~~~~~~~~~~~~~~
// 拖拽的样式
.drag_start {
    background-color: #bfa !important;
}

// 拖拽线默认无边框
.virtual {
    position: fixed;
    border: none;
}

// 激活时显示边框
.drag_active .virtual {
    border-left: 2px dotted red;
}

//~~~~~~~~~~~~~~历史记录~~~~~~~~~~~~~~
// 默认没有数据时显示文字样式
.hint {
    font-size: 20px;
    font-style: italic;
    text-align: center;
    margin-top: 100px;
}

.history {
    position: absolute;
    bottom: 20px;
    margin: 0 20px;

    &>div {
        display: flex;
        margin-bottom: 10px;

        &>div {
            font-weight: bold;
            margin-right: 20px;
            cursor: pointer;

            &:hover {
                color: red;
            }
        }
    }

    li {
        font-size: 14px;
        margin-top: 5px;
        cursor: pointer;

        span:hover {
            color: red;
        }

        .el-icon {
            transform: translateY(10%);
            margin-left: 10px;

            &:hover {
                color: red;
            }
        }
    }
}
</style>