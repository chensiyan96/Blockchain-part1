DROP TABLE IF EXISTS `MoneyGiverProfile`;
DROP TABLE IF EXISTS `SupplierProfile`;
DROP TABLE IF EXISTS `CoreBusinessProfile`;
DROP TABLE IF EXISTS `Financing`;
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

CREATE TABLE `Financing`(
    `Id` bigint NOT NULL auto_increment PRIMARY KEY ,
    `Sid` bigint NOT NULL,
    `Cid` bigint NOT NULL,
    `Mid` bigint NOT NULL,
    `CreateTime` date NOT NULL,
    `State` tinyint NOT NULL,
    CONSTRAINT `FK_Financing_Users_Sid` FOREIGN KEY (`Sid`) REFERENCES `User` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Financing_Users_Cid` FOREIGN KEY (`Cid`) REFERENCES `User` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Financing_Users_Mid` FOREIGN KEY (`Mid`) REFERENCES `User` (`Id`) ON DELETE CASCADE
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
commit;