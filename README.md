
# Ellucian PubSub to DB project

### Author: Sydney Mason
### Date: 2/18/2022

The following are the different parts of this system:
- Apache ActiveMQ
- app.mjs
- app.groovy
- MariaDB
- ActiveMQ_Database.sql

All code/scripts are in the Code folder in my submitted zip file. All needed Jar files are in the Jars_for_Groovy; these need to be placed in the Groovy lib folder (`%GROOVY_HOME%\lib` for Windows systems, `$HOME/.groovy/lib` for *nix systems).

## app.mjs

This piece acts as the publisher in the project. It will grab JSON messages from jsonplaceholder.typicode.com
It will pass up 25 JSON messages or less to Apache ActiveMQ.
To accomplish this task with NodeJS, I installed the following NPM packages:
- node-fetch: `npm install node-fetch`
- workerpool: `npm install workerpool`
- stomp-client: `npm install stomp-client`

During my development I would run the code by using `node app.mjs` in my command line.

## app.groovy

This was supposed to act as the subscriber in the project. I found that two extra drivers were needed; the MariaDB JDBC driver (provided by the MariaDB foundation in a Jar) and the Apache ActiveMQ driver (provided by the Apache ActiveMQ developers in a Jar). It is able to grab a messaage from ActiveMQ and insert into the database table with the provided subscriber user. Unfortunately, there is still work to be done to extract the JSON post from the ActiveMQ message.

During my development I would run this with the command `groovy app.groovy` in my command line.

## MariaDB

I used MariaDB for my database system. I have provided a script that sets up the database, table, user, and permissions for you as along as an accessible MariaDB system is setup. To run this script you should be able to run the following: `source path/to/script/ActiveMQ_Database.sql`

The database will have a single, two column table called `json_store` which will hold integers (userid) and strings (title). The user, `subscriber`, only has access to the singular in the database. They are able to select, insert, and delete from the table.

## Apache ActiveMQ

The Message Queue system used for this project. It recieves string messages from `app.mjs`. Its messages are retrieved by `app.groovy`, which was then supposed to extract the userid and title from the help JSON string in the message to read into a MariaDB database table.

To start Apache ActiveMQ, do the following:

    cd [activemq_install_dir]
    bin\activemq start

## Running this system

In order to successfully run the after getting everything setup is:

    Start activemq

    run app.groovy

    run app.mjs
