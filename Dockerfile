# =====================================================================
# Stage 1: Build the Spring Boot application using the Gradle image
# =====================================================================
FROM gradle:8.8-jdk21-alpine AS builder
WORKDIR /app
COPY --chown=gradle:gradle gradlew .
COPY --chown=gradle:gradle gradle/wrapper gradle/wrapper
COPY --chown=gradle:gradle build.gradle settings.gradle /app/
RUN gradle dependencies
COPY --chown=gradle:gradle . /app
RUN gradle clean build --no-daemon

# =====================================================================
# Stage 2: Create the final, lightweight, and secure runtime image
# =====================================================================
FROM openjdk:21-slim
WORKDIR /app
RUN addgroup --system spring && adduser --system --ingroup spring spring
COPY --from=builder /app/build/libs/*.jar /app/app.jar
USER spring:spring
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
