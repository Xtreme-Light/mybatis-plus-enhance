package com.light.mybatis.enhance.dev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.light.mybatis.enhance.dev.entity.TenantInfoEntity;
import com.light.mybatis.enhance.dev.entity.UserInfoEntity;
import com.light.mybatis.enhance.dev.mapper.TenantInfoMapper;
import com.light.mybatis.enhance.dev.service.ITenantInfoService;
import com.light.mybatis.enhance.dev.vo.UserTenantVO;
import com.light.mybatis.enhance.multi.query.conditions.query.QueryWrapper2;
import com.light.mybatis.enhance.multi.query.UserTenantMapper2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 租户基本信息 服务实现类
 * </p>
 *
 * @author light
 * @since 2022-06-01
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TenantInfoServiceImpl extends
    ServiceImpl<TenantInfoMapper, TenantInfoEntity> implements ITenantInfoService {

  private final UserTenantMapper2 multiMapper;
  private final TenantInfoMapper tenantInfoMapper;

  @Override
  public void selectBy() {
    final QueryWrapper<TenantInfoEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.select(TenantInfoEntity.TYPE);
    final TenantInfoEntity tenantInfoEntity = tenantInfoMapper.selectOne(queryWrapper);
    log.info("=================查询出的结果为{}",tenantInfoEntity);
    final QueryWrapper2<TenantInfoEntity, UserInfoEntity> multiQueryWrapper = new QueryWrapper2<>();
    multiQueryWrapper.eq(TenantInfoEntity.NAME, "");
    final UserTenantVO userTenantVO = multiMapper.selectOne(multiQueryWrapper);
    log.info("-----------------查询出的结果为{}",userTenantVO);
  }
}
