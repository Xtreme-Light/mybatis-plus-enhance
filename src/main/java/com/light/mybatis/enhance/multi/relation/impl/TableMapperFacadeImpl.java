package com.light.mybatis.enhance.multi.relation.impl;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.light.mybatis.enhance.multi.relation.TableMapperFacade;
import com.light.mybatis.enhance.multi.relation.metadata.MapperKey;
import com.light.mybatis.enhance.multi.relation.metadata.TableMap;
import java.util.concurrent.ConcurrentHashMap;

public class TableMapperFacadeImpl implements TableMapperFacade {

  protected final ConcurrentHashMap<MapperKey, TableMap<Object, Object, Object>> tableMapRegistry;

  public TableMapperFacadeImpl(ConcurrentHashMap<MapperKey, TableMap<Object, Object, Object>> tableMapRegistry) {
    this.tableMapRegistry = tableMapRegistry;
  }

  @Override
  public  TableMap<Object, Object, Object> getTableMap(TableInfo t1TableInfo,TableInfo t2TableInfo,Class<?> resultClass) {
    final TableMap<Object, Object, Object> tableMap = tableMapRegistry.get(
        new MapperKey(t1TableInfo.getEntityType(), t2TableInfo.getEntityType(), resultClass));
    return tableMap;
  }
}
