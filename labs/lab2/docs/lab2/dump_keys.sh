#!/bin/sh
# Выводит все ключи KeyDB с их типом и значением.
# Запуск внутри контейнера:  docker exec -it keydb-lab3 sh   ->   sh /tmp/dump_keys.sh
# (скопировать скрипт в контейнер: docker cp docs/lab2/dump_keys.sh keydb-lab3:/tmp/dump_keys.sh)
#
# Это исправленная версия скрипта из методички: там цикл с CURSOR=0 не выполняется ни разу,
# а `read ... from` не является валидным синтаксисом sh.

CLI="keydb-cli --raw"
CURSOR=0

while :; do
    # shellcheck disable=SC2046
    set -- $($CLI SCAN "$CURSOR")
    CURSOR=$1
    shift

    for key in "$@"; do
        type=$($CLI TYPE "$key")
        echo "Key: $key, Type: $type"
        case "$type" in
            string) $CLI GET "$key";;
            hash)   $CLI HGETALL "$key";;
            list)   $CLI LRANGE "$key" 0 -1;;
            set)    $CLI SMEMBERS "$key";;
            zset)   $CLI ZRANGE "$key" 0 -1 WITHSCORES;;
        esac
        echo
    done

    [ "$CURSOR" = "0" ] && break
done
