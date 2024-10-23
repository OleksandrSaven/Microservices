FROM openjdk:17-alpine
WORKDIR /app
COPY target/gateway-service-0.0.1-SNAPSHOT.jar /app/gateway-service.jar
ENTRYPOINT ["java", "-jar", "gateway-service.jar"]
