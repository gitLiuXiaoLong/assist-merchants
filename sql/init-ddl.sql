DROP TABLE IF EXISTS `core_weapp_visitor`;
CREATE TABLE `core_weapp_visitor` (
  `openid` varchar(30) CHARACTER SET utf8 NOT NULL COMMENT '小程序openid',
  `union_id` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT 'unionId',
  `session_key` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `nick_name` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `gender` varchar(2) CHARACTER SET utf8 DEFAULT NULL COMMENT '性别',
  `city` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '市',
  `province` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '省',
  `country` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '国',
  `avatar_url` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '头像',
  `token` varchar(40) CHARACTER SET utf8 DEFAULT NULL COMMENT '小程序验证用token',
  `creater` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
  `ctdt` datetime DEFAULT NULL COMMENT '创建时间',
  `updater` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '更新人',
  `updt` datetime DEFAULT NULL COMMENT '更新时间',

  PRIMARY KEY (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='微信小程序游客openid临时保存';
create index idx_core_weapp_visitor_token on core_weapp_visitor(token);

DROP TABLE IF EXISTS `core_module`;
CREATE TABLE `core_module` (
  `module_no` varchar(10) NOT NULL COMMENT '模块NO',
  `order_by` decimal(5,0) NOT NULL COMMENT '排序',
  `module_name` varchar(30) NOT NULL COMMENT '菜单名称',
  `page` varchar(50) DEFAULT NULL COMMENT '页面',
  `del_flg` char(1) DEFAULT '0' COMMENT '删除FLG',
  PRIMARY KEY (`module_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='模块表';


DROP TABLE IF EXISTS `core_menu`;
CREATE TABLE `core_menu` (
  `module_no` varchar(10) NOT NULL COMMENT '模块NO',
  `menu_no` varchar(10) NOT NULL COMMENT '菜单NO',
  `order_by` decimal(5,0) NOT NULL COMMENT '排序',
  `menu_name` varchar(30) NOT NULL COMMENT '菜单名称',
  `page` varchar(100) DEFAULT NULL COMMENT '页面',
  `del_flg` char(1) DEFAULT '0' COMMENT '删除FLG',
  PRIMARY KEY (`module_no`,`menu_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='菜单表';

DROP TABLE IF EXISTS `core_role`;
CREATE TABLE `core_role` (
  `role_no` varchar(10) NOT NULL COMMENT '角色NO',
  `order_by` decimal(5,0) NOT NULL COMMENT '排序',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `del_flg` char(1) DEFAULT '0' COMMENT '删除FLG',
  PRIMARY KEY (`role_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='角色表';

DROP TABLE IF EXISTS `core_role_menu`;
CREATE TABLE `core_role_menu` (
  `role_no` varchar(10) NOT NULL COMMENT '角色NO',
  `menu_no` varchar(10) NOT NULL COMMENT '菜单NO',
  PRIMARY KEY (`role_no`,`menu_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='角色菜单关系表';

DROP TABLE IF EXISTS `core_user_role`;
CREATE TABLE `core_user_role` (
  `user_id` int(10) NOT NULL COMMENT '用户编号',
  `role_no` varchar(10) NOT NULL COMMENT '角色NO',
  PRIMARY KEY (`user_id`,`role_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='用户角色表';

DROP TABLE IF EXISTS `core_user`;
CREATE TABLE `core_user` (
  `user_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `tel` varchar(11) NOT NULL COMMENT '电话',
  `openid` varchar(30) DEFAULT NULL COMMENT '小程序openid',
  `status` enum('working','leaving','holiday','lock') DEFAULT 'working' COMMENT '用户状态（在职/离职/休假/锁定）',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='用户表';

DROP TABLE IF EXISTS `core_company`;
CREATE TABLE `core_company` (
  `company_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '公司编号',
  `name` varchar(50) NOT NULL COMMENT '公司名',
  `banner` varchar(100) NOT NULL COMMENT 'banner图片',
  `logo` varchar(100) NOT NULL COMMENT 'logo图片',
  `comment` varchar(100) NOT NULL COMMENT '公司说明',
  `card_invitation_code` varchar(10) NOT NULL COMMENT '名片邀请码',
  `del_flg` char(1) DEFAULT '0' COMMENT '删除FLG',
  PRIMARY KEY (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='公司表';