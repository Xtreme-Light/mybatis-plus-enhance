package com.light.mybatis.enhance.multi.query.injector;

import com.light.mybatis.enhance.multi.query.injector.methods.SelectLeftJoinList;
import com.light.mybatis.enhance.multi.query.metadata.TableInfoAlias;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

public class DefaultMultiSqlInjector extends AbstractMultiSqlInjector {
  @Override
  public List<AbstractMultiMethod> getMethodList(Class<?> mapperClass, TableInfoAlias tableInfo1,
      TableInfoAlias tableInfo2) {
    final Builder<AbstractMultiMethod> add = Stream.<AbstractMultiMethod>builder()
        .add(new SelectLeftJoinList());

    return add.build().collect(Collectors.toList());
  }
}
