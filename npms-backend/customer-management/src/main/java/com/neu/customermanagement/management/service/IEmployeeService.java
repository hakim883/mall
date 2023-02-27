package com.neu.customermanagement.management.service;

import com.neu.customermanagement.management.dto.common.Role;
import com.neu.customermanagement.management.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**

 *
 * @author xsb
 * @since 2021-07-08
 */
public interface IEmployeeService extends IService<Employee> {
    public List<Role> getRoleById(String emp_id);
    public String getEmpNameById(String emp_id);

}
