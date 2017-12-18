# --- !Ups
CREATE TABLE project(
  id BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR(200) NOT NULL,
  status ENUM('ready','set','go') NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE task(
  id BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR(200) NOT Null,
  due DATETIME NOT NULL,
  projectId BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`projectId`) REFERENCES project(`id`) ON DELETE CASCADE
);

# --- !Downs
DROP TABLE task;
DROP TABLE project;
