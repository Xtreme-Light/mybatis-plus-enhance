package com.light.mybatis.enhance.dev.config;

import com.light.mybatis.enhance.dev.entity.TenantInfoEntity;
import com.light.mybatis.enhance.dev.entity.UserInfoEntity;
import com.light.mybatis.enhance.dev.vo.UserTenantVO;
import com.light.mybatis.enhance.multi.relation.impl.DefaultTableMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CustomInit implements CommandLineRunner {

  @Autowired
  private DefaultTableMapperFactory mapperFactory;
  @Override
  public void run(String... args) throws Exception {
//    DefaultTableMapperFactory mapperFactory = new DefaultTableMapperFactory.Builder().build();
    mapperFactory.tableMap(TenantInfoEntity.class, UserInfoEntity.class,
            UserTenantVO.class,"id","tenantId")
        .fieldMapT1To("name","tenantName")
        .fieldMapT2To("name","userName")
        .fieldMapT2To("id","userId")
        .fieldMapT1To("id","tenantId")
        .fieldMapT1To("createTime","tenantCreateTime")
        .fieldMapT2To("createTime","userCreateTime")
        .byDefault().register();
  }
}
