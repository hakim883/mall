package com.neu.customermanagement.management.controller;


import com.neu.customermanagement.management.dto.handover.HandoverInfo;
import com.neu.customermanagement.management.dto.handover.HandoverInfoSearch;
import com.neu.customermanagement.management.dto.handover.HandoverPageInfo;
import com.neu.customermanagement.management.dto.opportunity.OppDetail;
import com.neu.customermanagement.management.entity.Customer;
import com.neu.customermanagement.management.entity.Opportunity;
import com.neu.customermanagement.management.service.IHandoverlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**

 *
 * @author xsb
 * @since 2021-07-12
 */
@RestController
@RequestMapping("/cusManagement/handoverlog")
public class HandoverlogController {

    @Autowired
    IHandoverlogService iHandoverlogService;

    @GetMapping("getHandoverPage")
    public HandoverPageInfo getHandoverPage(){
        return iHandoverlogService.getHandoverPage();
    }

    @PostMapping("searchOutCusInfo")
    public List<Customer> searchOutCusInfo(@RequestBody HandoverInfoSearch handoverInfoSearch){
        return iHandoverlogService.searchOutCusInfo(handoverInfoSearch);
    }

    @GetMapping("showOppDetail")
    public OppDetail showOppDetail(@RequestParam String oppId){
        return iHandoverlogService.showOppDetail(oppId);
    }

    @GetMapping("getOppByCusId")
    public List<Opportunity> getOppByCusId(@RequestParam String cusId){
        return iHandoverlogService.getOppByCusId(cusId);
    }
    @PostMapping("handover")
    public List<Customer> handover(@RequestBody HandoverInfo handoverInfo){
        return iHandoverlogService.handover(handoverInfo);
    }


}

