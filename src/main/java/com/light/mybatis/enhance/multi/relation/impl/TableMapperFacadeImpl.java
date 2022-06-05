package com.light.mybatis.enhance.multi.relation.impl;

import com.light.mybatis.enhance.multi.relation.MappingContextFactory;
import com.light.mybatis.enhance.multi.relation.TableMapperFacade;
import com.light.mybatis.enhance.multi.relation.TableMapperFactory;

public class TableMapperFacadeImpl implements TableMapperFacade {

  protected final TableMapperFactory tableMapperFactory;
  private final MappingContextFactory contextFactory;

  public TableMapperFacadeImpl(TableMapperFactory tableMapperFactory,
      MappingContextFactory contextFactory) {
    this.tableMapperFactory = tableMapperFactory;
    this.contextFactory = contextFactory;
  }

  @Override
  public String[] getBridgeOn() {
    return new String[0];
  }
}
