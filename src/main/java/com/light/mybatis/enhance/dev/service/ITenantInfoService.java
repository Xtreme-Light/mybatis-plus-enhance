package com.light.mybatis.enhance.dev.service;

import com.light.mybatis.enhance.dev.entity.TenantInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 租户基本信息 服务类
 * </p>
 *
 * @author light
 * @since 2022-06-01
 */
public interface ITenantInfoService extends IService<TenantInfoEntity> {

  void selectBy();
}
