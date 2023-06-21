# TradeArea
## version 0.1.0 extended
Площадка-агрегатор, где компании выставляют свои предложения по продаже чего-либо.

#  Описание предметной области 
##  Сущности и их отношения
Company - компания, которая выставляет предложения о продаже.

Offer - предложение о продаже чего-либо.

###   Отношения сущностей
Company-Offer - OneToMany

##  Описание свойств сущностей
###   Company
+ id (int64), nonNull Unique - уникальный идентификатор сущности
+ name, nonNull Unique - наименование компании
+ unp, nonNull Unique - унп номер организации
+ email, nonNull - email адрес компании
+ description, nonNull - описание компании
+ created, (date+time) nonNull - дата и время добаление в систему

###  Offer
+ id (int64), nonNull - уникальный идентификатор сущности
+ company_id, nonNull - уникальный идентификатор компании
+ description, nonNull - описание товара
+ phone, nonNull - номер телефона для связи
+ price (int), nonNull - цена за еденицу товара
+ amount (int), nonNull - сколько еще осталось единиц товара
+ updated, (date+time) nonNull - дата и время последнего обновления предложения

#  Описание зависимостей

## Spring boot 
Этот модуль означительно упрощает настройку приложения и позволят автоматически сконфигурировать веб-сервер.

## Spring-boot-starter-data-jpa
Объединяет сразу несколько библиотек и отвечает за взаимодействие программ с базами данных. Этот модуль превращает объекты Java в записи базы данных, позволяет вносить изменения, организовывать быстрый и безопасный доступ к данным

## Spring-boot-starter-web
Подключает библиотеки необходимые для разработки MVC приложения, такие как spring-webmvc и jackson-json.

### Spring MVC
Этот модуль реализует популярную схему веб-приложений — разделение её на три части.
Данный модуль в ключает в себя работу с REST контролерром (прием различных HTTP запросов и отправка ответов).

### jackson-json
Библиотека, которая конвертирует строки JSON и простые объекты Java (англ POJO — Plain Old Java Object). Он также поддерживает многие другие форматы данных, такие как CSV, YML и XML.

## postgresql
Зависимость для подключения PostgreSQL JDBC драйвера.

## liquibase
Модуль позволяющий создавать и поддерживать liqubase миграции.

## spring-boot-maven-plugin
Обеспечивает корректное развертывание Spring boot приложения с использованием Maven.

## liquibase-maven-plugin
Модуль обеспечивающий выполнение liqubase миграций.

## Spring-boot-starter-test
Подключает библиотеки необходимые для разработки unit тестов, такие как Junit и Mockito.

## testcontainers
Библиотека Java, которая поддерживает тесты JUnit, предоставляя легкие одноразовые экземпляры общих баз данных, веб-браузеров Selenium или чего-либо еще, что может работать в контейнере Docker.

## junit-jupiter
Модуль позволяющий организовать unit тестирование приложения.

## assertj
Библиотека которая позволяет гораздо проще и удобнее писать тесты, а также получать достаточно информативный вывод об ошибках.

## jacoco-maven-plugin
Jacoco встраивает агент среды выполнения в JVM, который сканирует пути, пройденные кодом автоматизированных тестов, и создает отчет для этих путей.

#  Инструкция по запуску приложения
1. Открыть терминал(PowerShell или bash-терминал);
2. Перейти в корневую директорию проекта;
3. Выполнить команду **docker compose up**;
4. Дождаться появления строки, вида: **Container tradearea-tradearea Started**;
Внимание, первый запуск контейнера может быть весьма продолжительным!
5. Началось развертывание Spring boot приложения;
Дождаться появления записи, вида: **Started TradeAreaApplication**;
6. Приложение готово к использованию, по адресу **localhost:8080** или **localhost:8000**;
Адрес развертывания приложения можно изменить в файле docker-compose.yml.

#Created by Vsevolod Grinchick
