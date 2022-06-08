package com.light.mybatis.enhance.multi.query.constant;

import com.baomidou.mybatisplus.core.toolkit.StringPool;

/**
 * 自用常量 雅虎
 *
 */
public interface MultiConstants extends StringPool {


  /**
   * project name
   */
  String MYBATIS_PLUS = "mybatis-plus";

  /**
   * MD5
   */
  String MD5 = "MD5";
  /**
   * AES
   */
  String AES = "AES";
  /**
   * AES 算法
   */
  String AES_CBC_CIPHER = "AES/CBC/PKCS5Padding";
  /**
   * as
   */
  String AS = " AS ";


  /**
   * 实体类 T1
   */
  String ENTITY_T1 = "t1";
  /**
   * 实体类 T1 带后缀 ==> .
   */
  String ENTITY_T1_DOT = ENTITY_T1 + DOT;
  /**
   * 实体类 T2
   */
  String ENTITY_T2 = "t2";
  /**
   * 实体类 T2 带后缀 ==> .
   */
  String ENTITY_T2_DOT = ENTITY_T1 + DOT;
  /**
   * wrapper 类
   */
  String WRAPPER = "ew";
  /**
   * wrapper 类 带后缀 ==> ew.
   */
  String WRAPPER_DOT = WRAPPER + DOT;
  /**
   * wrapper 类的属性 entity ew.t1
   */
  String WRAPPER_ENTITY_T1 = WRAPPER_DOT + ENTITY_T1;
  /**
   * wrapper 类的属性 entity ew.t2
   */
  String WRAPPER_ENTITY_T2 = WRAPPER_DOT + ENTITY_T2;
  /**
   * wrapper 类的属性  ==> ew.sqlSegment
   */
  String WRAPPER_SQLSEGMENT = WRAPPER_DOT + "sqlSegment";
  /**
   * wrapper 类的属性   ==> ew.emptyOfNormal
   */
  String WRAPPER_EMPTYOFNORMAL = WRAPPER_DOT + "emptyOfNormal";
  /**
   * wrapper 类的属性   ==> ew.nonEmptyOfNormal
   */
  String WRAPPER_NONEMPTYOFNORMAL = WRAPPER_DOT + "nonEmptyOfNormal";
  /**
   * wrapper 类的属性   ==> ew.nonEmptyOfEntity
   */
  String WRAPPER_NONEMPTYOFENTITY = WRAPPER_DOT + "nonEmptyOfEntity";
  /**
   * wrapper 类的属性   ==> ew.emptyOfWhere
   */
  String WRAPPER_EMPTYOFWHERE = WRAPPER_DOT + "emptyOfWhere";
  /**
   * wrapper 类的判断属性   ==> ew.nonEmptyOfWhere
   */
  String WRAPPER_NONEMPTYOFWHERE = WRAPPER_DOT + "nonEmptyOfWhere";

  /**
   * wrapper 类的属性 entity 带后缀 ==> ew.t1.
   */
  String WRAPPER_ENTITY_T1_DOT = WRAPPER_DOT + ENTITY_T1 + DOT;

  /**
   * wrapper 类的属性 entity 带后缀 ==> ew.t2.
   */
  String WRAPPER_ENTITY_T2_DOT = WRAPPER_DOT + ENTITY_T2 + DOT;
  /**
   * wrapper 类的属性 expression 下级属性 order  ==> ew.useAnnotationOrderBy
   */
  String WRAPPER_EXPRESSION_ORDER = WRAPPER_DOT + "useAnnotationOrderBy";
  /**
   * UpdateWrapper 类的属性   ==> ew.sqlSet
   */
  String U_WRAPPER_SQL_SET = WRAPPER_DOT + "sqlSet";
  /**
   * QueryWrapper 类的属性   ==> ew.sqlSelect
   */
  String Q_WRAPPER_SQL_SELECT = WRAPPER_DOT + "sqlSelect";
  /**
   * wrapper 类的属性   ==> ew.sqlComment
   */
  String Q_WRAPPER_SQL_COMMENT = WRAPPER_DOT + "sqlComment";
  /**
   * wrapper 类的属性   ==> ew.sqlFirst
   */
  String Q_WRAPPER_SQL_FIRST = WRAPPER_DOT + "sqlFirst";
  /**
   * columnMap
   */
  String COLUMN_MAP = "cm";
  /**
   * cm.isEmpty
   */
  String COLUMN_MAP_IS_EMPTY = COLUMN_MAP + DOT + "isEmpty";
  /**
   * collection
   *
   * @see #COLL
   */
  String COLLECTION = "collection";

  /**
   * @since 3.5.2
   */
  String COLL = "coll";
  /**
   * list
   *
   * @since 3.5.0
   */
  String LIST = "list";
  /**
   * where
   */
  String WHERE = "WHERE";
  /**
   * on
   */
  String ON = "on";
  /**
   * limit
   */
  String LIMIT = "LIMIT";

  /**
   * @since 3.5.2
   */
  String ARRAY = "array";
  /**
   * order by
   */
  String ORDER_BY = "ORDER BY";
  /**
   * asc
   */
  String ASC = "ASC";
  /**
   * desc
   */
  String DESC = "DESC";
  /**
   * 乐观锁字段
   */
  String MP_OPTLOCK_VERSION_ORIGINAL = "MP_OPTLOCK_VERSION_ORIGINAL";

  /**
   * wrapper 内部参数相关
   */
  String WRAPPER_PARAM = "MPGENVAL";
  String WRAPPER_PARAM_MIDDLE = ".paramNameValuePairs" + DOT;

}
