package com.light.mybatis.enhance.multi.query.injector.methods;


import com.light.mybatis.enhance.multi.query.enums.MultiSqlMethod;
import com.light.mybatis.enhance.multi.query.injector.AbstractMultiMethod;
import com.light.mybatis.enhance.multi.query.metadata.TableInfoAlias;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

public class SelectLeftJoinList extends AbstractMultiMethod {

  public SelectLeftJoinList() {
    super(MultiSqlMethod.SELECT_LIST_LEFT_JOIN_2.getMethod());
  }

  @Override
  public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass1,
      Class<?> modelClass2, TableInfoAlias tableInfo1, TableInfoAlias tableInfo2, Class<?> returnClass) {
    final MultiSqlMethod selectLeftJoinList2 = MultiSqlMethod.SELECT_LIST_LEFT_JOIN_2;
    // TODO 找到 modelClass1 modelClass2 returnClass 三者之间的关系 ，主要是 表一 和表二 的on 连接关系
    // TODO 和 returnClass 的字段 查询结果之间的映射关系
    // %s SELECT %s FROM %s LEFT JOIN %s ON %s.%s = %s.%s %s %s %s
    final String sql = String.format(MultiSqlMethod.SELECT_LIST_LEFT_JOIN_2.getSql(),
        sqlFirst(),
        sqlSelectColumns(tableInfo1,tableInfo2,returnClass, true),
        tableInfo1.getAliasTableName(),
        tableInfo2.getAliasTableName(),
        tableInfo1.getAlias(),
        "id",
        tableInfo2.getAlias(),
        "tenant_id",
        sqlWhereWrapper(true, tableInfo1, tableInfo2),
        sqlOrderBy(tableInfo1,tableInfo2),
        sqlComment()
    );

    // 参数的类型信息
    SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, returnClass);

    return this.addSelectMappedStatementForTable(mapperClass, getMethod(selectLeftJoinList2), sqlSource, tableInfo1,tableInfo2,returnClass);
  }
}
