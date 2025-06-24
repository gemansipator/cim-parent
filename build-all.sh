#!/bin/bash

# Сборка frontend
cd C:/Users/Nikolay/WebstormProjects/cim-parent-frontend
npm run build

# ⚠️ НЕ запускаем rollup для ifc-viewer — всё работает из example напрямую

# Сборка Spring Boot backend
cd C:/Users/Nikolay/IdeaProjects/cim-parent
mvn clean package

# Запуск всех сервисов в Docker
docker-compose up --build -d
