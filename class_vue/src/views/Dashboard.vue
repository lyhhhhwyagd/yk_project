<template>
  <div class="py-4 container-fluid">
    <div class="row">
      <div class="col-12">
        <calendar-card/>
      </div>
    </div>
    
  </div>
</template>

<script>

import CalendarCard from "./components/CalendarCard.vue"
export default {
  name: "File",
  components: {
   
    CalendarCard
  },
  
};
</script>
