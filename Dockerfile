# Stage 1: Build with Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy project files
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime with JRE
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/system-0.0.1-SNAPSHOT.jar /app/application.jar

# Environment variables (can be overridden at runtime)
ENV SPRING_PROFILES_ACTIVE=prod

# Expose backend port
EXPOSE 8082

# Healthcheck for container orchestration
HEALTHCHECK --interval=30s --timeout=10s \
  CMD curl -f http://localhost:8082/actuator/health || exit 1

# Run the Spring Boot app
CMD ["java", "-jar", "application.jar"]
