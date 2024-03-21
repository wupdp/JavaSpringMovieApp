FROM maven AS build

ENV DBMS_URL http://dbms-service:8080/
ENV API_URL http://api-service:8081/

WORKDIR /app

COPY ./pom.xml ./

RUN mvn dependency:go-offline

COPY ./src ./src

RUN mvn clean install -DskipTests

FROM openjdk:21-jdk-slim AS RUN

ENV DBMS_URL http://dbms-service:8080/
ENV API_URL http://api-service:8081/

WORKDIR /app

COPY --from=build /app/target/*.jar /app/web-movie.jar

EXPOSE 8081

CMD ["java", "-jar", "web-movie.jar"]
