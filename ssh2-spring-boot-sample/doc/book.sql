DROP TABLE IF EXISTS book;
CREATE TABLE book (
  ID 			INT(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  NAME			VARCHAR(64) NOT NULL DEFAULT '' COMMENT '图书名称',
  LINK 			VARCHAR(256) NOT NULL DEFAULT '' COMMENT '内容链接',
  PRIMARY KEY (ID)
 )ENGINE=INNODB DEFAULT CHARSET=UTF8 COMMENT '图书信息表';
 
 INSERT INTO `book` (`NAME`, `LINK`) VALUES 
 	('北京_中轴线上的城市','http://oarfc773f.bkt.clouddn.com/100000760bjzzxsdcs_1_1_r.jpg'),
 	('爸爸去上班','http://oarfc773f.bkt.clouddn.com/100000082bbqsb_1_1_r.jpg'),
 	('彩虹色的花','http://oarfc773f.bkt.clouddn.com/100000085chsdh_1_1_r.jpg'),
 	('迟到大王','http://oarfc773f.bkt.clouddn.com/100000086cddw_1_1_r.jpg'),
 	('动物绝对不应该穿衣服','http://oarfc773f.bkt.clouddn.com/100000087dwjdbygcyf_1_1_r.jpg'),
 	('战役','http://oarfc773f.bkt.clouddn.com/100000088zy_1_1_r.jpg')
 	;