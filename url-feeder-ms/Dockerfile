FROM openjdk:20
ADD build/libs/url-feeder-ms-0.0.1-plain.jar url-feeder-ms.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "url-feeder-ms.jar"]