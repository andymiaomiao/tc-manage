package com.opencloud.device.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author jihao
 */
@Data
//@Component
@ConfigurationProperties("storage")
public class StorageProperties {

	private boolean rename = true;

	private boolean debug = true;
	private boolean todisk = true;
	private boolean toqiniu = true;
	private boolean tofastdfs = false;
	private boolean tomongodb = false;
	private boolean toseaweedfs = false;
	private boolean toalioss = false;

	private String downloadurl;

	private String imageurl;

	private StorageAliProperties ali = new StorageAliProperties();

	private StorageDiskProperties disk = new StorageDiskProperties();

	private StorageGridfsProperties gridfs = new StorageGridfsProperties();

	private StorageQiniuProperties qiniu = new StorageQiniuProperties();

	private StorageSeaweedfsProperties Seaweedfs = new StorageSeaweedfsProperties();
}
