# Use the Java 21 JDK image for the build environment
FROM openjdk:21-jdk-slim AS build

# Set the working directory
WORKDIR /workspace/app

# Copy the Maven wrapper and project definition
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src src

# Package the application, skipping tests
RUN ./mvnw package -DskipTests

# Use the more compatible 'slim' image for the final runtime environment
FROM openjdk:21-slim
VOLUME /tmp

# Copy the packaged JAR file from the build stage
COPY --from=build /workspace/app/target/*.jar app.jar

# Expose the port your Spring app runs on
EXPOSE 8080

# Run the JAR file when the container starts
ENTRYPOINT ["java","-jar","/app.jar"]