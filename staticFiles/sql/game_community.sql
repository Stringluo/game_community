/*
 Navicat MySQL Data Transfer

 Source Server         : luo
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : game_community

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 13/04/2023 21:29:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_action
-- ----------------------------
DROP TABLE IF EXISTS `tb_action`;
CREATE TABLE `tb_action`  (
  `action_id` int NOT NULL AUTO_INCREMENT COMMENT '操作ID',
  `user_id` int NOT NULL COMMENT '操作用户ID',
  `post_id` int NULL DEFAULT NULL COMMENT '帖子ID',
  `comment_id` int NULL DEFAULT NULL COMMENT '评论ID',
  `action_category_id` int NULL DEFAULT NULL COMMENT '操作类别ID',
  `action_time` datetime NOT NULL COMMENT '用户操作时间',
  PRIMARY KEY (`action_id`) USING BTREE,
  INDEX `action_user_id`(`user_id`) USING BTREE,
  INDEX `action_post_id`(`post_id`) USING BTREE,
  INDEX `action_comment_id`(`comment_id`) USING BTREE,
  INDEX `action_category_id`(`action_category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 143 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_action
-- ----------------------------
INSERT INTO `tb_action` VALUES (138, 6, 20, NULL, 1, '2022-05-25 23:47:45');
INSERT INTO `tb_action` VALUES (139, 6, 46, NULL, 1, '2022-05-26 09:40:55');
INSERT INTO `tb_action` VALUES (140, 6, NULL, 26, 1, '2022-05-26 09:41:19');
INSERT INTO `tb_action` VALUES (141, 8, NULL, 27, 1, '2023-03-10 16:48:46');
INSERT INTO `tb_action` VALUES (142, 8, 58, NULL, 1, '2023-03-10 16:48:50');
INSERT INTO `tb_action` VALUES (143, 8, 58, NULL, 2, '2023-03-10 16:49:33');

-- ----------------------------
-- Table structure for tb_action_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_action_category`;
CREATE TABLE `tb_action_category`  (
  `action_category_id` int NOT NULL AUTO_INCREMENT COMMENT '操作类别id',
  `action_category_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作类别',
  PRIMARY KEY (`action_category_id`) USING BTREE,
  INDEX `action_category_name`(`action_category_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_action_category
-- ----------------------------
INSERT INTO `tb_action_category` VALUES (3, '举报');
INSERT INTO `tb_action_category` VALUES (2, '收藏');
INSERT INTO `tb_action_category` VALUES (1, '点赞');
INSERT INTO `tb_action_category` VALUES (4, '点踩');

-- ----------------------------
-- Table structure for tb_admin
-- ----------------------------
DROP TABLE IF EXISTS `tb_admin`;
CREATE TABLE `tb_admin`  (
  `admin_id` int NOT NULL AUTO_INCREMENT COMMENT '管理员主键',
  `admin_code` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员账号',
  `admin_password` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员密码',
  `admin_state` int NOT NULL COMMENT '管理员账号状态0：正常使用，1：封禁',
  `role_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_admin
-- ----------------------------
INSERT INTO `tb_admin` VALUES (1, '100001', '10241024', 0, 1);

-- ----------------------------
-- Table structure for tb_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment`  (
  `comment_id` int NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `comment_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论内容',
  `comment_root_parent_id` int NULL DEFAULT NULL COMMENT '评论的根级父评论',
  `comment_parent_id` int NULL DEFAULT NULL COMMENT '父级评论ID',
  `post_id` int NOT NULL COMMENT '所属帖子ID',
  `user_id` int NOT NULL COMMENT '所属用户ID',
  `comment_likes_num` int NOT NULL COMMENT '评论点赞量，初始为0',
  `comment_create_time` datetime NOT NULL COMMENT '评论时间',
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `comment_parent_id`(`comment_parent_id`) USING BTREE,
  INDEX `comment_post_id`(`post_id`) USING BTREE,
  INDEX `comment_user_id`(`user_id`) USING BTREE,
  INDEX `comment_root_parent_id`(`comment_root_parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_comment
-- ----------------------------
INSERT INTO `tb_comment` VALUES (25, 'x', NULL, NULL, 46, 6, 0, '2022-05-26 09:41:05');
INSERT INTO `tb_comment` VALUES (26, 't', 25, 25, 46, 6, 1, '2022-05-26 09:41:13');
INSERT INTO `tb_comment` VALUES (27, '评论测试', NULL, NULL, 58, 8, 1, '2023-03-10 16:48:40');
INSERT INTO `tb_comment` VALUES (28, '子级评论测试', 27, 27, 58, 8, 0, '2023-03-10 16:49:01');

-- ----------------------------
-- Table structure for tb_focus
-- ----------------------------
DROP TABLE IF EXISTS `tb_focus`;
CREATE TABLE `tb_focus`  (
  `focus_id` int NOT NULL AUTO_INCREMENT COMMENT '用户关注中间表id',
  `user_id` int NOT NULL COMMENT '用户id',
  `focus_user_id` int NOT NULL COMMENT '关注的用户id',
  PRIMARY KEY (`focus_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `focus_user_id`(`focus_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_focus
-- ----------------------------
INSERT INTO `tb_focus` VALUES (14, 6, 1);

-- ----------------------------
-- Table structure for tb_module
-- ----------------------------
DROP TABLE IF EXISTS `tb_module`;
CREATE TABLE `tb_module`  (
  `module_id` int NOT NULL AUTO_INCREMENT,
  `module_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `module_parent_id` int NULL DEFAULT NULL,
  `module_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `module_dec` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`module_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_module
-- ----------------------------

-- ----------------------------
-- Table structure for tb_partition
-- ----------------------------
DROP TABLE IF EXISTS `tb_partition`;
CREATE TABLE `tb_partition`  (
  `partition_id` int NOT NULL AUTO_INCREMENT COMMENT '分区ID',
  `partition_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分区名',
  PRIMARY KEY (`partition_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_partition
-- ----------------------------
INSERT INTO `tb_partition` VALUES (1, '酒馆');
INSERT INTO `tb_partition` VALUES (2, '攻略');
INSERT INTO `tb_partition` VALUES (3, '官方');

-- ----------------------------
-- Table structure for tb_post
-- ----------------------------
DROP TABLE IF EXISTS `tb_post`;
CREATE TABLE `tb_post`  (
  `post_id` int NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
  `partition_id` int NOT NULL COMMENT '分区ID',
  `user_id` int NOT NULL COMMENT '用户ID ',
  `post_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '帖子标题',
  `post_article` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '帖子正文',
  `post_abbreviation` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '帖子简写',
  `post_likes_num` int NOT NULL COMMENT '帖子点赞数，初始为0',
  `post_looks_num` int NOT NULL COMMENT '帖子浏览量，初始为0',
  `post_collections_num` int NOT NULL COMMENT '帖子收藏量，初始为0',
  `post_comments_num` int NOT NULL COMMENT '帖子评论量，初始为0',
  `post_create_time` datetime NOT NULL COMMENT '帖子创建时间',
  `post_edit_time` datetime NOT NULL COMMENT '帖子最新修改时间，初始为创建时间',
  `post_state` int NOT NULL COMMENT '帖子状态(0:待审核，1:正常，2：封禁)',
  PRIMARY KEY (`post_id`) USING BTREE,
  INDEX `post_partition_id`(`partition_id`) USING BTREE,
  INDEX `post_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_post
-- ----------------------------
INSERT INTO `tb_post` VALUES (17, 3, 1, '「导能原盘·跋尾」活动：首通「诡境」领原石', '<p><img src=\"http://localhost:10241/img/postImg/66af3e9b873741bfb75a2eccac12da52.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(73, 73, 73);\">活动期间，旅行者可以挑战「调查点」和「诡境」，获取「诡境」的挑战积分。</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(73, 73, 73);\">最高挑战积分累计达到指定目标可领取</span><span style=\"color: rgb(173, 114, 14);\">大英雄的经验、武器突破素材、摩拉、精锻用魔矿</span><span style=\"color: rgb(73, 73, 73);\">等奖励，首次通关挑战关卡更可获得</span><span style=\"color: rgb(173, 114, 14);\">原石</span><span style=\"color: rgb(73, 73, 73);\">奖励。</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(73, 73, 73);\">&nbsp;</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(73, 73, 73);\"><strong>〓活动时间〓</strong></span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(173, 114, 14);\">2021/12/24&nbsp;10:00&nbsp;~&nbsp;2022/01/03&nbsp;03:59</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(73, 73, 73);\">&nbsp;</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(73, 73, 73);\"><strong>〓参与条件〓</strong></span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(173, 114, 14);\">冒险等阶≥20级</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(73, 73, 73);\">&nbsp;</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(73, 73, 73);\"><strong>〓活动说明〓</strong></span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(73, 73, 73);\">●&nbsp;每一幕开启时，都会开启3个新的「调查点」。旅行者前往指定地点，击败该地点的敌人，可以获得</span><span style=\"color: rgb(173, 114, 14);\">原石、大英雄的经验、摩拉</span><span style=\"color: rgb(73, 73, 73);\">等奖励。</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(73, 73, 73);\">●&nbsp;活动期间，完成第一幕内的所有「调查点」挑战后，即可获得所有的</span><span style=\"color: rgb(65, 70, 75);\">「地脉之果碎片」</span><span style=\"color: rgb(73, 73, 73);\">。完成任务「我，游学者」将会获得导能原盘。将</span><span style=\"color: rgb(65, 70, 75);\">「地脉之果碎片」装备至导能原盘上，在挑战</span><span style=\"color: rgb(73, 73, 73);\">「诡境」时可以获得加成。</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(73, 73, 73);\">●&nbsp;活动期间，完成每一幕中的「调查点」挑战，即可开启一关「诡境」。</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(73, 73, 73);\">●&nbsp;正式开始「诡境」挑战前，可以选择不同的挑战难度和挑战条件。根据所选择的挑战难度和条件，完成挑战可获得相应积分。首次通关可领取</span><span style=\"color: rgb(173, 114, 14);\">原石</span><span style=\"color: rgb(73, 73, 73);\">奖励。</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(73, 73, 73);\">●活动期间内，旅行者可以多次挑战同一挑战关卡，刷新该关卡最高记录。</span><span style=\"color: rgb(192, 0, 0);\">累计最高挑战积分为当前所有挑战关卡最高记录积分之和</span><span style=\"color: rgb(73, 73, 73);\">，达到指定积分时可领取对应的奖励。</span></p>', '活动期间，旅行者可以挑战「调查点」和「诡境」，获取「诡境」的挑战积分。最高挑战积分累计达到指定目标可领取大英雄的经验、武器突破素材、摩拉、精锻用魔矿等奖励，首次通关挑战关卡更可获得原石奖励。〓活动时间〓2021/12/2410:00~2022/01/0303:59〓参与条件〓冒险等阶≥20级〓活动说明〓●每一幕开启时，都会开启3个新的「调查点」。旅行者前往指定地点，击败该地点的敌人，可以获得原石、', 0, 4, 0, 0, '2022-04-18 19:44:50', '2022-04-18 19:44:50', 1);
INSERT INTO `tb_post` VALUES (18, 3, 1, '《原神》2.4版本前瞻特别节目预告', '<p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(51, 51, 51);\">旅行者们大家好，又到了直播预告时间&gt;&nbsp;&lt;</span></p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(51, 51, 51);\">《原神》2.4版本前瞻特别节目将于12月26日（本周日）晚20:00正式开启！</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(51, 51, 51);\">本次特别节目中，开发组的同学将给大家带来2.4版本的更新内容，直播间内还有兑换码等福利“掉落”。</span><img src=\"http://localhost:10241/img/postImg/b9bfba743f334d86bd1e0f6eea4b6f61.jpg\" alt=\"\" data-href=\"\" style=\"\"></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(51, 51, 51);\">提前关注</span><a href=\"https://live.bilibili.com/21987615\" target=\"_blank\">直播间</a><span style=\"color: rgb(51, 51, 51);\">，我们不见不散！</span></p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(73, 73, 73);\">同时，本次特别节目将在以下12个平台同步开播：B站直播间、斗鱼直播间、虎牙直播间、企鹅电竞直播间、快手直播间、抖音直播间、西瓜直播间、微博直播间、微信视频号直播间、4399直播间、知乎直播间和百度贴吧直播间，欢迎旅行者们前往收看！</span><img src=\"http://localhost:10241/img/postImg/9ff740cb8ddd4c59ba561bfe88c3b658.jpg\" alt=\"\" data-href=\"\" style=\"\"></p>', '旅行者们大家好，又到了直播预告时间><《原神》2.4版本前瞻特别节目将于12月26日（本周日）晚20:00正式开启！本次特别节目中，开发组的同学将给大家带来2.4版本的更新内容，直播间内还有兑换码等福利“掉落”。提前关注直播间，我们不见不散！同时，本次特别节目将在以下12个平台同步开播：B站直播间、斗鱼直播间、虎牙直播间、企鹅电竞直播间、快手直播间、抖音直播间、西瓜直播间、微博直播间、微信视频号直', 0, 0, 0, 0, '2022-04-18 19:50:34', '2022-04-18 19:50:34', 1);
INSERT INTO `tb_post` VALUES (19, 3, 1, '《原神》PV短片——「雪霁逢椿」', '<p><img src=\"http://localhost:10241/img/postImg/57d16e727aaf483399833255c6a676ca.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(0, 0, 0);\">花开再艳，也终有一日须经受严冬的考验。</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(0, 0, 0);\">但霜雪总有停时，寒木依然挺立。</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(0, 0, 0);\">待云开雪霁，方觉春日已至。</span></p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(0, 0, 0);\">CV：</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(0, 0, 0);\">神里绫华——小N</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(0, 0, 0);\">神里绫人——赵路</span></p>', '花开再艳，也终有一日须经受严冬的考验。但霜雪总有停时，寒木依然挺立。待云开雪霁，方觉春日已至。CV：神里绫华——小N神里绫人——赵路', 0, 0, 0, 0, '2022-04-18 20:09:57', '2022-04-18 20:09:57', 1);
INSERT INTO `tb_post` VALUES (20, 2, 6, '【V2.6攻略】#角色攻略#神里绫人全面解析，从入门到入土', '<p><span style=\"color: rgb(30, 31, 32); background-color: rgb(255, 255, 255); font-size: 16px; font-family: -apple-system, BlinkMacSystemFont, &quot;Helvetica Neue&quot;, Helvetica, &quot;Segoe UI&quot;, Arial, Roboto, &quot;PingFang SC&quot;, miui, &quot;Hiragino Sans GB&quot;, &quot;Microsoft Yahei&quot;, sans-serif;\">UID&nbsp;100372047</span><img src=\"http://localhost:10241/img/postImg/f732a22241024a90a3e01be3cfac0fca.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(234, 0, 119);\"><strong>前言（概述）</strong></span></p><p style=\"text-indent: 0px; text-align: left;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;神里绫人，优秀的战场主c，手感极佳，伤害不俗</p><p style=\"text-indent: 0px; text-align: left;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;有着全游<span style=\"color: rgb(234, 0, 119);\"><strong>最短的循环轴</strong></span>，但并不适合作为速切副c，站场时间虽然较短，但放到正常配队中，必然还是会挤压主c的输出环境，那么话不多说，我们进入主题吧</p><p style=\"text-indent: 0px; text-align: left;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;（补文）祈鸢更擅长分析，对于数据侧，只能提供部分仅供参考，并不能涵盖所有目录</p><hr/><p style=\"text-indent: 0px; text-align: left;\">1，机制</p><p style=\"text-indent: 0px; text-align: left;\">2，圣遗物</p><p style=\"text-indent: 0px; text-align: left;\">3，武器</p><p style=\"text-indent: 0px; text-align: left;\">4，命座</p><p style=\"text-indent: 0px; text-align: left;\">5，天赋</p><p style=\"text-indent: 0px; text-align: left;\">6，配队</p><p style=\"text-indent: 0px; text-align: left;\">7，毕业面板</p><hr/><h1 style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(255, 144, 0);\">一，绫人的机制</span></h1><h2 style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(9, 190, 153);\"><em>1，普通攻击·神里流·转</em></span></h2><p><img src=\"http://localhost:10241/img/postImg/8e3100e3fd384e96a52824e3dc965e90.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p style=\"text-indent: 0px; text-align: left;\">（1）怎么说呢，确实帅，可惜的是倍率太低了</p><p style=\"text-indent: 0px; text-align: left;\">只能说<span style=\"color: rgb(234, 0, 119);\"><strong>观赏性大于实用性</strong></span>，收益略低，一般不建议点，全凭喜欢</p><p style=\"text-indent: 0px; text-align: left;\">（2）重击也是同样，不是重点，我们就先跳过吧</p><hr/><h2 style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(9, 190, 153);\"><em>2，e技能—神里流·镜花</em></span></h2><p style=\"text-indent: 0px; text-align: left;\">释放e技能后，会制造一个<span style=\"color: rgb(9, 190, 153);\"><strong>水影</strong></span>，并进入鉴花状态，使用<span style=\"color: rgb(9, 190, 153);\"><strong>瞬水剑</strong></span>攻击<img src=\"http://localhost:10241/img/postImg/1c71b112135a4474a40fe6179990db38.jpg\" alt=\"\" data-href=\"\" style=\"\"></p><p style=\"text-indent: 0px; text-align: left;\">1）绫人的核心技能，占总伤害的<span style=\"color: rgb(234, 0, 119);\"><strong>76.7%左右</strong></span></p><p style=\"text-indent: 0px; text-align: left;\">（2）先讲比较简单的<span style=\"color: rgb(9, 190, 153);\"><strong>水影</strong></span></p><p style=\"text-indent: 0px; text-align: left;\">·配合方向键，可以调整垫步<span style=\"color: rgb(234, 0, 119);\"><strong>位移的方向</strong></span></p><p style=\"text-indent: 0px; text-align: left;\">·水影可以造成伤害，在<span style=\"color: rgb(255, 144, 0);\"><strong>范围内存在敌人</strong></span>或<span style=\"color: rgb(255, 144, 0);\"><strong>持续时间</strong></span>到时破裂，倍率还算不错（8级e）</p><p style=\"text-indent: 0px; text-align: left;\">·配合<span style=\"color: rgb(9, 190, 153);\"><strong>天赋·清泷绕峰</strong></span>，有着可观的收益，e如果释放后直接破裂，可以有效提高增伤覆盖率。毕竟等它自己破，e都结束了</p><p style=\"text-indent: 0px; text-align: left;\">综上，<span style=\"color: rgb(9, 190, 153);\"><strong>水影</strong></span>的机制鼓励我们直接<span style=\"color: rgb(234, 0, 119);\"><strong>把e丢怪脸上</strong></span>，收益最大化</p><p style=\"text-indent: 0px; text-align: left;\">（3）接下来将<span style=\"color: rgb(9, 190, 153);\"><strong>瞬水剑</strong></span></p><p style=\"text-indent: 0px; text-align: left;\">·伤害<span style=\"color: rgb(234, 0, 119);\"><strong>视为普通攻击</strong></span>，可以吃到普攻类加成</p><p style=\"text-indent: 0px; text-align: left;\">·退场自动解除该效果</p><p style=\"text-indent: 0px; text-align: left;\">·<span style=\"color: rgb(234, 0, 119);\"><strong>对群能力出色</strong></span></p><p style=\"text-indent: 0px; text-align: left;\">·可以<span style=\"color: rgb(255, 144, 0);\"><strong>按住左键</strong></span>（重击）打出连续的普攻，不需要一下一下按</p><p style=\"text-indent: 0px; text-align: left;\">·在e技能的释放期间<span style=\"color: rgb(234, 0, 119);\"><strong>提高抗打断性</strong></span>，但说实话，绫人是真正意义上的<span style=\"color: rgb(255, 144, 0);\"><strong>站桩输出</strong></span>，个人认为配个盾会舒服一点，按着左键就行了。</p><p style=\"text-indent: 0px; text-align: left;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当然，对于<span style=\"color: rgb(234, 0, 119);\"><strong>追求极致输出</strong></span>来说，意义还是比较大的</p><p style=\"text-indent: 0px; text-align: left;\">·e技能释放需要1s，然后持续6s，冷却12s，e技能释放不算在持续时间内，<span style=\"color: rgb(234, 0, 119);\"><strong>真空5s</strong></span></p><p style=\"text-indent: 0px; text-align: left;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;短轴循环的最大问题就是<span style=\"color: rgb(234, 0, 119);\"><strong>buff利用率较低</strong></span>，且相较于其他角色，变数更多</p><p style=\"text-indent: 0px; text-align: left;\">·总共可以<span style=\"color: rgb(234, 0, 119);\"><strong>释放15次</strong></span>，倍率是三刀一循环，实战中可以砍到16/17下</p><p style=\"text-indent: 0px; text-align: left;\">·<span style=\"color: rgb(255, 144, 0);\"><strong>产球方面，</strong></span>绫人的<span style=\"color: rgb(234, 0, 119);\"><strong>瞬水剑产球</strong></span>，产球的量为<span style=\"color: rgb(234, 0, 119);\"><strong>1-2（50%）</strong></span>，产球cd大约在2s不到一点，也就是说第1，7，13下会产球，<span style=\"color: rgb(255, 36, 2);\"><strong>总计产球4.5个</strong></span></p><p style=\"text-indent: 0px; text-align: left;\">·<span style=\"color: rgb(255, 144, 0);\"><strong>挂水方面</strong></span>，受计时器和计数器的双重影响，第1，4，7，10，13，16下可以挂水（技能释放的时机，卡肉，攻速都会一定影响），总体来说就是1秒1水左右</p>', 'UID100372047前言（概述）神里绫人，优秀的战场主c，手感极佳，伤害不俗有着全游最短的循环轴，但并不适合作为速切副c，站场时间虽然较短，但放到正常配队中，必然还是会挤压主c的输出环境，那么话不多说，我们进入主题吧（补文）祈鸢更擅长分析，对于数据侧，只能提供部分仅供参考，并不能涵盖所有目录1，机制2，圣遗物3，武器4，命座5，天赋6，配队7，毕业面板一，绫人的机制1，普通攻击·神里流·转（1', 1, 0, 0, 0, '2022-04-18 20:33:11', '2022-04-18 20:33:11', 1);
INSERT INTO `tb_post` VALUES (33, 3, 1, '《原神》稻妻篇第二张OST「佚落迁忘之岛」现已正式上线！', '<p><img src=\"http://localhost:10241/img/postImg/eeba7bc9d9d443dd8dc060d3e5910408.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(0, 0, 0);\">亲爱的旅行者，《原神》稻妻篇第二张OST「佚落迁忘之岛&nbsp;Islands&nbsp;of&nbsp;the&nbsp;Lost&nbsp;and&nbsp;Forgotten」现已正式上线！欢迎旅行者前往下列平台，收听完整版OST。</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(0, 0, 0);\">网易云音乐收听地址：</span><a href=\"https://music.163.com/#/album?id=143306893\" target=\"_blank\">https://music.163.com/#/album?id=143306893</a></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(0, 0, 0);\">Apple&nbsp;Music收听地址：</span><a href=\"https://music.apple.com/cn/album/1618534475\" target=\"_blank\">https://music.apple.com/cn/album/1618534475</a></p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(0, 0, 0);\">本张专辑共收录60首由HOYO-MiX音乐团队为《原神》稻妻地区创作的原声音乐。</span><img src=\"http://localhost:10241/img/postImg/767e4738645248df99d2dbebd935c9ac.jpg\" alt=\"\" data-href=\"\" style=\"\"></p>', '亲爱的旅行者，《原神》稻妻篇第二张OST「佚落迁忘之岛IslandsoftheLostandForgotten」现已正式上线！欢迎旅行者前往下列平台，收听完整版OST。网易云音乐收听地址：https://music.163.com/#/album?id=143306893AppleMusic收听地址：https://music.apple.com/cn/album/1618534475本张专辑共收', 0, 0, 0, 0, '2022-04-18 20:39:43', '2022-04-18 20:39:43', 1);
INSERT INTO `tb_post` VALUES (34, 3, 1, '「奔行世间」常驻祈愿开启！', '<p><img src=\"http://localhost:10241/img/postImg/b71a1368efb04a69a9663b17a11fda0e.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p style=\"text-indent: 0px; text-align: left;\">亲爱的旅行者：</p><p style=\"text-indent: 0px; text-align: left;\">「奔行世间」常驻祈愿现已开启，旅行者可以在这里获得更多角色与武器，组建强大的队伍！</p><p style=\"text-indent: 0px; text-align: left;\">（祈愿解锁后，PC端按下F3键、手机端点击右上角十字星图标、PS4端长按L1键开启快捷轮盘选择十字星图标即可打开祈愿界面。）</p><p style=\"text-indent: 0px; text-align: left;\">&nbsp;</p><p style=\"text-indent: 0px; text-align: left;\"><strong>〓祈愿时间〓</strong></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(204, 146, 85);\">永久</span></p><p style=\"text-indent: 0px; text-align: left;\">&nbsp;</p><p style=\"text-indent: 0px; text-align: left;\"><strong>〓祈愿介绍〓</strong></p><p style=\"text-indent: 0px; text-align: left;\">「奔行世间」常驻祈愿为<span style=\"color: rgb(204, 146, 85);\">永久性</span>祈愿活动，在本祈愿内旅行者可以抽取非限定角色与武器。</p><p style=\"text-indent: 0px; text-align: left;\">在本祈愿内，每次十连祈愿必会获得<span style=\"color: rgb(204, 146, 85);\">至少1个4星或以上</span>武器或角色。</p><p style=\"text-indent: 0px; text-align: left;\">&nbsp;</p><p style=\"text-indent: 0px; text-align: left;\">※&nbsp;更多祈愿信息可点击游戏内祈愿界面左下角【详情】按钮进行查询。</p>', '亲爱的旅行者：「奔行世间」常驻祈愿现已开启，旅行者可以在这里获得更多角色与武器，组建强大的队伍！（祈愿解锁后，PC端按下F3键、手机端点击右上角十字星图标、PS4端长按L1键开启快捷轮盘选择十字星图标即可打开祈愿界面。）〓祈愿时间〓永久〓祈愿介绍〓「奔行世间」常驻祈愿为永久性祈愿活动，在本祈愿内旅行者可以抽取非限定角色与武器。在本祈愿内，每次十连祈愿必会获得至少1个4星或以上武器或角色。※更多祈愿', 0, 0, 0, 0, '2022-04-18 23:27:59', '2022-04-18 23:27:59', 1);
INSERT INTO `tb_post` VALUES (35, 3, 1, '「杯装之诗」角色活动祈愿开启', '<p><img src=\"http://localhost:10241/img/postImg/f701ecf06f6f46118c05ed464276ab76.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p style=\"text-indent: 0px; text-align: left;\">亲爱的旅行者：</p><p style=\"text-indent: 0px; text-align: left;\">活动祈愿「杯装之诗」将在9月28日开启，旅行者可以在这里获得更多角色与武器，组建强大的队伍！</p><p style=\"text-indent: 0px; text-align: left;\">（祈愿解锁后，PC端按下F3键、手机端点击右上角十字星图标、PS4端长按L1键开启快捷轮盘选择十字星图标即可打开祈愿界面。）</p><p style=\"text-indent: 0px; text-align: left;\">&nbsp;</p><p style=\"text-indent: 0px; text-align: left;\">〓<strong>祈愿时间</strong>〓</p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(204, 146, 85);\">2020/09/28&nbsp;停服更新后~2020-10-18&nbsp;17:59:59</span></p><p style=\"text-indent: 0px; text-align: left;\">&nbsp;</p><p style=\"text-indent: 0px; text-align: left;\">〓<strong>祈愿介绍</strong>〓</p><p style=\"text-indent: 0px; text-align: left;\"><img src=\"http://localhost:10241/img/postImg/a8454f810eeb4c7b8e0eea4986a70b42.jpg\" alt=\"\" data-href=\"\" style=\"\"></p><p style=\"text-indent: 0px; text-align: left;\">活动期间内，限定五星角色<span style=\"color: rgb(204, 146, 85);\">「风色诗人·温迪(风)」</span>的祈愿获取概率将<span style=\"color: rgb(232, 85, 70);\">大幅提升</span>！</p><p style=\"text-indent: 0px; text-align: left;\">活动期间内，四星角色<span style=\"color: rgb(204, 146, 85);\">「闪耀偶像·芭芭拉(水)」「断罪皇女！！·菲谢尔(雷)」「万民百味·香菱(火)」</span>的祈愿获取概率将<span style=\"color: rgb(232, 85, 70);\">大幅提升</span>！</p><p style=\"text-indent: 0px; text-align: left;\">&nbsp;</p><p style=\"text-indent: 0px; text-align: left;\"><strong>※&nbsp;</strong>更多祈愿信息可点击游戏内祈愿界面左下角【详情】按钮进行查询。</p>', '亲爱的旅行者：活动祈愿「杯装之诗」将在9月28日开启，旅行者可以在这里获得更多角色与武器，组建强大的队伍！（祈愿解锁后，PC端按下F3键、手机端点击右上角十字星图标、PS4端长按L1键开启快捷轮盘选择十字星图标即可打开祈愿界面。）〓祈愿时间〓2020/09/28停服更新后~2020-10-1817:59:59〓祈愿介绍〓活动期间内，限定五星角色「风色诗人·温迪(风)」的祈愿获取概率将大幅提升！活动', 0, 0, 0, 0, '2022-04-18 23:29:43', '2022-04-18 23:29:43', 2);
INSERT INTO `tb_post` VALUES (36, 3, 1, '「神铸赋形」武器活动祈愿预告！', '<p><img src=\"http://localhost:10241/img/postImg/d2c125fab12e44d0a049febc18e6fc14.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p style=\"text-indent: 0px; text-align: left;\">亲爱的旅行者：</p><p style=\"text-indent: 0px; text-align: left;\">活动祈愿「神铸赋形」将在9月28日开启，旅行者可以在这里获得更多武器与角色，提升队伍的战斗力！</p><p style=\"text-indent: 0px; text-align: left;\">（祈愿解锁后，PC端按下F3键、手机端点击右上角十字星图标、PS4端长按L1键开启快捷轮盘选择十字星图标即可打开祈愿界面。）</p><p style=\"text-indent: 0px; text-align: left;\">&nbsp;</p><p style=\"text-indent: 0px; text-align: left;\">〓<strong>祈愿时间</strong>〓</p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(204, 146, 85);\">2020/09/28&nbsp;停服更新后~2020-10-18&nbsp;17:59:59</span></p><p style=\"text-indent: 0px; text-align: left;\">&nbsp;</p><p style=\"text-indent: 0px; text-align: left;\">〓<strong>祈愿介绍</strong>〓</p><p style=\"text-indent: 0px; text-align: left;\"><img src=\"http://localhost:10241/img/postImg/9ec82c9bb69647c5a91feb8caacda7b1.jpg\" alt=\"\" data-href=\"\" style=\"\"></p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\">活动期间内，以上特定五星武器<span style=\"color: rgb(204, 146, 85);\">「单手剑·风鹰剑」「弓·阿莫斯之弓」</span>的祈愿获取概率将<span style=\"color: rgb(232, 85, 70);\">大幅提升</span>！</p><p style=\"text-indent: 0px; text-align: left;\">活动期间内，特定四星武器<span style=\"color: rgb(204, 146, 85);\">「单手剑·笛剑」「双手剑·钟剑」「法器·流浪乐章」「弓·绝弦」「长柄武器·西风长枪」</span>的祈愿获取概率将<span style=\"color: rgb(232, 85, 70);\">大幅提升</span>！</p><p style=\"text-indent: 0px; text-align: left;\">&nbsp;</p><p style=\"text-indent: 0px; text-align: left;\"><strong>※&nbsp;</strong>更多祈愿信息可点击游戏内祈愿界面左下角【详情】按钮进行查询。</p>', '亲爱的旅行者：活动祈愿「神铸赋形」将在9月28日开启，旅行者可以在这里获得更多武器与角色，提升队伍的战斗力！（祈愿解锁后，PC端按下F3键、手机端点击右上角十字星图标、PS4端长按L1键开启快捷轮盘选择十字星图标即可打开祈愿界面。）〓祈愿时间〓2020/09/28停服更新后~2020-10-1817:59:59〓祈愿介绍〓活动期间内，以上特定五星武器「单手剑·风鹰剑」「弓·阿莫斯之弓」的祈愿获取概', 0, 0, 0, 0, '2022-04-18 23:30:54', '2022-04-18 23:30:54', 2);
INSERT INTO `tb_post` VALUES (37, 3, 1, '「闪焰的驻足」即将开启，「逃跑的太阳·可莉(火)」概率UP！', '<p><img src=\"http://localhost:10241/img/postImg/9f41849c003a44d9a58fcbeb73ac766c.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p style=\"text-indent: 0px; text-align: left;\">亲爱的旅行者：</p><p style=\"text-indent: 0px; text-align: left;\">活动祈愿「闪焰的驻足」限时开启，旅行者可以在这里获得更多角色与武器，组建强大的队伍！</p><p style=\"text-indent: 0px; text-align: left;\">（祈愿解锁后，PC端按下F3键、手机端点击右上角十字星图标、PS4端长按L1键开启快捷轮盘选择十字星图标即可打开祈愿界面。）</p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\">〓<strong>祈愿时间</strong>〓</p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(204, 146, 85);\">2020/10/20&nbsp;18:00:00~2020/11/09&nbsp;17:59:59</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(51, 51, 51);\">（*我们对活动祈愿「闪焰的驻足」的结束时间进行了调整，结束时间请以2020/11/10&nbsp;14:59:59为准。）</span></p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\">〓<strong>祈愿介绍</strong>〓</p><p style=\"text-indent: 0px; text-align: left;\"><img src=\"http://localhost:10241/img/postImg/4d1ef41f902a480c9cbf26c213b49147.jpg\" alt=\"\" data-href=\"\" style=\"\"></p><p style=\"text-indent: 0px; text-align: left;\">活动期间内，限定五星角色<span style=\"color: rgb(204, 146, 85);\">「逃跑的太阳·可莉(火)」</span>的祈愿获取概率将<span style=\"color: rgb(232, 85, 70);\">大幅提升</span>！</p><p style=\"text-indent: 0px; text-align: left;\">活动期间内，四星角色<span style=\"color: rgb(204, 146, 85);\">「少年春衫薄·行秋(水)」「未授勋之花·诺艾尔(岩)」「无害甜度·砂糖(风)」</span>的祈愿获取概率将<span style=\"color: rgb(232, 85, 70);\">大幅提升</span>！</p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\"><strong>※&nbsp;</strong>更多祈愿信息可点击祈愿界面左下角【详情】按钮进行查询。</p>', '亲爱的旅行者：活动祈愿「闪焰的驻足」限时开启，旅行者可以在这里获得更多角色与武器，组建强大的队伍！（祈愿解锁后，PC端按下F3键、手机端点击右上角十字星图标、PS4端长按L1键开启快捷轮盘选择十字星图标即可打开祈愿界面。）〓祈愿时间〓2020/10/2018:00:00~2020/11/0917:59:59（*我们对活动祈愿「闪焰的驻足」的结束时间进行了调整，结束时间请以2020/11/1014:', 0, 1, 0, 0, '2022-04-18 23:55:09', '2022-04-18 23:55:09', 1);
INSERT INTO `tb_post` VALUES (38, 3, 1, '「神铸赋形」即将开启，「四风原典」「狼的末路」概率UP！', '<p><img src=\"http://localhost:10241/img/postImg/d2c6767d8c7249b394d9f7d882be7043.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p style=\"text-indent: 0px; text-align: left;\">亲爱的旅行者：</p><p style=\"text-indent: 0px; text-align: left;\">活动祈愿「神铸赋形」限时开启，旅行者可以在这里获得更多武器与角色，提升队伍的战斗力！</p><p style=\"text-indent: 0px; text-align: left;\">（祈愿解锁后，PC端按下F3键、手机端点击右上角十字星图标、PS4端长按L1键开启快捷轮盘选择十字星图标即可打开祈愿界面。）</p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\">〓<strong>祈愿时间</strong>〓</p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(204, 146, 85);\">2020/10/20&nbsp;18:00:00~2020/11/09&nbsp;17:59:59</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(51, 51, 51);\">（*我们对活动祈愿「闪焰的驻足」的结束时间进行了调整，结束时间请以2020/11/10&nbsp;14:59:59为准。）</span></p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\">〓<strong>祈愿介绍</strong>〓</p><p style=\"text-indent: 0px; text-align: left;\"><img src=\"http://localhost:10241/img/postImg/0836647cbc524f98b10eed1094eb153b.jpg\" alt=\"\" data-href=\"\" style=\"\"></p><p style=\"text-indent: 0px; text-align: left;\">活动期间内，以上特定五星武器<span style=\"color: rgb(204, 146, 85);\">「法器·四风原典」「双手剑·狼的末路」</span>的祈愿获取概率将<span style=\"color: rgb(232, 85, 70);\">大幅提升</span>！</p><p style=\"text-indent: 0px; text-align: left;\">活动期间内，特定四星武器<span style=\"color: rgb(204, 146, 85);\">「单手剑·祭礼剑」「双手剑·祭礼大剑」「法器·祭礼残章」「弓·祭礼弓」「长柄武器·匣里灭辰」</span>的祈愿获取概率将<span style=\"color: rgb(232, 85, 70);\">大幅提升</span>！</p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\"><strong>※&nbsp;</strong>更多祈愿信息可点击祈愿界面左下角【详情】按钮进行查询。</p>', '亲爱的旅行者：活动祈愿「神铸赋形」限时开启，旅行者可以在这里获得更多武器与角色，提升队伍的战斗力！（祈愿解锁后，PC端按下F3键、手机端点击右上角十字星图标、PS4端长按L1键开启快捷轮盘选择十字星图标即可打开祈愿界面。）〓祈愿时间〓2020/10/2018:00:00~2020/11/0917:59:59（*我们对活动祈愿「闪焰的驻足」的结束时间进行了调整，结束时间请以2020/11/1014:', 0, 0, 0, 0, '2022-04-18 23:55:56', '2022-04-18 23:55:56', 1);
INSERT INTO `tb_post` VALUES (39, 3, 1, '「暂别冬都」祈愿开启！', '<p><img src=\"http://localhost:10241/img/postImg/08ada3ea7eb94d8f84c25177c30011fe.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p style=\"text-indent: 0px; text-align: left;\">活动祈愿「暂别冬都」开启，旅行者可以在这里获得更多角色与武器，组建强大的队伍！</p><p style=\"text-indent: 0px; text-align: left;\">（祈愿解锁后，PC端按下F3键、手机端点击右上角十字星图标、PS4端长按L1键开启快捷轮盘选择十字星图标即可打开祈愿界面。）</p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\"><strong>〓祈愿时间〓</strong></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(204, 146, 85);\">1.1版本更新后&nbsp;-&nbsp;2020-12-01&nbsp;15:59:59</span></p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\"><strong>〓祈愿介绍〓</strong></p><p style=\"text-indent: 0px; text-align: left;\"><img src=\"http://localhost:10241/img/postImg/227c6014ad12438d8ad91f8b7ef15871.jpg\" alt=\"\" data-href=\"\" style=\"\"></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(51, 51, 51);\">●&nbsp;</span>活动期间内，限定五星角色<span style=\"color: rgb(204, 146, 85);\">「「公子」·达达利亚(水)」</span>的祈愿获取概率将<span style=\"color: rgb(232, 85, 70);\">大幅提升</span>！</p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(51, 51, 51);\">●&nbsp;</span>活动期间内，四星角色<span style=\"color: rgb(204, 146, 85);\">「猫尾特调·迪奥娜(冰)」「无冕的龙王·北斗(雷)」「掩月天权·凝光(岩)」</span>的祈愿获取概率将<span style=\"color: rgb(232, 85, 70);\">大幅提升</span>！</p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(51, 51, 51);\">●&nbsp;</span>活动结束后，四星角色<span style=\"color: rgb(204, 146, 85);\">「猫尾特调·迪奥娜(冰)」</span>将在1.2版本进入「奔行世间」常驻祈愿。</p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(232, 85, 70);\">※以上角色中，限定角色不会进入「奔行世间」常驻祈愿。</span></p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\">※&nbsp;更多祈愿信息可点击祈愿界面左下角【详情】按钮进行查询。</p>', '活动祈愿「暂别冬都」开启，旅行者可以在这里获得更多角色与武器，组建强大的队伍！（祈愿解锁后，PC端按下F3键、手机端点击右上角十字星图标、PS4端长按L1键开启快捷轮盘选择十字星图标即可打开祈愿界面。）〓祈愿时间〓1.1版本更新后-2020-12-0115:59:59〓祈愿介绍〓●活动期间内，限定五星角色「「公子」·达达利亚(水)」的祈愿获取概率将大幅提升！●活动期间内，四星角色「猫尾特调·迪奥娜', 0, 2, 0, 0, '2022-04-18 23:56:46', '2022-04-18 23:56:46', 1);
INSERT INTO `tb_post` VALUES (40, 3, 1, '「神铸赋形」祈愿开启！', '<p><img src=\"http://localhost:10241/img/postImg/ea8c69e8961c4bddaddd45ee1684ce99.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p style=\"text-indent: 0px; text-align: left;\">活动祈愿「神铸赋形」开启，旅行者可以在这里获得更多武器与角色，提升队伍的战斗力！</p><p style=\"text-indent: 0px; text-align: left;\">（祈愿解锁后，PC端按下F3键、手机端点击右上角十字星图标、PS4端长按L1键开启快捷轮盘选择十字星图标即可打开祈愿界面。）</p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\"><strong>〓祈愿时间〓</strong></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(204, 146, 85);\">1.1版本更新后&nbsp;-&nbsp;2020-12-01&nbsp;15:59:59</span></p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\"><strong>〓祈愿介绍〓</strong></p><p style=\"text-indent: 0px; text-align: left;\"><img src=\"http://localhost:10241/img/postImg/2c4d285549154799b4cd948910eb38d5.jpg\" alt=\"\" data-href=\"\" style=\"\"></p><p style=\"text-indent: 0px; text-align: left;\">●&nbsp;活动期间内，限定五星武器<span style=\"color: rgb(204, 146, 85);\">「法器·尘世之锁」</span>、五星武器<span style=\"color: rgb(204, 146, 85);\">「弓·天空之翼」</span>的祈愿获取概率将<span style=\"color: rgb(232, 85, 70);\">大幅提升</span>！</p><p style=\"text-indent: 0px; text-align: left;\">●&nbsp;活动期间内，四星武器<span style=\"color: rgb(204, 146, 85);\">「单手剑·笛剑」「双手剑·雨裁」「法器·昭心」「弓·弓藏」「长柄武器·西风长枪」</span>的祈愿获取概率将<span style=\"color: rgb(232, 85, 70);\">大幅提升</span>！</p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(232, 85, 70);\">※以上武器中，限定武器不会进入「奔行世间」常驻祈愿。</span></p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\">※&nbsp;更多祈愿信息可点击祈愿界面左下角【详情】按钮进行查询。</p>', '活动祈愿「神铸赋形」开启，旅行者可以在这里获得更多武器与角色，提升队伍的战斗力！（祈愿解锁后，PC端按下F3键、手机端点击右上角十字星图标、PS4端长按L1键开启快捷轮盘选择十字星图标即可打开祈愿界面。）〓祈愿时间〓1.1版本更新后-2020-12-0115:59:59〓祈愿介绍〓●活动期间内，限定五星武器「法器·尘世之锁」、五星武器「弓·天空之翼」的祈愿获取概率将大幅提升！●活动期间内，四星武器', 0, 0, 0, 0, '2022-04-18 23:57:35', '2022-04-18 23:57:35', 1);
INSERT INTO `tb_post` VALUES (46, 3, 1, '「振晶的研究」玩法说明', '<p><img src=\"http://localhost:10241/img/postImg/87fd114335f848eb973a1b68c01220d4.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(51, 51, 51);\">活动期间，帮助前来璃月的枫丹学者研究名为「振晶」的宝石之间的「映射」反应，完成试验挑战可获得原石、大英雄的经验、武器突破素材、精锻用魔矿、摩拉奖励。</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(51, 51, 51);\">&nbsp;</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(51, 51, 51);\"><strong>〓活动时间〓</strong></span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(173, 114, 14);\">2022/04/21&nbsp;10:00&nbsp;~&nbsp;2022/05/05&nbsp;03:59</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(51, 51, 51);\">&nbsp;</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(51, 51, 51);\"><strong>〓参与条件〓</strong></span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(173, 114, 14);\">冒险等阶≥28级</span></p><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(173, 114, 14);\">且完成魔神任务：第一章·第三幕「迫近的客星」中的「送仙」</span></p><p><img src=\"http://localhost:10241/img/postImg/9215bd93ce144ee390c55032cc88b9a9.jpg\" alt=\"\" data-href=\"\" style=\"\"/><img src=\"http://localhost:10241/img/postImg/e8ec5910e3b14054bc78421853ced8ff.jpg\" alt=\"\" data-href=\"\" style=\"\"/><img src=\"http://localhost:10241/img/postImg/90b1ac48ec6049019b24df031f4f99c1.jpg\" alt=\"\" data-href=\"\" style=\"\"/><img src=\"http://localhost:10241/img/postImg/cba7d0ed321d46ae83cda94a4fa23892.jpg\" alt=\"\" data-href=\"\" style=\"\"/><img src=\"http://localhost:10241/img/postImg/5d73b1ade5b94ddfaf663b070a871763.jpg\" alt=\"\" data-href=\"\" style=\"\"/><img src=\"http://localhost:10241/img/postImg/6a05870eda0c4e05bad63fab1e72bc11.jpg\" alt=\"\" data-href=\"\" style=\"\"/><img src=\"http://localhost:10241/img/postImg/86cebea28c734163989edbc4a250885f.jpg\" alt=\"\" data-href=\"\" style=\"\"/><img src=\"http://localhost:10241/img/postImg/68700f4b22cc48ce873c6118edd9b4ba.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p>', '活动期间，帮助前来璃月的枫丹学者研究名为「振晶」的宝石之间的「映射」反应，完成试验挑战可获得原石、大英雄的经验、武器突破素材、精锻用魔矿、摩拉奖励。〓活动时间〓2022/04/2110:00~2022/05/0503:59〓参与条件〓冒险等阶≥28级且完成魔神任务：第一章·第三幕「迫近的客星」中的「送仙」', 1, 12, 0, 2, '2022-04-20 21:40:35', '2022-04-20 21:40:35', 1);
INSERT INTO `tb_post` VALUES (58, 1, 6, '测试', '<p>测试</p>', '测试', 1, 7, 1, 2, '2022-05-30 09:19:23', '2022-05-30 09:19:23', 1);

-- ----------------------------
-- Table structure for tb_post_img
-- ----------------------------
DROP TABLE IF EXISTS `tb_post_img`;
CREATE TABLE `tb_post_img`  (
  `post_img_id` int NOT NULL AUTO_INCREMENT COMMENT '帖子引用图片id',
  `post_img_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '帖子引用图片路径',
  `post_id` int NOT NULL COMMENT '引用帖子ID',
  PRIMARY KEY (`post_img_id`) USING BTREE,
  INDEX `img_post_id`(`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 83 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_post_img
-- ----------------------------
INSERT INTO `tb_post_img` VALUES (22, 'img/postImg/b9bfba743f334d86bd1e0f6eea4b6f61.jpg', 18);
INSERT INTO `tb_post_img` VALUES (23, 'img/postImg/9ff740cb8ddd4c59ba561bfe88c3b658.jpg', 18);
INSERT INTO `tb_post_img` VALUES (24, 'img/postImg/57d16e727aaf483399833255c6a676ca.jpg', 19);
INSERT INTO `tb_post_img` VALUES (25, 'img/postImg/f732a22241024a90a3e01be3cfac0fca.jpg', 20);
INSERT INTO `tb_post_img` VALUES (26, 'img/postImg/8e3100e3fd384e96a52824e3dc965e90.jpg', 20);
INSERT INTO `tb_post_img` VALUES (27, 'img/postImg/1c71b112135a4474a40fe6179990db38.jpg', 20);
INSERT INTO `tb_post_img` VALUES (52, 'img/postImg/eeba7bc9d9d443dd8dc060d3e5910408.jpg', 33);
INSERT INTO `tb_post_img` VALUES (53, 'img/postImg/767e4738645248df99d2dbebd935c9ac.jpg', 33);
INSERT INTO `tb_post_img` VALUES (54, 'img/postImg/b71a1368efb04a69a9663b17a11fda0e.jpg', 34);
INSERT INTO `tb_post_img` VALUES (55, 'img/postImg/f701ecf06f6f46118c05ed464276ab76.jpg', 35);
INSERT INTO `tb_post_img` VALUES (56, 'img/postImg/a8454f810eeb4c7b8e0eea4986a70b42.jpg', 35);
INSERT INTO `tb_post_img` VALUES (57, 'img/postImg/d2c125fab12e44d0a049febc18e6fc14.jpg', 36);
INSERT INTO `tb_post_img` VALUES (58, 'img/postImg/9ec82c9bb69647c5a91feb8caacda7b1.jpg', 36);
INSERT INTO `tb_post_img` VALUES (59, 'img/postImg/9f41849c003a44d9a58fcbeb73ac766c.jpg', 37);
INSERT INTO `tb_post_img` VALUES (60, 'img/postImg/4d1ef41f902a480c9cbf26c213b49147.jpg', 37);
INSERT INTO `tb_post_img` VALUES (61, 'img/postImg/d2c6767d8c7249b394d9f7d882be7043.jpg', 38);
INSERT INTO `tb_post_img` VALUES (62, 'img/postImg/0836647cbc524f98b10eed1094eb153b.jpg', 38);
INSERT INTO `tb_post_img` VALUES (63, 'img/postImg/08ada3ea7eb94d8f84c25177c30011fe.jpg', 39);
INSERT INTO `tb_post_img` VALUES (64, 'img/postImg/227c6014ad12438d8ad91f8b7ef15871.jpg', 39);
INSERT INTO `tb_post_img` VALUES (65, 'img/postImg/ea8c69e8961c4bddaddd45ee1684ce99.jpg', 40);
INSERT INTO `tb_post_img` VALUES (66, 'img/postImg/2c4d285549154799b4cd948910eb38d5.jpg', 40);
INSERT INTO `tb_post_img` VALUES (67, 'img/postImg/f7a2aa65f3124f4da5d48cb145701340.jpg', 45);
INSERT INTO `tb_post_img` VALUES (68, 'img/postImg/87fd114335f848eb973a1b68c01220d4.jpg', 46);
INSERT INTO `tb_post_img` VALUES (69, 'img/postImg/9215bd93ce144ee390c55032cc88b9a9.jpg', 46);
INSERT INTO `tb_post_img` VALUES (70, 'img/postImg/e8ec5910e3b14054bc78421853ced8ff.jpg', 46);
INSERT INTO `tb_post_img` VALUES (71, 'img/postImg/90b1ac48ec6049019b24df031f4f99c1.jpg', 46);
INSERT INTO `tb_post_img` VALUES (72, 'img/postImg/cba7d0ed321d46ae83cda94a4fa23892.jpg', 46);
INSERT INTO `tb_post_img` VALUES (73, 'img/postImg/5d73b1ade5b94ddfaf663b070a871763.jpg', 46);
INSERT INTO `tb_post_img` VALUES (74, 'img/postImg/6a05870eda0c4e05bad63fab1e72bc11.jpg', 46);
INSERT INTO `tb_post_img` VALUES (75, 'img/postImg/86cebea28c734163989edbc4a250885f.jpg', 46);
INSERT INTO `tb_post_img` VALUES (76, 'img/postImg/68700f4b22cc48ce873c6118edd9b4ba.jpg', 46);

-- ----------------------------
-- Table structure for tb_reporting
-- ----------------------------
DROP TABLE IF EXISTS `tb_reporting`;
CREATE TABLE `tb_reporting`  (
  `reporting_id` int NOT NULL AUTO_INCREMENT COMMENT '举报操作id',
  `user_id` int NOT NULL COMMENT '用户id',
  `reporting_category_id` int NOT NULL COMMENT '举报类别id',
  `comment_id` int NULL DEFAULT NULL COMMENT '评论id',
  `post_id` int NULL DEFAULT NULL COMMENT '帖子id',
  PRIMARY KEY (`reporting_id`) USING BTREE,
  INDEX `user_reporting_id`(`user_id`) USING BTREE,
  INDEX `reporting_comment_id`(`comment_id`) USING BTREE,
  INDEX `reporting_post_id`(`post_id`) USING BTREE,
  INDEX `reporting_categort_id`(`reporting_category_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_reporting
-- ----------------------------

-- ----------------------------
-- Table structure for tb_reporting_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_reporting_category`;
CREATE TABLE `tb_reporting_category`  (
  `reporting_category_id` int NOT NULL AUTO_INCREMENT COMMENT '举报类别ID',
  `reporting_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '举报类别',
  PRIMARY KEY (`reporting_category_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_reporting_category
-- ----------------------------

-- ----------------------------
-- Table structure for tb_right
-- ----------------------------
DROP TABLE IF EXISTS `tb_right`;
CREATE TABLE `tb_right`  (
  `right_id` int NOT NULL AUTO_INCREMENT,
  `right_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `right_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `right_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `right_dec` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `right_module_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`right_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_right
-- ----------------------------

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES (1, '系统所有者');

-- ----------------------------
-- Table structure for tb_role_right
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_right`;
CREATE TABLE `tb_role_right`  (
  `role_right_id` int NOT NULL,
  `role_id` int NOT NULL,
  `right_id` int NOT NULL,
  PRIMARY KEY (`role_right_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role_right
-- ----------------------------

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID，用户唯一标识',
  `user_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名，唯一，登录使用\r\n\r\n',
  `user_mail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户邮箱',
  `user_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户手机号，注册时用，登录时用，唯一',
  `user_password` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码，登录使用',
  `user_img_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像路径',
  `user_sign` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户个性签名',
  `user_sex` int NULL DEFAULT NULL COMMENT '用户性别：0：女；1：男',
  `user_fans_num` int NOT NULL COMMENT '用户粉丝数，初始为0',
  `user_focus_num` int NOT NULL COMMENT '用户关注数，初始为0',
  `user_likes_num` int NOT NULL COMMENT '用户获赞数，初始为0',
  `user_role` int NOT NULL COMMENT '用户角色，1为普通，0为官方，2为测试用户',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, '西风快报员', 'xfkby@gc.com', NULL, 'xfkby', 'img/avatar/official/avatar10014.png', '人类的本质是咕咕咕！', 1, 1, 0, 1, 0);
INSERT INTO `tb_user` VALUES (6, '祈1', '3012289897@qq.com', '17638250215', '111111', 'img/avatar/b9bfba743f334d86bd1e0f6eea4b6f43.jpg', '群号796484286，抖音号662402061，B站昵称，是祈鸢呀，停更', 1, 0, 1, 3, 1);
INSERT INTO `tb_user` VALUES (8, '落雨听风', 'cqk428010518@163.com', NULL, 'cqk428010518', 'img/avatar/46f28b17b5a740f6b8696c182c0b678c.jpg', '听风便是雨。。', 1, 0, 0, 1, 1);
INSERT INTO `tb_user` VALUES (10, '测试用户1', 'csyh1@gc.com', NULL, 'csyh1', 'img/avatar/official/defaultUserAvatar.png', '测试用户1', 1, 0, 0, 0, 1);
INSERT INTO `tb_user` VALUES (22, '测试用户1', 'csyh1@gc.com', NULL, 'csyh1', 'img/avatar/official/defaultUserAvatar.png', '测试用户1', 1, 0, 0, 0, 1);
INSERT INTO `tb_user` VALUES (23, '测试用户2', 'csyh2@gc.com', NULL, 'csyh2', 'img/avatar/official/defaultUserAvatar.png', '测试用户2', 1, 0, 0, 0, 1);
INSERT INTO `tb_user` VALUES (24, '测试用户3', 'csyh3@gc.com', NULL, 'csyh3', 'img/avatar/official/defaultUserAvatar.png', '测试用户3', 1, 0, 0, 0, 1);
INSERT INTO `tb_user` VALUES (25, '测试用户4', 'csyh4@gc.com', NULL, 'csyh4', 'img/avatar/official/defaultUserAvatar.png', '测试用户4', 1, 0, 0, 0, 1);
INSERT INTO `tb_user` VALUES (26, '测试用户5', 'csyh5@gc.com', NULL, 'csyh5', 'img/avatar/official/defaultUserAvatar.png', '测试用户5', 1, 0, 0, 0, 1);
INSERT INTO `tb_user` VALUES (27, '测试用户6', 'csyh6@gc.com', NULL, 'csyh6', 'img/avatar/official/defaultUserAvatar.png', '测试用户6', 1, 0, 0, 0, 1);
INSERT INTO `tb_user` VALUES (28, '测试用户7', 'csyh7@gc.com', NULL, 'csyh7', 'img/avatar/official/defaultUserAvatar.png', '测试用户7', 1, 0, 0, 0, 1);
INSERT INTO `tb_user` VALUES (29, '测试用户8', 'csyh8@gc.com', NULL, 'csyh8', 'img/avatar/official/defaultUserAvatar.png', '测试用户8', 1, 0, 0, 0, 1);
INSERT INTO `tb_user` VALUES (30, '测试用户9', 'csyh9@gc.com', NULL, 'csyh9', 'img/avatar/official/defaultUserAvatar.png', '测试用户9', 1, 0, 0, 0, 1);
INSERT INTO `tb_user` VALUES (31, '测试用户10', 'csyh10@gc.com', NULL, 'csyh10', 'img/avatar/official/defaultUserAvatar.png', '测试用户10', 1, 0, 0, 0, 1);
INSERT INTO `tb_user` VALUES (32, '测试用户11', 'csyh11@gc.com', NULL, 'csyh11', 'img/avatar/official/defaultUserAvatar.png', '测试用户11', 1, 0, 0, 0, 1);
INSERT INTO `tb_user` VALUES (33, '旅行者', '1577862505@qq.com', NULL, '123456', 'img/avatar/official/defaultUserAvatar.png', '系统原装签名，送给每一位小可爱', NULL, 0, 0, 0, 1);

SET FOREIGN_KEY_CHECKS = 1;
