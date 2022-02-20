-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: 47.99.75.151    Database: boot-tutu-mall
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
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
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_area` (
  `area_id` int NOT NULL AUTO_INCREMENT,
  `area_name` varchar(200) NOT NULL,
  `priority` int NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`area_id`),
  UNIQUE KEY `UK_AREA` (`area_name`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_area`
--

LOCK TABLES `tb_area` WRITE;
/*!40000 ALTER TABLE `tb_area` DISABLE KEYS */;
INSERT INTO `tb_area` VALUES (5,'金水区',46,'2020-09-20 13:55:54','2020-09-20 13:55:58'),(6,'二七区',45,'2020-09-02 13:56:10','2020-09-20 13:56:14'),(7,'管城区',78,'2020-09-02 13:56:51','2020-11-24 10:09:20'),(8,'郑州新区',19,'2020-09-25 14:09:23','2020-11-17 13:10:05'),(18,'惠济区',23,'2021-05-04 15:21:39','2021-05-04 15:21:41'),(19,'中原区',45,'2021-05-04 15:22:03','2021-05-04 15:22:05');
/*!40000 ALTER TABLE `tb_area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_award`
--

DROP TABLE IF EXISTS `tb_award`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_award` (
  `award_id` int NOT NULL AUTO_INCREMENT,
  `award_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `award_description` varchar(1024) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `award_image` varchar(1024) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `point` int NOT NULL DEFAULT '0',
  `priority` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  `enable_status` int NOT NULL DEFAULT '0',
  `shop_id` int DEFAULT NULL,
  PRIMARY KEY (`award_id`),
  KEY `fk_award_shop_id` (`shop_id`),
  CONSTRAINT `fk_award_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_award`
--

LOCK TABLES `tb_award` WRITE;
/*!40000 ALTER TABLE `tb_award` DISABLE KEYS */;
INSERT INTO `tb_award` VALUES (11,'坚果彩虹数据线','七彩配色随机发货，为生活增添一份小小惊喜','\\upload\\item\\shop\\85\\202105046769.jpg',100,20,'2021-05-04 09:35:53','2021-05-05 07:35:07',1,85);
/*!40000 ALTER TABLE `tb_award` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_head_line`
--

DROP TABLE IF EXISTS `tb_head_line`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_head_line` (
  `line_id` int NOT NULL AUTO_INCREMENT,
  `line_name` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `line_link` varchar(2000) NOT NULL,
  `line_image` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `priority` int DEFAULT NULL,
  `enable_status` int NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`line_id`),
  UNIQUE KEY `line_name` (`line_name`)
) ENGINE=InnoDB AUTO_INCREMENT=157 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_head_line`
--

LOCK TABLES `tb_head_line` WRITE;
/*!40000 ALTER TABLE `tb_head_line` DISABLE KEYS */;
INSERT INTO `tb_head_line` VALUES (150,'头条一','https://www.taobao.com/','\\upload\\item\\headtitle\\banner1.jpg',4,1,'2020-11-19 16:33:42','2020-12-02 08:00:05'),(151,'头条二','https://www.taobao.com/','\\upload\\item\\headtitle\\banner2.jpg',6,1,'2020-11-19 16:34:30','2020-11-21 11:12:06'),(152,'头条三','https://www.taobao.com/','\\upload\\item\\headtitle\\banner3.jpg',7,1,'2020-11-19 16:35:04','2020-11-24 10:15:36'),(153,'头条四','https://www.taobao.com/','\\upload\\item\\headtitle\\banner4.jpg',9,1,'2020-11-19 16:35:24','2020-12-02 09:49:48');
/*!40000 ALTER TABLE `tb_head_line` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_local_auth`
--

DROP TABLE IF EXISTS `tb_local_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_local_auth` (
  `local_auth_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `username` varchar(128) NOT NULL,
  `password` varchar(128) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`local_auth_id`),
  UNIQUE KEY `uk_local_profile` (`username`),
  KEY `fk_localauth_profile` (`user_id`),
  CONSTRAINT `fk_localauth_profile` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_local_auth`
--

LOCK TABLES `tb_local_auth` WRITE;
/*!40000 ALTER TABLE `tb_local_auth` DISABLE KEYS */;
INSERT INTO `tb_local_auth` VALUES (2,3,'qiming','d4db4238b0ca23820edd50ab6g7584ac','2020-11-11 12:55:09','2021-05-05 02:58:50'),(3,5,'user01','d4db4238b0ca23820edd50ab6g7584ac','2020-12-02 17:15:40','2021-05-05 07:19:45'),(5,7,'boss01','6a8e51b1ae8b121df5814aae7c701668','2021-05-02 12:40:09','2021-05-03 12:31:33'),(6,9,'zhouqingwei','6a8e51b1ae8b121df5814aae7c701668','2021-05-05 07:23:26','2021-05-05 07:23:26');
/*!40000 ALTER TABLE `tb_local_auth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_product`
--

DROP TABLE IF EXISTS `tb_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_product` (
  `point` int DEFAULT NULL,
  `product_id` int NOT NULL AUTO_INCREMENT,
  `product_name` varchar(100) NOT NULL,
  `product_description` varchar(2000) DEFAULT NULL,
  `image_address` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `normal_price` varchar(100) DEFAULT NULL,
  `promotion_price` varchar(100) DEFAULT NULL,
  `priority` int NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  `enable_status` int NOT NULL DEFAULT '0',
  `product_category_id` int DEFAULT NULL,
  `shop_id` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `product_name` (`product_name`),
  KEY `fk_product_category` (`product_category_id`),
  KEY `fk_product_shop` (`shop_id`),
  CONSTRAINT `fk_product_category` FOREIGN KEY (`product_category_id`) REFERENCES `tb_product_category` (`product_category_id`),
  CONSTRAINT `fk_product_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_product`
--

LOCK TABLES `tb_product` WRITE;
/*!40000 ALTER TABLE `tb_product` DISABLE KEYS */;
INSERT INTO `tb_product` VALUES (23,47,' SERGE LUTENS色唇膏','好看的唇色','\\upload\\item\\shop\\83\\202105048939.jpg','178','156',10,'2021-05-01 05:14:19','2021-05-01 05:14:19',0,65,83),(87,48,'科颜氏亚马逊白泥清洁面膜','平衡油脂分泌 活肤滋润','\\upload\\item\\shop\\83\\202105046215.jpg','449','399',16,'2021-05-01 05:25:08','2021-05-01 05:25:08',0,64,83),(23,51,'韩国ROUNDLAB独岛面膜 ','补水保湿18片/盒加送1片到手19片','\\upload\\item\\shop\\83\\202105044438.jpg','23','34',23,'2021-05-01 08:26:26','2021-05-01 08:26:26',1,64,83),(99,52,'韩国MISSHA谜尚眼线膏液体眼线笔','自然棕色持久不晕染','\\upload\\item\\shop\\83\\202105046338.jpg','49','49',23,'2021-05-01 11:21:26','2021-05-01 11:21:26',0,66,83),(190,53,'Smartisan T恤 任天堂发售红白机','100% 美国SUPIMA棉、舒适拉绒质地','\\upload\\item\\shop\\85\\202105043253.png','','149.0',23,'2021-05-04 08:35:29','2021-05-04 08:35:29',1,68,85),(160,54,'Smartisan 帆布鞋','','\\upload\\item\\shop\\85\\202105046014.jpg','','199.0',34,'2021-05-04 08:37:39','2021-05-04 08:37:39',1,70,85),(300,55,'畅呼吸智能空气净化器除甲醛版','购新空净 赠价值 699 元活性炭滤芯','\\upload\\item\\shop\\85\\202105047233.png','2999.0','2799.0',24,'2021-05-04 08:38:55','2021-05-04 08:38:55',1,71,85),(40,56,'坚果 3 \"足迹\"背贴 乐高创始人出生','','\\upload\\item\\shop\\85\\202105045584.jpg','','79.0',30,'2021-05-04 08:40:35','2021-05-04 08:40:35',1,72,85),(50,57,'坚果 3 TPU 软胶保护套','TPU 环保材质、完美贴合、周到防护','\\upload\\item\\shop\\85\\202105049707.jpg','125.0','105.0',56,'2021-05-04 08:41:18','2021-05-04 08:41:18',1,72,85),(50,58,'坚果 3 TPU 软胶透明保护套','轻薄透明、完美贴合、TPU 环保材质','\\upload\\item\\shop\\85\\202105043183.jpg','','29.0',39,'2021-05-04 08:42:00','2021-05-04 08:42:00',1,72,85),(299,59,'iphone X','IPhone X 全面屏 全面绽放','\\upload\\item\\shop\\85\\202105048755.png','','4999.0',45,'2021-05-04 08:45:17','2021-05-04 08:45:17',1,73,85),(70,60,'Smartisan 半入耳式耳机','经典配色、专业调音、高品质麦克风','\\upload\\item\\shop\\85\\202105043936.jpg','','169.0',45,'2021-05-04 09:24:42','2021-05-04 09:24:42',1,74,85),(100,61,'FIIL Diva Pro 全场景无线降噪耳机','智能语音交互、高清无损本地存储播放','\\upload\\item\\shop\\85\\202105041598.png','','299.0',45,'2021-05-04 09:25:44','2021-05-04 09:25:44',1,74,85);
/*!40000 ALTER TABLE `tb_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_product_category`
--

DROP TABLE IF EXISTS `tb_product_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_product_category` (
  `product_category_id` int NOT NULL AUTO_INCREMENT,
  `product_category_name` varchar(100) NOT NULL,
  `priority` int DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `shop_id` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`product_category_id`),
  KEY `fk_procate_shop` (`shop_id`),
  CONSTRAINT `fk_procate_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_product_category`
--

LOCK TABLES `tb_product_category` WRITE;
/*!40000 ALTER TABLE `tb_product_category` DISABLE KEYS */;
INSERT INTO `tb_product_category` VALUES (63,'护发素',12,NULL,83),(64,'面膜',11,NULL,83),(65,'唇膏',23,NULL,83),(66,'眼线',16,NULL,83),(68,'T恤',23,NULL,85),(69,'上衣',15,NULL,85),(70,'鞋子',20,NULL,85),(71,'空气净化器',21,NULL,85),(72,'配件',10,NULL,85),(73,'手机',40,NULL,85),(74,'耳机',17,NULL,85);
/*!40000 ALTER TABLE `tb_product_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_product_image`
--

DROP TABLE IF EXISTS `tb_product_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_product_image` (
  `product_image_id` int NOT NULL AUTO_INCREMENT,
  `image_address` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `image_description` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `priority` int DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  PRIMARY KEY (`product_image_id`),
  KEY `fk_product_img` (`product_id`),
  CONSTRAINT `fk_product_img` FOREIGN KEY (`product_id`) REFERENCES `tb_product` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_product_image`
--

LOCK TABLES `tb_product_image` WRITE;
/*!40000 ALTER TABLE `tb_product_image` DISABLE KEYS */;
INSERT INTO `tb_product_image` VALUES (1,'\\upload\\item\\shop\\78\\202105014468.jpg',NULL,NULL,'2021-05-01 05:14:20',47),(2,'\\upload\\item\\shop\\85\\202105049258.jpg',NULL,NULL,'2021-05-04 08:35:29',53),(3,'\\upload\\item\\shop\\85\\202105046299.jpg',NULL,NULL,'2021-05-04 08:45:17',59),(4,'\\upload\\item\\shop\\85\\202105047535.jpg',NULL,NULL,'2021-05-04 08:45:18',59);
/*!40000 ALTER TABLE `tb_product_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_product_sell_daily`
--

DROP TABLE IF EXISTS `tb_product_sell_daily`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_product_sell_daily` (
  `product_sell_daily_id` int NOT NULL AUTO_INCREMENT,
  `product_id` int DEFAULT NULL,
  `shop_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `total` int DEFAULT '0',
  PRIMARY KEY (`product_sell_daily_id`),
  UNIQUE KEY `uc_product_sell` (`product_id`,`shop_id`,`create_time`),
  KEY `fk_product_sell_product` (`product_id`),
  KEY `fk_product_sell_shop` (`shop_id`),
  CONSTRAINT `fk_product_sell_product` FOREIGN KEY (`product_id`) REFERENCES `tb_product` (`product_id`),
  CONSTRAINT `fk_product_sell_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_product_sell_daily`
--

LOCK TABLES `tb_product_sell_daily` WRITE;
/*!40000 ALTER TABLE `tb_product_sell_daily` DISABLE KEYS */;
INSERT INTO `tb_product_sell_daily` VALUES (14,56,85,'2021-05-01 15:42:52',12),(15,57,85,'2021-05-01 15:44:05',45),(16,55,85,'2021-05-01 15:45:52',14),(17,59,85,'2021-05-01 15:45:57',32),(18,54,85,'2021-05-01 15:46:00',18),(19,61,85,'2021-05-01 15:46:04',24);
/*!40000 ALTER TABLE `tb_product_sell_daily` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_shop`
--

DROP TABLE IF EXISTS `tb_shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_shop` (
  `shop_id` int NOT NULL AUTO_INCREMENT,
  `owner_id` int NOT NULL COMMENT '店铺创始人',
  `area_id` int DEFAULT NULL,
  `shop_category_id` int DEFAULT NULL,
  `shop_name` varchar(256) NOT NULL,
  `shop_description` varchar(1024) NOT NULL,
  `shop_address` varchar(200) DEFAULT NULL,
  `phone` varchar(128) DEFAULT NULL,
  `shop_image` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `priority` int DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  `enable_status` int NOT NULL DEFAULT '0',
  `advice` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`shop_id`),
  UNIQUE KEY `shop_name` (`shop_name`),
  KEY `fk_shop_area` (`area_id`),
  KEY `fk_shop_profile` (`owner_id`),
  KEY `fk_shop_category` (`shop_category_id`),
  CONSTRAINT `fk_shop_area` FOREIGN KEY (`area_id`) REFERENCES `tb_area` (`area_id`),
  CONSTRAINT `fk_shop_category` FOREIGN KEY (`shop_category_id`) REFERENCES `tb_shop_category` (`shop_category_id`),
  CONSTRAINT `fk_shop_profile` FOREIGN KEY (`owner_id`) REFERENCES `tb_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_shop`
--

LOCK TABLES `tb_shop` WRITE;
/*!40000 ALTER TABLE `tb_shop` DISABLE KEYS */;
INSERT INTO `tb_shop` VALUES (83,3,6,23,'曲曲豆姐俩','..','日本','14598743578','\\upload\\item\\shop\\83\\202105046791.jpg',NULL,'2021-05-04 07:06:02','2021-05-04 07:44:20',1,NULL),(84,3,6,23,'唐心蛋小卖部','...',' 郑州市金水区二七路与太康路交叉口西北丹尼斯大卫城F7','15673451309','\\upload\\item\\shop\\84\\202105049431.jpg',NULL,'2021-05-04 07:06:28','2021-05-04 07:34:44',1,NULL),(85,3,18,29,'锤子科技','锤子科技是一家制造移动互联网终端设备的公司，公司的使命是用完美主义的工匠精神，打造用户体验一流的数码消费类产品（智能手机为主），改善人们的生活质量','郑州市金水区祥盛街与心怡路交叉路口往西约100米(绿地·原盛国际','14598743578','\\upload\\item\\shop\\85\\202105049709.jpg',NULL,'2021-05-04 08:32:08','2021-05-04 08:32:08',1,NULL);
/*!40000 ALTER TABLE `tb_shop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_shop_auth_map`
--

DROP TABLE IF EXISTS `tb_shop_auth_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_shop_auth_map` (
  `shop_auth_id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int NOT NULL,
  `shop_id` int NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `title_flag` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  `enable_status` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`shop_auth_id`),
  KEY `fk_shop_auth_map_shop` (`shop_id`),
  KEY `uk_shop_auth_map` (`employee_id`,`shop_id`),
  CONSTRAINT `fk_shop_auth_map_employee` FOREIGN KEY (`employee_id`) REFERENCES `tb_user` (`user_id`),
  CONSTRAINT `fk_shop_auth_map_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_shop_auth_map`
--

LOCK TABLES `tb_shop_auth_map` WRITE;
/*!40000 ALTER TABLE `tb_shop_auth_map` DISABLE KEYS */;
INSERT INTO `tb_shop_auth_map` VALUES (13,3,83,'店家',0,'2021-05-04 07:06:03','2021-05-04 07:06:03',1),(14,3,84,'店家',0,'2021-05-04 07:06:28','2021-05-04 07:06:28',1),(15,3,85,'店家',0,'2021-05-04 08:32:08','2021-05-04 08:32:08',1),(17,9,85,'员工',1,'2021-05-05 05:32:24','2021-05-05 05:33:54',1);
/*!40000 ALTER TABLE `tb_shop_auth_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_shop_category`
--

DROP TABLE IF EXISTS `tb_shop_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_shop_category` (
  `shop_category_id` int NOT NULL AUTO_INCREMENT,
  `shop_category_name` varchar(100) NOT NULL DEFAULT '',
  `shop_category_description` varchar(1000) DEFAULT '',
  `shop_category_image` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `priority` int NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  `parent_id` int DEFAULT NULL,
  PRIMARY KEY (`shop_category_id`),
  KEY `fk_shop_category_self` (`parent_id`),
  CONSTRAINT `fk_shop_category_self` FOREIGN KEY (`parent_id`) REFERENCES `tb_shop_category` (`shop_category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_shop_category`
--

LOCK TABLES `tb_shop_category` WRITE;
/*!40000 ALTER TABLE `tb_shop_category` DISABLE KEYS */;
INSERT INTO `tb_shop_category` VALUES (7,'精品服饰','男装/女装/鞋类/箱包','/upload/item/shopcategory/1.png',4,'2020-09-09 13:58:59','2020-09-02 13:59:03',NULL),(8,'手机数码','智能手机/电脑/相机','/upload/item/shopcategory/2.png',5,'2020-09-02 14:05:08','2020-09-20 14:05:12',NULL),(9,'图书音像','书籍/文具','/upload/item/shopcategory/3.png',6,'2020-09-02 14:05:10','2020-09-03 14:05:15',NULL),(10,'生鲜水果','水果/下午茶/蔬菜/名酒/肉类','/upload/item/shopcategory/4.png',7,'2020-09-24 14:06:11','2020-09-09 14:06:20',NULL),(11,'母婴玩具','儿童装/孕妇装','/upload/item/shopcategory/5.png',1,'2020-09-16 14:06:14','2020-09-08 14:06:23',NULL),(12,'美妆彩妆','美容护肤/面膜/个人护理','/upload/item/shopcategory/6.png',10,'2020-09-10 14:06:17','2020-09-16 14:06:27',NULL),(23,'美容护肤','美容护肤',NULL,23,'2021-05-04 15:02:40','2021-05-04 15:02:43',12),(24,'水果蔬菜','全天特供 每日新鲜',NULL,23,'2021-05-04 15:24:58','2021-05-04 15:25:00',10),(25,'手机','',NULL,45,'2021-05-04 16:26:08','2021-05-04 16:26:11',8),(26,'耳机','',NULL,23,'2021-05-04 16:26:30','2021-05-04 16:26:32',8),(27,'男士上衣','',NULL,51,'2021-05-04 16:27:07','2021-05-04 16:27:09',7),(28,'鞋类','',NULL,23,'2021-05-04 16:28:01','2021-05-04 16:28:04',7),(29,'在线商城','官方自营',NULL,45,'2021-05-04 19:11:01','2021-05-04 19:11:03',8);
/*!40000 ALTER TABLE `tb_shop_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user`
--

DROP TABLE IF EXISTS `tb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `profile_image` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `email` varchar(1024) DEFAULT NULL,
  `gender` char(1) DEFAULT NULL,
  `enable_status` int NOT NULL DEFAULT '0',
  `user_type` int NOT NULL DEFAULT '1',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user`
--

LOCK TABLES `tb_user` WRITE;
/*!40000 ALTER TABLE `tb_user` DISABLE KEYS */;
INSERT INTO `tb_user` VALUES (3,'周启明','\\upload\\item\\user\\1.jpg','198232345@163.com','男',1,2,'2020-10-01 14:29:42','2020-09-23 14:29:45'),(5,'小红',NULL,'123@qq.com','男',1,1,'2020-10-14 17:36:38','2020-10-14 17:36:40'),(7,'小兰',NULL,'d','男',1,2,NULL,NULL),(9,'周庆伟','https://thirdwx.qlogo.cn/mmopen/vi_32/mY7SstJmaqtsJYJC0KqkblTAic0uicb1jnlepppja7M1c9pa5x8ib1ZbXPEJTDibZEGBr1CAS8Yic9BB0etmWriafwsA/132',NULL,'',1,1,'2021-05-05 05:30:59',NULL);
/*!40000 ALTER TABLE `tb_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_award_map`
--

DROP TABLE IF EXISTS `tb_user_award_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_award_map` (
  `user_award_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `award_id` int NOT NULL,
  `shop_id` int NOT NULL,
  `operator_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `used_status` int NOT NULL DEFAULT '0',
  `point` int DEFAULT NULL,
  PRIMARY KEY (`user_award_id`),
  KEY `fk_user_award_map_profile` (`user_id`),
  KEY `fk_user_award_map_award` (`award_id`),
  KEY `fk_user_award_map_shop` (`shop_id`),
  KEY `fk_user_award_map_operator` (`operator_id`),
  CONSTRAINT `fk_user_award_map_award` FOREIGN KEY (`award_id`) REFERENCES `tb_award` (`award_id`),
  CONSTRAINT `fk_user_award_map_operator` FOREIGN KEY (`operator_id`) REFERENCES `tb_user` (`user_id`),
  CONSTRAINT `fk_user_award_map_profile` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`),
  CONSTRAINT `fk_user_award_map_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_award_map`
--

LOCK TABLES `tb_user_award_map` WRITE;
/*!40000 ALTER TABLE `tb_user_award_map` DISABLE KEYS */;
INSERT INTO `tb_user_award_map` VALUES (3,5,11,83,3,'2021-05-04 17:53:49',0,100),(4,9,11,85,9,'2021-05-05 07:02:27',1,100),(5,9,11,85,9,'2021-05-05 07:28:59',1,100);
/*!40000 ALTER TABLE `tb_user_award_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_product_map`
--

DROP TABLE IF EXISTS `tb_user_product_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_product_map` (
  `user_product_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `shop_id` int DEFAULT NULL,
  `operator_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `point` int DEFAULT '0',
  PRIMARY KEY (`user_product_id`),
  KEY `fk_user_product_map_profile` (`user_id`),
  KEY `fk_user_product_map_product` (`product_id`),
  KEY `fk_user_product_map_shop` (`shop_id`),
  KEY `fk_user_product_map_operator` (`operator_id`),
  CONSTRAINT `fk_user_product_map_operator` FOREIGN KEY (`operator_id`) REFERENCES `tb_user` (`user_id`),
  CONSTRAINT `fk_user_product_map_product` FOREIGN KEY (`product_id`) REFERENCES `tb_product` (`product_id`),
  CONSTRAINT `fk_user_product_map_profile` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`),
  CONSTRAINT `fk_user_product_map_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_product_map`
--

LOCK TABLES `tb_user_product_map` WRITE;
/*!40000 ALTER TABLE `tb_user_product_map` DISABLE KEYS */;
INSERT INTO `tb_user_product_map` VALUES (9,9,60,85,9,'2021-05-05 07:01:46',70),(10,9,60,85,9,'2021-05-05 07:02:18',70),(11,9,59,85,9,'2021-05-05 07:28:48',299);
/*!40000 ALTER TABLE `tb_user_product_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_shop_map`
--

DROP TABLE IF EXISTS `tb_user_shop_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_shop_map` (
  `user_shop_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `shop_id` int NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `point` int DEFAULT NULL,
  PRIMARY KEY (`user_shop_id`),
  UNIQUE KEY `uq_user_shop` (`user_id`,`shop_id`),
  KEY `fk_user_shop_shop` (`shop_id`),
  CONSTRAINT `fk_user_shop_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`),
  CONSTRAINT `fk_user_shop_user` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_shop_map`
--

LOCK TABLES `tb_user_shop_map` WRITE;
/*!40000 ALTER TABLE `tb_user_shop_map` DISABLE KEYS */;
INSERT INTO `tb_user_shop_map` VALUES (3,9,85,'2021-05-05 07:01:47',239);
/*!40000 ALTER TABLE `tb_user_shop_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_wechat_auth`
--

DROP TABLE IF EXISTS `tb_wechat_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_wechat_auth` (
  `wechat_auth_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `open_id` varchar(1024) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`wechat_auth_id`),
  UNIQUE KEY `open_id` (`open_id`),
  KEY `fk_wechat_profile` (`user_id`),
  CONSTRAINT `fk_wechat_profile` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_wechat_auth`
--

LOCK TABLES `tb_wechat_auth` WRITE;
/*!40000 ALTER TABLE `tb_wechat_auth` DISABLE KEYS */;
INSERT INTO `tb_wechat_auth` VALUES (2,9,'oxgxg6sV-FiEodX5ixk7epeQmkCs','2021-05-05 05:30:59');
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

-- Dump completed on 2021-05-05 16:15:02
