package com.light.mybatis.enhance.multi.relation.metadata;


import lombok.Getter;

/**
 * 阐述字段之间的关系，T1或者T2的某个字段对应R的某个字段
 */
@Getter
public class FieldMapBuilder<T1, T2, R> {

  private final TableMapBuilder<T1, T2, R> tableMapBuilder;
  private final boolean isT1Field;
  private final boolean isT2Field;
  private final String resultField;
  private final String t1Field;
  private final String t2Field;
  private final Property t1FieldProperty;
  private final Property t2FieldProperty;
  private final Property resultProperty;

  public FieldMapBuilder(TableMapBuilder<T1, T2, R> tableMapBuilder, boolean isT1Field,
      boolean isT2Field, String resultField, String t1Field, String t2Field,
      Property t1FieldProperty, Property t2FieldProperty, Property resultProperty) {
    this.tableMapBuilder = tableMapBuilder;
    this.isT1Field = isT1Field;
    this.isT2Field = isT2Field;
    this.resultField = resultField;
    this.t1Field = t1Field;
    this.t2Field = t2Field;
    this.t1FieldProperty = t1FieldProperty;
    this.t2FieldProperty = t2FieldProperty;
    this.resultProperty = resultProperty;
  }

  public TableMapBuilder<T1, T2, R> add() {
    tableMapBuilder.addFieldMap(toFieldMap());
    return tableMapBuilder;
  }

  private FieldMap<T1, T2, R> toFieldMap() {
    return new FieldMap<>(isT1Field, isT2Field, resultField, t1Field, t2Field, t1FieldProperty,
        t2FieldProperty, resultProperty,
        tableMapBuilder.getT1(), tableMapBuilder.getT2(), tableMapBuilder.getResultClass());
  }
}
