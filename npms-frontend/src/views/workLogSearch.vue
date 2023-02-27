<template>
<div class="goods-add">
  <el-card shadow="never">
      <template #header>
        <div class="clearfix">
            <span style="font-size:20px">Worklog Search</span>
        </div>
    </template>

    <div class="content">
        <div class="content-header">           
            <el-row style="width:100%" gutter="50">
            <el-form :model="keyword">  
            <el-col :span="4">
                <el-select v-model="keyword.cusSalesDeptId" placeholder="Department" clearable=true @clear="keyword.cusSalesDeptId = null">
                    <el-option v-for="item in deptOptions"
                        :key="item.id"
                        :label="item.deptName"
                        :value="item.id">
                    </el-option>
                </el-select>
            </el-col>

            <el-col :span="4">
                <el-select v-model="keyword.cusCustomerManagerId" placeholder="Manager" clearable=true @clear="clearCustomerManagerId()">
                    <el-option v-for="item in empOptions"
                        :key="item.empId"
                        :label="item.empName"
                        :value="item.empId">
                    </el-option>
                </el-select>
            </el-col>

            <el-col :span="6">
                <el-date-picker
                v-model="keyword.startTime"
                type="date"
                placeholder="pick date"
                :disabled-date="disabledDate"
                :shortcuts="shortcuts"
                ></el-date-picker>
            </el-col>

            <el-col :span="6">
                <el-date-picker
                v-model="keyword.endTime"
                type="date"
                placeholder="pick date"
                :disabled-date="disabledDate"
                :shortcuts="shortcuts"
                ></el-date-picker>
            </el-col>

            <el-col :span="4">   
                <el-button type="primary" @click="query()">Search</el-button>
            </el-col>

            <el-col :span="4">   
                <el-button type="primary" @click="getSalesLogPage()">Reset</el-button>
            </el-col>

            </el-form>

            </el-row>  

        </div>

        <div class="content-main">
            <el-table :data="tableData" border stripe style="width: 100%; margin: 1em 0 ;margin-top:20px"
                        v-loading="loading"
                        element-loading-text="Loading..."
                        element-loading-spinner="el-icon-loading"
                        element-loading-background="rgba(0, 0, 0, 0.8)">
                <el-table-column prop="deptName" label="Sales Department" align="center" show-overflow-tooltip/>
                <el-table-column prop="cusMgrID" label="Customer Manager Id" align="center" show-overflow-tooltip/>
                <el-table-column prop="cusMgrName" label="Customer Manager Name" align="center" show-overflow-tooltip/>
                <el-table-column prop="time" label="Time" align="center" show-overflow-tooltip/>
                <el-table-column prop="content" label="Work Content" align="center" show-overflow-tooltip/>

            </el-table>

            <el-pagination @size-change="sizeChange" @current-change="currentChange"
                            layout="total, sizes, prev, pager, next, jumper"
                            :current-page="currentPage"
                            :page-sizes="pageSizes"
                            :page-size="pageSize"
                            :total="total"/>
                </div>
    </div>    
  </el-card>
</div>
</template>
<script>
import { onMounted,reactive,toRefs } from 'vue'

export default{
    setup() {
            // const router = useRouter()
            const state = reactive({
                currentPage: 1,
                pageSizes: [10, 20, 50, 100],
                pageSize: 10,

                formLabelWidth: '180px',
                emp_info:{
                    empId:'',
                    access_token:''
                },

            })
            onMounted(() => {
                getEmp()
            })
            const getEmp = () =>{
                state.emp_info.empId = window.sessionStorage.getItem('username');
                state.emp_info.access_token = window.sessionStorage.getItem('token');
                console.log(state.emp_info);

            }
            return {
                ...toRefs(state),
                getEmp
            }
        },
   data(){
       return{
           loading: true,
           deptOptions:[],
           empOptions:[],
           total:0,
           keyword:{},
           tableData:[],

           disabledDate(time) {
           return time.getTime() > Date.now()
        },
           shortcuts: [{
           text: 'Today',
           value: new Date(),
        }, {
          text: 'Yesterday',
          value: (() => {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24)
            return date
          })(),
        }, {
          text: 'A week ago',
          value: (() => {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
            return date
          })(),
        }],

       }
   },
   methods:{
       getSalesLogPage(){
        this.axios.get('/npmsBackEndServer/report-view/init' + '/' +this.emp_info.empId)
                .then((res)=>{
                    console.log(res.data);
                    this.deptOptions = res.data.body.depts;
                    this.empOptions = res.data.body.depts[0].cusMgrs;
                    console.log(this.empOptions);
                    this.tableData = [];
                    this.total = this.tableData.length;
                    this.loading = false;
                })
      },
      query(){
          this.loading = true;
          this.total = 0;
          this.axios.get('/npmsBackEndServer/report-view/query',
          {params:{
              deptID:this.keyword.cusSalesDeptId,
              cusMgrID:this.keyword.cusCustomerManagerId,
              timeFrom:this.keyword.startTime,
              timeTo:this.keyword.endTime,
          }}).then((res)=>{
                    console.log(res.data);
                    if(res.data != null){
                        this.tableData = res.data.body.entries;
                        this.total = this.tableData.length;
                        this.loading = false;
                    }    
                })


      }

      
      
   }
}
</script>
<style>

</style>
