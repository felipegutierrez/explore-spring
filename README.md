[![Java CI with Gradle](https://github.com/felipegutierrez/explore-spring/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/felipegutierrez/explore-spring/actions/workflows/gradle.yml)
[![Run Test Suites](https://github.com/felipegutierrez/explore-spring/actions/workflows/codecov-test-suites.yml/badge.svg)](https://github.com/felipegutierrez/explore-spring/actions/workflows/codecov-test-suites.yml)
[![codacy-coverage-reporter](https://github.com/felipegutierrez/explore-spring/actions/workflows/codacy-coverage-reporter.yml/badge.svg)](https://github.com/felipegutierrez/explore-spring/actions/workflows/codacy-coverage-reporter.yml)
[![codecov](https://codecov.io/gh/felipegutierrez/explore-spring/branch/main/graph/badge.svg?token=GOUUP2T07P)](https://codecov.io/gh/felipegutierrez/explore-spring)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/9a331d3f2b594cdea7a1acfa78a98153)](https://app.codacy.com/gh/felipegutierrez/explore-spring?utm_source=github.com&utm_medium=referral&utm_content=felipegutierrez/explore-spring&utm_campaign=Badge_Grade_Settings)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/06261b73b18f43038e1dc36cafa90ff4)](https://www.codacy.com/gh/felipegutierrez/explore-spring/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=felipegutierrez/explore-spring&amp;utm_campaign=Badge_Coverage)
![Lines of code](https://img.shields.io/tokei/lines/github/felipegutierrez/explore-spring)

# Explore Spring Framework

Exploring Spring framework version 2.4.3 with [projectreactor](https://projectreactor.io/) and JDK 11.

### Modules

- [Spring basics](spring-basics)
- [Spring web](spring-web)
- [Spring reactive web server](spring-web-react)
- [Spring reactive web client](spring-web-react-client)
- [Spring with Kafka producer](spring-kafka-library-producer)
- [Spring with Kafka consumer](spring-kafka-library-consumer)

### Basic commands

- `./gradlew clean`
- `./gradlew build`
- `./gradlew test --info`
- Starting MongoDB:
```
cd spring-web-flux/
docker-compose up
docker ps
docker volume ls
mongo admin -u root -p rootpassword
show dbs
```
- Start spring-web-reactive application using Spring profiles:
```
cd spring-web-flux/
gradle build -x test
java -jar -Dspring.profiles.active=dev build/libs/spring-web-react-0.0.1.jar
```
- Start Zookeeper: `./bin/zookeeper-server-start.sh config/zookeeper.properties`
- Configure `config/server.properties`:
```
listeners=PLAINTEXT://:9092
auto.create.topics.enable=false
```
- Start Kafka 2.7.0 broker with the `config/server.properties` configuration file:
```
broker.id=0|1|2
listeners=PLAINTEXT://:9092|9093|9094
auto.create.topics.enable=false
log.dirs=/tmp/kafka-logs-0|1|2
```
Command:
```
./bin/kafka-server-start.sh config/server.properties
./bin/kafka-server-start.sh config/server-1.properties
./bin/kafka-server-start.sh config/server-2.properties
```
- tests:
```
./bin/kafka-topics.sh --create --topic test-topic            -zookeeper localhost:2181 --replication-factor 1 --partitions 4
./bin/kafka-topics.sh --create --topic test-topic-replicated -zookeeper localhost:2181 --replication-factor 3 --partitions 3
./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test-topic
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --from-beginning
./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test-topic            --property "key.separator=-" --property "parse.key=true"
./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test-topic-replicated --property "key.separator=-" --property "parse.key=true"
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic            --from-beginning --property "key.separator= - " --property "print.key=true"
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic-replicated --from-beginning --property "key.separator= - " --property "print.key=true"
./bin/kafka-topics.sh --zookeeper localhost:2181 --list
./bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --from-beginning --property "key.separator= - " --property "print.key=true" --group g1
./bin/kafka-run-class.sh kafka.tools.DumpLogSegments --deep-iteration --files /tmp/kafka-logs/test-topic-0/00000000000000000000.log
./bin/kafka-topics.sh --zookeeper localhost:2181 --describe --topic test-topic-replicated
```




