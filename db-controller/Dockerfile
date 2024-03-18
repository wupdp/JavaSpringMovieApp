FROM maven AS build

ENV DBMS_URL http://dbms-service:8080/
ENV API_URL http://api-service:8081/

WORKDIR /app

COPY ./src ./src
COPY ./pom.xml ./

RUN mvn clean install

FROM openjdk:21-jdk-slim AS RUN

ENV DBMS_URL http://dbms-service:8080/
ENV API_URL http://api-service:8081/

WORKDIR /app

COPY --from=build /app/target/*.jar /app/web-movie.jar

EXPOSE 8081

CMD ["java", "-jar", "web-movie.jar"]
