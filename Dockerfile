FROM adoptopenjdk/openjdk11:latest
WORKDIR /opt/dockerapp
ARG JAR_FILE=target/home.assignment-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} dockerapp.jar
ENTRYPOINT ["java","-jar","dockerapp.jar"]