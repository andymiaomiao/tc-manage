/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : tc

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 27/12/2020 23:21:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(128) NOT NULL,
  `client_secret` varchar(256) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `scope` varchar(1024) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(2048) DEFAULT NULL,
  `access_token_validity` int DEFAULT NULL,
  `refresh_token_validity` int DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2客户端信息';

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
BEGIN;
INSERT INTO `oauth_client_details` VALUES ('7gBZcbsC7kLIWCdELIl8nxcs', '7e15db745f2c53964007d08df18c8dae2bfff7186920595a1454ce2cb7e56bce46497acbc6b2c4b1', '', 'userProfile', 'client_credentials,password,authorization_code', 'http://www.baidu.com', '', -1, -1, '{\"website\":\"http://www.baidu.com\",\"apiKey\":\"7gBZcbsC7kLIWCdELIl8nxcs\",\"secretKey\":\"0osTIhce7uPvDKHz6aa67bhCukaKoYl4\",\"appName\":\"后台管理服务器\",\"updateTime\":1574625417000,\"isPersist\":1,\"appOs\":\"\",\"appIcon\":\"\",\"developerId\":0,\"createTime\":1542016125000,\"appType\":\"server\",\"appId\":\"1552274783265\",\"appDesc\":\"资源服务器\",\"appNameEn\":\"admin-server\",\"status\":1}', '');
INSERT INTO `oauth_client_details` VALUES ('dwojp2egdmjc9z69s4k51zr4', '3914eed97be4e39d90c1b324a39211cc1fa2308c91c162ea9a3708ac09766744268569182f14a044', '', 'userProfile', 'authorization_code,client_credentials,implicit,refresh_token,password', NULL, '', 43200, 604800, '{\"appId\":\"1581138294780\",\"apiKey\":\"dwojp2egdmjc9z69s4k51zr4\",\"secretKey\":\"26nzfpctucxdi1eexo5jkbhcsfr4pm7j\",\"appType\":\"server\",\"appIcon\":\"\",\"appName\":\"商城服务\",\"appNameEn\":\"mall-server\",\"appOs\":\"\",\"developerId\":1222738554824736769,\"appDesc\":\"商城服务\",\"website\":\"商城服务\",\"status\":1,\"isPersist\":null,\"createTime\":null,\"updateTime\":1582458833846}', '');
COMMIT;

-- ----------------------------
-- Table structure for oss_disk_file
-- ----------------------------
DROP TABLE IF EXISTS `oss_disk_file`;
CREATE TABLE `oss_disk_file` (
  `file_id` bigint NOT NULL,
  `server_name` varchar(55) DEFAULT NULL,
  `group_name` varchar(55) DEFAULT NULL,
  `tenant_id` varchar(100) DEFAULT NULL,
  `download_num` int DEFAULT NULL,
  `expiration_time` datetime DEFAULT NULL,
  `file_version` varchar(45) DEFAULT NULL,
  `file_ext` varchar(145) DEFAULT NULL,
  `file_flag` varchar(45) DEFAULT NULL,
  `now_name` varchar(200) DEFAULT NULL,
  `file_name` varchar(200) DEFAULT NULL,
  `file_size` int DEFAULT NULL,
  `file_source` varchar(200) DEFAULT NULL,
  `file_url` varchar(200) DEFAULT NULL,
  `form_id` varchar(100) DEFAULT NULL,
  `is_public` varchar(45) DEFAULT NULL,
  `url_disk` varchar(145) DEFAULT NULL,
  `url_qiniu` varchar(145) DEFAULT NULL,
  `url_fastdfs` varchar(145) DEFAULT NULL,
  `url_mongodb` varchar(145) DEFAULT NULL,
  `url_alioss` varchar(145) DEFAULT NULL,
  `upload_user` varchar(145) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for system_access_logs
-- ----------------------------
DROP TABLE IF EXISTS `system_access_logs`;
CREATE TABLE `system_access_logs` (
  `access_id` bigint NOT NULL COMMENT '访问ID',
  `user_name` varchar(55) DEFAULT NULL COMMENT '访问路径',
  `user_id` bigint DEFAULT NULL COMMENT '访问ID',
  `tenant_id` bigint DEFAULT NULL COMMENT '访问ID',
  `path` varchar(100) DEFAULT NULL COMMENT '访问路径',
  `params` text COMMENT '请求数据',
  `result` text COMMENT '请求头',
  `headers` text COMMENT '请求头',
  `ip` varchar(20) DEFAULT NULL COMMENT '请求IP',
  `http_status` varchar(10) DEFAULT NULL COMMENT '响应状态',
  `method` varchar(50) DEFAULT NULL,
  `request_time` datetime DEFAULT NULL COMMENT '访问时间',
  `response_time` datetime DEFAULT NULL,
  `use_time` varchar(15) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL COMMENT '区域',
  `authentication` text COMMENT '认证信息',
  `option_name` varchar(55) DEFAULT NULL COMMENT '服务名',
  `server_name` varchar(55) DEFAULT NULL COMMENT '服务名',
  `error` varchar(255) DEFAULT NULL COMMENT '错误信息',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`access_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='访问日志';

-- ----------------------------
-- Table structure for system_account
-- ----------------------------
DROP TABLE IF EXISTS `system_account`;
CREATE TABLE `system_account` (
  `account_id` bigint NOT NULL COMMENT '账户id',
  `email` varchar(65) NOT NULL COMMENT '标识：手机号、邮箱、 用户名、或第三方应用的唯一标识',
  `mobile` varchar(65) NOT NULL COMMENT '标识：手机号、邮箱、 用户名、或第三方应用的唯一标识',
  `account` varchar(65) NOT NULL COMMENT '标识：手机号、邮箱、 用户名、或第三方应用的唯一标识',
  `password` varchar(255) NOT NULL COMMENT '密码凭证：站内的保存密码、站外的不保存或保存token）',
  `account_type` varchar(50) NOT NULL COMMENT '登录类型:password-密码、mobile-手机号、email-邮箱、weixin-微信、weibo-微博、qq-等等',
  `domain` varchar(255) DEFAULT NULL COMMENT '账户域:@admin.com,@developer.com',
  `register_ip` varchar(100) DEFAULT NULL COMMENT '注册IP',
  `status` tinyint DEFAULT NULL COMMENT '状态:0-禁用 1-启用 2-锁定',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='登录账号';

-- ----------------------------
-- Records of system_account
-- ----------------------------
BEGIN;
INSERT INTO `system_account` VALUES (521677655332356543, '1277821959@qq.com', '15219190193', 'tc573', '611a051fc30eedc8e5b43ab6507f1f6e62efb426f29babf4f19141ef4425071d08e6e9b7ccd8f2f3', 'mobile', '@admin.com', NULL, 1, '2020-07-03 17:11:59', '2020-12-03 22:45:46');
INSERT INTO `system_account` VALUES (1336715934148194306, 'VMHLlSclYwdN', '15219190193', 'test', '65c852de6faecbd5a2c8ddb45fb21dde0229d11ac5f814938f30f4f96a66cf3f9305e2248b11f75b', 'mobile', '@admin.com', NULL, 1, '2020-12-10 00:54:48', '2020-12-10 00:54:48');
COMMIT;

-- ----------------------------
-- Table structure for system_action
-- ----------------------------
DROP TABLE IF EXISTS `system_action`;
CREATE TABLE `system_action` (
  `action_id` bigint NOT NULL COMMENT '资源ID',
  `action_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '资源编码',
  `action_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '资源名称',
  `action_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '资源描述',
  `action_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '资源路径',
  `menu_id` bigint DEFAULT NULL COMMENT '资源父节点',
  `priority` int NOT NULL DEFAULT '0' COMMENT '优先级 越小越靠前',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
  `is_persist` tinyint NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统资源-功能操作';

-- ----------------------------
-- Records of system_action
-- ----------------------------
BEGIN;
INSERT INTO `system_action` VALUES (1283009693361586000, 'systemRolePage', '角色页面', '角色页面', '/admin/role/page', 3, 2, 1, 1, '2020-07-10 17:36:00', '2020-07-30 09:52:22');
INSERT INTO `system_action` VALUES (1283256240405375200, 'menu_save', '菜单保存', '菜单保存', '/admin/menu/save', 2, 11, 1, 1, '2020-07-13 10:36:00', '2020-07-13 10:36:00');
INSERT INTO `system_action` VALUES (1283305587120500738, 'devicePage', '设备页面', '', '/iot/device/page', 1283942201348001794, 1, 0, 0, '2020-07-15 15:41:09', '2020-07-15 16:40:48');
INSERT INTO `system_action` VALUES (1283320434906329089, 'deviceSave', '设备保存', '', '/iot/device/save', 1283942201348001794, 0, 0, 0, '2020-07-15 16:40:09', '2020-08-27 16:40:50');
INSERT INTO `system_action` VALUES (1283591992017114600, 'systemUserGrant', '用户授权', '用户角色授权', '/admin/user/roles/save', 5, 8, 1, 1, '2020-07-13 10:36:00', '2020-08-13 16:25:25');
INSERT INTO `system_action` VALUES (1283604397850038274, 'alarmPage', '告警页面', '', '/iot/alarm/page', 1232280128543265432, 0, 0, 0, '2020-07-16 11:28:31', '2020-07-16 11:28:59');
INSERT INTO `system_action` VALUES (1283623870607232800, 'systemRoleList', '角色列表', '角色列表', '/admin/role/list', 3, 7, 1, 1, '2020-07-13 10:36:00', '2020-08-13 11:52:23');
INSERT INTO `system_action` VALUES (1283696331994107906, 'distribuPage', '分布地址页面', '', '/iot/distribu/page', 1283701687365341185, 3, 0, 0, '2020-07-16 17:33:50', '2020-07-31 16:12:34');
INSERT INTO `system_action` VALUES (1283792655153665800, 'systemUserPage', '用户页面', '用户页面', '/admin/user', 5, 5, 1, 1, '2020-07-13 10:36:00', '2020-08-13 16:25:09');
INSERT INTO `system_action` VALUES (1283813967688940500, 'tenant_page', '商户列表', '商户劣币哦啊', '/admin/tenant/page', 1231491008353611777, 0, 1, 1, '2020-07-13 10:36:00', '2020-07-29 18:15:04');
INSERT INTO `system_action` VALUES (1283952377037610800, 'menu_list', '菜单页面', '菜单页面', '/admin/menu/list', 2, 2, 1, 1, '2020-07-10 17:36:00', '2020-07-13 17:35:09');
INSERT INTO `system_action` VALUES (1283954855995183105, 'distribuSave', '分布地址保存', '', '/iot/distribu/save', 1283701687365341185, 0, 0, 0, '2020-07-17 10:41:07', '2020-07-17 11:08:58');
INSERT INTO `system_action` VALUES (1284422873434464354, 'deviceUpgrade', '设备升级', '', '/iot/device/upgrade/upload', 1232280128558666456, 14, 0, 0, '2020-07-18 17:40:51', '2020-07-18 17:41:30');
INSERT INTO `system_action` VALUES (1284422873656765545, 'deviceSysnc', '设备同步', '', '/iot/device/info/to/device', 1283942201348001794, 4, 0, 0, '2020-07-18 17:40:51', '2020-08-27 16:47:35');
INSERT INTO `system_action` VALUES (1284422873756576547, 'alarmRepair', '告警修复', '', '/iot/alarm/repair/to/device', 1232280128543265432, 12, 0, 0, '2020-07-18 17:40:51', '2020-07-18 17:41:30');
INSERT INTO `system_action` VALUES (1284422873763276976, 'server_list', '服务列表', '服务列表', '/admin/api/server/list', 1284422330173874178, 0, 0, 0, '2020-07-18 17:40:51', '2020-07-18 17:41:30');
INSERT INTO `system_action` VALUES (1284422873764061186, 'apiPage', 'api页面', 'api页面', '/admin/api/page', 1284422330173874178, 0, 0, 0, '2020-07-18 17:40:51', '2020-07-18 17:41:30');
INSERT INTO `system_action` VALUES (1284422873764435435, 'userPage', '用户页面', '', '/iot/user/page', 1232280128554754657, 0, 0, 0, '2020-07-18 17:40:51', '2020-07-18 17:41:30');
INSERT INTO `system_action` VALUES (1284422873764655665, 'api_save', 'api保存', 'api保存', '/admin/api/save', 1284422330173874178, 0, 0, 0, '2020-07-18 17:40:51', '2020-07-18 17:41:30');
INSERT INTO `system_action` VALUES (1288041429427322881, 'dictionaryList', '字典列表', '', NULL, 1284422330143654544, 1, 1, 0, '2020-07-28 17:19:42', '2020-07-28 17:19:47');
INSERT INTO `system_action` VALUES (1288314965249318914, 'userRolePage', '角色页面', '', NULL, 1232280128554437643, 0, 1, 0, '2020-07-29 11:26:38', '2020-07-29 11:26:38');
INSERT INTO `system_action` VALUES (1288392564457652225, 'userRoleSave', '角色保存', '', NULL, 1232280128554437643, 1, 1, 0, '2020-07-29 16:34:59', '2020-08-08 14:16:27');
INSERT INTO `system_action` VALUES (1288417657623707649, 'tenantGrantMenu', '商户编辑', '', NULL, 1231491008353611777, 1, 1, 0, '2020-07-29 18:14:42', '2020-07-29 18:16:38');
INSERT INTO `system_action` VALUES (1288653527056408578, 'systemRoleGrant', '角色授权', '', NULL, 3, 1, 1, 0, '2020-07-30 09:51:58', '2020-07-30 09:51:58');
INSERT INTO `system_action` VALUES (1288734387655004162, 'userRoleGrant', '角色授权', '', NULL, 1232280128554437643, 2, 1, 0, '2020-07-30 15:13:16', '2020-08-08 14:16:33');
INSERT INTO `system_action` VALUES (1288777336436011010, 'userManageRole', '用户角色', '', NULL, 1232280128554657644, 1, 1, 0, '2020-07-30 18:03:56', '2020-08-07 19:50:23');
INSERT INTO `system_action` VALUES (1289037869339394050, 'monitorCenterDashboard', '警告首页', '', NULL, 1232280128545436554, 0, 1, 0, '2020-07-31 11:19:12', '2020-07-31 11:19:12');
INSERT INTO `system_action` VALUES (1289111578234339329, 'distribuRemove', '分布地址删除', '', NULL, 1283701687365341185, 4, 1, 0, '2020-07-31 16:12:05', '2020-07-31 16:12:29');
INSERT INTO `system_action` VALUES (1289118750976524290, 'deviceDistribuList', '设备分布地址列表', '', NULL, 1283942201344345565, 0, 1, 0, '2020-07-31 16:40:36', '2020-07-31 16:40:36');
INSERT INTO `system_action` VALUES (1289134103249043458, 'logPage', '日志页面', '', NULL, 1283663427784085506, 0, 1, 0, '2020-07-31 17:41:36', '2020-07-31 17:41:40');
INSERT INTO `system_action` VALUES (1289463070820741121, 'logUpload', '日志上传', '', NULL, 1283661175769993217, 0, 1, 0, '2020-08-01 15:28:48', '2020-08-01 15:28:53');
INSERT INTO `system_action` VALUES (1291185709192916994, 'deviceDelete', '设备删除', '', NULL, 1283942201348001794, 2, 1, 0, '2020-08-06 09:33:57', '2020-08-06 09:34:04');
INSERT INTO `system_action` VALUES (1291293505343270913, 'logExports', '日志导出', '', NULL, 1283663427784085506, 1, 1, 0, '2020-08-06 16:42:17', '2020-08-06 16:42:17');
INSERT INTO `system_action` VALUES (1291667020533805058, 'logDelete', '日志删除', '', NULL, 1283663427784085506, 2, 1, 0, '2020-08-07 17:26:30', '2020-08-07 17:26:35');
INSERT INTO `system_action` VALUES (1291703210720931842, 'userSave', '用户添加', '', NULL, 1232280128554657644, 0, 1, 0, '2020-08-07 19:50:19', '2020-08-07 19:50:19');
INSERT INTO `system_action` VALUES (1291942104561438722, 'userRemove', '用户删除', '', NULL, 1232280128554657644, 2, 1, 0, '2020-08-08 11:39:36', '2020-08-08 11:39:36');
INSERT INTO `system_action` VALUES (1291942908466909186, 'userGrantRole', '用户授权角色', '', NULL, 1232280128554657644, 3, 1, 0, '2020-08-08 11:42:47', '2020-08-08 11:42:47');
INSERT INTO `system_action` VALUES (1291981559378198530, 'userRoleRemove', '角色删除', '', NULL, 1232280128554437643, 3, 1, 0, '2020-08-08 14:16:22', '2020-08-08 14:16:22');
INSERT INTO `system_action` VALUES (1292669145967513602, 'distribuInfo', '分布地址详情', '', NULL, 1283701687365341185, 1, 1, 0, '2020-08-10 11:48:36', '2020-08-10 11:48:42');
INSERT INTO `system_action` VALUES (1292745665852866561, 'userLogPage', '日志页面', '', NULL, 1232280128555434534, 0, 1, 0, '2020-08-10 16:52:39', '2020-08-10 16:52:45');
INSERT INTO `system_action` VALUES (1293086261728292866, 'categoryManageSave', '分类添加', '', NULL, 1293086086939062274, 1, 1, 0, '2020-08-11 15:26:04', '2020-08-11 16:07:31');
INSERT INTO `system_action` VALUES (1293086338400169985, 'categoryManageRemove', '分类删除', '', NULL, 1293086086939062274, 2, 1, 0, '2020-08-11 15:26:22', '2020-08-11 16:07:35');
INSERT INTO `system_action` VALUES (1293096666877964289, 'categoryManageList', '分类页面', '', NULL, 1293086086939062274, 0, 1, 0, '2020-08-11 16:07:25', '2020-08-11 16:07:25');
INSERT INTO `system_action` VALUES (1293756890437767170, 'systemRoleSave', '角色保存', '', NULL, 3, 3, 1, 0, '2020-08-13 11:50:54', '2020-08-13 11:50:59');
INSERT INTO `system_action` VALUES (1293825817708707841, 'systemUserSave', '用户保存', '', NULL, 5, 1, 1, 0, '2020-08-13 16:24:48', '2020-08-13 16:24:48');
INSERT INTO `system_action` VALUES (1295287486197936129, 'userManageUpdatePassword', '用户修改密码', '', NULL, 1232280128554657644, 4, 1, 0, '2020-08-17 17:12:57', '2020-08-17 17:13:02');
INSERT INTO `system_action` VALUES (1295299241682894849, 'accountManagePage', '用户页面', '', NULL, 1293752319820812289, 0, 1, 0, '2020-08-17 17:59:39', '2020-08-17 17:59:44');
INSERT INTO `system_action` VALUES (1297728950746742785, 'logManagePage', '日志页面', '', NULL, 1293755541499269122, 0, 1, 0, '2020-08-24 10:54:27', '2020-08-24 10:54:32');
INSERT INTO `system_action` VALUES (1297745412173615105, 'roleManagePage', '角色页面', '', NULL, 1296753610633248769, 0, 1, 0, '2020-08-24 11:59:52', '2020-08-24 11:59:52');
INSERT INTO `system_action` VALUES (1297780372431458306, 'roleManageSave', '角色保存', '', NULL, 1296753610633248769, 1, 1, 0, '2020-08-24 14:18:47', '2020-08-24 14:18:47');
INSERT INTO `system_action` VALUES (1297804956853620737, 'userManagePage', '用户页面', '', NULL, 1293751690058649602, 0, 1, 0, '2020-08-24 15:56:28', '2020-08-24 15:56:28');
INSERT INTO `system_action` VALUES (1298095953558958082, 'userManageSave', '用户新增', '', NULL, 1293751690058649602, 1, 1, 0, '2020-08-25 11:12:47', '2020-08-25 11:12:47');
INSERT INTO `system_action` VALUES (1298169358689824770, 'versionManagePage', '版本页面', '', NULL, 1293753638946197506, 0, 1, 0, '2020-08-25 16:04:29', '2020-08-25 16:04:29');
INSERT INTO `system_action` VALUES (1298189534390972417, 'userDeviceReportPage', '设备健康报表页', '', NULL, 1293754914840891393, 0, 1, 0, '2020-08-25 17:24:39', '2020-08-25 17:24:39');
INSERT INTO `system_action` VALUES (1298189690964340737, 'healthReportPage', '健康报表页', '', NULL, 1293755220538544129, 0, 1, 0, '2020-08-25 17:25:16', '2020-08-25 17:25:16');
INSERT INTO `system_action` VALUES (1298571093023453186, 'accountManageGrant', '用户角色授权', '', NULL, 1293752319820812289, 0, 1, 0, '2020-08-26 18:40:50', '2020-08-26 18:40:50');
INSERT INTO `system_action` VALUES (1298903248278712321, 'deviceCenterExport', '设备导入', '', NULL, 1283942201348001794, 3, 1, 0, '2020-08-27 16:40:42', '2020-08-27 16:47:29');
INSERT INTO `system_action` VALUES (1299246684055859201, 'deviceUpgradeFileRemove', '设备升级文件删除', '', NULL, 1232280128558666456, 0, 1, 0, '2020-08-28 15:25:23', '2020-08-28 15:25:23');
COMMIT;

-- ----------------------------
-- Table structure for system_action_api
-- ----------------------------
DROP TABLE IF EXISTS `system_action_api`;
CREATE TABLE `system_action_api` (
  `action_id` bigint NOT NULL,
  `api_id` bigint NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`action_id`,`api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of system_action_api
-- ----------------------------
BEGIN;
INSERT INTO `system_action_api` VALUES (1283305587120500738, 1283096045490354200, '2020-07-31 16:42:57', '2020-07-31 16:42:57');
INSERT INTO `system_action_api` VALUES (1283305587120500738, 1289119286752722945, '2020-07-31 16:42:57', '2020-07-31 16:42:57');
INSERT INTO `system_action_api` VALUES (1283320434906329089, 1283171311919111700, NULL, NULL);
INSERT INTO `system_action_api` VALUES (1283604397850038274, 1283096045490354200, '2020-08-08 15:12:15', '2020-08-08 15:12:15');
INSERT INTO `system_action_api` VALUES (1283604397850038274, 1283154366683447800, '2020-08-08 15:12:16', '2020-08-08 15:12:16');
INSERT INTO `system_action_api` VALUES (1283696331994107906, 1283149777012644400, NULL, NULL);
INSERT INTO `system_action_api` VALUES (1283954855995183105, 1283836187335895300, '2020-08-08 16:52:18', '2020-08-08 16:52:18');
INSERT INTO `system_action_api` VALUES (1283954855995183105, 1292020598150590465, '2020-08-08 16:52:18', '2020-08-08 16:52:18');
INSERT INTO `system_action_api` VALUES (1284422873434464354, 1283199209442827800, '2020-08-03 10:06:04', '2020-08-03 10:06:04');
INSERT INTO `system_action_api` VALUES (1284422873434464354, 1289483731601534977, '2020-08-03 10:06:04', '2020-08-03 10:06:04');
INSERT INTO `system_action_api` VALUES (1284422873434464354, 1290106573443641346, '2020-08-03 10:06:04', '2020-08-03 10:06:04');
INSERT INTO `system_action_api` VALUES (1284422873656765545, 1283651901934994400, '2020-08-06 14:16:20', '2020-08-06 14:16:20');
INSERT INTO `system_action_api` VALUES (1284422873656765545, 1291256576258158594, '2020-08-06 14:16:20', '2020-08-06 14:16:20');
INSERT INTO `system_action_api` VALUES (1284422873756576547, 1283310641917632800, NULL, NULL);
INSERT INTO `system_action_api` VALUES (1284422873764435435, 1283157041535945700, NULL, NULL);
INSERT INTO `system_action_api` VALUES (1288314965249318914, 1288315377943666689, '2020-07-29 15:43:04', '2020-07-29 15:43:04');
INSERT INTO `system_action_api` VALUES (1288392564457652225, 1288392754375737345, '2020-07-29 16:38:48', '2020-07-29 16:38:48');
INSERT INTO `system_action_api` VALUES (1288734387655004162, 1288735611318030338, '2020-07-30 17:54:41', '2020-07-30 17:54:41');
INSERT INTO `system_action_api` VALUES (1288734387655004162, 1288735773939585026, '2020-07-30 17:54:41', '2020-07-30 17:54:41');
INSERT INTO `system_action_api` VALUES (1288734387655004162, 1288774929631080449, '2020-07-30 17:54:41', '2020-07-30 17:54:41');
INSERT INTO `system_action_api` VALUES (1288777336436011010, 1288777176830160898, '2020-07-30 18:49:34', '2020-07-30 18:49:34');
INSERT INTO `system_action_api` VALUES (1288777336436011010, 1288788681835384834, '2020-07-30 18:49:34', '2020-07-30 18:49:34');
INSERT INTO `system_action_api` VALUES (1289037869339394050, 1289037717597863937, '2020-07-31 11:19:17', '2020-07-31 11:19:17');
INSERT INTO `system_action_api` VALUES (1289111578234339329, 1289112122743078913, '2020-07-31 16:15:30', '2020-07-31 16:15:30');
INSERT INTO `system_action_api` VALUES (1289118750976524290, 1289118906627145729, '2020-07-31 16:41:25', '2020-07-31 16:41:25');
INSERT INTO `system_action_api` VALUES (1289134103249043458, 1289134272807976961, '2020-07-31 17:42:27', '2020-07-31 17:42:27');
INSERT INTO `system_action_api` VALUES (1289463070820741121, 1289462680020660225, '2020-08-06 14:16:42', '2020-08-06 14:16:42');
INSERT INTO `system_action_api` VALUES (1289463070820741121, 1289462916910755842, '2020-08-06 14:16:42', '2020-08-06 14:16:42');
INSERT INTO `system_action_api` VALUES (1289463070820741121, 1290942582863507457, '2020-08-06 14:16:42', '2020-08-06 14:16:42');
INSERT INTO `system_action_api` VALUES (1289463070820741121, 1291256719636246529, '2020-08-06 14:16:41', '2020-08-06 14:16:41');
INSERT INTO `system_action_api` VALUES (1291185709192916994, 1291185862918352897, '2020-08-06 09:34:49', '2020-08-06 09:34:49');
INSERT INTO `system_action_api` VALUES (1291293505343270913, 1291293390830383105, '2020-08-06 16:42:22', '2020-08-06 16:42:22');
INSERT INTO `system_action_api` VALUES (1291667020533805058, 1291667737629765634, '2020-08-07 17:32:42', '2020-08-07 17:32:42');
INSERT INTO `system_action_api` VALUES (1291703210720931842, 1291703099118891010, '2020-08-07 19:50:34', '2020-08-07 19:50:34');
INSERT INTO `system_action_api` VALUES (1291942104561438722, 1291942224656945153, '2020-08-08 11:42:14', '2020-08-08 11:42:14');
INSERT INTO `system_action_api` VALUES (1291942104561438722, 1291942344358187010, '2020-08-08 11:42:14', '2020-08-08 11:42:14');
INSERT INTO `system_action_api` VALUES (1291942908466909186, 1291942542509690882, '2020-08-08 11:43:25', '2020-08-08 11:43:25');
INSERT INTO `system_action_api` VALUES (1291981559378198530, 1291981735912259585, '2020-08-08 14:17:20', '2020-08-08 14:17:20');
INSERT INTO `system_action_api` VALUES (1292669145967513602, 1292668979558502402, '2020-08-10 11:48:47', '2020-08-10 11:48:47');
INSERT INTO `system_action_api` VALUES (1292745665852866561, 1292745539411378177, '2020-08-10 16:52:49', '2020-08-10 16:52:49');
INSERT INTO `system_action_api` VALUES (1293086261728292866, 1293094411927859202, '2020-08-11 16:07:46', '2020-08-11 16:07:46');
INSERT INTO `system_action_api` VALUES (1293086338400169985, 1293094529615835138, '2020-08-11 16:07:50', '2020-08-11 16:07:50');
INSERT INTO `system_action_api` VALUES (1293096666877964289, 1293094290683113473, '2020-08-11 16:07:41', '2020-08-11 16:07:41');
INSERT INTO `system_action_api` VALUES (1295287486197936129, 1295287279385194498, '2020-08-17 17:13:08', '2020-08-17 17:13:08');
COMMIT;

-- ----------------------------
-- Table structure for system_api
-- ----------------------------
DROP TABLE IF EXISTS `system_api`;
CREATE TABLE `system_api` (
  `api_id` bigint NOT NULL COMMENT '接口ID',
  `api_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '接口编码',
  `api_name` varchar(55) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '接口名称',
  `api_type` tinyint NOT NULL COMMENT '接口平台类型',
  `api_category` varchar(55) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT 'default' COMMENT '接口分类:default-默认分类',
  `api_desc` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '资源描述',
  `request_method` varchar(55) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '请求方式',
  `content_type` varchar(55) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '响应类型',
  `path` varchar(55) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '请求路径',
  `priority` int NOT NULL DEFAULT '0' COMMENT '优先级',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
  `is_persist` tinyint NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  `is_auth` tinyint NOT NULL DEFAULT '1' COMMENT '是否需要认证: 0-无认证 1-身份认证 默认:1',
  `class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '类名',
  `method_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '方法名',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`api_id`),
  UNIQUE KEY `api_code` (`api_code`) USING BTREE,
  UNIQUE KEY `api_id` (`api_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统资源-API接口';

-- ----------------------------
-- Records of system_api
-- ----------------------------
BEGIN;
INSERT INTO `system_api` VALUES (1283065449914662100, 'role_page', '角色页面', 1, 'default', '角色页面', 'GET', 'json', '/admin/role/page', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283096045490354200, 'devicePage', '设备页面', 10, 'default', '', 'GET', 'json', '/iot/device/page', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283149777012644400, 'distribuPage', '分布地址页面', 10, 'default', '', 'GET', 'json', '/iot/distribu/page', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-31 16:13:46');
INSERT INTO `system_api` VALUES (1283154366683447800, 'alarmPage', '告警页面', 10, 'default', '', 'GET', 'json', '/iot/alarm/page', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283157041535945700, 'userPage', '用户页面', 10, 'default', '', 'GET', 'json', '/iot/user/page', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283171311919111700, 'deviceSave', '设备保存', 10, 'default', '', 'GET', 'json', '/iot/device/save', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283176947381580300, 'menu_action_save', '菜单操作保存', 1, 'default', '菜单操作保存', 'GET', 'json', '/admin/action/save', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283199209442827800, 'deviceUpgrade', '设备上传文件', 10, 'default', '', 'GET', 'json', '/iot/device/soft/to/device', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283288975824834800, 'menu_list', '菜单页面', 1, 'default', '菜单页面', 'GET', 'json', '/admin/menu/list', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283307727538747600, 'role_list', '角色列表', 1, 'default', '角色列表', 'GET', 'json', '/admin/role/list', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283310641917632800, 'alarmRepair', '告警修复', 10, 'default', '', 'GET', 'json', '/iot/alarm/repair/to/device', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283426367068663300, 'menu_action_page', '菜单操作页面', 1, 'default', '菜单操作页面', 'GET', 'json', '/admin/menu/action', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283515822606626800, 'tenant_page', '商户列表', 1, 'default', '商户列表', 'GET', 'json', '/admin/tenant/page', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283592366764089000, 'user_role', '用户角色', 1, 'default', '用户角色', 'GET', 'json', '/admin/user/roles', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283620060762856200, 'menu_save', '菜单保存', 1, 'default', '菜单保存', 'GET', 'json', '/admin/menu/save', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283651901934994400, 'deviceSysnc', '设备同步', 10, 'default', '', 'GET', 'json', '/iot/device/interval', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283816158032510500, 'apiPage', 'api页面', 1, 'default', 'api页面', 'GET', 'json', '/admin/api/page', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283836187335895300, 'distribuSave', '分布地址保存', 10, 'default', '', 'GET', 'json', '/iot/distribu/save', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283853368868120000, 'user_page', '用户页面', 1, 'default', '用户页面', 'GET', 'json', '/admin/user/page', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283927120401434543, 'api_save', 'api保存', 1, 'default', 'API保存', 'POST', 'json', '/admin/api/save', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283927120401989600, 'user_grant_role', '用户角色授权', 1, 'default', '用户角色授权', 'GET', 'json', '/admin/user/roles/save', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283927120404565455, 'server_list', '服务列表', 1, 'default', '服务列表', 'GET', 'json', '/admin/api/server/list', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1283927120407677655, 'dict_list', '字典列表', 1, 'default', '字典列表', 'GET', 'json', '/admin/dict/list', 1, 1, 1, 1, '', '', '2020-07-28 11:34:13', '2020-07-28 11:34:13');
INSERT INTO `system_api` VALUES (1288064254397677569, 'jdIafr0u09txz86', '菜单操作授权', 1, 'default', '菜单操作授权', 'GET', NULL, '/admin/api/action/list', 0, 1, 0, 1, NULL, NULL, '2020-07-28 18:50:24', '2020-07-28 18:52:47');
INSERT INTO `system_api` VALUES (1288315377943666689, 'zhkeMoqFP5kJ', '角色页面', 10, 'default', '角色页面', 'GET', NULL, '/iot/role/page', 0, 1, 0, 1, NULL, NULL, '2020-07-29 11:28:17', '2020-07-29 11:28:17');
INSERT INTO `system_api` VALUES (1288358336839905282, '8lgooaAQ4HRW', '菜单操作授权保存', 1, 'default', '菜单操作授权保存', 'POST', NULL, '/admin/action/api/grant', 0, 1, 0, 1, NULL, NULL, '2020-07-29 14:18:59', '2020-07-29 14:18:59');
INSERT INTO `system_api` VALUES (1288392754375737345, 'bMyDOdGYGNxt', '角色保存', 10, 'default', '角色保存', 'POST', NULL, '/iot/role/save', 0, 1, 0, 1, NULL, NULL, '2020-07-29 16:35:44', '2020-07-29 16:35:44');
INSERT INTO `system_api` VALUES (1288418060968951809, 'P24SZdVTB1cS', '商户菜单功能权限', 1, 'default', '商户功能授权', 'GET', NULL, '/admin/tenant/authority/menu', 0, 1, 0, 1, NULL, NULL, '2020-07-29 18:16:18', '2020-07-30 16:57:39');
INSERT INTO `system_api` VALUES (1288653326065360898, 'VxcTdI02DoDP', '角色菜单功能授权', 1, 'default', '角色菜单功能权限', 'POST', NULL, '/admin/role/grant/menu', 0, 1, 0, 1, NULL, NULL, '2020-07-30 09:51:10', '2020-07-30 10:30:27');
INSERT INTO `system_api` VALUES (1288663126421577730, 'MqoyhFI7rUeJ', '角色菜单功能权限', 1, 'default', '角色功能权限', 'GET', NULL, '/admin/role/authority/menu', 0, 1, 0, 1, NULL, NULL, '2020-07-30 10:30:06', '2020-07-30 10:30:37');
INSERT INTO `system_api` VALUES (1288735611318030338, '0YwfwAb452KX', '角色菜单功能权限', 10, 'default', '角色菜单功能权限-智慧物联平台', 'GET', NULL, '/iot/role/authority/menu', 0, 1, 0, 1, NULL, NULL, '2020-07-30 15:18:08', '2020-07-30 15:18:08');
INSERT INTO `system_api` VALUES (1288735773939585026, 'aXYwEXFVSIAF', '角色菜单功能授权', 10, 'default', '角色菜单功能授权-智慧物联平台', 'POST', NULL, '/iot/role/grant/menu', 0, 1, 0, 1, NULL, NULL, '2020-07-30 15:18:47', '2020-07-30 15:18:47');
INSERT INTO `system_api` VALUES (1288760506036215809, 'ACyITbFLbqhj', '商户菜单功能授权', 1, 'default', '商户菜单功能授权', 'POST', NULL, '/admin/tenant/grant/menu', 0, 1, 0, 1, NULL, NULL, '2020-07-30 16:57:03', '2020-07-30 16:57:44');
INSERT INTO `system_api` VALUES (1288774929631080449, 'MxGYLkIS5nw0', '角色授权用户列举', 10, 'default', '角色授权用户列举', 'GET', NULL, '/iot/user/list', 0, 1, 0, 1, NULL, NULL, '2020-07-30 17:54:22', '2020-07-30 17:54:22');
INSERT INTO `system_api` VALUES (1288777176830160898, 'hIjaaEkGDavm', '用户角色列举', 10, 'default', '用户角色列举', 'GET', NULL, '/iot/role/list', 0, 1, 0, 1, NULL, NULL, '2020-07-30 18:03:18', '2020-07-30 18:03:18');
INSERT INTO `system_api` VALUES (1288788681835384834, 'EKIzz6L5bzQq', '用户角色列举', 10, 'default', '用户角色列举', 'GET', NULL, '/iot/user/roles', 0, 1, 0, 1, NULL, NULL, '2020-07-30 18:49:01', '2020-07-30 18:49:01');
INSERT INTO `system_api` VALUES (1289037717597863937, 'sRuxtevt2O9V', '智慧云平台首页', 10, 'default', '智慧云平台首页', 'GET', NULL, '/iot/device/info/dashboard', 0, 1, 0, 1, NULL, NULL, '2020-07-31 11:18:36', '2020-07-31 11:18:36');
INSERT INTO `system_api` VALUES (1289112122743078913, 'Hf2BRHOy4rw5', '分布地址删除', 10, 'default', '分布地址删除', 'DELETE', NULL, '/iot/distribu/remove', 0, 1, 0, 1, NULL, NULL, '2020-07-31 16:14:15', '2020-07-31 16:14:15');
INSERT INTO `system_api` VALUES (1289118906627145729, 'k7noSu0q7LV9', '设备分布地址列表', 10, 'default', '设备分布地址列表', 'GET', NULL, '/iot/distribu/list', 0, 1, 0, 1, NULL, NULL, '2020-07-31 16:41:13', '2020-07-31 16:41:13');
INSERT INTO `system_api` VALUES (1289119286752722945, 'bjibCKF52YXs', '设备信息', 10, 'default', '设备信息', 'GET', NULL, '/iot/device/info', 0, 1, 0, 1, NULL, NULL, '2020-07-31 16:42:43', '2020-07-31 16:42:43');
INSERT INTO `system_api` VALUES (1289134272807976961, 'ZGzaWCDDH2eH', '日志页面', 10, 'default', '日志页面', 'GET', NULL, '/iot/log/page', 0, 1, 0, 1, NULL, NULL, '2020-07-31 17:42:16', '2020-07-31 17:42:16');
INSERT INTO `system_api` VALUES (1289462680020660225, 'D2H8onR5lrjC', '日志上传', 10, 'default', '日志上传', 'GET', NULL, '/iot/log/upload/to/device', 0, 1, 0, 1, NULL, NULL, '2020-08-01 15:27:15', '2020-08-01 15:27:15');
INSERT INTO `system_api` VALUES (1289462916910755842, 'G7WCFLVPsUSb', '日志批量上传', 10, 'default', '日志批量上传', 'GET', NULL, '/iot/log/upload/to/batch', 0, 1, 0, 1, NULL, NULL, '2020-08-01 15:28:11', '2020-08-01 15:28:11');
INSERT INTO `system_api` VALUES (1289483731601534977, 'tAijtPw5cY6x', '升级文件列表', 10, 'default', '升级文件列表', 'GET', NULL, '/iot/disk/file/page', 0, 1, 0, 1, NULL, NULL, '2020-08-01 16:50:54', '2020-08-01 16:50:54');
INSERT INTO `system_api` VALUES (1290106573443641346, 'lleUCzlxW8mn', '设备批量升级', 10, 'default', '设备批量升级', 'POST', NULL, '/iot/device/soft/to/batch', 0, 1, 0, 1, NULL, NULL, '2020-08-03 10:05:51', '2020-08-03 10:05:51');
INSERT INTO `system_api` VALUES (1290942582863507457, 'cUROVFDJOUGc', '日志文件详情', 10, 'default', '日志文件详情', 'GET', NULL, '/iot/log/info', 0, 1, 0, 1, NULL, NULL, '2020-08-05 17:27:51', '2020-08-05 17:27:51');
INSERT INTO `system_api` VALUES (1291185862918352897, 'vZdlNeX8pgSr', '设备删除', 10, 'default', '设备删除', 'DELETE', NULL, '/iot/device/remove', 0, 1, 0, 1, NULL, NULL, '2020-08-06 09:34:33', '2020-08-06 09:34:33');
INSERT INTO `system_api` VALUES (1291256576258158594, 'VEdDB5DgTwxx', '设备上传进度', 10, 'default', '设备上传进度', 'GET', NULL, '/iot/device/progress', 0, 1, 0, 1, NULL, NULL, '2020-08-06 14:15:33', '2020-08-06 14:15:33');
INSERT INTO `system_api` VALUES (1291256719636246529, 'apZ6osb4XYvW', '日志上传进度', 10, 'default', '日志上传进度', 'GET', NULL, '/iot/log/progress', 0, 1, 0, 1, NULL, NULL, '2020-08-06 14:16:07', '2020-08-06 14:16:07');
INSERT INTO `system_api` VALUES (1291293390830383105, 'wQj2gE02czD9', '日志导出', 10, 'default', '日志导出', 'POST', NULL, '/oss/file/package', 0, 1, 0, 1, NULL, NULL, '2020-08-06 16:41:50', '2020-08-06 16:54:42');
INSERT INTO `system_api` VALUES (1291667737629765634, 'ZunmPduZMxDm', '日志批量删除', 10, 'default', '日志批量删除', 'POST', NULL, '/iot/log/remove/batch', 0, 1, 0, 1, NULL, NULL, '2020-08-07 17:29:21', '2020-08-07 17:29:21');
INSERT INTO `system_api` VALUES (1291703099118891010, 'Iv2X11g7arUd', '用户添加', 10, 'default', '用户添加', 'POST', NULL, '/iot/user/save', 0, 1, 0, 1, NULL, NULL, '2020-08-07 19:49:52', '2020-08-07 19:49:52');
INSERT INTO `system_api` VALUES (1291942224656945153, 'Q786TmOdlg7Z', '用户删除', 10, 'default', '用户删除', 'DELETE', NULL, '/iot/user/remove', 0, 1, 0, 1, NULL, NULL, '2020-08-08 11:40:04', '2020-08-08 11:40:04');
INSERT INTO `system_api` VALUES (1291942344358187010, '3elCfv5pxq2u', '用户批量删除', 10, 'default', '用户批量删除', 'POST', NULL, '/iot/use/remove/batch', 0, 1, 0, 1, NULL, NULL, '2020-08-08 11:40:33', '2020-08-08 11:40:33');
INSERT INTO `system_api` VALUES (1291942542509690882, 'iyaEarmhsgVm', '用户角色授权', 10, 'default', '用户角色授权', 'POST', NULL, '/iot/user/roles/save', 0, 1, 0, 1, NULL, NULL, '2020-08-08 11:41:20', '2020-08-08 11:41:20');
INSERT INTO `system_api` VALUES (1291981735912259585, 'pSSyiaGlbzvu', '角色删除', 10, 'default', '角色删除', 'POST', NULL, '/iot/role/remove', 0, 1, 0, 1, NULL, NULL, '2020-08-08 14:17:04', '2020-08-08 14:17:04');
INSERT INTO `system_api` VALUES (1292020598150590465, 'WhVxxqWVh3rk', '分布地址详情', 10, 'default', '分布地址详情', 'GET', NULL, '/iot/distribu/info', 0, 1, 0, 1, NULL, NULL, '2020-08-08 16:51:30', '2020-08-08 16:51:30');
INSERT INTO `system_api` VALUES (1292668979558502402, 'vV3EjcsHDvlJ', '设备列表', 10, 'default', '设备列表', 'GET', NULL, '/iot/device/list', 0, 1, 0, 1, NULL, NULL, '2020-08-10 11:47:56', '2020-08-10 11:47:56');
INSERT INTO `system_api` VALUES (1292745539411378177, 'pmBmvPbAazce', '用户日志', 10, 'default', '用户日志', 'GET', NULL, '/iot/access/log/page', 0, 1, 0, 1, NULL, NULL, '2020-08-10 16:52:09', '2020-08-10 16:52:09');
INSERT INTO `system_api` VALUES (1293094290683113473, 'thLKo5TWVn0y', '分类列表', 10, 'default', '分类列表', 'GET', NULL, '/iot/category/list', 0, 1, 0, 1, NULL, NULL, '2020-08-11 15:57:58', '2020-08-11 15:57:58');
INSERT INTO `system_api` VALUES (1293094411927859202, 'Zz0I4r6Kvi1y', '分类添加', 10, 'default', '分类添加', 'POST', NULL, '/iot/category/save', 0, 1, 0, 1, NULL, NULL, '2020-08-11 15:58:27', '2020-08-11 15:58:27');
INSERT INTO `system_api` VALUES (1293094529615835138, 'smpSCTHSmDzp', '分类删除', 10, 'default', '分类删除', 'POST', NULL, '/iot/category/remove', 0, 1, 0, 1, NULL, NULL, '2020-08-11 15:58:55', '2020-08-11 15:58:55');
INSERT INTO `system_api` VALUES (1293757117068595201, 'cPYM01RUHJh9', '角色保存', 1, 'default', '角色保存', 'POST', NULL, '/admin/role/save', 0, 1, 0, 1, NULL, NULL, '2020-08-13 11:51:48', '2020-08-13 11:51:48');
INSERT INTO `system_api` VALUES (1293826152409972738, 'iZMwFDMtkyZl', '系统用户保存', 1, 'default', '系统用户保存', 'POST', NULL, '/admin/user/save', 0, 1, 0, 1, NULL, NULL, '2020-08-13 16:26:08', '2020-08-13 16:32:09');
INSERT INTO `system_api` VALUES (1295287279385194498, '4XNrBtdKbRdF', '修改用户密码', 10, 'default', '修改用户密码', 'POST', NULL, '/iot/user/update/password', 0, 1, 0, 1, NULL, NULL, '2020-08-17 17:12:07', '2020-08-17 17:12:07');
INSERT INTO `system_api` VALUES (1298903856310185985, '32uu8ORLh091', '设备导入', 10, 'default', '设备导入', 'POST', NULL, '/iot/device/template/export', 0, 1, 0, 1, NULL, NULL, '2020-08-27 16:43:06', '2020-08-27 16:43:55');
INSERT INTO `system_api` VALUES (1298904025558740993, 'FxpYyfiEh2Vs', '设备模版下载', 10, 'default', '设备模版下载', 'GET', NULL, '/iot/device/template/download', 0, 1, 0, 1, NULL, NULL, '2020-08-27 16:43:47', '2020-08-27 16:43:47');
INSERT INTO `system_api` VALUES (1299246800330354690, 'LY43h63Gyh2E', '设备文件删除', 10, 'default', '设备文件删除', 'POST', NULL, '/iot/disk/file/remove', 0, 1, 0, 1, NULL, NULL, '2020-08-28 15:25:51', '2020-08-28 15:25:51');
COMMIT;

-- ----------------------------
-- Table structure for system_category
-- ----------------------------
DROP TABLE IF EXISTS `system_category`;
CREATE TABLE `system_category` (
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `parent_id` bigint DEFAULT '0' COMMENT '分类父ID',
  `tenant_id` bigint DEFAULT NULL COMMENT '分类树形结构',
  `level` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '层数',
  `name` varchar(100) DEFAULT NULL COMMENT '分类名称',
  `status` tinyint DEFAULT '0' COMMENT '状态',
  `sort` int DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `category_type` int DEFAULT NULL,
  PRIMARY KEY (`category_id`),
  KEY `parent_id` (`parent_id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu` (
  `menu_id` bigint NOT NULL COMMENT '菜单Id',
  `parent_id` bigint DEFAULT NULL COMMENT '父级菜单',
  `menu_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '菜单编码',
  `menu_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '菜单名称',
  `menu_type` tinyint NOT NULL COMMENT '菜单类型',
  `menu_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `scheme` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '路径前缀',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '请求路径',
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '菜单标题',
  `target` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '_self' COMMENT '打开方式:_self窗口内,_blank新窗口',
  `priority` int NOT NULL DEFAULT '0' COMMENT '优先级 越小越靠前',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
  `is_persist` tinyint NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  `level` tinyint DEFAULT '2' COMMENT '层级1导航，2，菜单，3，按钮',
  `cache` tinyint DEFAULT '1' COMMENT '0:不缓存，1:缓存',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`menu_id`,`menu_code`,`menu_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统资源-菜单信息';

-- ----------------------------
-- Records of system_menu
-- ----------------------------
BEGIN;
INSERT INTO `system_menu` VALUES (1232280128543265432, 0, 'alarmCenter', '资产', 10, '资产中心', '/', 'assets/center/index', 'icon_alarm_dark', '_self', 9, 1, 0, 1, 0, '2020-02-25 20:24:12', '2020-02-25 20:24:12');
INSERT INTO `system_menu` VALUES (1232280128545436554, 0, 'monitorCenter', '首页', 10, '监控中心', '/', 'monitor/center/index', 'icon_monitor_center_white', '_self', 11, 1, 0, 1, 0, '2020-02-25 20:24:12', '2020-02-25 20:24:12');
INSERT INTO `system_menu` VALUES (1232280128547676564, 0, 'deviceCenter', '设备', 10, '设备中心', '/', 'device/manage/index', 'icon_device_center_dark', '_self', 4, 1, 0, 1, 0, '2020-02-25 20:24:12', '2020-02-25 20:24:12');
INSERT INTO `system_menu` VALUES (1232280128554437643, 1232280128554754657, 'userRole', '角色管理', 10, '角色管理', '/', 'user/role/index', 'icon_authority_management_gray', '_self', 8, 1, 0, 2, 0, '2020-02-25 20:24:12', '2020-07-14 12:02:23');
INSERT INTO `system_menu` VALUES (1232280128554657644, 1232280128554754657, 'userManage', '用户管理', 10, '用户管理', '/', 'user/manage/index', 'icon_user_management', '_self', 9, 1, 0, 2, 0, '2020-02-25 20:24:12', '2020-07-14 12:02:23');
INSERT INTO `system_menu` VALUES (1232280128554754657, 0, 'userCenter', '用户', 10, '用户中心', '/', 'user/manage/index', 'icon_user_management', '_self', 3, 1, 0, 1, 0, '2020-02-25 20:24:12', '2020-07-14 12:02:23');
INSERT INTO `system_menu` VALUES (1232280128555434534, 1232280128554754657, 'userLog', '用户日志', 10, '用户日志', '/', 'user/log/index', 'icon_user_log_gray', '_self', 7, 1, 0, 2, 0, '2020-02-25 20:24:12', '2020-07-14 12:02:23');
INSERT INTO `system_menu` VALUES (1232280128555554535, 1232280128558666456, 'stratcaster', '电吉他', 10, '电吉他', '/', 'music/stratcaster/index', 'icon_userinfo_white', '_self', 5, 1, 0, 2, 0, '2020-02-25 20:24:12', '2020-07-14 12:02:23');
INSERT INTO `system_menu` VALUES (1232280128555676657, 1232280128554754657, 'userProfile', '个人资料', 10, '个人资料', '/', 'user/account/index', 'icon_userinfo_white', '_self', 5, 1, 0, 2, 0, '2020-02-25 20:24:12', '2020-07-14 12:02:23');
INSERT INTO `system_menu` VALUES (1232280128558666456, 0, 'musicCenter', '音乐', 10, '音乐', '/', 'music/manage/index', 'icon_fota_update_white', '_self', 2, 1, 0, 1, 0, '2020-02-25 20:24:12', '2020-07-14 12:02:23');
INSERT INTO `system_menu` VALUES (1283660834362036226, 0, 'workCenter', '蘑菇工作', 10, '蘑菇工作', '/', 'mogu/manage/index', 'icon_device_log_white', '_self', 1, 1, 0, 2, 0, '2020-07-16 15:12:47', '2020-07-16 15:12:47');
INSERT INTO `system_menu` VALUES (1283661175769890765, 1283660834362036226, 'stuCount', '学生清单', 10, '学生清单', '/', 'mogu/student/index', 'icon_log_upload_white', '_self', 1, 1, 0, 2, 0, '2020-07-16 15:14:08', '2020-07-16 15:14:08');
INSERT INTO `system_menu` VALUES (1283661175769993217, 1283660834362036226, 'musicWork', '课程统计', 10, '课程统计', '/', 'mogu/work/index', 'icon_log_upload_white', '_self', 1, 1, 0, 2, 0, '2020-07-16 15:14:08', '2020-07-16 15:14:08');
INSERT INTO `system_menu` VALUES (1283663427784085506, 1283660834362036226, 'musicSalaryManage', '薪资统计', 10, '薪资统计', '/', 'mogu/manage/index', 'icon_export_dark', '_self', 0, 1, 0, 2, 0, '2020-07-16 15:23:05', '2020-07-16 15:23:05');
INSERT INTO `system_menu` VALUES (1283701687364356454, 1283701687365341185, 'distribuAdd', '分布地址添加页面', 10, '分布地址添加页面', '/', 'device/date/add', '', '_self', 0, 1, 0, 3, 0, '2020-07-16 17:55:07', '2020-07-16 17:55:07');
INSERT INTO `system_menu` VALUES (1283701687365341185, 1232280128547676564, 'distribuIndex', '日期', 10, '日期', '/', 'device/date/index', 'icon_location_white', '_self', 0, 1, 0, 2, 0, '2020-07-16 17:55:07', '2020-07-16 17:55:07');
INSERT INTO `system_menu` VALUES (1283942201344345565, 1283942201348001794, 'deviceAdd', '设备添加页面', 10, '设备添加页面', '/', 'device/manage/add', '', '_self', 6, 1, 0, 3, 0, '2020-07-17 09:50:50', '2020-07-14 12:02:23');
INSERT INTO `system_menu` VALUES (1283942201348001794, 1232280128547676564, 'deviceIndex', '设备管理', 10, '设备管理', '/', 'device/manage/index', 'icon_device_center_white', '_self', 6, 1, 0, 2, 0, '2020-07-17 09:50:50', '2020-07-14 12:02:23');
INSERT INTO `system_menu` VALUES (1292030655458242562, 1283942201348001794, 'deviceInfoPage', '设备详情页面', 10, '设备详情页面', '/', 'device/manage/info', '', '_self', 0, 1, 0, 3, 1, '2020-08-08 17:31:28', '2020-08-08 17:31:28');
INSERT INTO `system_menu` VALUES (1293086086939062274, 1232280128547676564, 'categoryManage', '分类管理', 10, '分类管理', '/', 'category/manage/index', 'icon_category_line', '_self', 0, 1, 0, 2, 1, '2020-08-11 15:25:22', '2020-08-11 15:25:22');
INSERT INTO `system_menu` VALUES (1293751292266663938, 0, 'userCenter', '用户中心', 20, '用户中心', '/', '', 'health-yonghuzhongxin', '_self', 90, 1, 0, 1, 1, '2020-08-13 11:28:39', '2020-08-13 11:28:39');
INSERT INTO `system_menu` VALUES (1293751690058649602, 1293751292266663938, 'userManage', '用户管理', 20, '用户管理', '/', 'user/manage/index', 'health-yonghuguanli', '_self', 0, 1, 0, 2, 1, '2020-08-13 11:30:14', '2020-08-13 11:30:14');
INSERT INTO `system_menu` VALUES (1293752108666966018, 0, 'systemCenter', '系统中心', 20, '系统中心', '/', '', 'health-system-copy', '_self', 50, 1, 0, 1, 1, '2020-08-13 11:31:54', '2020-08-13 11:31:54');
INSERT INTO `system_menu` VALUES (1293752319820812289, 1293752108666966018, 'accountManage', '账号管理', 20, '账号管理', '/', 'system/account/manage/index', '', '_self', 4, 1, 0, 2, 1, '2020-08-13 11:32:44', '2020-08-13 17:02:35');
INSERT INTO `system_menu` VALUES (1293752971317858305, 0, 'deviceCenter', '设备中心', 20, '设备中心', '/', '', 'health-shebeiguanli', '_self', 80, 1, 0, 1, 1, '2020-08-13 11:35:20', '2020-08-13 11:35:20');
INSERT INTO `system_menu` VALUES (1293753638946197506, 1293752971317858305, 'versionManage', '版本管理', 20, '版本管理', '/', 'version/manage/index', '', '_self', 0, 1, 0, 2, 1, '2020-08-13 11:37:59', '2020-08-13 11:37:59');
INSERT INTO `system_menu` VALUES (1293754519464824833, 0, 'dataCenter', '数据中心', 20, '数据中心', '/', '', 'health-shujutongji', '_self', 60, 1, 0, 1, 1, '2020-08-13 11:41:29', '2020-08-13 11:41:29');
INSERT INTO `system_menu` VALUES (1293754914840891393, 1293754519464824833, 'userDeviceReport', '用户设备报表', 20, '用户设备报表', '/', 'user/device/report/index', '', '_self', 0, 1, 0, 2, 1, '2020-08-13 11:43:03', '2020-08-13 11:43:03');
INSERT INTO `system_menu` VALUES (1293755541499269122, 1293752108666966018, 'logManage', '日志管理', 20, '日志管理', '/', 'system/log/manage/index', '', '_self', 2, 1, 0, 2, 1, '2020-08-13 11:45:33', '2020-08-13 17:02:43');
INSERT INTO `system_menu` VALUES (1293756276936921090, 0, 'dashboard', '首页', 20, '首页仪表盘', '/', 'home/dashboard/index', 'health-dashboard-line', '_self', 100, 1, 0, 1, 1, '2020-08-13 11:48:28', '2020-08-13 11:48:41');
INSERT INTO `system_menu` VALUES (1296753610633248769, 1293752108666966018, 'roleManage', '角色管理', 20, '角色管理', '/', 'system/role/manage/index', '', '_self', 3, 1, 0, 2, 1, '2020-08-21 18:18:48', '2020-08-21 18:18:48');
INSERT INTO `system_menu` VALUES (1298577194477481985, 1293752108666966018, 'userProfile', '个人资料', 20, '个人资料', '/', 'user/account/index', '', '_self', 0, 1, 0, 2, 1, '2020-08-26 19:05:04', '2020-08-26 19:05:04');
INSERT INTO `system_menu` VALUES (1298578536780591106, 1293753638946197506, 'versionAdd', '版本添加', 20, '版本添加', '/', 'version/manage/add', '', '_self', 0, 1, 0, 3, 1, '2020-08-26 19:10:24', '2020-08-26 19:10:24');
COMMIT;

-- ----------------------------
-- Table structure for system_role
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `role_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '角色编码',
  `role_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '角色名称',
  `role_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '角色描述',
  `tenant_id` bigint DEFAULT NULL COMMENT '角色描述',
  `role_type` tinyint DEFAULT '1' COMMENT '平台类型 1:系统管理平台，10:设备管理平台',
  `status` tinyint DEFAULT '1' COMMENT '类型0为平台',
  `is_system` tinyint DEFAULT '0' COMMENT '是否系统0-否 1-是',
  `is_persist` tinyint DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统角色-基础信息';

-- ----------------------------
-- Records of system_role
-- ----------------------------
BEGIN;
INSERT INTO `system_role` VALUES (1, 'ceshi', '测试角色', '测试角色', NULL, 10, 1, 0, 0, '2020-08-19 14:57:52', '2020-08-19 14:57:52');
INSERT INTO `system_role` VALUES (2, 'device_admin', '系统管理员', '系统管理员', 1, 10, 1, 1, 0, '2020-07-13 10:32:00', '2020-07-13 10:32:00');
COMMIT;

-- ----------------------------
-- Table structure for system_role_action
-- ----------------------------
DROP TABLE IF EXISTS `system_role_action`;
CREATE TABLE `system_role_action` (
  `action_id` bigint NOT NULL COMMENT '操作ID',
  `role_id` bigint NOT NULL COMMENT 'API',
  PRIMARY KEY (`action_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统权限-角色功能操作关联表';

-- ----------------------------
-- Records of system_role_action
-- ----------------------------
BEGIN;
INSERT INTO `system_role_action` VALUES (1283305587120500738, 2);
INSERT INTO `system_role_action` VALUES (1283320434906329089, 2);
INSERT INTO `system_role_action` VALUES (1283604397850038274, 2);
INSERT INTO `system_role_action` VALUES (1283696331994107906, 2);
INSERT INTO `system_role_action` VALUES (1283954855995183105, 2);
INSERT INTO `system_role_action` VALUES (1284422873434464354, 1);
INSERT INTO `system_role_action` VALUES (1284422873434464354, 2);
INSERT INTO `system_role_action` VALUES (1284422873656765545, 2);
INSERT INTO `system_role_action` VALUES (1284422873756576547, 2);
INSERT INTO `system_role_action` VALUES (1284422873764435435, 2);
INSERT INTO `system_role_action` VALUES (1288314965249318914, 2);
INSERT INTO `system_role_action` VALUES (1288392564457652225, 2);
INSERT INTO `system_role_action` VALUES (1288734387655004162, 2);
INSERT INTO `system_role_action` VALUES (1288777336436011010, 2);
INSERT INTO `system_role_action` VALUES (1289037869339394050, 1);
INSERT INTO `system_role_action` VALUES (1289037869339394050, 2);
INSERT INTO `system_role_action` VALUES (1289111578234339329, 2);
INSERT INTO `system_role_action` VALUES (1289118750976524290, 2);
INSERT INTO `system_role_action` VALUES (1289134103249043458, 1);
INSERT INTO `system_role_action` VALUES (1289134103249043458, 2);
INSERT INTO `system_role_action` VALUES (1289463070820741121, 1);
INSERT INTO `system_role_action` VALUES (1289463070820741121, 2);
INSERT INTO `system_role_action` VALUES (1291185709192916994, 2);
INSERT INTO `system_role_action` VALUES (1291293505343270913, 1);
INSERT INTO `system_role_action` VALUES (1291293505343270913, 2);
INSERT INTO `system_role_action` VALUES (1291667020533805058, 1);
INSERT INTO `system_role_action` VALUES (1291667020533805058, 2);
INSERT INTO `system_role_action` VALUES (1291703210720931842, 2);
INSERT INTO `system_role_action` VALUES (1291942104561438722, 2);
INSERT INTO `system_role_action` VALUES (1291942908466909186, 2);
INSERT INTO `system_role_action` VALUES (1291981559378198530, 2);
INSERT INTO `system_role_action` VALUES (1292669145967513602, 2);
INSERT INTO `system_role_action` VALUES (1292745665852866561, 2);
INSERT INTO `system_role_action` VALUES (1293086261728292866, 2);
INSERT INTO `system_role_action` VALUES (1293086338400169985, 2);
INSERT INTO `system_role_action` VALUES (1293096666877964289, 2);
INSERT INTO `system_role_action` VALUES (1295287486197936129, 2);
INSERT INTO `system_role_action` VALUES (1299246684055859201, 1);
COMMIT;

-- ----------------------------
-- Table structure for system_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_role_menu`;
CREATE TABLE `system_role_menu` (
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`menu_id`,`role_id`),
  KEY `fk_user` (`menu_id`) USING BTREE,
  KEY `fk_role` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统菜单-角色关联';

-- ----------------------------
-- Records of system_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `system_role_menu` VALUES (1232280128543265432, 2);
INSERT INTO `system_role_menu` VALUES (1232280128545436554, 1);
INSERT INTO `system_role_menu` VALUES (1232280128545436554, 2);
INSERT INTO `system_role_menu` VALUES (1232280128547676564, 2);
INSERT INTO `system_role_menu` VALUES (1232280128554437643, 2);
INSERT INTO `system_role_menu` VALUES (1232280128554657644, 2);
INSERT INTO `system_role_menu` VALUES (1232280128554754657, 2);
INSERT INTO `system_role_menu` VALUES (1232280128555434534, 2);
INSERT INTO `system_role_menu` VALUES (1232280128555554535, 1);
INSERT INTO `system_role_menu` VALUES (1232280128555554535, 2);
INSERT INTO `system_role_menu` VALUES (1232280128555676657, 2);
INSERT INTO `system_role_menu` VALUES (1232280128555777778, 2);
INSERT INTO `system_role_menu` VALUES (1232280128558666456, 1);
INSERT INTO `system_role_menu` VALUES (1232280128558666456, 2);
INSERT INTO `system_role_menu` VALUES (1283660834362036226, 1);
INSERT INTO `system_role_menu` VALUES (1283660834362036226, 2);
INSERT INTO `system_role_menu` VALUES (1283661175769890765, 1);
INSERT INTO `system_role_menu` VALUES (1283661175769890765, 2);
INSERT INTO `system_role_menu` VALUES (1283661175769993217, 1);
INSERT INTO `system_role_menu` VALUES (1283661175769993217, 2);
INSERT INTO `system_role_menu` VALUES (1283663427784085506, 1);
INSERT INTO `system_role_menu` VALUES (1283663427784085506, 2);
INSERT INTO `system_role_menu` VALUES (1283701687364356454, 2);
INSERT INTO `system_role_menu` VALUES (1283701687365341185, 2);
INSERT INTO `system_role_menu` VALUES (1283942201344345565, 2);
INSERT INTO `system_role_menu` VALUES (1283942201348001794, 2);
INSERT INTO `system_role_menu` VALUES (1292030655458242562, 2);
INSERT INTO `system_role_menu` VALUES (1293086086939062274, 2);
INSERT INTO `system_role_menu` VALUES (1300329554874789890, 2);
COMMIT;

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `account_id` bigint DEFAULT NULL COMMENT '用户ID',
  `user_name` varchar(255) DEFAULT NULL COMMENT '登陆账号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT '' COMMENT '头像',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号',
  `user_type` varchar(20) DEFAULT 'normal' COMMENT '用户类型:super-超级管理员 normal-普通管理员',
  `type` tinyint DEFAULT NULL COMMENT '1:系统，10供应商，20服务商，30采购商，40仓库，50',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `user_desc` varchar(255) DEFAULT '' COMMENT '描述',
  `status` tinyint DEFAULT '1' COMMENT '状态:0-禁用 1-正常 2-锁定',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户-管理员信息';

-- ----------------------------
-- Records of system_user
-- ----------------------------
BEGIN;
INSERT INTO `system_user` VALUES (521677655436576554, 521677655332356543, '魏其森', '魏其森', '', '1277821959@qq.com', '中国', '15219190193', 'normal', 10, 2, 'building!!!', 1, '2020-07-12 13:20:45', '2020-12-10 00:36:27');
INSERT INTO `system_user` VALUES (1336715933976227842, 1336715934148194306, 'test', 'tc', '', '15219190193@163.com', NULL, '15219190193', 'normal', 10, NULL, '测试账号，密码tctest', 1, '2020-12-10 00:54:48', '2020-12-11 14:13:19');
COMMIT;

-- ----------------------------
-- Table structure for system_user_role
-- ----------------------------
DROP TABLE IF EXISTS `system_user_role`;
CREATE TABLE `system_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `fk_user` (`user_id`) USING BTREE,
  KEY `fk_role` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统角色-用户关联';

-- ----------------------------
-- Records of system_user_role
-- ----------------------------
BEGIN;
INSERT INTO `system_user_role` VALUES (521677655436576554, 2);
INSERT INTO `system_user_role` VALUES (1336715933976227842, 1);
COMMIT;

-- ----------------------------
-- Table structure for tc_device
-- ----------------------------
DROP TABLE IF EXISTS `tc_device`;
CREATE TABLE `tc_device` (
  `device_id` bigint NOT NULL COMMENT '设备id',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `device_rom` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备规格',
  `device_bug` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备缺陷',
  `device_new` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备成色',
  `category_name` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备类型',
  `device_name` varchar(55) DEFAULT NULL COMMENT '设备名称',
  `device_sn` varchar(60) DEFAULT NULL COMMENT '设备序列号',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of tc_device
-- ----------------------------
BEGIN;
INSERT INTO `tc_device` VALUES (1295973124576509954, NULL, '2/64', '边角磨损', '95%', '测试分类', 'Surface3', '32343464743', '2020-08-19 14:37:26', '2020-08-19 14:37:26');
INSERT INTO `tc_device` VALUES (1295982761996623873, NULL, '4/128', 'A面划痕', '95%', '测试分类', 'Surface pro5 ', '32343464743', '2020-08-19 15:15:43', '2020-11-11 14:47:07');
INSERT INTO `tc_device` VALUES (1326695232087076865, NULL, '4/128', '屏幕星空屏', '90%', '', 'surface3', '32343464743', '2020-11-12 09:16:07', '2020-11-12 09:16:07');
COMMIT;

-- ----------------------------
-- Table structure for tc_music_lesson
-- ----------------------------
DROP TABLE IF EXISTS `tc_music_lesson`;
CREATE TABLE `tc_music_lesson` (
  `lesson_id` bigint NOT NULL,
  `stu_name` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `teacher_name` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `lesson_money` int DEFAULT NULL,
  `lesson_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `type` varchar(55) DEFAULT NULL,
  `lesson_time` varchar(0) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `homework` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`lesson_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for tc_music_salary
-- ----------------------------
DROP TABLE IF EXISTS `tc_music_salary`;
CREATE TABLE `tc_music_salary` (
  `salary_id` bigint NOT NULL COMMENT '薪水id',
  `payer` varchar(55) DEFAULT NULL COMMENT '发放人',
  `receiver` varchar(55) DEFAULT NULL COMMENT '收款人',
  `money` double DEFAULT NULL COMMENT '金额',
  `type` varchar(55) DEFAULT NULL COMMENT '类型',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`salary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;
