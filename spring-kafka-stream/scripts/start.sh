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

# using parameter --json to consume messages serialized in JSON to AVRO
# kafka-avro-console-consumer --bootstrap-server localhost:9092 --topic loyalty-avro-topic --from-beginning --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property print.key=true --property key.separator=":"
# kafka-avro-console-consumer --bootstrap-server localhost:9092 --topic hadoop-sink-avro-topic --from-beginning --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property print.key=true --property key.separator=":"

# using parameter --avro to consume messages serialized in AVRO to JSON
# kafka-json-schema-console-consumer --bootstrap-server localhost:9092 --topic loyalty-topic --from-beginning --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property print.key=true --property key.separator=":"
# kafka-json-schema-console-consumer --bootstrap-server localhost:9092 --topic hadoop-sink-topic --from-beginning --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property print.key=true --property key.separator=":"

# using exactly-once semantic
# kafka-avro-console-consumer --bootstrap-server localhost:9092 --topic loyalty-avro-ex-topic --from-beginning --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property print.key=true --property key.separator=":"
# kafka-avro-console-consumer --bootstrap-server localhost:9092 --topic hadoop-sink-avro-ex-topic --from-beginning --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property print.key=true --property key.separator=":"

# consuming XML messages and producing JSON messages to 2 branches
# kafka-console-producer --topic xml-order-topic --broker-list localhost:9092
# kafka-console-consumer --bootstrap-server localhost:9092 --topic india-orders-topic --from-beginning --property print.key=true --property key.separator=":"
# kafka-console-consumer --bootstrap-server localhost:9092 --topic abroad-orders-topic --from-beginning --property print.key=true --property key.separator=":"
# kafka-console-consumer --bootstrap-server localhost:9092 --topic order-error-topic --from-beginning --property print.key=true --property key.separator=":"
# Abroad Order ---------------
# <?xml version="1.0" encoding="UTF-8"?><order order-id="889923" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="order.xsd"><order-by>John Smith</order-by><ship-to><name>Ola Nordmann</name><address>Langgt 23</address><city>4000 Stavanger</city><country>Norway</country></ship-to><item><title>Empire Burlesque</title><note>Special Edition</note><quantity>1</quantity><price>10.90</price></item><item><title>Hide your heart</title><quantity>1</quantity><price>9.90</price></item></order>
# <?xml version="1.0" encoding="UTF-8"?><order order-id="889924" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="order.xsd"><order-by>Michael Jordan</order-by><ship-to><name>Chris Stokes</name><address>Fremont</address><city>Geneva</city><country>Switzerland</country></ship-to><item><title>Empire Burlesque</title><note>Special Edition</note><quantity>1</quantity><price>10.90</price></item><item><title>Hide your heart</title><quantity>1</quantity><price>9.90</price></item></order>
# Indian Order --------------
# <?xml version="1.0" encoding="UTF-8"?><order order-id="889925" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="order.xsd"><order-by>Abdul Hamid</order-by><ship-to><name>Nawab Aalam</name><address>42 Park Squire</address><city>Bangalore</city><country>India</country></ship-to><item><title>Empire Burlesque</title><note>Special Edition</note><quantity>1</quantity><price>10.90</price></item><item><title>Hide your heart</title><quantity>1</quantity><price>9.90</price></item></order>
# Parsing Error Order ---------------------
# <?xml version="1.0" encoding="UTF-8"?><order order-id="889926" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="order.xsd"><order-by>Abdul Hamid<order-by><ship-to><name>Nawab Aalam</name><address>42 Park Squire</address><city>Bangalore</city><country>India</country></ship-to><item><title>Empire Burlesque</title><note>Special Edition</note><quantity>1</quantity><price>10.90</price></item><item><title>Hide your heart</title><quantity>1</quantity><price>9.90</price></item></order>
# Missing City Error ------------------------
# <?xml version="1.0" encoding="UTF-8"?><order order-id="889927" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="order.xsd"><order-by>Abdul Hamid</order-by><ship-to><name>Nawab Aalam</name><address>42 Park Squire</address><city></city><country>India</country></ship-to><item><title>Empire Burlesque</title><note>Special Edition</note><quantity>1</quantity><price>10.90</price></item><item><title>Hide your heart</title><quantity>1</quantity><price>9.90</price></item></order>

# KTable stream demo
# kafka-console-producer --topic stock-tick-topic --broker-list localhost:9092 --property parse.key=true --property key.separator=":"
HDFCBANK:2120
HDFCBANK:2150
HDFCBANK:2180
TCS:2920

# KTable streaming aggregation word count
# kafka-console-producer --topic streaming-words-topic --broker-list localhost:9092
# kafka-console-consumer --bootstrap-server localhost:9092 --topic streaming-words-output-topic --from-beginning --property print.key=true --property key.separator=":"

# KStream aggregation of Employees
# kafka-avro-console-producer --broker-list localhost:9092 --topic employees-topic --property value.schema='{"namespace": "com.github.felipegutierrez.explore.spring.model","type": "record","name": "Employee","fields": [{"name": "id","type": "string"},{"name": "name","type": "string"},{"name": "department","type": "string"},{"name": "salary","type":"int"}]}'
# data
{"id": "101", "name": "Prashant", "department": "engineering", "salary": 5000}
{"id": "102", "name": "John", "department": "accounts", "salary": 8000}
{"id": "103", "name": "Abdul", "department": "engineering", "salary": 3000}
{"id": "104", "name": "Melinda", "department": "support", "salary": 7000}
{"id": "105", "name": "Jimmy", "department": "support", "salary": 6000}

{"id": "101", "name": "Prashant", "department": "support", "salary": 5000}
{"id": "104", "name": "Melinda", "department": "engineering", "salary": 7000}

# Stop and delete all topics and data of confluent platform
# $ confluent local stop
# $ confluent local destroy
