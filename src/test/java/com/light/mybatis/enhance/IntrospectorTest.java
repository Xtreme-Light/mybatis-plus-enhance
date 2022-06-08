package com.light.mybatis.enhance;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.light.mybatis.enhance.dev.entity.TenantInfoEntity;
import com.light.mybatis.enhance.dev.entity.UserInfoEntity;
import com.light.mybatis.enhance.dev.vo.UserTenantVO;
import com.light.mybatis.enhance.multi.relation.TableMapperFacade;
import com.light.mybatis.enhance.multi.relation.impl.DefaultTableMapperFactory;
import com.light.mybatis.enhance.multi.relation.metadata.MapperKey;
import com.light.mybatis.enhance.multi.relation.metadata.Property;
import com.light.mybatis.enhance.multi.relation.metadata.TableMap;
import com.light.mybatis.enhance.multi.relation.property.IntrospectorPropertyResolver;
import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.Test;

public class IntrospectorTest {

  @Test
  public void test() {
    final IntrospectorPropertyResolver introspectorPropertyResolver = new IntrospectorPropertyResolver(
        true);
    Map<String, Property> properties = new HashMap<>();
    introspectorPropertyResolver.collectProperties(TenantInfoEntity.class, properties);
    System.out.println(properties);
  }

  @Test
  public void register() {
    final TableInfo tableInfo = TableInfoHelper.initTableInfo(
        new MapperBuilderAssistant(new MybatisConfiguration(), ""), TenantInfoEntity.class);
    final TableInfo tableInfo1 = TableInfoHelper.initTableInfo(
        new MapperBuilderAssistant(new MybatisConfiguration(), ""), UserInfoEntity.class);

    DefaultTableMapperFactory mapperFactory = new DefaultTableMapperFactory.Builder().build();
    mapperFactory.tableMap(TenantInfoEntity.class, UserInfoEntity.class,
            UserTenantVO.class,"id","tenantId")
        .fieldMapT1To("name","tenantName")
        .fieldMapT2To("name","userName")
        .fieldMapT2To("id","userId")
        .fieldMapT1To("id","tenantId")
        .fieldMapT1To("createTime","tenantCreateTime")
        .fieldMapT2To("createTime","userCreateTime")
        .byDefault().register();
    TableMapperFacade mapper = mapperFactory.getTableMapperFacade();
    final TableMap<Object, Object, Object> tableMap = mapper.getTableMap(
        tableInfo,tableInfo1,UserTenantVO.class);
    System.out.println(tableMap);
  }
}
