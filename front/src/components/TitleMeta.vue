<template>
  <div class="title-meta">
    <H2Title icon="ios-cog" title="详细配置" />

    <Form ref="meta" :model="meta" :label-width="60" class="form" >
      <Row>
        <Col :span="12">
          <FormItem label="标题" label-for="columnTitle" prop="columnTitle">
            <Input element-id="columnTitle" v-model="meta.columnTitle" clearable />
          </FormItem>
        </Col>
        <Col :span="12">
          <FormItem label="格式" label-for="format" prop="format">
            <Input element-id="format" v-model="meta.format" clearable :disabled="meta.format == null" />
          </FormItem>
        </Col>
      </Row>

      <Row>
        <Col :span="12">
          <FormItem label="最小值" label-for="min" prop="min">
            <Input element-id="min" v-model="meta.min" clearable :disabled="meta.min == null" />
          </FormItem>
        </Col>
        <Col :span="12">
          <FormItem label="最大值" label-for="max" prop="max">
            <Input element-id="max" v-model="meta.max" clearable :disabled="meta.max == null" />
          </FormItem>
        </Col>
      </Row>

      <Row>
        <Col :span="12">
          <FormItem label="前缀" label-for="prefix" prop="prefix">
            <Input element-id="prefix" v-model="meta.prefix" clearable />
          </FormItem>
        </Col>
        <Col :span="12">
          <FormItem label="后缀" label-for="suffix" prop="suffix">
            <Input element-id="suffix" v-model="meta.suffix" clearable />
          </FormItem>
        </Col>
      </Row>

      <FormItem label="枚举值" label-for="code" prop="code">
        <Input element-id="code" v-model="meta.code" clearable :disabled="meta.code == null" />
      </FormItem>

      <Row>     
        <FormItem label="多选" prop="codeMulti">
          <Checkbox v-model="meta.codeMulti" :disabled="meta.code == null"/>
        </FormItem>   

        <Button type="primary" style="margin-left: 20px" @click="save" :disabled="meta.isModified === undefined">保存</Button>
        <Button type="warning" style="margin-left: 20px" @click="back" :disabled="meta.isModified === undefined">还原</Button>
      </Row>
    </Form>
  </div>
</template>

<script>
import H2Title from "./H2Title"

export default {
  name: 'TitleMeta',
  components: {
    H2Title
  },
  data(){
    return {
      meta: {      
        min: null,
        max: null,
        columnTitle:'',
        format: '',
        prefix:'',
        suffix:'',
        code: null,
        codeMulti: false,
      },
    }
  },
  methods: {
    save(){

    },
    back(){

    }
  },
  mounted() {
    this.$bus.$on("hoverGen", (gen) => {
      this.meta = gen;
    });
    this.$bus.$on("clickColumn", (meta) => {
      this.meta = meta;
    });
  },
  beforeDestroy(){
    this.$bus.$off("hoverGen")
    this.$bus.$off("clickColumn")
  }
};
</script>

<style lang="less" scoped>
.title-meta {
  width: 450px;
  .form {
    padding-right: 25px;
  }
}

.ivu-form-item {
  margin-bottom: 8px;
}
</style>