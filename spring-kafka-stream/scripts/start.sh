
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

# Stop and delete all topics and data of confluent platform
# $ confluent local stop
# $ confluent local destroy

