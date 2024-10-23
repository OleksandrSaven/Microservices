FROM openjdk:17-alpine
WORKDIR /app
COPY target/discovery-service-0.0.1-SNAPSHOT.jar /app/discovery-service.jar
ENTRYPOINT ["java", "-jar", "discovery-service.jar"]
