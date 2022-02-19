import fetch from "node-fetch"; // API to asynchronously fetch and process data from a given URI
import workerpool from "workerpool"; // API for easier multithreading
import stomp from "stomp-client"; // ActiveMQ client API

/* grabs posts JSON from jsonplaceholder.typicode.com */
const fetchedPosts = await fetch('https://jsonplaceholder.typicode.com/posts')
                           .then((response) => response.json());
const fetchedPostsNum = fetchedPosts.length; //number of posts obtained from jsonplaceholder

const pool = workerpool.pool(); //creates pool constant for workerpool

let posts = []; // array of json posts to pass into ActiveMQ

/* ternary function to determine is the number of held posts is less than 25 */
function getArrayLength(num){
    return ((num < 25) ? num : 25);
}

/* fills `let posts` with 25 or less json posts */
function makeMqArray(jsonArr, size){
    let mqArr = []
    for(let i = 0; i < size; i++)
        mqArr[i] = jsonArr[i];
    return mqArr.slice(0);
}

/* determines whether the 25 or less posts will be published to ActiveMQ
 * prevents more than 25 json posts being sent ot ActiveMQ */
const size = await pool.exec(getArrayLength, [fetchedPostsNum])
    .then(function(result){
        return result;
    })
    .catch(function(e){
        console.error(e);
    });

pool.terminate(); //closes thread pool

posts = makeMqArray(fetchedPosts, size); //sets `let posts`

const stompClient = new stomp("127.0.0.1", 61613); //sets connection info for ActiveMQ

/* Connects to ActiveMQ and publishes the json posts to the json_posts queue */
stompClient.connect(function(){
   console.log("Publisher connected");

   //loops through posts[] and publishes them to ActiveMQ
   posts.forEach(e => {
       //console.log(JSON.stringify(e));
       stompClient.publish("/queue/json_posts", JSON.stringify(e));
   });

   stompClient.disconnect(); //closes connection to ActiveMQ
});
