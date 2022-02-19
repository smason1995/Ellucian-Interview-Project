/*
 *Script to consume messages from ActiveMQ
 * In order to work, script requires activemq-all-5.15.8.jar in %GROOVY_HOME%\lib (Windows) or $HOME/.groovy/lib (*nix)
 */

// ActiveMQ Imports
import javax.jms.*
import org.apache.activemq.*
import org.apache.activemq.command.*
// SQL Imports
import groovy.sql.Sql
import java.sql.*
// JSON Parsing API
import groovy.json.JsonSlurper

// Sets up ActiveMQ connection
def amqURL = "failover:tcp://localhost:61616"
println "Connecting to ActiveMQ at URL " + amqURL
def connFactory = new ActiveMQConnectionFactory(amqURL)
def conn = connFactory.createConnection()
conn.start()

// Sets up Mariadb connection
Sql sql
try{
    def url = "jdbc:mariadb://localhost:3306/activemq_db"
    def username = "subscriber"
    def password = "uuDzSgHSd9CEvvqa"
    def driver = 'org.mariadb.jdbc.Driver'
    sql = Sql.newInstance(url, username, password, driver)
    println "Connected to MariaDB as " + url
}
catch(ClassNotFoundException | SQLException e){
    println e
}

// JSON Parser
def jsonParser = new JsonSlurper()

// Creates session for ActiveMQ
println "Creating session"
def session = conn.createSession(true, ActiveMQSession.CLIENT_ACKNOWLEDGE)
// Creates queue in ActiveMQ
def dest = session.createQueue("json_posts")
// Creates consumer for ActiveMQ
def consumer = session.createConsumer(dest)

println("Waiting for messages...");
while(true) {
    msg = consumer.receive();
    println "Received message " + msg.toString()
    // Fill this witht the code to insert extract the JSON message userid and title for the SQL insert
    if( msg instanceof  ActiveMQBytesMessage ) {
        println "Message content: " + msg.getContent();
        // Insert message into activemq_db.json_store
        try{
            def userid = 1 // replace `1` with `userid` fromm JSON
            def title = 'TODO' // replace TODO with `title` from JSON
            sql.execute 'insert into activemq_db.json_store values(?, ?);', [userid, title]
        }
        catch(SQLException e){
            println(e)
        }
        msg.acknowledge()
        session.commit()
    }


}
