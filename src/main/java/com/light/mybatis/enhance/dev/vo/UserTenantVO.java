package com.light.mybatis.enhance.dev.vo;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserTenantVO {

  private String tenantName;
  private String userName;
  private Integer tenantId;
  private Integer userId;
  private String account;
  private Integer type;
  private LocalDateTime tenantCreateTime;
  private LocalDateTime userCreateTime;

}
