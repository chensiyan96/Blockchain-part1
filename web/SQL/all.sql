-- CREATE SCHEMA `blockchain` DEFAULT CHARACTER SET utf8 ;

-- 基本用户信息
CREATE TABLE `Users` (
	  `Id` INT NOT NULL auto_increment,
    `Email` varchar(256) NOT NULL,
    `NormalizedEmail` varchar(256) NOT NULL,
    `PasswordHash` longtext NOT NULL,
    `CompanyName` longtext NULL,
    `Profile`longtext NULL,
    `Role` varchar(256) NULL,
	PRIMARY KEY (`Id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- --- 所有角色
--  CREATE TABLE `Roles` (
-- 	`Id` INT NOT NULL auto_increment,
--     `Name` varchar(256) NOT NULL,
--     `NormalizedUserName` varchar(256)NOT NULL,
-- 	PRIMARY KEY (`Id`)
--  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
--
-- --- 用户角色
--  CREATE TABLE `UserRoles` (
--     `UserId` INT NOT NULL,
--     `RoleId` INT NOT NULL,
--     CONSTRAINT `PK_UserRoles` PRIMARY KEY (`UserId`, `RoleId`),
--     CONSTRAINT `FK_UserRoles_Roles_RoleId` FOREIGN KEY (`RoleId`) REFERENCES `Roles` (`Id`) ON DELETE CASCADE,
--     CONSTRAINT `FK_UserRoles_Users_UserId` FOREIGN KEY (`UserId`) REFERENCES `Users` (`Id`) ON DELETE CASCADE
-- )ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
    `Id` INT NOT NULL,
    `Money` decimal(20,2) NOT NULL,
    `CreateTime` DATETIME NULL,
    `Deadline` DATETIME NULL,
    `PartyA` INT NULL,
    `PartyB` INT NULL,
    `Status` INT NULL,
    PRIMARY KEY (`Id`),
    CONSTRAINT `FK_Credits_Users_PartyA` FOREIGN KEY (`PartyA`) REFERENCES `Users` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Credits_Users_PartyB` FOREIGN KEY (`PartyB`) REFERENCES `Users` (`Id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 转账记录
CREATE TABLE `Payments` (
    `Id` INT NOT NULL,
    `CreateTime` DATETIME NULL,
    `Money` decimal(20,2) NOT NULL,
    `PartyA` INT NULL,
    `PartyB` INT NULL,
    PRIMARY KEY (`Id`),
    CONSTRAINT `FK_Payments_Users_PartyA` FOREIGN KEY (`PartyA`) REFERENCES `Users` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Payments_Users_PartyB` FOREIGN KEY (`PartyB`) REFERENCES `Users` (`Id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 合同
CREATE TABLE `Agreements` (
    `Id` INT NOT NULL,
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
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 贷款抵押
CREATE TABLE `Mortgages`(
    `Id` INT NOT NULL,
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

)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 融资 其实和合同没什么区别？
CREATE TABLE `Financing`(
    `Id` INT NOT NULL,
    `Terms` longtext NULL,
    `CreateTime` DATETIME NULL,
    `PartyA` INT NULL,
    `PartyB` INT NULL,
    `Status` varchar(256) NULL,
    `Mid` INT NOT NULL,
    `Aid` INT NOT NULL,
    PRIMARY KEY (`Id`),
    CONSTRAINT `FK_Financing_Users_PartyA` FOREIGN KEY (`PartyA`) REFERENCES `Users` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Financing_Users_PartyB` FOREIGN KEY (`PartyB`) REFERENCES `Users` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Financing_Mortgages_CreditId` FOREIGN KEY (`MId`) REFERENCES `Mortgages` (`Id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Message`(
    `Id` INT NOT NULL,
    `Msg` longtext NULL,
    `CreateTime` DATETIME NULL,
    `PartyA` INT NULL,
    `PartyB` INT NULL,
    `Status` INT NULL,
    PRIMARY KEY (`Id`),
    CONSTRAINT `FK_Message_Users_PartyA` FOREIGN KEY (`PartyA`) REFERENCES `Users` (`Id`) ON DELETE CASCADE,
    CONSTRAINT `FK_Message_Users_PartyB` FOREIGN KEY (`PartyB`) REFERENCES `Users` (`Id`) ON DELETE CASCADE,
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
