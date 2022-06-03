package com.light.mybatis.enhance.multi.query;

import com.light.mybatis.enhance.dev.entity.TenantInfoEntity;
import com.light.mybatis.enhance.dev.entity.UserInfoEntity;
import com.light.mybatis.enhance.dev.vo.UserTenantVO;
import com.light.mybatis.enhance.multi.query.mapper.Mapper2;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserTenantMapper2 extends Mapper2<TenantInfoEntity, UserInfoEntity, UserTenantVO> {

}
