

# 1. Generating the KeyStore
keytool -keystore server.keystore.jks -alias localhost -validity 365 -genkey -keyalg RSA
keytool -list -v -keystore server.keystore.jks

# 2. Generating CA
openssl req -new -x509 -keyout ca-key -out ca-cert -days 365 -subj "/CN=local-security-CA"

# 3. Certificate Signing Request(CSR)
keytool -keystore server.keystore.jks -alias localhost -certreq -file cert-file

# 4. Signing the certificate
openssl x509 -req -CA ca-cert -CAkey ca-key -in cert-file -out cert-signed -days 365 -CAcreateserial -passin pass:password
keytool -printcert -v -file cert-signed

# 5. Adding the Signed Cert in to the KeyStore file
keytool -keystore server.keystore.jks -alias CARoot -import -file ca-cert
keytool -keystore server.keystore.jks -alias localhost -import -file cert-signed

# 6. edit the kafka server config/server.property
listeners=PLAINTEXT://localhost:9092, SSL://localhost:9095
ssl.keystore.location=/home/felipe/workspace-idea/explore-spring/spring-kafka-library-producer/ssl/server.keystore.jks
ssl.keystore.password=password
ssl.key.password=password
ssl.endpoint.identification.algorithm=


# 7. Generate the TrustStore
keytool -keystore client.truststore.jks -alias CARoot -import -file ca-cert


# 8. create the file "config/client-ssl.properties" with the content
security.protocol=SSL
ssl.truststore.location=/home/felipe/workspace-idea/explore-spring/spring-kafka-library-producer/ssl/client.truststore.jks
ssl.truststore.password=password
ssl.truststore.type=JKS

# ./bin/kafka-console-producer.sh --broker-list localhost:9095,localhost:9096,localhost:9097 --topic test-topic --producer.config config/client-ssl.properties



