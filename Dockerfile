# -- STAGE 1: BUILD --
# Use a full JDK 21 image with Gradle pre-installed for the build environment.
FROM gradle:8-jdk21 AS build

# Set the working directory inside the container.
WORKDIR /app

# Copy the Gradle wrapper and project files needed for dependencies.
# This improves caching and speeds up subsequent builds.
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle settings.gradle ./

# Download all dependencies without building the final JAR.
# This allows Docker to cache this layer.
RUN gradle dependencies

# Copy the project source code.
COPY src ./src

# Package the application into a JAR file.
RUN gradle clean build

# -- STAGE 2: RUN --
# Use a minimal JRE 21 image for the runtime environment.
FROM eclipse-temurin:21-jre-jammy AS run

# Set the working directory.
WORKDIR /app
EXPOSE 8080

#Environment variable for required port of Cloud Run
ENV PORT=8080

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar /app/app.jar

# Define the entrypoint to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
