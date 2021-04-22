FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine
VOLUME /tmp
ARG DEPENDENCY=build
RUN echo ${DEPENDENCY}
COPY build/libs/reactor-only-0.1.0.jar reactor-only.jar
ENTRYPOINT ["java","-jar","reactor-only.jar", "0"]
