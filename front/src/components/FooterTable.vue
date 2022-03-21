<template>
  <div class="footer-table">
    <div class="center-line" v-show="columns.length > 0">
      <Button type="primary" @click.native="refreshAll" :disabled="processing">刷新表格</Button>
      <Button type="error" @click.native="deleteAll" :disabled="processing">删除全部</Button>

      <Form :model="config" :label-width="80">
        <Row>
          <Col :span="7">
            <FormItem prop="sampleSize" label-for="sampleSize" label="下载行数">
                <InputNumber element-id="sampleSize" :max="10000" :min="10" :step="100" v-model="config.sampleSize" :disabled="processing"/>
            </FormItem>
          </Col>

          <Col :span="7">
            <FormItem prop="fileName" label-for="fileName" label="文件名称">
                <Input element-id="fileName" type="text" v-model="config.fileName" :disabled="processing"/>
            </FormItem>
          </Col>

          <Col :span="6">
            <FormItem label="格式" label-for="fileFormat" :label-width="60">
              <Select v-model="config.fileFormat" :disabled="processing">
                <Option value="excel">Excel</Option>
                <Option value="sql">SQL</Option>
                <Option value="csv">CSV</Option>
                <Option value="tsc">TSV</Option>
              </Select>
            </FormItem>
          </Col>

          <FormItem :label-width="10">
              <Button type="primary" @click="downTable" :disabled="processing">下载<span v-if="processing">({{this.processingNumber}})</span></Button>
          </FormItem>
        </Row>
      </Form>
    </div>

    <span  class="prompt" v-if="columns.length == 0">请点击上方生成器，生成所需模拟数据...</span>
    <Table class="table"  v-else :columns="columns" :data="rows" stripe border :max-height="380"></Table>
  </div>
</template>

<script>
import { nanoid } from "nanoid"
import { getData, refreshTable, downTable } from '@/apis'

export default {
  name: "FooterTable",
  data() {
    return {
      config: {
        sampleSize: 1000,
        fileName: '用户表',
        fileFormat: 'excel'
      },
      processing: false,
      processingNumber: 0,
      columns: [],
      rows: [{},{},{},{},{},   {},{},{},{},{}],
    };
  },

  methods: {
    addColumn(gen) {
      const key = nanoid()

      // 数据
      for (let i = 0; i < 10; i++) {
        this.rows[i][key] = gen.sampleData[i]
      }

      // 标题
      if (this.columns.length == 0) {
        this.columns.push({
          type: 'index',
          width: 60,
          align: 'center'
        })          
      }

      this.columns.push({
        key: key,
        title: gen.columnTitle,
        minWidth: 100,
        ellipsis: true,

        // 保留原始的gen 和 可以修改的meta
        meta: {...gen, isModified: true, columnKey: key},

        // 自定义渲染数据和表头
        render: (h, params) => {
          return h('div', {
            style: {
              color: gen.color,
              paddingLeft: '18px',
              paddingRight: '18px'
            }
          }, params.row[key])
        },
        renderHeader: (h, params) => {
          return h('div', {
            style: { 
              display: 'flex'
            } // 横向排列且让标题自增长, 关闭按钮大小固定
          }, [
            h('div',{
                style: { color: gen.color },
                'class': ['thTitleClass'],
                attrs: {draggable: true},
                on: { mouseenter: () => { this.hoverGen(params)} }
              }, params.column.title),
            h('Icon', {
              props: { type: "md-close" },
              'class': ['thCloseClass'],
              on: { click: () => {this.deleteColumn(params)} }
            })
          ])
        }
      });
    },

    hoverGen(params){
      this.$bus.$emit("hoverGen", params.column.meta);
      return false
    },

    deleteColumn(params) {
      const key = params.column.key
      this.columns = this.columns.filter(c => c.key != key)
      this.rows.forEach(r => delete r[key])
      if (this.columns.length == 1) {
        this.columns = []
      }
      return false
    },

    updateMeta(meta) {
      const {name, min, max, format, prefix, suffix, code, codeMulti} = {...meta}
      const params = {sampleSize: 10, name, min, max, format, prefix, suffix, code, codeMulti}

      getData(params).then(data => {
        this.columns.forEach(c => {
          if (c.key != meta.columnKey) return
          c.meta = meta
          c.title = meta.columnTitle
        })

        for (let i = 0; i < 10; i++) {
          this.rows[i][meta.columnKey] = data[i]       
        }
      })
    },

    refreshAll(){
      const metaList = this.columns.map(c => c.meta).filter(m => m != null)

      refreshTable({
        metaList,
        sampleSize: 10
      }).then(data => {
        this.rows = data
      })

    },

    deleteAll(){
      this.$Modal.confirm({
          title: '系统提示',
          content: `确认删除全部列吗？`,
          onOk: ()=>{
            this.columns = []
            this.rows = [{},{},{},{},{},   {},{},{},{},{}]
          }
      });
    },

    downTable(){
      this.processingNumber = 10
      this.processing = true
      
      const metaList = this.columns
          .map(c => {
            let m = {...c.meta}
            delete m.sampleData // 减少参数的体积
            return m
          })
          .filter(m => m.name) // 去掉序号列

      downTable({...this.config, metaList}).then( res => {
          let timer = setInterval(() => {
              this.processingNumber--
              if (this.processingNumber == 0) {
                this.processing = false
                clearInterval(timer)
              }
            }, 1000)
      })
    },
  },
  mounted() {
    this.$bus.$on("addColumn", this.addColumn);
    this.$bus.$on("updateMeta", this.updateMeta)
  },
  beforeDestroy(){
    this.$bus.$off("addColumn")
    this.$bus.$off("updateMeta")
  }
};
</script>

<style lang="less">
.footer-table {

  .center-line {
    height: 40px;
    margin: 5px auto 8px;

    button {
      margin-left: 10px;
    }

    form {
      float: right;
      height: 40px;
    }
  }

  .prompt {
    display: block;
    text-align: center;
    padding-top: 200px;
    color: #aaaaaa;
    font-size: 18px;
    font-style: italic;
  }

  .table {
    width: 100%;

    .thTitleClass {
      cursor: pointer;
      flex-grow: 1;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      padding: 8px 18px; // 样式 .ivu-table th 下的padding被关闭, 在这里补回来(原因: 点击事件的范围)
      
      &:hover {
        background-color: #bfa;
      }
    }

    .thCloseClass {
      font-size: 24px;
      cursor: pointer;
      padding: 8px;
      border-left: '1px solid #e8eaec';
      cursor: pointer;
      
      &:hover {
        background-color: #bfa;
      }
    }

    // 解决设计界面中表格出现滚动条问题: 上面加入scoped则不生效!! 不知为何, 优先级? 
    .ivu-table td {
      height: 32px;
    }

    // 以下两个解决表头点击范围的问题: 此处去掉, 渲染内部时加回来
    .ivu-table th {
      padding: 0;
    }

    .ivu-table-cell {
      display: block;
      padding-left: 0;
      padding-right: 0;
    }
  }

}
</style>