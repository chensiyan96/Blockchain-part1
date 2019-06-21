-- CREATE SCHEMA `blockchain` DEFAULT CHARACTER SET utf8 ;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS financing;
DROP TABLE IF EXISTS mortgages;
DROP TABLE IF EXISTS agreements;
DROP TABLE IF EXISTS credits;
DROP TABLE IF EXISTS suppliers;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS useraccounts;


/*
-- 用户账户余额
CREATE TABLE `UserAccounts` (
    `UserId` INT NOT NULL,
    `Money` decimal(20,2) NOT NULL,
    CONSTRAINT `PK_UserAccounts` PRIMARY KEY (`UserId`),
    CONSTRAINT `FK_UserAccounts_Users_UserId` FOREIGN KEY (`UserId`) REFERENCES `Users` (`Id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 供应商评级
CREATE TABLE `Suppliers` (
    `UserId` INT NOT NULL,
    `Rating` INT NULL,
    `LimitedMoney` decimal(20,2) NULL,
    `Rule` longtext NULL,
    CONSTRAINT `PK_Suppliers` PRIMARY KEY (`UserId`),
    CONSTRAINT `FK_Suppliers_Users_UserId` FOREIGN KEY (`UserId`) REFERENCES `Users` (`Id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 贷款、借条
CREATE TABLE `Credits` (
    `Id` INT NOT NULL auto_increment,
    `Money` decimal(20,2) NOT NULL,
    `CreateTime` DATETIME NULL,
    `Deadline` DATETIME NULL,
    `PartyA` INT NULL,
    `PartyB` INT NULL,
    `Status` INT NULL,
    PRIMARY KEY (`Id`),
    CONSTRAINT `FK_Credits_Users_PartyA` FOREIGN KEY (`PartyA`) REFERENCES `Users` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Credits_Users_PartyB` FOREIGN KEY (`PartyB`) REFERENCES `Users` (`Id`) ON DELETE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 转账记录
CREATE TABLE `Payments` (
    `Id` INT NOT NULL auto_increment,
    `CreateTime` DATETIME NULL,
    `Money` decimal(20,2) NOT NULL,
    `PartyA` INT NULL,
    `PartyB` INT NULL,
    PRIMARY KEY (`Id`),
    CONSTRAINT `FK_Payments_Users_PartyA` FOREIGN KEY (`PartyA`) REFERENCES `Users` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Payments_Users_PartyB` FOREIGN KEY (`PartyB`) REFERENCES `Users` (`Id`) ON DELETE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 合同
CREATE TABLE `Agreements` (
    `Id` INT NOT NULL auto_increment,
    `Terms` longtext NULL,
    `CreateTime` DATETIME NULL,
    `PartyA` INT NULL,
    `PartyB` INT NULL,
    `Status` varchar(256) NULL,
    `CreditId` int NULL,
    PRIMARY KEY (`Id`),
    CONSTRAINT `FK_Agreements_Users_PartyA` FOREIGN KEY (`PartyA`) REFERENCES `Users` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Agreements_Users_PartyB` FOREIGN KEY (`PartyB`) REFERENCES `Users` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Agreements_Credits_CreditId` FOREIGN KEY (`CreditId`) REFERENCES `Credits` (`Id`) ON DELETE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 贷款抵押
CREATE TABLE `Mortgages`(
    `Id` INT NOT NULL auto_increment,
    `Cid` INT NOT NULL,
    `Money` decimal(20,2) NOT NULL,
    `CreateTime` DATETIME NULL,
    `PartyA` INT NULL,
    `PartyB` INT NULL,
    `Status` INT NULL,
    PRIMARY KEY (`Id`),
    CONSTRAINT `FK_Mortgages_Credits_Cid` FOREIGN KEY (`Cid`) REFERENCES `Credits` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Mortgages_Users_PartyA` FOREIGN KEY (`PartyA`) REFERENCES `Users` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Mortgages_Users_PartyB` FOREIGN KEY (`PartyB`) REFERENCES `Users` (`Id`) ON DELETE CASCADE

)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `Message`(
    `Id` INT NOT NULL auto_increment,
    `Msg` longtext NULL,
    `CreateTime` DATETIME NULL,
    `PartyA` INT NULL,
    `PartyB` INT NULL,
    `Status` INT NULL,
    PRIMARY KEY (`Id`),
    CONSTRAINT `FK_Message_Users_PartyA` FOREIGN KEY (`PartyA`) REFERENCES `Users` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Message_Users_PartyB` FOREIGN KEY (`PartyB`) REFERENCES `Users` (`Id`) ON DELETE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
*/
