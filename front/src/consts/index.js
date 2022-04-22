// 事件名称
export const HOVER_GEN   = 'hoverGen'   // 鼠标划入生成器 或 表格的标题, 样例和配置区显示对应数据
export const ADD_COLUMNS = 'addColumns' // 点击生成器, 表格添加列; 点击按钮<随机选择>, 表格添加多列
export const UPDATE_META = 'updateMeta' // 鼠标划入表格的标题, 配置区的按钮可以点击, 以便修改指定列的元数据
export const RANDOM_COLS = 'randomCols' // 随机选择生成器事件名称
export const SAMPLE_COLS = 'sampleCols' // 预设样例事件名称


// 普通常量
export const ITEM_KEY    = 'tables'     // localStorage存储历史记录的key名称
export const TIMER_COUNT = 5            // 点击下载后, 多少秒不能重复下载
export const ROW_COUNT   = 10           // 样例数为固定的10个

// 预设样例表
export const PRE_SAMPLE_COLS =  {
    'user': {

    },
    'role': {

    }
}