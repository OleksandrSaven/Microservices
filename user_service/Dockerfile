FROM openjdk:17-alpine
WORKDIR /app
COPY /target/user_service-0.0.1-SNAPSHOT.jar /app/user-service.jar
ENTRYPOINT ["java", "-jar", "user-service.jar"]
