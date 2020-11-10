package com.opencloud.device.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.Maps;
import com.opencloud.device.model.ResultBody;
import com.opencloud.device.properties.StorageProperties;
import com.opencloud.device.service.impl.FileStorageServiceImpl;
import com.opencloud.device.utils.ZipUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jihao
 */
@RestController
public class FileDownloadController {

    @Autowired
    private FileStorageServiceImpl storageService;

    @Autowired
    private StorageProperties storageProperties;

    @ApiOperation(value = "通过HttpHeaders下载文件")
    @GetMapping("/file/download/{filename}")
    public ResponseEntity<Resource> diskFileDownload(@PathVariable(value = "filename") String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @ApiOperation(value = "通过HttpHeaders下载文件")
    @GetMapping("/file/image/{filename}")
    public ResponseEntity<Resource> diskFileImage(@PathVariable(value = "filename") String filename) {
        Resource file = storageService.loadAsResource(filename);
        System.out.println("文件路径============================"+storageProperties.getDisk().getLocation());
        System.out.println("下载地址============================"+storageProperties.getDownloadurl());
        System.out.println("imageUrl============================"+storageProperties.getImageurl());
        return ResponseEntity.ok(file);
    }


    @PostMapping("/file/package")
    public ResultBody<Map<String,Object>> packageZip(@RequestParam Map<String, Object> params, HttpServletResponse response) throws IOException {
        Map<String,Object> resultMap = Maps.newHashMap();
        Object fileObj = params.get("fileList");
        JSONArray fileList = JSON.parseArray((String) fileObj);
        if (fileList != null && fileList.size() > 0) {
            List<Map<String, Object>> fileUrl = fileList.stream().map(m -> {
                JSONObject jsonObj = (JSONObject) m;
                Map<String, Object> fileMap = Maps.newHashMap();
                fileMap.put("fileUrl", storageProperties.getDisk().getLocation() + File.separator + jsonObj.getString("realName"));
                fileMap.put("realName", jsonObj.getString("realName"));
                fileMap.put("fileName", jsonObj.getString("fileName"));
                return fileMap;
            }).collect(Collectors.toList());
            String nowFile = IdWorker.getIdStr()+".zip";
            ZipUtil.toZipOutStream(fileUrl, storageProperties.getDisk().getLocation() + File.separator, nowFile, true);
            resultMap.put("downloadUrl",storageProperties.getDownloadurl()+nowFile);
        }
        return ResultBody.ok().data(resultMap);
    }
}
