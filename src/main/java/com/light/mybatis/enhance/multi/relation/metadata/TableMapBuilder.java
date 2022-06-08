package com.light.mybatis.enhance.multi.relation.metadata;

import com.light.mybatis.enhance.multi.query.constant.MultiConstants;
import com.light.mybatis.enhance.multi.query.exception.MappingException;
import com.light.mybatis.enhance.multi.relation.TableMapperFactory;
import com.light.mybatis.enhance.multi.relation.metadata.TableMap.Builder;
import com.light.mybatis.enhance.multi.relation.property.PropertyResolverStrategy;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 提供 fluent api ，用来定义表对象之间的连接关系
 */
@Getter
public class TableMapBuilder<T1, T2, R> implements MultiConstants {

  private static final Logger LOGGER = LoggerFactory.getLogger(TableMapBuilder.class);

  public void register() {
    if (this.tableMapperFactory == null) {
      throw new IllegalStateException(
          "register() is not supported from deprecated static TableMapBuilder.map(..) instances");
    }
    tableMapperFactory.registerTableMap(this);
  }

  private final Class<T1> t1;
  private final Class<T2> t2;

  private final String[] onBridge;
  private final String t1Alias;
  private final String t2Alias;
  private final Class<R> resultClass;
  private final TableMapperFactory tableMapperFactory;
  private final PropertyResolverStrategy propertyResolverStrategy;
  private final Map<String, Property> t1Properties;
  private final Map<String, Property> t2Properties;
  private final Map<String, Property> resultClassProperties;

  private final Set<FieldMap> fieldMaps;

  public TableMapBuilder(Class<T1> t1, Class<T2> t2, Class<R> resultClass, String t1Field,
      String t2Field, String t1Alias, String t2Alias, TableMapperFactory tableMapperFactory,
      PropertyResolverStrategy propertyResolverStrategy) {
    if (t1 == null) {
      throw new MappingException("[t1] is required");
    }

    if (t2 == null) {
      throw new MappingException("[t2] is required");
    }

    if (resultClass == null) {
      throw new MappingException("[rClass] is required");
    }
    this.t1 = t1;
    this.t2 = t2;
    this.t1Alias = t1Alias;
    this.t2Alias = t2Alias;
    this.resultClass = resultClass;
    this.tableMapperFactory = tableMapperFactory;
    this.propertyResolverStrategy = propertyResolverStrategy;
    this.t1Properties = propertyResolverStrategy.getProperties(t1);
    this.t2Properties = propertyResolverStrategy.getProperties(t2);
    this.resultClassProperties = propertyResolverStrategy.getProperties(resultClass);
    this.onBridge = new String[]{t1Field, t2Field};
    this.fieldMaps = new LinkedHashSet<>();
  }


  public TableMapBuilder<T1, T2, R> byDefault() {
    return byDefault(MappingDirection.UnderscoreToCamelCase);
  }

  /**
   * 默认场景下
   */
  @SuppressWarnings("unchecked")
  public TableMapBuilder<T1, T2, R> byDefault(MappingDirection direction) {
    return this;
  }


  protected Property resolveProperty(java.lang.reflect.Type type, String field) {
    return propertyResolverStrategy.getProperty(type, field);
  }

  /**
   * 注册入口
   */
  @SuppressWarnings("unchecked")
  public TableMap<T1, T2, R> toTableMap() {
    return (TableMap<T1, T2, R>) new Builder<>()
        .resultClass((Class<Object>) resultClass)
        .t1((Class<Object>) t1)
        .t2((Class<Object>) t2)
        .t1Alias(t1Alias)
        .t2Alias(t2Alias)
        .onBridge(onBridge)
        .build();
  }

  /**
   * 自定义转换关系
   */
  public TableMapBuilder<T1, T2, R> fieldMapT1To(String t1Field, String mapName) {
    if (!resultClassProperties.containsKey(mapName)) {
      throw new RuntimeException("返回类中不包含" + mapName + "字段");
    }
    fieldMaps.add(new FieldMap(t1Alias, t1Field, mapName));
    return this;
  }

  /**
   * 将t2的字段映射为指定字段
   *
   * @param t2Field t2 中的字段名称
   * @param mapName 要被映射成为的字段
   * @return 当前工厂
   */
  public TableMapBuilder<T1, T2, R> fieldMapT2To(String t2Field, String mapName) {
    if (!resultClassProperties.containsKey(mapName)) {
      throw new RuntimeException("返回类中不包含" + mapName + "字段");
    }
    fieldMaps.add(new FieldMap(t2Alias, t2Field, mapName));

    return this;
  }


  public TableMapBuilder<T1, T2, R> fieldMap(String field, String resultField) {
    if (!resultClassProperties.containsKey(resultField)) {
      throw new RuntimeException("返回类中不包含" + resultField + "字段");
    }
    fieldMaps.add(new FieldMap(t2Alias, field, resultField));

    return this;
  }
}
