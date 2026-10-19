#!/bin/sh
# Собирает jar всех модулей внутри контейнера Maven (JDK и Maven на компе не нужны).
# Результат: <модуль>/target/*.jar — их потом берут Dockerfile'ы.
cd "$(dirname "$0")"
docker run --rm -v "$(pwd)":/app -v archapp-m2:/root/.m2 -w /app \
    maven:3.9-eclipse-temurin-17 mvn -B clean package -DskipTests
