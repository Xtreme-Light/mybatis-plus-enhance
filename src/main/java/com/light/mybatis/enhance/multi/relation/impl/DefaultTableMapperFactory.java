package com.light.mybatis.enhance.multi.relation.impl;

import com.light.mybatis.enhance.multi.relation.TableMapperFacade;
import com.light.mybatis.enhance.multi.relation.TableMapperFactory;
import com.light.mybatis.enhance.multi.relation.metadata.MapperKey;
import com.light.mybatis.enhance.multi.relation.metadata.TableMap;
import com.light.mybatis.enhance.multi.relation.metadata.TableMapBuilder;
import com.light.mybatis.enhance.multi.relation.metadata.TableMapBuilderFactory;
import com.light.mybatis.enhance.multi.relation.property.IntrospectorPropertyResolver;
import com.light.mybatis.enhance.multi.relation.property.PropertyResolverStrategy;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTableMapperFactory implements TableMapperFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTableMapperFactory.class);
  protected final ConcurrentHashMap<MapperKey, TableMap<Object, Object, Object>> tableMapRegistry;
  protected final PropertyResolverStrategy propertyResolverStrategy;
  protected final TableMapperFacade tableMapperFacade;

  protected final TableMapBuilderFactory tableMapBuilderFactory;

  public DefaultTableMapperFactory() {
    this.tableMapRegistry = new ConcurrentHashMap<>();
    this.propertyResolverStrategy = new IntrospectorPropertyResolver(false);
    this.tableMapBuilderFactory = new TableMapBuilderFactory() {
      @Override
      protected <T1, T2, R> TableMapBuilder<T1, T2, R> newClassMapBuilder(Class<T1> t1,
          Class<T2> t2, Class<R> rClass, String t1Field, String t2Field, String t1Alias,
          String t2Alias,
          TableMapperFactory tableMapperFactory, PropertyResolverStrategy propertyResolver) {
        return new TableMapBuilder<>(t1, t2, rClass, t1Field, t2Field, t1Alias, t2Alias,
            tableMapperFactory, propertyResolver);
      }
    };
    this.tableMapBuilderFactory.setPropertyResolver(this.propertyResolverStrategy);
    this.tableMapBuilderFactory.setTableMapperFactory(this);
    this.tableMapperFacade = buildTableMapperFacade();

  }

  protected TableMapperFacade buildTableMapperFacade() {
    return new TableMapperFacadeImpl(tableMapRegistry);
  }

  @Override
  public <T1, T2, R> TableMapBuilder<T1, T2, R> tableMap(Class<T1> t1, Class<T2> t2,
      Class<R> resultClass,
      String t1Field,
      String t2Field,
      String t1Alias,
      String t2Alias) {
    if (tableMapBuilderFactory != null) {
      return tableMapBuilderFactory.map(t1, t2, resultClass, t1Field, t2Field, t1Alias, t2Alias);
    } else {
      return getTableMapBuilderFactory().map(t1, t2, resultClass, t1Field, t2Field, t1Alias,
          t2Alias);
    }
  }

  @Override
  public <T1, T2, R> TableMapBuilder<T1, T2, R> tableMap(Class<T1> t1, Class<T2> t2,
      Class<R> resultClass,
      String t1Field,
      String t2Field) {
    if (tableMapBuilderFactory != null) {
      return tableMapBuilderFactory.map(t1, t2, resultClass, t1Field, t2Field, "t1", "t2");
    } else {
      return getTableMapBuilderFactory().map(t1, t2, resultClass, t1Field, t2Field, "t1", "t2");
    }
  }

  @Override
  public <T1, T2, R> void registerTableMap(TableMapBuilder<T1, T2, R> builder) {
    registerTableMap(builder.toTableMap());
  }

  @Override
  public TableMapperFacade getTableMapperFacade() {

    return tableMapperFacade;
  }


  @SuppressWarnings("unchecked")
  public synchronized <T1, T2, R> void registerTableMap(TableMap<T1, T2, R> tableMap) {
    tableMapRegistry.put(
        new MapperKey(tableMap.getT1(), tableMap.getT2(), tableMap.getResultClass()),
        (TableMap<Object, Object, Object>) tableMap);
  }

  protected TableMapBuilderFactory getTableMapBuilderFactory() {
    return tableMapBuilderFactory;
  }

  public static class Builder extends TableMapBuilderFactory {

    @Override
    protected <T1, T2, R> TableMapBuilder<T1, T2, R> newClassMapBuilder(Class<T1> t1, Class<T2> t2,
        Class<R> rClass, String t1Field, String t2Field, String t1Alias, String t2Alias,
        TableMapperFactory tableMapperFactory, PropertyResolverStrategy propertyResolver) {
      return new TableMapBuilder<>(t1, t2, rClass, t1Field, t2Field, t1Alias, t2Alias,
          tableMapperFactory, propertyResolver);
    }

    public DefaultTableMapperFactory build() {
      return new DefaultTableMapperFactory();
    }
  }
}
