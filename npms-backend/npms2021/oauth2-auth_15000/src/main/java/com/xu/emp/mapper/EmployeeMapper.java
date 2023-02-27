package com.xu.emp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.emp.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xsb
 * @since 2021-07-21
 */
@Mapper
@Repository
public interface EmployeeMapper extends BaseMapper<Employee> {

    public List<Employee> getAllUser();
}
