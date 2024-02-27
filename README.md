# Kinopoisk web Service
## Description of the repository
This service is under development. The repository contains the source code of the Spring Boot application, nginx configuration and Dockerfile for running two containers (Spring server and Nginx proxy-web server). This application provides access to some kinopoiskAPI features through a browser.
## Technologies Used:

1. Java
2. Spring Boot
3. Docker
4. Maven
5. Nginx
6. HTML
## Example
###### *GET* request to get info about any film by its title:
```
http://192.168.1.111:8088/movie/info?title=Мстители:Финал
```
```json
{
  "docs":[{"id":843650,
    "name":"Мстители: Финал",
    "alternativeName":"Avengers: Endgame",
    "enName":"",
    "type":"movie",
    "year":2019,
    "description":"Оставшиеся в живых члены команды Мстителей и их союзники должны разработать новый план, который поможет противостоять разрушительным действиям могущественного титана Таноса. После наиболее масштабной и трагической битвы в истории они не могут допустить ошибку.",
    "shortDescription":"Железный человек, Тор и другие пытаются переиграть Таноса. Эпохальное завершение супергеройской франшизы",
    "movieLength":181,
    "isSeries":false,
    "ticketsOnSale":false,
    "totalSeriesLength":null,
    "seriesLength":null,
    "ratingMpaa":"pg13",
    "ageRating":18,
    "top10":null,
    "top250":null,
    "typeNumber":1,
    "status":null,
    "names":[{"name":"Мстители: Финал"}]
  }]
}
```