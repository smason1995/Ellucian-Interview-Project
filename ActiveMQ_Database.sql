/* Creates ActiveMQ Database */
create database activemq_db;

/* Switches connection to activemq_db */
use activemq_db;

/* Creates table to store userid and title from ActiveMQ */
create table json_store(userid int, -- column for userid from JSON
                        title  varchar(6)); --column for title from JSON

/* Creates user 'subscriber' with a unique password */
create or replace user subscriber identified by 'uuDzSgHSd9CEvvqa';

/* Grants all privileges on activemq_db.json_store to our 'subscriber' user */
grant all privileges on activemq_db.json_store to 'subscriber'@localhost identified by 'uuDzSgHSd9CEvvqa';

/* Updates privleges */
flush privileges;
