/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost
 Source Database       : haohao

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : utf-8

 Date: 12/16/2019 17:22:03 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `sys_app`
-- ----------------------------
DROP TABLE IF EXISTS `sys_app`;
CREATE TABLE `sys_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `app_key` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT 'client_key(unique)',
  `app_secret` varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT 'clien_secret',
  `url` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `ip` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT 'ip list',
  `index_path` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '/',
  `callback_path` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '/sso/callback',
  `locked` tinyint(1) unsigned zerofill NOT NULL DEFAULT '0' COMMENT 'locked',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'gmt_create',
  `gmt_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'gmt_update',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_app_key` (`app_key`),
  UNIQUE KEY `UNIQUE_app_name` (`app_name`),
  UNIQUE KEY `UNIQUE_app_secret` (`app_secret`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='接入应用';

-- ----------------------------
--  Records of `sys_app`
-- ----------------------------
BEGIN;
INSERT INTO `sys_app` VALUES ('1', 'haohao-upm', '64fbc60bfacbfef24d76a68d15b85fb0', 'a57d43d82ab30e980800967694cc1727', 'http://haohao-upm:8384', '127.0.0.1', '/', '/sso/callback', '0', '2019-11-28 13:02:58', '2019-12-06 15:22:11'), ('2', 'web-sample', '64fbc60bfacbfef24d76a68d15b85fb1', 'a57d43d82ab30e980800967694cc1728', 'http://hansoul-sample-web1:8381', '172.16.18.101,172.16.18.102', '/', '/sso/callback', '0', '2019-11-21 17:17:22', '2019-12-06 18:45:41'), ('3', 'web-sample2', '64fbc60bfacbfef24d76a68d15b85fb2', 'a57d43d82ab30e980800967694cc1729', 'http://hansoul-sample-web2:8382', '172.16.18.101,172.16.18.102', '/', '/sso/callback', '0', '2019-11-25 10:49:48', '2019-12-06 18:45:41'), ('4', 'rest-sample', '64fbc60bfacbfef24d76a68d15b85fb3', 'a57d43d82ab30e980800967694cc1730', 'http://hansoul-sample-rest:8383', '127.0.0.1', '/', '/sso/callback', '0', '2019-12-02 22:28:59', '2019-12-06 18:45:45'), ('5', 'testweb', 'abcd', 'abcd1234', 'http://localhost:8888', 'localhost', '/', '/sso/callback', '0', '2019-12-12 15:39:58', '2019-12-12 15:39:58');
COMMIT;

-- ----------------------------
--  Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `merchant_code` varchar(45) COLLATE utf8mb4_bin NOT NULL,
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `login_name` varchar(30) COLLATE utf8mb4_bin NOT NULL COMMENT '登录账号',
  `user_name` varchar(30) COLLATE utf8mb4_bin NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) COLLATE utf8mb4_bin DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) COLLATE utf8mb4_bin DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) COLLATE utf8mb4_bin DEFAULT '' COMMENT '手机号码',
  `sex` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) COLLATE utf8mb4_bin DEFAULT '' COMMENT '头像路径',
  `credential` varchar(128) COLLATE utf8mb4_bin DEFAULT '' COMMENT '密码',
  `salt` varchar(64) COLLATE utf8mb4_bin DEFAULT '' COMMENT '盐加密',
  `status` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(50) COLLATE utf8mb4_bin DEFAULT '' COMMENT '最后登陆IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `create_by` varchar(64) COLLATE utf8mb4_bin DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_bin DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户信息表';

-- ----------------------------
--  Records of `sys_user`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES ('1', 'hanshow', '103', 'admin', 'Admin', '00', 'aaasdf@163.com', '15888888888', '0', '/profile/avatar/2019/11/04/57f341ee666f7d0bbdb10033d2001fff.png', 'a5613710a0e8fce92086e17518fecf46cfb5b031c7b1211758b53cc37eae8b07b02c921702239dadfb6c6dd74e077869e63c940bb01ee937bda8184430410d0d', '6e859b04aba7ed565953bdb328a5b11f', '0', '0', '127.0.0.1', '2019-11-16 00:17:10', 'admin', '2018-03-16 11:33:00', 'ry', '2019-12-04 15:53:57', '管理员'), ('2', 'hanshow', '105', 'test001', 'test001', '00', 'ry@qq.com', '15666666666', '1', '', '9a0f86c5931d19924a729ba79a30b3b1', '6e6b72', '0', '0', '127.0.0.1', '2019-11-15 21:38:52', 'admin', '2018-03-16 11:33:00', 'ry', '2019-12-04 15:53:57', '测试员'), ('3', 'hanshow', '103', 'hansoul', 'hansoul', '00', 'asdf@asdc.com', '', '1', '', 'a5613710a0e8fce92086e17518fecf46cfb5b031c7b1211758b53cc37eae8b07b02c921702239dadfb6c6dd74e077869e63c940bb01ee937bda8184430410d0d', '6e859b04aba7ed565953bdb328a5b11f', '0', '0', '', null, '', '2019-11-19 16:44:46', '', '2019-12-04 15:53:57', null);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
