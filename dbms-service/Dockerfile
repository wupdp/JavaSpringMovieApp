FROM maven AS build

WORKDIR /app

ENV DATABASE_URL 192.168.1.111:5432

COPY ./src ./src
COPY ./pom.xml ./

RUN mvn clean install

FROM openjdk:21-jdk-slim AS RUN

ENV DATABASE_URL db-container:5432
WORKDIR /app

COPY --from=build /app/target/*.jar /app/web-movie.jar

EXPOSE 8080

CMD ["java", "-jar", "web-movie.jar"]
