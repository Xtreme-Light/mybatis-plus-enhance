package com.light.mybatis.enhance.multi.query.injector;

import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.core.toolkit.reflect.GenericTypeUtils;
import com.light.mybatis.enhance.multi.query.mapper.Mapper2;
import com.light.mybatis.enhance.multi.query.metadata.TableInfoAlias;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

public abstract class AbstractMultiSqlInjector implements ISqlInjector {

  protected final Log logger = LogFactory.getLog(this.getClass());

  private final ISqlInjector singISqlInjector = new DefaultSqlInjector();
  @Override
  public void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass) {
//    mapperClass.reflectionData.referent.interfaces
    final Class<?>[] interfaces = mapperClass.getInterfaces();
    if (interfaces.length == 1 && Mapper2.class == interfaces[0]) {
      // 多表
      final Class<?>[] classes = GenericTypeUtils.resolveTypeArguments(
          ClassUtils.getUserClass(mapperClass), Mapper2.class);
      assert classes.length == 3;
      final Class<?> aClass1 = classes[0];
      final Class<?> aClass2 = classes[1];
      final Class<?> aClass3 = classes[2];
      if (aClass1 != null && aClass2 != null && aClass3 != null) {
        final String className = mapperClass.toString();
        Set<String> mapperRegistryCache = GlobalConfigUtils.getMapperRegistryCache(builderAssistant.getConfiguration());
        if (!mapperRegistryCache.contains(className)) {
          // TODO 给表一个别名
          TableInfo tableInfo1 = TableInfoHelper.initTableInfo(builderAssistant, aClass1);
          final String t1 = camelPickAlias(tableInfo1, 1);
          final TableInfoAlias tableInfoAliasT1 = new TableInfoAlias(t1,tableInfo1);
          TableInfo tableInfo2 = TableInfoHelper.initTableInfo(builderAssistant, aClass2);
          final String t2 = camelPickAlias(tableInfo1, 2);
          final TableInfoAlias tableInfoAliasT2 = new TableInfoAlias(t2,tableInfo2);
          List<AbstractMultiMethod> methodList = this.getMethodList(mapperClass, tableInfoAliasT1,
              tableInfoAliasT2);
          if (CollectionUtils.isNotEmpty(methodList)) {
            // 循环注入自定义方法
            methodList.forEach(m -> m.inject(builderAssistant, mapperClass, aClass1, aClass2,
                tableInfoAliasT1, tableInfoAliasT2,aClass3));
          } else {
            logger.debug(mapperClass.toString() + ", No effective injection method was found.");
          }
          mapperRegistryCache.add(className);
        }
      }
    }else {
      singISqlInjector.inspectInject(builderAssistant,mapperClass);
    }



  }

  public String camelPickAlias(TableInfo tableInfo,int index) {
    if (index == 1) {
      return "t1";
    }else {
      return "t2";
    }
  }

  /**
   * <p>
   * 获取 注入的方法
   * </p>
   *
   * @param mapperClass 当前mapper
   * @return 注入的方法集合
   * @since 3.1.2 add  mapperClass
   */
  public abstract List<AbstractMultiMethod> getMethodList(Class<?> mapperClass,
      TableInfoAlias tableInfo1, TableInfoAlias tableInfo2);
}
