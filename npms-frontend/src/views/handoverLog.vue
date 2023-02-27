<template>
<div class="goods-add">
  <el-card shadow="never">
    <template #header>
      <div class="clearfix">
        <span style="font-size:20px">Customer Handover Query</span>
      </div>
    </template>

    <div class="content">
      <el-row gutter="100">
        <!-- 移出方之列 -->
     
        <div class="content-header">
        <el-col :span="12">
          
        <el-row>
          <span>Handover:</span>
         </el-row> 
         <br/>
        <el-row style="width:100%" gutter="20">

        <el-form :model="keyword">  
          <el-col :span="5">
              <el-select v-model="keyword.cusSalesDeptId" placeholder="Department" clearable=true>
                  <el-option v-for="item in deptOptions"
                    :key="item.deptId"
                    :label="item.deptName"
                    :value="item.deptId">
                  </el-option>
              </el-select>
          </el-col>

          <el-col :span="5">
                        <el-select v-model="keyword.cusCustomerManagerId" placeholder="Manager" clearable=true>
                            <el-option v-for="item in empOptions"
                                :key="item.empId"
                                :label="item.empName"
                                :value="item.empId">
                            </el-option>
                        </el-select>
                    </el-col>

          <el-col :span="6">
              <el-input v-model="keyword.cusName" placeholder="Customer name"></el-input>
          </el-col>

           <el-col :span="6">
              <el-input v-model="keyword.oppName" placeholder="Opportunity name"></el-input>
          </el-col>

        </el-form>
      </el-row>
      </el-col>
      <el-col :span="12">
                 <el-row>
          <span>Reciever:</span>
         </el-row> 
         <br/>
      <el-row style="width:100%" gutter="20">

        <el-form :model="receiver">  

          <el-col :span="7">
              <el-select v-model="keyword.recDeptId" placeholder="Department" clearable=true>
                  <el-option v-for="item in deptOptions"
                    :key="item.deptId"
                    :label="item.deptName"
                    :value="item.deptId">
                  </el-option>
              </el-select>
          </el-col>

          <el-col :span="7">
                        <el-select v-model="keyword.recCusMngId" placeholder="Manager" clearable=true>
                            <el-option v-for="item in empOptions"
                                :key="item.empId"
                                :label="item.empName"
                                :value="item.empId">
                            </el-option>
                        </el-select>
                    </el-col>

          <el-col :span="4">
              <el-button type="primary" @click="query()">Search</el-button>
             
          </el-col>
           <el-col :span="4">
              <el-button type="primary" @click="init()">Reset</el-button>
             
          </el-col>

        </el-form>
      </el-row>
      </el-col>
      </div>
      </el-row>
      <div class="content-main">
                    <el-table :data="tableData" border stripe style="width: 100%; margin: 1em 0 ;margin-top:20px"
                              v-loading="loading"
                              element-loading-text="Loading..."
                              element-loading-spinner="el-icon-loading"
                              element-loading-background="rgba(0, 0, 0, 0.8)">
                        <el-table-column prop="oppoID" label="Opportunity Id" align="center"  width="150" show-overflow-tooltip/>
                        <el-table-column prop="oppoName" label="Opportunity Name" align="center" width="150" show-overflow-tooltip/>
                        <el-table-column prop="cusID" label="Customer Id" align="center" width="150" show-overflow-tooltip/>
                        <el-table-column prop="cusName" label="Customer Name" align="center" width="150" show-overflow-tooltip/>
                        <el-table-column prop="outDeptName" label="Handover Sales Department" align="center" show-overflow-tooltip/>
                        <el-table-column prop="outCusMgrName" label="Handover Customer Manager" align="center" show-overflow-tooltip/>
                        <el-table-column prop="inDeptName" label="Reciever Sales Department" align="center" show-overflow-tooltip/>
                        <el-table-column prop="inCusMgrName" label="Receiver Customer Manager" align="center" show-overflow-tooltip/>
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
import {onMounted, reactive, toRefs} from 'vue'
export default {
   setup() {
            // const router = useRouter()
            const state = reactive({               
                emp_info:{
                    empId:'',
                    access_token:'',
                    position:''
                },

            })

            onMounted(() => {
                getEmp()
            })

            // 初始化数据
            const getEmp = () =>{
                state.emp_info.empId = window.sessionStorage.getItem('username');
                state.emp_info.access_token = window.sessionStorage.getItem('token');
                state.emp_position = window.sessionStorage.getItem('position');
                console.log(state.emp_info);
                state.loading = false;

            }
            
            return {
                ...toRefs(state),
                getEmp,
            }
        },
    data() {  
      return {
        currentPage: 1,
        pageSizes: [10, 20, 50, 100],
        pageSize: 10,

        formLabelWidth: '180px',

        keyword:{
            cusName:null,
            cusSalesDeptId:null,
            cusCustomerManagerId:null,
            oppName:null,
            recDeptId:null,
            recCusMngId:null
        },
        tableData:[],
        total:0,
        loading: true,      
        
        deptOptions:[],
        empOptions:[],

      };
    },

    methods: {
      init(){
          this.axios.get('/npmsBackEndServer/handover/init' + '/' +this.emp_info.empId)
                .then((res)=>{
                    console.log(res.data);
                    this.deptOptions = res.data.body.depts;
                    this.empOptions = res.data.body.depts[0].cusMgrs;
                    console.log(this.empOptions);
                    this.loading = false;
                })
      },
      query(){
          this.loading = true;
           this.axios.get('/npmsBackEndServer/handover/query',
          {params:{
              inDeptID:this.keyword.cusSalesDeptId,
              outDeptID:this.keyword.recDeptId,
              inCusMgrID:this.keyword.cusCustomerManagerId,
              outCusMgrID:this.keyword.recCusMngId,
              cusName:this.keyword.cusName,
              oppName:this.keyword.oppName,
          }}).then((res)=>{
                    console.log(res.data);
                    this.tableData = res.data.body.entries;
                    this.total = this.tableData.length;
                    this.loading = false;
                })
      }

    }
}
</script>

<style>
  
</style>