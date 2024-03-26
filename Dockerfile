FROM openjdk:17-jdk-slim-buster
ENV TZ=America/New_York
WORKDIR /app

COPY ./target/srv-auth-0.0.1-SNAPSHOT.jar /app/

WORKDIR /app
ENTRYPOINT java -jar srv-auth-0.0.1-SNAPSHOT.jar
