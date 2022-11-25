# https://docs.spring.io/spring-boot/docs/current/reference/html/container-images.html#container-images
# https://github.com/graalvm/container/pkgs/container/graalvm-ce/47277487?tag=ol9-java17
FROM springci/graalvm-ce:java17-0.12.x as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM springci/graalvm-ce:java17-0.12.x
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
COPY front/dist /root/app/mock-data/front/dist
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
