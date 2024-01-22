<template>
    <div v-if="data.userType=='学生'"> 此页面不对学生开放</div>
    <div  v-if="data.userType!='学生'">
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
                <el-button  style="background-color: #76dc30" type="primary" @click="handleAdd">新增</el-button>
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
                            <el-button style="background-color: #76dc30;margin-right: 0" type="primary"  @click="handleEdit(scope.row)">查看与编辑</el-button>
                        </template>
                    </el-table-column>
                    <el-table-column >
                        <template #default="scope">
                            <el-button style="margin-right: 0" type="danger"  @click="del(scope.row.id)">删除</el-button>
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

        <el-dialog title="题目信息" width="40%" v-model="data.formVisible" >
            <el-form :model="data.form" label-width="100px" style="padding-right: 50px">
                <el-form-item label="题目" prop="problemname">
                    <el-input v-model="data.form.problemname" autocomplete="off" />
                </el-form-item>
                <el-form-item label="题目描述" prop="problemdes">
                    <el-input v-model="data.form.problemdes" autocomplete="off" />
                </el-form-item>
                <el-form-item label="a选项" prop="a">
                    <el-input v-model="data.form.a" autocomplete="off" />
                </el-form-item>
                <el-form-item label="b选项" prop="b">
                    <el-input v-model="data.form.b" autocomplete="off" />
                </el-form-item>
                <el-form-item label="c选项" prop="c">
                    <el-input v-model="data.form.c" autocompleate="off" />
                </el-form-item>
                <el-form-item label="d选项" prop="d">
                    <el-input v-model="data.form.d" autocomplete="off" />
                </el-form-item>
                <el-form-item label="答案" prop="ans">
                    <el-input v-model="data.form.ans" autocomplete="off" />
                </el-form-item>
                <el-form-item label="出题老师" prop="teacherid">
                    <el-input v-model="data.form.teacherid" autocomplete="off" />
                </el-form-item>
                <el-form-item label="时间" prop="time">
                    <el-input v-model="data.form.time" autocomplete="off" />
                </el-form-item>
            </el-form>
            <template #footer>
      <span class="dialog-footer">
        <el-button  @click="data.formVisible = false">取 消</el-button>
        <el-button style="background-color: #76dc30" type="primary" @click="save">保 存</el-button>
      </span>
            </template>
        </el-dialog>



    </div>
    <div id="main" style="width: 100%; height: 300px" v-if="data.userType!='学生'">

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

    const data = reactive({
       // courseName:"",
        //dep:"",
        userType:'',
        "problemname":"",
        "teacherid":"",
        tableData: [{
            "problemdes":"","id":"","problemname":"","a":"1","b":"3","c":"1","d":"1","ans":"","teacherid":"1","time":"1"}],
        total:0,
        pageSize:4,//当前最大个数
        pageNum:1,
        formVisible:false,
        form:{}
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
    const handleAdd = () =>
    {

        data.form={};
        data.formVisible=true;
    }
    const save = () =>
    {
        console.log(data.form.id);
        request.request({
            url: data.form.id ? '/problem/update' :'/problem/add',
            method: data.form.id? 'PUT' :'POST',
            data: data.form
        }).then(res=>{
            if(res.code=="200")
            {
                ElMessage.success("操作成功");
                data.formVisible=false;
                load();
                aa();
            }
            else
            {
                ElMessage.error(res.msg);
            }
        })


    }


    const handleEdit = (row) => {
        data.form = JSON.parse(JSON.stringify(row));
        data.formVisible = true
    }
    const del = (id) => {
        ElMessageBox.confirm("删除后无法恢复，您确认删除吗",'删除确认',{ type: 'warning'}).then(res=>{
                request.delete("/problem/delete/"+id).then(res=>
                {
                    if(res.code=="200")
                    {
                        ElMessage.success("操作成功");
                        load();
                        aa();
                    }
                    else
                    {
                        ElMessage.error(res.msg);
                    }

                })
            }
        )

    }
    const exportAll =()=>
    {
        window.open("http://localhost:8080/problem/export")
    }
    //调用方法获取后台
    load()

    onMounted( () => {
        setTimeout(() => {aa()}, 1000)
    })


    const aa =()=> {
        var option;
        option = {
            xAxis: {
                type: 'category',
                data: ["答案A","答案B","答案C","答案D"]
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
    async  function getUserType()
    {
        try {

            var url = window.location.href ;             //获取当前url
            var cs = url.split('?')[1];                //获取?之后的参数字符串
            var cs_arr = cs.split('&');                    //参数字符串分割为数组
            var cs1={};
            for(var i=0;i<cs_arr.length;i++){         //遍历数组，拿到json对象

                cs1[cs_arr[i].split('=')[0]] = cs_arr[i].split('=')[1]

            }
            const userId = cs1.userID;
            if (!Number.isInteger(Number(userId))) {
                console.error('Invalid userID:', userId);
                return;
            }
            const response = await fetch(`http://localhost:8080/user/userType/${userId}`);

            // 输出响应头中的内容类型
            console.log('Content-Type:', response.headers.get('content-type'));
            // 直接使用 response.text() 获取纯文本内容
            const textData = await response.text();
            // 在这里你可以根据需要处理 textData
            data.userType = textData;
            console.log(data.userType);
            //console.log('User Type:', this.userType);
        } catch (error) {
            console.error('获取用户权限时出错：', error);
        }
    }
    getUserType();
</script>
