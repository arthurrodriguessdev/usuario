FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew build --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/usuario.jar /app/usuario.jar
EXPOSE 8081
CMD ["java", "-jar", "/app/usuario.jar"]