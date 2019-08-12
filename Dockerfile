FROM openjdk:8-jdk

LABEL maintainer="patrick.zhou@isep.fr"

EXPOSE 8080

RUN mkdir -p /usr/src/web-semantic
ADD target/api-0.0.1-SNAPSHOT.jar /usr/src/web-semantic/api-0.0.1-SNAPSHOT.jar
WORKDIR /usr/src/web-semantic
COPY . /usr/src/web-semantic
ENTRYPOINT ["java","-jar","api-0.0.1-SNAPSHOT.jar"]



