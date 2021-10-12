FROM openjdk:11
EXPOSE 8080
VOLUME /tmp
ADD target/cityweatherinformation-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar","-web -webAllowOthers -tcp -tcpAllowOthers -browser"]