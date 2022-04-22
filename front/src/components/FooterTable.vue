<template>
    <el-row style="margin: 10px;">
        <!-- 
            // flex布局, 左边按钮, 右边配置
            // margin-right 不设置的话默认是0，父容器width 定宽之后，margin-right取值为 auto ，则自动占据了剩余的全部宽度
         -->
        <div class="btn" style="margin-right: auto;">
            <el-button-group>
                <el-tooltip content="随机选择N个生成器" placement="top-start">
                    <el-button type="primary" :icon="Orange" @click="randomCols">随机选择</el-button>
                </el-tooltip>

                <el-tooltip content="样例生成器列表" placement="top-start">
                    <el-dropdown @command="preTables">
                        <el-button type="info" :icon="Present">
                            预设样例&nbsp;
                            <el-icon>
                                <arrow-down />
                            </el-icon>
                        </el-button>

                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item v-for="(_, name) in PRE_TABLS" :key="name" :command="name">{{ name }}
                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </el-tooltip>
            </el-button-group>

            <el-button-group v-show="columns.length > 0" style="margin-left: 20px">
                <el-tooltip content="刷新表格中的样例数据" placement="top-start">
                    <el-button type="success" :icon="Refresh" @click="refreshData">刷新</el-button>
                </el-tooltip>
                <el-tooltip content="保存当前表格到历史记录" placement="top-start">
                    <el-button type="warning" :icon="Wallet" @click="saveHis">保存</el-button>
                </el-tooltip>
                <el-tooltip content="删除当前表格全部列" placement="top-start">
                    <el-button type="danger" :icon="Delete" @click="deleteAll">清空</el-button>
                </el-tooltip>
            </el-button-group>
        </div>
        <div class="config" v-show="columns.length > 0">
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

                <el-button type="primary" @click="downData" style="width: 80px;" :icon="Download"
                    :loading="status.loading">
                    <span>下载</span>
                    <span v-if="status.timerCount > 0">({{ status.timerCount }})</span>
                </el-button>
            </el-form>
        </div>
    </el-row>

    <div v-show="columns.length == 0">
        <div class="prompt">请点击上方生成器，生成所需模拟数据...</div>
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
        :cell-class-name="dragClassName" :header-cell-class-name="dragClassName">
        <el-table-column type="index" label="#" align="center" fixed="left" />
        <el-table-column v-for="(col, index) in columns" :prop="col.key" :label="col.label">
            <template #default="scope">
                <div class="text" :style="{ color: col.color }" @mousemove="mouseMove(index)">{{
                        scope.row[col.key]
                }}</div>
                <div class="virtual"></div>
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
import { ArrowDown, Close, CloseBold, Delete, Download, Orange, Present, Refresh, Wallet } from '@element-plus/icons-vue';
import dayjs from 'dayjs';
import { ElMessage } from 'element-plus';
import { nanoid } from 'nanoid';
import { computed, getCurrentInstance, onMounted, onUnmounted, onUpdated, reactive } from 'vue';
import { downTable, getData, refreshTable } from '../apis';
import { ADD_COLUMNS, HOVER_GEN, ITEM_KEY, PRE_TABLS, RANDOM_COLS, ROW_ARRAY, ROW_COUNT, TIMER_COUNT, UPDATE_META } from '../consts';
import bus from '../plugins/bus';

onUnmounted(() => {
    bus.off(ADD_COLUMNS, UPDATE_META)
})
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~数据~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// 配置区
const config = reactive({
    sampleSize: 100,
    fileName: '测试表',
    fileFormat: 'excel',
})

// 下载按钮状态
const status = reactive({
    timerCount: 0,
    loading: false
})

// 表格数据
const columns = reactive([])

// 历史记录
const tables = reactive([])

// 行数据, 可以由列数据计算出来(因为列上面已经保存了生成器的样例数据)
const data = computed(() => {
    const rows = []
    if (columns.length == 0) return rows

    for (let i = 0; i < ROW_COUNT; i++) {
        const row = {}
        for (let j = 0; j < columns.length; j++) {
            const col = columns[j]
            row[col.key] = col.meta.sampleData[i]

        }
        rows.push(row)
    }

    return rows
})
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~功能~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// 随机选择生成器
function randomCols() {
    bus.emit(RANDOM_COLS)
}

// 预设样例选择: 获取到对应的meta数组, 去请求数据
function preTables(command) {
    const gens = PRE_TABLS[command]
    config.fileName = command
    bus.emit(ADD_COLUMNS, { gens, deleteAll: true, refreshAll: true })
}

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

    bus.emit(HOVER_GEN, col.meta)
    return false
}

// 添加列
function addColumns(gens) {
    gens.forEach(gen => {
        const key = nanoid()
        columns.push({
            key,
            label: gen.columnTitle,
            color: gen.color,
            meta: { ...gen, isModified: true, columnKey: key, sampleData: gen.sampleData || [...ROW_ARRAY] },
        })
    });
}

bus.on(ADD_COLUMNS, e => {
    if (e.deleteAll && columns.length > 0) {
        ElMessageBox.confirm(
            '确认覆盖全部列吗？',
            '系统提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }
        ).then(() => {
            columns.splice(0)

            addColumns(e.gens)
            if (e.refreshAll) {
                refreshData()
            }
        }).catch(() => { })
    } else {
        addColumns(e.gens)
        if (e.refreshAll) {
            refreshData()
        }
    }
})

// 详细配置后, 保存更新列数据
bus.on(UPDATE_META, meta => {
    const params = { sampleSize: ROW_COUNT, ...meta }

    // 发送请求前删除多余数据
    deleteNotNeedData(params)
    getData(params).then(data => {
        columns.forEach(col => {
            if (col.key != meta.columnKey) return;

            col.label = meta.columnTitle
            col.meta = meta

            for (let i = 0; i < ROW_COUNT; i++) {
                col.meta.sampleData[i] = data[i]
            }

        })
        ElMessage({
            message: '保存并获取数据成功',
            type: 'success',
        })
    })
})

// 刷新表格数据
function refreshData() {
    const params = {
        metaList: columns.map(c => deleteNotNeedData({ ...c.meta })),
        sampleSize: ROW_COUNT
    }
    console.log("刷新参数: ", params)

    refreshTable(params).then(data => {

        for (let i = 0; i < ROW_COUNT; i++) {
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

// 删除不必要的参数, 减少体积
function deleteNotNeedData(m) {
    delete m.sampleData
    // delete m.color   方便造预设样例, 此处保留(刷新时会打印)
    delete m.isModified
    delete m.type
    return m
}

// 下载数据
function downData() {
    const params = {
        sampleSize: config.sampleSize,
        fileName: config.fileName,
        fileFormat: config.fileFormat,
        metaList: columns.map(c => deleteNotNeedData({ ...c.meta }))
    }

    status.timerCount = TIMER_COUNT
    status.loading = true
    downTable(params).then(() => {
        status.loading = false

        let timer = setInterval(() => {
            status.timerCount--
            if (status.timerCount <= 0) {
                clearInterval(timer)
            }
        }, 1000)

        // 下载成功后, 保存到历史记录
        saveHis(false)
    })
}

// !!! 防止动态删除列时的抖动 !!!
onUpdated(() => {
    if (columns.length > 0) {
        getCurrentInstance().proxy.$refs.table.doLayout()
    }
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

function dragClassName({ columnIndex }) {
    if (!dragState.dragging) return ''
    let active = ''
    
    if (dragState.start < dragState.end) {
        active = columnIndex - 1 === dragState.end ? 'drag_active' : ''
    } else if (dragState.start > dragState.end) {
        active = columnIndex === dragState.end ? 'drag_active' : ''
    }
    let start = columnIndex - 1 === dragState.start ? 'drag_start' : ''
    return `${active} ${start}`
}
//#endregion

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~本地保存~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
function saveHis(showMessage = true) {
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

    if (showMessage) {
        ElMessage({
            message: '保存成功',
            type: 'success',
        })
    }
}

// 打开某条历史
function openHis({ config: tabConfig, columns: tabColumns }) {
    config.sampleSize = tabConfig.sampleData || 100
    config.fileName = tabConfig.fileName || '测试表'
    config.fileFormat = tabConfig.fileFormat || 'excel'

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
// 按钮右侧的表单样式, 并定制第三个input(select)的宽度
.el-form--inline .el-form-item {
    margin-bottom: 0;
    margin-right: 20px;
    width: 220px;
}

// 表格
.el-table {
    .el-table__cell,
    .cell {
        padding: 0 !important; // 去掉padding(因为拖拽的虚线位置)
        color: black;        // 默认颜色改为黑色 ==> 序号列显示为黑色
    }
}

// 表格标题
.el-table thead {

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
            // 此处处理标题头的...省略号显示, 实测不行(猜测是table-layout的auto影响), 但是加入此行还是可以合适显示的
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
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
.el-table tbody {
    // 减少行高
    .el-table__cell {
        height: 32px !important;

        // 文字多余省略显示
        .cell {
            max-width: 200px;

            .text {
                margin: 0 10px;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
        }
    }
}

//~~~~~~~~~~~~~~拖拽~~~~~~~~~~~~~~
// 拖拽的样式
.drag_start {
    background-color: #bfa !important;
}

// 拖拽线默认无边框
.virtual {
    position: absolute;
    right: 0;
    bottom: 0;
    height: 100%;
    border: none;
}

// 激活时显示边框
.drag_active .virtual {
    border-left: 2px dotted red;
}

//~~~~~~~~~~~~~~历史记录~~~~~~~~~~~~~~
// 默认没有数据时显示文字样式
.prompt {
    color: rgba(gray, .5);
    font-size: 26px;
    font-style: italic;
    text-align: center;
    margin-top: 80px;
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