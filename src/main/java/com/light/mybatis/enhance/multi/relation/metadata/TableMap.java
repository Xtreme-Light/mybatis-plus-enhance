package com.light.mybatis.enhance.multi.relation.metadata;


import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.ToString;

/**
 * 保存必要的所有映射字段信息
 *
 * @param <T1>
 * @param <T2>
 * @param <R>
 */
@Getter
@ToString
public class TableMap<T1, T2, R> {

  private final Class<T1> t1;
  private final Class<T2> t2;
  private final Class<R> resultClass;

  private final String t1Alias;

  private final String t2Alias;

  /**
   * 链接字符
   */
  private final String[] onBridge;



  public TableMap(Class<T1> t1, Class<T2> t2, Class<R> resultClass, String t1Alias, String t2Alias,
      String[] onBridge) {
    this.t1 = t1;
    this.t2 = t2;
    this.resultClass = resultClass;
    this.t1Alias = t1Alias;
    this.t2Alias = t2Alias;
    this.onBridge = onBridge;
  }

  public static class Builder<T1, T2, R> {

    private Class<T1> t1;
    private Class<T2> t2;
    private Class<R> resultClass;

    private String t1Alias;

    private String t2Alias;

    /**
     * 链接字符
     */
    private String[] onBridge;

    public Builder() {

    }

    public Builder<T1, T2, R> t1(Class<T1> t1) {
      this.t1 = t1;
      return this;
    }

    public Builder<T1, T2, R> t2(Class<T2> t2) {
      this.t2 = t2;
      return this;
    }

    public Builder<T1, T2, R> resultClass(Class<R> resultClass) {
      this.resultClass = resultClass;
      return this;
    }

    public Builder<T1, T2, R> t1Alias(String t1Alias) {
      this.t1Alias = t1Alias;
      return this;
    }


    public Builder<T1, T2, R> t2Alias(String t2Alias) {
      this.t2Alias = t2Alias;
      return this;
    }

    public Builder<T1, T2, R> onBridge(String[] onBridge) {
      this.onBridge = onBridge;
      return this;
    }

    public TableMap<T1, T2, R> build() {
      return new TableMap<>(t1, t2, resultClass, t1Alias, t2Alias, onBridge );
    }
  }

  @Getter
  @ToString
  public static class TableSelect {

    private final String alias;
    /**
     * column -> field
     */
    private final Map<String, String> columnMap;


    public TableSelect(String alias, Map<String, String> columnMap) {
      this.alias = alias;
      this.columnMap = columnMap;
    }
  }

  @Getter
  @ToString
  public static class TableWhere {

    private final String alias;

    private final Set<String> columns;


    public TableWhere(String alias, Set<String> columns) {
      this.alias = alias;
      this.columns = columns;
    }
  }
}
