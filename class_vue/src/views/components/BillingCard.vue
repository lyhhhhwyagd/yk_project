<template>
  <div class="card mb-4">
    <div class="card-header pb-0 d-flex justify-content-between align-items-center">
      <h6>用户管理</h6>
      <button @click="exportUsers" class="btn btn-primary mr-2">导出</button>
        <button @click="openPrintPreview" class="btn btn-info mr-2">打印</button>
      <div class="d-flex align-items-center">
        
        
        <!-- Search input field with Clear Search button -->
        <div class="form-group mb-0" style="height: 40px;"> <!-- Adjust the height as needed -->
          <label for="searchKeyword" class="mr-2">搜索：</label>
          <div class="input-group">
            <input type="text" v-model="searchKeyword" class="form-control" id="searchKeyword" style="height: 100%;"> <!-- Adjust the height as needed -->
            <!-- Clear Search button -->
            <button @click="resetSearch" class="btn btn-secondary">清除</button>
          </div>
        </div>
      </div>
    </div>
  <br>
    <!-- Update User Modal -->
    <div class="modal fade" id="updateUserModal" tabindex="-1" role="dialog" aria-labelledby="updateUserModalLabel" aria-hidden="true">
      <!-- ... modal content ... -->
    </div>

    <!-- Table and pagination -->
    <div class="table-responsive p-0">
      <table class="table table-hover">
        <!-- ... table header ... -->
        <thead>
  <tr>
    <th>用户ID</th>
    <th>用户名</th>
    <th>用户角色</th>
    <th>注册日期</th>
    <th>用户邮箱</th>
    <th>用户电话</th>
    <th>操作</th>
  </tr>
</thead>
        <tbody>
          <!-- Loop through paginated and filtered users -->
          <tr v-for="user in paginatedUsers" :key="user.userId">
            <td>{{ user.userId }}</td>
            <td>{{ user.userName }}</td>
            <td>{{ user.userType }}</td>
            <td>{{ user.createTime }}</td>
            <td>{{ user.userEmail }}</td>
            <td>{{ user.userPhone }}</td>
            <td>
              <button @click="deleteUser(user.userId)" class="btn btn-danger">Delete</button>
              <button @click="openUpdateUserModal(user)" class="btn btn-warning">Update</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination navigation -->
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
</template>


<script>
import $ from 'jquery';
import "jquery";
export default {
  name: "UserTable",
  data() {
    return {
      users: [],
      selectedUser: {},
      itemsPerPage: 5,
      currentPage: 1,
      searchKeyword: '', // Added searchKeyword
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
    fetchUserData() {
      fetch('http://localhost:8080/user/all')
        .then(response => response.json())
        .then(data => {
          this.users = data;
        })
        .catch(error => console.error('Error fetching user data:', error));
    },
    openPrintPreview() {
      // Open print preview
      window.print();
    },
    addUser() {
      console.log('Adding a new user...');
    },
    exportUsers() {
      // Implement the logic to trigger the user export
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
    openUpdateUserModal(user) {
      this.selectedUser = { ...user };
      $('#updateUserModal').modal('show');
    },
    updateUser() {
      fetch('http://localhost:8080/user/update', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.selectedUser),
      })
        .then(response => {
          if (response.ok) {
            console.log('User updated successfully');
            $('#updateUserModal').modal('hide');
            this.fetchUserData();
          } else {
            console.error('Failed to update user:', response.statusText);
          }
        })
        .catch(error => console.error('Error updating user:', error));
    },
    deleteUser(userId) {
      // Implement the logic to delete a user
      // You can use userId to identify the user to be deleted
      console.log("userId:" ,userId)
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
            // Refresh the page after deletion
            this.fetchUserData(); // Refresh the user data
          } else {
            console.error('Failed to delete user with ID:', userId);
          }
        })
        .catch(error => console.error('Error deleting user:', error));
    },
    resetSearch() {
      this.searchKeyword = ''; // Clear the search keyword
    },
  },
};
</script>

<style scoped>
/* Add your custom styles here */
</style>
