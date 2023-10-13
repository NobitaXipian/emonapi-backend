-- 创建库
create database if not exists emonapi;

-- 切换库
use emonapi;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userName     varchar(256)                           null comment '用户昵称',
    userAccount  varchar(256)                           not null comment '账号',
    userAvatar   varchar(1024)                          null comment '用户头像',
    gender       tinyint                                null comment '性别',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user / admin',
    userPassword varchar(512)                           not null comment '密码',
    `accessKey` varchar(512) not null comment 'accessKey',
    `secretKey` varchar(512) not null comment 'secretKey',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    constraint uni_userAccount
    unique (userAccount)
    ) comment '用户';

INSERT INTO `user` VALUES ('1', 'gaomu', 'gaomu', null, null, 'user', '78e99c2b820b83b2fbb64dbccb1311b5', 'fb0e8ba8cae5fdef8d321191517e61ec', '80d0d0daef59758438523e6556e13036', '2023-10-12 21:34:14', '2023-10-13 03:24:54', '0');
INSERT INTO `user` VALUES ('2', null, 'admin', null, null, 'admin', '78e99c2b820b83b2fbb64dbccb1311b5', '9d868b9eb9148a8fee73b4c7d3ddf2c0', '74a7e15239426151b862f77b55d545a7', '2023-10-13 03:44:11', '2023-10-13 03:44:36', '0');


-- 接口信息
create table if not exists emonapi.`interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `name` varchar(256) not null comment '名称',
    `description` varchar(256) null comment '描述',
    `url` varchar(512) not null comment '接口地址',
    `requestParams` text not null comment '请求参数',
    `requestHeader` text null comment '请求头',
    `responseHeader` text null comment '响应头',
    `status` int default 0 not null comment '接口状态（0-关闭，1-开启）',
    `method` varchar(256) not null comment '请求类型',
    `userId` bigint not null comment '创建人',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
    ) comment '接口信息';


INSERT INTO `interface_info` VALUES ('1', '测试数据-无法使用', 'LN0G', 'www.newton-rolfson.com', '测试', '测试', '测试', '0', 'GET', '1', '2023-10-13 15:02:17', '2023-10-13 15:02:29', '0');
INSERT INTO `interface_info` VALUES ('2', 'getUsernameByPost', '获取你的名字', 'http://localhost:8123/api/name/user', '[\n    {\"name\": \"username\", \"type\": \"string\"}\n]', '{\"Content-Type\": \"application/json\"}', '{\"Content-Type\": \"application/json\"}', '1', 'POST', '2', '2023-10-06 19:26:18', '2023-10-13 03:07:27', '0');
INSERT INTO `interface_info` VALUES ('3', '测试数据-无法使用', 'H44H', 'www.hyon-gorczany.co', '测试', '测试', '测试', '0', 'GET', '1', '2023-10-13 15:02:17', '2023-10-13 15:02:34', '0');
INSERT INTO `interface_info` VALUES ('6', '测试数据-无法使用', '8R', 'www.mertie-abernathy.org', '测试', '测试', '测试', '0', 'GET', '1', '2023-10-13 15:02:17', '2023-10-13 15:02:17', '0');
INSERT INTO `interface_info` VALUES ('7', '测试数据-无法使用', 'ft5Vx', 'www.ashlea-mosciski.net', '测试', '测试', '测试', '0', 'GET', '1', '2023-10-13 15:02:17', '2023-10-13 15:02:17', '0');
INSERT INTO `interface_info` VALUES ('8', '测试数据-无法使用', 'aPagp', 'www.patricia-harris.com', '测试', '测试', '测试', '0', 'GET', '1', '2023-10-13 15:02:18', '2023-10-13 15:02:18', '0');
INSERT INTO `interface_info` VALUES ('9', '测试数据-无法使用', 'sM2S', 'www.reid-greenfelder.net', '测试', '测试', '测试', '0', 'GET', '1', '2023-10-13 15:02:18', '2023-10-13 15:02:18', '0');
INSERT INTO `interface_info` VALUES ('10', '测试数据-无法使用', 'tI', 'www.boyd-bernier.io', '测试', '测试', '测试', '0', 'GET', '1', '2023-10-13 15:02:18', '2023-10-13 15:02:18', '0');
INSERT INTO `interface_info` VALUES ('11', '测试数据-无法使用', '8s', 'www.gaylord-heller.co', '测试', '测试', '测试', '0', 'GET', '1', '2023-10-13 15:02:18', '2023-10-13 15:02:18', '0');
INSERT INTO `interface_info` VALUES ('12', '测试数据-无法使用', 'Hh8', 'www.chong-fritsch.info', '测试', '测试', '测试', '0', 'GET', '1', '2023-10-13 15:02:18', '2023-10-13 15:02:18', '0');
INSERT INTO `interface_info` VALUES ('13', '测试数据-无法使用', 'Dj', 'www.lloyd-stiedemann.name', '测试', '测试', '测试', '0', 'GET', '1', '2023-10-13 15:02:18', '2023-10-13 15:02:18', '0');


-- 用户调用接口关系表
create table if not exists emonapi.`user_interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `userId` bigint not null comment '调用用户 id',
    `interfaceInfoId` bigint not null comment '接口 id',
    `totalNum` int default 0 not null comment '总调用次数',
    `leftNum` int default 0 not null comment '剩余调用次数',
    `status` int default 0 not null comment '0-正常，1-禁用',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户调用接口关系';

INSERT INTO `user_interface_info` VALUES ('1', '1', '1', '23', '7', '0', '2023-10-12 00:42:29', '2023-10-12 16:51:25', '0');
INSERT INTO `user_interface_info` VALUES ('2', '3', '1', '0', '100000', '0', '2023-10-12 03:15:20', '2023-10-12 03:15:20', '0');
INSERT INTO `user_interface_info` VALUES ('3', '2', '2', '3', '99999', '0', '2023-10-12 04:31:27', '2023-10-12 04:42:29', '0');
INSERT INTO `user_interface_info` VALUES ('4', '3', '2', '18', '99995', '0', '2023-10-12 04:31:53', '2023-10-13 03:49:05', '0');
INSERT INTO `user_interface_info` VALUES ('5', '2', '3', '3', '3', '0', '2023-10-12 04:32:04', '2023-10-12 04:42:40', '0');
INSERT INTO `user_interface_info` VALUES ('6', '3', '3', '30', '10000', '0', '2023-10-12 04:32:27', '2023-10-12 04:32:27', '0');
INSERT INTO `user_interface_info` VALUES ('7', '4', '2', '6', '99994', '0', '2023-10-13 03:24:08', '2023-10-13 03:43:12', '0');
