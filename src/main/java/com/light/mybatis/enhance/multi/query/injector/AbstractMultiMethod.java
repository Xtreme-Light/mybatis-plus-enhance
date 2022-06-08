package com.light.mybatis.enhance.multi.query.injector;



import static java.util.stream.Collectors.joining;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.light.mybatis.enhance.multi.query.constant.MultiConstants;
import com.light.mybatis.enhance.multi.query.enums.MultiSqlMethod;
import com.light.mybatis.enhance.multi.query.metadata.TableInfoAlias;
import com.light.mybatis.enhance.multi.relation.TableMapperFacade;
import com.light.mybatis.enhance.multi.relation.impl.DefaultTableMapperFactory;
import com.light.mybatis.enhance.multi.relation.metadata.MapperKey;
import com.light.mybatis.enhance.multi.relation.metadata.TableMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

public abstract class AbstractMultiMethod implements MultiConstants {

  protected static final Log logger = LogFactory.getLog(AbstractMultiMethod.class);

  protected Configuration configuration;
  protected LanguageDriver languageDriver;
  protected MapperBuilderAssistant builderAssistant;

  protected final String methodName;

  protected AbstractMultiMethod(String methodName) {
    Assert.notNull(methodName, "方法名不能为空");
    this.methodName = methodName;
  }

  public void inject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass,
      Class<?> modelClass1,
      Class<?> modelClass2, TableInfoAlias tableInfo1, TableInfoAlias tableInfo2, Class<?> returnClass) {
    this.configuration = builderAssistant.getConfiguration();
    this.builderAssistant = builderAssistant;
    this.languageDriver = configuration.getDefaultScriptingLanguageInstance();
    /* 注入自定义方法 */
    injectMappedStatement(mapperClass, modelClass1, modelClass2, tableInfo1, tableInfo2,
        returnClass);
  }


  /**
   * 注入自定义 MappedStatement
   *
   * @param mapperClass mapper 接口
   * @param modelClass1 mapper 泛型
   * @param modelClass2 mapper 泛型
   * @param returnClass 返回类封装
   * @return MappedStatement
   */
  public abstract MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass1,
      Class<?> modelClass2, TableInfoAlias tableInfo1, TableInfoAlias tableInfo2, Class<?> returnClass);

  protected String sqlFirst() {
    return convertIfEwParam(Q_WRAPPER_SQL_FIRST, true);
  }

  protected String convertIfEwParam(final String param, final boolean newLine) {
    return SqlScriptUtils.convertIf(SqlScriptUtils.unSafeParam(param),
        String.format("%s != null and %s != null", WRAPPER, param), newLine);
  }

  /**
   * 改成反射对应的VO信息
   *
   * @param returnClass  VO类
   * @param queryWrapper 查询器
   * @return sql 脚本
   */
  protected String sqlSelectColumns(TableInfoAlias tableInfo1,TableInfoAlias tableInfo2,Class<?> returnClass, boolean queryWrapper) {
    /* 假设存在用户自定义的 resultMap 映射返回 */
    String selectColumns = ASTERISK;
    // TODO 这里应该根据 A 表对象 B 表对象 和 返回对象三者的关系直接获取到选择的map
    DefaultTableMapperFactory mapperFactory = new DefaultTableMapperFactory.Builder().build();
    TableMapperFacade mapper = mapperFactory.getTableMapperFacade();
    final TableMap<Object, Object, Object> tableMap = mapper.getTableMap(tableInfo1.getTableInfo(),
        tableInfo2.getTableInfo(), returnClass);
    if (!queryWrapper) {
      return selectColumns;
    }
    return convertChooseEwSelect(selectColumns);
  }

  protected String convertChooseEwSelect(final String otherwise) {
    return SqlScriptUtils.convertChoose(
        String.format("%s != null and %s != null", WRAPPER, Q_WRAPPER_SQL_SELECT),
        SqlScriptUtils.unSafeParam(Q_WRAPPER_SQL_SELECT), otherwise);
  }

  /**
   * 方式获取select where
   *
   * @param newLine    是否提到下一行
   * @param tableInfo1 表信息
   * @param tableInfo2 表信息
   * @return String
   */
  protected String sqlWhereWrapper(boolean newLine, TableInfoAlias tableInfo1, TableInfoAlias tableInfo2) {
    String sqlScript1 = tableInfo1.getAllSqlWhere(false, WRAPPER_ENTITY_T1_DOT );
    sqlScript1 = SqlScriptUtils.convertIf(sqlScript1, String.format("%s != null", WRAPPER_ENTITY_T1),
        true);
    sqlScript1 += NEWLINE;
    String sqlScript2 = tableInfo2.getAllSqlWhere(false, MultiConstants.WRAPPER_ENTITY_T2_DOT);
    sqlScript2 = SqlScriptUtils.convertIf(sqlScript2, String.format("%s != null", WRAPPER_ENTITY_T2),
        true);
    sqlScript2 += NEWLINE;
    String sqlScript = sqlScript1 + sqlScript2;
    sqlScript += SqlScriptUtils.convertIf(String.format(SqlScriptUtils.convertIf(" AND",
            String.format("%s and %s", WRAPPER_NONEMPTYOFENTITY, WRAPPER_NONEMPTYOFNORMAL), false)
            + " ${%s}", WRAPPER_SQLSEGMENT),
        String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
            WRAPPER_NONEMPTYOFWHERE), true);
    sqlScript = SqlScriptUtils.convertWhere(sqlScript) + NEWLINE;
    sqlScript += SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER_SQLSEGMENT),
        String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
            WRAPPER_EMPTYOFWHERE), true);
    sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", WRAPPER), true);
    sqlScript = newLine ? NEWLINE + sqlScript : sqlScript;
    return sqlScript;
  }

  protected MappedStatement addSelectMappedStatementForTable(Class<?> mapperClass, String id,
      SqlSource sqlSource,
      TableInfoAlias table1, TableInfoAlias table2,Class<?> returnClass) {
    String resultMap1 = table1.getTableInfo().getResultMap();
    String resultMap2 = table2.getTableInfo().getResultMap();
    if (null != resultMap1 && null != resultMap2) {
      /* 返回 resultMap 映射结果集 */
      return addMappedStatement(mapperClass, id, sqlSource, SqlCommandType.SELECT, null,
          resultMap1, resultMap2, null, NoKeyGenerator.INSTANCE, null, null);
    } else {
      /* 普通查询 */
      return addSelectMappedStatementForOther(mapperClass, id, sqlSource, returnClass);
    }
  }
  /**
   * 查询
   */
  protected MappedStatement addSelectMappedStatementForOther(Class<?> mapperClass, String id, SqlSource sqlSource,
      Class<?> resultType) {
    return addMappedStatement(mapperClass, id, sqlSource, SqlCommandType.SELECT, null,
        null,null, resultType, NoKeyGenerator.INSTANCE, null, null);
  }

  /**
   * 添加 MappedStatement 到 Mybatis 容器
   */
  protected MappedStatement addMappedStatement(Class<?> mapperClass, String id, SqlSource sqlSource,
      SqlCommandType sqlCommandType, Class<?> parameterType,
      String resultMap1, String resultMap2, Class<?> resultType, KeyGenerator keyGenerator,
      String keyProperty, String keyColumn) {
    String statementName = mapperClass.getName() + DOT + id;
    if (hasMappedStatement(statementName)) {
      logger.warn(LEFT_SQ_BRACKET + statementName
          + "] Has been loaded by XML or SqlProvider or Mybatis's Annotation, so ignoring this injection for ["
          + getClass() + RIGHT_SQ_BRACKET);
      return null;
    }
    /* 缓存逻辑处理 */
    boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
    // TODO result Map 融合
    String resultMap;
    final boolean notBlank1 = StringUtils.isNotBlank(resultMap1);
    final boolean notBlank2 = StringUtils.isNotBlank(resultMap2);
    if (notBlank1 && notBlank2) {
      resultMap = resultMap1 + resultMap2;
    } else if (notBlank1) {
      resultMap = resultMap1;
    } else if (notBlank2) {
      resultMap = resultMap2;
    }else {
      resultMap = null;
    }
    return builderAssistant.addMappedStatement(id, sqlSource, StatementType.PREPARED,
        sqlCommandType,
        null, null, null, parameterType, resultMap, resultType,
        null, !isSelect, isSelect, false, keyGenerator,keyProperty, keyColumn,
        configuration.getDatabaseId(), languageDriver, null);
  }

  /**
   * 是否已经存在MappedStatement
   *
   * @param mappedStatement MappedStatement
   * @return true or false
   */
  private boolean hasMappedStatement(String mappedStatement) {
    return configuration.hasStatement(mappedStatement, false);
  }


  public String getMethod(MultiSqlMethod sqlMethod) {
    return StringUtils.isBlank(methodName) ? sqlMethod.getMethod() : this.methodName;
  }

  protected String sqlOrderBy(TableInfoAlias tableInfo1, TableInfoAlias tableInfo2) {
    /* 不存在排序字段，直接返回空 */
    List<TableFieldInfo> orderByFields = tableInfo1.getTableInfo().getOrderByFields();
    if (CollectionUtils.isEmpty(orderByFields)) {
      return StringPool.EMPTY;
    }
    orderByFields.sort(Comparator.comparingInt(TableFieldInfo::getOrderBySort));
    StringBuilder sql = new StringBuilder();
    sql.append(NEWLINE).append(" ORDER BY ");
    sql.append(orderByFields.stream().map(tfi -> String.format("%s %s", tfi.getColumn(),
        tfi.getOrderByType())).collect(joining(",")));
    /* 当wrapper中传递了orderBy属性，@orderBy注解失效 */
    return SqlScriptUtils.convertIf(sql.toString(), String.format("%s == null or %s", WRAPPER,
        WRAPPER_EXPRESSION_ORDER), true);
  }

  /**
   * SQL 注释
   *
   * @return sql
   */
  protected String sqlComment() {
    return convertIfEwParam(Q_WRAPPER_SQL_COMMENT, true);
  }
}
