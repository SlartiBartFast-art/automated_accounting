## Учет непродовольственных товаров на складе

[![Build Status](https://app.travis-ci.com/SlartiBartFast-art/automated_accounting.svg?branch=master)](https://app.travis-ci.com/SlartiBartFast-art/automated_accounting)
![GitHub top language](https://img.shields.io/github/languages/top/SlartiBartFast-art/automated_accounting?logo=java&logoColor=red&style=plastic)
![GitHub last commit](https://img.shields.io/github/last-commit/SlartiBartFast-art/automated_accounting?logo=github)

app is under development

Приложение для автоматизации учёта товаров на складе магазина. 

Приложение использует REST API архитектуру
развернуто в облаке(Heroku)
   - https://automated-accounting.herokuapp.com/

Used technology stack:
______________________________________________
- Java Core
- JWT
- Spring Boot (Web, Data JPA, Security) 
- Liquibase  
- Maven
- Travis C.I.
- Postman - (Postman is an API platform for building and using API)
- Docker

Что необходимо сделать:

Реализовать приложение для автоматизации учёта носков на складе магазина.

Кладовщик должен иметь возможность:
_____________
-  учесть приход и отпуск носков;
-  узнать общее количество носков определенного цвета и состава в данный момент времени.
Внешний интерфейс приложения представлен в виде HTTP API (REST, если хочется).

Список URL HTTP-методов
________

 POST /api/

 Регистрирует приход носков на склад.

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_2.jpg)

Параметры запроса передаются в теле запроса в виде JSON-объекта со следующими атрибутами:

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_3.jpg)

-  color Object — цвет носков (например, black, red, yellow);
-  cottonPart — процентное содержание хлопка в составе носков, целое число от 1 до 100 (например, 30, 18, 42);
-  quantity — количество пар носков, целое число, 1 и больше.

Результаты:

-  HTTP 201 — удалось добавить приход;
-  HTTP 400 — параметры запроса отсутствуют или имеют некорректный формат;
-  HTTP 500 — произошла ошибка, не зависящая от вызывающей стороны (например, база данных недоступна).

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_4.jpg)

POST /api/outcome

Регистрирует отпуск носков со склада. Здесь параметры и результаты аналогичные,
но общее количество носков указанного цвета и состава не увеличивается, а уменьшается.

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_5.jpg)

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_6.jpg)

GET /api/socks

Возвращает общее количество носков на складе, соответствующих переданным в параметрах критериям запроса.

Параметры запроса передаются в URL:

-  color — цвет носков, строка;
-  operation — оператор сравнения значения количества хлопка в составе носков, одно значение из: moreThan, lessThan, equal;
-  cottonPart — значение процента хлопка в составе носков из сравнения.

Результаты:

-  HTTP 200 — запрос выполнен, результат в теле ответа в виде строкового представления целого числа; 
-  HTTP 400 — параметры запроса отсутствуют или имеют некорректный формат;
-  HTTP 500 — произошла ошибка, не зависящая от вызывающей стороны (например, база данных недоступна).

Примеры запросов:

/api/socksAll

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_1.jpg)

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_7.jpg)

-  /api/socks?color=red&operator=moreThan&cottonPart=90 — должен вернуть общее количество красных носков с долей хлопка более 90%;

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_8.jpg)

-  /api/socks?color=black&operator=lessThan&cottonPart=15 — должен вернуть общее количество черных носков с долей хлопка менее 10%.

![Image of Arch](https://github.com/SlartiBartFast-art/automated_accounting/blob/master/image/Screenshot_9.jpg)

Для хранения данных системы можно использовать любую реляционную базу данных. Схему БД желательно хранить в репозитории в любом удобном виде.

Как это сделать:

-  написано на языке Java; 
-  standalone - состоять из одного выполняемого компонента верхнего уровня;
-  headless - без UI; 
-  оформлено как репозиторий.
   
Будет плюсом, если:

-  приложение будет основано на Spring(Boot) Framework;
-  для версионирования схемы базы данных будет использоваться Liquibase или Flyway;
-  приложение будет развернуто на любом облачном сервисе, например Heroku, и его API будет доступно для вызова.
-  база данных будет подниматься рядом с приложением в докер-контейнере;