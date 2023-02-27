<template>
    <div class="goods-list">
        <el-card shadow="never">
            <template #header>
                <div class="clearfix">
                    <span style="font-size:20px">Customer Relations Query</span>
                </div>
            </template>
            <div class="content">
                <div class="content-header">
                  
                    <el-row style="width:100%" gutter="20">
                    <el-form :model="keyword">  
                    <el-col :span="4">
                        <el-input v-model="keyword.cusId" placeholder="enter the customer ID"></el-input>
                    </el-col>

                    <el-col :span="4">
                        <el-input v-model="keyword.cusName" placeholder="enter the customer name"></el-input>
                    </el-col>

                    <el-col :span="4">
                        <el-select v-model="keyword.cusSalesDeptId" placeholder="select the department" clearable=true @clear="keyword.cusSalesDeptId = null">
                            <el-option v-for="item in deptOptions"
                                :key="item.deptId"
                                :label="item.deptName"
                                :value="item.deptId">
                            </el-option>
                        </el-select>
                    </el-col>

                    <el-col :span="4">
                        <el-select v-model="keyword.cusCustomerManagerId" placeholder="select the manager" clearable=true @clear="clearCustomerManagerId()">
                            <el-option v-for="item in empOptions"
                                :key="item.empId"
                                :label="item.empName"
                                :value="item.empId">
                            </el-option>
                        </el-select>
                    </el-col>

                    <el-col :span="4">
                        <el-button type="primary" @click="getCustomers()" style="float:left">Search</el-button>
                    </el-col>

                    <el-col :span="4">
                        <el-button type="primary" @click="getMainPage()">Reset</el-button>
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
                        <el-table-column prop="cusProductType" label="Product Type" align="center" show-overflow-tooltip/>
                        <el-table-column prop="cusEnterpriseProperty" label="Enterprise Property" align="center" show-overflow-tooltip/>
                        <el-table-column prop="cusSalesDeptName" label="Sales Department" align="center" show-overflow-tooltip/>
                        <el-table-column prop="cusCustomerManagerName" label="Customer Manager" align="center" show-overflow-tooltip/>
                        
                        <el-table-column label="Operations" align="center" width="210">
                            <template #default="scope">
                                <el-button size="small" round @click="getCusDetail(scope.$index);drawer = true">Relations</el-button>
                            </template>
                        </el-table-column>
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

    <el-drawer v-model="drawer" :with-header="false" direction="rtl" size="50%" >
        <dir style="padding: 1em">
            <div class="el-descriptions__title">Customer Contacts</div>
            <br/>
            <el-table :data="contactData" style="width: 100%" border  max-height="400">
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
            <div class="el-descriptions__title">Customers Relations</div>
            <br/>
            <el-table :data="relationData" style="width: 100%" border  max-height="400">
                <el-table-column prop="cusrelCusRelatedCusId" label="Related Customer Id" align="center" width="180"></el-table-column>
                <el-table-column prop="cusrelCusRelatedCusName" label="Related Customer Name" align="center" width="180"></el-table-column>
                <el-table-column prop="cusrelRelation" label="Relations" align="center"></el-table-column>
            </el-table>
        </dir>

    </el-drawer>

</template>

<script>
    import {onMounted, reactive, toRefs, ref} from 'vue'
    // import {useRouter} from 'vue-router'
    // import {getMainPage} from '@/api/serviceApi'
    import {ElMessageBox, ElNotification} from 'element-plus'  

    export default {
        setup() {
            // const router = useRouter()
            const state = reactive({
                currentPage: 1,
                pageSizes: [10, 20, 50, 100],
                pageSize: 10,

                formLabelWidth: '180px',

                
                addCustomerFormVisible:false,
                addContactFormVisible:false,
                addContactFormItemVisible:false,
                addRelatedFormVisible:false,
                addRelatedFormItemVisible:false,  
                
                emp_info:{
                    empId:'',
                    access_token:'',
                    position:''
                },

            })

            onMounted(() => {
                init()
            })

            // 初始化数据
            const init = () =>{
                state.emp_info.empId = window.sessionStorage.getItem('username');
                state.emp_info.access_token = window.sessionStorage.getItem('token');
                state.emp_position = window.sessionStorage.getItem('position');
                console.log(state.emp_info);
                state.loading = false;

            }
            
            return {
                ...toRefs(state),
                drawer: ref(false),
                init,
            }
        },
        data(){
            return{
                tableData:[],
                total:0,
                loading: true,
                
                currentIndex:-1,

                deptOptions:[],
                empOptions:[],
                cityProps:{ expandTrigger: 'hover' },

                keyword:{
                    cusName:null,
                    cusId:null,
                    cusEnterpriseProperty:null,
                    cusSalesDeptId:null,
                    cusCustomerManagerId:null
                },
                countryOptions:[{
                    lable:'China',
                    value:'China',
                    },{
                    lable:'Italy',
                    value:'Italy',
                }],

                provinceOptions:[],
                cityOptions:[], 
                selectCountryId:0,
                selectProvinceId:0,              
                chinaList:[{
                    lable:'Liaoning',
                    value:'Liaoning',
                    },{
                    lable:'Hebei',
                    value:'Hebei',
                }],
                italyList:[{
                    lable:'Lazio',
                    value:'Lazio',
                    },{
                    lable:'Tuscany',
                    value:'Tuscany',
                }],
                liaoningList:[{
                    lable:'Shenyang',
                    value:'Shenyang',
                    },{
                    lable:'Dalian',
                    value:'Dalian',
                }],
                hebeiList:[{
                    lable:'Baoding',
                    value:'Baoding',
                    },{
                    lable:'Tangshan',
                    value:'Tangshan',
                }],
                lazioList:[{
                    lable:'Rome',
                    value:'Rome',
                    },{
                    lable:'Latina',
                    value:'Latina',
                }],
                tuscanyList:[{
                    lable:'Florence',
                    value:'Florence',
                    },{
                    lable:'Pisa',
                    value:'Pisa',
                }],

                addCustomer:{},
                addContact:[],                   
                addRelated:[],

                addContactItem:[],
                addRelatedItem:[],     

                addOrUpdateItemState:0,
                addOrUpdateState: 0,
                addOrUpdateList:{},

                detailData:[],
                contactData:[],
                relationData:[],
            }
        },
        methods:{
            getMainPage(){
                this.axios.get('http://localhost:14000/cusManagement/cusManagement/customer/getMainPage',{params:this.emp_info})
                .then((res)=>{
                    console.log(res.data);
                    this.deptOptions = res.data.deptInfoList;
                    this.empOptions = res.data.empInfoList;
                    this.keyword.cusCustomerManagerId = this.empOptions[0].empId;
                    this.tableData = res.data.cusSearchResultList;
                    this.total = this.tableData.length;
                    this.loading = false;
                })
            },
            getCusDetail(index){
                var target = this.tableData[index]; 
                this.axios.get('http://localhost:14000/cusManagement/cusManagement/customer/getCusDetail',{params:{cusId:target.cusId,access_token:this.emp_info.access_token}})
                .then((res)=>{
                    console.log(res.data);          
                    this.detailData = res.data.customer;
                    this.contactData = res.data.contactList;
                    this.relationData = res.data.relationList;
                })
            },
            getCusEdit(index){
                var target = this.tableData[index]; 
                this.axios.get('http://localhost:14000/cusManagement/cusManagement/customer/getCusDetail',{params:{cusId:target.cusId,access_token:this.emp_info.access_token}})
                .then((res)=>{
                    console.log(res.data);          
                    this.addCustomer = res.data.customer;
                    this.addContact = res.data.contactList;
                    this.addRelated = res.data.relationList;
                })
            },
            getCustomers(){
                console.log(this.keyword)
                this.axios.post('http://localhost:14000/cusManagement/cusManagement/customer/getCustomers',this.keyword,{params:{access_token:this.emp_info.access_token}})
                .then((res)=>{
                    console.log(res.data);
                    this.tableData = res.data;
                    this.loading = false;
                    this.total = this.tableData.length;
                })
            },
            clearCustomerManagerId(){
                if(this.empOptions.length > 1){
                    this.keyword.cusCustomerManagerId = null;
                }else{
                    this.keyword.cusCustomerManagerId = this.empOptions[0].empId;
                }
            },
            finish(){
                this.addCustomer.cusSalesDeptId = this.emp_info.empDeptId;
                this.addCustomer.cusCustomerManagerId = this.emp_info.empId;

                console.log(this.addCustomer);
                console.log(this.addContact);
                console.log(this.addRelated);
               
                if(this.addOrUpdateState == 0){
                    this.axios.post('http://localhost:14000/cusManagement/cusManagement/customer/addCustomers',
                    {customer:this.addCustomer,contactList:this.addContact,relationList:this.addRelated})
                    .then((res)=>{
                        console.log(res.data);
                        this.notification(res.data);                 
                    })
                }
                if(this.addOrUpdateState == 1){
                    this.axios.post('http://localhost:14000/cusManagement/cusManagement/customer/updateCustomers',
                    {customer:this.addCustomer,contactList:this.addContact,relationList:this.addRelated})
                    .then((res)=>{
                        console.log(res.data);
                        this.notification(res.data);                 
                    })
                }
                
            },
            handleClick(rows){
                console.log(rows)
            },

            query(){
            },

            addOrUpdate(){
                if(this.addOrUpdateState == 0){

                    this.addOrUpdateList = {
                        customerTitle:'New Customer-Primary Information',
                        contactListTitle:'New Customer-Customer Contacts',
                        contactItemTitle:'New Customer-Add Customer Contact',
                        relatedListTitle:'New Customer-Related Customers',
                        relatedItemTitle:'New Customer-Add Related Customers',
                    }
                }
                if(this.addOrUpdateState == 1){
                    this.addOrUpdateList = {
                        customerTitle:'Edit Customer-Primary Information',
                        contactListTitle:'Edit Customer-Customer Contacts',
                        contactItemTitle:'Edit Customer-Add Customer Contact',
                        relatedListTitle:'Edit Customer-Related Customers',
                        relatedItemTitle:'Edit Customer-Add Related Customers',
                    }
                }

                

            },

            sizeChange(val){
                this.pageSize = val
            },

            currentChange(val){
                this.currentPage = val
            },

            deleteRow(index, rows){
                ElMessageBox.confirm('Data will be deleted. Continue?', 'Notification', {
                    confirmButtonText: 'Confirm',
                    cancelButtonText: 'Cancel',
                    type: 'warning',
                    callback: action =>{
                        if('confirm' == action){
                            rows.splice(index, 1);
                            // init()
                            ElNotification({
                                title: 'System Notification',
                                message: 'Delete Successfully！',
                                duration: 2000,
                                type: 'success'
                            })
                        }
                    }
                })               
            },

            addRow(itemRows,rowsItem){
                //默认加至最后一行
                var length = itemRows.length;
                itemRows.splice(length,0,rowsItem);
            },

            updateRow(index,itemRows,rowsItem){
                itemRows.splice(index,1);
                itemRows.splice(index,0,rowsItem);
            },

            addOrUpdateItem(itemRows,rowsItem){
                if(this.addOrUpdateItemState == 0){
                    this.addRow(itemRows,rowsItem);
                    rowsItem = [];
                }
                if(this.addOrUpdateItemState == 1){
                    this.updateRow(this.currentIndex,itemRows,rowsItem);
                    rowsItem = [];
                }
               
            },
            selectCountry(countryVal){
                if(countryVal == 'China'){
                    this.provinceOptions = this.chinaList;
                }
                if(countryVal == 'Italy'){
                    this.provinceOptions = this.italyList;
                }
            },
            selectProvince(countryVal,provinceVal){
                if(countryVal == 'China'){
                    if(provinceVal == 'Liaoning'){
                        this.cityOptions = this.liaoningList;
                    }
                    if(provinceVal == 'Hebei'){
                        this.cityOptions = this.hebeiList;
                    }
                }
                if(countryVal == 'Italy'){
                    if(provinceVal == 'Lazio'){
                        this.cityOptions = this.lazioList;
                    }
                    if(provinceVal == 'Tuscany'){
                        this.cityOptions = this.tuscanyList;
                    }                  
                }
            },
            

            notification(str){
                ElNotification({
                    title: 'System Notification',
                    message: str,
                    duration: 2000,
                    type: 'success'
                })
            }
        }

    }

</script>

<style lang="less">
    @import '../common/style/common';

    .goods-list {
        .content-main{
            .table-image{
                width: 45px;
                height: 45px;
            }
        }
    }

</style>