FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/cim-parent-1.0-SNAPSHOT.jar /app/cim-parent.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/cim-parent.jar"]