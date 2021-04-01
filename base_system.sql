/*
 Navicat Premium Data Transfer

 Source Server Type    : MySQL
 Source Server Version : 50564
 Source Schema         : base_system

 Target Server Type    : MySQL
 Target Server Version : 50564
 File Encoding         : 65001

 Date: 14/03/2020 22:18:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for x_dict
-- ----------------------------
DROP TABLE IF EXISTS `x_dict`;
CREATE TABLE `x_dict` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(40) NOT NULL COMMENT '标识',
  `label` varchar(50) NOT NULL COMMENT '标签',
  `value` varchar(150) NOT NULL COMMENT '值',
  `active` bit(1) NOT NULL DEFAULT b'1' COMMENT '启用',
  `editable` bit(1) NOT NULL DEFAULT b'1' COMMENT '可编辑',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UK_X_DICT_CODE_LABEL` (`code`,`label`) USING BTREE,
  UNIQUE KEY `UK_X_DICT_CODE_VALUE` (`code`,`value`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典表';

-- ----------------------------
-- Records of x_dict
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for x_menu
-- ----------------------------
DROP TABLE IF EXISTS `x_menu`;
CREATE TABLE `x_menu` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '父ID',
  `name` varchar(20) NOT NULL COMMENT '名称',
  `type` tinyint(1) NOT NULL COMMENT '类型（0：目录；1：页面；2：按钮）',
  `sort` int(4) NOT NULL DEFAULT '0' COMMENT '排序值',
  `component` varchar(200) DEFAULT NULL COMMENT '组件名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `icon` varchar(200) DEFAULT NULL COMMENT '图标',
  `perms` varchar(200) DEFAULT NULL COMMENT '权限标识',
  `url` varchar(200) DEFAULT NULL COMMENT '请求地址',
  `active` bit(1) NOT NULL DEFAULT b'1' COMMENT '启用',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- ----------------------------
-- Records of x_menu
-- ----------------------------
BEGIN;
INSERT INTO `x_menu` VALUES (1, 0, '基础信息管理', 0, 0, 'Layout', '基础信息管理', 'list', NULL, '/base', b'1', '2019-12-22 01:04:57', '2019-12-29 04:21:24');
INSERT INTO `x_menu` VALUES (2, 1, '用户管理', 1, 0, 'User', '用户管理', '', 'sys:user', '/user', b'1', '2019-12-22 01:05:36', '2020-01-01 12:52:57');
INSERT INTO `x_menu` VALUES (3, 2, '增加', 2, 0, NULL, '用户管理-增加', NULL, 'sys:user:add', NULL, b'1', '2019-12-26 05:09:05', '2020-01-01 12:53:02');
INSERT INTO `x_menu` VALUES (4, 2, '删除', 2, 2, NULL, '用户管理-删除', NULL, 'sys:user:delete', NULL, b'1', '2019-12-26 05:09:08', '2019-12-26 05:09:10');
INSERT INTO `x_menu` VALUES (5, 2, '修改', 2, 0, NULL, '用户管理-修改', NULL, 'sys:user:update', NULL, b'1', '2019-12-26 05:09:14', '2019-12-26 05:09:15');
INSERT INTO `x_menu` VALUES (6, 1, '角色管理', 1, 1, 'Role', '角色管理', '', 'sys:role', '/role', b'1', '2019-12-26 05:09:12', '2019-12-29 05:07:13');
INSERT INTO `x_menu` VALUES (7, 6, '增加', 2, 0, NULL, '角色管理-增加', NULL, 'sys:role:add', NULL, b'1', '2019-12-26 05:09:19', '2019-12-28 07:26:08');
INSERT INTO `x_menu` VALUES (8, 6, '删除', 2, 0, NULL, '角色管理-删除', NULL, 'sys:role:delete', NULL, b'1', '2019-12-26 05:09:20', '2019-12-26 05:09:22');
INSERT INTO `x_menu` VALUES (9, 6, '修改', 2, 0, NULL, '角色管理-修改', NULL, 'sys:role:update', NULL, b'1', '2019-12-26 05:09:25', '2020-01-11 16:55:32');
INSERT INTO `x_menu` VALUES (10, 6, '修改权限', 2, 0, NULL, '角色管理-修改权限', NULL, 'sys:role:perms', NULL, b'1', '2019-12-26 05:09:25', '2019-12-26 05:09:25');
INSERT INTO `x_menu` VALUES (11, 1, '节点管理', 1, 2, 'Menu', '菜单管理', '', 'sys:menu', '/menu', b'1', '2019-12-26 05:09:31', '2019-12-29 05:09:30');
INSERT INTO `x_menu` VALUES (12, 11, '增加', 2, 0, NULL, '菜单管理-增加', NULL, 'sys:menu:add', NULL, b'1', '2019-12-26 05:09:28', '2019-12-26 05:09:29');
INSERT INTO `x_menu` VALUES (13, 11, '删除', 2, 0, NULL, '菜单管理-删除', NULL, 'sys:menu:delete', NULL, b'1', '2019-12-26 05:09:34', '2019-12-26 05:09:36');
INSERT INTO `x_menu` VALUES (14, 11, '修改', 2, 0, NULL, '菜单管理-修改', NULL, 'sys:menu:update', NULL, b'1', '2019-12-26 05:09:37', '2019-12-26 05:09:39');
INSERT INTO `x_menu` VALUES (15, 1, '字典管理', 1, 0, 'Dict', '', '', 'sys:dict', '/dict', b'1', '2020-01-01 08:40:01', '2020-01-01 09:06:03');
INSERT INTO `x_menu` VALUES (16, 15, '增加', 2, 0, '', '字典管理-增加', '', 'sys:dict:add', '', b'1', '2020-01-01 08:41:23', '2020-01-01 08:41:23');
INSERT INTO `x_menu` VALUES (17, 15, '删除', 2, 0, '', '字典管理-删除', '', 'sys:dict:delete', '', b'1', '2020-01-01 08:41:45', '2020-01-01 08:41:45');
INSERT INTO `x_menu` VALUES (18, 15, '修改', 2, 0, '', '字典管理-修改', '', 'sys:dict:update', '', b'1', '2020-01-01 08:42:03', '2020-01-01 08:42:03');
COMMIT;

-- ----------------------------
-- Table structure for x_role
-- ----------------------------
DROP TABLE IF EXISTS `x_role`;
CREATE TABLE `x_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) NOT NULL COMMENT '名称',
  `role` varchar(200) NOT NULL COMMENT '角色标识',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `active` bit(1) NOT NULL DEFAULT b'1' COMMENT '启用',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Records of x_role
-- ----------------------------
BEGIN;
INSERT INTO `x_role` VALUES (1, 'admin', 'admin', '管理员', b'1', '2020-01-20 23:22:13', '2020-01-20 23:22:17');
COMMIT;

-- ----------------------------
-- Table structure for x_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `x_role_menu`;
CREATE TABLE `x_role_menu` (
  `role_id` bigint(20) unsigned NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) unsigned NOT NULL COMMENT '菜单ID',
  KEY `FK_X_ROLE_MENU_ROLE_ID` (`role_id`),
  KEY `FK_X_ROLE_MENU_MENU_ID` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- ----------------------------
-- Records of x_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `x_role_menu` VALUES (1, 1);
INSERT INTO `x_role_menu` VALUES (1, 2);
INSERT INTO `x_role_menu` VALUES (1, 3);
INSERT INTO `x_role_menu` VALUES (1, 4);
INSERT INTO `x_role_menu` VALUES (1, 5);
INSERT INTO `x_role_menu` VALUES (1, 6);
INSERT INTO `x_role_menu` VALUES (1, 7);
INSERT INTO `x_role_menu` VALUES (1, 8);
INSERT INTO `x_role_menu` VALUES (1, 9);
INSERT INTO `x_role_menu` VALUES (1, 10);
INSERT INTO `x_role_menu` VALUES (1, 11);
INSERT INTO `x_role_menu` VALUES (1, 12);
INSERT INTO `x_role_menu` VALUES (1, 13);
INSERT INTO `x_role_menu` VALUES (1, 14);
INSERT INTO `x_role_menu` VALUES (1, 15);
INSERT INTO `x_role_menu` VALUES (1, 16);
INSERT INTO `x_role_menu` VALUES (1, 17);
INSERT INTO `x_role_menu` VALUES (1, 18);
COMMIT;

-- ----------------------------
-- Table structure for x_user
-- ----------------------------
DROP TABLE IF EXISTS `x_user`;
CREATE TABLE `x_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `real_name` varchar(20) DEFAULT NULL COMMENT '真实姓名',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像地址',
  `salty` int(6) NOT NULL COMMENT '盐值',
  `active` bit(1) NOT NULL DEFAULT b'1' COMMENT '启用',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '已删除',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Records of x_user
-- ----------------------------
BEGIN;
INSERT INTO `x_user` VALUES (1, 'admin', '81ab692038d219ecfd3939fb21e8e0e6', '18222222222', '开发者', '', 568959, b'1', b'0', '2020-01-10 18:01:34', '2020-01-10 18:01:36');
COMMIT;

-- ----------------------------
-- Table structure for x_user_role
-- ----------------------------
DROP TABLE IF EXISTS `x_user_role`;
CREATE TABLE `x_user_role` (
  `user_id` bigint(20) unsigned NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) unsigned NOT NULL COMMENT '角色ID',
  KEY `FK_X_USER_ROLE_ROLE_ID` (`role_id`),
  KEY `FK_X_USER_ROLE_USER_ID` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ----------------------------
-- Records of x_user_role
-- ----------------------------
BEGIN;
INSERT INTO `x_user_role` VALUES (1, 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
