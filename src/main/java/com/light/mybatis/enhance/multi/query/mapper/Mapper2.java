package com.light.mybatis.enhance.multi.query.mapper;


import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.light.mybatis.enhance.multi.query.conditions.query.QueryWrapper2;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 顶级mapper 执行
 */
public interface Mapper2<T1, T2, R> extends Mapper<T1> {

  default R selectOne(@Param(Constants.WRAPPER) QueryWrapper2<T1, T2> queryWrapper) {
    List<R> ts = this.selectListLeftJoin2(queryWrapper);
    if (CollectionUtils.isNotEmpty(ts)) {
      if (ts.size() != 1) {
        throw ExceptionUtils.mpe(
            "One record is expected, but the query result is multiple records");
      }
      return ts.get(0);
    }
    return null;
  }

  List<R> selectListLeftJoin2(@Param(Constants.WRAPPER) QueryWrapper2<T1, T2> queryWrapper);
}
