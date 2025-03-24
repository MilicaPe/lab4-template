#!/bin/bash
set -e

# Логируем запуск
echo "Running initialization scripts..."

# Выполняем SQL-скрипты в нужном порядке
psql -U postgres -f /docker-entrypoint-initdb.d/10-create-user.sql
psql -U postgres -f /docker-entrypoint-initdb.d/20-db-v2.sql
psql -U postgres -f /docker-entrypoint-initdb.d/30-create-table.sql
psql -U postgres -f /docker-entrypoint-initdb.d/40-insert-data.sql

echo "Database initialization complete."
