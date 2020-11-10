package com.opencloud.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.opencloud.device.entity.DiskFile;
import com.opencloud.device.exception.BaseException;
import com.opencloud.device.form.req.FileUploadRes;
import com.opencloud.device.model.ResultBody;
import com.opencloud.device.properties.StorageProperties;
import com.opencloud.device.service.DiskFileService;
import com.opencloud.device.service.impl.FileStorageServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jihao
 */
@Slf4j
@RestController
public class FileUploadController{

    @Autowired
    private FileStorageServiceImpl storageService;
    @Autowired
    private StorageProperties storageProperties;
    @Autowired
    private DiskFileService diskFileService;

    private static String SUBVERSION1 = "_v";

    private static String SUBVERSION2 = "_V";

    ExecutorService executorService = Executors.newFixedThreadPool(5);

    @ApiOperation(value = "文件上传Demo,用于上传测试，上传后将重定向")
    @PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultBody handleFileUpload(@RequestPart("file") MultipartFile file,
                                       @RequestParam("serverName") String serverName,
                                       @RequestParam("username") String username,
                                       @RequestParam(value = "groupName", required = false) String groupName,
                                       @RequestParam("fileFlag") String fileFlag
    ) {
        String fileName = null;
        FileUploadRes fileUploadRes = null;
        if (file != null) {
            String realFileName = file.getOriginalFilename();
            if (storageProperties.isRename()) {
                if (realFileName != null) {
                    fileName = IdWorker.getId() + realFileName.substring(realFileName.lastIndexOf("."));
                    if (groupName != null && !groupName.isEmpty()) {
                        fileName = IdWorker.getId() + realFileName.substring(realFileName.lastIndexOf("."));
                    }
                }
            }

            final String finalFilename = fileName;
            doUpload(file, finalFilename);
            fileUploadRes = dbSave(serverName, username, groupName, file, fileName, realFileName,fileFlag);
        }
        return ResultBody.ok().data(fileUploadRes);
    }

    public void doUpload(MultipartFile file, final String finalFilename) {
        log.info("写入磁盘");
        // 磁盘存储
        if (storageProperties.isTodisk()) {
            executorService.execute(() -> storageService.store(file, finalFilename));
        }

        //第三方存储
//        UploadFileExt ufe;
//        try {
//            ufe = new UploadFileExt(finalFilename, file.getBytes(), file.getContentType(), file.getSize());
//            for (FileListener fl : StoreSource.getListensers()) {
//                executorService.execute(() -> fl.Store(ufe));
//            }
//        } catch (IOException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
    }

    public FileUploadRes dbSave(String serverName, String username, String groupiName, MultipartFile file, String fileName, String realFilaName,String fileFlag) {
        // 数据库存储
        DiskFile dbFile = new DiskFile();
        String fileId = UUID.randomUUID().toString();
        dbFile.setServerName(serverName);
        dbFile.setFileExt(file.getContentType());
        dbFile.setFileFlag(fileFlag);
        dbFile.setFileName(realFilaName);
        dbFile.setNowName(fileName);
        dbFile.setFileSize(BigInteger.valueOf(file.getSize()));
        dbFile.setIsPublic("1");
        dbFile.setGroupName(groupiName);
        dbFile.setUpdateTime(new Date());
        dbFile.setUploadUser(username);
        if (realFilaName.indexOf(SUBVERSION1) > 0) {
            dbFile.setFileVersion(realFilaName.substring(realFilaName.lastIndexOf(SUBVERSION1) + 1, realFilaName.lastIndexOf(".")));
        } else if (realFilaName.indexOf(SUBVERSION2) > 0) {
            dbFile.setFileVersion(realFilaName.substring(realFilaName.lastIndexOf(SUBVERSION2) + 1, realFilaName.lastIndexOf(".")));
        } else {
            dbFile.setFileVersion("V1.0.0.0");
        }
        if(fileFlag.equals("2")){
            dbFile.setUrlDisk(storageProperties.getImageurl() + fileName);
        }else if(fileFlag.equals("1")){
            dbFile.setUrlDisk(storageProperties.getDownloadurl() + fileName);
        }
//        if (file.getContentType().contains("image/")) {
//            dbFile.setUrlDisk(storageProperties.getImageurl() + fileName);
//        } else {
//            dbFile.setUrlDisk(storageProperties.getDownloadurl() + fileName);
//        }

        // 更新缓存
//        UsesCache.files++;
//        UsesCache.usedspace = UsesCache.usedspace + file.getSize();

        if (storageProperties.isToqiniu()) {
            dbFile.setUrlQiniu(storageProperties.getQiniu().getPrefix() + fileName);
        }

        if (storageProperties.isTomongodb()) {
            dbFile.setUrlMongodb(fileId);
        }
        boolean insert = diskFileService.save(dbFile);
        diskFileService.deleteOtherThree();
        FileUploadRes fileUploadRes = new FileUploadRes();
        if (insert) {
            fileUploadRes.setFileId(dbFile.getFileId());
            fileUploadRes.setFileName(dbFile.getFileName());
            fileUploadRes.setFileSize(dbFile.getFileSize().toString());
            fileUploadRes.setFileUrl(dbFile.getUrlDisk());
        } else {
            throw new BaseException("文件上传失败!");
        }
        return fileUploadRes;
    }


    @ApiOperation(value = "文件上传Demo,用于上传测试，上传后将重定向")
    @PostMapping(value = "/file/upload/three", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultBody handleFileUploadThree(@RequestPart("file") MultipartFile file,
                                            @RequestParam("serverName") String serverName,
                                            @RequestParam("username") String username,
                                            @RequestParam(value = "groupName", required = false) String groupName,
                                            @RequestParam("fileFlag") String fileFlag
    ) {
        String fileName = null;
        FileUploadRes fileUploadRes = null;
        if (file != null) {
            String realFileName = file.getOriginalFilename();
            if (storageProperties.isRename()) {
                if (realFileName != null) {
                    fileName = IdWorker.getId() + realFileName.substring(realFileName.lastIndexOf("."));
                    if (groupName != null && !groupName.isEmpty()) {
                        fileName = IdWorker.getId() + realFileName.substring(realFileName.lastIndexOf("."));
                    }
                }
            }
            LambdaQueryWrapper<DiskFile> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .eq(DiskFile::getFileFlag,"1")
                    .orderByDesc(DiskFile::getCreateTime)
                    .last("limit 0,3");
            List<DiskFile> diskFiles = diskFileService.list(queryWrapper);
            if (diskFiles != null && diskFiles.size() > 0) {
                String subFileName = null;
                String subVersion = null;
                if (realFileName.indexOf(SUBVERSION1) > 0) {
                    subFileName = realFileName.substring(0, realFileName.lastIndexOf(SUBVERSION1));
                    subVersion = realFileName.substring(realFileName.lastIndexOf(SUBVERSION1) + 1, realFileName.lastIndexOf("."));
                } else if (realFileName.indexOf(SUBVERSION2) > 0) {
                    subFileName = realFileName.substring(0, realFileName.lastIndexOf(SUBVERSION2));
                    subVersion = realFileName.substring(realFileName.lastIndexOf(SUBVERSION2) + 1, realFileName.lastIndexOf("."));
                }
                String finalSubFileName = subFileName;
                Optional<DiskFile> first = diskFiles.stream().filter(f ->
                        f.getFileName().equals(realFileName) ||
                                (finalSubFileName != null && f.getFileName().contains(finalSubFileName))
                ).findFirst();
                if (first.isPresent()) {
                    DiskFile diskFile = first.get();
                    String nowName = diskFile.getNowName();
                    doUpload(file, nowName);
                    diskFile.setUpdateTime(new Date());
                    diskFile.setFileName(subVersion == null ? diskFile.getFileName() : realFileName);
                    diskFile.setFileVersion(subVersion == null ? "V1.0.0.0" : subVersion);
                    boolean update = diskFileService.updateById(diskFile);
                    if (update) {
                        fileUploadRes = new FileUploadRes();
                        fileUploadRes.setFileId(diskFile.getFileId());
                        fileUploadRes.setFileName(diskFile.getFileName());
                        fileUploadRes.setFileSize(diskFile.getFileSize().toString());
                        fileUploadRes.setFileUrl(storageProperties.getDownloadurl() + diskFile.getNowName());
                    } else {
                        throw new BaseException("文件上传失败!");
                    }
                } else {
                    doUpload(file, fileName);
                    fileUploadRes = dbSave(serverName, username, groupName, file, fileName, realFileName,fileFlag);
                }
            } else {
                doUpload(file, fileName);
                fileUploadRes = dbSave(serverName, username, groupName, file, fileName, realFileName,fileFlag);
            }
        }
        return ResultBody.ok().data(fileUploadRes);
    }
}
