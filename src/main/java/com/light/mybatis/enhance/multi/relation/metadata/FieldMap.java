package com.light.mybatis.enhance.multi.relation.metadata;

import lombok.Getter;

@Getter
public class FieldMap {

  private final String alias;
  private final String fieldName;
  private final String asName;


  public FieldMap(String alias, String fieldName, String asName) {
    this.alias = alias;
    this.fieldName = fieldName;
    this.asName = asName;
  }
}
