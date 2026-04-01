# Multi-stage Dockerfile optimized for a Maven + Java backend

# Stage 1: build the application with Maven (uses Eclipse Temurin JDK 17)
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /workspace

# Copy only what is needed to download dependencies (improves cache reuse)
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -B -DskipTests dependency:go-offline

# Copy source and build the project (skip tests for faster CI builds)
COPY src ./src
RUN ./mvnw -B -DskipTests package --no-transfer-progress

# Stage 2: create minimal runtime image
FROM eclipse-temurin:17-jre-jammy

# Create non-root user early
RUN useradd -m -u 1000 appuser && mkdir -p /app && chown appuser:appuser /app
WORKDIR /app

# Copy the built jar from the builder stage; use explicit path glob
ARG JAR_GLOB=target/*.jar
COPY --from=builder /workspace/${JAR_GLOB} /app/app.jar
RUN chown appuser:appuser /app/app.jar

# Switch to non-root user
USER appuser

# Tunable JVM options; can be overridden at runtime via environment variable
ENV JAVA_OPTS="-XX:+UseG1GC -Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
