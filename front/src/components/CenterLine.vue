<template>
  <div class="center-line">
    <Button type="primary">刷新</Button>
    <Button type="error">删除全部</Button>

    <Tooltip content="操作提示" placement="top" theme="light">
      <Button  icon="md-help" @click="guide"></Button>
    </Tooltip>
    
    <Form ref="config" :model="config" :label-width="80">
      <Row>
        <Col :span="7">
          <FormItem prop="sampleSize" label-for="sampleSize" label="下载行数">
              <InputNumber element-id="sampleSize" :max="10000" :min="10" :step="100" v-model="config.sampleSize" />
          </FormItem>
        </Col>

        <Col :span="7">
          <FormItem prop="fileName" label-for="fileName" label="文件名称">
              <Input element-id="fileName" type="text" v-model="config.fileName" />
          </FormItem>
        </Col>

        <Col :span="6">
          <FormItem label="格式" label-for="fileFormat" :label-width="60">
            <Select v-model="config.fileFormat">
              <Option value="excel">Excel</Option>
              <Option value="sql">SQL</Option>
              <Option value="csv">CSV</Option>
              <Option value="tsc">TSV</Option>
            </Select>
          </FormItem>
        </Col>

        <FormItem :label-width="10">
            <Button type="primary" @click="download(config)">下载</Button>
        </FormItem>
      </Row>
    </Form>
  </div>
</template>

<script>
export default {
  name: 'CenterLine',
  data() {
    return {
      config: {
        sampleSize: 1000,
        fileName: '用户表',
        fileFormat: 'excel'
      }
    }
  },
  methods: {
    refresh(){

    },

    deleteAll(){

    },

    guide(){
      
    },

    download(){

    }
  }
}
</script>

<style lang="less" scoped>
.center-line {
  height: 40px;
  overflow: hidden;
  margin: 10px auto;

  button {
    margin-left: 10px;
  }

  form {
    float: right;
  }
}
</style>