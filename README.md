# kafka-streams-docker

The sample kafka streams dockerized application using docker-compose

## How to run

1. Execute command `sbt docker` 
2. In docker folder, open terminal and execute command `docker-compose up -d`
3. If you want to produce input messages, after 3 minutes from starting application execute command:  
`docker-compose exec broker kafka-console-producer --topic text-topic --bootstrap-server localhost:9092`
4. If you want to consume output messages, execute command:  
`docker-compose exec broker kafka-console-consumer --topic count-topic --from-beginning --bootstrap-server localhost:9092`
5. I order to delete application, execute command: `docker-compose down`