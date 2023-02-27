<template>
    <div class="goods-list">
        <el-card shadow="never">
            <template #header>
                <div class="clearfix">
                    <span style="font-size:20px">Worklog List</span>
                </div>
            </template>

            <div class="content">
                <div class="content-header">
                  
                    <el-row style="width:100%"> 
                       <el-button type="primary" @click="init()">refresh</el-button>                  
                       <el-button type="danger" @click="addLog = {};addOrUpdateState = 0;formVisible = true" style="float:right">New</el-button>
                    </el-row>  

                </div>
                <div class="content-main">
                    <el-table :data="tableData" border stripe style="width: 100%; margin: 1em 0 ;margin-top:20px"
                              v-loading="loading"
                              element-loading-text="Loading..."
                              element-loading-spinner="el-icon-loading"
                              element-loading-background="rgba(0, 0, 0, 0.8)">
                        <el-table-column prop="time" label="Time" align="center" show-overflow-tooltip/>
                        <el-table-column prop="content" label="Content" align="center" show-overflow-tooltip/>     
                        <el-table-column label="Operations" align="center" width="210">
                            <template #default="scope">
                                <!-- 这个按钮让表单变为编辑模式 -->
                                <el-button size="small" round @click="getCusEdit(scope.$index);formVisible = true">Edit</el-button>
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

    <!-- 添加/编辑用户联系人 -->
    <el-dialog v-model="formVisible">
        <el-form :model="addLog">
            <el-form-item label="Time" :label-width="formLabelWidth">
                <el-date-picker
                v-model="addLog.time"
                type="datetime"
                placeholder="选择日期时间"
                :shortcuts="shortcuts">
                </el-date-picker>
            </el-form-item>

            <el-form-item label="Content" :label-width="formLabelWidth">
                <el-input v-model="addLog.content"></el-input>
            </el-form-item>
        </el-form>
       
        <template #footer>
            <span class="dialog-footer">
            <el-button @click="formVisible = false">Cancel</el-button>
            <el-button type="primary" @click="finish();formVisible = false">Complete</el-button>
            </span>
        </template>
    </el-dialog>

</template>

<script>
    import { onMounted,reactive, toRefs } from 'vue'
    import {ElMessageBox, ElNotification} from 'element-plus'  

    export default {
        setup() {
            // const router = useRouter()
            const state = reactive({
                currentPage: 1,
                pageSizes: [10, 20, 50, 100],
                pageSize: 10,

                formLabelWidth: '180px',

                formVisible:false,  
                
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
                state.emp_info.empPositionId = window.sessionStorage.getItem('position');
                console.log(state.emp_info);

            }


            return {
                ...toRefs(state),
                getEmp,
            }
        },
        data(){
            return{
                tableData:[],
                total:0,
                loading: true,
                
                currentIndex:-1,
                addOrUpdateState:0,

                keyword:{
                    cusName:null,
                    cusId:null,
                    cusEnterpriseProperty:null,
                    cusSalesDeptId:null,
                    cusCustomerManagerId:null
                },

                addLog:{},
                shortcuts: [{
                    text: '今天',
                    value: new Date(),
                    }, {
                    text: '昨天',
                    value: () => {
                        const date = new Date();
                        date.setTime(date.getTime() - 3600 * 1000 * 24);
                        return date
                    },
                    }, {
                    text: '一周前',
                    value: () => {
                        const date = new Date();
                        date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                        return date
                    },
                    }],
            }
        },
        methods:{
            init(){
                this.axios.get('/npmsBackEndServer/report-edit/init/' + this.emp_info.empId)
                .then((res)=>{
                    console.log(res.data);
                    this.tableData = res.data.body.entries;
                    this.total = this.tableData.length;
                    this.loading = false;
                })
            },
            getCusEdit(index){
                this.addOrUpdateState = 1;
                this.addLog = this.tableData[index];
                
            },
         
            finish(){   
                console.log(this.addLog);         
                if(this.addOrUpdateState == 0){
                    this.axios.post('/npmsBackEndServer/report-edit/new',
                    {id: 1 ,
                    time: this.addLog.time,
                    content:this.addLog.content,
                    dept_id: 'cao',    
                    cus_mgr_id: this.emp_info.empId,})
                    .then((res)=>{
                        console.log(res.data);
                        this.notification('添加成功');
                        this.init();                 
                    })
                }
                if(this.addOrUpdateState == 1){
                    this.axios.post('/npmsBackEndServer/report-edit/update/',
                    {id: this.addLog.reportID ,
                    time: this.addLog.time,
                    content:this.addLog.content,
                    dept_id:'cao',    
                    cus_mgr_id: this.emp_info.empId,})
                    .then((res)=>{
                        console.log(res.data);
                        this.notification('修改成功'); 
                        this.init();                
                    })
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

<style>
</style>