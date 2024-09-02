FROM openjdk:18-jdk-alpine
ENV DATABASE_URL=jdbc:postgresql://172.17.0.2:5432/postgres
ARG JAR_FILE=target/*.jar
RUN mkdir -p /app/log
COPY ${JAR_FILE} /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]