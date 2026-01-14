FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/centre-formation-1.0.0-CLEAN.jar app.jar

RUN mkdir -p /app/backups

EXPOSE 8080

CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
