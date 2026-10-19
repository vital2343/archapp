# Образ на основе JDK 17
FROM eclipse-temurin:17-jdk

# Кладем jar приложения в корень образа под именем app.jar
COPY binaries/*.jar app.jar

# Порт приложения по умолчанию (можно переопределить переменной окружения при запуске)
ENV SERVER_PORT=8080
EXPOSE 8080

# При старте контейнера запускается приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
