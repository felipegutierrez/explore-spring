
# start the Kafka landoop cluster and access http://127.0.0.1:3030/
# sudo docker-compose up

# create the topic
# kafka-topics --create --topic library-events --zookeeper localhost:2181 --replication-factor 1 --partitions 1

# Kafka console producer with Avro schema registry
# kafka-avro-console-producer --broker-list 127.0.0.1:9092 --topic library-events --property schema.registry.url=http://127.0.0.1:8081 --property value.schema='{  "type" : "record",  "name" : "LibraryEvent",  "namespace" : "com.github.felipegutierrez.explore.spring.domain",  "fields" : [ {    "name" : "book",    "type" : {      "type" : "record",      "name" : "Book",      "fields" : [ {        "name" : "bookAuthor",        "type" : "string"      }, {        "name" : "bookId",        "type" : "int"      }, {        "name" : "bookName",        "type" : "string"      } ]    }  }, {    "name" : "libraryEventId",    "type" : "int"  }, {    "name" : "libraryEventType",    "type" : {      "type" : "enum",      "name" : "LibraryEventType",      "symbols" : [ "NEW", "UPDATE" ]    }  } ]}'

# produce data
# {"libraryEventId":23,"libraryEventType":"NEW","book":{"bookId":1,"bookName":"3 anos na Alemanha","bookAuthor":"Felipe"}}

# Kafka console consumer with Avro schema registry
# kafka-avro-console-consumer --topic library-events --bootstrap-server 127.0.0.1:9092 --property schema.registry.url=http://127.0.0.1:8081 --from-beginning





