FROM eclipse-temurin:17-jdk
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} notification-service.jar
ENTRYPOINT ["java","-jar","/notification-service.jar"]
