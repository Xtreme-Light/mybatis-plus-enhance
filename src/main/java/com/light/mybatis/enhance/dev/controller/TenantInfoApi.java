package com.light.mybatis.enhance.dev.controller;

import com.light.mybatis.enhance.dev.service.ITenantInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 租户基本信息 前端控制器
 * </p>
 *
 * @author light
 * @since 2022-06-01
 */
@RestController
@RequestMapping("/dev/tenantInfoEntity")
@RequiredArgsConstructor
public class TenantInfoApi {

  private final ITenantInfoService iTenantInfoService;

  @GetMapping
  public void selectBy() {
    iTenantInfoService.selectBy();
  }
}
