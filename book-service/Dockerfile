FROM openjdk:17-alpine
WORKDIR /app
COPY /target/BookService-0.0.1-SNAPSHOT.jar /app/bookservice.jar
ENTRYPOINT ["java", "-jar", "bookservice.jar"]
