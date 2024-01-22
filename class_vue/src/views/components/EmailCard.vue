<template>
  <div class="card mb-4">
    <div class="card-header pb-0 d-flex justify-content-between align-items-center">
      <h6>作业管理</h6>
      <button @click="exportHomework" class="btn btn-primary mr-2">导出</button>
      <button @click="openPrintPreview" class="btn btn-info mr-2">打印</button>
      <button @click="sortByDeadline" class="btn btn-success mr-2">按截止时间排序</button>
      <div class="d-flex align-items-center">
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
    <div class="table-responsive p-0">
      <table class="table table-hover">
        <thead>
          <tr>
            <th>作业ID</th>
            <th>作业名</th>
            <th>作业描述</th>
            <th>截止时间</th>
            <th>教师名</th>
            <th>是否提交</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="homework in paginatedHomework" :key="homework.id">
            <td>{{ homework.id }}</td>
            <td>{{ homework.title }}</td>
            <td>{{ homework.description }}</td>
            <td>{{ homework.deadline }}</td>
            <td>{{ homework.teacher }}</td>
            <td>{{ homework.ifSubmit }}</td>
            <td>
              <!-- <button @click="update(homework)" class="btn btn-info">更新作业信息</button> -->
              <button @click="selectFile(homework.id)" class="btn btn-success">选择文件</button>
              <button @click="uploadFile(homework.id)" class="btn btn-primary">上传作业</button>
              
              <button @click="deleteHomework(homework.id)" class="btn btn-danger" v-if="userType !== '学生'">删除作业</button>
              <input type="file" ref="fileInput" style="display:none" @change="handleFileChange" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
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

  <div class="row" v-if="userType !== '学生'">
    <div class="col-md-3">
      <input v-model="title" placeholder="作业名" class="form-control" />
    </div>
    <div class="col-md-2">
      <input v-model="description" placeholder="作业描述" class="form-control" />
    </div>
    <div class="col-md-2">
      <input v-model="deadline" placeholder="截止日期" class="form-control" />
    </div>
    <div class="col-md-1">
      <input v-model="teacher" placeholder="教师名" class="form-control" />
    </div>
        <div class="col-md-2">
      <button @click="add" class="btn btn-default">添加作业</button>
    </div>
    <!-- 添加文件上传输入框和触发 OCR 的按钮 -->
    <div class="col-md-3">
  <label class="custom-file-upload">
    <input type="file" @change="handleImageChange" style="display: none;" />
    <i class="fas fa-cloud-upload-alt"></i> 上传待识别图片（目前仅英文）
  </label>
</div>
     <div class="col-md-3">
      <button @click="copyToClipboard" class="btn btn-success">复制到剪贴板</button>
     </div>
    <div>
  <p>OCR 结果: {{ ocrResult }}</p>
  
</div>
  </div>
</template>

<script>
import Tesseract from 'tesseract.js';
export default {
  data() {
    return {
      searchKeyword: "",
      homeworkList: [],
      itemsPerPage: 5,
      currentPage: 1,
      courseId: '',
      sortOrder: 'asc',
      selectedFile: null,
      ocrResult: '',
      userType: '',
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.filteredHomework.length / this.itemsPerPage);
    },
    paginatedHomework() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredHomework.slice(start, end);
    },
    filteredHomework() {
      const keyword = this.searchKeyword.toLowerCase();
      return this.homeworkList.filter(homework =>
        (homework.title && typeof homework.title === 'string' && homework.title.toLowerCase().includes(keyword)) ||
        (homework.description && typeof homework.description === 'string' && homework.description.toLowerCase().includes(keyword)) ||
        (homework.deadline && typeof homework.deadline === 'string' && homework.deadline.toLowerCase().includes(keyword)) ||
        (homework.teacher && typeof homework.teacher === 'string' && homework.teacher.toLowerCase().includes(keyword))
      );
    },
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


    copyToClipboard() {
    // 获取 OCR 结果
    const ocrResult = this.ocrResult;

    // 创建一个 textarea 元素，将 OCR 结果放入其中
    const textarea = document.createElement('textarea');
    textarea.value = ocrResult;

    // 将 textarea 元素添加到 DOM 中
    document.body.appendChild(textarea);

    // 选择 textarea 中的文本
    textarea.select();

    // 复制文本到剪贴板
    document.execCommand('copy');

    // 从 DOM 中移除 textarea 元素
    document.body.removeChild(textarea);

    // 在控制台打印提示信息
    console.log('文本已复制到剪贴板:', ocrResult);
  },
  async performOCR(file) {
    try {
      const { data: { text } } = await Tesseract.recognize(file, 'eng');
      console.log('OCR 文本:', text); // 输出 OCR 结果
      this.ocrResult = text; // 设置 OCR 结果

      // 使用 $nextTick 来确保在 DOM 更新之后再访问 ocrResult
      this.$nextTick(() => {
        console.log('在 $nextTick 中访问 OCR 结果:', this.ocrResult);
      });
    } catch (error) {
      console.error('OCR 错误:', error);
    }
  },
  handleImageChange(event) {
    const file = event.target.files[0];
    if (file) {
      // 调用 performOCR 方法进行 OCR
      this.performOCR(file);
    }
  },
    deleteHomework(homeworkId) {
    if (!confirm("确定要删除此作业吗？")) {
      return;
    }

    // 发送删除作业的请求到后端
    fetch(`http://localhost:8080/homework/${homeworkId}`, {
      method: 'DELETE',
    })
    .then(response => {
      if (response.ok) {
        // 删除成功，刷新作业列表等操作
        this.fetchHomeworkData();
      } else {
        console.error('Failed to delete homework:', response.statusText);
      }
    })
    .catch(error => {
      console.error('Error deleting homework:', error);
    });
  },
  sortByDeadline() {
    // 使用JavaScript的sort方法对作业列表按截止时间排序
    this.homeworkList.sort((a, b) => {
      const deadlineA = new Date(a.deadline).getTime();
      const deadlineB = new Date(b.deadline).getTime();
      return deadlineA - deadlineB;
    });
  },
    add() {
    // 构建作业对象
    const newHomework = {
      title: this.title,
      description: this.description,
      deadline: this.deadline,
      teacher: this.teacher,
      // 不需要传递ifSubmit，后端会处理默认值
    };

    // 发送作业信息到后端
    fetch('http://localhost:8080/homework/add', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(newHomework),
    })
    .then(response => {
      if (response.ok) {
        // 添加成功，刷新作业列表等操作
        this.fetchHomeworkData();
        // 清空输入框
        this.title = '';
        this.description = '';
        this.deadline = '';
        this.teacher = '';
      } else {
        console.error('Failed to add homework:', response.statusText);
        print(response.statusText)
      }
    })
    .catch(error => {
      console.error('Error adding homework:', error);
    });
  },
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
    exportHomework() {
      // Implement the logic to trigger the file export
      fetch('http://localhost:8080/homework/downloadHomework', {
        method: 'GET',
        responseType: 'blob',
      })
        .then(response => {
          if (response.ok) {
            return response.blob();
          } else {
            console.error('Failed to fetch homework Excel file:', response.statusText);
          }
        })
        .then(blob => {
          const url = window.URL.createObjectURL(new Blob([blob]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', 'homework.xlsx');
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
        })
        .catch(error => console.error('Error exporting homework:', error));
    },
    openPrintPreview() {
      // Open print preview
      window.print();
    },
    update(homework) {
    // 构建更新的作业对象
    const updatedHomework = {
      id: homework.id, // 作业ID
      title: this.updatedTitle,
      description: this.updatedDescription,
      deadline: this.updatedDeadline,
      teacher: this.updatedTeacher,
      ifSubmit: this.updatedIfSubmit,
      // 其他属性...
    };

    // 冻结作业对象，使title属性不可修改
    Object.freeze(updatedHomework);

    // 发送更新后的作业信息到后端
    fetch(`http://localhost:8080/homework/update/${homework.id}`, {
      method: 'PUT', // 使用PUT请求进行更新
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(updatedHomework),
    })
    .then(response => {
      if (response.ok) {
        // 更新成功，刷新作业列表等操作
        this.fetchHomeworkData();
        // 清空输入框
        this.updatedTitle = '';
        this.updatedDescription = '';
        this.updatedDeadline = '';
        this.updatedTeacher = '';
        this.updatedIfSubmit = false; // 重置ifSubmit为默认值
      } else {
        console.error('Failed to update homework:', response.statusText);
      }
    })
    .catch(error => {
      console.error('Error updating homework:', error);
    });
  },
    async fetchHomeworkData() {
      try {
        const response = await fetch("http://localhost:8080/homework/all");
        const data = await response.json();
        this.homeworkList = data;
      } catch (error) {
        console.error("获取作业数据时出错：", error);
      }
    },
    selectFile(homeworkId) {
      // 触发文件选择的点击事件
      this.$refs.fileInput.click();
      // 保存选中的作业ID
      this.selectedFile = { homeworkId };
    },
    handleFileChange(event) {
      // 处理文件选择的变化
      const file = event.target.files[0];
      // 可以在这里处理选中的文件，例如显示文件名等
      console.log('Selected File:', file);
    },
    uploadFile(homeworkId) {
      // 获取选中的文件
      const fileInput = this.$refs.fileInput;
      const file = fileInput.files[0];

      if (!file) {
        alert("请先选择文件");
        return;
      }

      // 构建 FormData 对象
      const formData = new FormData();
      formData.append('file', file);

      // 发送文件上传请求
      fetch(`http://localhost:8080/homework/upload/${homeworkId}`, {
        method: 'POST',
        body: formData,
      })
      .then(response => response.json())
      .then(data => {
        // 上传成功后的处理
        console.log('File uploaded successfully:', data);
        // 刷新作业列表
        this.fetchHomeworkData();
      })
      .catch(error => {
        console.error('Error uploading file:', error);
      });
    },
    // ... 其他方法 ...
  },
  created() {
    this.fetchHomeworkData();
    this.getUserType(); 
  },
};
</script>

<style scoped>
/* 根据需要添加自定义样式 */
</style>
