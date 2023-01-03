package com.example.mybatisgenerator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.query.SQLQuery;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.baomidou.mybatisplus.generator.config.rules.DbColumnType.BOOLEAN;

public class MybatisPlusGenerator {

    public static void main(String[] args) {
        String baseDir = System.getProperty("user.dir") + "/src/main";

        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8";
        String username = "root";
        String password = "Aa123456";
        DataSourceConfig.Builder datasourceBuilder = new DataSourceConfig.Builder(url, username, password)
                .typeConvert(new MySqlTypeConvert() {
                    @Override
                    public IColumnType processTypeConvert(@NotNull GlobalConfig globalConfig, @NotNull TableField tableField) {
                        // 把status字段映射为Boolean
                        if (tableField.getColumnName().equalsIgnoreCase("status")) {
                            return BOOLEAN;
                        }
                        return super.processTypeConvert(globalConfig, tableField);
                    }

                    @Override
                    public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
                        return super.processTypeConvert(config, fieldType);
                    }
                })
                .databaseQueryClass(SQLQuery.class);
        FastAutoGenerator.create(datasourceBuilder)
                .globalConfig(builder -> {
                    builder.author("wujiajie") // TODO 设置作者
                            .enableSpringdoc() // TODO 开启 springdoc 模式 or swagger
                            //.enableSwagger() // TODO 开启 swagger 模式
                            .disableOpenDir()
                            .outputDir(baseDir + "/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("cn.pinming.ai") // TODO 设置父包名
                            .moduleName("test") // TODO 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, baseDir + "/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.controllerBuilder().enableRestStyle();
                    builder.mapperBuilder().enableBaseColumnList().enableBaseResultMap();
                    builder.entityBuilder().enableChainModel().enableLombok().enableRemoveIsPrefix().enableActiveRecord().idType(IdType.AUTO);
                    builder.addInclude(getTables("all")) // TODO 设置需要生成的表名
                            .addTablePrefix("_", "d_", "f_"); // TODO 设置过滤表前缀
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

    /**
     * 处理 all 情况
      */
    private static List<String> getTables(String... tables) {
        List<String> list = List.of(tables);
        if (list.size() == 1 && "all".equals(list.get(0))) {
            return Collections.emptyList();
        }
        return List.of(tables);
    }

}
