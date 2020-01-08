-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Хост: swprodb.mysql.database.azure.com
-- Время создания: Янв 08 2020 г., 20:42
-- Версия сервера: 5.7.27-log
-- Версия PHP: 7.3.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `iexplore`
--
CREATE DATABASE IF NOT EXISTS `iexplore` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `iexplore`;

DELIMITER $$
--
-- Процедуры
--
CREATE DEFINER=`swprodb`@`%` PROCEDURE `backup_db` ()  BEGIN

DROP DATABASE IF EXISTS iexplore_backup;
CREATE DATABASE iexplore_backup;

CALL copyCourses();
CALL copyUsers();
CALL copyFeedback();
CALL copyEvents();
CALL copyPicturesAndLocation();
CALL copyWishlist();
CALL copyTransactions();
CALL copyInvoices();
CALL copyCategory();
CALL copyThreads();
CALL copyPosts();

INSERT INTO iexplore_backup.courses
SELECT *
FROM iexplore.courses;

INSERT INTO iexplore_backup.users
SELECT *
FROM iexplore.users;

INSERT INTO iexplore_backup.feedback
SELECT *
FROM iexplore.feedback;

INSERT INTO iexplore_backup.event
SELECT *
FROM iexplore.event;

INSERT INTO iexplore_backup.location
SELECT *
FROM iexplore.location;

INSERT INTO iexplore_backup.pictures
SELECT *
FROM iexplore.pictures;

INSERT INTO iexplore_backup.wishlist
SELECT *
FROM iexplore.wishlist;

INSERT INTO iexplore_backup.transactions
SELECT *
FROM iexplore.transactions;

INSERT INTO iexplore_backup.invoice
SELECT *
FROM iexplore.invoice;

INSERT INTO iexplore_backup.category
SELECT *
FROM iexplore.category;

INSERT INTO iexplore_backup.thread
SELECT *
FROM iexplore.thread;

INSERT INTO iexplore_backup.post
SELECT *
FROM iexplore.post;

END$$

CREATE DEFINER=`swprodb`@`%` PROCEDURE `copyCategory` ()  BEGIN

DROP TABLE IF EXISTS `iexplore_backup`.`category`;
CREATE TABLE `iexplore_backup`.`category` (
  `Id` int(11) NOT NULL,
  `Name` varchar(155) NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Id`,`Name`)
);

END$$

CREATE DEFINER=`swprodb`@`%` PROCEDURE `copyCourses` ()  BEGIN
DROP TABLE IF EXISTS `iexplore_backup`.`courses`;
CREATE TABLE `iexplore_backup`.`courses` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  PRIMARY KEY (`Id`)
);
END$$

CREATE DEFINER=`swprodb`@`%` PROCEDURE `copyEvents` ()  BEGIN

DROP TABLE IF EXISTS `iexplore_backup`.`event`;
CREATE TABLE `iexplore_backup`.`event` (
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
);

END$$

CREATE DEFINER=`swprodb`@`%` PROCEDURE `copyFeedback` ()  BEGIN

DROP TABLE IF EXISTS `iexplore_backup`.`feedback`;
CREATE TABLE `iexplore_backup`.`feedback` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` int(11) NOT NULL,
  `Rating` int(1) DEFAULT NULL,
  `Message` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `UserIDFK` (`UserID`),
  CONSTRAINT `UserIDFK12` FOREIGN KEY (`UserID`) REFERENCES `users` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
);

END$$

CREATE DEFINER=`swprodb`@`%` PROCEDURE `copyInvoices` ()  BEGIN

DROP TABLE IF EXISTS `iexplore_backup`.`invoice`;
CREATE TABLE `iexplore_backup`.`invoice` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `CustomerName` varchar(100) DEFAULT NULL,
  `Date` date DEFAULT NULL,
  `Ammount` double DEFAULT NULL,
  `EventName` varchar(100) DEFAULT NULL,
  `Company` varchar(100) DEFAULT NULL,
  `TransactionID` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`Id`),
  KEY `transactionfk_idx` (`TransactionID`),
  CONSTRAINT `transactionfk` FOREIGN KEY (`TransactionID`) REFERENCES `transactions` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

END$$

CREATE DEFINER=`swprodb`@`%` PROCEDURE `copyPicturesAndLocation` ()  BEGIN

DROP TABLE IF EXISTS `iexplore_backup`.`pictures`;
CREATE TABLE `iexplore_backup`.`pictures` (
  `EventID` int(11) NOT NULL,
  `Logo` varchar(45) NOT NULL,
  `Picture` varchar(45) NOT NULL,
  PRIMARY KEY (`EventID`),
  CONSTRAINT `EventID_fk` FOREIGN KEY (`EventID`) REFERENCES `event` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS `iexplore_backup`.`location`;
CREATE TABLE `iexplore_backup`.`location` (
  `EventID` int(11) NOT NULL,
  `Latitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `City` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`EventID`),
  CONSTRAINT `EventID3kk` FOREIGN KEY (`EventID`) REFERENCES `event` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
);

END$$

CREATE DEFINER=`swprodb`@`%` PROCEDURE `copyPosts` ()  BEGIN

DROP TABLE IF EXISTS `iexplore_backup`.`post`;
CREATE TABLE `iexplore_backup`.`post` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `threadID` int(11) NOT NULL,
  `postAuthor` int(11) NOT NULL,
  `postContent` text NOT NULL,
  `postTime` varchar(20) NOT NULL,
  `postLastEdited` varchar(20) NOT NULL,
  PRIMARY KEY (`Id`,`threadID`,`postAuthor`),
  KEY `postAuthor_idx` (`postAuthor`),
  KEY `threadID_idx` (`threadID`),
  CONSTRAINT `postAuthor` FOREIGN KEY (`postAuthor`) REFERENCES `users` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `threadID` FOREIGN KEY (`threadID`) REFERENCES `thread` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

END$$

CREATE DEFINER=`swprodb`@`%` PROCEDURE `copyThreads` ()  BEGIN

DROP TABLE IF EXISTS `iexplore_backup`.`thread`;
CREATE TABLE `iexplore_backup`.`thread` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `categoryID` int(11) NOT NULL,
  `threadTitle` varchar(45) NOT NULL,
  `threadAuthor` int(11) NOT NULL,
  `threadFirstPost` int(11) DEFAULT NULL,
  `threadLastPost` int(11) DEFAULT NULL,
  `threadLocked` int(11) NOT NULL DEFAULT '0',
  `threadType` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`,`categoryID`,`threadAuthor`),
  KEY `categoryID_idx` (`categoryID`),
  KEY `author_idx` (`threadAuthor`),
  CONSTRAINT `author` FOREIGN KEY (`threadAuthor`) REFERENCES `users` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `categoryID` FOREIGN KEY (`categoryID`) REFERENCES `category` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
);

END$$

CREATE DEFINER=`swprodb`@`%` PROCEDURE `copyTransactions` ()  BEGIN

DROP TABLE IF EXISTS `iexplore_backup`.`transactions`;
CREATE TABLE `iexplore_backup`.`transactions` (
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
);

END$$

CREATE DEFINER=`swprodb`@`%` PROCEDURE `copyUsers` ()  BEGIN

DROP TABLE IF EXISTS `iexplore_backup`.`users`;
CREATE TABLE `iexplore_backup`.`users` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Email` varchar(45) NOT NULL,
  `FirstName` varchar(45) NOT NULL,
  `LastName` varchar(45) NOT NULL,
  `AccessLevel` int(1) unsigned zerofill NOT NULL DEFAULT '0',
  `CourseID` int(11) DEFAULT '5',
  `Password` varchar(45) NOT NULL,
  `Picture` varchar(100) DEFAULT 'https://i.imgur.com/KQ5pb3s.png',
  PRIMARY KEY (`Id`),
  KEY `Id_idx` (`CourseID`),
  CONSTRAINT `Idfxk` FOREIGN KEY (`CourseID`) REFERENCES `courses` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
);

END$$

CREATE DEFINER=`swprodb`@`%` PROCEDURE `copyWishlist` ()  BEGIN

DROP TABLE IF EXISTS `iexplore_backup`.`wishlist`;
CREATE TABLE `iexplore_backup`.`wishlist` (
  `StudentID` int(11) NOT NULL,
  `EventID` int(11) NOT NULL,
  PRIMARY KEY (`EventID`,`StudentID`),
  KEY `StudentID_idx` (`StudentID`),
  CONSTRAINT `EventID` FOREIGN KEY (`EventID`) REFERENCES `event` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `StudentID` FOREIGN KEY (`StudentID`) REFERENCES `users` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
);

END$$

CREATE DEFINER=`swprodb`@`%` PROCEDURE `createCourses` ()  BEGIN
DROP TABLE IF EXISTS `iexplore_backup`.`courses`;
CREATE TABLE `iexplore_backup`.`courses` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  PRIMARY KEY (`Id`)
);
END$$

CREATE DEFINER=`swprodb`@`%` PROCEDURE `session_call` (IN `userID` INT)  BEGIN
CALL session_procedure(userID);
END$$

CREATE DEFINER=`swprodb`@`%` PROCEDURE `session_procedure` (IN `userID` INT)  BEGIN
SELECT SLEEP(10);
UPDATE users 
SET users.Active = 0 
WHERE users.Id = userID;

END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Структура таблицы `category`
--

CREATE TABLE `category` (
  `Id` int(2) NOT NULL,
  `Name` varchar(155) NOT NULL,
  `type` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `category`
--

INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(1, 'General Discussion', 'general');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(2, 'CTS', 'course');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(3, 'INF', 'course');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(4, 'CSE', 'course');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(5, 'WF', 'course');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(12, 'Excursion to Hogwarts!', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(13, 'Trip to Neuschwanstein Castle', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(14, 'Tour of one of the largest palaces.', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(15, 'Excursion to one of Germany\'s best parks!', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(16, 'Johann Sebastian Bach\'s workplace.', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(17, 'Germany\'s largest art galleries.', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(18, 'Best Excursion', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(22, 'Party New Party alsdkas', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(23, 'Party in Neu Ulm', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(24, 'Johann Sebastian Bach\'s workplace.', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(35, 'Best Excursion', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(37, 'News and Announcements', 'general');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(38, 'Suggestions', 'general');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(39, 'General Help and Support', 'general');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(77, '[test event] -> delete', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(78, '[test] -> delete', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(79, '[test] ->delete', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(80, 'ttesttesttest', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(81, '[test] -> delete', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(82, '[test][test]', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(83, 'sgegqrgqerrgerrg', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(84, 'testtesttest', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(85, 'Excursion to one of Germany\'s best parks!', 'event');
INSERT INTO `category` (`Id`, `Name`, `type`) VALUES(86, 'New excursion created', 'event');

-- --------------------------------------------------------

--
-- Структура таблицы `courses`
--

CREATE TABLE `courses` (
  `Id` int(2) NOT NULL,
  `Name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `courses`
--

INSERT INTO `courses` (`Id`, `Name`) VALUES(1, 'CTS');
INSERT INTO `courses` (`Id`, `Name`) VALUES(2, 'INF');
INSERT INTO `courses` (`Id`, `Name`) VALUES(3, 'CSE');
INSERT INTO `courses` (`Id`, `Name`) VALUES(4, 'WF');
INSERT INTO `courses` (`Id`, `Name`) VALUES(5, 'None');

-- --------------------------------------------------------

--
-- Структура таблицы `event`
--

CREATE TABLE `event` (
  `Id` int(4) NOT NULL,
  `Date` date DEFAULT '2020-04-19',
  `Company` varchar(45) DEFAULT 'Hochschule Ulm',
  `Price` double DEFAULT '0',
  `TotalPlaces` int(2) DEFAULT NULL,
  `AvailablePlaces` int(2) DEFAULT NULL,
  `ShortDescription` varchar(60) DEFAULT NULL,
  `LongDescription` text,
  `EVENT_TYPE` varchar(45) NOT NULL DEFAULT 'COMPANY_EXCURSION'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `event`
--

INSERT INTO `event` (`Id`, `Date`, `Company`, `Price`, `TotalPlaces`, `AvailablePlaces`, `ShortDescription`, `LongDescription`, `EVENT_TYPE`) VALUES(9, '2020-03-13', 'Hochschule Ulm', 45, 35, 14, 'Trip to Neuschwanstein Castle', 'Witness the fairy tale castle of Neuschwanstein and Linderhof, created by Germany’’s 19th-century King Ludwig II, on a day trip from Ulm. With its snow-white limestone facade and fanciful turrets peeking out from the forested mountain tops of the Hohenschwangau valley, Neuschwanstein Castle could easily have been lifted from the pages of a fairy tale. In a way, it has—the German castle famously inspired Disney\'s Sleeping Beauty castle..', 'EXCURSION');
INSERT INTO `event` (`Id`, `Date`, `Company`, `Price`, `TotalPlaces`, `AvailablePlaces`, `ShortDescription`, `LongDescription`, `EVENT_TYPE`) VALUES(10, '2020-05-10', 'Hochschule Ulm', 65, 30, 13, 'Tour of one of the largest palaces.', ' The New Palace is an 18th-century Baroque palace and is one of the last large city palaces built in Southern Germany. It is located in the center of Stuttgart. Once a historic residence of the Kings of Württemberg, the New Palace derives its name from its commissioning by Duke Carl Eugen of Württemberg to replace the Old Castle in the early years of his reign. Join us in a tour of the palace and surrounding areas.', 'EXCURSION');
INSERT INTO `event` (`Id`, `Date`, `Company`, `Price`, `TotalPlaces`, `AvailablePlaces`, `ShortDescription`, `LongDescription`, `EVENT_TYPE`) VALUES(11, '2020-05-10', 'Hochschule Ulm', 45, 25, 16, 'Excursion to one of Germany\'s best parks!', 'The Olympic Park Munich in Munich, Germany, is an Olympic Park which was constructed for the 1972 Summer Olympics. Located in the Oberwiesenfeld neighborhood of Munich, the Park continues to serve as a venue for cultural, social, and religious events. The plan is to go around the whole park and if there is time do some sightseeing. ', 'EXCURSION');
INSERT INTO `event` (`Id`, `Date`, `Company`, `Price`, `TotalPlaces`, `AvailablePlaces`, `ShortDescription`, `LongDescription`, `EVENT_TYPE`) VALUES(13, '2020-05-10', 'Hochschule Ulm', 35, 20, 15, 'Johann Sebastian Bach\'s workplace.', 'St. Thomas Church is located in Leipzig, Germany. It is a well-known church, mainly because of Johann Sebastian Bach who worked here as a music director from 1723 until his death in 1750. Today, the church also holds his remains. Although rebuilt over the centuries, the church today retains the character of a late-Gothic hall church. The church has offered us a tour of the inside facility and a brief presentation on the history of the church.', 'EXCURSION');
INSERT INTO `event` (`Id`, `Date`, `Company`, `Price`, `TotalPlaces`, `AvailablePlaces`, `ShortDescription`, `LongDescription`, `EVENT_TYPE`) VALUES(14, '2020-05-10', 'Hochschule Ulm', 40, 30, 24, 'Germany\'s largest art galleries.', 'The Hamburger Kunsthalle was founded in 1850, consists of three connected buildings and is one of the largest museums in the country. The art gallery houses one of the few art collections in Germany that covers seven centuries of European art, from the Middle Ages to the present day.We will be taking a tour of the entire museum and we have the pleasure of seeing of all the fascinating paintings and art', 'EXCURSION');
INSERT INTO `event` (`Id`, `Date`, `Company`, `Price`, `TotalPlaces`, `AvailablePlaces`, `ShortDescription`, `LongDescription`, `EVENT_TYPE`) VALUES(1007, '2020-05-11', 'Hochschule Ulm', 70, 30, 11, 'Best Excursion', ' The New Palace is an 18th-century Baroque palace and is one of the last large city palaces built in Southern Germany. It is located in the center of Stuttgart. Once a historic residence of the Kings of Württemberg, the New Palace derives its name from its commissioning by Duke Carl Eugen of Württemberg to replace the Old Castle in the early years of his reign. Join us in a tour of the palace and surrounding areas.', 'EXCURSION');
INSERT INTO `event` (`Id`, `Date`, `Company`, `Price`, `TotalPlaces`, `AvailablePlaces`, `ShortDescription`, `LongDescription`, `EVENT_TYPE`) VALUES(1008, '2020-05-10', 'Hochschule Ulm', 45, 25, 24, 'Excursion to one of Germany\'s best parks!', 'The Olympic Park Munich in Munich, Germany, is an Olympic Park which was constructed for the 1972 Summer Olympics. Located in the Oberwiesenfeld neighborhood of Munich, the Park continues to serve as a venue for cultural, social, and religious events. The plan is to go around the whole park and if there is time do some sightseeing. ', 'EXCURSION');
INSERT INTO `event` (`Id`, `Date`, `Company`, `Price`, `TotalPlaces`, `AvailablePlaces`, `ShortDescription`, `LongDescription`, `EVENT_TYPE`) VALUES(1009, '2020-05-10', 'Hochschule Ulm', 45, 25, 24, 'New excursion created', 'The Olympic Park Munich in Munich, Germany, is an Olympic Park which was constructed for the 1972 Summer Olympics. Located in the Oberwiesenfeld neighborhood of Munich, the Park continues to serve as a venue for cultural, social, and religious events. The plan is to go around the whole park and if there is time do some sightseeing. ', 'EXCURSION');

--
-- Триггеры `event`
--
DELIMITER $$
CREATE TRIGGER `create_discussion` AFTER INSERT ON `event` FOR EACH ROW BEGIN 
    
    INSERT INTO category(Name,type)
    VALUES(NEW.shortDescription, 'event');

END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `update_discussion` AFTER UPDATE ON `event` FOR EACH ROW BEGIN     
    UPDATE category SET Name=NEW.shortDescription
    WHERE Name=OLD.shortDescription;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Структура таблицы `feedback`
--

CREATE TABLE `feedback` (
  `Id` int(5) NOT NULL,
  `UserID` int(5) NOT NULL,
  `Rating` double NOT NULL,
  `Message` varchar(250) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `feedback`
--

INSERT INTO `feedback` (`Id`, `UserID`, `Rating`, `Message`) VALUES(61, 10, 5, 'Great app, keep it up');
INSERT INTO `feedback` (`Id`, `UserID`, `Rating`, `Message`) VALUES(62, 803, 5, 'Nice app CTS');
INSERT INTO `feedback` (`Id`, `UserID`, `Rating`, `Message`) VALUES(63, 106, 5, 'Make 0 starts by default nigga');
INSERT INTO `feedback` (`Id`, `UserID`, `Rating`, `Message`) VALUES(64, 113, 4, 'Why are the admins not able to give feedback? Discrimination...');
INSERT INTO `feedback` (`Id`, `UserID`, `Rating`, `Message`) VALUES(65, 118, 5, 'Niiice app');
INSERT INTO `feedback` (`Id`, `UserID`, `Rating`, `Message`) VALUES(66, 123, 5, 'your application sucks epicly');

-- --------------------------------------------------------

--
-- Структура таблицы `invoice`
--

CREATE TABLE `invoice` (
  `Id` int(4) NOT NULL,
  `CustomerName` varchar(45) DEFAULT NULL,
  `Date` date DEFAULT NULL,
  `Ammount` double DEFAULT NULL,
  `EventName` varchar(60) DEFAULT NULL,
  `Company` varchar(50) DEFAULT NULL,
  `TransactionID` int(5) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `invoice`
--

INSERT INTO `invoice` (`Id`, `CustomerName`, `Date`, `Ammount`, `EventName`, `Company`, `TransactionID`) VALUES(62, 'Pitt', '2020-01-01', 45, 'Trip to Neuschwanstein Castle', 'Hochschule Ulm', 392);
INSERT INTO `invoice` (`Id`, `CustomerName`, `Date`, `Ammount`, `EventName`, `Company`, `TransactionID`) VALUES(63, 'Pitt', '2020-01-01', 65, 'Tour of one of the largest palaces.', 'Hochschule Ulm', 393);
INSERT INTO `invoice` (`Id`, `CustomerName`, `Date`, `Ammount`, `EventName`, `Company`, `TransactionID`) VALUES(64, 'Pitt', '2020-01-01', 45, 'Excursion to one of Germany\'s best parks!', 'Hochschule Ulm', 394);
INSERT INTO `invoice` (`Id`, `CustomerName`, `Date`, `Ammount`, `EventName`, `Company`, `TransactionID`) VALUES(65, 'Pitt', '2020-01-01', 35, 'Johann Sebastian Bach\'s workplace.', 'Hochschule Ulm', 395);
INSERT INTO `invoice` (`Id`, `CustomerName`, `Date`, `Ammount`, `EventName`, `Company`, `TransactionID`) VALUES(66, 'Bredesen', '2020-01-06', 40, 'Germany\'s largest art galleries.', 'Hochschule Ulm', 402);
INSERT INTO `invoice` (`Id`, `CustomerName`, `Date`, `Ammount`, `EventName`, `Company`, `TransactionID`) VALUES(67, 'Pitt', '2020-01-01', 40, 'Germany\'s largest art galleries.', 'Hochschule Ulm', 396);
INSERT INTO `invoice` (`Id`, `CustomerName`, `Date`, `Ammount`, `EventName`, `Company`, `TransactionID`) VALUES(68, 'Bredesen', '2020-01-06', 35, 'Johann Sebastian Bach\'s workplace.', 'Hochschule Ulm', 403);
INSERT INTO `invoice` (`Id`, `CustomerName`, `Date`, `Ammount`, `EventName`, `Company`, `TransactionID`) VALUES(69, 'Pitt', '2020-01-07', 70, 'Best Excursion', 'Hochschule Ulm', 404);
INSERT INTO `invoice` (`Id`, `CustomerName`, `Date`, `Ammount`, `EventName`, `Company`, `TransactionID`) VALUES(70, 'Pitt', '2020-01-01', 70, 'Best Excursion', 'Hochschule Ulm', 397);

-- --------------------------------------------------------

--
-- Структура таблицы `location`
--

CREATE TABLE `location` (
  `EventID` int(4) NOT NULL,
  `Latitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `City` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `location`
--

INSERT INTO `location` (`EventID`, `Latitude`, `Longitude`, `City`) VALUES(9, 47.557915, 10.749801, 'Hohenschwangau');
INSERT INTO `location` (`EventID`, `Latitude`, `Longitude`, `City`) VALUES(10, 48.778167, 9.18186, 'Stuttgartt');
INSERT INTO `location` (`EventID`, `Latitude`, `Longitude`, `City`) VALUES(11, 48.175679, 11.551743, 'Munich');
INSERT INTO `location` (`EventID`, `Latitude`, `Longitude`, `City`) VALUES(13, 51.339178, 12.372199, 'Munich');
INSERT INTO `location` (`EventID`, `Latitude`, `Longitude`, `City`) VALUES(14, 53.555683, 10.002567, 'Hamburg');
INSERT INTO `location` (`EventID`, `Latitude`, `Longitude`, `City`) VALUES(1007, 48.778167, 9.181861, 'Stuttgart');
INSERT INTO `location` (`EventID`, `Latitude`, `Longitude`, `City`) VALUES(1008, 48.175679, 11.551743, 'Munich');
INSERT INTO `location` (`EventID`, `Latitude`, `Longitude`, `City`) VALUES(1009, 48.175679, 11.551743, 'Munich');

-- --------------------------------------------------------

--
-- Структура таблицы `pictures`
--

CREATE TABLE `pictures` (
  `EventID` int(4) NOT NULL,
  `Logo` varchar(45) NOT NULL,
  `Picture` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `pictures`
--

INSERT INTO `pictures` (`EventID`, `Logo`, `Picture`) VALUES(9, 'https://i.imgur.com/LVkjPcV.png', 'https://i.imgur.com/hHoxKuf.png');
INSERT INTO `pictures` (`EventID`, `Logo`, `Picture`) VALUES(10, 'https://i.imgur.com/IPIBdgC.jpg', 'https://i.imgur.com/Eyak4nt.jpg');
INSERT INTO `pictures` (`EventID`, `Logo`, `Picture`) VALUES(11, 'https://i.imgur.com/zHNHIRE.png', 'https://i.imgur.com/DsGo0bZ.jpg');
INSERT INTO `pictures` (`EventID`, `Logo`, `Picture`) VALUES(13, 'https://i.imgur.com/y02zvQU.png', 'https://i.imgur.com/JpHITu6.jpg');
INSERT INTO `pictures` (`EventID`, `Logo`, `Picture`) VALUES(14, 'https://i.imgur.com/vl1jzL6.png', 'https://i.imgur.com/eiG1qvU.jpg');
INSERT INTO `pictures` (`EventID`, `Logo`, `Picture`) VALUES(1007, 'https://i.imgur.com/rcphR1A.png', 'https://i.imgur.com/VU5rSxD.png');
INSERT INTO `pictures` (`EventID`, `Logo`, `Picture`) VALUES(1008, 'https://i.imgur.com/JZS07Ke.png', 'https://i.imgur.com/Sp9o1Pa.png');
INSERT INTO `pictures` (`EventID`, `Logo`, `Picture`) VALUES(1009, 'https://i.imgur.com/CgVs90j.png', 'https://i.imgur.com/k7YoYyg.png');

-- --------------------------------------------------------

--
-- Структура таблицы `post`
--

CREATE TABLE `post` (
  `Id` int(11) NOT NULL,
  `threadID` int(6) NOT NULL,
  `postAuthor` int(5) DEFAULT NULL,
  `postContent` blob NOT NULL,
  `postTime` varchar(20) CHARACTER SET latin1 NOT NULL,
  `postLastEdited` varchar(20) CHARACTER SET latin1 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `post`
--

INSERT INTO `post` (`Id`, `threadID`, `postAuthor`, `postContent`, `postTime`, `postLastEdited`) VALUES(1, 1, 777, 0x736a64663b6c6b647368666c73646866736c64686673646c3b66733b6c646a, '1576744173648', '1576744173648');
INSERT INTO `post` (`Id`, `threadID`, `postAuthor`, `postContent`, `postTime`, `postLastEdited`) VALUES(4, 1, 118, 0xf09f988ef09f988ef09f988ef09f988ef09f988e, '1576875045709', '1576875045709');
INSERT INTO `post` (`Id`, `threadID`, `postAuthor`, `postContent`, `postTime`, `postLastEdited`) VALUES(5, 1, 786, 0x686579207468657265206861686168, '1577583391581', '1577583391581');
INSERT INTO `post` (`Id`, `threadID`, `postAuthor`, `postContent`, `postTime`, `postLastEdited`) VALUES(6, 2, 118, 0x67646667666766646764736667646667, '1577657931589', '1577657931589');
INSERT INTO `post` (`Id`, `threadID`, `postAuthor`, `postContent`, `postTime`, `postLastEdited`) VALUES(7, 3, 118, 0x6466736e666c6a6e660977656e6677656e667765, '1577658011124', '1577658011124');

-- --------------------------------------------------------

--
-- Структура таблицы `thread`
--

CREATE TABLE `thread` (
  `Id` int(6) NOT NULL,
  `categoryID` int(2) NOT NULL,
  `threadTitle` blob NOT NULL,
  `threadAuthor` int(5) DEFAULT NULL,
  `threadFirstPost` int(11) DEFAULT NULL,
  `threadLastPost` int(11) DEFAULT NULL,
  `threadLocked` int(1) NOT NULL DEFAULT '0',
  `threadType` int(5) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `thread`
--

INSERT INTO `thread` (`Id`, `categoryID`, `threadTitle`, `threadAuthor`, `threadFirstPost`, `threadLastPost`, `threadLocked`, `threadType`) VALUES(1, 1, 0x57656c636f6d6520746f204578706c6f7265487562, 777, 1, 5, 0, 0);
INSERT INTO `thread` (`Id`, `categoryID`, `threadTitle`, `threadAuthor`, `threadFirstPost`, `threadLastPost`, `threadLocked`, `threadType`) VALUES(2, 2, 0x6767, 118, 6, 6, 0, 0);
INSERT INTO `thread` (`Id`, `categoryID`, `threadTitle`, `threadAuthor`, `threadFirstPost`, `threadLastPost`, `threadLocked`, `threadType`) VALUES(3, 2, 0x66736466, 118, 7, 7, 0, 0);

-- --------------------------------------------------------

--
-- Структура таблицы `transactions`
--

CREATE TABLE `transactions` (
  `Id` int(5) NOT NULL,
  `StudentID` int(5) NOT NULL,
  `Date` date NOT NULL,
  `Completed` int(1) DEFAULT NULL,
  `EventID` int(4) NOT NULL,
  `PaymentMethod` int(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `transactions`
--

INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(392, 118, '2020-01-01', 1, 9, 0);
INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(393, 118, '2020-01-01', 1, 10, 0);
INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(394, 118, '2020-01-01', 1, 11, 0);
INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(395, 118, '2020-01-01', 1, 13, 0);
INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(396, 118, '2020-01-01', 1, 14, 1);
INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(397, 118, '2020-01-01', 1, 1007, 1);
INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(398, 123, '2020-01-02', 1, 9, 1);
INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(399, 113, '2020-01-05', 1, 11, 1);
INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(400, 6, '2020-01-06', 3, 11, 2);
INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(401, 6, '2020-01-06', 3, 13, 2);
INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(402, 6, '2020-01-06', 1, 14, 2);
INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(403, 6, '2020-01-06', 3, 13, 1);
INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(404, 118, '2020-01-07', 3, 1007, 1);
INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(405, 110, '2020-01-07', 0, 1007, 1);
INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(406, 110, '2020-01-07', 0, 1009, 1);
INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(407, 110, '2020-01-07', 0, 1008, 1);
INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(408, 110, '2020-01-07', 0, 9, 1);
INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`, `PaymentMethod`) VALUES(409, 110, '2020-01-07', 0, 14, 1);

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

CREATE TABLE `users` (
  `Id` int(5) NOT NULL,
  `Email` varchar(45) NOT NULL,
  `FirstName` varchar(45) NOT NULL,
  `LastName` varchar(45) NOT NULL,
  `AccessLevel` int(1) UNSIGNED ZEROFILL NOT NULL DEFAULT '0',
  `CourseID` int(2) DEFAULT '5',
  `Password` varchar(45) NOT NULL,
  `Picture` varchar(45) NOT NULL DEFAULT '/IMG/icon-account.png',
  `Active` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(6, 'Bredesen@hs-ulm.de', 'Denise', 'Bredesen', 0, 1, 'user6', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(7, 'Qualls@hs-ulm.de', 'Mark', 'Qualls', 0, 1, 'user7', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(8, 'Giles@hs-ulm.de', 'Marie', 'Giles', 0, 3, 'user8', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(9, 'Folta@hs-ulm.de', 'Jennifer', 'Folta', 0, 2, 'user9', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(10, 'Baker@hs-ulm.de', 'Sharon', 'Baker', 0, 4, 'user10', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(11, 'Chavez@hs-ulm.de', 'Peggie', 'Chavez', 0, 3, 'user11', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(12, 'Craner@hs-ulm.de', 'Juana', 'Craner', 0, 1, 'user12', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(13, 'Merrifield@hs-ulm.de', 'Jason', 'Merrifield', 0, 2, 'user13', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(17, 'Pengelly@hs-ulm.de', 'Christopher', 'Pengelly', 0, 3, 'user17', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(18, 'Bentley@hs-ulm.de', 'Jane', 'Bentley', 0, 2, 'user18', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(19, 'Hopkins@hs-ulm.de', 'Charles', 'Hopkins', 0, 4, 'user19', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(20, 'Wimmer@hs-ulm.de', 'Robert', 'Wimmer', 0, 4, 'user20', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(21, 'Powers@hs-ulm.de', 'Danny', 'Powers', 0, 2, 'user21', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(22, 'Stein@hs-ulm.de', 'Robert', 'Stein', 0, 4, 'user22', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(23, 'Borghese@hs-ulm.de', 'Leonel', 'Borghese', 0, 4, 'user23', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(24, 'Poremski@hs-ulm.de', 'Audrey', 'Poremski', 0, 3, 'user24', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(25, 'Peeples@hs-ulm.de', 'Dale', 'Peeples', 0, 4, 'user25', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(27, 'Gipe@hs-ulm.de', 'Raymond', 'Gipe', 0, 1, 'user27', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(28, 'Wilborn@hs-ulm.de', 'Josephine', 'Wilborn', 0, 3, 'user28', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(29, 'Burkett@hs-ulm.de', 'Fred', 'Burkett', 0, 1, 'user29', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(30, 'Kriner@hs-ulm.de', 'Clay', 'Kriner', 0, 1, 'user30', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(31, 'Harding@hs-ulm.de', 'Lloyd', 'Harding', 0, 3, 'user31', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(32, 'Palmer@hs-ulm.de', 'Shirley', 'Palmer', 0, 3, 'user32', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(33, 'Crumrine@hs-ulm.de', 'Bobby', 'Crumrine', 0, 3, 'user33', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(34, 'Hernandez@hs-ulm.de', 'Nicholas', 'Hernandez', 0, 1, 'user34', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(35, 'Frazier@hs-ulm.de', 'James', 'Frazier', 0, 4, 'user35', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(36, 'Roberts@hs-ulm.de', 'Linda', 'Roberts', 0, 4, 'user36', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(37, 'King@hs-ulm.de', 'Jefferson', 'King', 0, 2, 'user37', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(38, 'Lindsay@hs-ulm.de', 'Gerald', 'Lindsay', 0, 3, 'user38', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(39, 'Anaya@hs-ulm.de', 'Robert', 'Anaya', 0, 2, 'user39', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(40, 'Lewis@hs-ulm.de', 'Sylvia', 'Lewis', 0, 4, 'user40', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(41, 'Tuck@hs-ulm.de', 'Shirley', 'Tuck', 0, 1, 'user41', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(42, 'Takahashi@hs-ulm.de', 'Leigh', 'Takahashi', 0, 1, 'user42', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(43, 'Sutter@hs-ulm.de', 'Thomas', 'Sutter', 0, 3, 'user43', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(44, 'Amsterdam@hs-ulm.de', 'Matilda', 'Amsterdam', 0, 2, 'user44', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(45, 'Cecil@hs-ulm.de', 'Charlene', 'Cecil', 0, 4, 'user45', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(46, 'Nappi@hs-ulm.de', 'Billy', 'Nappi', 0, 2, 'user46', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(47, 'Freeman@hs-ulm.de', 'Maria', 'Freeman', 0, 2, 'user47', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(48, 'Bacich@hs-ulm.de', 'Bobby', 'Bacich', 0, 4, 'user48', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(49, 'Rappold@hs-ulm.de', 'Carol', 'Rappold', 0, 3, 'user49', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(50, 'williamsomething@hs-ulm.de', 'William', 'Cantwell', 0, 1, 'user50', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(51, 'Johnston@hs-ulm.de', 'Jerry', 'Johnston', 0, 2, 'user51', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(52, 'Wenrich@hs-ulm.de', 'Harold', 'Wenrich', 0, 4, 'user52', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(53, 'Datson@hs-ulm.de', 'Richard', 'Datson', 0, 1, 'user53', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(54, 'Smith@hs-ulm.de', 'Laura', 'Smith', 0, 4, 'user54', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(55, 'Guillen@hs-ulm.de', 'Sue', 'Guillen', 0, 3, 'user55', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(56, 'Turner@hs-ulm.de', 'Ian', 'Turner', 0, 3, 'user56', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(57, 'Reed@hs-ulm.de', 'Karen', 'Reed', 0, 1, 'user57', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(58, 'Rowell@hs-ulm.de', 'Elvis', 'Rowell', 0, 3, 'user58', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(59, 'Emerson@hs-ulm.de', 'Leon', 'Emerson', 0, 3, 'user59', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(60, 'Harju@hs-ulm.de', 'Debbie', 'Harju', 0, 3, 'user60', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(61, 'Robbins@hs-ulm.de', 'Stanley', 'Robbins', 0, 2, 'user61', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(62, 'Swiger@hs-ulm.de', 'Ann', 'Swiger', 0, 1, 'user62', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(63, 'Armstrong@hs-ulm.de', 'Raymond', 'Armstrong', 0, 4, 'user63', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(64, 'Reid@hs-ulm.de', 'Shirley', 'Reid', 0, 3, 'user64', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(65, 'Bonham@hs-ulm.de', 'Reginald', 'Bonham', 0, 3, 'user65', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(66, 'Stumbaugh@hs-ulm.de', 'Kathy', 'Stumbaugh', 0, 3, 'user66', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(67, 'Alston@hs-ulm.de', 'Brandon', 'Alston', 0, 3, 'user67', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(69, 'Simpson@hs-ulm.de', 'Kendra', 'Simpson', 0, 2, 'user69', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(70, 'Newsome@hs-ulm.de', 'Lillian', 'Newsome', 0, 3, 'user70', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(71, 'Rayborn@hs-ulm.de', 'Catherine', 'Rayborn', 0, 2, 'user71', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(72, 'Ryan@hs-ulm.de', 'Andres', 'Ryan', 0, 4, 'user72', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(73, 'Gonzalez@hs-ulm.de', 'Jose', 'Gonzalez', 0, 1, 'user73', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(74, 'Griffin@hs-ulm.de', 'Reuben', 'Griffin', 0, 4, 'user74', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(75, 'Pederson@hs-ulm.de', 'Alice', 'Pederson', 0, 4, 'user75', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(76, 'Chi@hs-ulm.de', 'Johnny', 'Chi', 0, 1, 'user76', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(77, 'Maynard@hs-ulm.de', 'Ashley', 'Maynard', 0, 2, 'user77', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(78, 'George@hs-ulm.de', 'Jeffrey', 'George', 0, 1, 'user78', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(79, 'Goldfeder@hs-ulm.de', 'Rose', 'Goldfeder', 0, 2, 'user79', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(80, 'Rader@hs-ulm.de', 'Shelly', 'Rader', 0, 1, 'user80', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(81, 'Iverson@hs-ulm.de', 'Francine', 'Iverson', 0, 2, 'user81', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(82, 'Brown@hs-ulm.de', 'Joseph', 'Brown', 0, 2, 'user82', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(83, 'Stechlinski@hs-ulm.de', 'Sarah', 'Stechlinski', 0, 2, 'user83', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(84, 'Walker@hs-ulm.de', 'Mildred', 'Walker', 0, 4, 'user84', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(85, 'Gleaves@hs-ulm.de', 'Jonathan', 'Gleaves', 0, 3, 'user85', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(86, 'Monath@hs-ulm.de', 'William', 'Monath', 0, 4, 'user86', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(87, 'Braswell@hs-ulm.de', 'Heidi', 'Braswell', 0, 2, 'user87', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(88, 'Dunn@hs-ulm.de', 'Ramon', 'Dunn', 0, 1, 'user88', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(89, 'Brannon@hs-ulm.de', 'Jackie', 'Brannon', 0, 3, 'user89', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(90, 'Martin@hs-ulm.de', 'Paris', 'Martin', 0, 4, 'user90', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(91, 'Wolford@hs-ulm.de', 'Catherine', 'Wolford', 0, 2, 'user91', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(92, 'Howard@hs-ulm.de', 'Victor', 'Howard', 0, 1, 'user92', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(93, 'Haralson@hs-ulm.de', 'Harry', 'Haralson', 0, 3, 'user93', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(94, 'Cook@hs-ulm.de', 'Tammy', 'Cook', 0, 3, 'user94', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(95, 'Silver@hs-ulm.de', 'Santos', 'Silver', 0, 2, 'user95', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(96, 'Staples@hs-ulm.de', 'Terri', 'Staples', 0, 3, 'user96', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(97, 'Mooney@hs-ulm.de', 'Mary', 'Mooney', 0, 3, 'user97', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(98, 'Anthony@hs-ulm.de', 'Thomas', 'Anthony', 0, 1, 'user98', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(99, 'Jones@hs-ulm.de', 'Arthur', 'Jones', 0, 2, 'user99', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(100, 'Wasserman@hs-ulm.de', 'Lincoln', 'Wasserman', 0, 1, 'user100', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(101, 'Clem@hs-ulm.de', 'Peggy', 'Clem', 1, 5, 'hahahalol', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(102, 'Thomas@hs-ulm.de', 'Anna', 'Thomas', 1, 5, 'admin2', 'https://i.imgur.com/keFJhgB.png', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(106, 'thenotoriuousmma@hs-ulm.de', 'Connor', 'Mcgregor', 0, 1, 'hahaha', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(107, 'miketyson@hs-ulm.de', 'Mike', 'Tyson', 0, 1, 'miketyson', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(109, 'kebabnurmagomedov@hs-ulm.de', 'Kebab', 'Nurmagomedov', 0, 4, 'kebab123', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(110, 'marmiss@mail.hs-ulm.de', 'Aleksejs', 'Marmiss', 0, 1, 'iddQd', 'https://i.imgur.com/hrwKITE.png', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(111, 'gicumironica@mail.hs-ulm.de', 'Giku', 'Mironica', 0, 1, 'hahaha', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(112, 'basit@hs-ulm.de', 'Abdul', 'Basit', 0, 1, 'basit123', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(113, 'ryazev@mail.hs-ulm.de', 'Hidayat', 'Rzayev', 0, 1, '123', 'https://i.imgur.com/0ZZxvLU.png', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(114, 'barackobama@mail.hs-ulm.de', 'Barack', 'Obama', 0, 1, 'lmaoloool', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(116, 'braz@hs-ulm.de', 'Braz', 'Castana', 0, 1, 'salt', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(117, 'waffo@mail.hs-ulm.de', 'Nelson', 'Waffo', 0, 1, 'manchild', 'https://files.catbox.moe/5nhpx3.jfi', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(118, '..', 'Brad', 'Pitt', 0, 1, '..', 'https://i.imgur.com/p4w0iL9.png', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(123, '...', 'Stop', 'Pit', 0, 1, '...', 'https://i.imgur.com/ote26Po.png', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(777, 'hochschule@hs-ulm.de', 'Hochschule', 'Ulm', 2, 5, '1234', 'https://i.imgur.com/EK2R1rn.jpg', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(778, 'donaldpump@hs-ulm.de', 'Donald', 'Pump', 1, 5, ' 123', 'https://i.imgur.com/HimoprE.png', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(786, 'bean@mail.hs-ulm.de', 'Mister', 'Bean', 0, 1, '123', 'https://i.imgur.com/bGG1DlJ.png', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(788, 'baer@hs-ulm.de', 'Klaus', 'Baer', 1, 5, '00f0cde5-5c2d-422e-b4f6-bf57dbef892b', '/IMG/icon-account.png', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(798, 'wgvs@mail.hs-ulm.de', 'wefwf', 'fwf', 0, 3, 'fsdfs', '/IMG/icon-account.png', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(799, 'fadf@mail.hs-ulm.de', 'fdfs', 'fsdfs', 0, 1, 'afsdf', '/IMG/icon-account.png', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(800, 'fsgEg@mail.hs-ulm.de', 'fsdfs', 'qgrhg', 0, 1, 'sdsdfsd', '/IMG/icon-account.png', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(801, 'sdfsf@mail.hs-ulm.de', 'sqrgqwgq', 'rgqrrgqe', 0, 1, 'efsdfs', '/IMG/icon-account.png', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(802, 'adfgag@mail.hs-ulm.de', 'rhqger', 'fqdgqwrh', 0, 1, 'fdsgwegwg', 'https://i.imgur.com/pyH4ZK4.png', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(803, 'bakri@mail.hs-ulm.de', 'Diaae', 'Bakri', 0, 1, '123456', '/IMG/icon-account.png', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(804, 'fdsfsdfs@mail.hs-ulm.de', 'fgdfsgdf', 'sdgsdfg', 0, 1, 'fdsfdfsdfsf', '/IMG/icon-account.png', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(806, 'mironica@mail.hs-ulm.de', 'Couch', 'Sofa', 0, 1, '1234', '/IMG/icon-account.png', 0);
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`, `Picture`, `Active`) VALUES(807, 'alex@hs-ulm.de', 'Alex', 'Boba', 1, 5, '87b744b9-3cec-4583-987a-01459afbc968', '/IMG/icon-account.png', 0);

-- --------------------------------------------------------

--
-- Структура таблицы `wishlist`
--

CREATE TABLE `wishlist` (
  `EventID` int(4) NOT NULL,
  `StudentID` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `wishlist`
--

INSERT INTO `wishlist` (`EventID`, `StudentID`) VALUES(10, 113);
INSERT INTO `wishlist` (`EventID`, `StudentID`) VALUES(1008, 118);
INSERT INTO `wishlist` (`EventID`, `StudentID`) VALUES(1009, 118);

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`Id`);

--
-- Индексы таблицы `courses`
--
ALTER TABLE `courses`
  ADD PRIMARY KEY (`Id`);

--
-- Индексы таблицы `event`
--
ALTER TABLE `event`
  ADD PRIMARY KEY (`Id`);

--
-- Индексы таблицы `feedback`
--
ALTER TABLE `feedback`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `UserID_FeedbackFK` (`UserID`);

--
-- Индексы таблицы `invoice`
--
ALTER TABLE `invoice`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `transactionfk_idx` (`TransactionID`);

--
-- Индексы таблицы `location`
--
ALTER TABLE `location`
  ADD PRIMARY KEY (`EventID`);

--
-- Индексы таблицы `pictures`
--
ALTER TABLE `pictures`
  ADD PRIMARY KEY (`EventID`);

--
-- Индексы таблицы `post`
--
ALTER TABLE `post`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `postAuthor_idx` (`postAuthor`),
  ADD KEY `ThreadID_PostFK_idx` (`threadID`);

--
-- Индексы таблицы `thread`
--
ALTER TABLE `thread`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `author_idx` (`threadAuthor`),
  ADD KEY `categoryId_idx` (`categoryID`);

--
-- Индексы таблицы `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `StudentID_idx` (`StudentID`),
  ADD KEY `EventID_idx` (`EventID`);

--
-- Индексы таблицы `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `Id_idx` (`CourseID`);

--
-- Индексы таблицы `wishlist`
--
ALTER TABLE `wishlist`
  ADD PRIMARY KEY (`EventID`,`StudentID`),
  ADD KEY `EventID_idx` (`EventID`),
  ADD KEY `StudentID_WishlistFK_idx` (`StudentID`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `category`
--
ALTER TABLE `category`
  MODIFY `Id` int(2) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=87;

--
-- AUTO_INCREMENT для таблицы `courses`
--
ALTER TABLE `courses`
  MODIFY `Id` int(2) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT для таблицы `event`
--
ALTER TABLE `event`
  MODIFY `Id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1010;

--
-- AUTO_INCREMENT для таблицы `feedback`
--
ALTER TABLE `feedback`
  MODIFY `Id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=67;

--
-- AUTO_INCREMENT для таблицы `invoice`
--
ALTER TABLE `invoice`
  MODIFY `Id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=71;

--
-- AUTO_INCREMENT для таблицы `post`
--
ALTER TABLE `post`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT для таблицы `thread`
--
ALTER TABLE `thread`
  MODIFY `Id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT для таблицы `transactions`
--
ALTER TABLE `transactions`
  MODIFY `Id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=410;

--
-- AUTO_INCREMENT для таблицы `users`
--
ALTER TABLE `users`
  MODIFY `Id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=808;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `feedback`
--
ALTER TABLE `feedback`
  ADD CONSTRAINT `UserID_FeedbackFK` FOREIGN KEY (`UserID`) REFERENCES `users` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `invoice`
--
ALTER TABLE `invoice`
  ADD CONSTRAINT `TransactionID_InvoiceFK` FOREIGN KEY (`TransactionID`) REFERENCES `transactions` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `location`
--
ALTER TABLE `location`
  ADD CONSTRAINT `EventID_LocationFK` FOREIGN KEY (`EventID`) REFERENCES `event` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `pictures`
--
ALTER TABLE `pictures`
  ADD CONSTRAINT `EventID_PicturesFK` FOREIGN KEY (`EventID`) REFERENCES `event` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `post`
--
ALTER TABLE `post`
  ADD CONSTRAINT `AuthorID_PostFK` FOREIGN KEY (`postAuthor`) REFERENCES `users` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ThreadID_PostFK` FOREIGN KEY (`threadID`) REFERENCES `thread` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ограничения внешнего ключа таблицы `thread`
--
ALTER TABLE `thread`
  ADD CONSTRAINT `AuthorID_ThreadFK` FOREIGN KEY (`threadAuthor`) REFERENCES `users` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `CategoryID_ThreadFK` FOREIGN KEY (`categoryID`) REFERENCES `category` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `EventID_TransactionFK` FOREIGN KEY (`EventID`) REFERENCES `event` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `StudentID_TransactionFK` FOREIGN KEY (`StudentID`) REFERENCES `users` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `CoursesID_UsersFK` FOREIGN KEY (`CourseID`) REFERENCES `courses` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `wishlist`
--
ALTER TABLE `wishlist`
  ADD CONSTRAINT `EventID_WishlistFK` FOREIGN KEY (`EventID`) REFERENCES `event` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `StudentID_WishlistFT` FOREIGN KEY (`StudentID`) REFERENCES `users` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

DELIMITER $$
--
-- События
--
CREATE DEFINER=`swprodb`@`%` EVENT `backup_database` ON SCHEDULE EVERY 1 DAY STARTS '2019-12-13 23:59:59' ON COMPLETION NOT PRESERVE ENABLE DO CALL backup_db()$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
