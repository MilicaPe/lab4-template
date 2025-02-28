FROM bitnami/postgresql:latest

# Копируем скрипты в директорию, откуда PostgreSQL их запустит
COPY scripts/*.sql /docker-entrypoint-initdb.d/
COPY scripts/20-create-databases.sh /docker-entrypoint-initdb.d/
COPY init-db.sh /docker-entrypoint-initdb.d/

# Даем права на выполнение
RUN chmod +x /docker-entrypoint-initdb.d/init-db.sh
