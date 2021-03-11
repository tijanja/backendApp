FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} backendApp-0.0.1-SNAPSHOT.jar
EXPOSE 9999
ENTRYPOINT ["java","-jar","/backendApp-0.0.1-SNAPSHOT.jar"]