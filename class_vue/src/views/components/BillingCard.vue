<template>
   <div v-if="userRole === '学生'" class="alert alert-warning" role="alert">
      本界面不对学生开放
    </div>
  <div class="card mb-4" v-if="userRole !== '学生'">
    <div class="card-header pb-0 d-flex justify-content-between align-items-center">
      <h6>用户管理</h6>
      <button @click="exportUsers" class="btn btn-primary mr-2" >导出</button>
      <button @click="openPrintPreview" class="btn btn-info mr-2">打印</button>
      <div class="d-flex align-items-center">
        <div class="form-group mb-0" style="height: 40px;">
          <label for="searchKeyword" class="mr-2">搜索：</label>
          <div class="input-group">
            <input type="text" v-model="searchKeyword" class="form-control" id="searchKeyword" style="height: 100%;">
            <button @click="resetSearch" class="btn btn-secondary">清除</button>
          </div>
        </div>
      </div>
    </div>
    <br>

    <div class="table-responsive p-0">
      <table class="table table-hover">
        <thead>
          <tr>
            <th>用户ID</th>
            <th>用户名</th>
            <th>用户角色</th>
            <th>注册日期</th>
            <th>用户密码</th>
            <th>用户邮箱</th>
            <th>用户电话</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in paginatedUsers" :key="user.userId">
            <td>{{ user.userId }}</td>
            <td>{{ user.userName }}</td>
            <td>{{ user.userType }}</td>
            <td>{{ user.createTime }}</td>
            <td >******</td>
            <td>{{ user.userEmail }}</td>
            <td>{{ user.userPhone }}</td>
            <td>
              <button @click="updateUser(user)" class="btn btn-warning">更新</button>
              <button @click="deleteUser(user.userId)" class="btn btn-danger">删除</button>
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
    <div class="row">
  <div class="col-md-2">
    <input v-model="userName" placeholder="用户名" class="form-control" />
  </div>
  <div class="col-md-1">
    <input v-model="userType" placeholder="用户角色" class="form-control" />
  </div>
  <div class="col-md-1">
    <input v-model="createTime" placeholder="注册日期" class="form-control" />
  </div>
  <div class="col-md-2">
    <input v-model="password" placeholder="密码" class="form-control" />
  </div>
  <div class="col-md-2">
    <input v-model="userEmail" placeholder="用户邮箱" class="form-control" />
  </div>
  <div class="col-md-2">
    <input v-model="userPhone" placeholder="用户电话" class="form-control" />
  </div>
  <div class="col-md-2">
    <button @click="addUser" class="btn btn-success">添加用户</button>
  </div>
</div>

    <!-- Update User Modal -->
    <div class="modal fade" id="updateUserModal" tabindex="-1" role="dialog" aria-labelledby="updateUserModalLabel" aria-hidden="true">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="updateUserModalLabel">更新用户信息</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="submitUpdate">
              <!-- User ID (disabled and not editable) -->
              <div class="form-group">
                <label for="updateUserId">用户ID</label>
                <input type="text" class="form-control" id="updateUserId" v-model="selectedUser.userId" disabled>
              </div>

              <!-- Other editable fields -->
              <div class="form-group">
                <label for="updateUserName">用户名</label>
                <input type="text" class="form-control" id="updateUserName" v-model="selectedUser.userName" required>
              </div>

              <div class="form-group">
                <label for="updateUserType">用户角色</label>
                <input type="text" class="form-control" id="updateUserType" v-model="selectedUser.userType" required>
              </div>

              <div class="form-group">
                <label for="updateCreateTime">注册日期</label>
                <input type="text" class="form-control" id="updateCreateTime" v-model="selectedUser.createTime" disabled>
              </div>
              
              <div class="form-group">
                <label for="updateUserEmail">用户密码</label>
                <input type="password" class="form-control" id="updateUserEmail" v-model="selectedUser.password" required>
              </div>

              <div class="form-group">
                <label for="updateUserEmail">用户邮箱</label>
                <input type="text" class="form-control" id="updateUserEmail" v-model="selectedUser.userEmail" required>
              </div>

              <div class="form-group">
                <label for="updateUserPhone">用户电话</label>
                <input type="text" class="form-control" id="updateUserPhone" v-model="selectedUser.userPhone" required>
              </div>

              <!-- Add other fields as needed -->

              <button type="submit" class="btn btn-primary">保存更新</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import $ from 'jquery';
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

export default {
  name: "UserTable",
  data() {
    return {
      users: [],
      selectedUser: {},
      itemsPerPage: 5,
      currentPage: 1,
      searchKeyword: '',
      userName: '',
      userType: '',
      createTime: '',
      password:'',
      userEmail: '',
      userPhone: '',
      userRole:'',//用于权限管理
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.filteredUsers.length / this.itemsPerPage);
    },
    paginatedUsers() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredUsers.slice(start, end);
    },
    filteredUsers() {
      const keyword = this.searchKeyword.toLowerCase();
      return this.users.filter(user =>
        (user.userId && typeof user.userId === 'string' && user.userId.toLowerCase().includes(keyword)) ||
        (user.userName && typeof user.userName === 'string' && user.userName.toLowerCase().includes(keyword)) ||
        (user.userType && typeof user.userType === 'string' && user.userType.toLowerCase().includes(keyword)) ||
        (user.createTime && typeof user.createTime === 'string' && user.createTime.toLowerCase().includes(keyword)) ||
        (user.userEmail && typeof user.userEmail === 'string' && user.userEmail.toLowerCase().includes(keyword)) ||
        (user.userPhone && typeof user.userPhone === 'string' && user.userPhone.toLowerCase().includes(keyword))
      );
    },
  },
  mounted() {
    this.fetchUserData();
  },
  created() {
  // 设置默认值，确保界面不会在获取用户角色之前渲染
  this.userRole = '';

  // 获取用户角色信息
  this.getUserRole();
},
  methods: {
    async getUserRole() {
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
    this.userRole = textData;

    console.log('User Type:', this.userRole);
  } catch (error) {
    console.error('获取用户权限时出错：', error);
  }
},
    addUser() {
    const newUser = {
      userName: this.userName,
      userType: this.userType,
      createTime: this.createTime,
      password:this.password,
      userEmail: this.userEmail,
      userPhone: this.userPhone,
    };

    // 发送 POST 请求到服务器
    fetch('http://localhost:8080/user/add', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(newUser),
    })
    .then(response => {
      if (response.ok) {
        console.log('User added successfully');
        // 可以在这里执行其他操作，例如刷新用户列表
        this.fetchUserData(); // 假设有一个名为 fetchUserData 的方法来获取用户数据
      } else {
        console.error('Failed to add user');
      }
    })
    .catch(error => console.error('Error adding user:', error));

    // 清空输入框
    this.userName = '';
    this.userType = '';
    this.createTime = '';
    this.userEmail = '';
    this.userPhone = '';
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
    fetchUserData() {
      fetch('http://localhost:8080/user/all')
        .then(response => response.json())
        .then(data => {
          this.users = data;
        })
        .catch(error => console.error('Error fetching user data:', error));
    },
    openPrintPreview() {
      window.print();
    },
    
    exportUsers() {
      fetch('http://localhost:8080/user/downloadUser', {
        method: 'GET',
        responseType: 'blob',
      })
        .then(response => {
          if (response.ok) {
            return response.blob();
          } else {
            console.error('Failed to fetch user Excel file:', response.statusText);
          }
        })
        .then(blob => {
          const url = window.URL.createObjectURL(new Blob([blob]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', 'users.xlsx');
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
        })
        .catch(error => console.error('Error exporting users:', error));
    },
    deleteUser(userId) {
      console.log("userId:", userId)
      fetch('http://localhost:8080/user/delete', {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ userId: userId }),
      })
        .then(response => {
          if (response.ok) {
            console.log('Successfully deleted user with ID:', userId);
            this.fetchUserData();
          } else {
            console.error('Failed to delete user with ID:', userId);
          }
        })
        .catch(error => console.error('Error deleting user:', error));
    },

    resetSearch() {
      this.searchKeyword = '';
    },
    updateUser(user) {
      this.selectedUser = { ...user };
      $('#updateUserModal').modal('show');
    },
    submitUpdate() {
      const { userId, ...updatedUser } = this.selectedUser;
      fetch('http://localhost:8080/user/update', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedUser),
      })
        .then(response => {
          if (response.ok) {
            console.log('Successfully updated user with ID:', userId);
            this.fetchUserData();
          } else {
            console.error('Failed to update user with ID:', userId);
          }
        })
        .catch(error => console.error('Error updating user:', error));
      $('#updateUserModal').modal('hide');
    },
  },
  
};
</script>

<style scoped>
/* Add your custom styles here */
</style>
