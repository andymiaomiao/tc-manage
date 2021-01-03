//package com.weiqisen.tc.generator;
//
//import com.baomidou.mybatisplus.annotation.FieldFill;
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.InjectionConfig;
//import com.baomidou.mybatisplus.generator.config.*;
//import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
//import com.baomidou.mybatisplus.generator.config.po.TableFill;
//import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.ArrayList;
//
///**
// * @author jihao
// * @version 1.0
// * @date 2020-09-04 16:28
// */
//public class GeneratorService {
//    public static void execute(GenerateConfig generateConfig) {
//        AutoGenerator mpg = new AutoGenerator();
//        // 全局配置
//        GlobalConfig gc = new GlobalConfig();
//        gc.setOutputDir(generateConfig.getOutputDir());
//        gc.setFileOverride(true);
//        //ActiveRecord特性
//        gc.setActiveRecord(false);
//        // XML ResultMap
//        gc.setBaseResultMap(true);
//        // XML columList
//        gc.setBaseColumnList(true);
//        gc.setEnableCache(false);
//        // 自动打开输出目录
//        gc.setOpen(false);
//        gc.setAuthor(generateConfig.getAuthor());
//        gc.setSwagger2(true);
//        //主键策略
//        gc.setIdType(IdType.ID_WORKER);
//
//
//        mpg.setGlobalConfig(gc);
//
//        // 数据源配置
//        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setDbType(generateConfig.getGenertarDataSource().getDbType());
//        dsc.setDriverName(generateConfig.getGenertarDataSource().getJdbcDriver());
//        dsc.setUrl(generateConfig.getGenertarDataSource().getJdbcUrl());
//        dsc.setUsername(generateConfig.getGenertarDataSource().getJdbcUserName());
//        dsc.setPassword(generateConfig.getGenertarDataSource().getJdbcPassword());
//        dsc.setTypeConvert(new MySqlTypeConvert() {
//            // 自定义数据库表字段类型转换【可选】
//            @Override
//            public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
//                //将数据库中datetime转换成date
//                if (fieldType.toLowerCase().contains("datetime")) {
//                    return DbColumnType.DATE;
//                }
//                return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
//            }
//        });
//        mpg.setDataSource(dsc);
//
//        // 策略配置
//        StrategyConfig strategy = new StrategyConfig();
//        strategy.setCapitalMode(false);
//        strategy.setRestControllerStyle(true);
//        strategy.setEntityLombokModel(true);
//        strategy.setEntityTableFieldAnnotationEnable(false);
//        // 此处可以移除表前缀表前缀
//        strategy.setTablePrefix(generateConfig.getTablePrefix());
//        // 表名生成策略
//        strategy.setNaming(NamingStrategy.underline_to_camel);
//        // 字段生成策略
//        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//
//
//        // 需要生成的表
//        strategy.setInclude(generateConfig.getIncludeTables());
//        // 包配置
//        PackageConfig pc = new PackageConfig();
//
//        // 配置模板
//        TemplateConfig templateConfig = new TemplateConfig();
//        genertorConfig(templateConfig,pc,strategy,gc,generateConfig);
//        // 配置自定义输出模板
//        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
//        // templateConfig.setEntity("templates/entity2.java");
//        // templateConfig.setService();
//        // templateConfig.setController();
//
////        ConfigBuilder config = new ConfigBuilder(new PackageConfig(), dsc, strategy, templateConfig, gc);
////        List<TableInfo> list = config.getTableInfoList();
//
//
//
//
//
//
//        // 公共字段填充
//        ArrayList<TableFill> tableFills = new ArrayList<>();
//        tableFills.add(new TableFill("createTime", FieldFill.INSERT));
//        tableFills.add(new TableFill("updateTime", FieldFill.UPDATE));
//        strategy.setTableFillList(tableFills);
//        mpg.setStrategy(strategy);
//
//        //父包名
//        pc.setParent(generateConfig.getParentPackage());
//        //父包模块名
//        pc.setModuleName(generateConfig.getModuleName());
//        // 自定义配置
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//                // to do nothing
//            }
//        };
////        String jsPath = "/templates/api.js.vm";
////        String vuePath = "/templates/index.vue.vm";
////        List<FileOutConfig> focList = new ArrayList<>();
////        // 自定义配置会被优先输出
////        focList.add(new FileOutConfig(jsPath) {
////            @Override
////            public String outputFile(TableInfo tableInfo) {
////                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
////                String path = gc.getOutputDir() + File.separator + pc.getParent().replace(".", File.separator) + File.separator + "js" + File.separator + tableInfo.getEntityName() + ".js";
////                return path;
////            }
////        });
////        focList.add(new FileOutConfig(vuePath) {
////            @Override
////            public String outputFile(TableInfo tableInfo) {
////                String path = gc.getOutputDir() + File.separator + pc.getParent().replace(".", File.separator) + File.separator + "view" + File.separator + tableInfo.getEntityName() + File.separator + "index.vue";
////                return path;
////            }
////        });
////
////        cfg.setFileOutConfigList(focList);
//        mpg.setCfg(cfg);
//        mpg.setPackageInfo(pc);
//        // 执行生成
//        mpg.execute();
//    }
//
//    private static void genertorConfig(TemplateConfig templateConfig, PackageConfig pc, StrategyConfig strategy, GlobalConfig gc, GenerateConfig generateConfig) {
//        // 自定义文件命名，注意 %s 会自动填充表实体属性！
//        gc.setServiceName("%sService");
//        gc.setServiceImplName("%sServiceImpl");
//        if(generateConfig.getGenertorTemplateConfig().getIsEntity()){
//            templateConfig.setEntity(generateConfig.getGenertorTemplateConfig().getEntity());
//            pc.setEntity(generateConfig.getGenertorPackageConfig().getEntity());
//            // 实体父类
//            strategy.setSuperEntityClass(generateConfig.getGenertorStrategyConfig().getSuperEntityClass());
////            strategy.setSuperEntityColumns("create_time", "update_time");
//            strategy.setSuperEntityColumns(generateConfig.getGenertorStrategyConfig().getSuperEntityColumns());
//            if(StringUtils.isNotEmpty(generateConfig.getGenertorGobalConfig().getEntityName())){
//                gc.setEntityName(generateConfig.getGenertorGobalConfig().getEntityName());
//            }
//        }else{
//            templateConfig.setEntity(null);
//        }
//        if(generateConfig.getGenertorTemplateConfig().getIsController()){
//            templateConfig.setController(generateConfig.getGenertorTemplateConfig().getController());
//            pc.setController(generateConfig.getGenertorPackageConfig().getController());
//            if(StringUtils.isNotEmpty(generateConfig.getGenertorGobalConfig().getControllerName())){
//                gc.setControllerName(generateConfig.getGenertorGobalConfig().getControllerName());
//            }
//        }else{
//            templateConfig.setController(null);
//        }
//        if(generateConfig.getGenertorTemplateConfig().getIsMapper()){
//            templateConfig.setMapper(generateConfig.getGenertorTemplateConfig().getMapper());
//            pc.setMapper(generateConfig.getGenertorPackageConfig().getMapper());
//            // mapper 父类
//            strategy.setSuperMapperClass(generateConfig.getGenertorStrategyConfig().getSuperMapperClass());
//            if(StringUtils.isNotEmpty(generateConfig.getGenertorGobalConfig().getMapperName())){
//                gc.setMapperName(generateConfig.getGenertorGobalConfig().getMapperName());
//            }
//        }else{
//            templateConfig.setMapper(null);
//        }
//        if(generateConfig.getGenertorTemplateConfig().getIsService()){
//            templateConfig.setService(generateConfig.getGenertorTemplateConfig().getService());
//            pc.setService(generateConfig.getGenertorPackageConfig().getService());
//            // 接口父类
//            strategy.setSuperServiceClass(generateConfig.getGenertorStrategyConfig().getSuperServiceClass());
//            if(StringUtils.isNotEmpty(generateConfig.getGenertorGobalConfig().getServiceName())){
//                gc.setServiceName(generateConfig.getGenertorGobalConfig().getServiceName());
//            }
//        }else{
//            templateConfig.setService(null);
//        }
//        if(generateConfig.getGenertorTemplateConfig().getIsServiceImpl()){
//            templateConfig.setServiceImpl(generateConfig.getGenertorTemplateConfig().getServiceImpl());
//            pc.setServiceImpl(generateConfig.getGenertorPackageConfig().getServiceImpl());
//            // 接口实现类父类
//            strategy.setSuperServiceImplClass(generateConfig.getGenertorStrategyConfig().getSuperServiceImplClass());
//            if(StringUtils.isNotEmpty(generateConfig.getGenertorGobalConfig().getServiceImplName())){
//                gc.setServiceImplName(generateConfig.getGenertorGobalConfig().getServiceImplName());
//            }
//        }else{
//            templateConfig.setServiceImpl(null);
//        }
//        if(generateConfig.getGenertorTemplateConfig().getIsXml()){
//            templateConfig.setXml(generateConfig.getGenertorTemplateConfig().getXml());
//            pc.setXml(generateConfig.getGenertorPackageConfig().getXml());
//            if(StringUtils.isNotEmpty(generateConfig.getGenertorGobalConfig().getXmlName())){
//                gc.setXmlName(generateConfig.getGenertorGobalConfig().getXmlName());
//            }
//        }else{
//            templateConfig.setXml(null);
//        }
//    }
//}
