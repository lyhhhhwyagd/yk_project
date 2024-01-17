<template>
  <div class="dual-range-slider">
    <input
        type="range"
        :min="min"
        :max="max"
        v-model="lowerValue"
        @input="updateValues"
    />
    <input
        type="range"
        :min="min"
        :max="max"
        v-model="upperValue"
        @input="updateValues"
    />
  </div>
</template>

<script>
export default {
  props: {
    min: {
      type: Number,
      required: true
    },
    max: {
      type: Number,
      required: true
    },
    initialLower: {
      type: Number,
      required: true
    },
    initialUpper: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      lowerValue: this.initialLower,
      upperValue: this.initialUpper
    };
  },
  methods: {
    updateValues() {
      if (this.lowerValue > this.upperValue) {
        let temp = this.lowerValue;
        this.lowerValue = this.upperValue;
        this.upperValue = temp;
      }
      this.$emit('update:lower', this.lowerValue);
      this.$emit('update:upper', this.upperValue);
    }
  }
};
</script>

<style>
.dual-range-slider input[type=range] {
  position: absolute;
}
</style>