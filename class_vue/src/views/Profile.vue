<template>
  <div class="py-4 container-fluid">
    <div class="row">
      <div class="col-12">
        <billing-card/>
      </div>
    </div>
    
  </div>
</template>

<script>

import BillingCard from "./components/BillingCard.vue"
export default {
  name: "ProfileOverview",
  components: {
   
    BillingCard
  },
  
};
</script>
