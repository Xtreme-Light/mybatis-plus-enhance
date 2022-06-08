package com.light.mybatis.enhance.dev.mapper;

import com.light.mybatis.enhance.dev.entity.UserInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户基本信息 Mapper 接口
 * </p>
 *
 * @author light
 * @since 2022-06-01
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfoEntity> {

}
