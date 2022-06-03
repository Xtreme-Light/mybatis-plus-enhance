package com.light.mybatis.enhance.dev.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.light.mybatis.enhance.multi.query.injector.DefaultMultiSqlInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class MybatisConfig {

  @Bean
  public ISqlInjector iSqlInjector() {
    return new DefaultMultiSqlInjector();
  }

}
