# Java 21 base image
FROM eclipse-temurin:21-jdk

# App directory inside container
WORKDIR /app

# Copy Spring Boot jar
COPY target/jobportal-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"]
