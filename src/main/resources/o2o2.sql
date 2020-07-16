-- MySQL dump 10.13  Distrib 5.7.26, for Win64 (x86_64)
--
-- Host: localhost    Database: o2o
-- ------------------------------------------------------
-- Server version	5.7.26-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tb_area`
--

DROP TABLE IF EXISTS `tb_area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_area` (
  `area_id` int(2) NOT NULL AUTO_INCREMENT,
  `area_name` varchar(200) NOT NULL,
  `priority` int(2) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`area_id`),
  UNIQUE KEY `UK_AREA` (`area_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_area`
--

LOCK TABLES `tb_area` WRITE;
/*!40000 ALTER TABLE `tb_area` DISABLE KEYS */;
INSERT INTO `tb_area` VALUES (1,'东苑',2,'2018-08-08 12:12:12','2018-08-08 13:13:13'),(2,'西苑',1,'2018-08-08 12:12:12','2018-08-08 13:13:13'),(3,'南苑',3,'2018-08-08 12:12:12','2018-08-08 13:13:13'),(4,'北苑',1,'2018-08-08 12:12:12','2018-08-08 13:13:13');
/*!40000 ALTER TABLE `tb_area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_head_line`
--

DROP TABLE IF EXISTS `tb_head_line`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_head_line` (
  `line_id` int(100) NOT NULL AUTO_INCREMENT,
  `line_name` varchar(1000) DEFAULT NULL,
  `line_link` varchar(2000) NOT NULL,
  `line_img` varchar(2000) NOT NULL,
  `priority` int(2) DEFAULT NULL,
  `enable_status` int(2) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`line_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_head_line`
--

LOCK TABLES `tb_head_line` WRITE;
/*!40000 ALTER TABLE `tb_head_line` DISABLE KEYS */;
INSERT INTO `tb_head_line` VALUES (1,'测试头条','测试','/upload/item/shopcategory/hl1.jpg',3,1,NULL,NULL),(2,'测试头条2','测试2','/upload/item/shopcategory/hl2.jpg',2,1,NULL,NULL),(3,'测试头条3','测试3','/upload/item/shopcategory/hl3.jpg',1,1,NULL,NULL);
/*!40000 ALTER TABLE `tb_head_line` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_local_auth`
--

DROP TABLE IF EXISTS `tb_local_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_local_auth` (
  `local_auth_id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `username` varchar(128) NOT NULL,
  `password` varchar(128) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`local_auth_id`),
  UNIQUE KEY `uk_local_profile` (`username`),
  KEY `fk_localauth_profile` (`user_id`),
  CONSTRAINT `fk_localauth_profile` FOREIGN KEY (`user_id`) REFERENCES `tb_person_info` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_local_auth`
--

LOCK TABLES `tb_local_auth` WRITE;
/*!40000 ALTER TABLE `tb_local_auth` DISABLE KEYS */;
INSERT INTO `tb_local_auth` VALUES (4,1,'zhangsan','50s2y25265q65s2lsq226906y55blqby','2020-04-22 14:29:46','2020-04-22 14:33:21');
/*!40000 ALTER TABLE `tb_local_auth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_person_info`
--

DROP TABLE IF EXISTS `tb_person_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_person_info` (
  `user_id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `profile_img` varchar(1024) DEFAULT NULL,
  `email` varchar(1024) DEFAULT NULL,
  `gendar` varchar(2) DEFAULT NULL,
  `enable_status` int(2) NOT NULL DEFAULT '0' COMMENT '0表示禁用，1表示激活',
  `user_type` int(2) NOT NULL DEFAULT '1' COMMENT '1.顾客 2.店家 3.超级管理员',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_person_info`
--

LOCK TABLES `tb_person_info` WRITE;
/*!40000 ALTER TABLE `tb_person_info` DISABLE KEYS */;
INSERT INTO `tb_person_info` VALUES (1,'测试用户','test','test','1',1,2,NULL,NULL),(3,'测试一下',NULL,NULL,NULL,1,1,'2020-04-21 17:36:19',NULL);
/*!40000 ALTER TABLE `tb_person_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_product`
--

DROP TABLE IF EXISTS `tb_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_product` (
  `product_id` int(100) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(100) NOT NULL,
  `product_desc` varchar(2000) DEFAULT NULL,
  `img_addr` varchar(2000) DEFAULT '',
  `normal_price` varchar(100) DEFAULT NULL,
  `promotion_price` varchar(100) DEFAULT NULL,
  `priority` int(2) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  `enable_status` int(2) NOT NULL DEFAULT '0',
  `product_category_id` int(11) DEFAULT NULL,
  `shop_id` int(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`product_id`),
  KEY `fk_product_procate` (`product_category_id`),
  KEY `fk_product_shop` (`shop_id`),
  CONSTRAINT `fk_product_procate` FOREIGN KEY (`product_category_id`) REFERENCES `tb_product_category` (`product_category_id`),
  CONSTRAINT `fk_product_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_product`
--

LOCK TABLES `tb_product` WRITE;
/*!40000 ALTER TABLE `tb_product` DISABLE KEYS */;
INSERT INTO `tb_product` VALUES (1,'不正式的商品','不正式的商品描述','\\upload\\item\\shop\\1\\2020041700281535083.jpg','99','80',3,'2020-04-16 16:40:27','2020-04-17 15:44:17',0,15,1),(2,'不知道的茗茶','一种茶','\\upload\\item\\shop\\2\\2020041722341713975.jpg','7','5',3,'2020-04-16 16:40:27','2020-04-22 17:38:51',1,13,2),(3,'爱疯手机','某疯牌子手机','\\upload\\item\\shop\\1\\2020041617414875991.jpg','99','66',9,'2020-04-16 17:41:48','2020-04-17 15:42:12',1,12,1),(4,'珍珠奶茶','珍珠奶茶123','\\upload\\item\\shop\\6\\2020041621580964427.jpg','12','8',4,'2020-04-16 21:58:09','2020-04-17 22:36:10',1,9,6),(5,'诺鸡呀','一部诺鸡呀','\\upload\\item\\shop\\1\\2020041722414131074.jpg','8848','48',2,'2020-04-17 22:41:41','2020-04-17 22:41:41',1,12,1),(6,'谢','鞋鞋','\\upload\\item\\shop\\7\\2020042223132256263.jpg','77','66',8,'2020-04-22 23:10:36','2020-04-22 23:13:21',1,16,7);
/*!40000 ALTER TABLE `tb_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_product_category`
--

DROP TABLE IF EXISTS `tb_product_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_product_category` (
  `product_category_id` int(11) NOT NULL AUTO_INCREMENT,
  `product_category_name` varchar(100) NOT NULL,
  `priority` int(2) DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `shop_id` int(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`product_category_id`),
  KEY `fk_procate_shop` (`shop_id`),
  CONSTRAINT `fk_procate_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_product_category`
--

LOCK TABLES `tb_product_category` WRITE;
/*!40000 ALTER TABLE `tb_product_category` DISABLE KEYS */;
INSERT INTO `tb_product_category` VALUES (9,'珍珠奶茶',10,NULL,6),(10,'普通奶茶',20,NULL,6),(12,'二手手机',4,NULL,1),(13,'茗茶',1,NULL,2),(14,'甜品',2,NULL,2),(15,'二手老人机',3,NULL,1),(16,'鞋垫',9,NULL,7),(17,'拖鞋',8,NULL,7);
/*!40000 ALTER TABLE `tb_product_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_product_img`
--

DROP TABLE IF EXISTS `tb_product_img`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_product_img` (
  `product_img_id` int(20) NOT NULL AUTO_INCREMENT,
  `img_addr` varchar(2000) NOT NULL,
  `img_desc` varchar(2000) DEFAULT NULL,
  `priority` int(2) DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `product_id` int(20) DEFAULT NULL,
  PRIMARY KEY (`product_img_id`),
  KEY `fk_proimg_product` (`product_id`),
  CONSTRAINT `fk_proimg_product` FOREIGN KEY (`product_id`) REFERENCES `tb_product` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_product_img`
--

LOCK TABLES `tb_product_img` WRITE;
/*!40000 ALTER TABLE `tb_product_img` DISABLE KEYS */;
INSERT INTO `tb_product_img` VALUES (7,'\\upload\\item\\shop\\1\\2020041617414937760.jpg',NULL,NULL,'2020-04-16 17:41:50',3),(8,'\\upload\\item\\shop\\1\\2020041617414916156.jpg',NULL,NULL,'2020-04-16 17:41:50',3),(9,'\\upload\\item\\shop\\6\\2020041621581034117.jpg',NULL,NULL,'2020-04-16 21:58:11',4),(10,'\\upload\\item\\shop\\6\\2020041621581091310.jpg',NULL,NULL,'2020-04-16 21:58:11',4),(11,'\\upload\\item\\shop\\6\\2020041621581147632.jpg',NULL,NULL,'2020-04-16 21:58:11',4),(14,'\\upload\\item\\shop\\1\\2020041700281620566.jpg',NULL,NULL,'2020-04-17 00:28:17',1),(15,'\\upload\\item\\shop\\1\\2020041700281658172.jpg',NULL,NULL,'2020-04-17 00:28:17',1),(16,'\\upload\\item\\shop\\2\\2020041722341746482.jpg',NULL,NULL,'2020-04-17 22:34:18',2),(17,'\\upload\\item\\shop\\2\\2020041722341743325.jpg',NULL,NULL,'2020-04-17 22:34:18',2),(18,'\\upload\\item\\shop\\1\\2020041722414248257.jpg',NULL,NULL,'2020-04-17 22:41:42',5),(21,'\\upload\\item\\shop\\7\\2020042223132220373.jpg',NULL,NULL,'2020-04-22 23:13:22',6);
/*!40000 ALTER TABLE `tb_product_img` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_shop`
--

DROP TABLE IF EXISTS `tb_shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop` (
  `shop_id` int(10) NOT NULL AUTO_INCREMENT,
  `owner_id` int(10) NOT NULL COMMENT '店铺创始人',
  `area_id` int(5) DEFAULT NULL,
  `shop_category_id` int(11) DEFAULT NULL,
  `shop_name` varchar(256) NOT NULL,
  `shop_desc` varchar(1024) DEFAULT NULL,
  `shop_addr` varchar(200) DEFAULT NULL,
  `phone` varchar(128) DEFAULT NULL,
  `shop_img` varchar(1024) DEFAULT NULL,
  `priority` int(3) DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  `enable_status` int(2) NOT NULL DEFAULT '0',
  `advice` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`shop_id`),
  KEY `fk_shop_area` (`area_id`),
  KEY `fk_shop_profile` (`owner_id`),
  KEY `fk_shop_shopcate` (`shop_category_id`),
  CONSTRAINT `fk_shop_area` FOREIGN KEY (`area_id`) REFERENCES `tb_area` (`area_id`),
  CONSTRAINT `fk_shop_profile` FOREIGN KEY (`owner_id`) REFERENCES `tb_person_info` (`user_id`),
  CONSTRAINT `fk_shop_shopcate` FOREIGN KEY (`shop_category_id`) REFERENCES `tb_shop_category` (`shop_category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_shop`
--

LOCK TABLES `tb_shop` WRITE;
/*!40000 ALTER TABLE `tb_shop` DISABLE KEYS */;
INSERT INTO `tb_shop` VALUES (1,1,3,8,'某二手手机店铺','各种二手手机','一个正式地址','1234567890','\\upload\\item\\shop\\1\\2020041721490910470.jpg',2,'2020-04-14 14:06:46','2020-04-17 21:49:09',1,'通过'),(2,1,1,3,'香喷喷奶茶店','香喷喷奶茶店','东苑xxx号','1234567890','\\upload\\item\\shop\\2\\2020041720370254491.jpg',6,'2020-04-14 15:59:16','2020-04-17 20:37:04',1,'通过'),(3,1,2,1,'测试店铺2','test2','test2','1234567890','\\upload\\item\\shop\\3\\2020041416172242543.jpg',1,'2020-04-14 16:17:22','2020-04-14 16:17:22',0,'审核中'),(5,1,3,2,'一个咖啡店','各种各样的咖啡','随便写的','12345678901','\\upload\\item\\shop\\5\\2020041721473275567.jpg',4,'2020-04-14 22:27:23','2020-04-17 21:47:33',1,'通过'),(6,1,2,3,'无名的奶茶店','这是无名的奶茶店','这是无名的奶茶店的地址','11111111113','\\upload\\item\\shop\\6\\2020041721363032720.jpg',3,'2020-04-16 00:57:46','2020-04-17 21:36:31',1,'通过'),(7,1,3,9,'儿童鞋店','儿 童 鞋 店','不知道',NULL,'\\upload\\item\\shop\\7\\2020041721543567958.jpg',NULL,'2020-04-17 21:54:35','2020-04-22 23:09:48',1,NULL);
/*!40000 ALTER TABLE `tb_shop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_shop_category`
--

DROP TABLE IF EXISTS `tb_shop_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop_category` (
  `shop_category_id` int(11) NOT NULL AUTO_INCREMENT,
  `shop_category_name` varchar(100) NOT NULL DEFAULT '',
  `shop_category_desc` varchar(1000) DEFAULT '',
  `shop_category_img` varchar(2000) DEFAULT NULL,
  `priority` int(2) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`shop_category_id`),
  KEY `fk_shop_category_self` (`parent_id`),
  CONSTRAINT `fk_shop_category_self` FOREIGN KEY (`parent_id`) REFERENCES `tb_shop_category` (`shop_category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_shop_category`
--

LOCK TABLES `tb_shop_category` WRITE;
/*!40000 ALTER TABLE `tb_shop_category` DISABLE KEYS */;
INSERT INTO `tb_shop_category` VALUES (1,'咖啡奶茶','咖啡奶茶','/upload/item/shopcategory/nc.jpg',4,NULL,NULL,NULL),(2,'咖啡','咖啡','test2',0,NULL,NULL,1),(3,'奶茶',' 奶茶','test3',1,NULL,NULL,1),(4,'二手市场','二手市场交易','/upload/item/shopcategory/sj.jpg',6,NULL,NULL,NULL),(5,'美容美发','美容美发','/upload/item/shopcategory/xhr.jpg',5,NULL,NULL,NULL),(6,'休闲娱乐','休闲娱乐','/upload/item/shopcategory/ip.jpg',3,NULL,NULL,NULL),(7,'培训机构','培训机构','/upload/item/shopcategory/xz.jpg',2,NULL,NULL,NULL),(8,'二手手机','二手市场市场',NULL,3,NULL,NULL,4),(9,'二手鞋子','二手鞋子市场',NULL,3,NULL,NULL,4);
/*!40000 ALTER TABLE `tb_shop_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_wechat_auth`
--

DROP TABLE IF EXISTS `tb_wechat_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_wechat_auth` (
  `wechat_auth_id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `open_id` varchar(1024) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`wechat_auth_id`),
  UNIQUE KEY `open_id` (`open_id`),
  KEY `fk_wechatauth_profile` (`user_id`),
  CONSTRAINT `fk_wechatauth_profile` FOREIGN KEY (`user_id`) REFERENCES `tb_person_info` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_wechat_auth`
--

LOCK TABLES `tb_wechat_auth` WRITE;
/*!40000 ALTER TABLE `tb_wechat_auth` DISABLE KEYS */;
INSERT INTO `tb_wechat_auth` VALUES (1,1,'dafahizhfdhaih','2020-04-21 17:22:35'),(3,3,'dafahizhfdhaih2','2020-04-21 17:36:19');
/*!40000 ALTER TABLE `tb_wechat_auth` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-17  0:07:18
