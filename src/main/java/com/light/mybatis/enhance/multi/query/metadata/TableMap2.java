package com.light.mybatis.enhance.multi.query.metadata;

public interface TableMap2<T1,T2,R> {
  Class<T1> getT1Type();
  Class<T2> getT2Type();
  Class<R> getRType();
}
