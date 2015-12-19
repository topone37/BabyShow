/*
Navicat MySQL Data Transfer

Source Server         : babyshow
Source Server Version : 50150
Source Host           : localhost:3306
Source Database       : baby

Target Server Type    : MYSQL
Target Server Version : 50150
File Encoding         : 65001

Date: 2015-12-15 21:59:59
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of album
-- ----------------------------
INSERT INTO `album` VALUES ('1', '宝宝', '1');
INSERT INTO `album` VALUES ('3', '二月', '2');
INSERT INTO `album` VALUES ('4', '三月', '2');
INSERT INTO `album` VALUES ('7', '22', '1');
INSERT INTO `album` VALUES ('9', '111', '3');

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect` (
  `colid` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `nid` int(11) NOT NULL,
  PRIMARY KEY (`colid`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of collect
-- ----------------------------
INSERT INTO `collect` VALUES ('15', '1', '24');
INSERT INTO `collect` VALUES ('16', '1', '11');
INSERT INTO `collect` VALUES ('17', '1', '14');
INSERT INTO `collect` VALUES ('19', '1', '4');
INSERT INTO `collect` VALUES ('35', '1', '8');
INSERT INTO `collect` VALUES ('36', '1', '10');
INSERT INTO `collect` VALUES ('37', '1', '3');
INSERT INTO `collect` VALUES ('38', '1', '2');
INSERT INTO `collect` VALUES ('39', '3', '4');
INSERT INTO `collect` VALUES ('40', '3', '9');
INSERT INTO `collect` VALUES ('41', '1', '9');
INSERT INTO `collect` VALUES ('42', '1', '7');

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
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES ('27', '1', '2', 'w34uj', '2015-12-11 20:06:32');
INSERT INTO `comment` VALUES ('28', '1', '9', '222', '2015-12-12 18:44:06');
INSERT INTO `comment` VALUES ('29', '1', '9', '22233', '2015-12-12 18:44:08');
INSERT INTO `comment` VALUES ('30', '1', '9', '12212121', '2015-12-12 19:11:48');
INSERT INTO `comment` VALUES ('31', '1', '13', '28', '2015-12-15 21:04:59');
INSERT INTO `comment` VALUES ('32', '1', '12', 'zjzn', '2015-12-15 21:05:20');
INSERT INTO `comment` VALUES ('33', '1', '12', 'z不错不错', '2015-12-15 21:05:28');
INSERT INTO `comment` VALUES ('34', '1', '13', '不错哦', '2015-12-15 21:54:03');
INSERT INTO `comment` VALUES ('35', '1', '13', '的', '2015-12-15 21:54:38');
INSERT INTO `comment` VALUES ('36', '1', '13', '默', '2015-12-15 21:54:49');
INSERT INTO `comment` VALUES ('37', '1', '13', '╭(°A°`)╮', '2015-12-15 21:55:06');
INSERT INTO `comment` VALUES ('38', '1', '12', '的', '2015-12-15 21:55:25');
INSERT INTO `comment` VALUES ('39', '1', '12', '的额呃呃呃', '2015-12-15 21:55:30');
INSERT INTO `comment` VALUES ('40', '1', '12', '的额呃呃呃噢噢噢呃呃呃', '2015-12-15 21:55:40');
INSERT INTO `comment` VALUES ('41', '1', '13', '的', '2015-12-15 21:55:48');
INSERT INTO `comment` VALUES ('42', '1', '12', '&new', '2015-12-15 21:57:21');
INSERT INTO `comment` VALUES ('43', '1', '12', '&new(๑•ั็ω•็ั๑)', '2015-12-15 21:57:27');

-- ----------------------------
-- Table structure for friends
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends` (
  `fid` int(11) NOT NULL AUTO_INCREMENT,
  `uid1` int(11) NOT NULL,
  `uid2` int(11) NOT NULL,
  PRIMARY KEY (`fid`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of friends
-- ----------------------------
INSERT INTO `friends` VALUES ('4', '2', '1');
INSERT INTO `friends` VALUES ('5', '1', '1');
INSERT INTO `friends` VALUES ('6', '2', '2');
INSERT INTO `friends` VALUES ('7', '3', '2');
INSERT INTO `friends` VALUES ('8', '3', '1');
INSERT INTO `friends` VALUES ('9', '6', '1');
INSERT INTO `friends` VALUES ('10', '6', '2');
INSERT INTO `friends` VALUES ('11', '6', '3');
INSERT INTO `friends` VALUES ('12', '6', '4');
INSERT INTO `friends` VALUES ('13', '6', '5');
INSERT INTO `friends` VALUES ('14', '6', '6');
INSERT INTO `friends` VALUES ('15', '4', '1');
INSERT INTO `friends` VALUES ('16', '7', '1');
INSERT INTO `friends` VALUES ('17', '5', '1');

-- ----------------------------
-- Table structure for images
-- ----------------------------
DROP TABLE IF EXISTS `images`;
CREATE TABLE `images` (
  `imgid` int(11) NOT NULL AUTO_INCREMENT,
  `nid` int(11) NOT NULL,
  `imgname` varchar(255) NOT NULL,
  PRIMARY KEY (`imgid`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

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
INSERT INTO `images` VALUES ('23', '10', '13416082321171.jpg');
INSERT INTO `images` VALUES ('24', '11', '13416082321171.jpg');
INSERT INTO `images` VALUES ('25', '12', '20151212_032700.jpg');
INSERT INTO `images` VALUES ('26', '13', 'w4.png');
INSERT INTO `images` VALUES ('27', '13', 'q3.png');
INSERT INTO `images` VALUES ('28', '13', 'img1.png');
INSERT INTO `images` VALUES ('29', '13', 'w2.png');
INSERT INTO `images` VALUES ('30', '13', 'w5.png');
INSERT INTO `images` VALUES ('31', '13', 'a5.png');

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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of news
-- ----------------------------
INSERT INTO `news` VALUES ('2', '2', '2015-11-16 00:00:00', '如果累了，就拉上窗帘，关上手机，关掉闹钟，深呼吸一口，放空自己');
INSERT INTO `news` VALUES ('3', '2', '2015-11-27 00:00:00', '人生最可悲的事情，莫过于胸怀大志，却又虚度光阴');
INSERT INTO `news` VALUES ('4', '2', '2015-11-04 00:00:00', '一个人的自愈的能力，才越有可能接近幸福。做一个寡言，却心有一片天海的人');
INSERT INTO `news` VALUES ('5', '2', '2015-11-11 00:00:00', '断点，只在一念之间');
INSERT INTO `news` VALUES ('6', '2', '2015-11-27 00:00:00', '哦，你也在这里，好巧哦');
INSERT INTO `news` VALUES ('7', '2', '2015-11-05 00:00:00', '不要去听别人的忽悠');
INSERT INTO `news` VALUES ('8', '2', '2015-12-05 00:00:00', '再笑，打你呀！');
INSERT INTO `news` VALUES ('9', '2', '2015-12-06 00:00:00', '这只是我的测试，不许笑 ');
INSERT INTO `news` VALUES ('10', '1', '2015-12-13 19:14:00', '324234');
INSERT INTO `news` VALUES ('11', '1', '2015-12-13 19:14:14', '');
INSERT INTO `news` VALUES ('12', '1', '2015-12-13 19:17:50', '223213');
INSERT INTO `news` VALUES ('13', '1', '2015-12-14 13:35:27', 'this  is my test content !');

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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

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
INSERT INTO `photo` VALUES ('11', '20151212_032814.jpg', '9', '2015-12-12');
INSERT INTO `photo` VALUES ('12', '20151212_032814.jpg', '9', '2015-12-12');
INSERT INTO `photo` VALUES ('13', '20151213_003406.jpg', '7', '2015-12-13');

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', 'tp', 'tp', '千山墓雪', 'KiN6D+MCaWO48n1rL6Xu7Ii5X5pp+sZPuGWuoLfmG1FVi1AzLuKNhm+uIY3LfEmLx+Hw0MJH0vOK/aq7LW1oBw==', '11', '男', '有多少天真，遗了美好', '13416082321171.jpg');
INSERT INTO `users` VALUES ('2', 'to', 'to', '兔兔', 'mx7dBncbz8DcXlIAl76MtmAT4+FOjT2hvrgm1G+oklBdyRCGfQwTN5XvF3fpWVt7Y9BSm/RFLGQAdgWi/8DLsw==', '11', '男', '我只分享经典', 'h6.png');
INSERT INTO `users` VALUES ('3', '11', '11', 'years', 'E5Tuyb1nPznxM//ZzCYiqIt5fjM77iGoU4I5HHb4PuUsGr/Ns1WzZXuf5MR/NFecxU28YXO6zjM=', '0', '男', 'be Best ', 'q1.png');
INSERT INTO `users` VALUES ('4', '22', '22', '123', 'tyhfMHlmaDK9QbZ0EVVIooi5X5pp+sZPuGWuoLfmG1G4whGtc0SN/WA2iRJPom0gb7lHA04zca2K/aq7LW1oBw==', '0', '男', '123', 'a2.png');
INSERT INTO `users` VALUES ('5', '33', '33', '123', '68wjGDDsSnEXV6/tr0j8tWJepK9OqL+ijf5na+m+pBU7a+s4GfUiF4bkdI9vAjUm6pIq4C7/pcg=', '0', '男', '123', 'a2.png');
INSERT INTO `users` VALUES ('6', '44', '44', '123', '7c4siMKfIao7YaY3zqYrlYi5X5pp+sZPuGWuoLfmG1G4whGtc0SN/cz8Ey7MCHulEfRUk2WLmqOK/aq7LW1oBw==', '0', '女', '123', 'a2.png');
INSERT INTO `users` VALUES ('7', 'qq', 'qq', '无名氏', 'slqI6+yQUl1a3PH8y/4j+oi5X5pp+sZPuGWuoLfmG1G4whGtc0SN/Z01KnMznux8QVteHtugyVmK/aq7LW1oBw==', '21', '男', '这家伙太懒了,什么也没有留下...', 'a2.png');
INSERT INTO `users` VALUES ('8', '1', '', '无名氏', '4ovBIuNy5OI1v0DidDj/0oi5X5pp+sZPuGWuoLfmG1G4whGtc0SN/dzCBH7Y1FxWBVVwd6XdkECK/aq7LW1oBw==', '21', '男', '这家伙太懒了,什么也没有留下...', 'a2.png');
INSERT INTO `users` VALUES ('9', '1', '', '无名氏', 'K5/MHOOh3c7hvr+tXQPpwIt5fjM77iGoU4I5HHb4PuVYI7zANMGMbdo7xeg74OPNPZNKU02c69o=', '21', '男', '这家伙太懒了,什么也没有留下...', 'a2.png');

-- ----------------------------
-- Table structure for zan
-- ----------------------------
DROP TABLE IF EXISTS `zan`;
CREATE TABLE `zan` (
  `zid` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `nid` int(11) NOT NULL,
  PRIMARY KEY (`zid`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

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
INSERT INTO `zan` VALUES ('20', '1', '25');
INSERT INTO `zan` VALUES ('21', '1', '8');
INSERT INTO `zan` VALUES ('22', '1', '2');
INSERT INTO `zan` VALUES ('23', '3', '9');
INSERT INTO `zan` VALUES ('24', '3', '4');
INSERT INTO `zan` VALUES ('25', '1', '9');
INSERT INTO `zan` VALUES ('26', '1', '7');
INSERT INTO `zan` VALUES ('27', '1', '3');
INSERT INTO `zan` VALUES ('28', '1', '1');
INSERT INTO `zan` VALUES ('29', '1', '5');
INSERT INTO `zan` VALUES ('30', '1', '12');
