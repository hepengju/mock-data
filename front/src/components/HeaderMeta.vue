<template>
    <Card :name="name" :title="title" class="meta">
        <el-form ref="formRef" :model="meta" label-width="60px">
            <el-row>
                <el-col :span="12">
                    <el-form-item label="标题" for="columnTitle">
                        <el-input :input-style="{ color: meta.color }" id="columnTitle" v-model="meta.columnTitle"
                            clearable />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="名称" for="columnName">
                        <el-input :input-style="{ color: meta.color }" id="columnName" v-model="meta.columnName"
                            disabled />
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row>
                <el-col :span="12">
                    <el-form-item label="最小" for="min">
                        <el-input :input-style="{ color: meta.color }" id="min" v-model="meta.min"
                            :disabled="meta.min == null" clearable />
                    </el-form-item>
                </el-col>

                <el-col :span="12">
                    <el-form-item label="最大" for="max">
                        <el-input :input-style="{ color: meta.color }" id="max" v-model="meta.max"
                            :disabled="meta.max == null" clearable />
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row>
                <el-col :span="12">
                    <el-form-item label="前缀" for="prefix">
                        <el-input :input-style="{ color: meta.color }" id="prefix" v-model="meta.prefix" clearable />
                    </el-form-item>
                </el-col>

                <el-col :span="12">
                    <el-form-item label="后缀" for="suffix">
                        <el-input :input-style="{ color: meta.color }" id="suffix" v-model="meta.suffix" clearable />
                    </el-form-item>
                </el-col>
            </el-row>

            <el-form-item label="格式" for="format">
                <el-input :input-style="{ color: meta.color }" id="format" v-model="meta.format"
                    :disabled="meta.format == null" clearable />
            </el-form-item>

            <el-form-item label="枚举" for="code">
                <el-input :input-style="{ color: meta.color }" id="code" v-model="meta.code"
                    :disabled="meta.code == null" clearable />
            </el-form-item>

            <el-row>
                <el-col :span="10">
                    <el-form-item label="多选">
                        <el-switch v-model="meta.codeMulti" :disabled="meta.code == null" />
                    </el-form-item>
                </el-col>

                <el-button type="primary" @click="confirmForm(meta)" :disabled="meta.isModified === undefined">确定
                </el-button>
                <el-button type="danger" @click="resetForm" :disabled="meta.isModified === undefined">重置</el-button>
                <el-button type="info" @click="modifyScript(meta)" v-show="meta.isModified && meta.name == 'script'">修改脚本</el-button>
            </el-row>
        </el-form>
    </Card>
</template>

<script setup>
import { ref } from 'vue';
import { HOVER_GEN, UPDATE_META } from '../consts';
import bus from '../plugins/bus';

defineProps(['name', 'title', 'width'])

let meta = ref({
    columnTitle: '',
    columnName: '',
    min: null,
    max: null,
    prefix: '',
    suffix: '',
    format: '',
    code: null,
    codeMulti: false,
    color: '#fff'
})

// 此处的备份和保存, 都采用复制1份的操作
let bakMeta = {}
bus.on(HOVER_GEN, gen => {
    bakMeta = { ...gen }
    meta.value = { ...gen }
})

function confirmForm(meta) {
    bus.emit(UPDATE_META, { ...meta })
}

function resetForm() {
    meta.value = { ...bakMeta }
}

function modifyScript(meta){
    // TODO 弹框显示js脚本(高亮), 且确定后保存到meta.script
}
</script>

<style lang="less" scoped>
.meta {
    flex: 0 0 400px;
}

.el-form-item {
    margin-bottom: 8px;

    input {
        color: blue;
    }
}
</style>