## Build Instructions
git clone https://github.com/n8-n/SleepyRestTestNFlynn.git 

cd SleepyRestTestNFlynn
mvn package
java -jar target\SleepyRestTest-1.0.jar
 
 
 
## API Definition
http://localhost:8080/ 

Resource                |   Result
----------------------- | ---------------------------
GET /queue              |   returns list of IDs.
GET /queue/{id}         |   return position of {id}.
GET /queue/meantime     |   returns the average wait time in seconds.
POST /queue/{id}/{date} |   creates a new order.
DELETE /queue           |   removes the top queue item and returns it.
DELETE /queue/{id}      |   removes {id}.
