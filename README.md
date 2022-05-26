## Учет непродовольственных товаров на складе

[![Build Status](https://app.travis-ci.com/SlartiBartFast-art/automated_accounting.svg?branch=master)](https://app.travis-ci.com/SlartiBartFast-art/automated_accounting)
![GitHub top language](https://img.shields.io/github/languages/top/SlartiBartFast-art/automated_accounting?logo=java&logoColor=red&style=plastic)
![GitHub last commit](https://img.shields.io/github/last-commit/SlartiBartFast-art/automated_accounting?logo=github&logoColor=red)

app is under development

Приложение для автоматизации учёта товаров на складе магазина. 

Приложение использует REST API архитектуру
развернуто в облаке(Heroku)

        - https://automated-accounting.herokuapp.com/

  Used technology stack:
______________________________________________
- Java Core
- JWT
- Spring Boot (Web, Data JPA) 
- Liquibase
- H2
- Maven
- Docker
- Travis C.I.
- Postman - (Postman is an API platform for building and using API)

Коллекция запрос расположена в папке проекта /collections

Docker образ приложения, Вы можете скачать по ссылке:

 https://hub.docker.com/r/slartibartfastart/automated

 или To pull image from repository:

docker pull slartibartfastart/automated:latest

------------
Что необходимо сделать:

Реализовать приложение для автоматизации учёта носков на складе магазина.

Кладовщик должен иметь возможность:
_____________
-  учесть приход и отпуск носков;
-  узнать общее количество носков определенного цвета и состава в данный момент времени.
Внешний интерфейс приложения представлен в виде HTTP API (REST, если хочется).
   
   Параметры запроса передаются в URL:

-  color — цвет носков, строка;
-  operation — оператор сравнения значения количества хлопка в составе носков, одно значение из: moreThan, lessThan, equal;
-  cottonPart — значение процента хлопка в составе носков из сравнения.

Результаты:

-  HTTP 200 — запрос выполнен, результат в теле ответа в виде строкового представления целого числа;
-  HTTP 400 — параметры запроса отсутствуют или имеют некорректный формат;
-  HTTP 500 — произошла ошибка, не зависящая от вызывающей стороны (например, база данных недоступна).

Для хранения данных системы можно использовать любую реляционную базу данных. Схему БД желательно хранить в репозитории в любом удобном виде.

Как это сделать:

-  написано на языке Java;
-  standalone - состоять из одного выполняемого компонента верхнего уровня;
-  headless - без UI;
-  оформлено как репозиторий.
-  приложение будет основано на Spring(Boot) Framework;
-  для версионирования схемы базы данных будет использоваться Liquibase или Flyway;
-  приложение будет развернуто на любом облачном сервисе, например Heroku, и его API будет доступно для вызова.
-  база данных будет подниматься рядом с приложением в докер-контейнере;

Список URL HTTP-методов
________
Entity (Sock)
![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_1.jpg)

Entity (Color)

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_2.jpg)

    - Получить список всех сущностей хранимых в БД

GET /sock/

доступен, вывод страниц согласно условиям пагинации и сортировки.

http://localhost:8080/sock?pageSize=5

http://localhost:8080/sock?pageSize=5&pageNo=1

http://localhost:8080/sock?pageSize=5&pageNo=2

http://localhost:8080/sock?pageSize=5&pageNo=1&sortBy=id

http://localhost:8080/sock?pageSize=5&pageNo=1&sortBy=id&sortDir=asc

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_3.jpg)

    - Составной, сложный пользовательский запрос

GET /sock/socks
-  /sock/socks?color=black&operator=lessThan&cottonPart=15 — должен вернуть общее количество черных носков с долей хлопка менее 10%.
-  /sock/socks?color=red&operator=moreThan&cottonPart=90 — должен вернуть общее количество красных носков с долей хлопка более 90%;
и т.д.

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_5.jpg)

Возвращает общее количество носков на складе, соответствующих переданным в параметрах критериям запроса.

    - валидация

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_4.jpg)

    -   Регистрирует приход товара на склад

 POST /sock/
 
![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_6.jpg)

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_7.jpg)

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_8.jpg)

    - Регистрирует уменьшение численности едениц товаров со склада

общее количество товара указанного цвета и состава не увеличивается, а уменьшается.

PATCH /sock/

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_9.jpg)

    - валидация

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_9.1.jpg)

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_9.2.jpg)

    - удалени товара со склада

DELETE /sock/{id}

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_10.jpg)

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_11.jpg)

