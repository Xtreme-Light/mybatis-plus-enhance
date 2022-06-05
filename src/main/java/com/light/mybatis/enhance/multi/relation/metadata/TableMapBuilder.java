package com.light.mybatis.enhance.multi.relation.metadata;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.light.mybatis.enhance.multi.query.exception.MappingException;
import com.light.mybatis.enhance.multi.query.metadata.TableInfoAlias;
import com.light.mybatis.enhance.multi.relation.DefaultFieldMapper;
import com.light.mybatis.enhance.multi.relation.TableMapperFactory;
import com.light.mybatis.enhance.multi.relation.property.PropertyResolverStrategy;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 提供 fluent api ，用来定义表对象之间的连接关系
 */
public class TableMapBuilder<T1, T2, R> {

  private static final Logger LOGGER = LoggerFactory.getLogger(TableMapBuilder.class);

  public void register() {
    if (this.tableMapperFactory == null) {
      throw new IllegalStateException(
          "register() is not supported from deprecated static TableMapBuilder.map(..) instances");
    }
    tableMapperFactory.registerTableMap(this);
  }

  public TableMap<T1, T2, R> toClassMap() {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("TableMap created:\n\t");
    }
    return new TableMap<>(t1, t2, resultClass, t1Field, t2Field);
  }

  public static class Factory extends TableMapBuilderFactory {

    @Override
    protected <T1, T2, R> TableMapBuilder<T1, T2, R> newClassMapBuilder(Class<T1> t1, Class<T2> t2,
        Class<R> rClass,
        String t1Field,
        String t2Field,
        TableMapperFactory tableMapperFactory, PropertyResolverStrategy propertyResolver,
        DefaultFieldMapper[] defaults) {
      return new TableMapBuilder<>(t1, t2, rClass, t1Field, t2Field, tableMapperFactory,
          propertyResolver, defaults);
    }
  }

  private final Class<T1> t1;
  private final Class<T2> t2;
  private final TableInfo t1TableInfo;
  private final TableInfo t2TableInfo;
  private final TableInfoAlias t1TableInfoAlias;
  private final TableInfoAlias t2TableInfoAlias;

  private final String t1Field;
  private final String t2Field;
  private final Class<R> resultClass;
  private final TableMapperFactory tableMapperFactory;
  private final PropertyResolverStrategy propertyResolverStrategy;
  private final DefaultFieldMapper[] defaults;
  private final Map<String, Property> t1Properties;
  private final Map<String, Property> t2Properties;
  private final Map<String, Property> resultClassProperties;
  private final Set<String> propertiesCacheT1;
  private final Set<String> propertiesCacheT2;

  private final Set<FieldMap> fieldsMapping;

  public TableMapBuilder(Class<T1> t1, Class<T2> t2, Class<R> resultClass, String t1Field,
      String t2Field, TableMapperFactory tableMapperFactory,
      PropertyResolverStrategy propertyResolverStrategy, DefaultFieldMapper[] defaults) {
    if (t1 == null) {
      throw new MappingException("[t1] is required");
    }

    if (t2 == null) {
      throw new MappingException("[t2] is required");
    }

    if (resultClass == null) {
      throw new MappingException("[rClass] is required");
    }
    this.t1Field = t1Field;
    this.t2Field = t2Field;
    this.t1 = t1;
    this.t2 = t2;
    this.t1TableInfo = TableInfoHelper.getTableInfo(t1);
    this.t2TableInfo = TableInfoHelper.getTableInfo(t2);
    this.t1TableInfoAlias = new TableInfoAlias("t1", t1TableInfo);
    this.t2TableInfoAlias = new TableInfoAlias("t2", t1TableInfo);
    this.resultClass = resultClass;
    this.tableMapperFactory = tableMapperFactory;
    this.propertyResolverStrategy = propertyResolverStrategy;
    this.defaults = defaults;
    this.t1Properties = propertyResolverStrategy.getProperties(t1);
    this.t2Properties = propertyResolverStrategy.getProperties(t2);
    this.resultClassProperties = propertyResolverStrategy.getProperties(resultClass);
    this.propertiesCacheT1 = new LinkedHashSet<>();
    this.propertiesCacheT2 = new LinkedHashSet<>();
    this.fieldsMapping = new LinkedHashSet<>();
  }


  public TableMapBuilder<T1, T2, R> byDefault(DefaultFieldMapper... withDefaults) {
    return byDefault(MappingDirection.UnderscoreToCamelCase, withDefaults);
  }

  /**
   * 默认场景下 根据提供的默认规则，分别从T1 T2 中，找到和R相同的field，如果R中的field的字段名同时对应到了T1 和T2 抛出异常
   *
   * @param direction    映射方式
   * @param withDefaults 零或多个需要走默认规则的字段
   * @return 当前TableMapBuilder实例对象
   */
  public TableMapBuilder<T1, T2, R> byDefault(MappingDirection direction,
      DefaultFieldMapper... withDefaults) {

    DefaultFieldMapper[] defaults;
    if (withDefaults.length == 0) {
      defaults = getDefaultFieldMappers();
    } else {
      defaults = withDefaults;
    }

    for (TableFieldInfo tableFieldInfo : t1TableInfo.getFieldList()) {
      final Field field = tableFieldInfo.getField();
      if (getPropertiesForRes)
    }

    for (final String propertyName : getPropertiesForT1()) {
      if (!getMappedPropertiesForT1().contains(propertyName)) {
        if (getPropertiesForT2().contains(propertyName)) {
          if (!getMappedPropertiesForT2().contains(propertyName)) {
            /*
             * Don't include the default mapping of Class to Class;
             * this property is resolved for all types, but can't be
             * mapped in either direction.
             */
            if (!propertyName.equals("class")) {
              fieldMap(propertyName).add();
            }
          }
        } else {
//          Property prop = resolvePropertyForT1(propertyName);
//          for (DefaultFieldMapper defaulter : defaults) {
//            String suggestion = defaulter.suggestMappedField(propertyName, prop.getType());
//            if (suggestion != null && getPropertiesForT2().contains(suggestion)) {
//              if (!getPropertiesForT2().contains(suggestion)) {
//                fieldMap(propertyName, suggestion, true).direction(direction).add();
//              }
//            }
//          }
        }
      }
    }

    return this;
  }


  /**
   * Create a fieldMap for the particular field (same property name used in both types)
   */
  public FieldMapBuilder<T1, T2, R> fieldMap(String field) {
    return fieldMap(field, field);
  }

  /**
   * 将t1的字段映射为指定字段
   *
   * @param t1Field     t1 中的字段名称
   * @param resultField 要被映射成为的字段
   * @return 当前工厂
   */
  public TableMapBuilder<T1, T2, R> fieldMapT1To(String t1Field, String resultField) {
    // TODO
    return this;
  }

  /**
   * 将t2的字段映射为指定字段
   *
   * @param t2Field     t2 中的字段名称
   * @param resultField 要被映射成为的字段
   * @return 当前工厂
   */
  public FieldMapBuilder<T1, T2, R> fieldMapT2To(String t2Field, String resultField) {
    return new FieldMapBuilder<>(this, false, true, resultField, null, t2Field,
        null, resolveProperty(this.getT2(), t2Field),
        resolveProperty(this.getResultClass(), resultField));
  }

  public FieldMapBuilder<T1, T2, R> fieldMap(String field, String resultField) {
    return new FieldMapBuilder<>(this, false, false, resultField, field, field,
        resolveProperty(this.getT1(), field), resolveProperty(this.getT2(), field),
        resolveProperty(this.getResultClass(), resultField));
  }

  protected Set<String> getMappedPropertiesForT1() {
    return propertiesCacheT1;
  }

  protected Set<String> getMappedPropertiesForT2() {
    return propertiesCacheT2;
  }

  protected Set<String> getMappedPropertiesForResultClass() {
    return propertiesCacheResult;
  }

  private Set<String> getPropertiesForT1() {
    return t1Properties.keySet();
  }

  private Set<String> getPropertiesForT2() {
    return t2Properties.keySet();
  }

  protected DefaultFieldMapper[] getDefaultFieldMappers() {
    return defaults;
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

  public TableMapperFactory getTableMapperFactory() {
    return tableMapperFactory;
  }

  public PropertyResolverStrategy getPropertyResolverStrategy() {
    return propertyResolverStrategy;
  }

  public DefaultFieldMapper[] getDefaults() {
    return defaults;
  }

  public Map<String, Property> getT1Properties() {
    return t1Properties;
  }

  public Map<String, Property> getT2Properties() {
    return t2Properties;
  }


  protected Property resolveProperty(java.lang.reflect.Type type, String field) {
    return propertyResolverStrategy.getProperty(type, field);
  }

  protected void addFieldMap(FieldMap<T1, T2, R> fieldMap) {
    getMappedFields().add(fieldMap);
  }

  protected Set<FieldMap> getMappedFields() {
    return fieldsMapping;
  }
}
