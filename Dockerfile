# =====================================================================
# Stage 1: Build the Spring Boot application using the Gradle image
# =====================================================================
FROM gradle:8.8-jdk21-alpine AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper files to leverage caching.
COPY --chown=gradle:gradle gradlew .
COPY --chown=gradle:gradle gradle/wrapper gradle/wrapper

# Copy the build configuration files.
COPY --chown=gradle:gradle build.gradle settings.gradle /app/

# Download all dependencies without building the application.
RUN gradle dependencies

# Copy the source code and build the application.
COPY --chown=gradle:gradle . /app
RUN gradle clean build --no-daemon

# =====================================================================
# Stage 2: Create the final, lightweight, and secure runtime image
# =====================================================================
FROM openjdk:21-slim
WORKDIR /app
RUN addgroup --system spring && adduser --system --ingroup spring spring

# *** CORRECTED LINE ***
# Copy the JAR file from the 'builder' stage to the final image
# Note the trailing slash to indicate the destination is a directory
COPY --from=builder /app/build/libs/EdaBackendPublisher.jar /app/

# Run the application as the non-root 'spring' user
USER spring:spring

# Expose the default Spring Boot port
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "EdaBackendPublisher.jar"]

