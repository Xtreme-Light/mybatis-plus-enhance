package com.light.mybatis.enhance.multi.query.metadata;

import static java.util.stream.Collectors.joining;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.light.mybatis.enhance.multi.query.constant.MultiConstants;
import java.util.Objects;

public class TableInfoAlias implements MultiConstants {

  private final String alias;
  private final TableInfo tableInfo;

  private final String aliasTableName;
  private final String aliasKeyColumn;
  private final String aliasKeyProperty;

  public TableInfoAlias(String alias, TableInfo tableInfo) {
    this.alias = alias;
    this.tableInfo = tableInfo;
    this.aliasTableName = tableInfo.getTableName() + SPACE + alias;
    this.aliasKeyColumn = alias + DOT + tableInfo.getKeyColumn();
    this.aliasKeyProperty = WRAPPER_DOT + aliasKeyColumn;
  }

  public String getAlias() {
    return alias;
  }

  public TableInfo getTableInfo() {
    return tableInfo;
  }

  public String getAliasTableName() {
    return aliasTableName;
  }

  /**
   * 获取所有的查询的 sql 片段
   *
   * @param ignoreLogicDelFiled 是否过滤掉逻辑删除字段
   * @param prefix              前缀
   * @return sql 脚本片段
   */
  public String getAllSqlWhere(boolean ignoreLogicDelFiled, final String prefix) {
    final String newPrefix = prefix == null ? EMPTY : prefix;
    String filedSqlScript = tableInfo.getFieldList().stream()
        .filter(i -> {
          if (ignoreLogicDelFiled) {
            return !(tableInfo.isWithLogicDelete() && i.isLogicDelete());
          }
          return true;
        })
        .map(i -> getSqlWhere(newPrefix, i)
        ).filter(Objects::nonNull).collect(joining(NEWLINE));
    String keySqlScript = " AND " + aliasKeyColumn + EQUALS + SqlScriptUtils.safeParam(aliasKeyProperty);
    return
        SqlScriptUtils.convertIf(keySqlScript, String.format("%s != null", convertIfProperty(newPrefix, tableInfo.getKeyColumn())), false)
            + NEWLINE + filedSqlScript;
  }

  /**
   * 获取 查询的 sql 片段
   *
   * @param prefix 前缀
   * @return sql 脚本片段
   */
  public String getSqlWhere(final String prefix, TableFieldInfo tableFieldInfo) {
    final String newPrefix = prefix == null ? EMPTY : prefix;
    // 默认:  AND alias.column=#{prefix + el}
    String sqlScript = " AND " + String.format(tableFieldInfo.getCondition(),
        alias + DOT + tableFieldInfo.getColumn(), newPrefix + tableFieldInfo.getEl());
    // 查询的时候只判非空
    return convertIf(tableFieldInfo, sqlScript,
        convertIfProperty(newPrefix, tableFieldInfo.getProperty()),
        tableFieldInfo.getWhereStrategy());
  }

  private String convertIf(TableFieldInfo tableFieldInfo, final String sqlScript,
      final String property, final FieldStrategy fieldStrategy) {
    if (fieldStrategy == FieldStrategy.NEVER) {
      return null;
    }
    if (tableFieldInfo.isPrimitive() || fieldStrategy == FieldStrategy.IGNORED) {
      return sqlScript;
    }
    if (fieldStrategy == FieldStrategy.NOT_EMPTY && tableFieldInfo.isCharSequence()) {
      return SqlScriptUtils.convertIf(sqlScript,
          String.format("%s != null and %s != ''", property, property),
          false);
    }
    return SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", property), false);
  }

  private String convertIfProperty(String prefix, String property) {
    return StringUtils.isNotBlank(prefix) ? prefix.substring(0, prefix.length() - 1) + "['"
        + property + "']" : property;
  }
}
