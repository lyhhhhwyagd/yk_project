<template>
  <div class="py-4 container-fluid">
    <div class="row">
      <div class="col-12">
        <rewards-list/>
      </div>
    </div>
  </div>
</template>

<script>
import RewardsList from "@/AAA_Ding/Components/AAARewardsList.vue"

export default {
  name: "tables",
  components: {
    RewardsList,
  },
};
</script>
