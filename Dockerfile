FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY build/libs/usuario-0.0.1-SNAPSHOT.jar /app/usuario.jar
EXPOSE 8081
CMD ["java", "-jar", "/app/usuario.jar"]