package com.light.mybatis.enhance.dev.service.impl;

import com.light.mybatis.enhance.dev.entity.UserInfoEntity;
import com.light.mybatis.enhance.dev.mapper.UserInfoMapper;
import com.light.mybatis.enhance.dev.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 * @author light
 * @since 2022-06-01
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoEntity> implements IUserInfoService {

}
