package com.weiqisen.tc.form.req;

import lombok.Data;

/**
 * @author weiqisen
 * @version 1.0
 * @date 2020-07-22 11:01
 */
@Data
public class FileUploadRes {

    private Long fileId;

    private String fileUrl;

    private String fileSize;

    private String fileName;
}
