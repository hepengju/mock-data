// 事件名称
export const HOVER_GEN = 'hoverGen'     // 鼠标划入生成器 或 表格的标题, 样例和配置区显示对应数据
export const ADD_COLUMNS = 'addColumns' // 点击生成器, 表格添加列; 点击按钮<随机选择>, 表格添加多列
export const UPDATE_META = 'updateMeta' // 鼠标划入表格的标题, 配置区的按钮可以点击, 以便修改指定列的元数据
export const RANDOM_COLS = 'randomCols' // 随机选择生成器事件名称

// 普通常量
export const ITEM_KEY = 'tables'     // localStorage存储历史记录的key名称
export const TIMER_COUNT = 5         // 点击下载后, 多少秒不能重复下载
export const ROW_COUNT = 10          // 样例数为固定的10个
export const ROW_ARRAY = ['','','','','',  '','','','',''] // 样例数为固定的10个

// 预设样例表
export const PRE_TABLS = {
    '用户表': [
        {
            "name": "autoIncrement",
            "columnKey": "FMBpNqp7n8jjctIkO4Rso",
            "columnTitle": "自增",
            "columnName": "autoIncrement",
            "min": "0",
            "max": null,
            "code": null,
            "codeMulti": false,
            "format": "#",
            "prefix": "",
            "suffix": "",
            "color": "#ff9900"
        },
        {
            "name": "randomAlphabetic",
            "columnKey": "5EjoG9UmyQjhhf5iHwq8m",
            "columnTitle": "账号",
            "columnName": "randomAlphabetic",
            "min": "6",
            "max": "12",
            "code": null,
            "codeMulti": false,
            "format": null,
            "prefix": "",
            "suffix": "",
            "color": "#19be6b"
        },
        {
            "name": "sha256",
            "columnKey": "B2R6MCWqcZHEBmXuJusBg",
            "columnTitle": "密码",
            "columnName": "sha256",
            "min": null,
            "max": null,
            "code": null,
            "codeMulti": false,
            "format": null,
            "prefix": "",
            "suffix": "",
            "color": "#409eff"
        },
        {
            "name": "chineseName",
            "columnKey": "cAFHuN2FTvwr9rDQfzYCv",
            "columnTitle": "姓名",
            "columnName": "chineseName",
            "min": null,
            "max": null,
            "code": null,
            "codeMulti": false,
            "format": null,
            "prefix": "",
            "suffix": "",
            "color": "#409eff"
        },
        {
            "name": "mobile",
            "columnKey": "ym2L3lXU5P7vLj6qDfoYv",
            "columnTitle": "手机号",
            "columnName": "mobile",
            "min": null,
            "max": null,
            "code": null,
            "codeMulti": false,
            "format": null,
            "prefix": "",
            "suffix": "",
            "color": "#409eff"
        },
        {
            "name": "identityCard",
            "columnKey": "SYzORYcsYqGCA3XZ0iRIN",
            "columnTitle": "身份证号",
            "columnName": "identityCard",
            "min": "1900-01-01",
            "max": "2100-12-31",
            "code": null,
            "codeMulti": false,
            "format": null,
            "prefix": "",
            "suffix": "",
            "color": "#409eff"
        },
        {
            "name": "date",
            "columnKey": "kq3PIP7HTPowiU9xjXvKA",
            "columnTitle": "出生日期",
            "columnName": "date",
            "min": "1949-10-01",
            "max": "2021-12-31",
            "code": null,
            "codeMulti": false,
            "format": "yyyy-MM-dd",
            "prefix": "",
            "suffix": "",
            "color": "#909399"
        },
        {
            "name": "code",
            "columnKey": "6Mb6O4_mcInweZpdVJifS",
            "columnTitle": "性别",
            "columnName": "code",
            "min": null,
            "max": null,
            "code": "M,F",
            "codeMulti": false,
            "format": null,
            "prefix": "",
            "suffix": "",
            "color": "#19be6b"
        },
        {
            "name": "email",
            "columnKey": "o-XQi20SdaaGH-kw0BuwF",
            "columnTitle": "邮箱",
            "columnName": "email",
            "min": null,
            "max": null,
            "code": null,
            "codeMulti": false,
            "format": null,
            "prefix": "",
            "suffix": "",
            "color": "#409eff"
        },
        {
            "name": "chinaAddress",
            "columnKey": "l16hMbH7SgKUcNNhFJfBf",
            "columnTitle": "中国地址",
            "columnName": "chinaAddress",
            "min": null,
            "max": null,
            "code": null,
            "codeMulti": false,
            "format": null,
            "prefix": "",
            "suffix": "",
            "color": "#409eff"
        }],

    '角色表': [
        {
            "name": "autoIncrement",
            "columnKey": "S6SR3ciekKdoTGWAWd_or",
            "columnTitle": "自增",
            "columnName": "autoIncrement",
            "min": "0",
            "max": null,
            "code": null,
            "codeMulti": false,
            "format": "#",
            "prefix": "",
            "suffix": "",
            "color": "#ff9900"
        },
        {
            "name": "randomAlphabetic",
            "columnKey": "ScAY2Xwn7MnISZQISN5N5",
            "columnTitle": "角色代号",
            "columnName": "randomAlphabetic",
            "min": "6",
            "max": "6",
            "code": null,
            "codeMulti": false,
            "format": null,
            "prefix": "",
            "suffix": "",
            "color": "#19be6b"
        },
        {
            "name": "randomChinese",
            "columnKey": "DEi7Gy5sDXqzGdqYd_X6o",
            "columnTitle": "角色名称",
            "columnName": "randomChinese",
            "min": "3",
            "max": "4",
            "code": null,
            "codeMulti": false,
            "format": null,
            "prefix": "",
            "suffix": "",
            "color": "#19be6b"
        },
        {
            "name": "dateTime",
            "columnKey": "-B3Zcu8fG15B2qeXyz1x5",
            "columnTitle": "创建时间",
            "columnName": "dateTime",
            "min": "1900-01-01 00:00:00",
            "max": "2100-12-31 23:59:59",
            "code": null,
            "codeMulti": false,
            "format": "yyyy-MM-dd HH:mm:ss",
            "prefix": "",
            "suffix": "",
            "color": "#409eff"
        }
    ]
}