# Use a specific Java version for the build environment
FROM openjdk:17-jdk-slim as build

# Set the working directory
WORKDIR /workspace/app

# Copy the Maven wrapper and project definition
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src src

# Package the application
RUN ./mvnw package -DskipTests

# Use a smaller image for the final runtime environment
FROM openjdk:17-jre-slim
VOLUME /tmp

# Copy the packaged JAR file from the build stage
COPY --from=build /workspace/app/target/*.jar app.jar

# Expose the port your Spring app runs on (usually 8080)
EXPOSE 8080

# Run the JAR file when the container starts
ENTRYPOINT ["java","-jar","/app.jar"]