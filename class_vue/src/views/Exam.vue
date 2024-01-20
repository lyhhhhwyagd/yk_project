<template>
    <div class="py-4 container-fluid">
      <div class="mt-4 row">
        <div class="col-12">
          <div class="card">
            <!-- Card header -->
            <div class="card-header">
              <h5 class="mb-0">考试管理</h5>
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
                @click="111"
                >增加数据</el-button
                >
              <el-button
                size="small"
                type="success"
                @click="111"
                >统计信息</el-button
                >
            </div>
            <el-table :data="filterdata" id="out-table" style="width: 100%">
                <el-table-column label="考试名称" prop="source" sortable />
                <el-table-column label="考试日期" prop="examDate" sortable />
                <el-table-column label="时长" prop="totalTime" sortable />
                <el-table-column label="年级" prop="grade" sortable />
                <el-table-column label="学期" prop="term" sortable />
                <el-table-column label="专业" prop="major" sortable />
                <el-table-column label="学院" prop="institute" sortable />
                <el-table-column label="总分" prop="totalScore" sortable />
                <el-table-column label="类型" prop="type" sortable />
                <el-table-column label="备注" prop="tips" sortable />
                <el-table-column align="right">
                  <template #header>
                    <el-input v-model="search" size="small" placeholder="请输入检索条件" />
                  </template>
                  <template #default="scope">
                    <el-button 
                     size="small"
                     type="success"
                     @click="handleEdit(scope.$index, scope.row)"
                      >编辑</el-button
                    >
                    <el-button
                      size="small"
                      @click="handleDelete(scope.$index, scope.row)"
                      >删除</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
           </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import AppFooter from "@/examples/Footer.vue";
  import Navbar from "@/examples/Navbars/Navbar.vue";
  import setTooltip from "@/assets/js/tooltip.js";

  const body = document.getElementsByTagName("body")[0];
  import { mapMutations, mapState } from "vuex";

  import axios from "axios"
  import { ElMessage } from 'element-plus'
  import { useRouter } from "vue-router";
  import FileSaver from "file-saver";
  import XLSX from "xlsx";
  import printJS from 'print-js';

  export default {
    name: "Exam",
    components: {
      AppFooter,
      Navbar,
    },
    data() {
      return {
        length: 0,
        tableData: [],
        search: ''
      }
    },
    computed: {
      ...mapState(["isTransparent", "isNavFixed", "navbarFixed", "mcolor"]),
      filterdata() {
        console.log(111+this.tableData)
        return this.tableData.filter(
            (data) => {
            // Convert each property to lowercase for case-insensitive comparison
            const lowerSearch = this.search.toLowerCase();
            const lowerSource = data.source != null ? data.source.toLowerCase() : '';
            const lowerExamDate = data.examDate != null ? data.examDate.toLowerCase() : '';
            const lowerTotalTime = data.totalTime != null ? data.totalTime.toLowerCase() : '';
            const lowerGrade = data.grade != null ? data.grade.toLowerCase() : '';
            const lowerTerm = data.term !=null ? data.term.toLowerCase() : '';
            const lowerMajor = data.major != null ? data.major.toLowerCase() : '';
            const lowerInstitute = data.institute != null ? data.institute.toLowerCase() : '';
            const lowerTotalScore = data.totalScore != null ? data.totalScore.toLowerCase() : '';
            const lowerType = data.type != null ? data.type.toLowerCase() : '';
            const lowerTips = data.tips != null ? data.tips.toLowerCase() : '';

            // Check if any property contains the search value
            return (
                !lowerSearch ||
                lowerSource.includes(lowerSearch) ||
                lowerExamDate.includes(lowerSearch) ||
                lowerTotalTime.includes(lowerSearch) ||
                lowerGrade.includes(lowerSearch) ||
                lowerTerm.includes(lowerSearch) ||
                lowerMajor.includes(lowerSearch) ||
                lowerInstitute.includes(lowerSearch) ||
                lowerTotalScore.includes(lowerSearch) ||
                lowerType.includes(lowerSearch) ||
                lowerTips.includes(lowerSearch)
            );
            }
        );
        },
    },
    mounted() {
      setTooltip(this.$store.state.bootstrap);
      axios.get('http://localhost:8080/exam/find_all').then(res=>{
            console.log(res);
            this.length += res.data.length;
            for (let i = 0; i <res.data.length; i++) {
                console.log(res.data[i].source);
                this.tableData.push(res.data[i]);
            }
        }).catch(err=>{
            console.log("获取数据失败" + err);
        })
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
      handleEdit(index, row) {
        console.log(index, row);
      },
      handleDelete(index, row) {
        console.log(index, row);
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
            "考试信息.xlsx"
          );
        } catch (e) {
          if (typeof console !== "undefined") console.log(e, wbout);
        }
        return wbout;
    },
    },
    
  };

  </script>
  