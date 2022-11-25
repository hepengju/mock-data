# https://docs.spring.io/spring-boot/docs/current/reference/html/container-images.html#container-images
# https://github.com/graalvm/container/pkgs/container/graalvm-ce/47277487?tag=ol9-java17
FROM ghcr.io/graalvm/graalvm-ce:ol9-java17 as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
COPY front/dist front/dist
RUN java -Djarmode=layertools -jar application.jar extract

FROM ghcr.io/graalvm/graalvm-ce:ol9-java17
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
