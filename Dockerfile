FROM openjdk:21-jdk-slim
WORKDIR /app

COPY mvnw .
COPY .mvn/ .mvn/
COPY pom.xml .

# Ensure the Maven wrapper has execution permissions
RUN chmod +x ./mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy the project source
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/App-1.jar"]
