package com.light.mybatis.enhance.dev.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 租户基本信息
 * </p>
 *
 * @author light
 * @since 2022-06-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tenant_info")
@ToString
public class TenantInfoEntity {

  @TableId
  private Integer id;
  /**
   * 租户名称
   */
  @TableField("`name`")
  private String name;

  /**
   * 租户类型
   */
  @TableField("`type`")
  private Integer type;

  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private LocalDateTime createTime;


  public static final String NAME = "name";

  public static final String TYPE = "type";

  public static final String CREATE_TIME = "create_time";

}
