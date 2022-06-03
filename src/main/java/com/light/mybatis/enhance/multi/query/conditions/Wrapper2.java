package com.light.mybatis.enhance.multi.query.conditions;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import java.util.Objects;

public abstract class Wrapper2<T1, T2> implements ISqlSegment {

  /**
   * 实体对象（子类实现）
   *
   * @return 泛型 T1
   */
  public abstract T1 getT1();

  /**
   * 实体对象（子类实现）
   *
   * @return 泛型 T1
   */
  public abstract T2 getT2();

  public String getSqlSelect() {
    return null;
  }

  public String getSqlSet() {
    return null;
  }

  public String getSqlComment() {
    return null;
  }

  public String getSqlFirst() {
    return null;
  }

  public abstract MergeSegments getExpression();

  /**
   * 查询条件为空(包含entity)
   */
  public boolean isEmptyOfWhere() {
    return isEmptyOfNormal() && isEmptyOfT1() && isEmptyOfT2();
  }

  /**
   * 查询条件为空(不包含entity)
   */
  public boolean isEmptyOfNormal() {
    return CollectionUtils.isEmpty(getExpression().getNormal());
  }

  /**
   * 查询条件不为空(包含entity)
   */
  public boolean nonEmptyOfWhere() {
    return !isEmptyOfWhere();
  }

  /**
   * 深层实体判断属性
   *
   * @return true 为空
   */
  public boolean isEmptyOfT1() {
    return !nonEmptyOfT1();
  }
  /**
   * 深层实体判断属性
   *
   * @return true 为空
   */
  public boolean isEmptyOfT2() {
    return !nonEmptyOfT2();
  }
  /**
   * T1深层实体判断属性
   *
   * @return true 不为空
   */
  public boolean nonEmptyOfT1() {
    T1 entity = getT1();
    return nonEmptyOfT(entity);
  }

  private boolean nonEmptyOfT(Object entity) {
    if (entity == null) {
      return false;
    }
    TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
    if (tableInfo == null) {
      return false;
    }
    if (tableInfo.getFieldList().stream().anyMatch(e -> fieldStrategyMatch(tableInfo, entity, e))) {
      return true;
    }
    return StringUtils.isNotBlank(tableInfo.getKeyProperty()) && Objects.nonNull(
        tableInfo.getPropertyValue(entity, tableInfo.getKeyProperty()));
  }

  public boolean nonEmptyOfT2() {
    T2 entity = getT2();
    return nonEmptyOfT(entity);
  }
  /**
   * 根据实体FieldStrategy属性来决定判断逻辑
   */
  private boolean fieldStrategyMatch(TableInfo tableInfo, Object entity, TableFieldInfo e) {
    return switch (e.getWhereStrategy()) {
      case IGNORED -> true;
      case NOT_EMPTY ->
          StringUtils.checkValNotNull(tableInfo.getPropertyValue(entity, e.getProperty()));
      case NEVER -> false;
      default -> Objects.nonNull(tableInfo.getPropertyValue(entity, e.getProperty()));
    };
  }

}
