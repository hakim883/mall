package com.xu.service;

import com.xu.emp.entity.Employee;
import com.xu.emp.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl {

    @Autowired
    EmployeeMapper employeeMapper;

   public InMemoryUserDetailsManager getAllUsers(){
       InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
       BCryptPasswordEncoder b = new BCryptPasswordEncoder();
       // 从数据库中获取所有用户名，密码，roles
       List<Employee> employeeList = employeeMapper.getAllUser();
       for (Employee e : employeeList) {
           String empId = e.getEmpId();
           String pwd = e.getEmpPassword();
           String roles = e.getEmpPositionId();
           System.out.println(empId);
           userDetailsService.createUser(User.withUsername(empId).password(pwd).roles(roles).build());
       }
       return userDetailsService;
   }
}
