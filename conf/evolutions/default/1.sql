# --- !Ups
CREATE TABLE user (
  userID    VARCHAR(255)                      NOT NULL PRIMARY KEY,
  firstName VARCHAR(255),
  lastName  VARCHAR(255),
  fullName  VARCHAR(255),
  email     VARCHAR(255),
  avatarURL VARCHAR(255),
  wechat    VARCHAR(255),
  type      ENUM ('super', 'admin', 'normal') NOT NULL,
  agreement BOOLEAN                           NOT NULL,
  activate  BOOLEAN                           NOT NULL,
  createAt  TIMESTAMP                         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE logininfo (
  id          BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  providerID  VARCHAR(255)          NOT NULL,
  providerKey VARCHAR(255)          NOT NULL
);
CREATE TABLE userlogininfo (
  userID      VARCHAR(255) NOT NULL,
  loginInfoId BIGINT       NOT NULL,
  `timeStamp` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE passwordinfo (
  hasher      VARCHAR(255) NOT NULL,
  password    VARCHAR(255) NOT NULL,
  salt        VARCHAR(255),
  loginInfoId BIGINT       NOT NULL
);
CREATE TABLE oauth1info (
  id          BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  token       VARCHAR(255)          NOT NULL,
  secret      VARCHAR(255)          NOT NULL,
  loginInfoId BIGINT                NOT NULL
);
CREATE TABLE oauth2info (
  id           BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  accesstoken  VARCHAR(255)          NOT NULL,
  tokentype    VARCHAR(255),
  expiresin    INTEGER,
  refreshtoken VARCHAR(255),
  logininfoid  BIGINT                NOT NULL
);
CREATE TABLE openidinfo (
  id          VARCHAR(255) NOT NULL PRIMARY KEY,
  logininfoid BIGINT       NOT NULL
);
CREATE TABLE openidattributes (
  id    VARCHAR(255) NOT NULL,
  `key` VARCHAR(255) NOT NULL,
  value VARCHAR(255) NOT NULL
);

CREATE TABLE project (
  id     BIGINT AUTO_INCREMENT       NOT NULL,
  name   VARCHAR(200)                NOT NULL,
  status ENUM ('ready', 'set', 'go') NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE task (
  id        BIGINT AUTO_INCREMENT NOT NULL,
  name      VARCHAR(200)          NOT NULL,
  due       DATETIME              NOT NULL,
  projectId BIGINT                NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`projectId`) REFERENCES project (`id`)
    ON DELETE CASCADE
);

CREATE TABLE `box` (
  `box_id`   INT(11)     NOT NULL AUTO_INCREMENT,
  `content`  TEXT        NOT NULL,
  `number`   TEXT        NOT NULL,
  `weight`   VARCHAR(32) NOT NULL DEFAULT '0'
  COMMENT 'serial number\n',
  `company`  TEXT        NOT NULL
  COMMENT 'delivery company',
  `name`     VARCHAR(32) NOT NULL
  COMMENT 'the name on the box\n',
  `user_id`  INT(11)     NOT NULL,
  `username` VARCHAR(32) NOT NULL,
  `order_id` INT(11)     NOT NULL,
  `createAt` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status`   INT(11)     NOT NULL DEFAULT '1',
  `message`  TEXT        NOT NULL,
  `group_id` INT(11)     NOT NULL,
  PRIMARY KEY (`box_id`)
);

CREATE TABLE `group` (
  `group_id` INT(11)     AUTO_INCREMENT NOT NULL,
  `type`     INT(11)     NOT NULL DEFAULT '1',
  `people`   INT(11)     NOT NULL,
  `box`      INT(11)     NOT NULL,
  `street`   TEXT        NOT NULL,
  `block`    TEXT        NOT NULL,
  `room`     TEXT        NOT NULL,
  `postal`   INT(11)     NOT NULL,
  `phone`    INT(11)     NOT NULL,
  `message`  TEXT        NOT NULL,
  `user_id`  INT(11)     NOT NULL,
  `username` VARCHAR(32) NOT NULL,
  `kh`       INT(11)     NOT NULL DEFAULT '1',
  `createAt` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status`   INT(11)     NOT NULL DEFAULT '1',
  `number`   VARCHAR(32)          DEFAULT '0',
  `ready`    INT(11)              DEFAULT '0',
  `weight`   VARCHAR(32)          DEFAULT '0',
  `price`    VARCHAR(32)          DEFAULT '0',
  `info`     TEXT,
  `date`     VARCHAR(32)          DEFAULT NULL,
  PRIMARY KEY (`group_id`)
);

CREATE TABLE `order` (
  `order_id` INT(11)     NOT NULL AUTO_INCREMENT,
  `group_id` INT(11)     NOT NULL,
  `user_id`  INT(11)     NOT NULL,
  `username` VARCHAR(32) NOT NULL,
  `price`    VARCHAR(32) NOT NULL DEFAULT '0',
  `type`     INT(11)     NOT NULL DEFAULT '1',
  `status`   INT(11)     NOT NULL DEFAULT '1',
  `createAt` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `weight`   VARCHAR(32)          DEFAULT '0',
  `bill`     INT(11)              DEFAULT '0',
  `paid`     INT(11)              DEFAULT '0',
  PRIMARY KEY (`order_id`)
);

# --- !Downs"
DROP TABLE openidattributes;
DROP TABLE openidinfo;
DROP TABLE oauth2info;
DROP TABLE oauth1info;
DROP TABLE passwordinfo;
DROP TABLE userlogininfo;
DROP TABLE logininfo;
DROP TABLE user;
DROP TABLE task;
DROP TABLE project;
DROP TABLE box;
DROP TABLE group;
DROP TABLE order;

