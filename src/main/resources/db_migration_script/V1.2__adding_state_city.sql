USE `iexpress`;

DROP TABLE IF EXISTS state;

CREATE TABLE state (
id int(11) NOT NULL AUTO_INCREMENT,
name varchar(512) NOT NULL DEFAULT '',
PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO state (id, name)
VALUES
(4,'Maharashtra'),
(5,'Andaman & Nicobar Islands'),
(6,'Andhra Pradesh'),
(7,'Arunachal Pradesh'),
(8,'Assam'),
(9,'Bihar'),
(10,'Chhattisgarh'),
(11,'Dadra & Nagar Haveli'),
(12,'Daman & Diu'),
(13,'Delhi'),
(14,'Goa'),
(15,'Gujarat'),
(16,' India'),
(17,'Gujrat'),
(18,'Hariyana'),
(19,'Haryana'),
(20,'Himachal Pradesh'),
(21,'Jammu & Kashmir'),
(22,'Jharkhand'),
(23,'Karnataka'),
(24,'Kerala'),
(25,'Lakshadweep'),
(26,'Madhya Pradesh'),
(27,'Maharastra'),
(28,'Manipur'),
(29,'Meghalaya'),
(30,'Mizoram'),
(31,'Nagaland'),
(32,'Orissa'),
(33,'Pondicherry'),
(34,'Punjab'),
(35,'Rajasthan'),
(36,'Rajastan'),
(37,'Sikkim'),
(38,'West Bengal'),
(39,'Tamil Nadu'),
(40,'Tripura'),
(41,'Uttar Pradesh'),
(42,' Ghazipur'),
(43,' Hardoi'),
(44,' Rampur'),
(45,' Agra'),
(46,' Farrukhabad'),
(47,' Bulandshahr'),
(48,'Uttarakhand'),
(49,' Purulia');


