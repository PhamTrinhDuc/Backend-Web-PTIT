# Multi-stage Dockerfile optimized for a Maven + Java backend
# Stage 1 - build
FROM maven:3.9.4-eclipse-temurin-17 as builder
WORKDIR /app
# Improve build caching: copy pom and wrapper first to download dependencies
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -B -DskipTests dependency:go-offline
# Copy sources and build application (skip tests for faster builds; remove -DskipTests if you need tests)
COPY src ./src
RUN ./mvnw -B -DskipTests package --no-transfer-progress

# Stage 2 - runtime
FROM eclipse-temurin:17-jre-jammy
# Create non-root user
RUN useradd -m appuser && mkdir /app && chown appuser:appuser /app
WORKDIR /app
# copy the fat/uber jar produced by the build stage
ARG JAR_NAME=target/*.jar
COPY --from=builder /app/${JAR_NAME} app.jar
RUN chown appuser:appuser app.jar
USER appuser

# Tunable JVM options
ENV JAVA_OPTS="-XX:+UseG1GC -Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom"
EXPOSE 8080
ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar /app/app.jar"]

