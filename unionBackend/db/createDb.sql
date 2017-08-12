
drop database if exists union_manage;

create database union_manage;
 
use union_manage;

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `no` varchar(50),
  `realname` varchar(50),
  `is_root` int DEFAULT 0,
  `belongs` varchar(50) not null,
  `token` varchar(50),
  `password` varchar(50),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO user (no, is_root, realname, belongs, password)
VALUES  ('test', 1, '管理员', '马克思主义分工会', '1'),
        ('2012123123', 0, '张三', '马克思主义分工会', '2'),
        ('2012456456', 0, '李四', '计算机系分工会', '3'),
        ('2012011284', 0, '章彦恺', '计算机系分工会', '1'),
        ('2012011270', 0, '滕爽', '计算机系分工会', '1');

CREATE TABLE `event` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50),
  `location` varchar(50),
  `start_date` DATETIME,
  `end_date` DATETIME,
  `detail` text,
  `comment` text,
  `department` varchar(50),
  `req_qrcode` boolean DEFAULT 0,
  `req_photo` boolean DEFAULT 0,
  `req_report` boolean DEFAULT 0,
  `activity` boolean DEFAULT 0,
  `event_qrcode` boolean DEFAULT 0,
  `file` boolean DEFAULT 0,
  `chat` boolean DEFAULT 0,
  `txt` boolean DEFAULT 0,
  `recieve` boolean DEFAULT 0,
  `feedback` boolean DEFAULT 0,
  `attachment` int DEFAULT -1,
  `creator` int NOT NULL,
  `creation_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `status` int DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
INSERT INTO event (name, location, start_date, end_date, detail, comment, req_qrcode,req_report,req_photo, status, department, creator, activity, feedback, chat, txt, file, recieve)
VALUES ('testEvent1', 'testLocation', '2016-1-1 23:22:11', '2016-1-2 23:22:11', 'testDetail', 'testComment',0,0,1,0, 'testDepartment', 1, 1, 0, 1, 1, 0, 0),
	 ('testEvent2', 'testLocation', '2016-1-1 23:22:11', '2016-1-2 23:22:11', 'testDetail', 'testComment',0,1,0,0, 'testDepartment', 1, 1, 1, 0, 1, 0, 0),
	 ('testEvent3', 'testLocation', '2016-1-1 23:22:11', '2016-1-2 23:22:11', 'testDetail', 'testComment',1,1,1,0, 'testDepartment', 1, 1, 1, 1, 1, 1, 1);
*/
	 
CREATE TABLE `activity` (
  `id` int NOT NULL AUTO_INCREMENT,
  `event_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `is_reply` boolean DEFAULT 0,
  `is_reply_time` DATETIME,
  `reply_file` int,
  `reply_file_time` DATETIME,
  `reply_content` TEXT,
  `reply_content_time` DATETIME,
  `photo_file` int,
  `photo_file_time` DATETIME,
  `sign_number` int,
  `activity_created` boolean DEFAULT 0,
  `report_file` int,
  `report_file_time` DATETIME,
  `name` varchar(50),
  `location` varchar(50),
  `tim` DATETIME,
  `detail` TEXT,
  `comment` TEXT,
  `belongs` varchar(50) not null,
  `creation_time` DATETIME,
  `status` int DEFAULT 0,
  `attachment` int, 
  `permission` int,
  PRIMARY KEY (`id`),
  FOREIGN KEY(event_id) REFERENCES event(id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
INSERT INTO activity (event_id, user_id,  belongs, activity_created, name)
              VALUES (1, 4,       '马克思主义分工会', 0, '马克思主义工会五·四青年节活动'),
                     (2, 4,       '马克思主义分工会', 0, '马克思主义工会五·四青年节活动'),
                     (3, 4,       '马克思主义分工会', 0, '马克思主义工会五·四青年节活动');
*/
					 
CREATE TABLE `activity_auth` (
  `id` int NOT NULL AUTO_INCREMENT,
  `activity_id` INT NOT NULL,
  `authority_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY(activity_id) REFERENCES activity(id) ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY(authority_id) REFERENCES user(id) ON UPDATE CASCADE ON DELETE RESTRICT,
  UNIQUE KEY `unique_authority` (`activity_id`,`authority_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*
INSERT INTO activity_auth (activity_id,  authority_id)
              VALUES (1,        4),
                     (2,        4),
                     (3,        4);
*/
CREATE TABLE `participant` (
  `id` int NOT NULL AUTO_INCREMENT,
  `activity_id` INT,
  `event_id` INT,
  `participant_id` INT NOT NULL,
  `checkin_stat` int,
  PRIMARY KEY (`id`),
  FOREIGN KEY(participant_id) REFERENCES user(id) ON UPDATE CASCADE ON DELETE RESTRICT,
  UNIQUE KEY `unique_participant` (`activity_id`,`participant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*
INSERT INTO participant (activity_id, participant_id, checkin_stat)
VALUES (1, 1, 'no show');
*/
CREATE TABLE `message` (
  `id` int NOT NULL AUTO_INCREMENT,
  `event_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `content` text,
  FOREIGN KEY(event_id) REFERENCES event(id) ON UPDATE CASCADE ON DELETE RESTRICT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `file` (
  `id` int NOT NULL AUTO_INCREMENT,
  `filename` varchar(256),
  `url` varchar(256),
  `uploader` int,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `target` (
  `id` int NOT NULL AUTO_INCREMENT,
  `listname` varchar(256),
  `content` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO target (listname, content) VALUES
  ("分工会主席全体", "['2012011284', '2012011270']"),
  ("文体部", "['2012011270']"),
  ("宣传部", "['2012011284']"),
  ("公会成员全体", "['2012011284', '2012011270']");