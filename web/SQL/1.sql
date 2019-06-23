DROP TABLE IF EXISTS `MoneyGiverProfile`;
DROP TABLE IF EXISTS `SupplierProfile`;
DROP TABLE IF EXISTS `CoreBusinessProfile`;
DROP TABLE IF EXISTS `Order`;
DROP TABLE IF EXISTS `Financing`;
DROP TABLE IF EXISTS `Product`;
DROP TABLE IF EXISTS `User`;

-- 基本用户信息
CREATE TABLE `User`(
	`Id` bigint NOT NULL auto_increment PRIMARY KEY,
    `Email` varchar(255) NOT NULL,
    `Name` varchar(255) NULL,
    `PasswordHash` varchar(255) NOT NULL,
    `Role` char(16) NOT NULL,
    `Additional` longtext NULL
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `Product`(
    `Id` bigint NOT NULL auto_increment PRIMARY KEY

)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `Financing`(
    `Id` bigint NOT NULL auto_increment PRIMARY KEY ,
    `Sid` bigint NOT NULL,
    `Cid` bigint NOT NULL,
    `Mid` bigint NOT NULL,
    `Pid` bigint NOT NULL,
    `Money` decimal(20,2) NOT NULL,
    `CreateTime` datetime NOT NULL,
    `Status` tinyint NOT NULL,
    CONSTRAINT `FK_Financing_User_Sid` FOREIGN KEY (`Sid`) REFERENCES `User` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Financing_User_Cid` FOREIGN KEY (`Cid`) REFERENCES `User` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Financing_User_Mid` FOREIGN KEY (`Mid`) REFERENCES `User` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Financing_Product_Pid` FOREIGN KEY (`Pid`) REFERENCES `Product` (`Id`) ON DELETE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `Order`(
    `Id` bigint NOT NULL auto_increment PRIMARY KEY ,
    `Sid` bigint NOT NULL,
    `Cid` bigint NOT NULL,
    `Money` decimal(20,2) NOT NULL,
    `CreateTime` datetime NOT NULL,
    `EndTime` datetime NOT NULL,
    `Status` tinyint NOT NULL,
    CONSTRAINT `FK_Order_User_Sid` FOREIGN KEY (`Sid`) REFERENCES `User` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Order_User_Cid` FOREIGN KEY (`Cid`) REFERENCES `User` (`Id`) ON DELETE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*
CREATE TABLE `CoreBusinessProfile` (
    `Uid` bigint NOT NULL,
    CONSTRAINT `FK_CoreBusinessProfile_User` FOREIGN KEY (`Uid`) REFERENCES `User` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `SupplierProfile` (
    `Uid` bigint NOT NULL,
    CONSTRAINT `FK_SupplierProfile_User` FOREIGN KEY (`Uid`) REFERENCES `User` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `MoneyGiverProfile` (
    `Uid` bigint NOT NULL,
    CONSTRAINT `FK_MoneyGiverProfile_User` FOREIGN KEY (`Uid`) REFERENCES `User` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
*/
