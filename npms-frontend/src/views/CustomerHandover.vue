<template>
<div class="goods-add">
  <el-card shadow="never">
    <template #header>
      <div class="clearfix">
        <span style="font-size:20px">Customer Handover</span>
      </div>
    </template>

    <div class="content">
      <el-row gutter="100">
        <!-- 移出方之列 -->
      <el-col :span="12">
        <div class="content-header">
          
        <el-row>
          <span>Handover:</span>
         </el-row> 
         <br/>
        <el-row style="width:100%" gutter="20">
        <!-- 客户查询框 -->
        <el-form :model="keyword">  
          <el-col :span="5">
              <el-input v-model="keyword.cusName" placeholder="Customer name"></el-input>
          </el-col>

          <el-col :span="5">
              <el-select v-model="keyword.cusSalesDeptId" placeholder="Department" clearable=true @clear="keyword.cusSalesDeptId = null">
                  <el-option v-for="item in deptOptions"
                    :key="item.deptId"
                    :label="item.deptName"
                    :value="item.deptId">
                  </el-option>
              </el-select>
          </el-col>

          <el-col :span="5">
                        <el-select v-model="keyword.cusCustomerManagerId" placeholder="Manager" clearable=true @clear="keyword.cusCustomerManagerId = null;">
                            <el-option v-for="item in empOptions"
                                :key="item.empId"
                                :label="item.empName"
                                :value="item.empId">
                            </el-option>
                        </el-select>
                    </el-col>

          <el-col :span="4">
              <el-button type="primary" @click="searchOutCusInfo()">Search</el-button>
              
          </el-col>

          <el-col :span="4">
            <el-button type="primary" @click="getHandoverPage()">Reset</el-button>
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
                        <el-table-column prop="cusId" label="Customer Id" align="center" show-overflow-tooltip/>
                        <el-table-column prop="cusName" label="Customer Name" align="center" show-overflow-tooltip/>
                        <el-table-column prop="cusSalesDeptName" label="Sales Department" align="center" show-overflow-tooltip width="150"/>
                        
                        <el-table-column label="Operations" align="center" width="300">
                            <template #default="scope">
                                <el-button size="small" round @click="getCusDetail(tableData[scope.$index]);drawer = true">More</el-button>
                                <el-button size="small" round @click="getOppByCusId(tableData[scope.$index]);oppListVisible = true">Opportunity</el-button>
                                <el-button size="small" round @click="deliverRow(scope.$index,tableData,recieverData)">Handover</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
      </div>

      </el-col>

      <!-- 接收方列 -->
      <el-col :span="12">
      <div class="content-header">
       <el-row>
          <span>Reciever:</span>
         </el-row> 
         <br/>
      <el-row style="width:100%" gutter="20">
        <!-- 客户查询框 -->
        <el-form :model="receiver">  

          <el-col :span="7">
              <el-select v-model="receiver.cusSalesDeptId" placeholder="Department">
                  <el-option v-for="item in deptOptions"
                    :key="item.deptId"
                    :label="item.deptName"
                    :value="item.deptId">
                  </el-option>
              </el-select>
          </el-col>

          <el-col :span="7">
                        <el-select v-model="receiver.cusCustomerManagerId" placeholder="Manager">
                            <el-option v-for="item in empOptions"
                                :key="item.empId"
                                :label="item.empName"
                                :value="item.empId">
                            </el-option>
                        </el-select>
                    </el-col>

          <el-col :span="4">
              <el-button type="primary" @click="handover()">Submit</el-button>
              
          </el-col>

        </el-form>
      </el-row>
      </div>

      <div class="content-main">
                    <el-table :data="recieverData" border stripe style="width: 100%; margin: 1em 0 ;margin-top:20px">
                        <el-table-column prop="cusId" label="Customer Id" align="center" show-overflow-tooltip/>
                        <el-table-column prop="cusName" label="Customer Name" align="center" show-overflow-tooltip/>
                        <el-table-column prop="cusSalesDeptName" label="Sales Department" align="center" show-overflow-tooltip width="150"/>
                        
                        <el-table-column label="Operations" align="center" width="300">
                            <template #default="scope">
                                <el-button size="small" round @click="getCusDetail(recieverData[scope.$index]);drawer = true">More</el-button>
                                <el-button size="small" round @click="getOppByCusId(recieverData[scope.$index]);oppListVisible = true">Opportunity</el-button>
                                <el-button size="small" round @click="deliverRow(scope.$index,recieverData,tableData)">Backout</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
      </div>

      </el-col>
      </el-row>
    </div>



  </el-card>
</div>

<!-- 客户详情描述页 -->
<el-drawer title="Customer Details" v-model="drawer" :with-header="false" direction="rtl" size="50%" >
        <el-descriptions title="Customer Primary Information" :column="2" border style="margin:1em">
            <el-descriptions-item label="Customer Id" label-align="right" align="center">{{ detailData.cusId }}</el-descriptions-item>
            <el-descriptions-item label="Customer Name" label-align="right" align="center">{{ detailData.cusName }}</el-descriptions-item>
            <el-descriptions-item label="Country" label-align="right" align="center">{{ detailData.cusCountry }}</el-descriptions-item>
            <el-descriptions-item label="City" label-align="right" align="center">{{ detailData.cusCity }}</el-descriptions-item>
            <el-descriptions-item label="Address" label-align="right" align="center">{{ detailData.cusAddress }}</el-descriptions-item>
            <el-descriptions-item label="Postcode" label-align="right" align="center">{{ detailData.cusPostcode }}</el-descriptions-item>
            <el-descriptions-item label="Taxpayer Id" label-align="right" align="center">{{ detailData.cusTaxpayerId }}</el-descriptions-item>
            <el-descriptions-item label="Tax Category" label-align="right" align="center">{{ detailData.cusTaxCategory }}</el-descriptions-item>
            <el-descriptions-item label="Account Allocation Section" label-align="right" align="center">{{ detailData.cusAccountAllocationSection }}</el-descriptions-item>
            <el-descriptions-item label="Enterprise Property" label-align="right" align="center">{{ detailData.cusEnterpriseProperty }}</el-descriptions-item>
            <el-descriptions-item label="Annual Sales Revenue" label-align="right" align="center">{{ detailData.cusAnnualSalesRevenue }}</el-descriptions-item>
            <el-descriptions-item label="Product Type" label-align="right" align="center">{{ detailData.cusProductType }}</el-descriptions-item>
            <el-descriptions-item label="Software Brand" label-align="right" align="center">{{ detailData.cusSoftwareBrand }}</el-descriptions-item>
            <el-descriptions-item label="Website" label-align="right" align="center">{{ detailData.cusWebsite }}</el-descriptions-item>
            <el-descriptions-item label="Effect of Informationization" label-align="right" align="center">{{ detailData.cusEffectOfInformationization }}</el-descriptions-item>
        </el-descriptions>

        <dir style="padding: 1em">
            <div class="el-descriptions__title">Customer Contact Information</div>
            <br/>
            <el-table :data="contactData" style="width: 100%" border  max-height="163">
                <el-table-column prop="conName" label="Name" align="center"></el-table-column>
                <el-table-column prop="conDept" label="Department" align="center"></el-table-column>
                <el-table-column prop="conRole" label="Role" align="center"></el-table-column>
                <el-table-column prop="conLandlinePhone" label="Landline Phone" align="center"></el-table-column>
                <el-table-column prop="conMobilePhone" label="Mobile Phone" align="center"></el-table-column>
                <el-table-column prop="conFax" label="Fax" align="center"></el-table-column>
                <el-table-column prop="conEmail" label="Email" align="center"></el-table-column>
                <el-table-column prop="conQq" label="QQ" align="center"></el-table-column>
                <el-table-column prop="conWechat" label="WeChat" align="center"></el-table-column>
            </el-table>
        </dir>

        <dir style="padding: 1em">
            <div class="el-descriptions__title">Related Customers List</div>
            <br/>
            <el-table :data="relationData" style="width: 100%" border  max-height="163">
                <el-table-column prop="cusrelCusRelatedCusId" label="Related Customer Id" align="center" width="180"></el-table-column>
                <el-table-column prop="cusrelCusRelatedCusName" label="Related Customer Name" align="center" width="180"></el-table-column>
                <el-table-column prop="cusrelRelation" label="Relations" align="center"></el-table-column>
            </el-table>
        </dir>

</el-drawer>

<el-dialog v-model="oppListVisible">
  <el-table :data="oppList" border>
                        <el-table-column prop="oppId" label="Opportunity Id" align="center" show-overflow-tooltip/>
                        <el-table-column prop="oppName" label="Opportunity Name" align="center" show-overflow-tooltip/>
                        
                        <el-table-column label="Operations" align="center" width="200">
                            <template #default="scope">
                                <el-button size="small" round @click="showOppDetail(oppList[scope.$index]);oppDetailVisible = true">Sub Opportunity</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
    
        <template #footer>
            <span class="dialog-footer">
            <el-button type="primary" @click="oppListVisible = false">Back</el-button>
            </span>
        </template>
    </el-dialog>

    <el-dialog v-model="oppDetailVisible">
    
        <template #footer>
            <span class="dialog-footer">
            <el-button type="primary" @click="oppListVisible = false">Back</el-button>
            </span>
        </template>
    </el-dialog>


    
</template>

<script>
import {onMounted, reactive, toRefs} from 'vue'
import { ElNotification } from 'element-plus'  
export default {
  setup(){
    const state = reactive({
      emp_info:{
                    empId:'',
                    access_token:''
                },
    })
    onMounted(() => {
                init()
            })

            // 初始化数据
            const init = () =>{
                state.emp_info.empId = window.sessionStorage.getItem('username');
                state.emp_info.access_token = window.sessionStorage.getItem('token');
                console.log(state.emp_info);

            }
            return {
                ...toRefs(state),
                init,
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
            cusCustomerManagerId:null
        },
        tableData:[],
        total:'',
        loading: true,

        
        

        deptOptions:[],
        empOptions:[],

        //查看用户信息页数据
        drawer:false,
        detailData:{},
        contactData:[],
        relationData:[],

        //查看用户机会页数据
        oppListVisible:false,
        oppList:[],

        //机会详情页数据
        oppDetailVisible:false,
        oppData:{},
        subOppList:[],
        competitorList:[],
        payerList:[],

        //接受者
        receiver:{
          cusSalesDeptId:null,
          cusCustomerManagerId:null
        },
        recieverData:[],


      };
    },

    methods: {
      //页面初始化
      getHandoverPage(){
        this.axios.get('http://localhost:14000/cusManagement/cusManagement/handoverlog/getHandoverPage',{params:{access_token:this.emp_info.access_token}})
                .then((res)=>{
                    console.log(res.data);
                    this.deptOptions = res.data.deptInfoList;
                    this.empOptions = res.data.empInfoList;
                    this.keyword.cusCustomerManagerId = this.empOptions[0].empId;
                    this.total = this.tableData.length;
                    this.loading = false;
                })
      },
      //销售部门和客户经理二级联动
      //查询移出方的客户和机会信息
      searchOutCusInfo(){
        console.log(this.keyword)
          this.axios.post('http://localhost:14000/cusManagement/cusManagement/handoverlog/searchOutCusInfo',this.keyword,{params:{access_token:this.emp_info.access_token}})
          .then((res)=>{
              console.log(res.data);
              this.tableData = res.data;
              this.getDeptName();
              this.loading = false;
              this.total = this.tableData.length;
          })
      },
      //--通过部门ID找到部门名称
      getDeptName(){
        for(var j=0;j<this.tableData.length;j++){
            for(var i=0;i<this.deptOptions.length;i++){
              if(this.tableData[j].cusSalesDeptId == this.deptOptions[i].deptId){
                this.tableData[j].cusSalesDeptName = this.deptOptions[i].deptName;
              }
            }
      }
      },
      //点击客户id，展示客户详细信息
      getCusDetail(row){
                this.axios.get('http://localhost:14000/cusManagement/cusManagement/customer/getCusDetail',{params:{cusId:row.cusId,access_token:this.emp_info.access_token}})
                .then((res)=>{
                    console.log(res.data);          
                    this.detailData = res.data.customer;
                    this.contactData = res.data.contactList;
                    this.relationData = res.data.relationList;
                })
            },
      //点击机会id，展示机会详细信息
      showOppDetail(row){
                this.axios.get('http://localhost:14000/cusManagement/cusManagement/handoverlog/showOppDetail',{params:{cusId:row.oppId,access_token:this.emp_info.access_token}})
                .then((res)=>{
                    console.log(res.data);          
                    this.oppData = res.data.OpportunityBasicInfo;
                    this.subOppList = res.data.subOpportunityList;
                    this.competitorList = res.data.competitorList;
                    this.payerList = res.data.payerList;
                })
      },

      //选择客户，展示对应的机会列表
      getOppByCusId(row){
        this.axios.get('http://localhost:14000/cusManagement/cusManagement/handoverlog/getOppByCusId',{params:{cusId:row.cusId,access_token:this.emp_info.access_token}})
                .then((res)=>{
                    console.log(res.data);          
                    this.oppList = res.data;
                })
      },
      //移交（同时移交客户和对应的机会）
      handover(){
        console.log(this.receiver);
        console.log(this.receiverData);
          this.axios.post('http://localhost:14000/cusManagement/cusManagement/handoverlog/handover',
          {
            outDeptId:this.keyword.cusSalesDeptId,
            outCusMgrId:this.keyword.cusCustomerManagerId,
            inDeptId:this.receiver.cusSalesDeptId,
            inCusMgrId:this.receiver.cusCustomerManagerId,
            customerList:this.recieverData,
          },{params:{access_token:this.emp_info.access_token}})
          .then((res)=>{
              console.log(res.data);
              this.recieverData = [];
              this.notification('Customer Handover Success');
          })
      },
      //--交换行
      deliverRow(index,rows1,rows2){
          var target = rows1[index];
          var length = rows2.length;
          rows2.splice(length,0,target);
          rows1.splice(index,1);
      },

      notification(str){
                ElNotification({
                    title: 'System Notification',
                    message: str,
                    duration: 2000,
                    type: 'success'
                })
            },

    }
}
</script>

<style>
  
</style>