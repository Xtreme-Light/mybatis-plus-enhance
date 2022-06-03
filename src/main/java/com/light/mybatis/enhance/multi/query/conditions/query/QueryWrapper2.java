package com.light.mybatis.enhance.multi.query.conditions.query;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.light.mybatis.enhance.multi.query.conditions.AbstractWrapper2;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class QueryWrapper2<T1, T2> extends
    AbstractWrapper2<T1, T2, String, QueryWrapper2<T1, T2>>
    implements Query2<QueryWrapper2<T1, T2>, T1, T2, String> , ISqlSegment {

  private final SharedString sqlSelect = new SharedString();

  protected SharedString lastSql;
  /**
   * SQL注释
   */
  protected SharedString sqlComment;
  /**
   * SQL起始语句
   */
  protected SharedString sqlFirst;

  public QueryWrapper2() {
    this(null, null);
  }

  public QueryWrapper2(T1 t1Entity, T2 t2Entity) {
    super.setT1(t1Entity);
    super.setT2(t2Entity);
    super.initNeed();
  }

  public QueryWrapper2(T1 t1Entity, T2 t2Entity, String... columns) {
    super.setT1(t1Entity);
    super.setT2(t2Entity);
    super.initNeed();
    this.select(columns);
  }

  /**
   * 非对外公开的构造方法,只用于生产嵌套 sql
   */
  private QueryWrapper2(T1 t1Entity, Class<T1> t1EntityClass, T2 t2Entity,
      Class<T2> t2EntityClass, AtomicInteger paramNameSeq,
      Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString paramAlias,
      SharedString lastSql, SharedString sqlComment, SharedString sqlFirst) {
    super.setT1(t1Entity);
    super.setT1Class(t1EntityClass);
    super.setT2(t2Entity);
    super.setT2Class(t2EntityClass);
    this.paramNameSeq = paramNameSeq;
    this.paramNameValuePairs = paramNameValuePairs;
    this.expression = mergeSegments;
    this.paramAlias = paramAlias;
    this.lastSql = lastSql;
    this.sqlComment = sqlComment;
    this.sqlFirst = sqlFirst;
  }

  @Override
  public QueryWrapper2<T1, T2> select(String... columns) {
    return null;
  }

  @Override
  public QueryWrapper2<T1, T2> select(Class<T1> entityClass1, Class<T2> entityClass2,
      Predicate<TableFieldInfo> predicate) {
    return null;
  }

  @Override
  public String getSqlSelect() {
    return null;
  }

  @Override
  public MergeSegments getExpression() {
    return null;
  }

  @Override
  protected QueryWrapper2<T1, T2> instance() {
    return new QueryWrapper2<>(getT1(), getT1Class(), getT2(),
        getT2Class(), paramNameSeq, paramNameValuePairs, new MergeSegments(),
        paramAlias, SharedString.emptyString(), SharedString.emptyString(),
        SharedString.emptyString());
  }

  @Override
  public String getSqlSegment() {
    // TODO
    return null;
  }
}
