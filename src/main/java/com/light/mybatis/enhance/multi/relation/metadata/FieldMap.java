package com.light.mybatis.enhance.multi.relation.metadata;

import lombok.Getter;

@Getter
public class FieldMap<T1, T2, R> {

  private final boolean isT1Field;

  private final boolean isT2Field;

  private final String resultField;

  private final String t1Field;

  private final String t2Field;

  private final Property t1FieldProperty;
  private final Property t2FieldProperty;
  private final Property resultProperty;

  private final Class<T1> t1;
  private final Class<T2> t2;
  private final Class<R> resultClass;

  public FieldMap(boolean isT1Field, boolean isT2Field, String resultField, String t1Field,
      String t2Field, Property t1FieldProperty, Property t2FieldProperty,Property resultProperty, Class<T1> t1,
      Class<T2> t2,
      Class<R> resultClass) {
    this.isT1Field = isT1Field;
    this.isT2Field = isT2Field;
    this.resultField = resultField;
    this.t1Field = t1Field;
    this.t2Field = t2Field;
    this.t1FieldProperty = t1FieldProperty;
    this.t2FieldProperty = t2FieldProperty;
    this.resultProperty = resultProperty;
    this.t1 = t1;
    this.t2 = t2;
    this.resultClass = resultClass;
  }
}
