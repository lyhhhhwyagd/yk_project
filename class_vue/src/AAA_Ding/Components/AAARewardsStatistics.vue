<template>
  <div class="col-md-12">
    <!-- line chart -->
    <div class="col-md-1"></div>
    <div class="card z-index-2 col-md-10">
      <gradient-line-chart
          id="chart-line"
          title="Gradient Line Chart"
          description="<i class='fa fa-arrow-up text-success'></i>
      <span class='font-weight-bold'>4% more</span> in 2021"
          :chart="{
              labels: [
                'Apr',
                'May',
                'Jun',
                'Jul',
                'Aug',
                'Sep',
                'Oct',
                'Nov',
                'Dec',
              ],
              datasets: [
                {
                  label: 'Mobile Apps',
                  data: [50, 40, 300, 220, 500, 250, 400, 230, 500],
                },
                {
                  label: 'Websites',
                  data: [30, 90, 40, 140, 290, 290, 340, 230, 400],
                },
              ],
            }"
      />

    </div>
  </div>
  <br>
  <div class="col-lg-12">
    <!-- line chart -->
    <div class="col-md-1"></div>
    <div class="card z-index-2 col-md-10">
      <gradient-line-chart
          id="chart-line"
          title="Gradient Line Chart"
          description="<i class='fa fa-arrow-up text-success'></i>
      <span class='font-weight-bold'>4% more</span> in 2021"
          :chart="{
              labels: [
                'Apr',
                'May',
                'Jun',
                'Jul',
                'Aug',
                'Sep',
                'Oct',
                'Nov',
                'Dec',
              ],
              datasets: [
                {
                  label: 'Mobile Apps',
                  data: [50, 40, 300, 220, 500, 250, 400, 230, 500],
                },
                {
                  label: 'Websites',
                  data: [30, 90, 40, 140, 290, 290, 340, 230, 400],
                },
              ],
            }"
      />

    </div>
  </div>
</template>

<script setup>

</script>