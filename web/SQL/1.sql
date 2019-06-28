DROP TABLE IF EXISTS `Credit`;
DROP TABLE IF EXISTS `Order`;
DROP TABLE IF EXISTS `Financing`;
DROP TABLE IF EXISTS `Product`;
DROP TABLE IF EXISTS `User`;
DROP TABLE IF EXISTS `AccessRule`;
DROP TABLE IF EXISTS `Transfer`;

-- 基本用户信息
CREATE TABLE `User`(
	`Id` bigint NOT NULL auto_increment PRIMARY KEY,
    `Email` varchar(255) NOT NULL,
    `Name` varchar(255) NOT NULL,
    `PasswordHash` varchar(255) NOT NULL,
    `Role` char(16) NOT NULL,
    `Additional` text NULL,
    `Frozen` tinyint NOT NULL,
    `AutoPass` tinyint NOT NULL,
    `LastTransfer` long NOT NULL
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 产品
CREATE TABLE `Product`(
    `Id` bigint NOT NULL auto_increment PRIMARY KEY,
    `Name` varchar(255) NOT NULL,
    `Days` int NOT NULL,
    `Rate` decimal(22,20) NOT NULL,
    `Additional` text NULL
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 融资申请
CREATE TABLE `Financing`(
    `Id` bigint NOT NULL auto_increment PRIMARY KEY,
    `Sid` bigint NOT NULL,
    `Cid` bigint NOT NULL,
    `Mid` bigint NOT NULL,
    `Money` decimal(20,2) NOT NULL,
    `Days` int NOT NULL,
    `Rate` decimal(22,20) NOT NULL,
    `CreateTime` datetime NOT NULL,
    `PayTime` datetime NULL,
    `RepayTime` datetime NULL,
    `Status` tinyint NOT NULL,
    CONSTRAINT `FK_Financing_User_Sid` FOREIGN KEY (`Sid`) REFERENCES `User` (`Id`),
    CONSTRAINT `FK_Financing_User_Cid` FOREIGN KEY (`Cid`) REFERENCES `User` (`Id`),
    CONSTRAINT `FK_Financing_User_Mid` FOREIGN KEY (`Mid`) REFERENCES `User` (`Id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 订单
CREATE TABLE `Order`(
    `Id` bigint NOT NULL auto_increment PRIMARY KEY,
    `Sid` bigint NOT NULL,
    `Cid` bigint NOT NULL,
    `Number` bigint NOT NULL,
    `Money` decimal(20,2) NOT NULL,
    `CreateTime` datetime NOT NULL,
    `Days` int NOT NULL,
    `Status` tinyint NOT NULL,
    CONSTRAINT `FK_Order_User_Sid` FOREIGN KEY (`Sid`) REFERENCES `User` (`Id`),
    CONSTRAINT `FK_Order_User_Cid` FOREIGN KEY (`Cid`) REFERENCES `User` (`Id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 准入规则
CREATE TABLE `AccessRule`(
    `Id` bigint NOT NULL auto_increment PRIMARY KEY,
    `Content` text NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 供应商授信
CREATE TABLE `Credit` (
    `Sid` bigint NOT NULL PRIMARY KEY,
    `Rank` varchar(255) NULL,
    `Applied` decimal(20,2) NULL,
    `Approved` decimal(20,2) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 转账记录
CREATE TABLE `Transfer`(
    `Id` bigint NOT NULL auto_increment PRIMARY KEY,
    `Dst` bigint NOT NULL,
    `Src` bigint NOT NULL,
    CONSTRAINT `FK_Transfer_User_Dst` FOREIGN KEY (`Dst`) REFERENCES `User` (`Id`),
    CONSTRAINT `FK_Transfer_User_Src` FOREIGN KEY (`Src`) REFERENCES `User` (`Id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
