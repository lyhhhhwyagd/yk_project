<template>
  <div class='bg'>
    <div class='customer'>
      <div style="margin-top: 20%" class="title">完成填写！</div>
        <el-button  type="primary" style="background-color: rgb(128, 214, 22);margin-left: 50%" @click="back()">再填一次</el-button>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    back() {
      const paperId = this.$route.params.paperId
      console.log(paperId)
      this.$router.push({ name: 'customer', params: { paperId } })
    }
  }
}
</script>

<style scoped>
.customer > .title {
  text-align: center;
  font-size: 100px;
}
</style>
