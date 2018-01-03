# --- !Ups
CREATE TABLE user (
  userID    VARCHAR(255) NOT NULL PRIMARY KEY,
  frstName  VARCHAR(255),
  lastName  VARCHAR(255),
  fullName  VARCHAR(255),
  email     VARCHAR(255),
  avatarURL VARCHAR(255)
);
CREATE TABLE logininfo (
  id          BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  providerID  VARCHAR(255)          NOT NULL,
  providerKey VARCHAR(255)          NOT NULL
);
CREATE TABLE userlogininfo (
  userID      VARCHAR(255) NOT NULL,
  loginInfoId BIGINT       NOT NULL
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

