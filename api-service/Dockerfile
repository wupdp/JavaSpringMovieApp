FROM maven AS build

WORKDIR /app

COPY ./pom.xml ./

RUN mvn dependency:go-offline

COPY ./src ./src

RUN mvn clean install -DskipTests

FROM openjdk:21-jdk-slim AS RUN

WORKDIR /app

COPY --from=build /app/target/*.jar /app/web-movie.jar

EXPOSE 8081

CMD ["java", "-jar", "web-movie.jar"]
