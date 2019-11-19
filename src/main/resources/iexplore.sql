-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Хост: swprodb.mysql.database.azure.com
-- Время создания: Ноя 04 2019 г., 11:47
-- Версия сервера: 5.7.26-log
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
CREATE DATABASE IF NOT EXISTS `iexplore` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `iexplore`;

-- --------------------------------------------------------

--
-- Структура таблицы `courses`
--

CREATE TABLE `courses` (
  `Id` int(11) NOT NULL,
  `Name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `courses`
--

INSERT INTO `courses` (`Id`, `Name`) VALUES(1, 'CTS');
INSERT INTO `courses` (`Id`, `Name`) VALUES(2, 'INF');
INSERT INTO `courses` (`Id`, `Name`) VALUES(3, 'CSE');
INSERT INTO `courses` (`Id`, `Name`) VALUES(4, 'WF');

-- --------------------------------------------------------

--
-- Структура таблицы `event`
--

CREATE TABLE `event` (
  `Id` int(11) NOT NULL,
  `Date` date NOT NULL,
  `Company` varchar(45) NOT NULL,
  `Price` double DEFAULT NULL,
  `TotalPlaces` int(11) NOT NULL,
  `AvailablePlaces` int(11) NOT NULL,
  `ShortDescription` varchar(45) DEFAULT NULL,
  `LongDescription` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `event`
--

INSERT INTO `event` (`Id`, `Date`, `Company`, `Price`, `TotalPlaces`, `AvailablePlaces`, `ShortDescription`, `LongDescription`) VALUES(1, '2019-10-10', 'BMW', 99.9, 20, 20, 'Excursion to BMW Car IT', 'This is the long description');
INSERT INTO `event` (`Id`, `Date`, `Company`, `Price`, `TotalPlaces`, `AvailablePlaces`, `ShortDescription`, `LongDescription`) VALUES(2, '2019-10-11', 'Daimler', 90, 10, 10, 'Excursion to Daimler TSS', 'This is the long description');
INSERT INTO `event` (`Id`, `Date`, `Company`, `Price`, `TotalPlaces`, `AvailablePlaces`, `ShortDescription`, `LongDescription`) VALUES(3, '2020-01-10', 'Elektrobit', 0, 20, 20, 'Take a look inside the Automotive World!', 'this is the long description');
INSERT INTO `event` (`Id`, `Date`, `Company`, `Price`, `TotalPlaces`, `AvailablePlaces`, `ShortDescription`, `LongDescription`) VALUES(4, '2020-01-10', 'Audi', 0, 20, 20, 'For the VAG fans', 'this is the long description');
INSERT INTO `event` (`Id`, `Date`, `Company`, `Price`, `TotalPlaces`, `AvailablePlaces`, `ShortDescription`, `LongDescription`) VALUES(5, '2020-03-10', 'Hochschule Ulm', 0, 30, 30, 'The world\'s most exciting motorsport complex', 'this is the long description');
INSERT INTO `event` (`Id`, `Date`, `Company`, `Price`, `TotalPlaces`, `AvailablePlaces`, `ShortDescription`, `LongDescription`) VALUES(6, '2020-05-10', 'Hochschule Ulm', 0, 40, 40, 'Best city', 'this is the long description');
INSERT INTO `event` (`Id`, `Date`, `Company`, `Price`, `TotalPlaces`, `AvailablePlaces`, `ShortDescription`, `LongDescription`) VALUES(7, '2020-02-10', 'Hochschule Ulm', 0, 30, 30, 'Excursion to Hogwarts!', 'this is the long description');
INSERT INTO `event` (`Id`, `Date`, `Company`, `Price`, `TotalPlaces`, `AvailablePlaces`, `ShortDescription`, `LongDescription`) VALUES(8, '2019-12-10', 'Transporeon', 0, 20, 20, 'Join us!', 'this is the long description');

-- --------------------------------------------------------

--
-- Структура таблицы `location`
--

CREATE TABLE `location` (
  `EventID` int(11) NOT NULL,
  `Xcoord` double DEFAULT NULL,
  `Ycoord` double DEFAULT NULL,
  `City` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `location`
--

INSERT INTO `location` (`EventID`, `Xcoord`, `Ycoord`, `City`) VALUES(1, 48.4192186, 9.9323005, 'Ulm');
INSERT INTO `location` (`EventID`, `Xcoord`, `Ycoord`, `City`) VALUES(2, 48.4214335, 9.939486, 'Ulm');
INSERT INTO `location` (`EventID`, `Xcoord`, `Ycoord`, `City`) VALUES(3, 48.4213288, 9.9329478, 'Ulm');
INSERT INTO `location` (`EventID`, `Xcoord`, `Ycoord`, `City`) VALUES(4, 48.7757936, 11.3978091, 'Ingolstadt');
INSERT INTO `location` (`EventID`, `Xcoord`, `Ycoord`, `City`) VALUES(5, 50.334098, 6.942662, 'Nurburg');
INSERT INTO `location` (`EventID`, `Xcoord`, `Ycoord`, `City`) VALUES(6, 52.354703, 4.8337496, 'Amsterdam');
INSERT INTO `location` (`EventID`, `Xcoord`, `Ycoord`, `City`) VALUES(7, 48.4232035, 9.4950177, 'Hogwarts');
INSERT INTO `location` (`EventID`, `Xcoord`, `Ycoord`, `City`) VALUES(8, 48.3990115, 9.9566127, 'Ulm');

-- --------------------------------------------------------

--
-- Структура таблицы `pictures`
--

CREATE TABLE `pictures` (
  `EventID` int(11) NOT NULL,
  `Logo` varchar(45) DEFAULT NULL,
  `Picture` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `pictures`
--

INSERT INTO `pictures` (`EventID`, `Logo`, `Picture`) VALUES(1, 'https://i.imgur.com/YWmWNTz.jpg', 'https://i.imgur.com/G8RRg7p.jpg');
INSERT INTO `pictures` (`EventID`, `Logo`, `Picture`) VALUES(2, 'https://i.imgur.com/YERA4Vd.jpg', 'https://i.imgur.com/gjt6DmF.jpg');
INSERT INTO `pictures` (`EventID`, `Logo`, `Picture`) VALUES(3, 'https://i.imgur.com/jz27GxF.jpg', 'https://i.imgur.com/THizcyH.jpg');
INSERT INTO `pictures` (`EventID`, `Logo`, `Picture`) VALUES(4, 'https://i.imgur.com/wlfI2FP.jpg', 'https://i.imgur.com/A2ZiTkt.jpg');
INSERT INTO `pictures` (`EventID`, `Logo`, `Picture`) VALUES(5, 'https://i.imgur.com/y02zvQU.png', 'https://i.imgur.com/Bhs5aVb.jpg');
INSERT INTO `pictures` (`EventID`, `Logo`, `Picture`) VALUES(6, 'https://i.imgur.com/y02zvQU.png', 'https://i.imgur.com/ZQBIuqp.jpg');
INSERT INTO `pictures` (`EventID`, `Logo`, `Picture`) VALUES(7, 'https://i.imgur.com/y02zvQU.png', 'https://i.imgur.com/9SIkbj6.jpg');
INSERT INTO `pictures` (`EventID`, `Logo`, `Picture`) VALUES(8, 'https://i.imgur.com/2Uv9bjk.png', 'https://i.imgur.com/KM4IVtW.jpg');

-- --------------------------------------------------------

--
-- Структура таблицы `transactions`
--

CREATE TABLE `transactions` (
  `Id` int(11) NOT NULL,
  `StudentID` int(11) NOT NULL,
  `Date` date NOT NULL,
  `Completed` int(11) DEFAULT NULL,
  `EventID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `transactions`
--

INSERT INTO `transactions` (`Id`, `StudentID`, `Date`, `Completed`, `EventID`) VALUES(1, 6, '2019-10-29', 0, 1);

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

CREATE TABLE `users` (
  `Id` int(11) NOT NULL,
  `Email` varchar(45) NOT NULL,
  `FirstName` varchar(45) NOT NULL,
  `LastName` varchar(45) NOT NULL,
  `AccessLevel` int(10) UNSIGNED ZEROFILL DEFAULT NULL,
  `CourseID` int(11) DEFAULT NULL,
  `Password` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(6, 'Bredesen@hs-ulm.de', 'Denise', 'Bredesen', 0000000000, 1, 'user6');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(7, 'Qualls@hs-ulm.de', 'Mark', 'Qualls', 0000000000, 1, 'user7');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(8, 'Giles@hs-ulm.de', 'Marie', 'Giles', 0000000000, 3, 'user8');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(9, 'Folta@hs-ulm.de', 'Jennifer', 'Folta', 0000000000, 2, 'user9');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(10, 'Baker@hs-ulm.de', 'Sharon', 'Baker', 0000000000, 4, 'user10');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(11, 'Chavez@hs-ulm.de', 'Peggie', 'Chavez', 0000000000, 3, 'user11');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(12, 'Craner@hs-ulm.de', 'Juana', 'Craner', 0000000000, 1, 'user12');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(13, 'Merrifield@hs-ulm.de', 'Jason', 'Merrifield', 0000000000, 2, 'user13');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(14, 'Holloway@hs-ulm.de', 'Paulette', 'Holloway', 0000000000, 2, 'user14');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(15, 'Browne@hs-ulm.de', 'Mohammed', 'Browne', 0000000000, 3, 'user15');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(16, 'Garcia@hs-ulm.de', 'Leo', 'Garcia', 0000000000, 1, 'user16');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(17, 'Pengelly@hs-ulm.de', 'Christopher', 'Pengelly', 0000000000, 3, 'user17');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(18, 'Bentley@hs-ulm.de', 'Jane', 'Bentley', 0000000000, 2, 'user18');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(19, 'Hopkins@hs-ulm.de', 'Charles', 'Hopkins', 0000000000, 4, 'user19');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(20, 'Wimmer@hs-ulm.de', 'Robert', 'Wimmer', 0000000000, 4, 'user20');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(21, 'Powers@hs-ulm.de', 'Danny', 'Powers', 0000000000, 2, 'user21');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(22, 'Stein@hs-ulm.de', 'Robert', 'Stein', 0000000000, 4, 'user22');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(23, 'Borghese@hs-ulm.de', 'Leonel', 'Borghese', 0000000000, 4, 'user23');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(24, 'Poremski@hs-ulm.de', 'Audrey', 'Poremski', 0000000000, 3, 'user24');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(25, 'Peeples@hs-ulm.de', 'Dale', 'Peeples', 0000000000, 4, 'user25');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(26, 'Santos@hs-ulm.de', 'Donald', 'Santos', 0000000000, 3, 'user26');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(27, 'Gipe@hs-ulm.de', 'Raymond', 'Gipe', 0000000000, 1, 'user27');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(28, 'Wilborn@hs-ulm.de', 'Josephine', 'Wilborn', 0000000000, 3, 'user28');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(29, 'Burkett@hs-ulm.de', 'Fred', 'Burkett', 0000000000, 1, 'user29');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(30, 'Kriner@hs-ulm.de', 'Clay', 'Kriner', 0000000000, 1, 'user30');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(31, 'Harding@hs-ulm.de', 'Lloyd', 'Harding', 0000000000, 3, 'user31');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(32, 'Palmer@hs-ulm.de', 'Shirley', 'Palmer', 0000000000, 3, 'user32');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(33, 'Crumrine@hs-ulm.de', 'Bobby', 'Crumrine', 0000000000, 3, 'user33');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(34, 'Hernandez@hs-ulm.de', 'Nicholas', 'Hernandez', 0000000000, 1, 'user34');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(35, 'Frazier@hs-ulm.de', 'James', 'Frazier', 0000000000, 4, 'user35');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(36, 'Roberts@hs-ulm.de', 'Linda', 'Roberts', 0000000000, 4, 'user36');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(37, 'King@hs-ulm.de', 'Jefferson', 'King', 0000000000, 2, 'user37');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(38, 'Lindsay@hs-ulm.de', 'Gerald', 'Lindsay', 0000000000, 3, 'user38');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(39, 'Anaya@hs-ulm.de', 'Robert', 'Anaya', 0000000000, 2, 'user39');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(40, 'Lewis@hs-ulm.de', 'Sylvia', 'Lewis', 0000000000, 4, 'user40');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(41, 'Tuck@hs-ulm.de', 'Shirley', 'Tuck', 0000000000, 1, 'user41');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(42, 'Takahashi@hs-ulm.de', 'Leigh', 'Takahashi', 0000000000, 1, 'user42');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(43, 'Sutter@hs-ulm.de', 'Thomas', 'Sutter', 0000000000, 3, 'user43');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(44, 'Amsterdam@hs-ulm.de', 'Matilda', 'Amsterdam', 0000000000, 2, 'user44');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(45, 'Cecil@hs-ulm.de', 'Charlene', 'Cecil', 0000000000, 4, 'user45');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(46, 'Nappi@hs-ulm.de', 'Billy', 'Nappi', 0000000000, 2, 'user46');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(47, 'Freeman@hs-ulm.de', 'Maria', 'Freeman', 0000000000, 2, 'user47');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(48, 'Bacich@hs-ulm.de', 'Bobby', 'Bacich', 0000000000, 4, 'user48');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(49, 'Rappold@hs-ulm.de', 'Carol', 'Rappold', 0000000000, 3, 'user49');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(50, 'williamsomething@hs-ulm.de', 'William', 'Cantwell', 0000000000, 1, 'user50');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(51, 'Johnston@hs-ulm.de', 'Jerry', 'Johnston', 0000000000, 2, 'user51');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(52, 'Wenrich@hs-ulm.de', 'Harold', 'Wenrich', 0000000000, 4, 'user52');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(53, 'Datson@hs-ulm.de', 'Richard', 'Datson', 0000000000, 1, 'user53');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(54, 'Smith@hs-ulm.de', 'Laura', 'Smith', 0000000000, 4, 'user54');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(55, 'Guillen@hs-ulm.de', 'Sue', 'Guillen', 0000000000, 3, 'user55');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(56, 'Turner@hs-ulm.de', 'Ian', 'Turner', 0000000000, 3, 'user56');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(57, 'Reed@hs-ulm.de', 'Karen', 'Reed', 0000000000, 1, 'user57');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(58, 'Rowell@hs-ulm.de', 'Elvis', 'Rowell', 0000000000, 3, 'user58');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(59, 'Emerson@hs-ulm.de', 'Leon', 'Emerson', 0000000000, 3, 'user59');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(60, 'Harju@hs-ulm.de', 'Debbie', 'Harju', 0000000000, 3, 'user60');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(61, 'Robbins@hs-ulm.de', 'Stanley', 'Robbins', 0000000000, 2, 'user61');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(62, 'Swiger@hs-ulm.de', 'Ann', 'Swiger', 0000000000, 1, 'user62');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(63, 'Armstrong@hs-ulm.de', 'Raymond', 'Armstrong', 0000000000, 4, 'user63');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(64, 'Reid@hs-ulm.de', 'Shirley', 'Reid', 0000000000, 3, 'user64');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(65, 'Bonham@hs-ulm.de', 'Reginald', 'Bonham', 0000000000, 3, 'user65');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(66, 'Stumbaugh@hs-ulm.de', 'Kathy', 'Stumbaugh', 0000000000, 3, 'user66');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(67, 'Alston@hs-ulm.de', 'Brandon', 'Alston', 0000000000, 3, 'user67');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(68, 'Appleton@hs-ulm.de', 'Claudette', 'Appleton', 0000000000, 1, 'user68');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(69, 'Simpson@hs-ulm.de', 'Kendra', 'Simpson', 0000000000, 2, 'user69');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(70, 'Newsome@hs-ulm.de', 'Lillian', 'Newsome', 0000000000, 3, 'user70');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(71, 'Rayborn@hs-ulm.de', 'Catherine', 'Rayborn', 0000000000, 2, 'user71');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(72, 'Ryan@hs-ulm.de', 'Andres', 'Ryan', 0000000000, 4, 'user72');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(73, 'Gonzalez@hs-ulm.de', 'Jose', 'Gonzalez', 0000000000, 1, 'user73');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(74, 'Griffin@hs-ulm.de', 'Reuben', 'Griffin', 0000000000, 4, 'user74');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(75, 'Pederson@hs-ulm.de', 'Alice', 'Pederson', 0000000000, 4, 'user75');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(76, 'Chi@hs-ulm.de', 'Johnny', 'Chi', 0000000000, 1, 'user76');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(77, 'Maynard@hs-ulm.de', 'Ashley', 'Maynard', 0000000000, 2, 'user77');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(78, 'George@hs-ulm.de', 'Jeffrey', 'George', 0000000000, 1, 'user78');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(79, 'Goldfeder@hs-ulm.de', 'Rose', 'Goldfeder', 0000000000, 2, 'user79');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(80, 'Rader@hs-ulm.de', 'Shelly', 'Rader', 0000000000, 1, 'user80');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(81, 'Iverson@hs-ulm.de', 'Francine', 'Iverson', 0000000000, 2, 'user81');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(82, 'Brown@hs-ulm.de', 'Joseph', 'Brown', 0000000000, 2, 'user82');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(83, 'Stechlinski@hs-ulm.de', 'Sarah', 'Stechlinski', 0000000000, 2, 'user83');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(84, 'Walker@hs-ulm.de', 'Mildred', 'Walker', 0000000000, 4, 'user84');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(85, 'Gleaves@hs-ulm.de', 'Jonathan', 'Gleaves', 0000000000, 3, 'user85');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(86, 'Monath@hs-ulm.de', 'William', 'Monath', 0000000000, 4, 'user86');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(87, 'Braswell@hs-ulm.de', 'Heidi', 'Braswell', 0000000000, 2, 'user87');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(88, 'Dunn@hs-ulm.de', 'Ramon', 'Dunn', 0000000000, 1, 'user88');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(89, 'Brannon@hs-ulm.de', 'Jackie', 'Brannon', 0000000000, 3, 'user89');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(90, 'Martin@hs-ulm.de', 'Paris', 'Martin', 0000000000, 4, 'user90');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(91, 'Wolford@hs-ulm.de', 'Catherine', 'Wolford', 0000000000, 2, 'user91');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(92, 'Howard@hs-ulm.de', 'Victor', 'Howard', 0000000000, 1, 'user92');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(93, 'Haralson@hs-ulm.de', 'Harry', 'Haralson', 0000000000, 3, 'user93');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(94, 'Cook@hs-ulm.de', 'Tammy', 'Cook', 0000000000, 3, 'user94');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(95, 'Silver@hs-ulm.de', 'Santos', 'Silver', 0000000000, 2, 'user95');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(96, 'Staples@hs-ulm.de', 'Terri', 'Staples', 0000000000, 3, 'user96');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(97, 'Mooney@hs-ulm.de', 'Mary', 'Mooney', 0000000000, 3, 'user97');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(98, 'Anthony@hs-ulm.de', 'Thomas', 'Anthony', 0000000000, 1, 'user98');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(99, 'Jones@hs-ulm.de', 'Arthur', 'Jones', 0000000000, 2, 'user99');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(100, 'Wasserman@hs-ulm.de', 'Lincoln', 'Wasserman', 0000000000, 1, 'user100');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(101, 'Clem@hs-ulm.de', 'Peggy', 'Clem', 0000000001, NULL, 'hahahalol');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(102, 'Thomas@hs-ulm.de', 'Anna', 'Thomas', 0000000001, NULL, 'admin2');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(103, 'Pena@hs-ulm.de', 'Regina', 'Pena', 0000000001, NULL, 'admin3');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(104, 'Layton@hs-ulm.de', 'Debra', 'Layton', 0000000001, NULL, 'admin4');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(105, 'Kama@hs-ulm.de', 'Christie', 'Kama', 0000000001, NULL, 'admin5');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(106, 'thenotoriuousmma@hs-ulm.de', 'Connor', 'Mcgregor', 0000000000, 1, 'hahaha');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(107, 'miketyson@hs-ulm.de', 'Mike', 'Tyson', 0000000000, 1, 'miketyson');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(108, 'angelamerkel@hs-ulm.de', 'Angela', 'Merkel', 0000000000, 1, '123456');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(109, 'kebabnurmagomedov@hs-ulm.de', 'Kebab', 'Nurmagomedov', 0000000000, 4, 'kebab123');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(110, 'marmiss@mail.hs-ulm.de', 'Aleksejs', 'Marmiss', 0000000000, 1, '6qGIBAbM62');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(111, 'gicumironica@mail.hs-ulm.de', 'Giku', 'Mironica', 0000000000, 1, 'hahaha');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(112, 'basit@hs-ulm.de', 'Abdul', 'Basit', 0000000000, 1, 'basit123');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(113, 'ryazev@mail.hs-ulm.de', 'Hidayat', 'Rzayev', 0000000000, 1, 'sikiminbashi');
INSERT INTO `users` (`Id`, `Email`, `FirstName`, `LastName`, `AccessLevel`, `CourseID`, `Password`) VALUES(114, 'barackobama@mail.hs-ulm.de', 'Barack', 'Obama', 0000000000, 1, 'hohoho');

-- --------------------------------------------------------

--
-- Структура таблицы `wishlist`
--

CREATE TABLE `wishlist` (
  `StudentID` int(11) NOT NULL,
  `EventID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `wishlist`
--

INSERT INTO `wishlist` (`StudentID`, `EventID`) VALUES(6, 1);
INSERT INTO `wishlist` (`StudentID`, `EventID`) VALUES(6, 2);

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `courses`
--
ALTER TABLE `courses`
  ADD PRIMARY KEY (`Id`);

--
-- Индексы таблицы `event`
--
ALTER TABLE `event`
  ADD PRIMARY KEY (`Id`,`Date`);

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
  ADD KEY `StudentID_idx` (`StudentID`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `courses`
--
ALTER TABLE `courses`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT для таблицы `event`
--
ALTER TABLE `event`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT для таблицы `transactions`
--
ALTER TABLE `transactions`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT для таблицы `users`
--
ALTER TABLE `users`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=115;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `location`
--
ALTER TABLE `location`
  ADD CONSTRAINT `EventID3` FOREIGN KEY (`EventID`) REFERENCES `event` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ограничения внешнего ключа таблицы `pictures`
--
ALTER TABLE `pictures`
  ADD CONSTRAINT `EventID2` FOREIGN KEY (`EventID`) REFERENCES `event` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ограничения внешнего ключа таблицы `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `EventID4` FOREIGN KEY (`EventID`) REFERENCES `event` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `StudentID2` FOREIGN KEY (`StudentID`) REFERENCES `users` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ограничения внешнего ключа таблицы `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `Id` FOREIGN KEY (`CourseID`) REFERENCES `courses` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ограничения внешнего ключа таблицы `wishlist`
--
ALTER TABLE `wishlist`
  ADD CONSTRAINT `EventID` FOREIGN KEY (`EventID`) REFERENCES `event` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `StudentID` FOREIGN KEY (`StudentID`) REFERENCES `users` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
