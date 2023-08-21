# java-explore-with-me

Ссылка на PR: ...

#### Дополнительный функиционал: ```location_processing```

Жизненный цикл ```Location``` (```LocationStatus```):
* PENDING
* APPROVED
* CANCELLED

1. Администратор имеет возможность добавлять новые локации (статус сразу APPROVED) 
2. Зарегистрированные пользователи могут добавлять свои события с указанием локации:
   - Если локация не была добавлена администратором, она переходит в статус PENDING
   - Вместе с подтверждением события администратором локации выставляется статус APPROVED
   - При отклононении события администратором статус локации CANCELED
3. Публичные пользователи могут просматривать список APPROVED локаций и смотреть списки:
   - Список APPROVED событий в этой локации 
   - Список APPROVED событий в соседних локациях по радиусу 
   - Если в локации не установлен радиус доступна только первая опция

#### Контроллеры
#### Admin
* POST /admin/locations
* UPDATE /admin/locations/{id}
* DELETE /admin/locations/{id}

#### Public
* GET /locations - получение списка всех одобренных локаций
* GET /locations/{id}/events - получение списка событий в локации

Обновленная модель ```Location```:
* ```name```
* ```lat```
* ```lon```
* ```rad``` (если при добавлении события радиус не указан, считается как 0)

При просмотре списка локаций возвращаем DTO ```LocationDtoWithEventsCount```:
* ```name``` Название локации
* ```events``` Количество событий в локации
(если имя локации не указано, не выводим в список)