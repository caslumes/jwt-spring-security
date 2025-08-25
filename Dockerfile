FROM maven:3.9.11-eclipse-temurin-24-alpine

WORKDIR /api

COPY ./target/securityservice-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "./securityservice-0.0.1-SNAPSHOT.jar"]
