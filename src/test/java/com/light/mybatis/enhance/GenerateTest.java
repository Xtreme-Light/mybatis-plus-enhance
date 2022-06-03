package com.light.mybatis.enhance;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig.Builder;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import java.util.Collections;

public class GenerateTest {

  public static void main(String[] args) {
    final DataSourceConfig root = new Builder("jdbc:mysql://127.0.0.1:3306/dev_database", "root",
        "123456")
        .dbQuery(new MySqlQuery())
        .keyWordsHandler(new MySqlKeyWordsHandler())
        .build();
    final GlobalConfig baomidou = new GlobalConfig.Builder()
        .outputDir("D:\\IdeaProjects\\mybatis-plus-enhance\\src\\main\\java")
        .author("light")
//        .enableKotlin()
//        .enableSwagger()
        .dateType(DateType.TIME_PACK)
        .commentDate("yyyy-MM-dd")
        .build();
    final PackageConfig build = new PackageConfig.Builder()
        .parent("com.light.mybatis.enhance")
        .moduleName("dev")
        .entity("entity")
        .service("service")
        .serviceImpl("service.impl")
        .mapper("mapper")
        .xml("mapper.xml")
        .controller("controller")
        .other("other")
        .pathInfo(Collections.singletonMap(OutputFile.xml,
            "D:\\IdeaProjects\\mybatis-plus-enhance\\src\\main\\resources\\mapper"))
        .build();
    final StrategyConfig build2 = new StrategyConfig.Builder()
        .enableSkipView()
        .disableSqlFilter()
        .entityBuilder()
//        .superClass(BaseEntity.class)
        .disableSerialVersionUID()
        .enableChainModel()
        .enableColumnConstant()
        .enableLombok()
        .enableRemoveIsPrefix()
        .enableTableFieldAnnotation()
//        .enableActiveRecord()
        .versionColumnName("version")
        .versionPropertyName("version")
        .logicDeleteColumnName("deleted")
        .logicDeletePropertyName("deleteFlag")
        .naming(NamingStrategy.underline_to_camel)
        .columnNaming(NamingStrategy.underline_to_camel)
        .addSuperEntityColumns("id", "created_by", "created_time", "updated_by", "updated_time")
        .addIgnoreColumns("age")
        .addTableFills(new Column("create_time", FieldFill.INSERT))
        .addTableFills(new Property("updateTime", FieldFill.INSERT_UPDATE))
        .idType(IdType.AUTO)
        .formatFileName("%sEntity")
        .controllerBuilder()
//        .enableHyphenStyle()
        .enableRestStyle()
        .formatFileName("%sApi")
        .build();
    final AutoGenerator autoGenerator = new AutoGenerator(root);
    autoGenerator.packageInfo(build).strategy(build2).global(baomidou);
    autoGenerator.execute();

  }
}
