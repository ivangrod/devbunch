#!/bin/bash
if [ -e docker/connect-plugins/connect-plugins/kafka-connect-elasticsearch-4.1.0-standalone.jar ]
then
    echo "kafka-connect-elasticsearch.jar is already located" 
else
    cd docker/confluentinc/kafka-connect-elasticsearch
    mvn clean package -P standalone
    cp target/kafka-connect-elasticsearch-4.1.0-standalone.jar ../../connect-plugins/connect-plugins/kafka-connect-elasticsearch-4.1.0-standalone.jar
    cd ../../..
fi
cd docker
docker-compose down
docker-compose rm -f
docker-compose up --build -d

function checkContainerIsHealthy(){
	while [ true ]; do
        if [ $(docker inspect --format="{{json .State.Health.Status }}" "$1") == "healthy" ]; then
                echo "$1 is healthy. :D"
                break
        else
                echo "$1 is not healthy. Sleep for 5 second"
                sleep 5
                break
        fi
	done
}

checkContainerIsHealthy elasticsearch
checkContainerIsHealthy neo4j
checkContainerIsHealthy zookeeper
checkContainerIsHealthy kafka
checkContainerIsHealthy kafka-connect
checkContainerIsHealthy kibana

echo "**********************************************************************************"
echo "************************** All containers are ready! :) **************************"
echo "**********************************************************************************"

FEED_ITEM_TOPIC=$(docker exec kafka kafka-topics --zookeeper localhost:2181 --create --topic feed-item --partitions 3 --replication-factor 1)
FEED_ITEM_COMPLETE_TOPIC=$(docker exec kafka kafka-topics --zookeeper localhost:2181 --create --topic feed-item-complete --partitions 3 --replication-factor 1 )

if [[ $FEED_ITEM_TOPIC == "Created topic \"feed-item\"." && $FEED_ITEM_COMPLETE_TOPIC == "Created topic \"feed-item-complete\"." ]]; then
	echo "Topics has beem created successfully!"
else
	echo "Topics hasn't been created! :( Please, check kafka topics and kafka config. We recommend use kafka-tool (http://www.kafka tool.com) as a helper tool."
	exit 1
fi

JSON_OUTPUT=$(curl --header "Content-Type: application/json" \
  	 --request POST \
  	 --data '{   "name": "feed-item-connector",   "config": {     "connector.class": "io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",     "tasks.max": "1",     "topics": "feed-item-complete",     "key.ignore": "true",     "schema.ignore": "true",     "connection.url": "http://0.0.0.0:9200",     "type.name": "feed-item-type",     "name": "feed-item-connector"   } }' \
  	 http://localhost:8083/connectors &)

if [[ $JSON_OUTPUT = *"feed-item-connector"* ]]; then
  	echo "**********************************************************************************"
	echo "*************************** All configs are fine ! :) ****************************"
	echo "**********************************************************************************" 	
else
	echo "Unable to create feed-item-connector. Check kafka-connect container logs."
fi

cd ..

if [[ $1 == "debug" ]]; then
	echo ""
    echo "You can run the applications in debug mode :) " 
else
	cd collector
	mvn spring-boot:run &
	cd ../extractor
	mvn spring-boot:run &
	cd ../graph-model
	mvn spring-boot:run &
	cd ..
fi

exit 0
