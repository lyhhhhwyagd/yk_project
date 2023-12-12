<template>
    <div  >
        <div class="card" style="margin-bottom: 10px ;" >
            <div  >
            <el-input style="width: 260px" v-model="data.courseName"  placeholder="请输入课程名称" :prefix-icon="Search"/>

            <el-input style="width: 260px" v-model="data.dep"  placeholder="请输入学院名称" :prefix-icon="Search"/>


            <el-button type="primary" style="margin-left :10px;background-color: #76dc30" @click="load">查询</el-button>

            <el-button type="info" @click="reset">重置</el-button>
            </div>
        </div>

        <div class="card" style="margin-bottom: 10px">
            <div style="margin-bottom: 10px">
                <el-button  style="background-color: #76dc30" type="primary" @click="handleAdd">新增</el-button>
                <el-button  style="background-color: #76dc30" type="info" @click="exportAll">导出</el-button>
                <el-button  style="background-color: #76dc30" type="info" @click="printAll">打印</el-button>
            </div>
            <div>
            <el-table  :data="data.tableData" :default-sort="{ prop: 'id', order: 'descending' }">
                <el-table-column label="编号" sortable prop="id"></el-table-column>
                <el-table-column label="课程名称" prop="courseName" width="180"></el-table-column>
                <el-table-column label="开课老师" prop="teacherId" width="180"></el-table-column>
                <el-table-column label="学院" prop="dep"></el-table-column>
                <el-table-column label="班级" prop="classId"></el-table-column>
                <el-table-column label="课程号" prop="courseId"></el-table-column>
                <el-table-column label="学分" prop="bonus"></el-table-column>
                <el-table-column >
                    <template #default="scope">
                        <el-button style="background-color: #76dc30;margin-right: 0" type="primary"  @click="handleEdit(scope.row)">编辑</el-button>
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

        <el-dialog title="课程信息" width="40%" v-model="data.formVisible" >
            <el-form :model="data.form" label-width="100px" style="padding-right: 50px">
                <el-form-item label="课程名称" prop="courseName">
                    <el-input v-model="data.form.courseName" autocomplete="off" />
                </el-form-item>
                <el-form-item label="开课老师" prop="teacherId">
                    <el-input v-model="data.form.teacherId" autocomplete="off" />
                </el-form-item>
                <el-form-item label="学院" prop="dep">
                    <el-input v-model="data.form.dep" autocomplete="off" />
                </el-form-item>
                <el-form-item label="班级" prop="classId">
                    <el-input v-model="data.form.classId" autocomplete="off" />
                </el-form-item>
                <el-form-item label="课程号" prop="courseId">
                    <el-input v-model="data.form.courseId" autocomplete="off" />
                </el-form-item>
                <el-form-item label="学分" prop="bonus">
                    <el-input v-model="data.form.bonus" autocomplete="off" />
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
   import {onMounted} from "@vue/runtime-core";
    // request.get('/').then(res => {
    //     console.log(res)
    // })

    const data = reactive({
        courseName:"",
        dep:"",
        tableData: [{
            "course_id":"","id":"","dep":"1","course":"1","bonus":"3","teacher_id":"1","class_id":"1"}],
        total:0,
        pageSize:4,//当前最大个数
        pageNum:1,
        formVisible:false,
        form:{}
    })
   const load=() =>{
        request.get("/course/selectpage", {
            params:
                {
                    pageNum: data.pageNum,
                    pageSize: data.pageSize,
                    courseName:data.courseName,
                    dep:data.dep
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
       data.courseName="";
       data.dep="",
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
           url: data.form.id ? '/course/update' :'/course/add',
           method: data.form.id? 'PUT' :'POST',
           data: data.form
           }).then(res=>{
           if(res.code=="200")
           {
               ElMessage.success("操作成功");
               data.formVisible=false;
               load();
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
            request.delete("/course/delete/"+id).then(res=>
            {
                if(res.code=="200")
                {
                    ElMessage.success("操作成功");
                    load();
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
        window.open("http://localhost:8090/course/export")
    }
   //调用方法获取后台
    load()

   onMounted(async () => {
       setTimeout(() => {aa()}, 1000)
   })


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
       request.get("/course/map").then(res =>{
           if(res.data) {
               //option.xAxis.data = res.data.x;
               option.series[0].data = res.data;
               option.series[1].data = res.data;
               myChart.setOption(option);
           }
       })

   }
   const printAll = (index, row) => {
       // 正常跳转
       // router.push({ name: 'record', params: { id: row.id } })
       // 为了打开新窗口用下面方式
       // const url = router.resolve({
       //     name: 'record',
       //     params: { id: row.id }
       // })
       window.open("Dashborad")
   }
</script>
