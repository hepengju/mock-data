FROM eclipse-temurin:17-jdk-centos7
MAINTAINER he_pengju@163.com
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo "Asia/Shanghai" > /etc/timezone

WORKDIR app
COPY front/dist            /app/static
COPY docs/hepengju.com.jks /app/cert/hepengju.com.jks
COPY target/*.jar          /app/app.jar

EXPOSE 443
EXPOSE 80

ENTRYPOINT ["/bin/sh","-c","java ${JAVA_OPTS} -jar /app/app.jar"]

# docker build -t mock-data:3.3 .
# docker tag mock-data:3.3 hepengju/mock-data:3.3
# docker push hepengju/mock-data:3.3
# docker run -d --name mockdata33 -p 443:443 -p 80:80 -e "JAVA_OPTS=-Xms250M -Xmx800M" --restart=always hepengju/mock-data:3.3
# docker exec -it mockdata33 bash
