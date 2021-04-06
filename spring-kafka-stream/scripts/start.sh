# configuring the confluent platform classpath at "~/.bashrc"
# export CONFLUENT_HOME=/home/felipe/Servers/confluent-6.1.1
# export PATH=$PATH:$CONFLUENT_HOME/bin

# Starting the confluent platform
# $ confluent local services start
# http://localhost:9021/

# simple input/output Spring+Kafka-stream application
# kafka-console-producer --broker-list localhost:9092 --topic input-topic
# kafka-console-consumer --topic output-topic --from-beginning --bootstrap-server localhost:9092

# User application with JSON:
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

# KStream with time window
# kafka-console-producer --broker-list localhost:9092 --topic simple-invoice-topic --property parse.key=true --property key.separator=":"
STR1534:{"InvoiceNumber": 101,"CreatedTime": "1549360860000","StoreID": "STR1534", "TotalAmount": 1920}
STR1535:{"InvoiceNumber": 102,"CreatedTime": "1549360900000","StoreID": "STR1535", "TotalAmount": 1860}
STR1534:{"InvoiceNumber": 103,"CreatedTime": "1549360999000","StoreID": "STR1534", "TotalAmount": 2400}

STR1536:{"InvoiceNumber": 104,"CreatedTime": "1549361160000","StoreID": "STR1536", "TotalAmount": 8936}
STR1534:{"InvoiceNumber": 105,"CreatedTime": "1549361270000","StoreID": "STR1534", "TotalAmount": 6375}
STR1536:{"InvoiceNumber": 106,"CreatedTime": "1549361370000","StoreID": "STR1536", "TotalAmount": 9365}

# User click session window
# kafka-console-producer --broker-list localhost:9092 --topic user-clicks-topic --property parse.key=true --property key.separator=":"
USR101:{"UserID": "USR101","CreatedTime": "1549360860000","CurrentLink": "NULL", "NextLink": "Home"}
USR102:{"UserID": "USR102","CreatedTime": "1549360920000","CurrentLink": "NULL", "NextLink": "Home"}
USR101:{"UserID": "USR101","CreatedTime": "1549361040000","CurrentLink": "Home", "NextLink": "Books"}
USR101:{"UserID": "USR101","CreatedTime": "1549361400000","CurrentLink": "Books", "NextLink": "Kafka"}
USR102:{"UserID": "USR102","CreatedTime": "1549360920000","CurrentLink": "NULL", "NextLink": "Home"}
USR102:{"UserID": "USR102","CreatedTime": "1549361400000","CurrentLink": "Home", "NextLink": "Courses"}
USR101:{"UserID": "USR101","CreatedTime": "1549361220000","CurrentLink": "Kafka", "NextLink": "Preview"}
USR101:{"UserID": "USR101","CreatedTime": "1549361940000","CurrentLink": "Preview", "NextLink": "Buy"}

# Kafka JOIN KStream of Payment Requests with Payment Confirmation
# kafka-console-producer --broker-list localhost:9092 --topic payment_request --property parse.key=true --property key.separator=":"
# kafka-console-producer --broker-list localhost:9092 --topic payment_confirmation --property parse.key=true --property key.separator=":"
100001:{"TransactionID": "100001", "CreatedTime": 1550149860000, "SourceAccountID": "131100", "TargetAccountID": "151837", "Amount": 3000, "OTP": 852960}
100002:{"TransactionID": "100002", "CreatedTime": 1550149920000, "SourceAccountID": "131200", "TargetAccountID": "151837", "Amount": 2000, "OTP": 931749}
100003:{"TransactionID": "100003", "CreatedTime": 1550149980000, "SourceAccountID": "131300", "TargetAccountID": "151837", "Amount": 5000, "OTP": 591296}
100004:{"TransactionID": "100004", "CreatedTime": 1550150100000, "SourceAccountID": "131400", "TargetAccountID": "151837", "Amount": 1000, "OTP": 283084}

100001:{"TransactionID": "100001", "CreatedTime": 1550150100000, "OTP": 852960}
100002:{"TransactionID": "100002", "CreatedTime": 1550150280000, "OTP": 931749}
100004:{"TransactionID": "100004", "CreatedTime": 1550150040000, "OTP": 283086}

# Kafka JOIN KTable of user login and details
# kafka-console-producer --broker-list localhost:9092 --topic user-master --property parse.key=true --property key.separator=":"
# kafka-console-producer --broker-list localhost:9092 --topic user-login --property parse.key=true --property key.separator=":"
100001:{"UserName": "Prashant", "LoginID": "100001", "LastLogin": 1550150109302}
100009:{"UserName": "Alisha", "LoginID": "100009", "LastLogin": 1550150280409}
100087:{"UserName": "Abdul", "LoginID": "100087", "LastLogin": 1550150290305}

100001:{"LoginID": "100001", "CreatedTime": 1550150291000}
100087:{"LoginID": "100087", "CreatedTime": 1550150580000}

# Kafka JOIN using GlobalKTable to process click using broadcast JOIN to a KStream
# kafka-console-producer --broker-list localhost:9092 --topic active-inventories --property parse.key=true --property key.separator=":"
# kafka-console-producer --broker-list localhost:9092 --topic ad-clicks --property parse.key=true --property key.separator=":"
1001:{"InventoryID": "1001", "NewsType": "Sports"}
1002:{"InventoryID": "1002", "NewsType": "Politics"}
1003:{"InventoryID": "1003", "NewsType": "LocalNews"}
1004:{"InventoryID": "1004", "NewsType": "WorldNews"}
1005:{"InventoryID": "1005", "NewsType": "Health"}
1006:{"InventoryID": "1006", "NewsType": "Lifestyle"}
1007:{"InventoryID": "1007", "NewsType": "Literature"}
1008:{"InventoryID": "1008", "NewsType": "Education"}
1009:{"InventoryID": "1009", "NewsType": "Social"}
1010:{"InventoryID": "1010", "NewsType": "Business"}

1001:{"InventoryID": "1001"}
1002:{"InventoryID": "1002"}
1003:{"InventoryID": "1003"}
1004:{"InventoryID": "1004"}
1004:{"InventoryID": "1004"}
1005:{"InventoryID": "1005"}
1006:{"InventoryID": "1006"}
1007:{"InventoryID": "1007"}
1008:{"InventoryID": "1008"}
1009:{"InventoryID": "1009"}
1010:{"InventoryID": "1010"}

##########################################################
################## FUNCTIONAL examples ###################
# kafka-console-producer --broker-list localhost:9092 --topic input-func-topic
# kafka-console-consumer --topic output-func-topic --from-beginning --bootstrap-server localhost:9092
# kafka-console-producer --broker-list localhost:9092 --topic input-func-stream-wordcount-topic
# kafka-console-consumer --topic output-func-stream-wordcount-topic --from-beginning --bootstrap-server localhost:9092 --property print.key=true --property key.separator=":"

# functional User click session window
# kafka-console-producer --broker-list localhost:9092      --topic user-clicks-func-in-topic --property parse.key=true  --property key.separator=":"
# kafka-console-consumer --bootstrap-server localhost:9092 --topic user-clicks-func-out-topic --property print.key=true --property key.separator=":"
USR101:{"UserID": "USR101","CreatedTime": "1549360860000","CurrentLink": "NULL", "NextLink": "Home"}
USR102:{"UserID": "USR102","CreatedTime": "1549360920000","CurrentLink": "NULL", "NextLink": "Home"}
USR101:{"UserID": "USR101","CreatedTime": "1549361040000","CurrentLink": "Home", "NextLink": "Books"}
USR101:{"UserID": "USR101","CreatedTime": "1549361400000","CurrentLink": "Books", "NextLink": "Kafka"}
USR102:{"UserID": "USR102","CreatedTime": "1549360920000","CurrentLink": "NULL", "NextLink": "Home"}
USR102:{"UserID": "USR102","CreatedTime": "1549361400000","CurrentLink": "Home", "NextLink": "Courses"}
USR101:{"UserID": "USR101","CreatedTime": "1549361220000","CurrentLink": "Kafka", "NextLink": "Preview"}
USR101:{"UserID": "USR101","CreatedTime": "1549361940000","CurrentLink": "Preview", "NextLink": "Buy"}

# function branches
# consuming XML messages and producing JSON messages to 2 branches
# kafka-console-producer --topic xml-input-order --broker-list localhost:9092
# kafka-console-consumer --bootstrap-server localhost:9092 --topic output-order-india --from-beginning --property print.key=true --property key.separator=":"
# kafka-console-consumer --bootstrap-server localhost:9092 --topic output-order-abroad --from-beginning --property print.key=true --property key.separator=":"
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


#
#
#
#
#
#

# Stop and delete all topics and data of confluent platform
# $ confluent local stop
# $ confluent local destroy
