package com.neu.customermanagement.management.controller;


import com.neu.customermanagement.management.dto.common.DeptInfo;
import com.neu.customermanagement.management.dto.common.EmpInfo;
import com.neu.customermanagement.management.dto.customer.*;
import com.neu.customermanagement.management.entity.Customer;
import com.neu.customermanagement.management.entity.Employee;
import com.neu.customermanagement.management.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**

 *
 * @author xsb
 * @since 2021-07-07
 */
@RestController
@RequestMapping("/cusManagement/customer")
public class CustomerController {

    @Autowired
    ICustomerService iCustomerService;


    @GetMapping("getMainPage")
    public CusManagePageInfo getMainPage(@RequestParam String empId){
        return iCustomerService.getCusManagePageInfo(empId);
    }

    @GetMapping("getEmpByDept")
    public List<EmpInfo> getEmpByDept(@RequestParam String deptId){
        return iCustomerService.getEmpByDept(deptId);
    }

    
    @GetMapping("getCusDetail")
    public CusDetail getCusDetail(@RequestParam String cusId){
        return iCustomerService.getCusDetail(cusId);
    }

    
    @PostMapping("getCustomers")
    public List<CusSearchResult> getCustomers(@RequestBody CusSearchCondition condition){
        return iCustomerService.getCustomers(condition);
    }

    @PostMapping("addCustomers")
    public String addCustomers(@RequestBody AddCustomerInfo addCustomerInfo){
        String msg="";
        try {
            msg = iCustomerService.addCustomers(addCustomerInfo);
        }
        catch (Exception exception){
            msg = "系统错误，请联系管理员...";
            System.err.println(exception);
        }
        return msg;
    }


    
    @PostMapping("updateCustomers")
    public String updateCustomers(@RequestBody UpdateCustomerInfo updateCustomerInfo){
        String msg="";
        try {
            msg = iCustomerService.updateCustomers(updateCustomerInfo);
        }
        catch (Exception exception){
            msg = "系统错误，请联系管理员...";
            System.err.println(exception);
        }
        return msg;
    }


    @PostMapping("frozenCustomer")
    public int frozenCustomer(@RequestBody Customer customer){
        String cus_id = customer.getCusId();
        String cus_status = customer.getCusStatus();
        return iCustomerService.frozenCustomer(cus_id, cus_status);
    }


    @GetMapping("getCustomerByName")
    public List<Customer> getCustomerByName(@RequestParam String cusName){
        return iCustomerService.getCustomerByName(cusName);
    }


    @GetMapping("export")
    public String export(){
        return "导出成功！";
    }


}

