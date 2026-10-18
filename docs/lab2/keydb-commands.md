# ЛР2 — команды keydb-cli

Подставь вместо `GR` номер группы, вместо `N` — номер в списке (например `student:5:12`).

```bash
docker compose -f dockercompose/docker-compose.yaml up -d keydb
docker exec -it keydb-lab3 keydb-cli --raw
```

## 1. Создание данных (п. 2 задания)

```
SET student:GR:N "Фамилия Имя Отчество"

HSET student:GR:N:info name "Фамилия Имя Отчество" age 20 email "mXXXXXXX@edu.misis.ru"

RPUSH student:GR:N:timetable "Архитектурирование" "Базы данных" "Физкультура"

SADD student:GR:N:skills Docker Java Python Git

ZADD student:GR:N:tasks_w_priority 100 "Сделать лабу 1" 150 "Сделать лабу 2" 200 "Сделать курсовую"
```

Затем выйти из cli и выполнить `docs/lab2/dump_keys.sh` — его вывод сохранить (скрин).

## 2. Вопросы к защите 7–11 (команды и асимптотика)

| Тип | Что сделать | Команды | Сложность |
|---|---|---|---|
| STRING | получить / заменить | `GET student:GR:N`, `SET student:GR:N "Другое"` | O(1) |
| HASH | все поля / поле name / поменять name | `HGETALL ...:info`, `HGET ...:info name`, `HSET ...:info name "Другое"` | O(N), O(1), O(1) |
| LIST | в начало / все / первое / удалить | `LPUSH ...:timetable "Новая"`, `LRANGE ...:timetable 0 -1`, `LRANGE ...:timetable 0 0` (или `LINDEX ... 0`), `LPOP ...:timetable` / `LREM ...:timetable 1 "Новая"` | O(1), O(N), O(1), O(1) / O(N) |
| SET | все / добавить / дубль / удалить | `SMEMBERS ...:skills`, `SADD ...:skills Redis` → 1, `SADD ...:skills Redis` → 0, `SREM ...:skills Redis` | O(N), O(1), O(1), O(1) |
| ZSET | все с весами / max / добавить с 0 / вес 100 / удалить | `ZRANGE ... 0 -1 WITHSCORES`, `ZRANGE ... -1 -1 WITHSCORES`, `ZADD ... 0 "Новая"`, `ZINCRBY ... 100 "Новая"`, `ZREM ... "Новая"` | O(log N + M), O(log N), O(log N), O(log N), O(log N) |

## 3. TTL

```
EXPIRE student:GR:N 60     # поставить TTL 60 c
TTL student:GR:N           # сколько осталось (-1 — бессрочно, -2 — ключа нет)
PERSIST student:GR:N       # снять TTL
SET tmp "x" EX 30          # сразу с TTL
```

## 4. Проверка cache-aside в приложении

1. `GET /users/{id}` первый раз → в логе `User cache miss`, в KeyDB появился ключ `user:v1:{id}` (`KEYS user:v1:*`, `TTL user:v1:{id}`).
2. Второй раз → `User cache hit`.
3. `PATCH /users/{id}` или `DELETE /users/{id}` → `User cache evict on update/delete`, ключ пропал.
4. Через 60 секунд без запросов ключ исчезает сам (TTL).
