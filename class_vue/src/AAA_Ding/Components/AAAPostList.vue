<template>
  <div class="card" style="width: 100%;">
    <div class="card-header pb-0 px-3">
      <h6 class="mb-0">{{ title }}</h6>
    </div>
    <div class="card-body pt-4 p-3">
      <ul class="list-group">
        <li v-for="({ title, content }, index) of bills" :key="index" class="list-group-item border-0 d-flex p-4 mb-2 bg-gray-100 border-radius-lg">
          <div class="d-flex flex-column justify-content-between" style="min-height: 15vh; overflow: hidden;" id="container">
              <div>
                <h3 class="mb-3 textL1">{{ title }}</h3>
                <span class="mb-2 text-xs">
                  <span class="text-dark textL3 font-weight-bold preserve-format" id="content">{{ content }}</span>
                </span>
              </div>
              <div class="d-flex justify-content-between">
                <div>
                  <a class="btn btn-link text-danger text-gradient px-3 mb-0">
                    <i class="fas fa-thumbs-up me-2" aria-hidden="true"></i>点赞
                  </a>
                  <a class="btn btn-link text-dark px-3 mb-0">
                    <i class="fas fa-pencil-alt text-dark me-2" aria-hidden="true"></i>评论
                  </a>
                  <a class="btn btn-link px-3 mb-0" id="expand-button">
                    <i class="fas fa-arrow-down"></i>阅读全文
                  </a>
                  <a class="btn btn-link text-dark px-3 mb-0">
                    <i class="fas fa-edit text-dark me-2" aria-hidden="true"></i>修改
                  </a>
                  <a href="javascript:;" class="btn btn-link text-danger text-gradient px-3 mb-0" @click="confirmDelete(bills[index].id)">
                    <i class="fas fa-trash text-danger me-2" aria-hidden="true"></i>删除
                  </a>
                </div>
              </div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>
  
  <script>
  import axios from "axios";

  export default {
    name: "BillingCard",
    props: {
      title: {
        type: String,
        default: "",
      },
      bills: {
        type: Array,
        title: String,
        content: String,
        author: String,
        id: String,
        createTime: String,
        default: () => [],
      },
    },
    methods: {
      confirmDelete(id) {
        console.log("确认删除？");
        if (window.confirm('确认删除？')) {
          this.deleteItem(id);
        }
      },
      async deleteItem(id) {
        console.log("开始删除");
        try {
          const response = await axios.delete(`http://localhost:8081/post/${id}`);
          if (response.data.code === 200) {
            console.log('删除成功');
            window.location.reload();
          } else {
            console.error('Error updating reward: ' + response.data.message);
          }
        } catch (error) {
          console.error(error);
        }
      },
    },
  };
  </script>
<style scoped>
.preserve-format {
  white-space: pre-wrap;
}
.textL1 {
  font-size: 20px; /* Adjust to your desired size */
}

.textL3 {
  font-size: 14px; /* Adjust to your desired size */
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
