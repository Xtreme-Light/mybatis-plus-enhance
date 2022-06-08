package com.light.mybatis.enhance.multi.relation.metadata;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MapperKey mapperKey = (MapperKey) o;
    return Objects.equals(t1, mapperKey.t1) && Objects.equals(t2, mapperKey.t2)
        && Objects.equals(resultClass, mapperKey.resultClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(t1, t2, resultClass);
  }
}
