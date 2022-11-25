# https://docs.spring.io/spring-boot/docs/current/reference/html/container-images.html#container-images
# https://github.com/graalvm/container/pkgs/container/graalvm-ce/47277487?tag=ol9-java17
FROM ghcr.io/graalvm/graalvm-ce:ol9-java17 as builder
WORKDIR app
RUN gu install js

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM ghcr.io/graalvm/graalvm-ce:ol9-java17
WORKDIR app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/app/ ./

# 静态打包资源
COPY front/dist app/static
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
