/*
Navicat MySQL Data Transfer

Source Server         : babyshow
Source Server Version : 50150
Source Host           : localhost:3306
Source Database       : baby

Target Server Type    : MYSQL
Target Server Version : 50150
File Encoding         : 65001

Date: 2015-12-10 21:44:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for album
-- ----------------------------
DROP TABLE IF EXISTS `album`;
CREATE TABLE `album` (
  `aid` int(11) NOT NULL AUTO_INCREMENT,
  `aname` varchar(200) NOT NULL,
  `uid` int(11) DEFAULT NULL,
  PRIMARY KEY (`aid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of album
-- ----------------------------
INSERT INTO `album` VALUES ('1', '宝宝', '1');
INSERT INTO `album` VALUES ('2', '一月', '1');
INSERT INTO `album` VALUES ('3', '二月', '2');
INSERT INTO `album` VALUES ('4', '三月', '2');
INSERT INTO `album` VALUES ('6', '222', '1');
INSERT INTO `album` VALUES ('7', '22', '1');

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect` (
  `colid` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `nid` int(11) NOT NULL,
  PRIMARY KEY (`colid`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of collect
-- ----------------------------
INSERT INTO `collect` VALUES ('15', '1', '24');
INSERT INTO `collect` VALUES ('16', '1', '11');
INSERT INTO `collect` VALUES ('17', '1', '14');
INSERT INTO `collect` VALUES ('18', '1', '3');
INSERT INTO `collect` VALUES ('19', '1', '4');
INSERT INTO `collect` VALUES ('20', '1', '12');
INSERT INTO `collect` VALUES ('24', '1', '25');
INSERT INTO `collect` VALUES ('26', '1', '10');
INSERT INTO `collect` VALUES ('35', '1', '8');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `comid` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `nid` int(11) NOT NULL,
  `content` varchar(255) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`comid`),
  KEY `uid` (`uid`),
  KEY `nid` (`nid`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `users` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`nid`) REFERENCES `news` (`nid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES ('1', '1', '1', 'www', '2015-12-09 10:48:33');
INSERT INTO `comment` VALUES ('2', '2', '11', '3ww', '2015-12-09 10:50:48');
INSERT INTO `comment` VALUES ('3', '2', '11', '3wwwe', '2015-12-09 10:50:50');
INSERT INTO `comment` VALUES ('4', '2', '11', '3wwweyyu', '2015-12-09 10:50:54');
INSERT INTO `comment` VALUES ('5', '1', '11', 'qqq', '2015-12-09 10:53:56');
INSERT INTO `comment` VALUES ('6', '1', '11', 'qqq111', '2015-12-09 10:54:03');
INSERT INTO `comment` VALUES ('7', '1', '13', '2222222', '2015-12-09 20:28:06');
INSERT INTO `comment` VALUES ('8', '1', '14', '666666', '2015-12-10 09:29:08');
INSERT INTO `comment` VALUES ('9', '1', '14', '111', '2015-12-10 10:25:59');
INSERT INTO `comment` VALUES ('10', '1', '14', 'this is my content', '2015-12-10 11:55:43');
INSERT INTO `comment` VALUES ('11', '1', '14', 'serhgdsr', '2015-12-10 14:44:01');
INSERT INTO `comment` VALUES ('12', '1', '25', 'aehrfioh', '2015-12-10 14:44:20');
INSERT INTO `comment` VALUES ('13', '1', '25', 'tt', '2015-12-10 15:31:04');
INSERT INTO `comment` VALUES ('14', '1', '14', '12121', '2015-12-10 15:44:50');
INSERT INTO `comment` VALUES ('15', '1', '14', '121211212', '2015-12-10 15:45:00');
INSERT INTO `comment` VALUES ('16', '1', '14', '121211212232', '2015-12-10 15:45:14');
INSERT INTO `comment` VALUES ('17', '1', '25', '11', '2015-12-10 15:47:40');
INSERT INTO `comment` VALUES ('18', '1', '25', '11222', '2015-12-10 15:47:43');
INSERT INTO `comment` VALUES ('19', '1', '25', '112223', '2015-12-10 15:47:46');
INSERT INTO `comment` VALUES ('20', '1', '25', '112223', '2015-12-10 15:47:48');
INSERT INTO `comment` VALUES ('21', '1', '25', '112223', '2015-12-10 15:47:50');
INSERT INTO `comment` VALUES ('22', '1', '25', '11222312312', '2015-12-10 15:47:59');
INSERT INTO `comment` VALUES ('23', '1', '25', '11222312312123123123', '2015-12-10 15:48:02');
INSERT INTO `comment` VALUES ('24', '1', '25', '11222312312123123123', '2015-12-10 15:48:04');
INSERT INTO `comment` VALUES ('25', '1', '25', '11222312312123123123', '2015-12-10 15:48:06');
INSERT INTO `comment` VALUES ('26', '1', '25', '111', '2015-12-10 20:13:25');

-- ----------------------------
-- Table structure for friends
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends` (
  `fid` int(11) NOT NULL AUTO_INCREMENT,
  `uid1` int(11) NOT NULL,
  `uid2` int(11) NOT NULL,
  PRIMARY KEY (`fid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of friends
-- ----------------------------
INSERT INTO `friends` VALUES ('4', '2', '1');
INSERT INTO `friends` VALUES ('5', '1', '1');
INSERT INTO `friends` VALUES ('6', '2', '2');
INSERT INTO `friends` VALUES ('7', '3', '2');

-- ----------------------------
-- Table structure for images
-- ----------------------------
DROP TABLE IF EXISTS `images`;
CREATE TABLE `images` (
  `imgid` int(11) NOT NULL AUTO_INCREMENT,
  `nid` int(11) NOT NULL,
  `imgname` varchar(255) NOT NULL,
  PRIMARY KEY (`imgid`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of images
-- ----------------------------
INSERT INTO `images` VALUES ('1', '1', 'a1.png');
INSERT INTO `images` VALUES ('2', '2', 'a2.png');
INSERT INTO `images` VALUES ('3', '3', 'a3.png');
INSERT INTO `images` VALUES ('4', '4', 'a4.png');
INSERT INTO `images` VALUES ('5', '5', 'a5.png');
INSERT INTO `images` VALUES ('6', '6', 'a6.png');
INSERT INTO `images` VALUES ('7', '7', 'q1.png');
INSERT INTO `images` VALUES ('8', '8', 'q2.png');
INSERT INTO `images` VALUES ('9', '9', 'q3.png');
INSERT INTO `images` VALUES ('10', '20', 'q4.png');
INSERT INTO `images` VALUES ('11', '1', 'q5.png');
INSERT INTO `images` VALUES ('12', '2', 'w1.png');
INSERT INTO `images` VALUES ('13', '3', 'h1.png');
INSERT INTO `images` VALUES ('14', '4', 'w3.png');
INSERT INTO `images` VALUES ('15', '5', 'a1.png');
INSERT INTO `images` VALUES ('16', '6', 'q1.png');
INSERT INTO `images` VALUES ('17', '7', 'home.png');
INSERT INTO `images` VALUES ('18', '8', 'setting.png');
INSERT INTO `images` VALUES ('19', '9', 'q1.png');
INSERT INTO `images` VALUES ('20', '10', 'img1.png');
INSERT INTO `images` VALUES ('21', '11', 'h7.png');
INSERT INTO `images` VALUES ('22', '25', '13416082321171.jpg');

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `nid` int(11) NOT NULL AUTO_INCREMENT COMMENT '动态ID',
  `uid` varchar(255) NOT NULL COMMENT '用户ID',
  `date` datetime DEFAULT NULL COMMENT '动态发布日期',
  `content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`nid`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of news
-- ----------------------------
INSERT INTO `news` VALUES ('1', '1', '2015-11-28 00:00:00', '毋容置疑，好的事情总会到来，而到他到来晚时，也不为一种惊喜');
INSERT INTO `news` VALUES ('2', '2', '2015-11-16 00:00:00', '如果累了，就拉上窗帘，关上手机，关掉闹钟，深呼吸一口，放空自己');
INSERT INTO `news` VALUES ('3', '2', '2015-11-27 00:00:00', '人生最可悲的事情，莫过于胸怀大志，却又虚度光阴');
INSERT INTO `news` VALUES ('4', '2', '2015-11-04 00:00:00', '一个人的自愈的能力，才越有可能接近幸福。做一个寡言，却心有一片天海的人');
INSERT INTO `news` VALUES ('5', '2', '2015-11-11 00:00:00', '断点，只在一念之间');
INSERT INTO `news` VALUES ('6', '2', '2015-11-27 00:00:00', '哦，你也在这里，好巧哦');
INSERT INTO `news` VALUES ('7', '2', '2015-11-05 00:00:00', '不要去听别人的忽悠');
INSERT INTO `news` VALUES ('8', '2', '2015-12-05 00:00:00', '再笑，打你呀！');
INSERT INTO `news` VALUES ('9', '2', '2015-12-06 00:00:00', '这只是我的测试，不许笑 ');
INSERT INTO `news` VALUES ('10', '2', '2015-12-06 15:55:27', 'don\'t be same , be better ');
INSERT INTO `news` VALUES ('11', '2', '2015-12-06 17:38:25', 'six pics');
INSERT INTO `news` VALUES ('12', '1', '2015-12-07 10:39:33', '45345');
INSERT INTO `news` VALUES ('13', '1', '2015-12-07 20:24:33', '11111111111');
INSERT INTO `news` VALUES ('14', '1', '2015-12-07 20:25:01', '33333');
INSERT INTO `news` VALUES ('25', '3', '2015-12-10 14:12:43', 'too');

-- ----------------------------
-- Table structure for photo
-- ----------------------------
DROP TABLE IF EXISTS `photo`;
CREATE TABLE `photo` (
  `pid` int(11) NOT NULL AUTO_INCREMENT,
  `pname` varchar(255) DEFAULT NULL,
  `aid` int(11) DEFAULT NULL,
  `pdate` date NOT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of photo
-- ----------------------------
INSERT INTO `photo` VALUES ('1', 'a1.png', '1', '2015-12-01');
INSERT INTO `photo` VALUES ('2', 'a2.png', '1', '2015-12-02');
INSERT INTO `photo` VALUES ('3', 'a3.png', '1', '2015-12-03');
INSERT INTO `photo` VALUES ('4', 'a4.png', '1', '2015-12-04');
INSERT INTO `photo` VALUES ('5', 'a5.png', '1', '2015-12-05');
INSERT INTO `photo` VALUES ('6', 'a6.png', '1', '2015-12-06');
INSERT INTO `photo` VALUES ('7', 'h6.png', '1', '2015-12-07');
INSERT INTO `photo` VALUES ('8', 'h5.png', '1', '2015-12-08');
INSERT INTO `photo` VALUES ('9', 'h4.png', '1', '2015-12-09');
INSERT INTO `photo` VALUES ('10', 'h3.png', '1', '2015-12-10');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `uname` varchar(50) NOT NULL,
  `upass` varchar(50) NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `token` varchar(255) NOT NULL,
  `age` int(11) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `intro` varchar(255) DEFAULT NULL,
  `head` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', 'tp', 'tp', '千山墓雪', 'KiN6D+MCaWO48n1rL6Xu7Ii5X5pp+sZPuGWuoLfmG1FVi1AzLuKNhm+uIY3LfEmLx+Hw0MJH0vOK/aq7LW1oBw==', '11', '男', '有多少天真，遗了美好', '13416082321171.jpg');
INSERT INTO `users` VALUES ('2', 'to', 'to', '兔兔', 'mx7dBncbz8DcXlIAl76MtmAT4+FOjT2hvrgm1G+oklBdyRCGfQwTN5XvF3fpWVt7Y9BSm/RFLGQAdgWi/8DLsw==', '11', '男', '我只分享经典', 'h6.png');
INSERT INTO `users` VALUES ('3', '11', '11', 'years', 'E5Tuyb1nPznxM//ZzCYiqIt5fjM77iGoU4I5HHb4PuUsGr/Ns1WzZXuf5MR/NFecxU28YXO6zjM=', '0', '男', 'be Best ', 'q1.png');

-- ----------------------------
-- Table structure for zan
-- ----------------------------
DROP TABLE IF EXISTS `zan`;
CREATE TABLE `zan` (
  `zid` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `nid` int(11) NOT NULL,
  PRIMARY KEY (`zid`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zan
-- ----------------------------
INSERT INTO `zan` VALUES ('1', '2', '19');
INSERT INTO `zan` VALUES ('2', '2', '21');
INSERT INTO `zan` VALUES ('3', '1', '21');
INSERT INTO `zan` VALUES ('4', '1', '18');
INSERT INTO `zan` VALUES ('5', '1', '10');
INSERT INTO `zan` VALUES ('6', '1', '16');
INSERT INTO `zan` VALUES ('7', '1', '19');
INSERT INTO `zan` VALUES ('8', '1', '13');
INSERT INTO `zan` VALUES ('9', '1', '17');
INSERT INTO `zan` VALUES ('10', '1', '15');
INSERT INTO `zan` VALUES ('11', '1', '14');
INSERT INTO `zan` VALUES ('12', '2', '13');
INSERT INTO `zan` VALUES ('13', '1', '24');
INSERT INTO `zan` VALUES ('14', '1', '11');
INSERT INTO `zan` VALUES ('15', '1', '11');
INSERT INTO `zan` VALUES ('16', '1', '24');
INSERT INTO `zan` VALUES ('17', '1', '24');
INSERT INTO `zan` VALUES ('18', '1', '4');
INSERT INTO `zan` VALUES ('19', '1', '12');
INSERT INTO `zan` VALUES ('20', '1', '25');
INSERT INTO `zan` VALUES ('21', '1', '8');