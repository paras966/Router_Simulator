# Router_Simulator
Creating simulator to produce realistic data on kafka topic

# prerequisite
Kafka cluster should be up, refer below commands and configurations

# START ZOOKEEPER
bin/zookeeper-server-start.sh config/zookeeper.properties

# START SERVERS [BROKERS]
bin/kafka-server-start.sh config/server2.properties

# you can create more broker by adding below properties in new properties file
broker.id=1
listeners=PLAINTEXT://localhost:9093
log.dirs=/tmp/kafka-logs-1

# CREATE TOPIC
bin/kafka-topics.sh --create --topic routersData --bootstrap-server localhost:9092 --replication-factor 3 --partitions 3

# LIST TOPICS
bin/kafka-topics.sh --bootstrap-server localhost:9092 --list

# DESCRIBE TOPIC
bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic routerData

# Read data
bin/kafka-console-consumer.sh --topic routersData --bootstrap-server localhost:9092 --from-beginning

