<template>
  <div class="footer-table">
    <span  class="prompt" v-if="columns.length == 0">请点击上方生成器，生成所需模拟数据...</span>
    <Table class="table"  v-else :columns="columns" :data="rows" stripe border :max-height="360"></Table>
  </div>
</template>

<script>
import { nanoid } from "nanoid";

export default {
  name: "FooterTable",
  data() {
    return {
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
        gen,  
        meta: {...gen, isModified: true},

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
            style: { display: 'flex'} // 横向排列且让标题自增长, 关闭按钮大小固定
          }, [
            h('div',{
                style: { color: gen.color },
                'class': ['thTitleClass'],
                on: { click: () => { this.clickColumn(params)} }
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

    // 点击某一列
    clickColumn(params){
      this.$bus.$emit("clickColumn", params.column.meta);
      return false
    },

    // 关闭某一列
    deleteColumn(params) {
      const key = params.column.key
      this.columns = this.columns.filter(c => c.key != key)
      this.rows.forEach(r => delete r[key])
      if (this.columns.length == 1) {
        this.columns = []
      }
      return false
    }
  },
  mounted() {
    this.$bus.$on("addColumn", this.addColumn);
  },
  beforeDestroy(){
    this.$bus.$off("addColumn")
  }
};
</script>

<style lang="less">
.footer-table {

  .prompt {
    display: block;
    text-align: center;
    padding-top: 100px;
    color: #aaaaaa;
    font-size: 18px;
    font-style: italic;
  }

  .table {
    width: 100%;

    .thTitleClass {
      cursor: pointer;
      flex-grow: 1;
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