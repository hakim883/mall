package com.neu.opportunitymanagement.oppManagement.controller;


import com.neu.opportunitymanagement.oppManagement.dto.approval.Approval;
import com.neu.opportunitymanagement.oppManagement.dto.common.*;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.*;
import com.neu.opportunitymanagement.oppManagement.dto.tracklog.UpdateTrackInfo;
import com.neu.opportunitymanagement.oppManagement.entity.Employee;
import com.neu.opportunitymanagement.oppManagement.entity.Opportunity;
import com.neu.opportunitymanagement.oppManagement.service.IOpportunityBufferService;
import com.neu.opportunitymanagement.oppManagement.service.IOpportunityService;
import com.neu.opportunitymanagement.oppManagement.service.ITrackinglogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**

 *
 * @author xsb
 * @since 2021-07-15
 */
@RestController
@RequestMapping("/oppManagement/opportunity")
public class OpportunityController {

    @Autowired
    IOpportunityService iOpportunityService;
    @Autowired
    IOpportunityBufferService iOpportunityBufferService;
    @Autowired
    ITrackinglogService iTrackinglogService;



    @GetMapping("getMainPage")
    public RespBean getMainPage(@RequestParam String empId) {
        OppManagePageInfo oppManagePageInfo = iOpportunityService.getMainPage(empId);
        RespBean respBean = RespBean.ok(200,"进入机会管理页面",oppManagePageInfo);
        return respBean;
    }


    @GetMapping("getEmpByDept")
    public RespBean getEmpByDept(@RequestParam String deptId){
        List<EmpInfo> empInfoList = iOpportunityService.getEmpByDept(deptId);
        RespBean respBean = RespBean.ok(200,"ok",empInfoList);
        return respBean;
    }


    @GetMapping("getProductByType")
    public RespBean getProductByType(@RequestParam String psoId){
        List<ProductInfo> productInfoList = iOpportunityService.getProductByType(psoId);
        RespBean respBean = RespBean.ok(200,"ok",productInfoList);
        return respBean;
    }


    @GetMapping("getTypeByProduct")
    public RespBean getTypeByProduct(@RequestParam String cproId){
        List<OppTypeInfo> oppTypeInfoList = iOpportunityService.getTypeByProduct(cproId);
        RespBean respBean = RespBean.ok(200,"ok",oppTypeInfoList);
        return respBean;
    }


    @GetMapping("showOppDetail")
    public RespBean showOppDetail(@RequestParam String oppId, @RequestParam String empId){
        OppDetail oppDetail = iOpportunityService.showOppDetail(oppId, empId);
        RespBean respBean = RespBean.ok(200,"ok",oppDetail);
        return respBean;
    }


    @PostMapping("getOpportunity")
    public RespBean getOpportunity(@RequestBody OppSearchCondition oppSearchCondition){
        return iOpportunityService.getOpportunity(oppSearchCondition);
    }


    @GetMapping("testAddRepetition")
    public RespBean testAddRepetition(@RequestParam String oppbName, @RequestParam String cusId){
        return iOpportunityBufferService.testAddRepetition(oppbName, cusId);
    }


    @PostMapping("addOpportunity")
    public RespBean addOpportunity(@RequestBody AddOpportunityInfo addOpportunityInfo){
        RespBean respBean = null;
        try {
            respBean = iOpportunityBufferService.addOpportunity(addOpportunityInfo);
        }catch (Exception e){
            respBean = RespBean.error(500, "系统错误，请联系管理员...");
        }
        return respBean;
    }


    @PostMapping("showUpdatePage")
    public RespBean showUpdatePage(@RequestBody OppIdAndOppBId oppIdAndOppBId){
        return iOpportunityService.showUpdatePage(oppIdAndOppBId);
    }


    @GetMapping("testUpdateRepetition")
    public RespBean testUpdateRepetition(@RequestBody String oppbName, @RequestBody String cusId, @RequestBody String oppbId){
        return iOpportunityBufferService.testUpdateRepetition(oppbName,cusId,oppbId);
    }


    @PostMapping("updateOpportunity")
    public RespBean updateOpportunity(@RequestBody UpdateOpportunityInfo updateOpportunityInfo){
        RespBean respBean = null;
        try {
            respBean = iOpportunityBufferService.updateOpportunity(updateOpportunityInfo);
        }catch (Exception e){
            respBean = RespBean.error(500, "系统错误，请联系管理员...");
        }
        return respBean;
    }


    @GetMapping("getOppTrackMainPage")
    public RespBean getOppTrackMainPage(@RequestParam String oppId){
        return iOpportunityService.getOppTrackMainPage(oppId);
    }


    
    @PostMapping("curdTrackinglog")
    public RespBean curdTrackinglog(@RequestBody UpdateTrackInfo updateTrackInfo){
        RespBean respBean = null;
        try {
            respBean = iTrackinglogService.curdTrackinglog(updateTrackInfo);
        }catch (Exception e){
            respBean = RespBean.error(500, "系统错误，请联系管理员...");
        }
        return respBean;
    }


    @GetMapping("getApprovalPage")
    public RespBean getApprovalPage(@RequestParam String empId){
        return iOpportunityService.getApprovalPage(empId);
    }


    @GetMapping("showOppApproveDetail")
    public RespBean showOppApproveDetail(@RequestParam String oppIdB){
        return iOpportunityService.showOppApproveDetail(oppIdB);
    }


    @PostMapping("approval")
    public RespBean approval(@RequestBody Approval approval){
        RespBean respBean = null;
        try {
            respBean = iOpportunityService.approval(approval);
        }catch (Exception e){
            respBean = RespBean.error(500, "系统错误，请联系管理员...");
            System.err.println(e);
        }
        return respBean;
    }






}

