-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: swprodb.mysql.database.azure.com    Database: iexplore
-- ------------------------------------------------------
-- Server version	5.6.42.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(155) NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'General Discussion','general'),(2,'CTS','course'),(3,'INF','course'),(4,'CSE','course'),(5,'WF','course'),(12,'Excursion to Hogwarts!','event'),(13,'Trip to Neuschwanstein Castle','event'),(14,'Tour of one of the largest palaces.','event'),(15,'Excursion to one of Germany\'s best parks!','event'),(16,'Johann Sebastian Bach\'s workplace.','event'),(17,'Germany\'s largest art galleries.','event'),(18,'Best Excursion','event'),(22,'Party New Party alsdkas','event'),(23,'Party in Neu Ulm','event'),(24,'Johann Sebastian Bach\'s workplace.','event'),(35,'Best Excursion','event'),(37,'News and Announcements','general'),(38,'Suggestions','general'),(39,'General Help and Support','general'),(77,'[test event] -> delete','event'),(78,'[test] -> delete','event'),(79,'[test] ->delete','event'),(80,'ttesttesttest','event'),(81,'[test] -> delete','event'),(82,'[test][test]','event'),(83,'sgegqrgqerrgerrg','event'),(84,'testtesttest','event');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (1,'CTS'),(2,'INF'),(3,'CSE'),(4,'WF'),(5,'None');
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Date` date DEFAULT '2020-04-19',
  `Company` varchar(45) DEFAULT 'Hochschule Ulm',
  `Price` double DEFAULT '0',
  `TotalPlaces` int(11) DEFAULT NULL,
  `AvailablePlaces` int(11) DEFAULT NULL,
  `ShortDescription` varchar(100) DEFAULT NULL,
  `LongDescription` text,
  `EVENT_TYPE` varchar(45) NOT NULL DEFAULT 'COMPANY_EXCURSION',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=1091 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (9,'2020-03-13','Hochschule Ulm',45,35,22,'Trip to Neuschwanstein Castle','Witness the fairy tale castle of Neuschwanstein and Linderhof, created by Germany’’s 19th-century King Ludwig II, on a day trip from Ulm. With its snow-white limestone facade and fanciful turrets peeking out from the forested mountain tops of the Hohenschwangau valley, Neuschwanstein Castle could easily have been lifted from the pages of a fairy tale. In a way, it has—the German castle famously inspired Disney\'s Sleeping Beauty castle.','EXCURSION'),(10,'2020-05-10','Hochschule Ulm',65,30,19,'Tour of one of the largest palaces.',' The New Palace is an 18th-century Baroque palace and is one of the last large city palaces built in Southern Germany. It is located in the center of Stuttgart. Once a historic residence of the Kings of Württemberg, the New Palace derives its name from its commissioning by Duke Carl Eugen of Württemberg to replace the Old Castle in the early years of his reign. Join us in a tour of the palace and surrounding areas.','EXCURSION'),(11,'2020-05-10','Hochschule Ulm',45,25,19,'Excursion to one of Germany\'s best parks!','The Olympic Park Munich in Munich, Germany, is an Olympic Park which was constructed for the 1972 Summer Olympics. Located in the Oberwiesenfeld neighborhood of Munich, the Park continues to serve as a venue for cultural, social, and religious events. The plan is to go around the whole park and if there is time do some sightseeing. ','EXCURSION'),(13,'2020-05-10','Hochschule Ulm',35,20,17,'Johann Sebastian Bach\'s workplace.','St. Thomas Church is located in Leipzig, Germany. It is a well-known church, mainly because of Johann Sebastian Bach who worked here as a music director from 1723 until his death in 1750. Today, the church also holds his remains. Although rebuilt over the centuries, the church today retains the character of a late-Gothic hall church. The church has offered us a tour of the inside facility and a brief presentation on the history of the church.','EXCURSION'),(14,'2020-05-10','Hochschule Ulm',40,30,27,'Germany\'s largest art galleries.','The Hamburger Kunsthalle was founded in 1850, consists of three connected buildings and is one of the largest museums in the country. The art gallery houses one of the few art collections in Germany that covers seven centuries of European art, from the Middle Ages to the present day.We will be taking a tour of the entire museum and we have the pleasure of seeing of all the fascinating paintings and art','EXCURSION'),(1007,'2020-05-11','Hochschule Ulm',70,30,16,'Best Excursion',' The New Palace is an 18th-century Baroque palace and is one of the last large city palaces built in Southern Germany. It is located in the center of Stuttgart. Once a historic residence of the Kings of Württemberg, the New Palace derives its name from its commissioning by Duke Carl Eugen of Württemberg to replace the Old Castle in the early years of his reign. Join us in a tour of the palace and surrounding areas.','EXCURSION');
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` int(11) NOT NULL,
  `Rating` double NOT NULL,
  `Message` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `UserIDFK` (`UserID`),
  CONSTRAINT `UserIDFK` FOREIGN KEY (`UserID`) REFERENCES `users` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
INSERT INTO `feedback` VALUES (60,118,5,'Nice app'),(61,10,5,'Great app, keep it up'),(62,803,5,'Nice app CTS'),(63,106,5,'Make 0 starts by default nigga'),(64,113,4,'Why are the admins not able to give feedback? Discrimination...');
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `CustomerName` varchar(100) DEFAULT NULL,
  `Date` date DEFAULT NULL,
  `Ammount` double DEFAULT NULL,
  `EventName` varchar(100) DEFAULT NULL,
  `Company` varchar(100) DEFAULT NULL,
  `TransactionID` int(11) DEFAULT '1',
  PRIMARY KEY (`Id`),
  KEY `transactionfk_idx` (`TransactionID`),
  CONSTRAINT `transactionfk` FOREIGN KEY (`TransactionID`) REFERENCES `transactions` (`Id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES (4,'Matiz','2019-12-11',0,'Excursion to BMW','BMW',NULL),(5,'Matiz','2019-12-11',0,'Excursion to Daimler TSS','Daimler',NULL),(6,'Giles','2019-12-16',35,'Johann Sebastian Bach\'s workplace.','Hochschule Ulm',NULL),(7,'Giles','2019-12-16',70,'Best Excursion','Hochschule Ulm',229),(8,'Marmiss','2019-12-19',65,'Trip to Neuschwanstein Castle','Hochschule Ulm',233),(9,'Bredesen','2019-12-19',65,'Trip to Neuschwanstein Castle','Hochschule Ulm',235),(10,'Pitt','2019-12-22',45,'Trip to Neuschwanstein Castle','Hochschule Ulm',NULL),(11,'Pitt','2019-12-22',45,'Trip to Neuschwanstein Castle','Hochschule Ulm',NULL),(12,'Pitt','2019-12-22',45,'Trip to Neuschwanstein Castle','Hochschule Ulm',NULL),(13,'Pitt','2019-12-23',45,'Excursion to one of Germany\'s best parks!','Hochschule Ulm',NULL),(14,'Pitt','2019-12-23',70,'Best Excursion','Hochschule Ulm',NULL),(15,'Pitt','2019-12-23',40,'Germany\'s largest art galleries.','Hochschule Ulm',NULL),(16,'Bredesen','2019-12-19',40,'Germany\'s largest art galleries.','Hochschule Ulm',234),(17,'Pitt','2019-12-23',35,'Johann Sebastian Bach\'s workplace.','Hochschule Ulm',NULL),(18,'Pitt','2019-12-23',45,'Trip to Neuschwanstein Castle','Hochschule Ulm',NULL),(19,'Pitt','2019-12-23',65,'Tour of one of the largest palaces.','Hochschule Ulm',NULL),(20,'Rzayev','2019-12-23',70,'Best Excursion','Hochschule Ulm',248),(21,'Rzayev','2019-12-23',40,'Germany\'s largest art galleries.','Hochschule Ulm',249),(22,'Rzayev','2019-12-23',45,'Excursion to one of Germany\'s best parks!','Hochschule Ulm',250),(23,'Rzayev','2019-12-23',45,'Excursion to one of Germany\'s best parks!','Hochschule Ulm',251),(24,'Baker','2019-12-24',45,'Trip to Neuschwanstein Castle','Hochschule Ulm',253),(25,'Baker','2019-12-24',65,'Tour of one of the largest palaces.','Hochschule Ulm',254),(26,'Baker','2019-12-24',70,'Best Excursion','Hochschule Ulm',255),(27,'Baker','2019-12-24',45,'Excursion to one of Germany\'s best parks!','Hochschule Ulm',258),(28,'Baker','2019-12-24',40,'Germany\'s largest art galleries.','Hochschule Ulm',257),(29,'Baker','2019-12-24',40,'Germany\'s largest art galleries.','Hochschule Ulm',259),(30,'Baker','2019-12-24',35,'Johann Sebastian Bach\'s workplace.','Hochschule Ulm',260),(31,'Rzayev','2019-12-23',35,'Johann Sebastian Bach\'s workplace.','Hochschule Ulm',252),(32,'Baker','2019-12-24',35,'Johann Sebastian Bach\'s workplace.','Hochschule Ulm',261),(33,'Baker','2019-12-24',40,'Germany\'s largest art galleries.','Hochschule Ulm',262),(34,'Folta','2019-12-24',65,'Tour of one of the largest palaces.','Hochschule Ulm',263),(35,'Folta','2019-12-24',45,'Trip to Neuschwanstein Castle','Hochschule Ulm',264),(36,'Rzayev','2019-12-23',65,'Tour of one of the largest palaces.','Hochschule Ulm',247),(37,'Rzayev','2019-12-24',40,'Germany\'s largest art galleries.','Hochschule Ulm',266),(38,'Baker','2019-12-24',40,'Germany\'s largest art galleries.','Hochschule Ulm',256),(39,'Pitt','2019-12-26',45,'Trip to Neuschwanstein Castle','Hochschule Ulm',NULL),(40,'Pitt','2019-12-26',65,'Tour of one of the largest palaces.','Hochschule Ulm',NULL),(41,'Pitt','2019-12-26',45,'Excursion to one of Germany\'s best parks!','Hochschule Ulm',NULL),(42,'Pitt','2019-12-26',35,'Johann Sebastian Bach\'s workplace.','Hochschule Ulm',NULL),(43,'Pitt','2019-12-26',35,'Johann Sebastian Bach\'s workplace.','Hochschule Ulm',NULL),(44,'Pitt','2019-12-26',40,'Germany\'s largest art galleries.','Hochschule Ulm',NULL),(45,'Pitt','2019-12-26',70,'Best Excursion','Hochschule Ulm',NULL),(46,'Pitt','2019-12-26',70,'Best Excursion','Hochschule Ulm',NULL),(47,'Pitt','2019-12-26',70,'Best Excursion','Hochschule Ulm',NULL),(48,'Pitt','2019-12-26',65,'Tour of one of the largest palaces.','Hochschule Ulm',357),(49,'Pitt','2019-12-26',45,'Excursion to one of Germany\'s best parks!','Hochschule Ulm',358),(50,'Pitt','2019-12-26',35,'Johann Sebastian Bach\'s workplace.','Hochschule Ulm',359);
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `location` (
  `EventID` int(11) NOT NULL,
  `Latitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `City` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`EventID`),
  CONSTRAINT `EventID3` FOREIGN KEY (`EventID`) REFERENCES `event` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
INSERT INTO `location` VALUES (9,47.557915,10.749801,'Hohenschwangau'),(10,48.778167,9.18186,'Stuttgartt'),(11,48.175679,11.551743,'Munich'),(13,51.339178,12.372199,'Munich'),(14,53.555683,10.002567,'Hamburg'),(1007,48.778167,9.181861,'Stuttgart');
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pictures`
--

DROP TABLE IF EXISTS `pictures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pictures` (
  `EventID` int(11) NOT NULL,
  `Logo` varchar(45) NOT NULL,
  `Picture` varchar(45) NOT NULL,
  PRIMARY KEY (`EventID`),
  CONSTRAINT `EventID_fk` FOREIGN KEY (`EventID`) REFERENCES `event` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pictures`
--

LOCK TABLES `pictures` WRITE;
/*!40000 ALTER TABLE `pictures` DISABLE KEYS */;
INSERT INTO `pictures` VALUES (9,'https://i.imgur.com/LVkjPcV.png','https://i.imgur.com/hHoxKuf.png'),(10,'https://i.imgur.com/IPIBdgC.jpg','https://i.imgur.com/Eyak4nt.jpg'),(11,'https://i.imgur.com/zHNHIRE.png','https://i.imgur.com/DsGo0bZ.jpg'),(13,'https://i.imgur.com/y02zvQU.png','https://i.imgur.com/JpHITu6.jpg'),(14,'https://i.imgur.com/vl1jzL6.png','https://i.imgur.com/eiG1qvU.jpg'),(1007,'https://i.imgur.com/rcphR1A.png','https://i.imgur.com/VU5rSxD.png');
/*!40000 ALTER TABLE `pictures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `threadID` int(11) NOT NULL,
  `postAuthor` int(11) DEFAULT NULL,
  `postContent` blob NOT NULL,
  `postTime` varchar(20) CHARACTER SET latin1 NOT NULL,
  `postLastEdited` varchar(20) CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `postAuthor_idx` (`postAuthor`),
  KEY `threadId_idx` (`threadID`),
  CONSTRAINT `postAuthor` FOREIGN KEY (`postAuthor`) REFERENCES `users` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `threadId` FOREIGN KEY (`threadID`) REFERENCES `thread` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,1,777,_binary 'sjdf;lkdshflsdhfsldhfsdl;fs;ldj','1576744173648','1576744173648'),(4,1,118,_binary '😎😎😎😎😎','1576875045709','1576875045709');
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `thread`
--

DROP TABLE IF EXISTS `thread`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `thread` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `categoryID` int(11) NOT NULL,
  `threadTitle` blob NOT NULL,
  `threadAuthor` int(11) DEFAULT NULL,
  `threadFirstPost` int(11) DEFAULT NULL,
  `threadLastPost` int(11) DEFAULT NULL,
  `threadLocked` int(11) NOT NULL DEFAULT '0',
  `threadType` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`),
  KEY `author_idx` (`threadAuthor`),
  KEY `categoryId_idx` (`categoryID`),
  CONSTRAINT `author` FOREIGN KEY (`threadAuthor`) REFERENCES `users` (`Id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `categoryId` FOREIGN KEY (`categoryID`) REFERENCES `category` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thread`
--

LOCK TABLES `thread` WRITE;
/*!40000 ALTER TABLE `thread` DISABLE KEYS */;
INSERT INTO `thread` VALUES (1,1,_binary 'Welcome to ExploreHub',777,1,4,0,0);
/*!40000 ALTER TABLE `thread` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `StudentID` int(11) NOT NULL,
  `Date` date NOT NULL,
  `Completed` int(11) DEFAULT NULL,
  `EventID` int(11) NOT NULL,
  `PaymentMethod` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`),
  KEY `StudentID_idx` (`StudentID`),
  KEY `EventID_idx` (`EventID`),
  CONSTRAINT `EventID4` FOREIGN KEY (`EventID`) REFERENCES `event` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `StudentID2` FOREIGN KEY (`StudentID`) REFERENCES `users` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=361 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (229,8,'2019-12-16',3,1007,1),(233,110,'2019-12-19',3,9,1),(234,6,'2019-12-19',3,14,1),(235,6,'2019-12-19',3,9,0),(247,113,'2019-12-23',3,10,1),(248,113,'2019-12-23',3,1007,0),(249,113,'2019-12-23',3,14,0),(250,113,'2019-12-23',3,11,0),(251,113,'2019-12-23',3,11,0),(252,113,'2019-12-23',3,13,1),(253,10,'2019-12-24',1,9,0),(254,10,'2019-12-24',1,10,1),(255,10,'2019-12-24',1,1007,1),(256,10,'2019-12-24',1,14,1),(257,10,'2019-12-24',3,14,1),(258,10,'2019-12-24',1,11,1),(259,10,'2019-12-24',3,14,1),(260,10,'2019-12-24',3,13,1),(261,10,'2019-12-24',3,13,1),(262,10,'2019-12-24',3,14,1),(263,9,'2019-12-24',1,10,0),(264,9,'2019-12-24',1,9,0),(265,113,'2019-12-24',2,9,1),(266,113,'2019-12-24',1,14,1),(267,6,'2019-12-25',0,9,2),(268,6,'2019-12-25',0,11,1),(269,6,'2019-12-25',0,11,1),(270,113,'2019-12-25',2,9,1),(271,113,'2019-12-25',2,11,1),(272,113,'2019-12-25',2,9,1),(273,113,'2019-12-25',2,10,1),(274,113,'2019-12-25',2,10,1),(275,113,'2019-12-25',2,9,1),(276,113,'2019-12-25',2,9,1),(277,113,'2019-12-25',2,10,1),(278,113,'2019-12-25',2,11,1),(279,113,'2019-12-25',2,13,1),(280,113,'2019-12-25',2,9,1),(281,113,'2019-12-25',2,10,1),(282,113,'2019-12-25',2,9,1),(283,113,'2019-12-25',2,10,1),(284,113,'2019-12-25',2,9,1),(285,113,'2019-12-25',2,10,1),(286,113,'2019-12-25',2,9,1),(288,113,'2019-12-25',2,11,1),(289,113,'2019-12-25',2,13,1),(290,113,'2019-12-25',2,13,1),(291,113,'2019-12-25',2,10,1),(292,113,'2019-12-25',2,11,1),(293,113,'2019-12-25',2,13,1),(303,113,'2019-12-26',2,11,1),(304,113,'2019-12-26',2,9,1),(305,113,'2019-12-26',2,9,1),(306,113,'2019-12-26',2,10,1),(307,113,'2019-12-26',2,11,1),(308,113,'2019-12-26',2,9,1),(309,113,'2019-12-26',2,11,1),(310,113,'2019-12-26',2,13,1),(311,113,'2019-12-26',2,1007,1),(312,113,'2019-12-26',2,10,1),(313,113,'2019-12-26',2,9,1),(314,113,'2019-12-26',2,10,1),(315,113,'2019-12-26',2,1007,1),(316,113,'2019-12-26',2,10,1),(317,113,'2019-12-26',2,9,1),(318,113,'2019-12-26',2,1007,1),(319,113,'2019-12-26',2,9,1),(320,113,'2019-12-26',2,10,1),(321,113,'2019-12-26',2,1007,1),(322,113,'2019-12-26',2,13,1),(323,113,'2019-12-26',2,1007,1),(324,113,'2019-12-26',2,10,1),(325,113,'2019-12-26',2,9,1),(326,113,'2019-12-26',2,10,1),(327,113,'2019-12-26',2,1007,1),(328,113,'2019-12-26',2,9,1),(329,113,'2019-12-26',2,10,1),(330,113,'2019-12-26',2,1007,1),(331,113,'2019-12-26',2,9,1),(332,113,'2019-12-26',2,10,1),(333,113,'2019-12-26',2,1007,1),(334,113,'2019-12-26',2,9,1),(335,113,'2019-12-26',2,10,1),(336,113,'2019-12-26',2,1007,1),(337,113,'2019-12-26',2,9,1),(338,113,'2019-12-26',2,10,1),(339,113,'2019-12-26',2,1007,1),(340,113,'2019-12-26',2,10,1),(341,113,'2019-12-26',2,9,1),(342,113,'2019-12-26',2,10,1),(343,113,'2019-12-26',2,1007,1),(344,113,'2019-12-26',2,11,1),(345,113,'2019-12-26',2,13,1),(346,113,'2019-12-26',2,9,1),(347,113,'2019-12-26',2,10,1),(348,113,'2019-12-26',2,1007,1),(349,113,'2019-12-26',2,11,1),(350,113,'2019-12-26',2,13,1),(351,113,'2019-12-26',2,9,1),(352,113,'2019-12-26',2,10,1),(353,113,'2019-12-26',2,1007,1),(354,113,'2019-12-26',2,11,1),(355,113,'2019-12-26',2,13,1),(356,118,'2019-12-26',0,9,1),(357,118,'2019-12-26',1,10,0),(358,118,'2019-12-26',1,11,0),(359,118,'2019-12-26',1,13,0),(360,118,'2019-12-26',0,14,1);
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Email` varchar(45) NOT NULL,
  `FirstName` varchar(45) NOT NULL,
  `LastName` varchar(45) NOT NULL,
  `AccessLevel` int(1) unsigned zerofill NOT NULL DEFAULT '0',
  `CourseID` int(11) DEFAULT '5',
  `Password` varchar(45) NOT NULL,
  `Picture` varchar(35) NOT NULL DEFAULT '/IMG/icon-account.png',
  `Active` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`),
  KEY `Id_idx` (`CourseID`),
  CONSTRAINT `Id` FOREIGN KEY (`CourseID`) REFERENCES `courses` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=806 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (6,'Bredesen@hs-ulm.de','Denise','Bredesen',0,1,'user6','https://i.imgur.com/EK2R1rn.jpg',1),(7,'Qualls@hs-ulm.de','Mark','Qualls',0,1,'user7','https://i.imgur.com/EK2R1rn.jpg',0),(8,'Giles@hs-ulm.de','Marie','Giles',0,3,'user8','https://i.imgur.com/EK2R1rn.jpg',0),(9,'Folta@hs-ulm.de','Jennifer','Folta',0,2,'user9','https://i.imgur.com/EK2R1rn.jpg',0),(10,'Baker@hs-ulm.de','Sharon','Baker',0,4,'user10','https://i.imgur.com/EK2R1rn.jpg',0),(11,'Chavez@hs-ulm.de','Peggie','Chavez',0,3,'user11','https://i.imgur.com/EK2R1rn.jpg',0),(12,'Craner@hs-ulm.de','Juana','Craner',0,1,'user12','https://i.imgur.com/EK2R1rn.jpg',0),(13,'Merrifield@hs-ulm.de','Jason','Merrifield',0,2,'user13','https://i.imgur.com/EK2R1rn.jpg',0),(17,'Pengelly@hs-ulm.de','Christopher','Pengelly',0,3,'user17','https://i.imgur.com/EK2R1rn.jpg',0),(18,'Bentley@hs-ulm.de','Jane','Bentley',0,2,'user18','https://i.imgur.com/EK2R1rn.jpg',0),(19,'Hopkins@hs-ulm.de','Charles','Hopkins',0,4,'user19','https://i.imgur.com/EK2R1rn.jpg',0),(20,'Wimmer@hs-ulm.de','Robert','Wimmer',0,4,'user20','https://i.imgur.com/EK2R1rn.jpg',0),(21,'Powers@hs-ulm.de','Danny','Powers',0,2,'user21','https://i.imgur.com/EK2R1rn.jpg',0),(22,'Stein@hs-ulm.de','Robert','Stein',0,4,'user22','https://i.imgur.com/EK2R1rn.jpg',0),(23,'Borghese@hs-ulm.de','Leonel','Borghese',0,4,'user23','https://i.imgur.com/EK2R1rn.jpg',0),(24,'Poremski@hs-ulm.de','Audrey','Poremski',0,3,'user24','https://i.imgur.com/EK2R1rn.jpg',0),(25,'Peeples@hs-ulm.de','Dale','Peeples',0,4,'user25','https://i.imgur.com/EK2R1rn.jpg',0),(27,'Gipe@hs-ulm.de','Raymond','Gipe',0,1,'user27','https://i.imgur.com/EK2R1rn.jpg',0),(28,'Wilborn@hs-ulm.de','Josephine','Wilborn',0,3,'user28','https://i.imgur.com/EK2R1rn.jpg',0),(29,'Burkett@hs-ulm.de','Fred','Burkett',0,1,'user29','https://i.imgur.com/EK2R1rn.jpg',0),(30,'Kriner@hs-ulm.de','Clay','Kriner',0,1,'user30','https://i.imgur.com/EK2R1rn.jpg',0),(31,'Harding@hs-ulm.de','Lloyd','Harding',0,3,'user31','https://i.imgur.com/EK2R1rn.jpg',0),(32,'Palmer@hs-ulm.de','Shirley','Palmer',0,3,'user32','https://i.imgur.com/EK2R1rn.jpg',0),(33,'Crumrine@hs-ulm.de','Bobby','Crumrine',0,3,'user33','https://i.imgur.com/EK2R1rn.jpg',0),(34,'Hernandez@hs-ulm.de','Nicholas','Hernandez',0,1,'user34','https://i.imgur.com/EK2R1rn.jpg',0),(35,'Frazier@hs-ulm.de','James','Frazier',0,4,'user35','https://i.imgur.com/EK2R1rn.jpg',0),(36,'Roberts@hs-ulm.de','Linda','Roberts',0,4,'user36','https://i.imgur.com/EK2R1rn.jpg',0),(37,'King@hs-ulm.de','Jefferson','King',0,2,'user37','https://i.imgur.com/EK2R1rn.jpg',0),(38,'Lindsay@hs-ulm.de','Gerald','Lindsay',0,3,'user38','https://i.imgur.com/EK2R1rn.jpg',0),(39,'Anaya@hs-ulm.de','Robert','Anaya',0,2,'user39','https://i.imgur.com/EK2R1rn.jpg',0),(40,'Lewis@hs-ulm.de','Sylvia','Lewis',0,4,'user40','https://i.imgur.com/EK2R1rn.jpg',0),(41,'Tuck@hs-ulm.de','Shirley','Tuck',0,1,'user41','https://i.imgur.com/EK2R1rn.jpg',0),(42,'Takahashi@hs-ulm.de','Leigh','Takahashi',0,1,'user42','https://i.imgur.com/EK2R1rn.jpg',0),(43,'Sutter@hs-ulm.de','Thomas','Sutter',0,3,'user43','https://i.imgur.com/EK2R1rn.jpg',0),(44,'Amsterdam@hs-ulm.de','Matilda','Amsterdam',0,2,'user44','https://i.imgur.com/EK2R1rn.jpg',0),(45,'Cecil@hs-ulm.de','Charlene','Cecil',0,4,'user45','https://i.imgur.com/EK2R1rn.jpg',0),(46,'Nappi@hs-ulm.de','Billy','Nappi',0,2,'user46','https://i.imgur.com/EK2R1rn.jpg',0),(47,'Freeman@hs-ulm.de','Maria','Freeman',0,2,'user47','https://i.imgur.com/EK2R1rn.jpg',0),(48,'Bacich@hs-ulm.de','Bobby','Bacich',0,4,'user48','https://i.imgur.com/EK2R1rn.jpg',0),(49,'Rappold@hs-ulm.de','Carol','Rappold',0,3,'user49','https://i.imgur.com/EK2R1rn.jpg',0),(50,'williamsomething@hs-ulm.de','William','Cantwell',0,1,'user50','https://i.imgur.com/EK2R1rn.jpg',0),(51,'Johnston@hs-ulm.de','Jerry','Johnston',0,2,'user51','https://i.imgur.com/EK2R1rn.jpg',0),(52,'Wenrich@hs-ulm.de','Harold','Wenrich',0,4,'user52','https://i.imgur.com/EK2R1rn.jpg',0),(53,'Datson@hs-ulm.de','Richard','Datson',0,1,'user53','https://i.imgur.com/EK2R1rn.jpg',0),(54,'Smith@hs-ulm.de','Laura','Smith',0,4,'user54','https://i.imgur.com/EK2R1rn.jpg',0),(55,'Guillen@hs-ulm.de','Sue','Guillen',0,3,'user55','https://i.imgur.com/EK2R1rn.jpg',0),(56,'Turner@hs-ulm.de','Ian','Turner',0,3,'user56','https://i.imgur.com/EK2R1rn.jpg',0),(57,'Reed@hs-ulm.de','Karen','Reed',0,1,'user57','https://i.imgur.com/EK2R1rn.jpg',0),(58,'Rowell@hs-ulm.de','Elvis','Rowell',0,3,'user58','https://i.imgur.com/EK2R1rn.jpg',0),(59,'Emerson@hs-ulm.de','Leon','Emerson',0,3,'user59','https://i.imgur.com/EK2R1rn.jpg',0),(60,'Harju@hs-ulm.de','Debbie','Harju',0,3,'user60','https://i.imgur.com/EK2R1rn.jpg',0),(61,'Robbins@hs-ulm.de','Stanley','Robbins',0,2,'user61','https://i.imgur.com/EK2R1rn.jpg',0),(62,'Swiger@hs-ulm.de','Ann','Swiger',0,1,'user62','https://i.imgur.com/EK2R1rn.jpg',0),(63,'Armstrong@hs-ulm.de','Raymond','Armstrong',0,4,'user63','https://i.imgur.com/EK2R1rn.jpg',0),(64,'Reid@hs-ulm.de','Shirley','Reid',0,3,'user64','https://i.imgur.com/EK2R1rn.jpg',0),(65,'Bonham@hs-ulm.de','Reginald','Bonham',0,3,'user65','https://i.imgur.com/EK2R1rn.jpg',0),(66,'Stumbaugh@hs-ulm.de','Kathy','Stumbaugh',0,3,'user66','https://i.imgur.com/EK2R1rn.jpg',0),(67,'Alston@hs-ulm.de','Brandon','Alston',0,3,'user67','https://i.imgur.com/EK2R1rn.jpg',0),(69,'Simpson@hs-ulm.de','Kendra','Simpson',0,2,'user69','https://i.imgur.com/EK2R1rn.jpg',0),(70,'Newsome@hs-ulm.de','Lillian','Newsome',0,3,'user70','https://i.imgur.com/EK2R1rn.jpg',0),(71,'Rayborn@hs-ulm.de','Catherine','Rayborn',0,2,'user71','https://i.imgur.com/EK2R1rn.jpg',0),(72,'Ryan@hs-ulm.de','Andres','Ryan',0,4,'user72','https://i.imgur.com/EK2R1rn.jpg',0),(73,'Gonzalez@hs-ulm.de','Jose','Gonzalez',0,1,'user73','https://i.imgur.com/EK2R1rn.jpg',0),(74,'Griffin@hs-ulm.de','Reuben','Griffin',0,4,'user74','https://i.imgur.com/EK2R1rn.jpg',0),(75,'Pederson@hs-ulm.de','Alice','Pederson',0,4,'user75','https://i.imgur.com/EK2R1rn.jpg',0),(76,'Chi@hs-ulm.de','Johnny','Chi',0,1,'user76','https://i.imgur.com/EK2R1rn.jpg',0),(77,'Maynard@hs-ulm.de','Ashley','Maynard',0,2,'user77','https://i.imgur.com/EK2R1rn.jpg',0),(78,'George@hs-ulm.de','Jeffrey','George',0,1,'user78','https://i.imgur.com/EK2R1rn.jpg',0),(79,'Goldfeder@hs-ulm.de','Rose','Goldfeder',0,2,'user79','https://i.imgur.com/EK2R1rn.jpg',0),(80,'Rader@hs-ulm.de','Shelly','Rader',0,1,'user80','https://i.imgur.com/EK2R1rn.jpg',0),(81,'Iverson@hs-ulm.de','Francine','Iverson',0,2,'user81','https://i.imgur.com/EK2R1rn.jpg',0),(82,'Brown@hs-ulm.de','Joseph','Brown',0,2,'user82','https://i.imgur.com/EK2R1rn.jpg',0),(83,'Stechlinski@hs-ulm.de','Sarah','Stechlinski',0,2,'user83','https://i.imgur.com/EK2R1rn.jpg',0),(84,'Walker@hs-ulm.de','Mildred','Walker',0,4,'user84','https://i.imgur.com/EK2R1rn.jpg',0),(85,'Gleaves@hs-ulm.de','Jonathan','Gleaves',0,3,'user85','https://i.imgur.com/EK2R1rn.jpg',0),(86,'Monath@hs-ulm.de','William','Monath',0,4,'user86','https://i.imgur.com/EK2R1rn.jpg',0),(87,'Braswell@hs-ulm.de','Heidi','Braswell',0,2,'user87','https://i.imgur.com/EK2R1rn.jpg',0),(88,'Dunn@hs-ulm.de','Ramon','Dunn',0,1,'user88','https://i.imgur.com/EK2R1rn.jpg',0),(89,'Brannon@hs-ulm.de','Jackie','Brannon',0,3,'user89','https://i.imgur.com/EK2R1rn.jpg',0),(90,'Martin@hs-ulm.de','Paris','Martin',0,4,'user90','https://i.imgur.com/EK2R1rn.jpg',0),(91,'Wolford@hs-ulm.de','Catherine','Wolford',0,2,'user91','https://i.imgur.com/EK2R1rn.jpg',0),(92,'Howard@hs-ulm.de','Victor','Howard',0,1,'user92','https://i.imgur.com/EK2R1rn.jpg',0),(93,'Haralson@hs-ulm.de','Harry','Haralson',0,3,'user93','https://i.imgur.com/EK2R1rn.jpg',0),(94,'Cook@hs-ulm.de','Tammy','Cook',0,3,'user94','https://i.imgur.com/EK2R1rn.jpg',0),(95,'Silver@hs-ulm.de','Santos','Silver',0,2,'user95','https://i.imgur.com/EK2R1rn.jpg',0),(96,'Staples@hs-ulm.de','Terri','Staples',0,3,'user96','https://i.imgur.com/EK2R1rn.jpg',0),(97,'Mooney@hs-ulm.de','Mary','Mooney',0,3,'user97','https://i.imgur.com/EK2R1rn.jpg',0),(98,'Anthony@hs-ulm.de','Thomas','Anthony',0,1,'user98','https://i.imgur.com/EK2R1rn.jpg',0),(99,'Jones@hs-ulm.de','Arthur','Jones',0,2,'user99','https://i.imgur.com/EK2R1rn.jpg',0),(100,'Wasserman@hs-ulm.de','Lincoln','Wasserman',0,1,'user100','https://i.imgur.com/EK2R1rn.jpg',0),(101,'Clem@hs-ulm.de','Peggy','Clem',1,5,'hahahalol','https://i.imgur.com/EK2R1rn.jpg',0),(102,'Thomas@hs-ulm.de','Anna','Thomas',1,5,'admin2','https://i.imgur.com/EK2R1rn.jpg',0),(106,'thenotoriuousmma@hs-ulm.de','Connor','Mcgregor',0,1,'hahaha','https://i.imgur.com/EK2R1rn.jpg',0),(107,'miketyson@hs-ulm.de','Mike','Tyson',0,1,'miketyson','https://i.imgur.com/EK2R1rn.jpg',0),(109,'kebabnurmagomedov@hs-ulm.de','Kebab','Nurmagomedov',0,4,'kebab123','https://i.imgur.com/EK2R1rn.jpg',0),(110,'marmiss@mail.hs-ulm.de','Aleksejs','Marmiss',0,1,'iddQd','https://i.imgur.com/EK2R1rn.jpg',0),(111,'gicumironica@mail.hs-ulm.de','Giku','Mironica',0,1,'hahaha','https://i.imgur.com/EK2R1rn.jpg',0),(112,'basit@hs-ulm.de','Abdul','Basit',0,1,'basit123','https://i.imgur.com/EK2R1rn.jpg',0),(113,'ryazev@mail.hs-ulm.de','Hidayat','Rzayev',0,1,'123','https://i.imgur.com/QSTuOYB.png',0),(114,'barackobama@mail.hs-ulm.de','Barack','Obama',0,1,'lmaoloool','https://i.imgur.com/EK2R1rn.jpg',0),(116,'braz@hs-ulm.de','Braz','Castana',0,1,'salt','https://i.imgur.com/EK2R1rn.jpg',0),(117,'waffo@mail.hs-ulm.de','Nelson','Waffo',0,1,'manchild','https://files.catbox.moe/5nhpx3.jfi',0),(118,'..','Brad','Pitt',0,1,'..','https://i.imgur.com/rwdvmzD.png',0),(123,'trolley@mail.hs-ulm.de','Trolley','Trolley',0,1,'agadsg','https://i.imgur.com/EK2R1rn.jpg',0),(777,'hochschule@hs-ulm.de','Hochschule','Ulm',2,5,'1234','https://i.imgur.com/EK2R1rn.jpg',0),(778,'donaldpump@hs-ulm.de','Donald','Pump',1,5,' 123','https://i.imgur.com/HimoprE.png',0),(786,'bean@mail.hs-ulm.de','Mister','Bean',1,5,'123','https://i.imgur.com/bGG1DlJ.png',0),(788,'baer@hs-ulm.de','Klaus','Baer',1,5,'00f0cde5-5c2d-422e-b4f6-bf57dbef892b','/IMG/icon-account.png',0),(798,'wgvs@mail.hs-ulm.de','wefwf','fwf',0,3,'fsdfs','/IMG/icon-account.png',0),(799,'fadf@mail.hs-ulm.de','fdfs','fsdfs',0,1,'afsdf','/IMG/icon-account.png',0),(800,'fsgEg@mail.hs-ulm.de','fsdfs','qgrhg',0,1,'sdsdfsd','/IMG/icon-account.png',0),(801,'sdfsf@mail.hs-ulm.de','sqrgqwgq','rgqrrgqe',0,1,'efsdfs','/IMG/icon-account.png',0),(802,'adfgag@mail.hs-ulm.de','rhqger','fqdgqwrh',0,1,'fdsgwegwg','https://i.imgur.com/pyH4ZK4.png',0),(803,'bakri@mail.hs-ulm.de','Diaae','Bakri',0,1,'123456','/IMG/icon-account.png',0),(804,'fdsfsdfs@mail.hs-ulm.de','fgdfsgdf','sdgsdfg',0,1,'fdsfdfsdfsf','/IMG/icon-account.png',0),(805,'root','Super','User',2,5,'.','/IMG/icon-account.png',0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wishlist`
--

DROP TABLE IF EXISTS `wishlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wishlist` (
  `StudentID` int(11) NOT NULL,
  `EventID` int(11) NOT NULL,
  PRIMARY KEY (`EventID`,`StudentID`),
  KEY `StudentID_idx` (`StudentID`),
  CONSTRAINT `EventID` FOREIGN KEY (`EventID`) REFERENCES `event` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `StudentID` FOREIGN KEY (`StudentID`) REFERENCES `users` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wishlist`
--

LOCK TABLES `wishlist` WRITE;
/*!40000 ALTER TABLE `wishlist` DISABLE KEYS */;
INSERT INTO `wishlist` VALUES (6,10),(8,9),(10,13),(10,14);
/*!40000 ALTER TABLE `wishlist` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-12-26 16:35:13
