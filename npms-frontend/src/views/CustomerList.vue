<template>
    <div class="goods-list">
        <el-card shadow="never">
            <template #header>
                <div class="clearfix">
                    <span style="font-size:20px">Customer List</span>
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
                        <el-select v-model="keyword.cusEnterpriseProperty" placeholder="Enterprise Property" clearable=true @clear="keyword.cusEnterpriseProperty = null">
                            <el-option label="20" value="20"></el-option>
                        <el-option label="30" value="30"></el-option>
                        </el-select>
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
                        <el-button type="primary" @click="getMainPage()">Reset</el-button>
                    </el-col>
                    </el-form>

                    <el-col :span="4">
                       
                        <el-button type="danger" :disabled="newButtonDisable"  @click="addCustomer = {};addContact = [];addRelated = [];addOrUpdateState = 0;addOrUpdate();addCustomerFormVisible = true;disabled = newButtonDisable" style="float:right">New</el-button>
                    </el-col>
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
                                <el-button size="small" round @click="getCusDetail(scope.$index);drawer = true">More</el-button>
                                <el-button size="small" round @click="addOrUpdateState = 1;addOrUpdate();getCusEdit(scope.$index);addCustomerFormVisible = true"  :disabled="newButtonDisable">Edit</el-button>
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

    <el-dialog :title="addOrUpdateList.customerTitle" v-model="addCustomerFormVisible">
        <el-form :model="addCustomer">

            <el-row>
                <el-form-item label="Customer Name" :label-width="formLabelWidth">
                    <el-input v-model="addCustomer.cusName"></el-input>
                </el-form-item>

                <el-form-item label="Country" :label-width="formLabelWidth">
                    <el-input v-model="addCustomer.cusCountry" :disabled="true"></el-input>
                </el-form-item>     
            </el-row>

            <el-row>
                <el-form-item label="Postcode" :label-width="formLabelWidth">
                    <el-input v-model="addCustomer.cusPostcode"></el-input>
                </el-form-item> 

                <el-form-item label="Website" :label-width="formLabelWidth">
                    <el-input v-model="addCustomer.cusWebsite"></el-input>
                </el-form-item> 
            </el-row>

             <el-row>
                <el-form-item label="Address" :label-width="formLabelWidth">
                    <el-input v-model="addCustomer.cusAddress"></el-input>
                </el-form-item>     

                <el-form-item label="City" :label-width="formLabelWidth">
                    <div>
                    <el-select v-model="addCustomer.cusCountry" placeholder="Country" @change="selectCountry(addCustomer.cusCountry)" style="width:110px">
                        <el-option v-for="item in countryOptions"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                    </el-select>
                    <el-select v-model="addCustomer.cusProvince" placeholder="Province" @change="selectProvince(addCustomer.cusCountry,addCustomer.cusProvince)" style="width:110px">
                        <el-option v-for="item in provinceOptions"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                    </el-select>
                    <el-select v-model="addCustomer.cusCity" placeholder="City" style="width:110px">
                        <el-option v-for="item in cityOptions"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                    </el-select>
                    </div>
                </el-form-item>
               
            </el-row>

            <el-row>
                <el-form-item label="Taxpayer Id" :label-width="formLabelWidth">
                    <el-input v-model="addCustomer.cusTaxpayerId"></el-input>
                </el-form-item> 

                <el-form-item label="Tax Category" :label-width="formLabelWidth">
                    <el-select v-model="addCustomer.cusTaxCategory" placeholder="Select the category">
                        <el-option label="Category 1" value="1"></el-option>
                        <el-option label="Category 2" value="2"></el-option>
                    </el-select>
                </el-form-item>
            </el-row>

            <el-row>
                <el-form-item label="Product Type" :label-width="formLabelWidth">
                    <el-input v-model="addCustomer.cusProductType"></el-input>
                </el-form-item> 

                <el-form-item label="Enterprise Property" :label-width="formLabelWidth">
                    <el-select v-model="addCustomer.cusEnterpriseProperty" placeholder="Select the property">
                        <el-option label="Property 1" value="20"></el-option>
                        <el-option label="Property 2" value="30"></el-option> 
                    </el-select>
                </el-form-item>
            </el-row>

            <el-row>
                <el-form-item label="Annual Sales Revenue" :label-width="formLabelWidth">
                    <el-input v-model="addCustomer.cusAnnualSalesRevenue"></el-input>
                </el-form-item> 

                <el-form-item label="Account Allocation Section" :label-width="formLabelWidth">
                    <el-select v-model="addCustomer.cusAccountAllocationSection" placeholder="Select the section">
                        <el-option label="Section 1" value="10"></el-option>
                        <el-option label="Section 2" value="20"></el-option>
                    </el-select>
                </el-form-item>
            </el-row>

            <el-form-item label="Software Brand" :label-width="formLabelWidth">
                <el-input v-model="addCustomer.cusSoftwareBrand"></el-input>
            </el-form-item> 

            <el-form-item label="Effect of Informationization" :label-width="formLabelWidth">
               <el-input v-model="addCustomer.cusEffectOfInformationization"></el-input>
            </el-form-item>

        </el-form>
        <template #footer>
            <span class="dialog-footer">
            <el-button @click="addCustomerFormVisible = false">Cancel</el-button>
            <el-button type="primary" @click="addCustomerFormVisible = false;addContactFormVisible = true">Next</el-button>
            </span>
        </template>
    </el-dialog>

    <!-- 新增/编辑客户联系人列表 -->
    <el-dialog :title="addOrUpdateList.contactListTitle" v-model="addContactFormVisible" width="70%">
        <div style="width:100%;text-align:right;padding-bottom:20px">
            <el-button type="danger" @click="addOrUpdateItemState = 0;addContactFormItemVisible = true">New</el-button>
        </div>
                
        <el-table :data="addContact" style="width: 100%" border>
            <el-table-column prop="conName" label="Name" align="center"></el-table-column>
            <el-table-column prop="conDept" label="Department" align="center"></el-table-column>
            <el-table-column prop="conRole" label="Role" align="center"></el-table-column>
            <el-table-column prop="conLandlinePhone" label="Landline Phone" align="center"></el-table-column>
            <el-table-column prop="conMobilePhone" label="Mobile Phone" align="center"></el-table-column>
            <el-table-column prop="conFax" label="Fax" align="center"></el-table-column>
            <el-table-column prop="conEmail" label="Email" align="center"></el-table-column>
            <el-table-column prop="conQq" label="QQ" align="center"></el-table-column>
            <el-table-column prop="conWechat" label="WeChat" align="center"></el-table-column>
            <el-table-column fixed="right" label="Operations">
                <template #default="scope">
                    <el-button type="text" size="small" @click="addOrUpdateItemState = 1;currentIndex = scope.$index;addContactItem=addContact[currentIndex];addContactFormItemVisible = true">Edit</el-button>
                    <el-button @click.prevent="deleteRow(scope.$index, addContact);deleteContactsName.push(addContact[scope.$index].conName)" type="text" size="small">Remove</el-button>
                </template>
            </el-table-column>
        </el-table>

        <template #footer>
            <span class="dialog-footer">
            <el-button @click="addContactFormVisible = false">Cancel</el-button>
            <el-button type="primary" @click="addContactFormVisible = false;addCustomerFormVisible = true">Back</el-button>
            <el-button type="primary" @click="addContactFormVisible = false;addRelatedFormVisible = true">Next</el-button>
            </span>
        </template>
    </el-dialog>

    <!-- 添加/编辑用户联系人项 -->
    <el-dialog v-model="addContactFormItemVisible">
        <el-form :model="addContactItem">
            <el-row>
                <el-form-item label="Name" :label-width="formLabelWidth">
                    <el-input v-model="addContactItem.conName"></el-input>
                </el-form-item>

                <el-form-item label="Department" :label-width="formLabelWidth">
                    <el-input v-model="addContactItem.conDept"></el-input>
                </el-form-item>     
            </el-row>
            <el-row>
                <el-form-item label="Role" :label-width="formLabelWidth">
                    <el-input v-model="addContactItem.conRole"></el-input>
                </el-form-item>

                <el-form-item label="Landline Phone" :label-width="formLabelWidth">
                    <el-input v-model="addContactItem.conLandlinePhone"></el-input>
                </el-form-item>
            </el-row>
            <el-row>
                <el-form-item label="Mobile Phone" :label-width="formLabelWidth">
                    <el-input v-model="addContactItem.conMobilePhone"></el-input>
                </el-form-item>

                <el-form-item label="Fax" :label-width="formLabelWidth">
                    <el-input v-model="addContactItem.conFax"></el-input>
                </el-form-item>
            </el-row>
            <el-row>
                <el-form-item label="Email" :label-width="formLabelWidth">
                    <el-input v-model="addContactItem.conEmail"></el-input>
                </el-form-item>

                <el-form-item label="QQ" :label-width="formLabelWidth">
                    <el-input v-model="addContactItem.conQq"></el-input>
                </el-form-item>
            </el-row>
            <el-row>
                <el-form-item label="WeChat" :label-width="formLabelWidth">
                    <el-input v-model="addContactItem.conWechat"></el-input>
                </el-form-item>
            </el-row>
        </el-form>

        <template #footer>
            <span class="dialog-footer">
            <el-button @click="addContactFormItemVisible = false">Cancel</el-button>
            <el-button type="primary" @click="addOrUpdateItem(addContact,addContactItem);addContactFormItemVisible = false">Complete</el-button>
            </span>
        </template>
    </el-dialog>


    <!-- 新增/编辑客户关系列表 -->
    <el-dialog :title="addOrUpdateList.relatedListTitle" v-model="addRelatedFormVisible">
        <div style="width:100%;text-align:right;padding-bottom:20px">
            <el-button type="danger" @click="addOrUpdateItemState = 0;addRelatedFormItemVisible = true">New</el-button>
        </div>

        <el-table :data="addRelated" style="width: 100%" border>
            <el-table-column prop="cusrelCusRelatedCusId" label="Related Customer Id" align="center" width="200"></el-table-column>
            <el-table-column prop="cusrelCusRelatedCusName" label="Related Customer Name" align="center" width="200"></el-table-column>
            <el-table-column prop="cusrelRelation" label="Relations" align="center"></el-table-column>
            <el-table-column fixed="right" label="Operations" width="150">
                <template #default="scope">
                    <el-button type="text" size="small" disabled>Edit</el-button>
                    <el-button @click.prevent="deleteRow(scope.$index, addRelated);deleteRelationCusId.push(addRelated[scope.$index].cusrelCusRelatedCusId)" type="text" size="small">Remove</el-button>
                </template>
            </el-table-column>
        </el-table>
        <template #footer>
            <span class="dialog-footer">
            <el-button @click="addRelatedFormVisible = false">Cancel</el-button>
            <el-button type="primary" @click="addRelatedFormVisible = false;addContactFormVisible = true">Back</el-button>
            <el-button type="primary" @click="finish();addRelatedFormVisible = false">Finish</el-button>
            </span>
        </template>
    </el-dialog>

    <!-- 新增/编辑客户关系项 -->
    <el-dialog v-model="addRelatedFormItemVisible">
        <el-form :model="addRelatedItem">
            <el-form-item label="Related Customer Id" :label-width="formLabelWidth">
                <el-input v-model="addRelatedItem.cusrelCusRelatedCusId"></el-input>
            </el-form-item>

            <el-form-item label="Related Customer Name" :label-width="formLabelWidth">
                <el-input v-model="addRelatedItem.cusrelCusRelatedCusName"></el-input>
            </el-form-item>

            <el-form-item label="Relations" :label-width="formLabelWidth">
                <el-input v-model="addRelatedItem.cusrelRelation"></el-input>
            </el-form-item>
        </el-form>
       
        <template #footer>
            <span class="dialog-footer">
            <el-button @click="addRelatedFormItemVisible = false">Cancel</el-button>
            <el-button type="primary" @click="addOrUpdateItem(addRelated,addRelatedItem);addRelatedFormItemVisible = false">Complete</el-button>
            </span>
        </template>
    </el-dialog>

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

</template>

<script>
    import {onMounted, reactive, toRefs,ref } from 'vue'
    // import {useRouter} from 'vue-router'
    import {ElMessageBox, ElNotification} from 'element-plus'  

    export default {
        setup() {
            // const router = useRouter()
            const state = reactive({
                currentPage: 1,
                pageSizes: [10, 20, 50, 100],
                pageSize: 10,

                emp_info:{
                    empId:'',
                    access_token:''
                },
                emp_position:'',
                loading:true,
                newButtonDisable:true,

                formLabelWidth: '180px',

                
                addCustomerFormVisible:false,
                addContactFormVisible:false,
                addContactFormItemVisible:false,
                addRelatedFormVisible:false,
                addRelatedFormItemVisible:false,     
            })

            onMounted(() => {
                init()
                checkRoot()
            })

            // 初始化数据
            const init = () =>{
                state.emp_info.empId = window.sessionStorage.getItem('username');
                state.emp_info.access_token = window.sessionStorage.getItem('token');
                state.emp_position = window.sessionStorage.getItem('position');
                console.log(state.emp_info);
                state.loading = false;

            }
            const checkRoot = () =>{
                console.log(state.emp_position)
                if(state.emp_position == 'ROLE_30000030' || state.emp_position == 'ROLE_30000010'){
                    state.newButtonDisable = false;
                }
                console.log()
            }
            

            return {
                ...toRefs(state),
                drawer:ref(false),
                init,
                checkRoot,
            }
        },
        
        data(){
            return{
                tableData:[],
                total:0,
                              
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

                addContactsList:[],
                updateContactsList:[],
                deleteContactsName:[],
                addRelationList:[],
                deleteRelationCusId:[],

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
                console.log(this.addCustomer);
                console.log(this.addContact);
                console.log(this.addRelated);
                console.log(this.emp_info.access_token);

                console.log('----------------');
                console.log(this.addCustomer);
                console.log(this.addContactsList);
                console.log(this.updateContactsList);
                console.log(this.deleteContactsName);
                console.log(this.addRelationList);
                console.log(this.deleteRelationCusId);
                this.addCustomer.cusCustomerManagerId = this.emp_info.empId;
                if(this.addOrUpdateState == 0){
                    this.axios.post('http://localhost:14000/cusManagement/cusManagement/customer/addCustomers',

                    {customer:this.addCustomer,contactList:this.addContact,relationList:this.addRelated},{params:{access_token:this.emp_info.access_token}})
                    .then((res)=>{
                        console.log(res.data);
                        this.notification(res.data);                 
                    })
                }
                if(this.addOrUpdateState == 1){
                    this.axios.post('http://localhost:14000/cusManagement/cusManagement/customer/updateCustomers',
                    {
                        customer:this.addCustomer,
                        addContactsList:this.addContactsList,
                        updateContactsList:this.updateContactsList,
                        deleteContactsName:this.deleteContactsName,
                        addRelationList:this.addRelationList,
                        deleteRelationCusId:this.deleteRelationCusId,
                        },{
                            params:{access_token:this.emp_info.access_token}
                            })
                    .then((res)=>{
                        console.log(res.data);
                        this.addContactsList = [];
                        this.updateContactsList = [];
                        this.deleteContactsName = [];
                        this.addRelationList = [];
                        this.deleteRelationCusId = [];
                        this.notification(res.data)          
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

            addOrUpdateItem(itemRows,rowsItem,type){
                if(this.addOrUpdateItemState == 0){
                    if(type == 0){
                        //contact
                        this.addContactsList.push(rowsItem);
                    }
                    if(type == 1){
                        this.addRelationList.push(rowsItem);
                    }
                    this.addRow(itemRows,rowsItem);
                   
                    rowsItem = [];
                }
                if(this.addOrUpdateItemState == 1){
                    this.updateContactsList.push(rowsItem);
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