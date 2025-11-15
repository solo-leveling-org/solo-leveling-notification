# Build stage
FROM maven:3.9.10-amazoncorretto-25-alpine AS build

# Создаём пользователя для безопасности
RUN adduser -D myuser && \
    mkdir -p /usr/src/app && \
    chown myuser:myuser /usr/src/app

WORKDIR /usr/src/app
USER myuser

# Копируем исходники
COPY --chown=myuser:myuser . .

# Собираем проект
RUN mvn clean package -DskipTests --settings settings.xml

# Run stage
FROM amazoncorretto:25-alpine3.21-jdk

# Копируем собранный JAR и frontend
COPY --from=build /usr/src/app/solo-leveling-notification-service/target/*.jar /app/solo-leveling-notification.jar

# Безопасность: создаём пользователя
RUN adduser -D myuser && \
    mkdir -p /app && \
    chown myuser:myuser /app

USER myuser
WORKDIR /app

# Открываем порт
EXPOSE 8080

# Запускаем приложение
CMD ["java", \
    "--enable-native-access=ALL-UNNAMED", \
    "--add-opens", "java.base/java.lang=ALL-UNNAMED", \
     "-Dspring.profiles.active=prod", \
    "-jar", "solo-leveling-notification.jar"]