<template>
  <div class="py-4 container-fluid">
    <div class="row">
      <div class="col-12">
        <email-card/>
      </div>
    </div>
    
  </div>
</template>

<script>

import EmailCard from "./components/EmailCard.vue"
export default {
  name: "File",
  components: {
   
    EmailCard
  },
  
};
</script>
