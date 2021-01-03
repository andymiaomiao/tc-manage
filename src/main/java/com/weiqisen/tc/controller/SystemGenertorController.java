//package com.weiqisen.tc.controller;
//
//import cn.hutool.extra.template.TemplateConfig;
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.core.config.GlobalConfig;
//import com.baomidou.mybatisplus.core.metadata.TableInfo;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.google.common.collect.Maps;
//import com.weiqisen.tc.entity.SystemGenertor;
//import com.weiqisen.tc.generator.*;
//import com.weiqisen.tc.model.RequestParams;
//import com.weiqisen.tc.model.ResultBody;
//import com.weiqisen.tc.security.SecurityHelper;
//import com.weiqisen.tc.service.SystemGenertorService;
//import com.weiqisen.tc.utils.DateUtils;
//import com.weiqisen.tc.utils.WebUtil;
//import com.weiqisen.tc.utils.ZipUtil;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author jihao
// * @version 1.0
// * @date 2020-09-04 18:07
// */
//@Api(tags = "代码生成管理")
//@RestController
//public class SystemGenertorController {
//
//    @Autowired
//    private SystemGenertorService systemGenertorService;
//
//    /**
//     * 获取分页功能按钮列表
//     *
//     * @return
//     */
//    @ApiOperation(value = "获取分页功能按钮列表", notes = "获取分页功能按钮列表")
//    @GetMapping("/code/genertor/page")
//    public ResultBody<Page<SystemGenertor>> findPage(@RequestParam(required = false) Map map) {
//        return systemGenertorServiceClient.getPage(map);
//    }
//
//
//    /**
//     * 获取功能按钮详情
//     *
//     * @param genertorId
//     * @return
//     */
//    @ApiOperation(value = "获取功能按钮详情", notes = "获取功能按钮详情")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "genertorId", required = true, value = "功能按钮Id", paramType = "path"),
//    })
//    @GetMapping("/code/genertor/info")
//    public ResultBody<SystemGenertor> get(@RequestParam("genertorId") Long genertorId) {
//        return systemGenertorServiceClient.get(genertorId);
//    }
//
//    /**
//     * 添加/编辑功能按钮
//     *
//     * @param genertorId   功能按钮ID
//     * @param type 功能按钮编码
//     * @param driverName 功能按钮名称
//     * @param username     上级菜单
//     * @param password     是否启用
//     * @return
//     */
//    @ApiOperation(value = "添加/编辑功能按钮", notes = "添加/编辑功能按钮")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "genertorId", required = false, value = "功能按钮ID", paramType = "form"),
//            @ApiImplicitParam(name = "lineName", required = true, value = "功能按钮编码", paramType = "form"),
//            @ApiImplicitParam(name = "type", required = true, value = "功能按钮编码", paramType = "form"),
//            @ApiImplicitParam(name = "driverName", required = true, value = "功能按钮名称", paramType = "form"),
//            @ApiImplicitParam(name = "username", required = true, value = "功能按钮路径", paramType = "form"),
//            @ApiImplicitParam(name = "password", required = true, value = "上级菜单", paramType = "form")
//    })
//    @PostMapping("/code/genertor/save")
//    public ResultBody save(
//            @RequestParam(value = "genertorId", required = false) Long genertorId,
//            @RequestParam(value = "lineName") String lineName,
//            @RequestParam(value = "type") String type,
//            @RequestParam(value = "driverName") String driverName,
//            @RequestParam(value = "url") String url,
//            @RequestParam(value = "port") String port,
//            @RequestParam(value = "schemas") String schemas,
//            @RequestParam(value = "username") String username,
//            @RequestParam(value = "password") String password
//    ) {
//        SystemGenertor genertor = new SystemGenertor();
//        genertor.setGenertorId(genertorId);
//        genertor.setLineName(lineName);
//        genertor.setType(type);
//        genertor.setDriverName(driverName);
//        genertor.setUrl(url);
//        genertor.setPort(port);
//        genertor.setSchemas(schemas);
//        genertor.setUsername(username);
//        genertor.setPassword(password);
//        return systemGenertorServiceClient.save(genertor);
//    }
//
//
//    /**
//     * 移除功能按钮
//     *
//     * @param genertorId
//     * @return
//     */
//    @ApiOperation(value = "移除功能按钮", notes = "移除功能按钮")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "genertorId", required = true, value = "功能按钮ID", paramType = "form")
//    })
//    @PostMapping("/code/genertor/remove")
//    public ResultBody removeAction(
//            @RequestParam("genertorId") Long genertorId
//    ) {
//        return systemGenertorServiceClient.remove(genertorId);
//    }
//
//    /**
//     * 获取所有表信息
//     *
//     * @return
//     */
//    @ApiOperation(value = "获取所有表信息", notes = "获取所有表信息")
//    @GetMapping("/code/genertor/tables")
//    public ResultBody<List<TableInfo>> tables(@RequestParam("genertorId") Long genertorId) {
//        ResultBody<SystemGenertor> systemGenertorBody = systemGenertorServiceClient.get(genertorId);
//        SystemGenertor systemGenertor = new SystemGenertor();
//        if(systemGenertorBody.getCode()==0&&systemGenertorBody.getData()!=null){
//            BeanUtils.copyProperties(systemGenertorBody.getData(),systemGenertor);
//        }
//        GlobalConfig gc = new GlobalConfig();
//        // 数据源配置
//        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setDbType(DbType.getDbType(systemGenertor.getType()));
//        dsc.setDriverName(systemGenertor.getDriverName());
////            config.setJdbcUrl("jdbc:mysql://localhost:3306/open-platform?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC");
//        dsc.setUrl("jdbc:mysql://"+systemGenertor.getUrl()+":"+systemGenertor.getPort()+"/"+systemGenertor.getSchemas()+"?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC");
//        dsc.setUsername(systemGenertor.getUsername());
//        dsc.setPassword(systemGenertor.getPassword());
//        StrategyConfig strategy = new StrategyConfig();
//        TemplateConfig templateConfig = new TemplateConfig();
//        ConfigBuilder config = new ConfigBuilder(new PackageConfig(), dsc, strategy, templateConfig, gc);
//        List<TableInfo> list = config.getTableInfoList();
//        return ResultBody.ok().data(list);
//    }
//
//
//    @ApiOperation(value = "代码生成并下载", notes = "代码生成并下载")
//    @PostMapping("/code/genertor/execute")
//    public ResultBody<Map<String,Object>> execute(
//            @RequestParam Map requestMap
//    ) throws Exception {
//        RequestParams requestParams = new RequestParams(requestMap);
//        GenerateConfig generateConfig = requestParams.mapToBean(GenerateConfig.class);
//        GenertorGobalConfig genertorGobalConfig = requestParams.mapToBean(GenertorGobalConfig.class);
//        GenertorPackageConfig genertorPackageConfig = requestParams.mapToBean(GenertorPackageConfig.class);
//        GenertorStrategyConfig genertorStrategyConfig = requestParams.mapToBean(GenertorStrategyConfig.class);
//        GenertorTemplateConfig genertorTemplateConfig = requestParams.mapToBean(GenertorTemplateConfig.class);
//
//        genertorConfig(generateConfig,genertorGobalConfig,genertorPackageConfig,genertorStrategyConfig,genertorTemplateConfig,requestParams);
//
//        String outputDir = System.getProperty("user.dir") + File.separator + "temp" + File.separator + "generator" + File.separator + DateUtils.getCurrentTimestampStr();
////        GenerateConfig config = new GenerateConfig();
//        ResultBody<SystemGenertor> systemGenertorBody = systemGenertorServiceClient.get(generateConfig.getGenertorId());
//        SystemGenertor systemGenertor = null;
//        if(systemGenertorBody.getCode()==0&&systemGenertorBody.getData()!=null){
//            systemGenertor = systemGenertorBody.getData();
//        }
//        GenertarDataSource genertarDataSource = new GenertarDataSource();
//        genertarDataSource.setDbType(DbType.getDbType(systemGenertor.getType()));
//        genertarDataSource.setJdbcDriver(systemGenertor.getDriverName());
//        genertarDataSource.setJdbcUrl("jdbc:mysql://"+systemGenertor.getUrl()+":"+systemGenertor.getPort()+"/"+systemGenertor.getSchemas()+"?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC");
//        genertarDataSource.setJdbcUserName(systemGenertor.getUsername());
//        genertarDataSource.setJdbcPassword(systemGenertor.getPassword());
//        generateConfig.setGenertarDataSource(genertarDataSource);
//        generateConfig.setAuthor(SecurityHelper.getUser().getNickName());
//
//        generateConfig.setOutputDir(outputDir);
//        GeneratorService.execute(generateConfig);
//        String fileName = generateConfig.getModuleName() + ".zip";
//        String filePath = outputDir + File.separator + fileName;
//        // 压缩目录
//        String[] srcDir = {outputDir + File.separator + (generateConfig.getParentPackage().substring(0, generateConfig.getParentPackage().indexOf(".")))};
//        ZipUtil.toZip(srcDir, filePath, true);
//        Map<String,Object> data = Maps.newHashMap();
//        data.put("filePath", filePath);
//        data.put("fileName", fileName);
//        return ResultBody.ok().data(data);
//    }
//
//    @ApiOperation(value = "文件下载", notes = "文件下载")
//    @GetMapping(value = "/code/genertors/download")
//    public ResponseEntity<Resource> download(
//            @RequestParam("filePath") String filePath
//    ) throws Exception {
////        File file = new File(filePath);
////        download(response, filePath, file.getName());
//        Path file = Paths.get(filePath);
//        Resource resource = new UrlResource(file.toUri());
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_TYPE,"application/x-zip-compressed")
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
//                .body(resource);
//    }
//
//    /**
//     * 文件下载
//     *
//     * @param response
//     * @param filePath
//     * @param fileName
//     * @throws IOException
//     */
//    private void download(HttpServletResponse response, String filePath, String fileName) throws IOException {
//        WebUtil.setFileDownloadHeader(response, fileName);
//        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(filePath));
//        BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());
//        byte[] buffer = new byte[1024];
//        int bytesRead = 0;
//        while ((bytesRead = inStream.read(buffer)) != -1) {
//            outStream.write(buffer, 0, bytesRead);
//        }
//        outStream.flush();
//        inStream.close();
//    }
//
//    private void genertorConfig(GenerateConfig generateConfig,
//                                GenertorGobalConfig genertorGobalConfig,
//                                GenertorPackageConfig genertorPackageConfig,
//                                GenertorStrategyConfig genertorStrategyConfig,
//                                GenertorTemplateConfig genertorTemplateConfig,
//                                RequestParams requestParams){
//        Object idType = requestParams.getRequestMap().get("idType");
//        if(idType!=null){
//            switch (idType.toString()){
//                case "AUTO" :
//                    //语句
//                    genertorGobalConfig.setIdType(IdType.AUTO);
//                    break;
//                case "NONE" :
//                    //语句
//                    genertorGobalConfig.setIdType(IdType.NONE);
//                    break;
//                case "INPUT" :
//                    //语句
//                    genertorGobalConfig.setIdType(IdType.INPUT);
//                    break;
//                case "ID_WORKER" :
//                    //语句
//                    genertorGobalConfig.setIdType(IdType.ID_WORKER);
//                    break;
//                case "UUID" :
//                    //语句
//                    genertorGobalConfig.setIdType(IdType.UUID);
//                    break;
//                case "ID_WORKER_STR" :
//                    //语句
//                    genertorGobalConfig.setIdType(IdType.ID_WORKER_STR);
//                    break;
//                default :
//                    genertorGobalConfig.setIdType(IdType.NONE);
//                    break;
//            }
//        }
//        Object includeTables = requestParams.getRequestMap().get("includeTables");
//        if(includeTables!=null){
//            generateConfig.setIncludeTables(includeTables.toString().split(","));
//        }
//        Object tablePrefix = requestParams.getRequestMap().get("tablePrefix");
//        if(tablePrefix!=null){
//            generateConfig.setTablePrefix(tablePrefix.toString().split(","));
//        }
//        generateConfig.setGenertorGobalConfig(genertorGobalConfig);
//        generateConfig.setGenertorPackageConfig(genertorPackageConfig);
//        generateConfig.setGenertorStrategyConfig(genertorStrategyConfig);
//        generateConfig.setGenertorTemplateConfig(genertorTemplateConfig);
//    }
//}
