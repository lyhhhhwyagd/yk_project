<template>
    <div class="py-4 container-fluid">
        <div class="row">
            <div class="col-12" >
                <sps></sps>
            </div>
        </div>
        <div class="row">
            <div class="col-12">

            </div>
        </div>
    </div>
</template>

<script >
    import  sps from "./components/sps"
    import {reactive} from "vue";
    export default {
        name: "sp.vue",
        components:
            {
                sps,
            },

    }




    //getUserType();

</script>

<style scoped>

</style>
