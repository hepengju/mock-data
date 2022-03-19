<template>
  <div id="app">
    <div id="title">
        <div id="title_inner" >
            <GenCard icon="ios-calendar-outline" title="日期数据" type="info" width="300px" :gens="dataList.date_number"/>
            <Card class="title_div title_date" :title="'日期数字（' + dataList.date_number.length + '）'"  icon="ios-calendar-outline">
                <ul>
                    <li class="title_btn_li" v-for="item in dataList.date_number" :key="item.name">
                        <Button type="info" @click="genClick">
                            {{item.columnTitle}}
                        </Button>
                    </li>
                </ul>
            </Card>
            <Card class="title_div title_character" title="字符" icon="ios-construct-outline">
                <ul class="title_btn_ul">
                    <li class="title_btn_li" v-for="item in dataList.string" :key="item.name">
                        <Button type="success" class="title_div_li title_div_li_string"  @click="genClick" @mouseover.native="mouseFn(item)">
                            {{item.columnTitle}}
                        </Button>
                    </li>
                </ul>
            </Card>
            <Card class="title_div title_custom" title="定制化" icon="ios-construct">
                <h4>定制生成器</h4>
                <ul class="title_btn_ul">
                    <li class="title_btn_li" v-for="item in dataList.custom" :key="item.name">
                        <Button type="warning" class="title_div_li title_div_li_custom"  @click="genClick"  @mouseover.native="mouseFn(item)">
                            {{item.columnTitle}}
                        </Button>
                    </li>
                </ul>
            </Card>
        </div>
        <!-- <div id="tooltip" class="tooltip">
            <h4>样例数据</h4>
            <div class="demo_tooltip">
                <ul class="tooltip_ul">
                    <li class="tooltip_li" v-for="(item,index) in dataTooltip">{{item.toString()}}</li>
                </ul>
            </div>
        </div> -->
        <!-- <div id="title_config">
            <h4>详细配置区</h4>
            <Form ref="configForm" :model="configForm" :label-width="85">
                <Row>
                    <Col :span="10">
                        <FormItem label="最小值" prop="min">
                            <Input class="title_config_input" v-model="configForm.min" clearable :disabled="configForm.min == null"></Input>
                        </FormItem>
                    </Col>
                    <Col :span="10">
                        <FormItem label="最大值" prop="max">
                            <Input class="title_config_input" v-model="configForm.max" clearable :disabled="configForm.max == null"></Input>
                        </FormItem>
                    </Col>
                </Row>
                <Row>
                    <Col :span="10">
                        <FormItem label="指定标题" prop="columnTitle">
                            <Input class="title_config_input" v-model="configForm.columnTitle" clearable :disabled="configForm.columnTitle == null"></Input>
                        </FormItem>
                    </Col>
                    <Col :span="10">
                        <FormItem label="格式" prop="format">
                            <Input class="title_config_input" v-model="configForm.format" clearable :disabled="configForm.format == null"></Input>
                        </FormItem>
                    </Col>
                </Row>
                <Row>
                    <Col :span="10">
                        <FormItem label="前缀" prop="prefix">
                            <Input class="title_config_input" v-model="configForm.prefix" clearable :disabled="configForm.prefix == null"></Input>
                        </FormItem>
                    </Col>
                    <Col :span="10">
                        <FormItem label="后缀" prop="suffix">
                            <Input class="title_config_input" v-model="configForm.suffix" clearable :disabled="configForm.suffix == null"></Input>
                        </FormItem>
                    </Col>
                </Row>
                <Row>
                    <Col :span="10">
                        <FormItem label="枚举值" prop="code">
                            <Input class="title_config_input" v-model="configForm.code" clearable :disabled="configForm.code == null"></Input>
                        </FormItem>
                    </Col>
                    <Col :span="12">
                        <FormItem label="枚举值多选" prop="codeMulti">
                            <Checkbox v-model="configForm.codeMulti" :disabled="configForm.code == null">多选</Checkbox>
                        </FormItem>
                    </Col>
                </Row>
            </Form>
            <div class="title_config_button">
                <Button type="dashed" @click="restore" >还原</Button>
                <Button type="dashed" @click="save">保存</Button>
            </div>
        </div> -->
    </div>

    <!-- <div class="button">
        <Button class="button_item" v-for="(item, index) in btnList" :type="item.type" @click="btnClick">{{item.label}}</Button>
        <Tooltip content="操作步骤提示" placement="top" theme="light">
            <Button  icon="md-help" @click="guide"></Button>
        </Tooltip>
        <div id="download">
            <Dropdown @on-click="downloadFile">
                <Button type="success" :disabled = "disabled">
                    下载<span v-if = "disabled">({{this.number}})</span>
                    <Icon type="ios-arrow-down"></Icon>
                </Button>
                <DropdownMenu slot="list" v-if="!disabled">
                    <DropdownItem v-for="(item,index) in downloadMode" :name="item">{{item}}</DropdownItem>
                </DropdownMenu>
            </Dropdown>
            <section class="download_input">
                <span class="download_span">下载行数</span>
                <InputNumber v-model="downloadNumber" style="width: 100px" :max="10000" :min="1"></InputNumber>
                <span class="download_span">文件名称</span>
                <Input v-model="fileName" style="width: 100px"></Input>
                <span class="download_span">表名称</span>
                <Input v-model="tableName" style="width: 100px"></Input>
            </section>
        </div>
    </div>

    <div class="prompt" v-if="showTable">
        <span>请选择生成器生成所需数据...</span>
    </div>
    <div class="content" v-else>
        <Table :columns="columns" :data="data" size="small" ref="data_table" tooltip-theme="light"></Table>
    </div> -->
  </div>
</template>

<script>
import GenCard from './components/GenCard'

export default {
  name: 'App',
  components: {
      GenCard
  },
  data () {
    return {
        showTable:true,
        dataTooltip:'',
        btnList:[
            // {label: '保存',       type:'info'},
            {label: '刷新',       type:'primary'},
            {label: '删除全部',   type:'warning'},
        ],
        downloadMode:[
            'csv',
            'tsv',
            'sql',
            'excel'
        ],
        configForm:{
            columnTitle:'',
            min: '',
            max: '',
            code: '',
            format: '',
            codeMulti: false,
            prefix:'',
            suffix:'',
        },
        downloadNumber:1000,
        fileName:'用户表',   // 文件名
        tableName:'z010_user',  //表名
        columns: [],
        data: [],
        dataTmp:[],
        dataList:[],
        selectColumn:[], //选中的列
        currentColumn:[], // 当前列
        metaList: [],  //  json数组
        currentIndex:'', //当前的下标
        count:1 , //   key+count 列的唯一键
        number:10, //下载倒计时
        disabled:false,
    }
  },
  methods: {
    btnClick(event) {
        let val = event.target.innerText;
        switch (val) {
            // case '保存':
            //     break;
            case '刷新':
                this.refresh();
                break;
            case '删除全部':
                this.deleteAll();
                break;
        }
    },

    //蒙层指引
    guide(){
        introJs().setOptions({
            prevLabel:"上一步",
            nextLabel:"下一步",
            skipLabel:"跳过",
            doneLabel:"结束",
            exitOnOverlayClick: false,// 是否允许点击空白处退出，默认true：允许
            showStepNumbers: true,// 是否显示说明的数据步骤  默认true:显示
            //对应的数组，顺序出现每一步引导提示
            steps: [
                {
                    //第一步引导
                    //这个属性类似于jquery的选择器， 可以通过jquery选择器的方式来选择你需要选中的对象进行指引
                    element: '#title_inner',
                    //这里是每个引导框具体的文字内容，中间可以编写HTML代码
                    intro: '选择所需生成器',
                    //这里可以规定引导框相对于选中对象出现的位置 top,bottom,left,right
                    position: 'bottom',
                },
                {
                    //第二步引导
                    element: '#title_config',
                    intro: '点击生成器表头可进行详细配置',
                    position: 'bottom'
                },
                {
                    //第二步引导
                    element: '#download',
                    intro: '设置下载行数、文件名、表名后，可进行下载',
                    position: 'bottom'
                }
            ]
        }).start()
    },

    // 样例数据展示
    mouseFn(item){
        this.dataTooltip = item.sampleData
    },

    refresh() {
        this.getCurrentColumns(this.columns, 'refresh');
        let params = {'metaList': this.metaList};
        params = JSON.stringify(params);
        this.$axios.post('refreshTable',params).then(res=>{
            let newData = res.data.data;
            for (let j = 0; j < newData.length ; j++) {
                for (let i = 0; i < this.metaList.length; i++) {
                    let newKey = this.metaList[i].columnKey;
                    this.$set(this.data[j],newKey,newData[j][newKey])
                }
            }
        })
    },

    // 显示配置
    showConfig(item) {
        this.currentColumn = [];
        this.currentColumn.push(item.column);
        this.selectColumn = [];
        this.$set(item.column,['className'],'demo-table-info-column');
        for (let key in this.configForm){
            this.configForm[key] = item.column[key];
            this.configForm.columnTitle = item.column.title;
        }
        this.selectColumn.push(item.column);
        this.currentIndex = item.index;
    },

    // 保存配置
    save() {
        if (this.columns.length == 0) {
            this.$Message.warning({
                content:'暂无生成器可保存，请选择所需生成器！'
            });
            return
        }
        if (this.selectColumn.length == 0) {
            this.$Message.warning({
                content:'请选择需要详细配置的生成器！'
            });
            return
        }
        this.count++;
        for (let key in this.configForm){
            this.columns[this.currentIndex][key] =this.configForm[key] ;
        }
        this.columns[this.currentIndex].title = this.configForm.columnTitle;
        // this.columns[this.currentIndex].key =  this.columns[this.currentIndex].key + this.count;
        let nameStr = this.columns[this.currentIndex].key.match(/[\u4e00-\u9fa5]{2,}/g).toString(); //用正则把文字匹配出来
        this.columns[this.currentIndex].key = nameStr  + this.count;
        this.selectColumn = [];
        this.selectColumn.push(this.columns[this.currentIndex]);
        this.singleRestore(this.selectColumn[0])
    },

    // 刷新单列
    singleRestore(val) {
        let otherObj = {
            columnKey: val.key,
            columnName:val.name,
            name: val.name,
            type: val.type,
        };
        let metaObj = Object.assign({},otherObj,this.configForm);
        this.metaList.push(metaObj);
        let params = JSON.stringify({'metaList':this.metaList});
        this.$axios.post('refreshTable',params)
            .then(res => {
                this.metaList = [];
                let newData = res.data.data;
                let newkey =  val.key;
                for (let i = 0; i < newData.length ; i++) {
                    this.$set(this.data[i],newkey,newData[i][newkey])
                }
            });
        this.$set(this.selectColumn[0],['className'],'demo-table-info-column');
    },

    // 还原配置
    restore() {
        if (this.columns.length == 0) {
            this.$Message.warning({
                content:'暂无生成器可还原，请选择所需生成器！'
            });
            return
        }
        if (this.selectColumn.length == 0) {
            this.$Message.warning({
                content:'请选择需要还原的生成器！'
            });
            return
        }
        for (let item in this.configForm){
            this.configForm[item] = this.currentColumn[0][item]
        }
        this.configForm.columnTitle = this.currentColumn[0].title;
        this.columns[this.currentIndex].title = this.currentColumn[0].title;
        this.columns[this.currentIndex].key =  this.currentColumn[0].key ;
        this.singleRestore(this.currentColumn[0])
    },

    // 点击各个生成器生成样例数据
    genClick(event){
        this.showTable = false;
        let name = event.target.innerText;
        let dataArr = this.dataList.date_number.concat(this.dataList.string, this.dataList.custom);
        dataArr.forEach(item => {
            if(name == item.columnTitle) {
                this.columns.push({
                    title: name,
                    key: name,
                    renderHeader:(h,params) => {
                        return h('div',[
                            h('span',{
                                style:{
                                    cursor:'pointer',
                                },
                                on: {
                                    click: ()=>{
                                        this.showConfig(params)
                                    },
                                }
                            }, params.column.title),
                            h('icon',{
                                props: {
                                    type: 'ios-close',
                                    size: '18',
                                },
                                style:{
                                    cursor:'pointer'
                                },
                                on: {
                                    click: () => {
                                        this.deleteCol(params)
                                    },
                                }
                            })
                        ])
                    },
                    min: item.min,
                    max: item.max,
                    code: item.code,
                    codeMulti: item.codeMulti,
                    format:item.format,
                    prefix:item.prefix,
                    suffix:item.suffix,
                    name: item.name,
                    type:item.type,
                    ellipsis:true,
                    tooltip:true,
                    minWidth:100,
                });
            }
        });

    },

    // 删除列
    deleteCol(params) {
        this.columns.splice(params.index,1);
        this.count = 0;
        if (this.columns.length == 0){
            this.showTable = true
        }
    },

    //删除全部列
    deleteAll() {
        if (this.columns.length == 0) {
            this.$Message.warning({
                content:'暂无生成器可删除，请选择所需生成器！'
            });
            return
        }
        this.$Modal.confirm({
            title: `系统提示`,
            content: `确认删除全部列吗？`,
            onOk: ()=>{
                this.columns = [];
                this.showTable = true;
            },
            onCancel: ()=>{
            }
        });
    },

    // 生成 dataTmp 空对象
    makeData () {
        for (let i = 0 ; i < 10 ; i++) {
            let obj = {};
            this.dataTmp.push(obj)
        }
    },

    //封装各个模板数据
    packageData() {
        // 数字日期生成器
        for (let i = 0 ; i < this.dataList.date_number.length; i++) {
            // 向dataTmp 10条对象里添加属性名值对
            for (let j = 0 ; j < this.dataList.date_number[i].sampleData.length; j++) {
                this.dataTmp[j][this.dataList.date_number[i].columnTitle] = this.dataList.date_number[i].sampleData[j];
            }
        }
        // 字符生成器
        for (let i = 0 ; i < this.dataList.string.length ; i++) {
            for (let j = 0 ; j < this.dataList.string[i].sampleData.length; j++) {
                this.dataTmp[j][this.dataList.string[i].columnTitle] = this.dataList.string[i].sampleData[j]
            }
        }
        // 定制生成器
        for (let i = 0 ; i < this.dataList.custom.length ; i++) {
            for (let j = 0 ; j < this.dataList.custom[i].sampleData.length; j++) {
                this.dataTmp[j][this.dataList.custom[i].columnTitle] = this.dataList.custom[i].sampleData[j]
            }
        }
        this.data = JSON.parse(JSON.stringify(this.dataTmp));
    },

    // 获取当前表格列信息
    getCurrentColumns(val, event) {
        if (event == 'downLoad') {
            this.metaList = [];
            val.forEach(item => {
                this.metaList.push({
                    name: item.name,
                    min: item.min,
                    max: item.max,
                    code: item.code,
                    codeMulti: item.codeMulti,
                    format:item.format,
                    prefix:item.prefix,
                    suffix:item.suffix,
                    columnKey: item.key,
                    columnName:item.name,
                    columnTitle:item.title,
                    type:item.type,
                });
            });
        } else {
            this.count++;
            this.metaList = [];
            val.forEach(item => {
                item.key = item.key.match(/[\u4e00-\u9fa5]{2,}/g).toString() + this.count;
                this.metaList.push({
                    name: item.name,
                    min: item.min,
                    max: item.max,
                    code: item.code,
                    codeMulti: item.codeMulti,
                    format:item.format,
                    prefix:item.prefix,
                    suffix:item.suffix,
                    columnKey: item.key,
                    columnName:item.name,
                    columnTitle:item.title,
                    type:item.type,
                });
            });
        }
    },

    // 下载表格
    downloadFile(val) {
        this.number = 10;
        if (this.columns.length == 0) {
            this.$Message.warning({
                content:'暂无数据可下载，请先选择所需生成器生成数据！'
            });
            return
        }
        this.disabled = true;
        this.getCurrentColumns(this.columns, 'downLoad');
        let params = {'fileFormat':val,
            'sampleSize':this.downloadNumber,
            'metaList':this.metaList,
            'tableName':this.tableName,
            'fileName': this.fileName};
        params = JSON.stringify(params);
        this.$axios.post('downTable',params, {
            responseType: 'blob'
        }).then((res)=>{
        })
        let interval = setInterval( () => {
            this.number--;
            if (this.number == 0) {
                this.disabled = false;
                clearInterval(interval)
            }
        },1000)
    },

    //获取所有数据
    getData() {
        this.$axios.get('getGenMap')
            .then(res => {
                this.dataList = res.data.data;
                this.packageData();
            })
    },

    showModal(val){
        this.$Modal.warning({
            title: `系统提示`,
            content: val,
        });
    }
  },
  mounted: function () {
      this.makeData();
      console.log(this)
      this.getData();
  },
}
</script>

<style lang="less">
* {
    margin: 0;
    padding: 0;
    list-style: none;
}

#title {
    height: 286px;
    // display: flex;
}


/* #title_inner{
    display: flex;
    justify-content: space-evenly;
} */

#title_inner button{
    width: 100px;
    margin: 2px
}

#title_inner ul:nth-of-type(1) {
    width: 200px;
}

.title_div ul{
    display: flex;
    flex-wrap: wrap;
}

</style>
