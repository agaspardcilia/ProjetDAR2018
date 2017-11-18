DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `idaccount` int(11) NOT NULL AUTO_INCREMENT,
  `iduser` int(11) NOT NULL,
  `balance` int(11) DEFAULT NULL,
  PRIMARY KEY (`idaccount`),
  UNIQUE KEY `iduser_UNIQUE` (`iduser`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `cities`;
CREATE TABLE `cities` (
  `idcity` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(45) DEFAULT NULL,
  `owmid` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idcity`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
DROP TABLE IF EXISTS `events`;
CREATE TABLE `events` (
  `idevent` int(11) NOT NULL AUTO_INCREMENT,
  `idcity` int(11) NOT NULL,
  `eventtype` int(11) NOT NULL,
  `date` varchar(13) NOT NULL,
  `lastmodif` varchar(13) NOT NULL,
  `status` varchar(13) NOT NULL DEFAULT 'wait',
  `odd` float NOT NULL DEFAULT '1',
  PRIMARY KEY (`idevent`)
) ENGINE=InnoDB AUTO_INCREMENT=118 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `eventtypes`;
CREATE TABLE `eventtypes` (
  `ideventtype` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(45) NOT NULL,
  PRIMARY KEY (`ideventtype`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `keys`;
CREATE TABLE `keys` (
  `token` varchar(32) NOT NULL,
  `iduser` int(11) NOT NULL,
  `lastUsed` datetime NOT NULL,
  PRIMARY KEY (`token`),
  UNIQUE KEY `token_UNIQUE` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `idusers` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(64) NOT NULL,
  `email` varchar(45) NOT NULL,
  `salt` varchar(45) NOT NULL,
  PRIMARY KEY (`idusers`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
