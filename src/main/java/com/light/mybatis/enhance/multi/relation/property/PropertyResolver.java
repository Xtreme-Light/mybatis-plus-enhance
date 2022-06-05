package com.light.mybatis.enhance.multi.relation.property;


import com.light.mybatis.enhance.multi.query.exception.MappingException;
import com.light.mybatis.enhance.multi.relation.metadata.Property;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PropertyResolver implements PropertyResolverStrategy {

  private final boolean includePublicFields;


  public PropertyResolver(boolean includePublicFields) {
    this.includePublicFields = includePublicFields;
  }

  private final Map<java.lang.reflect.Type, Map<String, Property>> propertiesCache = new ConcurrentHashMap<>();

  /**
   * 不考虑泛型
   *
   * @param theType
   * @return
   */
  public Map<String, Property> getProperties(Class<?> theType) {
    Map<String, Property> properties = propertiesCache.get(theType);
    if (properties == null) {
      //TODO 警告解决
      synchronized (theType) {
        properties = propertiesCache.get(theType);
        if (properties == null) {
          properties = new LinkedHashMap<>();
          LinkedList<Class<?>> types = new LinkedList<>();
          types.addFirst(theType);
          while (!types.isEmpty()) {
            final Class<?> type = types.removeFirst();
            collectProperties(type, properties);

            if (type.getSuperclass() != null && !Object.class.equals(type.getSuperclass())) {
              types.add(type.getSuperclass());
            }
            final List<Class<?>> interfaces = Arrays.asList(type.getInterfaces());
            types.addAll(interfaces);
          }
          propertiesCache.putIfAbsent(theType, Collections.unmodifiableMap(properties));
        }
      }
    }
    return properties;
  }


  protected abstract void collectProperties(Class<?> type, Map<String, Property> properties);


  /**
   * 获取指定类型的字段类型等描述信息
   * @param type 所在的字段类型
   * @param field 字段
   */
  @Override
  public Property getProperty(java.lang.reflect.Type type, String field){
    Property property = null;
    final Map<String, Property> properties = getProperties(type.getClass());
    if (properties != null) {
        property = properties.get(field);
    }
    return property;
  }

}
