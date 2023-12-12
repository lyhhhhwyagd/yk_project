<template>
  <div class="card mb-4">
    <div class="card-header pb-0">
      <h6>User Management</h6>
      <!-- <button @click="addUser" class="btn btn-success">Add User</button> -->
      <button @click="exportUsers" class="btn btn-primary">Export Users</button>
      <button @click="openPrintPreview" class="btn btn-info">Print Preview</button>
    </div>
    
    <div class="card-body px-0 pt-0 pb-2">
      <div class="table-responsive p-0">
        <table class="table table-hover">
          <thead>
            <tr>
              <th>User ID</th>
              <th>User Name</th>
              <th>User Type</th>
              <th>Create Time</th>
              <th>User Email</th>
              <th>User Phone</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in users" :key="user.userId">
              <td>{{ user.userId }}</td>
              <td>{{ user.userName }}</td>
              <td>{{ user.userType }}</td>
              <td>{{ user.createTime }}</td>
              <td>{{ user.userEmail }}</td>
              <td>{{ user.userPhone }}</td>
              <td>
                <button @click="openUpdateModal(user)" class="btn btn-warning">Update</button>
                <button @click="deleteUser(user.userId)" class="btn btn-danger">Delete</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <!-- Update User Modal -->
<div v-if="isUpdateModalVisible" class="modal">
  <div class="modal-content">
    <span class="close" @click="closeUpdateModal">&times;</span>

    <!-- Update User Form -->
    <form @submit.prevent="updateUser">
      <label for="userId">User ID:</label>
      <input type="text" v-model="selectedUser.userId" readonly>

      <label for="userName">User Name:</label>
      <input type="text" v-model="selectedUser.userName">

      <label for="userType">User Type:</label>
      <input type="text" v-model="selectedUser.userType">

      <label for="createTime">Create Time:</label>
      <input type="text" v-model="selectedUser.createTime" readonly>

      <label for="userEmail">User Email:</label>
      <input type="text" v-model="selectedUser.userEmail">

      <label for="userPhone">User Phone:</label>
      <input type="text" v-model="selectedUser.userPhone">

      <button type="submit" class="btn btn-success">Update User</button>
    </form>
  </div>
</div>

</template>


<script>
export default {
  name: "UserManagement",
  data() {
    return {
      users: [],
      selectedUser: {},
      isUpdateModalVisible: true,
    };
  },
  mounted() {
    this.fetchUserData();
  },
  methods: {
    fetchUserData() {
      fetch('http://localhost:8080/user/all')
        .then(response => response.json())
        .then(data => {
          this.users = data;
        })
        .catch(error => console.error('Error fetching user data:', error));
    },
    openUpdateModal(user) {
    this.selectedUser = { ...user };
    this.isUpdateModalVisible = true;
    // You can perform any additional setup for the update modal here
  },

  closeUpdateModal() {
    this.isUpdateModalVisible = false;
    // Reset any update modal-related data if needed
  },

  updateUser() {
    const updatedUser = {
      userId: this.selectedUser.userId,
      userName: this.selectedUser.userName,
      userType: this.selectedUser.userType,
      userEmail: this.selectedUser.userEmail,
      userPhone: this.selectedUser.userPhone,
    };

    // Implement logic to update user using this.selectedUser
    fetch('/user/update', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(updatedUser),
    })
      .then(response => {
        if (response.ok) {
          console.log('User updated successfully:', updatedUser);
          // After updating, close the modal and refresh the user data
          this.closeUpdateModal();
          this.fetchUserData();
        } else {
          console.error('Failed to update user:', response.statusText);
        }
      })
      .catch(error => console.error('Error updating user:', error));
  },
    
    deleteUser(userId) {
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
            this.fetchUserData(); // Refresh the user data
          } else {
            console.error('Failed to delete user with ID:', userId);
          }
        })
        .catch(error => console.error('Error deleting user:', error));
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
      responseType: 'blob', // Set the response type to 'blob' for binary data
      
    })
      .then(response => {
        // Check if the response is successful
        if (response.ok) {
          // Create a blob from the response data
          return response.blob();
        } else {
          console.error('Failed to fetch user Excel file:', response.statusText);
        }
      })
      .then(blob => {
        // Create a link element and trigger a download
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
  },
};


</script>

<style>
/* Add your custom styles here */
</style>
