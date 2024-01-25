<template>
    <div class="py-4 container-fluid">
      <div class="mt-4 row">
        <div class="col-12">
          <div class="card">
            <!-- Card header -->
            <div class="card-header">
              <h5 class="mb-0">活动管理</h5>
              <el-button 
                size="small"
                type="success"
                @click="print()"
                >打印表格</el-button
                >
              <el-button
                size="small"
                type="success"
                @click="exportExcel()"
                >导出表格</el-button
                >
              <el-button
                size="small"
                type="success"
                @click="dialogVisible = true"
                v-if="userType == '管理员'">增加数据</el-button
                >
              <el-button
                size="small"
                type="success"
                @click="dialogVisible2 = true"
                >统计信息</el-button
                >
            </div>
            <el-table :data="filterdata" id="out-table" style="width: 100%">
                <el-table-column label="活动名称" prop="activityName" sortable />
                <el-table-column label="活动日期" prop="activityDate" sortable />
                <el-table-column label="活动时长/小时" prop="activityDuration" sortable />
                <el-table-column label="活动内容" prop="activityContent"  />
                <el-table-column label="备注" prop="remarks" />
                <el-table-column align="right">
                  <template #header>
                    <el-input v-model="search" size="small" placeholder="请输入检索条件" />
                  </template>
                  <template #default="scope">
                    <el-button 
                     size="small"
                     type="success"
                     @click="dialogVisible1 = true,handleEdit(scope.$index, scope.row)"
                     v-if="userType == '管理员'">编辑</el-button
                    >
                    <el-button
                      size="small"
                      @click="handleDelete(scope.$index, scope.row)"
                      v-if="userType == '管理员'">删除</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
           </div>
        </div>
      </div>
    </div>
    <el-dialog
        v-model="dialogVisible"
        title="添加信息"
        width="30%"
        :before-close="handleClose"
      >
        <!-- <span>This is a message</span> -->
        <el-form :model="addForm" label-width="120px">
          <el-form-item label="活动名称"> 
            <el-input v-model="addForm.activityName"  />
          </el-form-item>
          <el-form-item label="活动日期">
            <el-input v-model="addForm.activityDate"  />
          </el-form-item>
          <el-form-item label="活动时长/小时"> 
            <el-input v-model="addForm.activityDuration"  />
          </el-form-item>
          <el-form-item label="活动内容"> 
            <el-input v-model="addForm.activityContent"  />
          </el-form-item>
          <el-form-item label="备注"> 
            <el-input v-model="addForm.remarks"  />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false" >取消</el-button>
            <el-button type="success" @click="dialogVisible = false, handleAdd()">
              确认
            </el-button>
          </span>
        </template>
      </el-dialog>
      <el-dialog
        v-model="dialogVisible1"
        title="编辑信息"
        width="30%"
        :before-close="handleClose"
      >
        <!-- <span>This is a message</span> -->
        <el-form :model="editForm" label-width="120px">
            <el-form-item label="活动名称"> 
                <el-input v-model="editForm.activityName"  />
              </el-form-item>
              <el-form-item label="活动日期">
                <el-input v-model="editForm.activityDate"  />
              </el-form-item>
              <el-form-item label="活动时长/小时"> 
                <el-input v-model="editForm.activityDuration"  />
              </el-form-item>
              <el-form-item label="活动内容"> 
                <el-input v-model="editForm.activityContent"  />
              </el-form-item>
              <el-form-item label="备注"> 
                <el-input v-model="editForm.remarks"  />
              </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible1 = false">取消</el-button>
            <el-button type="success" @click="dialogVisible1 = false, submmitChange()">
              确认
            </el-button>
          </span>
        </template>
      </el-dialog>
      <el-dialog v-model="dialogVisible2" title="历年活动数及活动时长统计" width="50%" draggable>
        <div class="chart">
            <mixed-chart
            :chart="chartContent"
          />
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible2 = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>
  </template>
  
  <script>
  import AppFooter from "@/examples/Footer.vue";
  import Navbar from "@/examples/Navbars/Navbar.vue";
  import setTooltip from "@/assets/js/tooltip.js";
  import MixedChart from "./components/MixedChart.vue";

  const body = document.getElementsByTagName("body")[0];
  import { mapMutations, mapState } from "vuex";

  import axios from "axios"
  import { ElMessage } from 'element-plus'
  import { useRouter } from "vue-router";
  import FileSaver from "file-saver";
  import XLSX from "xlsx";
  import printJS from 'print-js';

  export default {
    name: "Activity",
    components: {
      AppFooter,
      Navbar,
      MixedChart,
    },
    data() {
      return {
        userType:'',
        dialogVisible: false,
        dialogVisible1: false,
        dialogVisible2: false,
        length: 0,
        tableData: [],
        addForm:{
                activityId:"",
                activityName:"",
                activityDate:"",
                activityDuration:"",
                activityContent:"",
                remarks:""},
        editForm:{
                activityID:"",
                activityName:"",
                activityDate:"",
                activityDuration:"",
                activityContent:"",
                remarks:""},
        search: '',
        chartContent:{
              labels: [
                '2020',
                '2021',
                '2022',
                '2023',
                '2024',
                
              ],
              datasets: [],
            }
      }
    },
    created(){
        this.getUserType();
    },
    computed: {
      ...mapState(["isTransparent", "isNavFixed", "navbarFixed", "mcolor"]),
      filterdata() {
        console.log(111+this.tableData)
        return this.tableData.filter(
            (data) => {
            // Convert each property to lowercase for case-insensitive comparison
            const lowerSearch = this.search.toLowerCase();
            const lowerActivityName = data.activityName != null ? data.activityName.toLowerCase() : '';
            const lowerActivityDate = data.activityDate != null ? data.activityDate.toLowerCase() : '';
            const lowerActivityDuration = data.activityDuration != null ? data.activityDuration.toLowerCase() : '';
            const lowerActivityContent = data.activityContent != null ? data.activityContent.toLowerCase() : '';
            const lowerRemarks = data.remarks !=null ? data.remarks.toLowerCase() : '';
            

            // Check if any property contains the search value
            return (
                !lowerSearch ||
                lowerActivityName.includes(lowerSearch) ||
                lowerActivityDate.includes(lowerSearch) ||
                lowerActivityDuration.includes(lowerSearch) ||
                lowerActivityContent.includes(lowerSearch) ||
                lowerRemarks.includes(lowerSearch)
            );
            }
        );
        },
    },
    mounted() {
      setTooltip(this.$store.state.bootstrap);
      axios.get('http://localhost:8080/activity/find_all').then(res=>{
            console.log(res);
            this.length += res.data.length;
            for (let i = 0; i <res.data.length; i++) {
                console.log(res.data[i].source);
                this.tableData.push(res.data[i]);
            }
        }).catch(err=>{
            console.log("获取数据失败" + err);
        })
        // getStatistic(){
      axios.get('http://localhost:8080/activity/statistic').then(res=>{
            console.log(res);
            // this.length += res.data.length;
            let chartdata1 ={label:'活动总数',data:[]}
            let chartdata2 ={label:'活动总时长',data:[]}
            for (let i = 0; i <res.data.length; i++) {
                console.log(res.data[i]);
                chartdata1.data.push(res.data[i].times)
                chartdata2.data.push(res.data[i].duration);
            }
            this.chartContent.datasets.push(chartdata2);
            this.chartContent.datasets.push(chartdata1);
            console.log(this.chartContent);
        }).catch(err=>{
            console.log("获取数据失败" + err);
        })
    //   },
    },
    beforeMount() {
      this.$store.state.showNavbar = true;
      this.$store.state.showSidenav = true;
      this.$store.state.showFooter = true;
      
      this.$store.state.isTransparent = "bg-transparent";
      
    },
    beforeUnmount() {
      this.$store.state.showNavbar = true;
      this.$store.state.showSidenav = true;
      this.$store.state.showFooter = true;
      
  
      if (this.$store.state.isPinned === false) {
        const sidenav_show = document.querySelector(".g-sidenav-show");
        sidenav_show.classList.remove("g-sidenav-hidden");
        sidenav_show.classList.add("g-sidenav-pinned");
        this.$store.state.isPinned = true;
      }
      this.$store.state.isTransparent = "bg-transparent";
    },
    
    methods: {
        async getUserType() {
            try {
                const userId = this.$route.query.userID;
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
                this.userType = textData;
                console.log('User Type:', this.userType);
            } catch (error) {
                console.error('获取用户权限时出错：', error);
            }
        },
      handleEdit(index, row) {
        console.log(index, row);
        this.editForm = row;
        this.editForm.activityID = row.activityId;
      },
      submmitChange(){
        axios.post('http://localhost:8080/activity/update', this.editForm).then(res=>{
            console.log(res);
        }).catch(err=>{
            console.log("修改数据失败" + err);
        })
        this.$router.go(0);
        // this.students[this.index] =  this.editForm;
        // this.prizes[this.index].fonts = [{ text: this.editForm.name }];
      },
      handleDelete(index, row) {
        console.log(index, row);
        axios.delete('http://localhost:8080/activity/delete/'+row.activityId).then(res=>{
            console.log(res);
        }).catch(err=>{
            console.log("删除数据失败" + err);
        })
        this.$router.go(0);
      },
      handleAdd(){
        axios.post('http://localhost:8080/activity/add', this.addForm).then(res=>{
            console.log(res);
        }).catch(err=>{
            console.log("增加数据失败" + err);
        })
        this.$router.go(0);
      },
      print() {
        const style = '@page {  } ' + '@media print {td{border:1px    solid #000;text-align:center;height:40px}th{border:1px solid #000} }';//这里修改的是el-table边框问题
        printJS({
            printable: 'out-table',	//打印区域id
            type: 'html',		//打印类型是html
            scanStyles: false,
            style: style,
            targetStyles: ['*'],
        })
      },
      exportExcel() {
        var xlsxParam = { raw: true };
        /* 从表生成工作簿对象 */
        var wb = XLSX.utils.table_to_book(
          document.querySelector("#out-table"),
          xlsxParam
        );
        /* 获取二进制字符串作为输出 */
        var wbout = XLSX.write(wb, {
          bookType: "xlsx",
          bookSST: true,
          type: "array",
        });
        try {
          FileSaver.saveAs(
            //Blob 对象表示一个不可变、原始数据的类文件对象。
            //Blob 表示的不一定是JavaScript原生格式的数据。
            //File 接口基于Blob，继承了 blob 的功能并将其扩展使其支持用户系统上的文件。
            //返回一个新创建的 Blob 对象，其内容由参数中给定的数组串联组成。
            new Blob([wbout], { type: "application/octet-stream" }),
            //设置导出文件名称
            "活动信息.xlsx"
          );
        } catch (e) {
          if (typeof console !== "undefined") console.log(e, wbout);
        }
        return wbout;
    },
    },
    
  };

  </script>
  