#!/bin/bash
cd C:/Users/Nikolay/WebstormProjects/cim-parent-frontend
npm run build
cd C:/Users/Nikolay/IdeaProjects/cim-parent
mvn clean package
docker-compose up --build -d