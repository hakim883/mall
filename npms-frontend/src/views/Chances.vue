<template>
  <div class="chances">
    <el-card shadow="never">
      <template #header>
        <div class="clearfix">
          <el-image
            class="icon"
            :src="require('@/assets/image/icon/list.png')"
          />
          <span style="font-size: 20px">机会维护</span>
        </div>
      </template>
      <div class="content">
        <div class="content-header">
          <el-row style="width: 100%">
            <el-select v-model="search" placeholder="请选择要搜索的分类">
              <el-option label="机会编号" value="01"></el-option>
              <el-option label="机会名称" value="02"></el-option>
              <el-option label="机会来源" value="03"></el-option>
              <el-option label="机会阶段" value="04"></el-option>
              <el-option label="销售部门" value="05"></el-option>
              <el-option label="客户经理" value="06"></el-option>
            </el-select>

            <el-col :span="4">
              <el-input
                v-model="keyword"
                placeholder="请输入关键字进行查询"
              ></el-input>
            </el-col>

            <el-col :span="4" style="margin-left: 20px">
              <el-button type="primary" @click="init">查 询</el-button>
            </el-col>

            <el-col :span="4" style="float: right; text-align: right">
              <el-button
                type="primary"
                @click="
                  addChance = {};
                  chanceData = [];
                  newSonChance = {};
                  competeData = [];
                  newCompetitor = {};
                  payerData = [];
                  newDecider = {};
                  addOrUpdateState = 0;
                  addOrUpdate();
                  addChanceForm = true;
                "
                >新 增</el-button
              >
            </el-col>
            <el-col :span="4" style="float: right; text-align: right">
              <el-button type="primary" @click="getMainPage()">刷 新</el-button>
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
            element-loading-background="rgba(0, 0, 0, 0.8)">
            <!-- 可加sortable? -->
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
              width="200"
              show-overflow-tooltip
            />
            <el-table-column
              prop="oppOrigin"
              label="机会来源"
              align="center"
              show-overflow-tooltip
            />
            <el-table-column
              prop="oppPhase"
              label="机会阶段"
              align="center"
              show-overflow-tooltip
            />
            <el-table-column
              prop="oppSignTime"
              label="预签时间"
              align="center"
              width="180"
              show-overflow-tooltip
            />
            <el-table-column
              prop="oppSalesDeptName"
              label="销售部门"
              align="center"
              show-overflow-tooltip
            />
            <el-table-column
              prop="oppCustomerManagerName"
              label="客户经理"
              align="center"
              show-overflow-tooltip
            />

            <el-table-column label="操作" align="center" width="300">
              <template #default="scope">
                <el-button
                  size="small"
                  round
                  @click="
                    getChanceDetail(scope.$index);
                    changeActive(scope.$index);
                    detailDataForm = true;
                  "
                  >详情</el-button
                >
                <el-button
                  size="small"
                  round
                  @click="
                    addOrUpdateState = 1;
                    addOrUpdate();
                    addChanceForm = true;
                    getChanceEdit(scope.$index);
                  "
                  >编辑</el-button
                >
                <el-button
                  size="small"
                  round
                  @click="
                    trackChanceForm = true;
                    getChanceTrack(scope.$index);
                  "
                  >机会跟踪</el-button
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

  <el-dialog
    :title="addOrUpdateList.ChanceTitle"
    v-model="addChanceForm"
    width="60%"
  >
    <el-container>
      <el-header style="text-align: left; font-size: 16px">
        <offset>机会阶段说明</offset>
      </el-header>
      <el-main>
        <el-steps :active="oppPhaseId" align-center>
          <el-step title="E" description="仅有意向，选型还未开始"></el-step>
          <el-step title="D" description="交流展示中"></el-step>
          <el-step title="C" description="交流展结束、等待投标"></el-step>
          <el-step title="B" description="投标中"></el-step>
          <el-step title="A" description="中标、商务、合同中"></el-step>
          <el-step title="S" description="合同已签订"></el-step>
        </el-steps>
      </el-main>
    </el-container>

    <br />

    <el-container>
      <el-header style="text-align: left; font-size: 16px">
        <offset>机会基本信息</offset>
      </el-header>
      <el-main>
        <el-form :model="addChance">
          <el-row>
            <el-form-item label="机会名称" :label-width="formLabelWidth">
              <el-input
                v-model="addChance.oppbName"
               
              ></el-input>
               <!-- @blur="testAddRepetition()" -->
            </el-form-item>

            <el-form-item label="客户名称" :label-width="formLabelWidth">
              <el-input v-model="addChance.oppbCusId"></el-input>
            </el-form-item>
          </el-row>

          <el-row>
            <el-form-item
              label="机会来源"
              :label-width="formLabelWidth"
              style="width: 402px"
            >
              <el-select
                v-model="addChance.oppbOrigin"
                placeholder="请选择机会来源"
              >
                <el-option label="公司" value="company"></el-option>
                <el-option label="个人" value="person"></el-option>
                <el-option label="工厂" value="factory"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item
              label="机会阶段"
              :label-width="formLabelWidth"
              style="width: 402px"
            >
              <el-select
                v-model="addChance.oppbPhase"
                placeholder="请选择机会阶段"
              >
                <el-option label="E" value="E"></el-option>
                <el-option label="D" value="D"></el-option>
                <el-option label="C" value="C"></el-option>
                <el-option label="B" value="B"></el-option>
                <el-option label="A" value="A"></el-option>
                <el-option label="S" value="S"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="" :label-width="formLabelWidth">
              <!-- `checked` 为 true 或 false -->
              <el-checkbox v-model="addChance.oppbCigarettes"
                >烟草/电力老客户</el-checkbox
              >
            </el-form-item>
          </el-row>

          <el-row>
            <el-form-item
              label="预签时间"
              :label-width="formLabelWidth"
              style="width: 402px"
            >
              <el-date-picker
                v-model="addChance.oppbSignTime"
                type="date"
                placeholder="选择日期"
              ></el-date-picker>
            </el-form-item>

            <el-form-item
              label="机会归属"
              :label-width="formLabelWidth"
              style="width: 402px"
            >
              <el-select
                v-model="addChance.oppbBelonging"
                placeholder="请选择机会归属"
              >
                <el-option label="产品事业一部" value="shenyang"></el-option>
                <el-option label="产品事业二部" value="beijing"></el-option>
                <el-option label="行业事业一部" value="beijing"></el-option>
                <el-option label="行业事业二部" value="beijing"></el-option>
                <el-option label="行业事业三部" value="beijing"></el-option>
              </el-select>
            </el-form-item>
          </el-row>

          <el-form-item
            label="机会背景说明"
            :label-width="formLabelWidth"
            style="width: 805px"
          >
            <el-input
              v-model="addChance.oppbBackground"
              type="textarea"
              rows="3"
            ></el-input>
          </el-form-item>
        </el-form>

        <br />

        <el-header style="text-align: left; font-size: 16px">
          <offset>子机会列表</offset>
          <el-col :span="4" style="float: right; text-align: right">
            <el-button type="primary" @click="addSonChanceForm = true"
              >新 增</el-button>
          </el-col>
        </el-header>
        <el-table :data="chanceData" style="width: 100%">
          <el-table-column
            prop="subOppbName"
            label="子机会名称"
            width="170"
            align="center"
          >
          </el-table-column>

          <el-table-column
            prop="subOppbType"
            label="机会类型"
            width="140"
            align="center"
          >
          </el-table-column>

          <el-table-column
            prop="subOppbProduct"
            label="产品"
            width="140"
            align="center"
          >
          </el-table-column>

          <el-table-column
            prop="subOppbDept"
            label="事业部"
            width="150"
            align="center"
          >
          </el-table-column>

          <el-table-column
            prop="subOppbCurrency"
            label="货币"
            width="80"
            align="center"
          >
          </el-table-column>

          <el-table-column
            prop="subOppbMoney"
            label="金额"
            width="140"
            align="center"
          >
          </el-table-column>
          <el-table-column
            fixed="right"
            label="操作"
            width="100"
            align="center"
          >
            <template #default="scope">
              <el-button
                @click.prevent="deleteRow(scope.$index, chanceData)"
                type="text"
                size="small"
                >移除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <br />
        <br />
        <el-header style="text-align: left; font-size: 16px">
          <offset>竞争情况</offset>
          <el-col :span="4" style="float: right; text-align: right">
            <el-button type="primary" @click="addCompetitorForm = true"
              >新 增</el-button
            >
          </el-col>
        </el-header>

        <el-dialog
          :title="addOrUpdateList.SonChanceTitle"
          v-model="addSonChanceForm"
          width="30%"
        >
          <el-form v-model="newSonChance">
            <el-row justify="center">
              <el-form-item label="子机会名称">
                <el-input
                  v-model="newSonChance.subOppbName"
                  placeholder="输入名称"
                  style="width: 200px"
                ></el-input>
              </el-form-item>
            </el-row>
            <el-row justify="center">
              <el-form-item label="机会类型">
                <el-select
                  v-model="newSonChance.subOppbType"
                  style="width: 215px"
                >
                  <el-option label="第三方软件产品实施" value="01"></el-option>
                  <el-option label="定制开发" value="02"></el-option>
                  <el-option label="第三方服务" value="05"></el-option>
                  <el-option label="第三方软件产品" value="06"></el-option>
                  <el-option
                    label="第三方软件产品维护费"
                    value="07"
                  ></el-option>
                  <el-option label="自有软件产品" value="08"></el-option>
                  <el-option label="自有软件产品维护费" value="09"></el-option>
                  <el-option label="硬件产品" value="12"></el-option>
                  <el-option label="非产品研发" value="14"></el-option>
                  <el-option label="产品研发" value="15"></el-option>
                </el-select>
              </el-form-item>
            </el-row>
            <el-row justify="center">
              <el-form-item label="产品">
                <el-select
                  v-model="newSonChance.subOppbProduct"
                  style="width: 245px"
                >
                  <el-option label="SAP" value="01"></el-option>
                  <el-option label="ORACLE" value="02"></el-option>
                  <el-option label="NSRM" value="03"></el-option>
                  <el-option label="NPMS" value="04"></el-option>
                  <el-option label="NFOL" value="05"></el-option>
                </el-select>
              </el-form-item>
            </el-row>
            <el-row justify="center">
              <el-form-item label="事业部">
                <el-select
                  v-model="newSonChance.subOppbDept"
                  style="width: 230px"
                >
                  <el-option label="行业事业一部" value="01"></el-option>
                  <el-option label="行业事业二部" value="02"></el-option>
                  <el-option label="行业事业三部" value="03"></el-option>
                  <el-option label="行业事业四部" value="04"></el-option>
                  <el-option label="行业事业五部" value="05"></el-option>
                  <el-option label="产品事业一部" value="06"></el-option>
                  <el-option label="产品事业二部" value="07"></el-option>
                </el-select>
              </el-form-item>
            </el-row>
            <el-row justify="center">
              <el-form-item label="货币">
                <el-select
                  v-model="newSonChance.subOppbCurrency"
                  style="width: 245px"
                >
                  <el-option label="美元" value="01"></el-option>
                  <el-option label="人民币" value="02"></el-option>
                </el-select>
              </el-form-item>
            </el-row>
            <el-row justify="center">
              <el-form-item label="金额">
                <el-input
                  v-model="newSonChance.subOppbMoney"
                  placeholder="输入金额"
                  style="width: 250px"
                ></el-input>
              </el-form-item>
            </el-row>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="addSonChanceForm = false" fixed="right"
                >取 消</el-button
              >
              <el-button
                type="primary"
                @click="
                  addSonChanceForm = false;
                  addItem(chanceData, newSonChance);
                "
                fixed="right"
                >确 定</el-button
              >
            </span>
          </template></el-dialog
        >

        <el-table :data="competeData" style="width: 100%">
          <el-table-column prop="compbName" label="竞争对手" align="center">
          </el-table-column>

          <el-table-column prop="compbPosition" label="竞争位势" align="center">
          </el-table-column>

          <el-table-column fixed="right" label="操作" width="90" align="center">
            <template #default="scope">
              <el-button
                @click.prevent="deleteRow(scope.$index, competeData)"
                type="text"
                size="small"
                >移除</el-button
              >
            </template>
          </el-table-column>
        </el-table>

        <el-dialog
          :title="addOrUpdateList.CompetitorTitle"
          v-model="addCompetitorForm"
        >
          <el-form :inline="true" v-model="newCompetitor">
            <el-form-item label="竞争对手">
              <el-input
                v-model="newCompetitor.compbName"
                placeholder="输入竞争对手"
              ></el-input>
            </el-form-item>

            <el-form-item label="竞争位势">
              <el-select v-model="newCompetitor.compbPosition">
                <el-option label="唯一选择" value="10"></el-option>
                <el-option label="领跑者" value="20"></el-option>
                <el-option label="旗鼓相当" value="30"></el-option>
                <el-option label="落后" value="40"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="addCompetitorForm = false" fixed="right"
                >取 消</el-button
              >
              <el-button
                type="primary"
                @click="
                  addCompetitorForm = false;
                  addItem(competeData, newCompetitor);
                "
                fixed="right"
                >确 定</el-button
              >
            </span>
          </template>
        </el-dialog>

        <br />
        <br />
        
        <el-header style="text-align: left; font-size: 16px">
          <offset>购买决策人信息</offset>
          <el-col :span="4" style="float: right; text-align: right">
            <el-button type="primary" @click="addDeciderForm = true"
              >新 增</el-button
            >
          </el-col>
        </el-header>
        <el-table :data="payerData" style="width: 100%">
          <el-table-column
            prop="pbName"
            label="姓名"
            width="110"
            align="center"
          >
          </el-table-column>

          <el-table-column
            prop="pbDept"
            label="部门"
            width="170"
            align="center"
          >
          </el-table-column>

          <el-table-column prop="pbPosition" label="职位" align="center">
          </el-table-column>

          <el-table-column
            prop="pbPhone"
            label="联系电话"
            width="120"
            align="center"
          >
          </el-table-column>

          <el-table-column
            prop="pbDecision"
            label="购买决策人"
            width="130"
            align="center"
          >
          </el-table-column>

          <el-table-column
            prop="pbEffect"
            label="影响程度"
            width="80"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="pbAgree"
            label="认可程度"
            width="80"
            align="center"
          >
          </el-table-column>

          <el-table-column fixed="right" label="操作" width="90" align="center">
            <template #default="scope">
              <el-button
                @click.prevent="deleteRow(scope.$index, payerData)"
                type="text"
                size="small"
                >移除</el-button
              >
            </template>
          </el-table-column>
        </el-table>

        <!--新增购买决策人信息-->
        <el-dialog
          :title="addOrUpdateList.DeciderTitle"
          v-model="addDeciderForm"
          width="30%"
        >
          <el-form :inline="true" v-model="newDecider">
            <el-row justify="center">
              <el-form-item label="姓名">
                <el-input
                  v-model="newDecider.pbName"
                  placeholder="输入决策人姓名"
                ></el-input>
              </el-form-item>
            </el-row>

            <el-row justify="center">
              <el-form-item label="部门">
                <el-input
                  v-model="newDecider.pbDept"
                  placeholder="输入决策人部门"
                ></el-input>
              </el-form-item>
            </el-row>

            <el-row justify="center">
              <el-form-item label="职位">
                <el-input
                  v-model="newDecider.pbPosition"
                  placeholder="输入决策人职位"
                ></el-input>
              </el-form-item>
            </el-row>

            <el-row justify="center">
              <el-form-item label="联系电话">
                <el-input
                  v-model="newDecider.pbPhone"
                  placeholder="输入决策人联系电话"
                ></el-input>
              </el-form-item>
            </el-row>

            <el-row justify="center">
              <el-form-item label="决策人身份">
                <el-select v-model="newDecider.pbDecision">
                  <el-option label="最终决策人" value="10"></el-option>
                  <el-option label="决策人" value="20"></el-option>
                  <el-option label="技术负责人" value="30"></el-option>
                  <el-option label="使用人" value="40"></el-option>
                </el-select>
              </el-form-item>
            </el-row>

            <el-row justify="center">
              <el-form-item label="影响程度">
                <el-select v-model="newDecider.pbEffect">
                  <el-option label="高" value="10"></el-option>
                  <el-option label="中" value="20"></el-option>
                  <el-option label="低" value="30"></el-option>
                </el-select>
              </el-form-item>
            </el-row>

            <el-row justify="center">
              <el-form-item label="认可程度">
                <el-select v-model="newDecider.pbAgree">
                  <el-option label="完全支持" value="10"></el-option>
                  <el-option label="支持" value="20"></el-option>
                  <el-option label="中立" value="30"></el-option>
                </el-select>
              </el-form-item>
            </el-row>
          </el-form>

          <template #footer>
            <span class="dialog-footer">
              <el-button @click="addDeciderForm = false" fixed="right"
                >取 消</el-button
              >
              <el-button
                type="primary"
                @click="
                  addDeciderForm = false;
                  addItem(payerData, newDecider);
                "
                fixed="right"
                >确 定</el-button
              >
            </span>
          </template>
        </el-dialog>
      </el-main>
    </el-container>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="addChanceForm = false" fixed="right"
          >取 消</el-button
        >
        <el-button
          type="primary"
          @click="
            addChanceForm = false;
            addType = 0;
            Finish1();
          "
          fixed="right"
          >保 存</el-button
        >
        <el-button
          type="danger"
          @click="
            addChanceForm = false;
            addType = 1;
            Finish1();
          "
          fixed="right"
          >确 定</el-button
        >
      </span>
    </template>
  </el-dialog>

  <el-drawer
    v-model="detailDataForm"
    :with-header="false"
    direction="rtl"
    size="60%"
    style="overflow-y: scroll"
  >
    <el-header></el-header>
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
        label="机会名称"
        label-align="right"
        align="center"
        >{{ detailData.oppName }}</el-descriptions-item
      >
      <el-descriptions-item
        label="客户名称"
        label-align="right"
        align="center"
        >{{ detailData.oppCusId }}</el-descriptions-item
      >
      <el-descriptions-item
        label="机会编号"
        label-align="right"
        align="center"
        >{{ detailData.oppId }}</el-descriptions-item
      >
      <el-descriptions-item
        label="机会阶段"
        label-align="right"
        align="center"
        >{{ detailData.oppPhase }}</el-descriptions-item
      >
      <el-descriptions-item
        label="机会归属"
        label-align="right"
        align="center"
        >{{ detailData.oppBelonging }}</el-descriptions-item
      >
      <el-descriptions-item
        label="预签时间"
        label-align="right"
        align="center"
        >{{ detailData.oppSignTime }}</el-descriptions-item
      >
      <el-descriptions-item
        label="机会背景说明"
        label-align="right"
        align="center"
        >{{ detailData.oppBackground }}</el-descriptions-item
      >
    </el-descriptions>

    <dir style="padding: 1em">
      <div class="el-descriptions__title">类型列表</div>
      <br />
      <el-table :data="categoryData" style="width: 100%" border>
        <el-table-column
          prop="subOppId"
          label="子机会编号"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="subOppName"
          label="子机会名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="subOppType"
          label="机会类型"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="subOppProduct"
          label="产品"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="subOppDept"
          label="事业部"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="subOppCurrency"
          label="货币"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="subOppMoney"
          label="金额"
          align="center"
        ></el-table-column>
      </el-table>
    </dir>

    <dir style="padding: 1em">
      <div class="el-descriptions__title">竞争情况</div>
      <br />
      <el-table :data="compData" style="width: 100%" border max-height="163">
        <el-table-column
          prop="compName"
          label="竞争对手姓名"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="compPosition"
          label="竞争位势"
          align="center"
        ></el-table-column>
      </el-table>
    </dir>

    <dir style="padding: 1em">
      <div class="el-descriptions__title">购买决策人信息</div>
      <br />
      <el-table :data="decideData" style="width: 100%" border max-height="163">
        <el-table-column
          prop="pName"
          label="姓名"
          align="center"
          width="100"
        ></el-table-column>
        <el-table-column
          prop="pDept"
          label="部门"
          align="center"
          width="100"
        ></el-table-column>
        <el-table-column
          prop="pPosition"
          label="职位"
          align="center"
          width="100"
        ></el-table-column>
        <el-table-column
          prop="pPhone"
          label="联系电话"
          align="center"
          width="150"
        ></el-table-column>
        <el-table-column
          prop="pDecision"
          label="决策人身份"
          align="center"
          width="150"
        ></el-table-column>
        <el-table-column
          prop="pEffect"
          label="影响程度"
          align="center"
          width="100"
        ></el-table-column>
        <el-table-column
          prop="pAgree"
          label="认可程度"
          align="center"
          width="120"
        ></el-table-column>
      </el-table>
    </dir>
  </el-drawer>

  <el-dialog title="机会跟踪" v-model="trackChanceForm" width="60%">
    <el-header style="text-align: left; font-size: 16px">
      <offset>机会跟踪记录</offset>
      <el-col :span="4" style="float: right; text-align: right"> </el-col>
    </el-header>
    <el-main>
      <el-form :inline="true" v-model="trackData">
        <el-form-item label="机会编号">
          <span>
            {{ chanceNumber }}
          </span>
        </el-form-item>
        <el-form-item label="机会名称">
          <span>
            {{ chanceName }}
          </span>
        </el-form-item>
      </el-form>
    </el-main>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="trackChanceForm = false" fixed="right"
          >取 消</el-button
        >
        <el-button
          type="danger"
          @click="
            trackChanceForm = false;
            Finish2();
          "
          fixed="right"
          >确 定</el-button
        >
      </span>
    </template>

    <el-header style="text-align: left; font-size: 16px">
      <offset>机会跟踪详情</offset>
      <el-col :span="4" style="float: right; text-align: right">
        <el-button
          type="primary"
          @click="
            oppTrackList = [];

            addOrUpdateState = 0;
            addOrUpdate();
            addTrackDataForm = true;
          "
          >新 增</el-button
        >
      </el-col>
    </el-header>
    <el-table :data="trackData" style="width: 100%">
      <el-table-column prop="tType" label="业务类别" width="80" align="center">
      </el-table-column>

      <el-table-column prop="tDate" label="访谈日期" width="130" align="center">
      </el-table-column>

      <el-table-column
        prop="tOurParty"
        label="我方人员"
        width="100"
        align="center"
      >
      </el-table-column>

      <el-table-column
        prop="tCusParty"
        label="客户方人员"
        width="100"
        align="center"
      >
      </el-table-column>

      <el-table-column
        prop="tThirdParty"
        label="第三方人员"
        width="110"
        align="center"
      >
      </el-table-column>

      <el-table-column
        prop="tContactWay"
        label="沟通方式"
        width="80"
        align="center"
      >
      </el-table-column>

      <el-table-column prop="tPlace" label="地点" width="115" align="center">
      </el-table-column>
      <el-table-column
        prop="tContactCondition"
        label="沟通情况"
        width="60"
        align="center"
      >
      </el-table-column>

      <el-table-column
        prop="tContactEffect"
        label="沟通效果"
        width="60"
        align="center"
      >
      </el-table-column>

      <el-table-column fixed="right" label="操作" width="70">
        <template #default="scope">
          <el-button
            @click.prevent="deleteRow(scope.$index, trackData)"
            type="text"
            size="small"
            >移除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      :title="addOrUpdateList.TrackDataTitle"
      v-model="addTrackDataForm"
      width="50%"
    >
      <el-main>
        <el-form :model="oppTrackList">
          <el-row>
            <el-form-item label="业务类别" style="width: 400px">
              <el-select v-model="oppTrackList.tType">
                <el-option label="第三方软件服务" value="01"></el-option>
                <el-option label="第三方软件产品" value="02"></el-option>
                <el-option label="自有软件产品" value="03"></el-option>
                <el-option label="自有软件产品服务" value="04"></el-option>
                <el-option label="系统集成" value="05"></el-option>
                <el-option label="非产品开发" value="06"></el-option>
                <el-option label="产品研发" value="07"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="沟通方式">
              <el-select v-model="oppTrackList.tContactWay">
                <el-option label="面对面" value="面对面"></el-option>
                <el-option label="电话" value="电话"></el-option>
              </el-select>
            </el-form-item>
          </el-row>

          <el-row>
            <el-form-item label="访谈日期" style="width: 400px">
              <el-date-picker
                v-model="oppTrackList.tDate"
                type="date"
                placeholder="选择日期"
              ></el-date-picker>
            </el-form-item>

            <el-form-item label="访谈地点">
              <el-input
                v-model="oppTrackList.tPlace"
                placeholder="输入访谈地点"
                style="width: 215px"
              ></el-input>
            </el-form-item>
          </el-row>

          <el-row>
            <el-form-item label="我方人员">
              <el-input
                v-model="oppTrackList.tOurParty"
                style="width: 615px"
              ></el-input>
            </el-form-item>
          </el-row>

          <el-row>
            <el-form-item label="客户方人员">
              <el-input
                v-model="oppTrackList.tCusParty"
                style="width: 600px"
              ></el-input>
            </el-form-item>
          </el-row>

          <el-row>
            <el-form-item label="第三方人员">
              <el-input
                v-model="oppTrackList.tThirdParty"
                style="width: 600px"
              ></el-input>
            </el-form-item>
          </el-row>

          <el-row>
            <el-form-item label="沟通内容">
              <el-input
                v-model="oppTrackList.tContactCondition"
                style="width: 615px"
              ></el-input>
            </el-form-item>
          </el-row>

          <el-row>
            <el-form-item label="沟通效果">
              <el-input
                v-model="oppTrackList.tContactEffect"
                style="width: 615px"
              ></el-input>
            </el-form-item>
          </el-row>
        </el-form>
      </el-main>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addTrackDataForm = false" fixed="right"
            >取 消</el-button
          >
          <el-button
            type="danger"
            @click="
              addTrackDataForm = false;
              addItem(trackData, oppTrackList);
            "
            fixed="right"
            >确 定</el-button
          >
        </span>
      </template>
    </el-dialog>
  </el-dialog>
</template>





<script>
import { defineComponent, reactive, toRefs ,onMounted } from "vue";
import { ElMessageBox, ElNotification } from "element-plus";

export default defineComponent({
  setup() {
    const state = reactive({
      keyword: "",
      types: [],
      typeVal: "",
      loading: true,
      tableData: [],
      currentPage: 1,
      pageSizes: [10, 20, 50, 100],
      pageSize: 10,
      total: "",
      rowNumber: 0,

      formLabelWidth: "200px",

      addChanceForm: false,
      addSonChanceForm: false,
      addCompetitorForm: false,
      addDeciderForm: false,
      addTrackDataForm: false,
      detailDataForm: false,
      trackChanceForm: false,
      detailData: [],

      addOrUpdateState: 0,
      addOrUpdateList: {},

      customer: [],
      opportunity: [],

      categoryData: [],
      compData: [],
      competeData: [],
      decideData: [],
      payerData: [],
      input1: "",
      input2: "",

      search: [],
      disabledDate(time) {
        return time.getTime() > Date.now();
      },
      value1: "",
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
        state.emp_position = window.sessionStorage.getItem('position');
        console.log(state.emp_info);
    }

    return {
      ...toRefs(state),
      init
    };
  
  
  
  },

  data() {
    return {
      opp_cigarettes: true,

      chanceName: "",
      chanceNumber: "",
      trackData: [],
      oppPhase: "",
      oppPhaseId: "",
      loading: true,

      activeNumber: 1,

      addType: "",
      addChance: {},
      chanceData: [],
      competeData: [],
      payerData: [],
      newSonChance: {},

      newCompetitor: {},

      newDecider: {},
      deptOptions: [],
      empOptions: [],
      oppDeptOptions: [],

      oppTrackList: {
        tId: "",
        oppId: "",
        tType: "",
        tContactWay: "",
        tDate: "",
        tOurParty: "",
        tCusParty: "",
        tThirdParty: "",
        tContactCondition: "",
        tContactEffect: "",
      },
    };
  },

  methods: {
    onSubmit() {
      console.log("submit!");
    },

    addRow(itemRows, rowsItem) {
      var length = itemRows.length;
      itemRows.splice(length, 0, rowsItem);
    },

    addItem(itemRows, rowsItem) {
      this.addRow(itemRows, rowsItem);
      rowsItem = [];
    },

    changeActive(index) {
      var target = this.tableData[index];
      this.detailData.oppPhase = target.oppPhase;
      if (this.detailData.oppPhase == "E") {
        this.oppPhaseId = 1;
      } else if (this.detailData.oppPhase == "D") {
        this.oppPhaseId = 2;
      } else if (this.detailData.oppPhase == "C") {
        this.oppPhaseId = 3;
      } else if (this.detailData.oppPhase == "B") {
        this.oppPhaseId = 4;
      } else if (this.detailData.oppPhase == "A") {
        this.oppPhaseId = 5;
      } else if (this.detailData.oppPhase == "S") {
        this.oppPhaseId = 6;
      } else {
        this.oppPhaseId = 0;
      }
    },

    getMainPage() {
      this.axios
        .get("http://localhost:14000/oppManagement/oppManagement/opportunity/getMainPage", {
          params: { empId: this.emp_info.empId,access_token:this.emp_info.access_token },
        })
        .then((res) => {
          console.log(res.data);
          this.tableData = res.data.obj.oppSearchResultList;

          this.deptOptions = res.data.obj.deptInfoList;
          this.empOptions = res.data.obj.empInfoList;
          this.oppDeptOptions = res.data.obj.oppBelongingList;

          this.loading = false;
        });
    },

    getChanceDetail(index) {
      var target = this.tableData[index];
      this.axios
        .get("http://localhost:14000/oppManagement/oppManagement/opportunity/showOppDetail", {
          params: {
            oppId: target.oppId,
            empId: this.emp_info.empId,
            access_token:this.emp_info.access_token,
          },
        })
        .then((res) => {
          console.log(res.data);
          this.detailData = res.data.obj.opportunity;
          this.categoryData = res.data.obj.subOpportunityList;
          this.compData = res.data.obj.competitorList;
          this.decideData = res.data.obj.payerList;
          this.trackData = res.data.obj.oppTrackList;
        });
    },

    testAddRepetition() {
      this.axios
        .get(
          "http://localhost:14000/oppManagement/oppManagement/opportunity/testAddRepetition",
          {
            params: {
              
              oppbName: this.addChance.oppbName,
              access_token:this.emp_info.access_token
            },
          }
        )
        .then((res) => {
          console.log(res.data.msg);
          if (res.data.msg != "ok") {
            this.$notify.error({
              message: "机会名已存在",
              showClose: true,
              type: "fail",
            });
          } else if (res.data.msg == "ok") {
            this.$notify(
              this.$message({
                message: "机会名可以使用",
                showClose: true,
                type: "success",
              })
            );
          }
        });
    },

    Finish1() {
      this.addChance.oppbCusId = "800001";
      this.addChance.oppbCustomerManagerId = "800123";
      this.addChance.oppbSalesDept = "8021140";
      if (this.addOrUpdateState == 0) {
        this.axios
          .post(
            "http://localhost:14000/oppManagement/oppManagement/opportunity/addOpportunity",
            {
              type: this.addType,
              opportunityBuffer: this.addChance,
              subOpportunityBufferList: this.chanceData,
              competitorBufferList: this.competeData,
              payerBufferList: this.payerData,},
            // {
            //   type: "1",
            //   opportunityBuffer: {
            //       oppbName:"addOpp1",
            //       oppbCusId:"800001"
            //   },
            //   subOpportunityBufferList: [],
            //   competitorBufferList: [],
            //   payerBufferList: []
            // },

              {params:{access_token:this.emp_info.access_token}}
          )
          .then(() => {
            ElNotification({
              title: "系统提示",
              message: "机会新增已提交，请等待审批通过！",
              duration: 2000,
              type: "success",
            });
          });
      }
      if (this.addOrUpdateState == 1) {
          this.axios
            .post(
              "http://localhost:14000/oppManagement/cusManagement/customer/updateOpportunity",
              {
                customer: this.addCustomer,
                contactsList: this.addContact,
                relationList: this.addRelated,
              },{params:{access_token:this.emp_info.access_token}}
            )
            .then(() => {
              ElNotification({
                title: "系统提示",
                message: "新增成功",
                duration: 2000,
                type: "success",
              });
            });
      }
    },

    Finish3() {
      this.addChance.oppbCusId = '800001';
      this.axios
        .post(
          "http://localhost:14000/oppManagement/oppManagement/opportunity/addOpportunity",
          {
            type: "0",
            opportunityBuffer: this.addChance,
            subOpportunityBufferList: this.chanceData,
            competitorBufferList: this.competeData,
            payerBufferList: this.prayerData,
          },{params:{access_token:this.emp_info.access_token}}
        )
        .then((res) => {
          console.log("res data" + res.data);
          ElNotification({
            title: "系统提示",
            message: "保存成功",
            duration: 2000,
            type: "success",
          });
        });
    },

    Finish2() {
      if (this.addOrUpdateState == 0) {
        this.axios
          .post(
            "http://localhost:14000/oppManagement/oppManagement/opportunity/curdTrackinglog",
            {
              oppTrackList: this.oppTrackList,
            },{params:{access_token:this.emp_info.access_token}}
          )
          .then(() => {
            ElNotification({
              title: "系统提示",
              message: "新增成功",
              duration: 2000,
              type: "success",
            });
          });
      }
      if (this.addOrUpdateState == 1) {
        this.axios
          .post(
            "http://localhost:14000/oppManagement/oppManagement/customer/curdTrackinglog",
            {
              oppTrackList: this.oppTrackList,
            },{params:{access_token:this.emp_info.access_token}}
          )
          .then(() => {
            ElNotification({
              title: "系统提示",
              message: "新增成功",
              duration: 2000,
              type: "success",
            });
          });
      }
    },

    getChanceTrack(index) {
      var target = this.tableData[index];
      this.axios
        .get(
          "http://localhost:14000/oppManagement/oppManagement/opportunity/getOppTrackMainPage",
          {
            params: { oppId: target.oppId,access_token:this.emp_info.access_token },
          }
        )
        .then((res) => {
          console.log(res.data);
          this.chanceNumber = res.data.obj.oppId;
          this.chanceName = res.data.obj.oppName;
          this.trackData = res.data.obj.oppTrackList;
        });
    },

    addOrUpdate() {
      if (this.addOrUpdateState == 0) {
        this.addOrUpdateList = {
          ChanceTitle: "新增机会",
          SonChanceTitle: "新增子机会",
          CompetitorTitle: "新增竞争对手",
          DeciderTitle: "新增购买决策人",
          TrackDataTitle: "新增机会跟踪",
        };
      }
      if (this.addOrUpdateState == 1) {
        this.addOrUpdateList = {
          ChanceTitle: "编辑机会",
          SonChanceTitle: "编辑子机会",
          CompetitorTitle: "编辑竞争对手",
          DeciderTitle: "编辑购买决策人",
          TrackDataTitle: "编辑机会跟踪",
        };
      }
    },

    deleteRow(index, rows) {
      ElMessageBox.confirm("此条数据会被永久删除，确认删除？", "提示", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
        callback: (action) => {
          if ("confirm" == action) {
            rows.splice(index, 1);
            // init()
            ElNotification({
              title: "系统提示",
              message: "删除成功",
              duration: 2000,
              type: "success",
            });
          }
        },
      });
    },

    notification(str) {
      ElNotification({
        title: "系统提示",
        message: str,
        duration: 2000,
        type: "success",
      });
    },
  },
});
</script>

<style lang="less">
@import "../common/style/common";

// .el-input__inner{
//     width: 200px;
// }
.chance {
  .content-main {
    .table-image {
      width: 45px;
      height: 45px;
    }
  }
}
.el-drawer__body {
  overflow-y: auto !important;
}
/* #eldrawer{
    overflow-y: auto
  } */
</style>