FROM postgres:latest

# Устанавливаем переменные окружения
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_DB=mydatabase

# Копируем скрипты инициализации
COPY ./scripts /docker-entrypoint-initdb.d/

# Даем права на выполнение всех скриптов
RUN chmod -R 755 /docker-entrypoint-initdb.d/
