FROM maven AS build

WORKDIR /app

COPY ./src ./src
COPY ./pom.xml ./

RUN mvn clean install

FROM openjdk:21-jdk-slim AS RUN

WORKDIR /app

COPY --from=build /app/target/*.jar /app/web-movie.jar

EXPOSE 8081

CMD ["java", "-jar", "web-movie.jar"]