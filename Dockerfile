FROM eclipse-temurin:17-jdk-centos7 as builder
WORKDIR app
COPY front/dist /app/static
COPY target/*.jar app.jar
RUN jar -xf app.jar && rm app.jar
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

# docker build -t mock-data:3.0.0 .
# docker run -d --name mock-data -p 443:443 mock-data:3.0.0
# docker exec -it mock-data bash
