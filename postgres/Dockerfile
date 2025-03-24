# Используем официальный образ PostgreSQL
FROM postgres:latest

# Устанавливаем переменные окружения для пользователя, пароля и базы данных
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_DB=mydatabase

# Копируем все файлы из папки postgres в контейнер
# Этот путь указывает, что все скрипты, включая db-v1.sql, init-db.sh и другие, будут скопированы
COPY postgres /docker-entrypoint-initdb.d/

# Даем права на выполнение всех скриптов в папке /docker-entrypoint-initdb.d/
RUN chmod -R 755 /docker-entrypoint-initdb.d/

# Для скриптов на bash (например, init-db.sh), которые могут потребовать выполнения
RUN apt-get update && apt-get install -y bash

# Указываем, что контейнер будет использовать PostgreSQL по умолчанию
CMD ["postgres"]
