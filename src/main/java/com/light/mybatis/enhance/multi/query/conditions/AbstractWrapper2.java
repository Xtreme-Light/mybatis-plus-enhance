package com.light.mybatis.enhance.multi.query.conditions;


import static com.baomidou.mybatisplus.core.enums.SqlKeyword.EQ;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper.DoSomething;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.interfaces.Compare;
import com.baomidou.mybatisplus.core.conditions.interfaces.Func;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * 连表查询
 */
@SuppressWarnings({"unchecked"})
public abstract class AbstractWrapper2<T1, T2, R, Children extends AbstractWrapper2<T1, T2, R, Children>>
  extends Wrapper2<T1,T2>
    implements Compare<Children, R>, Func<Children, R> {

  protected SharedString paramAlias;
  /**
   * 必要度量
   */
  protected AtomicInteger paramNameSeq;
  /**
   * 占位符
   */
  protected final Children typedThis = (Children) this;

  /**
   * 实体类型(主要用于确定泛型以及取TableInfo缓存)
   */
  private Class<T1> t1Class;
  /**
   * 数据库表映射实体类
   */
  private T1 t1;

  /**
   * 实体类型(主要用于确定泛型以及取TableInfo缓存)
   */
  private Class<T2> t2Class;
  /**
   * 数据库表映射实体类
   */
  private T2 t2;

  protected Map<String, Object> paramNameValuePairs;
  protected MergeSegments expression;

  public T1 getT1() {
    return t1;
  }

  public T2 getT2() {
    return t2;
  }

  public Children setT1(T1 entity) {
    this.t1 = entity;
    return typedThis;
  }

  public Children setT2(T2 entity) {
    this.t2 = entity;
    return typedThis;
  }

  public Children setT1Class(Class<T1> entityClass) {
    if (entityClass != null) {
      this.t1Class = entityClass;
    }
    return typedThis;
  }

  public Children setT2Class(Class<T2> entityClass) {
    if (entityClass != null) {
      this.t2Class = entityClass;
    }
    return typedThis;
  }

  public Class<T1> getT1Class() {
    if (t1Class == null && t1 != null) {
      t1Class = (Class<T1>) t1.getClass();
    }
    return t1Class;
  }

  public Class<T2> getT2Class() {
    if (t2Class == null && t2 != null) {
      t2Class = (Class<T2>) t2.getClass();
    }
    return t2Class;
  }

  @Override
  public <V> Children allEq(boolean condition, Map<R, V> params, boolean null2IsNull) {
    if (condition && CollectionUtils.isNotEmpty(params)) {
      params.forEach((k, v) -> {
        if (StringUtils.checkValNotNull(v)) {
          eq(k, v);
        } else {
          if (null2IsNull) {
            isNull(k);
          }
        }
      });
    }
    return typedThis;
  }

  @Override
  public <V> Children allEq(boolean condition, BiPredicate<R, V> filter, Map<R, V> params,
      boolean null2IsNull) {
    return null;
  }

  @Override
  public Children eq(boolean condition, R column, Object val) {
    return addCondition(condition, column, EQ, val);
  }

  @Override
  public Children ne(boolean condition, R column, Object val) {
    return null;
  }

  @Override
  public Children gt(boolean condition, R column, Object val) {
    return null;
  }

  @Override
  public Children ge(boolean condition, R column, Object val) {
    return null;
  }

  @Override
  public Children lt(boolean condition, R column, Object val) {
    return null;
  }

  @Override
  public Children le(boolean condition, R column, Object val) {
    return null;
  }

  @Override
  public Children between(boolean condition, R column, Object val1, Object val2) {
    return null;
  }

  @Override
  public Children notBetween(boolean condition, R column, Object val1, Object val2) {
    return null;
  }

  @Override
  public Children like(boolean condition, R column, Object val) {
    return null;
  }

  @Override
  public Children notLike(boolean condition, R column, Object val) {
    return null;
  }

  @Override
  public Children likeLeft(boolean condition, R column, Object val) {
    return null;
  }

  @Override
  public Children likeRight(boolean condition, R column, Object val) {
    return null;
  }

  @Override
  public Children isNull(R column) {
    return Func.super.isNull(column);
  }

  protected Children addCondition(boolean condition, R column, SqlKeyword sqlKeyword, Object val) {
    return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(column), sqlKeyword,
        () -> formatParam(null, val)));
  }

  /**
   * 函数化的做事
   *
   * @param condition 做不做
   * @param something 做什么
   * @return Children
   */
  protected final Children maybeDo(boolean condition, DoSomething something) {
    if (condition) {
      something.doIt();
    }
    return typedThis;
  }

  /**
   * 添加 where 片段
   *
   * @param sqlSegments ISqlSegment 数组
   */
  protected void appendSqlSegments(ISqlSegment... sqlSegments) {
    expression.add(sqlSegments);
  }

  /**
   * 获取 columnName
   */
  protected final ISqlSegment columnToSqlSegment(R column) {
    return () -> columnToString(column);
  }

  /**
   * 获取 columnName
   */
  protected String columnToString(R column) {
    return (String) column;
  }

  /**
   * 处理入参
   *
   * @param mapping 例如: "javaType=int,jdbcType=NUMERIC,typeHandler=xxx.xxx.MyTypeHandler" 这种
   * @param param   参数
   * @return value
   */
  protected final String formatParam(String mapping, Object param) {
    final String genParamName = Constants.WRAPPER_PARAM + paramNameSeq.incrementAndGet();
    final String paramStr = getParamAlias() + Constants.WRAPPER_PARAM_MIDDLE + genParamName;
    paramNameValuePairs.put(genParamName, param);
    return SqlScriptUtils.safeParam(paramStr, mapping);
  }

  public String getParamAlias() {
    return paramAlias == null ? Constants.WRAPPER : paramAlias.getStringValue();
  }

  @Override
  public Children isNull(boolean condition, R column) {
    return null;
  }

  @Override
  public Children isNotNull(boolean condition, R column) {
    return null;
  }

  @Override
  public Children in(boolean condition, R column, Collection<?> coll) {
    return null;
  }

  @Override
  public Children in(boolean condition, R column, Object... values) {
    return null;
  }

  @Override
  public Children notIn(boolean condition, R column, Collection<?> coll) {
    return null;
  }

  @Override
  public Children notIn(boolean condition, R column, Object... values) {
    return null;
  }

  @Override
  public Children inSql(boolean condition, R column, String inValue) {
    return null;
  }

  @Override
  public Children gtSql(boolean condition, R column, String inValue) {
    return null;
  }

  @Override
  public Children geSql(boolean condition, R column, String inValue) {
    return null;
  }

  @Override
  public Children ltSql(boolean condition, R column, String inValue) {
    return null;
  }

  @Override
  public Children leSql(boolean condition, R column, String inValue) {
    return null;
  }

  @Override
  public Children notInSql(boolean condition, R column, String inValue) {
    return null;
  }

  @Override
  public Children groupBy(boolean condition, R column) {
    return null;
  }

  @Override
  public Children groupBy(boolean condition, List<R> columns) {
    return null;
  }

  @Override
  public Children groupBy(boolean condition, R column, R... columns) {
    return null;
  }

  @Override
  public Children orderBy(boolean condition, boolean isAsc, R column) {
    return null;
  }

  @Override
  public Children orderBy(boolean condition, boolean isAsc, List<R> columns) {
    return null;
  }

  @Override
  public Children orderBy(boolean condition, boolean isAsc, R column, R... columns) {
    return null;
  }

  @Override
  public Children having(boolean condition, String sqlHaving, Object... params) {
    return null;
  }

  @Override
  public Children func(boolean condition, Consumer<Children> consumer) {
    return null;
  }

  protected void initNeed() {
    paramNameSeq = new AtomicInteger(0);
    paramNameValuePairs = new HashMap<>(16);
    expression = new MergeSegments();
//    lastSql = SharedString.emptyString();
//    sqlComment = SharedString.emptyString();
//    sqlFirst = SharedString.emptyString();
  }

  /**
   * 子类返回一个自己的新对象
   */
  protected abstract Children instance();
}
