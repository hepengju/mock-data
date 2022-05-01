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

    <el-dialog v-model="dialogVisible" draggable title="编辑脚本" width="50%" :before-close="handleClose">
        <v-ace-editor v-model:value="meta.script" lang="javascript" theme="iplastic"
             :options="{showLineNumbers : false, fontSize: 18}"
            style="height: 300px" />
        <div class="editor-seperator" v-show="evalResult">
            评估结果： {{ evalResult }}
        </div>

        <template #footer>
            <span class="dialog-footer">
                <el-button @click="cancelScript">取消</el-button>
                <el-button type="warning" @click="evalScriptNow">评估</el-button>
                <el-button type="primary" @click="confirmScript">确定</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script setup>
// ACE脚本编辑器
// https://github.com/CarterLi/vue3-ace-editor/tree/gh-pages/demo-source
// https://www.cnblogs.com/China-Dream/p/13883153.html
import { VAceEditor } from 'vue3-ace-editor';
import 'ace-builds/src-noconflict/mode-javascript';
import 'ace-builds/src-noconflict/theme-iplastic';
import { ref } from 'vue';
import { evalScript } from '../apis/';
import { HOVER_GEN, UPDATE_META } from '../consts';
import bus from '../plugins/bus';


defineProps(['name', 'title', 'width'])

const evalResult = ref('')
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

/**
 * 确认元数据, 并重新请求数据
 */
function confirmForm() {
    bus.emit(UPDATE_META, { ...meta.value })
}

/**
 * 重置元数据表格
 */
function resetForm() {
    meta.value = { ...bakMeta }
}

/**
 * 取消按钮
 */
function cancelScript() {
    dialogVisible.value = false
    evalResult.value = ''
}

/**
 * 关闭脚本编辑窗口后清空评估结果
 */
function handleClose(done) {
    evalResult.value = ''
    done()
}

/**
 * 评估脚本
 */
function evalScriptNow() {
    evalScript(meta.value.script).then(data => {
        if (typeof (data[0]) == 'string' && data[0].startsWith('脚本生成器错误')) {
            evalResult.value = data[0]
        } else {
            evalResult.value = data.join(' , ')
        }
    })
}

/**
 * 确认脚本后刷新数据
 */
function confirmScript() {
    dialogVisible.value = false
    evalResult.value = ''
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

.editor-seperator {
    margin-top: 20px;
    font-size: 16px;
    color: red;
}
</style>