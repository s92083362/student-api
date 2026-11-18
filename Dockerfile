
# Stage 1: Build the Spring Boot application
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre

WORKDIR /app

# Create a non-root user
RUN groupadd --system spring && useradd --system --gid spring spring

# Copy the application JAR
COPY --from=build /app/target/*.jar app.jar

# Run as a non-root user
USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
