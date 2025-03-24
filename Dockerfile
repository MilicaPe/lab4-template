# Используем официальный образ PostgreSQL
FROM postgres:13

# Устанавливаем переменные окружения для пользователя, пароля и базы данных
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres

# Копируем все файлы из папки postgres в контейнер
# Этот путь указывает, что все скрипты, включая db-v1.sql, init-db.sh и другие, будут скопированы
COPY postgres /docker-entrypoint-initdb.d/

EXPOSE 5432