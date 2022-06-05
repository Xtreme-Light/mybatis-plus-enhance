package com.light.mybatis.enhance.multi.relation.impl;

import com.light.mybatis.enhance.multi.relation.DefaultFieldMapper;
import com.light.mybatis.enhance.multi.relation.MappingContextFactory;
import com.light.mybatis.enhance.multi.relation.TableMapperFacade;
import com.light.mybatis.enhance.multi.relation.TableMapperFactory;
import com.light.mybatis.enhance.multi.relation.metadata.MapperKey;
import com.light.mybatis.enhance.multi.relation.metadata.TableMap;
import com.light.mybatis.enhance.multi.relation.metadata.TableMapBuilder;
import com.light.mybatis.enhance.multi.relation.metadata.TableMapBuilderFactory;
import com.light.mybatis.enhance.multi.relation.property.IntrospectorPropertyResolver;
import com.light.mybatis.enhance.multi.relation.property.PropertyResolverStrategy;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTableMapperFactory implements TableMapperFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTableMapperFactory.class);
  protected final ConcurrentHashMap<MapperKey, TableMap<Object, Object, Object>> tableMapRegistry;
  protected final TableMapBuilderFactory tableMapBuilderFactory;
  protected final List<DefaultFieldMapper> defaultFieldMappers;
  protected final PropertyResolverStrategy propertyResolverStrategy;
  protected volatile boolean isBuilt = false;
  protected volatile boolean isBuilding = false;

  protected final TableMapperFacade tableMapperFacade;
  protected final MappingContextFactory contextFactory;

  public DefaultTableMapperFactory(TableMapperFactoryBuilder<?, ?> builder) {
    this.tableMapRegistry = new ConcurrentHashMap<>();
    this.tableMapBuilderFactory = builder.tableMapBuilderFactory;
    this.propertyResolverStrategy = builder.propertyResolverStrategy;
    this.tableMapBuilderFactory.setPropertyResolver(this.propertyResolverStrategy);
    this.tableMapBuilderFactory.setTableMapperFactory(this);
    this.defaultFieldMappers = new CopyOnWriteArrayList<>();
    this.contextFactory = builder.mappingContextFactory;
    this.tableMapperFacade = buildTableMapperFacade(contextFactory);

  }

  protected TableMapperFacade buildTableMapperFacade(MappingContextFactory contextFactory) {
    return new TableMapperFacadeImpl(this, contextFactory);
  }

  @Override
  public <T1, T2, R> TableMapBuilder<T1, T2, R> tableMap(Class<T1> t1, Class<T2> t2,
      Class<R> resultClass,
      String t1Field,
      String t2Field) {
    if (tableMapBuilderFactory != null) {
      return tableMapBuilderFactory.map(t1, t2, resultClass, t1Field, t2Field);
    } else {
      return getTableMapBuilderFactory().map(t1, t2, resultClass, t1Field, t2Field);
    }
  }


  @Override
  public <T1, T2, R> void registerTableMap(TableMapBuilder<T1, T2, R> builder) {
    registerClassMap(builder.toClassMap());
  }

  @Override
  public TableMapperFacade getTableMapperFacade() {
    if (!isBuilt) {
      synchronized (tableMapperFacade) {
        if (!isBuilt) {
          build();
        }
      }
    }
    return tableMapperFacade;
  }
  // 将收集到的注册类型信息 使用起来
  public synchronized void build() {

    if (!isBuilding && !isBuilt) {
      isBuilding = true;
      for (Entry<MapperKey, TableMap<Object, Object, Object>> tableMapEntry : tableMapRegistry.entrySet()) {
        final TableMap<Object, Object, Object> value = tableMapEntry.getValue();
      }
      isBuilt = true;
      isBuilding = false;
    }
  }

  @SuppressWarnings("unchecked")
  public synchronized <T1, T2, R> void registerClassMap(TableMap<T1, T2, R> tableMap) {
    tableMapRegistry.put(
        new MapperKey(tableMap.getT1(), tableMap.getT2(), tableMap.getResultClass()),
        (TableMap<Object, Object, Object>) tableMap);
  }

  public static class Builder extends
      TableMapperFactoryBuilder<DefaultTableMapperFactory, Builder> {

    public DefaultTableMapperFactory build() {
      return new DefaultTableMapperFactory(this);
    }
  }

  public static abstract class TableMapperFactoryBuilder<F extends DefaultTableMapperFactory, B extends TableMapperFactoryBuilder<F, B>> {

    protected TableMapBuilderFactory tableMapBuilderFactory;
    protected PropertyResolverStrategy propertyResolverStrategy;
    protected MappingContextFactory mappingContextFactory;

    public TableMapperFactoryBuilder() {
      this.propertyResolverStrategy = new IntrospectorPropertyResolver(false);
      this.tableMapBuilderFactory = new TableMapBuilder.Factory();
    }
  }

  protected TableMapBuilderFactory getTableMapBuilderFactory() {
    if (!tableMapBuilderFactory.isInitialized()) {
      tableMapBuilderFactory.setDefaultFieldMappers(defaultFieldMappers.toArray(
          new DefaultFieldMapper[0]));
    }
    return tableMapBuilderFactory;
  }
}
