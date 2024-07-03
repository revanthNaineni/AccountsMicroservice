FROM openjdk:17-jdk-slim

MAINTAINER https://github.com/revanthNaineni

COPY target/accountsmicroservices-0.0.1-SNAPSHOT.jar accountsmicroservices-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","accountsmicroservices-0.0.1-SNAPSHOT.jar"]