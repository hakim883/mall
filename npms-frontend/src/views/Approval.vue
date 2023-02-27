<template>
  <div class="approval">
    <el-card shadow="never">
      <template #header>
        <div class="clearfix">
          <el-image
            class="icon"
            :src="require('@/assets/image/icon/list.png')"
          />
          <span style="font-size: 20px">机会审批</span>
        </div>
      </template>
      <div class="content">
        <div class="content-header">
          <el-row style="width: 100%">
            <el-col :span="8" style="float: right; text-align: right">
              <el-button type="primary" @click="getApprovalPage()"
                >刷 新</el-button
              >
            </el-col>
          </el-row>
        </div>

        <div class="content-main">
          <el-table
            :data="tableData"
            border
            stripe
            style="width: 100%; margin: 1em 0; margin-top: 20px"
            v-loading="loading"
            element-loading-text="Loading..."
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
          >
            <el-table-column
              prop="flowName"
              label="流程名称"
              align="center"
              width="180"
              show-overflow-tooltip
            />
            <el-table-column
              prop="oppId"
              label="机会编号"
              align="center"
              show-overflow-tooltip
            />
            <el-table-column
              prop="oppName"
              label="机会名称"
              align="center"
              width="300"
              show-overflow-tooltip
            />
            <el-table-column
              prop="oppPhase"
              label="机会阶段"
              align="center"
              show-overflow-tooltip
            />
            <el-table-column
              prop="oppSubmiter"
              label="提交人"
              align="center"
              show-overflow-tooltip
            />
            <el-table-column
              prop="submitDate"
              label="提交日期"
              align="center"
              width="220"
              show-overflow-tooltip
            />

            <el-table-column label="操作" align="center" width="200">
              <template #default="scope">
                <el-button
                  size="small"
                  round
                  @click="
                    getApproveDetail(scope.$index);
                    ApproveDetailForm = true;
                    oppIdB = tableData[scope.$index].oppIdB;
                    changeActive(scope.$index);
                  "
                  >进入审批页面</el-button
                >
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            @size-change="sizeChange"
            @current-change="currentChange"
            layout="total, sizes, prev, pager, next, jumper"
            :current-page="currentPage"
            :page-sizes="pageSizes"
            :page-size="pageSize"
            :total="total"
          />
        </div>
      </div>
    </el-card>
  </div>

  <!-- 机会审批详情 -->
  <el-dialog v-model="ApproveDetailForm" :with-header="false" width="60%">
    <el-header style="font-size: 18px"
      >机会阶段说明
      <el-col :span="4" style="float: right; text-align: right">
        <el-button
          fixed="right"
          size="medium"
          type="primary"
          round
          @click="
            flowChartForm = true;
            getApproveStatus();
          "
          >流程图</el-button
        >
      </el-col>
    </el-header>
    <el-dialog v-model="flowChartForm">
      <el-steps
        :space="200"
        :active="approvePhaseId"
        finish-status="success"
        align-center
      >
        <el-step title="提交"></el-step>
        <el-step title="营销副总审批"></el-step>
        <el-step title="销售总监审批"></el-step>
      </el-steps>
    </el-dialog>

    <el-steps :active="oppPhaseId" align-center>
      <el-step title="E" description="仅有意向，选型还未开始"></el-step>
      <el-step title="D" description="交流展示中"></el-step>
      <el-step title="C" description="交流展结束、等待投标"></el-step>
      <el-step title="B" description="投标中"></el-step>
      <el-step title="A" description="中标、商务、合同中"></el-step>
      <el-step title="S" description="合同已签订"></el-step>
    </el-steps>

    <el-descriptions
      title="机会基本信息"
      :column="2"
      border
      style="margin: 1em"
    >
      <el-descriptions-item
        label="机会编号"
        label-align="right"
        align="center"
        >{{ ApproveDetail.oppId }}</el-descriptions-item
      >
      <el-descriptions-item
        label="机会名称"
        label-align="right"
        align="center"
        >{{ ApproveDetail.oppbName }}</el-descriptions-item
      >
      <el-descriptions-item
        label="销售部门"
        label-align="right"
        align="center"
        >{{ ApproveDetail.oppbSalesDept }}</el-descriptions-item
      >
      <el-descriptions-item
        label="客户经理"
        label-align="right"
        align="center"
        >{{ ApproveDetail.oppbCustomerManagerId }}</el-descriptions-item
      >
      <el-descriptions-item
        label="客户名称"
        label-align="right"
        align="center"
        >{{ ApproveDetail.oppbCusId }}</el-descriptions-item
      >

      <el-descriptions-item
        label="机会归属"
        label-align="right"
        align="center"
        >{{ ApproveDetail.oppbBelonging }}</el-descriptions-item
      >
      <el-descriptions-item
        label="机会来源"
        label-align="right"
        align="center"
        >{{ ApproveDetail.oppbOrigin }}</el-descriptions-item
      >
      <el-descriptions-item
        label="预签时间"
        label-align="right"
        align="center"
        >{{ ApproveDetail.oppbSignTime }}</el-descriptions-item
      >
      <el-descriptions-item
        label="机会阶段"
        label-align="right"
        align="center"
        >{{ ApproveDetail.oppPhase }}</el-descriptions-item
      >
      <el-descriptions-item
        label="烟草/电力老客户"
        label-align="right"
        align="center"
        >{{ ApproveDetail.oppbCigarettes }}</el-descriptions-item
      >
      <el-descriptions-item
        label="机会背景说明"
        label-align="right"
        align="center"
        >{{ ApproveDetail.oppbBackground }}</el-descriptions-item
      >
    </el-descriptions>

    <dir style="padding: 1em">
      <div class="el-descriptions__title">类型列表</div>
      <br />
      <el-table
        :data="subOpportunityBufferList"
        style="width: 100%"
        border
        max-height="163"
      >
        <el-table-column
          prop="subOppbId"
          label="子机会编号"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="subOppbName"
          label="子机会名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="subOppbType"
          label="机会类型"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="subOppbProduct"
          label="产品"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="subOppbDept"
          label="事业部"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="subOppbCurrency"
          label="货币"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="subOppbMoney"
          label="金额"
          align="center"
        ></el-table-column>
      </el-table>
    </dir>

    <dir style="padding: 1em">
      <div class="el-descriptions__title">竞争情况</div>
      <br />
      <el-table
        :data="competitorBufferList"
        style="width: 100%"
        border
        max-height="163"
      >
        <el-table-column
          prop="compbName"
          label="竞争对手姓名"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="compbPosition"
          label="竞争位势"
          align="center"
        ></el-table-column>
      </el-table>
    </dir>

    <dir style="padding: 1em">
      <div class="el-descriptions__title">购买决策人信息</div>
      <br />
      <el-table
        :data="payerBufferList"
        style="width: 100%"
        border
        max-height="163"
      >
        <el-table-column
          prop="pbName"
          label="姓名"
          align="center"
          width="120"
        ></el-table-column>
        <el-table-column
          prop="pbDept"
          label="部门"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="pbPosition"
          label="职位"
          align="center"
          width="110"
        ></el-table-column>
        <el-table-column
          prop="pbPhone"
          label="联系电话"
          align="center"
          width="140"
        ></el-table-column>
        <el-table-column
          prop="pbDecision"
          label="决策人身份"
          align="center"
          width="140"
        ></el-table-column>
        <el-table-column
          prop="pbEffect"
          label="影响程度"
          align="center"
          width="115"
        ></el-table-column>
        <el-table-column
          prop="pbAgree"
          label="认可程度"
          align="center"
          width="115"
        ></el-table-column>
      </el-table>
    </dir>

    <dir style="padding: 1em">
      <div class="el-descriptions__title">机会跟踪记录</div>
      <br />
      <el-table
        :data="trackinglogList"
        style="width: 100%"
        border
        max-height="163"
      >
        <el-table-column
          prop="tType"
          label="业务类别"
          width="80"
          align="center"
        >
        </el-table-column>

        <el-table-column
          prop="tDate"
          label="访谈日期"
          width="120"
          align="center"
        >
        </el-table-column>

        <el-table-column prop="tOurParty" label="我方人员" align="center">
        </el-table-column>

        <el-table-column prop="tCusParty" label="客户方人员" align="center">
        </el-table-column>

        <el-table-column prop="tThirdParty" label="第三方人员" align="center">
        </el-table-column>

        <el-table-column
          prop="tContactWay"
          label="沟通方式"
          width="100"
          align="center"
        >
        </el-table-column>

        <el-table-column prop="tPlace" label="地点" width="115" align="center">
        </el-table-column>
        <el-table-column
          prop="tContactCondition"
          label="沟通情况"
          width="80"
          align="center"
        >
        </el-table-column>

        <el-table-column
          prop="tContactEffect"
          label="沟通效果"
          width="80"
          align="center"
        >
        </el-table-column>
      </el-table>
    </dir>

    <dir style="padding: 1em">
      <div class="el-descriptions__title">机会审批日志</div>
      <br />
      <el-table
        :data="approvallogList"
        style="width: 100%"
        border
        max-height="163"
      >
        <el-table-column prop="flowName" label="流程名称" align="center">
        </el-table-column>

        <el-table-column prop="approver" label="审批人" align="center">
        </el-table-column>

        <el-table-column
          prop="approveStatus"
          label="审批状态"
          width="120"
          align="center"
        >
        </el-table-column>

        <el-table-column
          prop="approveOpinion"
          label="审批意见"
          width="120"
          align="center"
        >
        </el-table-column>

        <el-table-column prop="approveTime" label="申请时间" align="center">
        </el-table-column>
      </el-table>
    </dir>
    <br />
    <el-select v-model="approveResult" placeholder="审批结果">
      <el-option label="同意" value="1"></el-option>
      <el-option label="拒绝" value="-1"></el-option>
      <el-option label="退回" value="0"></el-option>
    </el-select>
    <el-input
      v-model="approveAdvice"
      placeholder="请填写审批意见"
      style="width: 400px"
    ></el-input>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="ApproveDetailForm = false" fixed="right"
          >取 消</el-button
        >
        <el-button type="primary" @click="submitApproval()" fixed="right"
          >确 定</el-button
        >
      </span>
    </template>
  </el-dialog>
</template>



<script>
import { reactive, toRefs ,onMounted } from "vue";
import { ElNotification } from "element-plus";
export default {
  setup() {
    const state = reactive({
      emp_info: {
        empId:'',
        access_token:'',
        empPositionId: '',
      },
    });
    onMounted(() => {
        init()
    })
    const init = () => {
        state.emp_info.empId = window.sessionStorage.getItem('username');
        state.emp_info.access_token = window.sessionStorage.getItem('token');
        state.emp_info.empPositionId = window.sessionStorage.getItem('position').substring(5);
        console.log(state.emp_info);
    }

    return {
      ...toRefs(state),
      init
    };
  },

  data() {
    return {
      tableData: [],
      ApproveDetailForm: false,
      flowChartForm: false,
      oppIdB: "",
      ApproveDetail: [],
      subOpportunityBufferList: [],
      competitorBufferList: [],
      payerBufferList: [],
      trackinglogList: [],
      approvallogList: [],
      customer: [],

      approvePhaseId: 0,

      oppPhase: "",
      oppPhaseId: "",

      approveResult: "",
      approveAdvice: "",

      loading: true,
    };
  },

  methods: {
    getApproveStatus() {
      this.axios
        .get(
          "http://localhost:14000/oppManagement/oppManagement/opportunity/showOppApproveDetail",
          {
            params: { oppIdB: this.oppIdB ,access_token:this.emp_info.access_token},
          }
        )
        .then((res) => {
          console.log(res.data);
          this.flowPhaseId =
            parseInt(res.data.obj.opportunityBuffer.oppbApproveStatus) + 1;
          console.log('flowPhaseId'+this.flowPhaseId);
        });
    },

    getApprovalPage() {
      this.axios
        .get(
          "http://localhost:14000/oppManagement/oppManagement/opportunity/getApprovalPage",
          {
            params: { empId: this.emp_info.empId ,access_token:this.emp_info.access_token},
          }
        )
        .then((res) => {
          console.log(res.data);
          this.tableData = res.data.obj;

          this.loading = false;
        });
    },

    getApproveDetail(index) {
      var target = this.tableData[index];
      console.log(target);
      this.axios
        .get(
          "http://localhost:14000/oppManagement/oppManagement/opportunity/showOppApproveDetail",
          {
            params: { oppIdB: target.oppIdB ,access_token:this.emp_info.access_token},
          }
        )
        .then((res) => {
          console.log(res.data);
          this.ApproveDetail = res.data.obj.opportunityBuffer;
          this.approvePhaseId =
            parseInt(this.ApproveDetail.oppbApproveStatus) + 1;

          this.subOpportunityBufferList = res.data.obj.subOpportunityBufferList;
          this.competitorBufferList = res.data.obj.competitorBufferList;
          this.payerBufferList = res.data.obj.payerBufferList;
          this.trackinglogList = res.data.obj.trackinglogList;
          this.approvallogList = res.data.obj.approvallogList;

          this.customer = res.data.obj.customer;
        });
    },

    changeActive(index) {
      var target = this.tableData[index];
      this.ApproveDetail.oppPhase = target.oppPhase;
      console.log(this.oppPhase);
      if (this.ApproveDetail.oppPhase == "E") {
        this.oppPhaseId = 1;
      } else if (this.ApproveDetail.oppPhase == "D") {
        this.oppPhaseId = 2;
      } else if (this.ApproveDetail.oppPhase == "C") {
        this.oppPhaseId = 3;
      } else if (this.ApproveDetail.oppPhase == "B") {
        this.oppPhaseId = 4;
      } else if (this.ApproveDetail.oppPhase == "A") {
        this.oppPhaseId = 5;
      } else if (this.ApproveDetail.oppPhase == "S") {
        this.oppPhaseId = 6;
      } else {
        this.oppPhaseId = 0;
      }
      console.log(this.oppPhaseId);
    },

    submitApproval() {
            console.log('所有用到的:')
            console.log(this.oppIdB)
            console.log(this.approveResult)
            console.log(this.approveAdvice)
            console.log(this.emp_info.access_token)
            console.log(this.emp_info.empPositionId)

            this.axios.post(
              "http://localhost:14000/oppManagement/oppManagement/opportunity/approval",
              {
                empName: "kim",
                empPositionId: this.emp_info.empPositionId,
                oppIdB: this.oppIdB,
                approveResult: '' + this.approveResult,
                approveAdvice: this.approveAdvice,
              },{params:{access_token:this.emp_info.access_token}}
            ).then((res) => {
              console.log(res.data);
            });

            this.ApproveDetailForm = false;

            ElNotification({
              title: "系统提示",
              message: "审批成功！",
              duration: 2000,
              type: "success",
            });
    },
  },
};
</script>
<style lang="less">
@import "../common/style/common";
.approval {
  .content-main {
    .table-image {
      width: 45px;
      height: 45px;
    }
  }
}
</style>