FROM maven AS build

WORKDIR /app

COPY ./src ./src
COPY ./pom.xml ./

RUN mvn clean install

FROM openjdk:17-jdk-slim AS RUN

WORKDIR /app

COPY --from=build /app/target/*.jar /app/web-movie.jar

EXPOSE 8081

CMD ["java", "-jar", "web-movie.jar"]

# Разрешаем входящий трафик на порт 8080 только с определенного IP-адреса
#iptables -A INPUT -p tcp --dport 8080 -s 192.168.1.200 -j ACCEPT

# Блокируем входящий трафик на порт 8080 с других IP-адресов
#iptables -A INPUT -p tcp --dport 8080 -j DROP
