<template>
  <div class="card mb-4">
    <div class="card-header pb-0 d-flex justify-content-between align-items-center">
      <h6>文件管理</h6>
      
      <button @click="exportFiles" class="btn btn-success mr-2">导出</button>
      <button @click="openPrintPreview" class="btn btn-info mr-2">打印</button>
      <button @click="redirectToSpringBootPage" class="btn btn-default mr-2">图表</button>
      <button @click="sortByCourseId" class="btn btn-success mr-2">按课程号排序</button>
      <div class="d-flex align-items-center">
        <!-- 搜索输入框和清除搜索按钮 -->
        <div class="form-group mb-0" style="height: 40px;">
          <label for="searchKeyword" class="mr-2">搜索：</label>
          <div class="input-group">
            <input type="text" v-model="searchKeyword" class="form-control" id="searchKeyword" style="height: 100%;"/>
            <button @click="resetSearch" class="btn btn-secondary">清除</button>
          </div>
        </div>
      </div>
    </div>
    <br />
    <!-- 表格和分页 -->
    <div class="table-responsive p-0">
      <table class="table table-hover">
        <!-- 文件信息表头 -->
        <thead>
          <tr>
            <th>文件ID</th>
            <th>文件名</th>
            <th>文件大小</th>
            <th>上传时间</th>
            <th>课程ID</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <!-- 循环展示分页和过滤后的文件 -->
         <!-- 循环展示分页和过滤后的文件 -->
<tr v-for="file in paginatedFiles" :key="file.id">
  <!-- 文件信息 -->
  <td>{{ file.id }}</td>
  <td>{{ file.fileName }}</td>
  <td>{{ file.fileSize }}KB</td>
  <td>{{ file.uploadTime }}</td>
  <td>{{ file.courseId }}</td>
  <td>
    <!-- 操作按钮 -->
    <button @click="downloadFile(file.fileName)" class="btn btn-info">下载</button>
    
  </td>
</tr>

        </tbody>
      </table>
    </div>
    <!-- 分页导航 -->
    <nav aria-label="Page navigation">
      <ul class="pagination justify-content-center">
        <li class="page-item" :class="{ disabled: currentPage === 1 }">
          <a class="page-link" @click="prevPage" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li v-for="page in totalPages" :key="page" class="page-item" :class="{ active: currentPage === page }">
          <a class="page-link" @click="goToPage(page)">{{ page }}</a>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === totalPages }">
          <a class="page-link" @click="nextPage" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>
  </div>
  <div class="row">
  <div class="col-md-2" v-if="userType !== '学生'">
    <input v-model="courseId" placeholder="课程ID" class="form-control" />
  </div>
  <div class="col-md-4" v-if="userType !== '学生'">
    <input type="file" @change="handleFileChange" class="form-control is-valid" />
  </div>
  <div class="col-md-2" v-if="userType !== '学生'">
    <button @click="uploadFile" class="btn btn-default" >上传文件</button>
  </div>
</div>

  
</template>

<script>
export default {
  data() {
    return {
      searchKeyword: "",
      fileList: [],
      itemsPerPage: 5,
      currentPage: 1,
      courseId: '',
      sortOrder: 'asc',
      selectedFile: null,
      userType: '',
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.filteredFiles.length / this.itemsPerPage);
    },
    paginatedFiles() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredFiles.slice(start, end);
    },
    filteredFiles() {
      const keyword = this.searchKeyword.toLowerCase();
      return this.fileList.filter(file =>
        (file.fileName && typeof file.fileName === 'string' && file.fileName.toLowerCase().includes(keyword)) ||
        (file.fileSize && typeof file.fileSize === 'string' && file.fileSize.toLowerCase().includes(keyword)) ||
        (file.uploadTime && typeof file.uploadTime === 'string' && file.uploadTime.toLowerCase().includes(keyword)) ||
        (file.courseId && typeof file.courseId === 'string' && file.courseId.toLowerCase().includes(keyword))
      );
    },
  },
  methods: {
    
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
      }
    },
    goToPage(page) {
      this.currentPage = page;
    },
    sortByCourseId() {
  // 切换排序方式（升序或降序）
  this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';

  // 使用Array.sort()方法对fileList进行排序
  this.fileList.sort((a, b) => {
    const valueA = String(a.courseId); // Convert to string
    const valueB = String(b.courseId); // Convert to string

    // 根据排序方式进行比较
    if (this.sortOrder === 'asc') {
      return valueA.localeCompare(valueB, undefined, { numeric: true, sensitivity: 'base' });
    } else {
      return valueB.localeCompare(valueA, undefined, { numeric: true, sensitivity: 'base' });
    }
  });
},

    handleFileChange(event) {
      this.selectedFile = event.target.files[0];
    },
    async uploadFile() {
  try {
    const formData = new FormData();
    formData.append('courseId', this.courseId);
    formData.append('file', this.selectedFile);

    const response = await fetch('http://localhost:8080/file/upload', {
      method: 'POST',
      body: formData,
    });

    if (response.ok) {
      const responseData = await response.json();
      // 处理上传成功的响应
      console.log(responseData);

      // 上传成功后刷新页面
      window.location.reload();
    } else {
      // 处理上传失败的情况
      const errorData = await response.json();
      console.error('文件上传失败：', errorData);
    }
  } catch (error) {
    // 处理请求失败的情况
    console.error('文件上传失败：', error);
  }
},

    async fetchData() {
      try {
        const response = await fetch("http://localhost:8080/file/all");
        const data = await response.json();
        this.fileList = data;
      } catch (error) {
        console.error("获取文件数据时出错：", error);
      }
    },
    exportFiles() {
      // Implement the logic to trigger the file export
      fetch('http://localhost:8080/file/downloadFiles', {
        method: 'GET',
        responseType: 'blob',
      })
        .then(response => {
          if (response.ok) {
            return response.blob();
          } else {
            console.error('Failed to fetch file Excel file:', response.statusText);
          }
        })
        .then(blob => {
          const url = window.URL.createObjectURL(new Blob([blob]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', 'files.xlsx');
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
        })
        .catch(error => console.error('Error exporting files:', error));
    },
    openPrintPreview() {
      // Open print preview
      window.print();
    },
    redirectToSpringBootPage() {
      // 使用 window.location.href 跳转到指定页面
      window.location.href = 'http://localhost:8080/001/index.html';
    },
    resetSearch() {
      this.searchKeyword = ''; // 清除搜索关键字
    },
    downloadFile(fileName) {
      fetch(`/file/download?fileName=${fileName}`)
        .then(response => {
          if (response.ok) {
            return response.blob();
          } else {
            console.error("无法获取文件：", response.statusText);
          }
        })
        .then(blob => {
          const url = window.URL.createObjectURL(new Blob([blob]));
          const link = document.createElement("a");
          link.href = url;
          link.setAttribute("download", fileName);
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
        })
        .catch(error => {
          console.error("下载文件时出错：", error);
        });
    },
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


    async deleteFile(fileId) {
      try {
        const response = await fetch(`http://localhost:8080/file/delete?id=${fileId}`, {
          method: "DELETE",
        });
        // 根据需要处理文件删除
      } catch (error) {
        console.error("删除文件时出错：", error);
      }
    },
  },
  created() {
    this.fetchData();
    this.getUserType();
  },
};
</script>

<style scoped>
/* 根据需要添加自定义样式 */
</style>
