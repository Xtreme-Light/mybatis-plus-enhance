package com.light.mybatis.enhance.multi.relation;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.light.mybatis.enhance.multi.relation.metadata.MapperKey;
import com.light.mybatis.enhance.multi.relation.metadata.TableMap;
import java.util.concurrent.ConcurrentHashMap;

public interface TableMapperFacade {

  TableMap<Object, Object, Object> getTableMap(TableInfo t1TableInfo,TableInfo t2TableInfo,Class<?> resultClass);
}
