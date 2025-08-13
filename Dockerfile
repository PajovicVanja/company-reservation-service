# syntax=docker/dockerfile:1

FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /build
COPY pom.xml ./
COPY .mvn/ .mvn/
COPY mvnw ./
RUN chmod +x mvnw && ./mvnw -q -B -DskipTests dependency:go-offline
COPY src ./src
RUN ./mvnw -q -B -DskipTests package spring-boot:repackage

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
