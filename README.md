# TradeArea
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
