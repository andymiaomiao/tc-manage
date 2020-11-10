package com.opencloud.device.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.opencloud.device.mybatis.base.entity.AbstractEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.util.Date;


/**
 * The persistent class for the diskfile database table.
 *
 * @author jihao
 */
@Data
@TableName("oss_disk_file")
public class DiskFile extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.ID_WORKER)
	private Long fileId;

	private String serverName;

	private String groupName;

	private Long tenantId;

	private Integer downloadNum;

	@TableField(exist = false)
	private String downloadPwd;

	/**
	 * 有效时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(value = "expiration_time", fill = FieldFill.INSERT)
	private Date expirationTime;

	@TableField(exist = false)
	private String extra1;

	@TableField(exist = false)
	private String extra2;

	@TableField(exist = false)
	private String extra3;

	@TableField(exist = false)
	private String extra4;

	@TableField(exist = false)
	private String extra5;

	private String fileExt;

	private String fileFlag;

	private String fileName;

	private String nowName;

	private String fileVersion;

	private BigInteger fileSize;

	private String fileSource;

	private String fileUrl;

	private String formId;

//	private String groupName;

	private String isPublic;
	private String urlDisk;
	private String urlQiniu;
	private String urlFastdfs;
	private String urlMongodb;
//	private String urlbfs;
//	private String urlpaxossurlre;
	private String urlAlioss;
	
//	private String urlcfs;

	private String uploadUser;
	

}