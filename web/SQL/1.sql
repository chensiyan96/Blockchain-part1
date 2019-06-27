DROP TABLE IF EXISTS `MoneyGiverProfile`;
DROP TABLE IF EXISTS `CoreBusinessProfile`;
DROP TABLE IF EXISTS `SupplierProfile`;
DROP TABLE IF EXISTS `Order`;
DROP TABLE IF EXISTS `Financing`;
DROP TABLE IF EXISTS `Product`;
DROP TABLE IF EXISTS `User`;
DROP TABLE IF EXISTS `AccessRule`;

-- 基本用户信息
CREATE TABLE `User`(
	`Id` bigint NOT NULL auto_increment PRIMARY KEY,
    `Email` varchar(255) NOT NULL,
    `Name` varchar(255) NULL,
    `PasswordHash` varchar(255) NOT NULL,
    `Role` char(16) NOT NULL,
    `Additional` text NULL,
    `Frozen` tinyint NOT NULL,
    `AutoPass` tinyint NOT NULL
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
    `Id` bigint NOT NULL auto_increment PRIMARY KEY ,
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
    `Id` bigint NOT NULL auto_increment PRIMARY KEY ,
    `Sid` bigint NOT NULL,
    `Cid` bigint NOT NULL,
    `Money` decimal(20,2) NOT NULL,
    `CreateTime` datetime NOT NULL,
    `EndTime` datetime NULL,
    `Status` tinyint NOT NULL,
    CONSTRAINT `FK_Order_User_Sid` FOREIGN KEY (`Sid`) REFERENCES `User` (`Id`),
    CONSTRAINT `FK_Order_User_Cid` FOREIGN KEY (`Cid`) REFERENCES `User` (`Id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 准入规则
CREATE TABLE `AccessRule`(
    `Id` bigint NOT NULL PRIMARY KEY,
    `Content` longtext NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
CREATE TABLE `SupplierProfile` (
    `Uid` bigint NOT NULL,
    CONSTRAINT `FK_SupplierProfile_User` FOREIGN KEY (`Uid`) REFERENCES `User` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `CoreBusinessProfile` (
    `Uid` bigint NOT NULL,
    CONSTRAINT `FK_CoreBusinessProfile_User` FOREIGN KEY (`Uid`) REFERENCES `User` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `MoneyGiverProfile` (
    `Uid` bigint NOT NULL,
    CONSTRAINT `FK_MoneyGiverProfile_User` FOREIGN KEY (`Uid`) REFERENCES `User` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
*/
