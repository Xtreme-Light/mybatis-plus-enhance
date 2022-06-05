package com.light.mybatis.enhance.multi.relation.metadata;

public class MapperKey {

  private final Class<Object> t1;
  private final Class<Object> t2;
  private final Class<Object> resultClass;

  @SuppressWarnings("unchecked")
  public MapperKey(Class<?> t1, Class<?> t2, Class<?> resultClass) {
    this.t1 = (Class<Object>)t1;
    this.t2 = (Class<Object>)t2;
    this.resultClass = (Class<Object>)resultClass;
  }

  public Class<Object> getT1() {
    return t1;
  }

  public Class<Object> getT2() {
    return t2;
  }

  public Class<Object> getResultClass() {
    return resultClass;
  }
}
