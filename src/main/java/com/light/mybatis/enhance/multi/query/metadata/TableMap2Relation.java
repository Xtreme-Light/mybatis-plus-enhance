package com.light.mybatis.enhance.multi.query.metadata;

public class TableMap2Relation<T1, T2, R> implements TableMap2<T1, T2, R> {

  private final Class<T1> t1Class;
  private final Class<T2> t2Class;
  private final Class<R> rClass;

  @Override
  public Class<T1> getT1Type() {
    return t1Class;
  }

  @Override
  public Class<T2> getT2Type() {
    return t2Class;
  }

  @Override
  public Class<R> getRType() {
    return rClass;
  }

  public TableMap2Relation(Class<T1> t1Class, Class<T2> t2Class, Class<R> rClass) {
    this.t1Class = t1Class;
    this.t2Class = t2Class;
    this.rClass = rClass;
  }

}

