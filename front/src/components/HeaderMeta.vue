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

                <el-button type="primary" @click="confirmForm" :disabled="meta.isModified === undefined">确定
                </el-button>
                <el-button type="danger" @click="resetForm" :disabled="meta.isModified === undefined">重置</el-button>
                <el-button type="info" @click="dialogVisible = true" v-show="meta.isModified && meta.name == 'script'">
                    编辑脚本</el-button>
            </el-row>
        </el-form>
    </Card>

    <el-dialog v-model="dialogVisible" draggable title="编辑脚本" width="60%">
        <v-ace-editor v-model:value="meta.script" lang="javascript" theme="gruvbox" style="height: 300px" />
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="dialogVisible = false">评估</el-button>
                <el-button type="primary" @click="confirmScript">确定</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script setup>
import { VAceEditor } from 'vue3-ace-editor';
import 'ace-builds/src-noconflict/mode-javascript';
import 'ace-builds/src-noconflict/theme-gruvbox';
import ace from 'ace-builds';

import { ref } from 'vue';
import { HOVER_GEN, UPDATE_META } from '../consts';
import bus from '../plugins/bus';

// ACE脚本编辑器
// https://github.com/CarterLi/vue3-ace-editor/tree/gh-pages/demo-source
// https://www.cnblogs.com/China-Dream/p/13883153.html

// ace.config.set('showLineNumbers', false)
ace.config.set('fontSize', 20)

defineProps(['name', 'title', 'width'])

const dialogVisible = ref(false)
const meta = ref({
    columnTitle: '',
    columnName: '',
    min: null,
    max: null,
    prefix: '',
    suffix: '',
    format: '',
    code: null,
    codeMulti: false,
    color: '#fff',
    script: ''
})

// 此处的备份和保存, 都采用复制的操作
let bakMeta = {}
bus.on(HOVER_GEN, gen => {
    bakMeta = { ...gen }
    meta.value = { ...gen }
})

function confirmForm() {
    bus.emit(UPDATE_META, { ...meta.value })
}

function resetForm() {
    meta.value = { ...bakMeta }
}

function confirmScript() {
    dialogVisible.value = false
    bus.emit(UPDATE_META, { ...meta.value })
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