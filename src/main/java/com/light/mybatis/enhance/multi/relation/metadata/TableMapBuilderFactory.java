package com.light.mybatis.enhance.multi.relation.metadata;

import com.light.mybatis.enhance.multi.relation.DefaultFieldMapper;
import com.light.mybatis.enhance.multi.relation.TableMapperFactory;
import com.light.mybatis.enhance.multi.relation.impl.DefaultTableMapperFactory;
import com.light.mybatis.enhance.multi.relation.property.PropertyResolverStrategy;

/**
 * 本工厂构造 需要注册表实体对象关系的 tableMapBuilder
 */
public abstract class TableMapBuilderFactory {

  /**
   * 下一个工厂
   */
  protected TableMapBuilderFactory nextTableMapBuilderFactory;
  /**
   * 当前工厂使用的 TableMapperFactory
   */
  protected TableMapperFactory tableMapperFactory;
  /**
   * 当前工厂使用的 PropertyResolverStrategy 主要是为了在注册阶段就避免 字段之间的映射错误，比如T1中的name为String 而R 中的name 为
   * Long，显然是不行的
   */
  protected PropertyResolverStrategy propertyResolver;

  /**
   * 当前工厂使用的默认字段映射
   */
  protected DefaultFieldMapper[] defaults;

  /**
   * @param tableMapperFactory 会使用给出的工厂来实例化TableMapBuilder 并调用他们的register()方法
   */
  public synchronized void setTableMapperFactory(TableMapperFactory tableMapperFactory) {
    this.tableMapperFactory = tableMapperFactory;
  }

  /**
   * 设置链中的下一个工厂
   *
   * @param tableMapBuilderFactory 下一个工厂
   */
  public void setChainTableMapBuilderFactory(TableMapBuilderFactory tableMapBuilderFactory) {
    nextTableMapBuilderFactory = tableMapBuilderFactory;
  }

  /**
   * @param propertyResolver 解决解决字段间不同类型字段的问题
   */
  public synchronized void setPropertyResolver(PropertyResolverStrategy propertyResolver) {
    this.propertyResolver = propertyResolver;
  }

  /**
   * @param defaults zero or more DefaultFieldMapper instances that should be applied when the
   *                 <code>byDefault</code> method of the ClassMapBuilder is called.
   */
  public synchronized void setDefaultFieldMappers(DefaultFieldMapper... defaults) {
    this.defaults = defaults;
  }

  /**
   * Verifies whether the factory has been properly initialized
   *
   * @return true if the factory has been initialized
   */
  public synchronized boolean isInitialized() {
    return propertyResolver != null && defaults != null;
  }


  protected abstract <T1, T2, R> TableMapBuilder<T1, T2, R> newClassMapBuilder(Class<T1> t1,
      Class<T2> t2, Class<R> rClass,
      String t1Field,
      String t2Field,
      TableMapperFactory tableMapperFactory, PropertyResolverStrategy propertyResolver,
      DefaultFieldMapper[] defaults);

  public <T1, T2, R> TableMapBuilderFactory chooseTableMapBuilderFactory(Class<T1> t1, Class<T2> t2,
      Class<R> resultClass) {
    if (appliesTo(t1, t2, resultClass)) {
      return this;
    }
    return nextTableMapBuilderFactory == null ? null
        : nextTableMapBuilderFactory.chooseTableMapBuilderFactory(t1, t2, resultClass);
  }

  protected <T1, T2, R> boolean appliesTo(Class<T1> t1, Class<T2> t2, Class<R> rClass) {
    return false;
  }

  public <T2, R, T1> TableMapBuilder<T1, T2, R> map(Class<T1> t1, Class<T2> t2,
      Class<R> resultClass,
      String t1Field,
      String t2Field) {
    return getNewTableMapBuilder(t1, t2, resultClass, t1Field, t2Field);
  }

  private synchronized <T1, T2, R> TableMapBuilder<T1, T2, R> getNewTableMapBuilder(Class<T1> t1,
      Class<T2> t2,
      Class<R> resultClass,
      String t1Field,
      String t2Field) {
    return newClassMapBuilder(t1, t2, resultClass, t1Field, t2Field, tableMapperFactory,
        propertyResolver, defaults);
  }

  public void setMapperFactory(DefaultTableMapperFactory tableMapperFactory) {
    this.tableMapperFactory = tableMapperFactory;
  }
}
