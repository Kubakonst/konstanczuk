FROM maven:3-openjdk-11 AS build

WORKDIR /workspace/app
COPY pom.xml pom.xml
COPY src src
RUN mvn package -DskipTests

FROM adoptopenjdk/openjdk11:alpine
RUN mkdir -p /app
COPY --from=build /workspace/app/target/*.jar /app/application.jar
ARG profile
ENV SPRING_PROFILES_ACTIVE=$profile

ENTRYPOINT java -jar /app/application.jar -d spring.profiles.active=$profile