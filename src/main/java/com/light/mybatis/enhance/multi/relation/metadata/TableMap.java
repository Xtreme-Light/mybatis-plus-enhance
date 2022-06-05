package com.light.mybatis.enhance.multi.relation.metadata;

public class TableMap<T1, T2, R> {

  private final Class<T1> t1;
  private final Class<T2> t2;

  private final Class<R> resultClass;

  private final String t1Field;
  private final String t2Field;

  public TableMap(Class<T1> t1, Class<T2> t2, Class<R> resultClass,
      String t1Field,
      String t2Field) {
    this.t1 = t1;
    this.t2 = t2;
    this.t1Field = t1Field;
    this.t2Field = t2Field;
    this.resultClass = resultClass;
  }

  public Class<T1> getT1() {
    return t1;
  }

  public Class<T2> getT2() {
    return t2;
  }

  public Class<R> getResultClass() {
    return resultClass;
  }

  public String getT1Field() {
    return t1Field;
  }

  public String getT2Field() {
    return t2Field;
  }
}
