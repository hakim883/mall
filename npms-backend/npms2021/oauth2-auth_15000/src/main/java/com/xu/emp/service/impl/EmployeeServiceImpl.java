package com.xu.emp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.emp.entity.Employee;
import com.xu.emp.mapper.EmployeeMapper;
import com.xu.emp.service.IEmployeeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsb
 * @since 2021-07-21
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

}
