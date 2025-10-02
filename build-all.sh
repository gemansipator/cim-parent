#!/bin/bash

# Сборка frontend
cd ./frontend
npm run build

# Сборка backend
cd ../backend
mvn clean package

# Запуск всех сервисов в Docker
cd ..
docker-compose up --build -d