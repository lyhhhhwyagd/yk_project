<template>
    <div  >
        <div class="card" style="margin-bottom: 10px ;" >
            <div  >
                <el-input style="width: 260px" v-model="data.problemname"  placeholder="请输入题目名称" :prefix-icon="Search"/>

                <el-input style="width: 260px" v-model="data.teacherid"  placeholder="请输入教师名称" :prefix-icon="Search"/>


                <el-button type="primary" style="margin-left :10px;background-color: #76dc30" @click="load">查询</el-button>

                <el-button type="info" @click="reset">重置</el-button>
            </div>
        </div>

        <div class="card" style="margin-bottom: 10px">
            <div style="margin-bottom: 10px">
                <el-button  style="background-color: #76dc30" type="info" @click="exportAll">导出</el-button>
                <el-button  style="background-color: #76dc30" type="info" @click="handlePrint">打印</el-button>
            </div>
            <div>
                <el-table  :data="data.tableData" :default-sort="{ prop: 'id', order: 'descending' }" >
                    <el-table-column label="编号" sortable prop="id"></el-table-column>
                    <el-table-column label="题目" prop="problemname" width="180"></el-table-column>
                    <el-table-column label="出题老师" prop="teacherid" width="180"></el-table-column>
                    <el-table-column label="时间" prop="time"></el-table-column>

                    <el-table-column >
                        <template #default="scope">
                            <el-button style="background-color: #76dc30;margin-right: 0" type="primary"  @click="handleEdit(scope.row)">答题</el-button>
                        </template>
                    </el-table-column>

                </el-table>
            </div>
        </div>

        <div class="card">
            <el-pagination v-model:current-page="data.pageNum" v-model:page-size="data.pageSize"
                           @current-change=" handleCurrentChange"
                           background layout="prev, pager, next" :total="data.total"/>
        </div>
        <el-dialog title="题目信息" width="40%" v-model="data.radioVisible" height="60%" >
          {{data.form.problemdes}}
        <el-radio-group v-model="radio">
            <el-radio :label="1">{{data.form.a}}</el-radio>
            <el-radio :label="2">{{data.form.b}}</el-radio>
            <el-radio :label="3">{{data.form.c}}</el-radio>
            <el-radio :label="4">{{data.form.d}}</el-radio>
        </el-radio-group>
            <div class="dialog-footer" v-html="data.scores">
            </div>

            <div class="dialog-footer">

                <el-button style="background-color: #76dc30" type="primary" @click="save(radio)">提交</el-button>
        <el-button  @click="data.radioVisible = false">取 消</el-button>
      </div>
        </el-dialog>





    </div>
    <div id="main" style="width: 100%; height: 300px">

    </div>
</template>

<script setup>

    //import request from "@/utils/request";
    import {reactive} from "vue";
    import request from "../../utils/request";
    import {ElMessage} from "element-plus";
    import {ElMessageBox} from "element-plus";
    import * as echarts from 'echarts';
    import {onMounted} from "vue";
    import printJS from 'print-js';
    // request.get('/').then(res => {
    //     console.log(res)
    // })
    import { ref } from 'vue'
    const radio = ref(1)
    const data = reactive({
        // courseName:"",
        scores:"",
        myans:"1",
        userid:"1",
        problemname:"",
        teacherid:"",
        tableData: [{
            "problemdes":"","id":"","problemname":"","a":"1","b":"3","c":"1","d":"1","ans":"","teacherid":"1","time":"1"}],
        total:0,
        pageSize:4,//当前最大个数
        pageNum:1,
        formVisible:false,
        form:{},
        radioVisible:false,
    })
    const load=() =>{
        request.get("/problem/selects", {
            params:
                {
                    pageNum: data.pageNum,
                    pageSize: data.pageSize,
                    problemname:data.problemname,
                    teacherid:data.teacherid
                }
        }).then(res =>{
            //console.log(res)
            data.tableData=res.data.list
            data.total=res.data.total || 0
        })

    }

    const  handleCurrentChange =()=>
    {
        //翻页时重新加载
        load();
    }
    const reset =()=>
    {
        data.problemname="";
        data.teacherid="",
            load();
    }
    const save = (radio) =>
    {
        console.log(data.form.id);
        let option;
        if(radio==1)
        {
             option ="A";
        }
        if(radio==2)
        {
             option ="B";
        }
        if(radio==3)
        {
           option ="C";
        }
        if(radio==4)
        {
          option ="D";
        }
        console.log(option);
        if(option==data.form.ans)
        {
            //option="回答正确";
            console.log("yes");
        }
        else
        {
            //option="回答错误";
            console.log("no");
        }
        var url = window.location.href ;             //获取当前url
        var cs = url.split('?')[1];                //获取?之后的参数字符串
        var cs_arr = cs.split('&');                    //参数字符串分割为数组
        var cs1={};
        for(var i=0;i<cs_arr.length;i++){         //遍历数组，拿到json对象

            cs1[cs_arr[i].split('=')[0]] = cs_arr[i].split('=')[1]

        }
        request.request({
            url: '/ans/add',
            method: 'POST',
            data: {problemid:data.form.id,userid:cs1.userID,myans:option}
        }).then(res=>{
            if(res.code=="200")
            {
                ElMessage.success("操作成功");
                data.formVisible=false;
                load();
             //   aa();
            }
            else
            {
                ElMessage.error("请勿重复作答");
            }
        })
        data.radioVisible = false;
    }


    const handleEdit = (row) => {
        data.form = JSON.parse(JSON.stringify(row));
        data.radioVisible = true;
        var url = window.location.href ;             //获取当前url
        var cs = url.split('?')[1];                //获取?之后的参数字符串
        var cs_arr = cs.split('&');                    //参数字符串分割为数组
        var cs1={};
        for(var i=0;i<cs_arr.length;i++){         //遍历数组，拿到json对象

            cs1[cs_arr[i].split('=')[0]] = cs_arr[i].split('=')[1]

        }
        data.scores=null;
        request.get("/ans/query", {
            params:
                {
                    problemid:row.id,
                    userid:cs1.userID,
                }
        }).then(res =>{
            console.log(res);
            data.scores=res.data==null?null:res.data.myans;
            if(data.scores==data.form.ans) data.scores="回答正确 "+"正确答案:"+data.form.ans;
            else if(data.scores!=null && data.scores!=data.form.ans) data.scores="回答错误,"+" 你的答案: "+data.scores+"正确答案:"+data.form.ans;
            console.log(data.scores);
        })

    }

    const exportAll =()=>
    {
        window.open("http://localhost:8080/problem/export")
    }
    //调用方法获取后台
    load()

    // onMounted( () => {
    //     setTimeout(() => {aa()}, 1000)
    // })

    const aa =()=> {
        var option;
        option = {
            xAxis: {
                type: 'category',
                data: ["计算机学院","软件学院","数学学院","文学与新闻学院"]
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    data: [],
                    type: 'line'
                },
                {
                    data: [],
                    type: 'bar'
                },

            ]
        };
        var chartDom = document.getElementById('main');
        var myChart = echarts.init(chartDom);
        request.get("/problem/map").then(res =>{
            if(res.data) {
                //option.xAxis.data = res.data.x;
                option.series[0].data = res.data;
                option.series[1].data = res.data;
                myChart.setOption(option);
            }
        })

    }
    const handlePrint=()=> {
        printJS({
            header: "习题表",
            type: "json",
            properties: [
                { field: "id", displayName: "编号" },
                { field: "problemname", displayName: "题目" },
                { field: "problemdes", displayName: "题目描述" },
                { field: "teacherid", displayName: "出题老师" },
                { field: "a", displayName: "a选项" },
                { field: "b", displayName: "b选项" },
                { field: "c", displayName: "c选项" },
                { field: "d", displayName: "d选项" },
                { field: "ans", displayName: "答案" },
                { field: "time", displayName: "时间" },
            ],
            printable: data.tableData,
        });
    }
</script>
