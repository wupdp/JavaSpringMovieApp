FROM maven AS build

ENV DBMS_URL http://dbms-service:8080/
ENV API_URL http://api-service:8081/

WORKDIR /app

COPY ./pom.xml ./

RUN mvn dependency:go-offline -DexcludeArtifactIds=maven-checkstyle-plugin

COPY ./src ./src

RUN mvn clean install -DskipTests -Pdocker

FROM openjdk:21-jdk-slim AS RUN

ENV DBMS_URL http://dbms-service:8080/
ENV API_URL http://api-service:8081/

WORKDIR /app

COPY --from=build /app/target/*.jar /app/web-movie.jar

EXPOSE 8088

CMD ["java", "-jar", "web-movie.jar"]
