<template>
    <el-radio-group v-model="initObj.type">
        <el-tooltip content="me.ahoo.cosid.CosId">
            <el-radio-button label="cosid">CosId</el-radio-button>
        </el-tooltip>
        <el-radio-button label="mpsequence">MybatisPlus-Sequence</el-radio-button>
    </el-radio-group>

    <el-descriptions :column="1" border style="margin-top:10px">
        <el-descriptions-item label="主键值" style="padding: 0;">
            <el-input v-model="initObj.value" placeholder="请输入主键值" />
        </el-descriptions-item>

        <el-descriptions-item label="二进制">
            <el-tag effect="dark" type="success">{{ idObj.sign }}</el-tag> -
            <el-tag effect="dark">{{ idObj.time }}</el-tag> -

            <el-tag v-if="initObj.type == 'cosid'" effect="dark" type="info">{{ idObj.mach }}</el-tag>
            <span v-if="initObj.type != 'cosid'">
                <el-tag effect="dark" type="info">{{ idObj.mach1 }}</el-tag> -
                <el-tag effect="dark" type="info">{{ idObj.mach2 }}</el-tag>
            </span>
            -
            <el-tag effect="dark" type="warning">{{ idObj.seq }}</el-tag>
        </el-descriptions-item>

        <el-descriptions-item label="分解">
            <el-tag effect="dark" type="success">符号位(1位)</el-tag> -
            <el-tooltip content="约可以存储69年的时间戳，即可以使用的绝对时间为EPOCH+69年">
                <el-tag effect="dark">时间戳偏移量(41位)</el-tag>
            </el-tooltip> -
            <el-tooltip v-if="initObj.type == 'cosid'" content="相同业务可以部署1024个副本">
                <el-tag effect="dark" type="info">机器号(10位)</el-tag>
            </el-tooltip>

            <span v-if="initObj.type != 'cosid'">
                <el-tag effect="dark" type="info">数据中心(5位)</el-tag> -
                <el-tag effect="dark" type="info">机器号(5位)</el-tag>
            </span>

            -
            <el-tooltip content="单机每秒可生成约409W的ID">
                <el-tag effect="dark" type="warning">自增值(12位)</el-tag>
            </el-tooltip>
        </el-descriptions-item>

        <el-descriptions-item label="十进制">
            <el-tag effect="dark" type="success">{{ to10(idObj.sign) }}</el-tag> -
            <el-tag effect="dark">{{ to10(idObj.time) }}</el-tag> -
            <el-tag v-if="initObj.type == 'cosid'" effect="dark" type="info">{{ to10(idObj.mach) }}</el-tag>
            <span v-if="initObj.type != 'cosid'">
                <el-tag effect="dark" type="info">{{ to10(idObj.mach1) }}</el-tag> -
                <el-tag effect="dark" type="info">{{ to10(idObj.mach2) }}</el-tag>
            </span>
            -
            <el-tag effect="dark" type="warning">{{ to10(idObj.seq) }}</el-tag>
        </el-descriptions-item>

        <el-descriptions-item label="起始时间">
            <el-tag effect="dark">{{ idObj.timeBegin }}（代码中固定）</el-tag> -
            <el-tag>{{ idObj.timeBeginHuman }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="主键时间">
            <el-tag effect="dark">{{ idObj.timeNow }}（加上偏移量）</el-tag> -
            <el-tag>{{ idObj.timeNowHuman }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="源代码类">{{ idObj.infoCode }}
        </el-descriptions-item>
        <el-descriptions-item label="项目代码">
            <el-link type="primary" :href="idObj.infoSite" target="_blank">{{ idObj.infoSite }}</el-link>
        </el-descriptions-item>
        <!-- 
        <el-descriptions-item label="备注">
            CosId的机器号从redis中在线获取并从0开始递增。MybatisPlus的Sequence的机器号由数据中心（5位）+ 机器标识（5位）组成。
        </el-descriptions-item>
        -->
    </el-descriptions>
</template>

<script setup>
import { reactive, watchEffect } from 'vue';

const initObj = reactive({})
const idObj = reactive({})
initObj.type = 'mpsequence'
initObj.value = '400004000040000400'

// utools插件中初始化id值
if (window.utools) {
    window.utools.onPluginEnter(({ code, type, payload }) => {
        //console.log('用户进入插件应用', code, type, payload)
        if (code == 'id' && type == 'regex') {
            initObj.value = payload
        }
    })
}

// 监控数据变化，跟随变化
watchEffect(() => {
    // 二进制
    idObj.binary = BigInt(initObj.value).toString(2).padStart(64, '0')

    // 符号位、时间戳、机器号、自增值
    idObj.sign = idObj.binary.substring(0, 1)
    idObj.time = idObj.binary.substring(1, 42)
    idObj.mach = idObj.binary.substring(42, 52)
    idObj.seq = idObj.binary.substring(52)

    // MybatisPlus的mach，分为两部分: 数据中心(5) + 机器号(5)
    idObj.mach1 = idObj.mach.substring(0, 5)
    idObj.mach2 = idObj.mach.substring(5, 10)

    // 时间戳起始值: 代码中写死的
    // cosid      : 2019-12-25 00:00:00  1577203200000L me.ahoo.cosid.CosId
    // mybatisPlus: 2010-11-04 09:42:54  1288834974657  com.baomidou.mybatisplus.core.toolkit.Sequence
    idObj.timeBegin = initObj.type == 'cosid' ? 1577203200000 : 1288834974657
    idObj.timeBeginHuman = initObj.type == 'cosid' ? '2019-12-25 00:00:00' : '2010-11-04 09:42:54'
    idObj.infoCode = initObj.type == 'cosid' ? 'me.ahoo.cosid.CosId' : 'com.baomidou.mybatisplus.core.toolkit.Sequence'
    idObj.infoSite = initObj.type == 'cosid' ? 'https://cosid.ahoo.me/guide/#分布式id分配方案' : 'https://gitee.com/yu120/sequence'

    // 时间格式
    idObj.timeNow = parseInt(to10(idObj.time)) + idObj.timeBegin
    idObj.timeNowHuman = formatDate(idObj.timeNow)

})

// 转换为十进制
function to10(binaryValue) {
    return BigInt('0b' + binaryValue).toString()
}

function formatDate(timeStamp) {
    var date = new Date(timeStamp);
    var year = date.getFullYear(),
        month = date.getMonth() + 1,//月份是从0开始的
        day = date.getDate(),
        hour = date.getHours(),
        min = date.getMinutes(),
        sec = date.getSeconds();
    var newTime = year + '-' +
        (month < 10 ? '0' + month : month) + '-' +
        (day < 10 ? '0' + day : day) + ' ' +
        (hour < 10 ? '0' + hour : hour) + ':' +
        (min < 10 ? '0' + min : min) + ':' +
        (sec < 10 ? '0' + sec : sec);

    return newTime;
}
</script>