package com.light.mybatis.enhance.multi.relation;


/**
 * 提供通用的 字段映射关系
 */
public interface DefaultFieldMapper {
  /**
   * @param fromProperty
   * @return a suggested optional mapping name for the given property,
   * or <code>null</code> if no suggestion for the given property
   */
  String suggestMappedField(String fromProperty, Class<?> fromPropertyType);
}
