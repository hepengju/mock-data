<template>
  <div class="footer-table">
    <span  class="prompt" v-if="columns.length == 0">请点击上方生成器，生成所需模拟数据...</span>
    <Table class="table"  v-else :columns="columns" :data="rows" stripe border :max-height="360"></Table>
  </div>
</template>

<script>
import { nanoid } from "nanoid";
import { getData } from "@/apis";

export default {
  name: "FooterTable",
  data() {
    return {
      columns: [],
      rows: [{},{},{},{},{},   {},{},{},{},{}],
    };
  },

  methods: {
    addGen(gen) {
      getData(gen.name).then((sampleList) => {
        const key = nanoid()

        // 数据
        for (let i = 0; i < 10; i++) {
          this.rows[i][key] = sampleList[i]
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

          meta: {...gen, isModified: true}, // 记录着这个生成器的相关配置, 以便配置修改

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
              }
            }, [
              h('div',{
                style: {
                  color: gen.color,
                  cursor: 'pointer',
                  flexGrow: 1,
                  padding: '8px 18px', // 样式 .ivu-table th 下的padding被关闭, 在这里补回来(原因: 点击事件的范围)
                },
                on: {
                  click: () => { this.clickTitle(params.column) }
                }
              }, params.column.title),

              h('Icon', {
                props: {
                  type: "md-close"
                },
                style: {
                  fontSize: '24px',
                  cursor: 'pointer',
                  padding: '8px',
                  borderLeft: '1px solid #e8eaec'
                }
              })
            ])
          }
        })
      });
    },

    clickTitle(column){
      console.log(111, column)

      return false
    }
  },

  mounted() {
    this.$bus.$on("addGen", this.addGen);
  },
  beforeDestroy(){
    this.$bus.$off("addGen")
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