package com.light.mybatis.enhance;

import com.light.mybatis.enhance.dev.entity.TenantInfoEntity;
import com.light.mybatis.enhance.dev.entity.UserInfoEntity;
import com.light.mybatis.enhance.dev.vo.UserTenantVO;
import com.light.mybatis.enhance.multi.relation.TableMapperFacade;
import com.light.mybatis.enhance.multi.relation.impl.DefaultTableMapperFactory;
import com.light.mybatis.enhance.multi.relation.metadata.Property;
import com.light.mybatis.enhance.multi.relation.property.IntrospectorPropertyResolver;
import java.util.HashMap;
import java.util.Map;
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
    DefaultTableMapperFactory mapperFactory = new DefaultTableMapperFactory.Builder().build();
    mapperFactory.tableMap(TenantInfoEntity.class, UserInfoEntity.class,
            UserTenantVO.class,"id","tenantId")
        .fieldMapT1To("name","tenantName")
        .fieldMapT2To("name","userName")
        .add()
        .byDefault().register();
    TableMapperFacade mapper = mapperFactory.getTableMapperFacade();
    mapper.getBridgeOn();
  }
}
