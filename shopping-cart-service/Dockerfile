FROM openjdk:17-alpine
WORKDIR /app
COPY ./target/shopping-cart-service-0.0.1-SNAPSHOT.jar /app/shopping-cart-service.jar
ENTRYPOINT ["java", "-jar", "/app/shopping-cart-service.jar"]
