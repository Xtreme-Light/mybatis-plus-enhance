package com.light.mybatis.enhance.multi.relation.property;

import com.light.mybatis.enhance.multi.relation.metadata.Property;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 处理各自的java 类型关系
 */
public interface PropertyResolverStrategy {

  Map<String, Property> getProperties(Class<?> type);

  Property getProperty(Type type, String field);
}
