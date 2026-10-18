# ЛР3 — что создать в UI RabbitMQ

UI: http://localhost:15672 (guest / guest). `GR` — номер группы, `N` — номер в списке.

## Обменники (вкладка Exchanges → Add a new exchange)

| Имя | Type |
|---|---|
| `GR.N.fanout` | fanout |
| `GR.N.direct` | direct |
| `GR.N.topic` | topic |
| `GR.N.headers` | headers |

## Очередь (Queues and Streams → Add a new queue)

`queue.GR.N` (Durable)

## Биндинги (открыть обменник → Add binding from this exchange → To queue `queue.GR.N`)

| Exchange | Routing key | Arguments |
|---|---|---|
| fanout | пусто | — |
| direct | `GR.N.routing.key` | — |
| topic | `GR.*.routing.key` | — |
| headers | пусто | `x-match`=`all`, `group`=`GR`, `number`=`N` |

## Проверка роутинга (обменник → Publish message)

| Exchange | Сообщение 1 — попадает | Сообщение 2 — НЕ попадает |
|---|---|---|
| fanout | routing key пустой | routing key `anything` (тоже попадает — fanout игнорирует ключ) |
| direct | `GR.N.routing.key` | `GR.N.other.key` |
| topic | `GR.N.routing.key` | `GR.N.extra.routing.key` (два слова вместо `*`) |
| headers | headers `group=GR`, `number=N` | только `group=GR` |

После каждой пары открыть `queue.GR.N` → Get messages, чтобы показать, что пришло.
Окно публикации показывает «Message published.» (попало хотя бы в одну очередь) или
«Message published, but not routed.» (никуда не попало).

## Скрины для защиты

1. Список обменников (видны все 4 своих).
2. Список очередей.
3. Bindings у `queue.GR.N` (все 4 связи).
4. Для каждого обменника: отправка двух сообщений и результат в очереди.
5. Логи `notification-service` с `user created: UserCreatedEvent[...]` после `POST /users`.
6. Обменник `user.exchange` и очередь `user.events`, созданные приложением.
