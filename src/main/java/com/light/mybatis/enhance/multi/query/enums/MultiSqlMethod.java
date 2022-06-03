package com.light.mybatis.enhance.multi.query.enums;

/**
 * 多表查询定义语句
 */
public enum MultiSqlMethod {
  /**
   * 两张表的left join 查询
   */
  SELECT_LIST_LEFT_JOIN_2("selectListLeftJoin2", "查询满足条件所有数据",
      "<script>%s SELECT %s FROM %s LEFT JOIN %s ON %s.%s = %s.%s %s %s %s\n</script>"),
  ;
  private final String method;
  private final String desc;
  private final String sql;

  MultiSqlMethod(String method, String desc, String sql) {
    this.method = method;
    this.desc = desc;
    this.sql = sql;
  }

  public String getMethod() {
    return method;
  }

  public String getDesc() {
    return desc;
  }

  public String getSql() {
    return sql;
  }
}
