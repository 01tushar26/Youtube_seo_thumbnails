# Use Java 17 (recommended for Spring Boot)
FROM eclipse-temurin:21-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy project files
COPY . .

RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

Expose port (Render uses PORT internally)
EXPOSE 8080

# Run the jar
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]