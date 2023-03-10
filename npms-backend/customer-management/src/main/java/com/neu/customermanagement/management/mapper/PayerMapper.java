package com.neu.customermanagement.management.mapper;

import com.neu.customermanagement.management.entity.Payer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 
 * </p>
 *
 * @author xsb
 * @since 2021-07-12
 */
@Mapper
@Repository
public interface PayerMapper extends BaseMapper<Payer> {

}
