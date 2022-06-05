package com.light.mybatis.enhance.multi.relation;

import com.light.mybatis.enhance.multi.relation.metadata.TableMapBuilder;

/**
 * 核心工厂
 */
public interface TableMapperFactory {


  <T1, T2,R> TableMapBuilder<T1, T2,R> tableMap(Class<T1> t1, Class<T2> t2,
      Class<R> resultClas,
      String t1Field,
      String t2Field);

  <T1, T2,R> void registerTableMap(TableMapBuilder<T1, T2,R> builder);

  TableMapperFacade getTableMapperFacade();
}
