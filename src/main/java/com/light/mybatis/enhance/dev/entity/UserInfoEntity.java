package com.light.mybatis.enhance.dev.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户基本信息
 * </p>
 *
 * @author light
 * @since 2022-06-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user_info")
public class UserInfoEntity {

  @TableId
  private Integer id;
  /**
   * 账号
   */
  @TableField("`account`")
  private String account;

  /**
   * 用户名字
   */
  @TableField("`name`")
  private String name;

  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  /**
   * 租户ID
   */
  @TableField("tenant_id")
  private Integer tenantId;


  public static final String ACCOUNT = "account";

  public static final String NAME = "name";

  public static final String CREATE_TIME = "create_time";

  public static final String TENANT_ID = "tenant_id";

}
