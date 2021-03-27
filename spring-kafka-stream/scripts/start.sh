
# configuring the confluent platform classpath at "~/.bashrc"
# export CONFLUENT_HOME=/home/felipe/Servers/confluent-6.1.1
# export PATH=$PATH:$CONFLUENT_HOME/bin

# Starting the confluent platform
# $ confluent local services start
# http://localhost:9021/

# General commands:
# $ kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic users
# $ kafka-console-producer --topic users --broker-list localhost:9092
# $ kafka-console-producer --topic test-topic --broker-list localhost:9092
# > {"name": "Felipe", "age": 38, "gender": "male"}
# > {"name": "Simone", "age": 40, "gender": "female"}
# $ kafka-console-consumer --topic test-topic --bootstrap-server localhost:9092 --from-beginning
# $ kafka-console-consumer --topic pos-topic --bootstrap-server localhost:9092 --from-beginning

# kafka-avro-console-consumer --bootstrap-server localhost:9092 --topic loyalty-avro-topic --from-beginning --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property print.key=true --property key.separator=":"
# kafka-avro-console-consumer --bootstrap-server localhost:9092 --topic hadoop-sink-avro-topic --from-beginning --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property print.key=true --property key.separator=":"
#

# Stop and delete all topics and data of confluent platform
# $ confluent local stop
# $ confluent local destroy

