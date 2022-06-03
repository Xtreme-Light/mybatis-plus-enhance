package com.light.mybatis.enhance.multi.query.metadata;

public class MapperTableKey<T1, T2, R> implements TableMap2<T1, T2, R> {

  private final Class<T1> t1Class;
  private final Class<T2> t2Class;
  private final Class<R> rClass;

  @Override
  public Class<T1> getT1Type() {
    return null;
  }

  @Override
  public Class<T2> getT2Type() {
    return null;
  }

  @Override
  public Class<R> getRType() {
    return null;
  }

  public MapperTableKey(Class<T1> t1Class, Class<T2> t2Class, Class<R> rClass) {
    this.t1Class = t1Class;
    this.t2Class = t2Class;
    this.rClass = rClass;
  }

  @Override
//  @SuppressWarnings("unchecked")
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final MapperTableKey<T1,T2,R> mapperKey = (MapperTableKey<T1,T2,R>) o;
    return t1Class == mapperKey.t1Class && t2Class == mapperKey.t2Class && rClass == mapperKey.rClass;
  }
}
