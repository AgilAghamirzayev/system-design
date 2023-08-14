FROM openjdk:20
ADD build/libs/html-file-worker-0.0.1-plain.jar html-file-worker.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "html-file-worker.jar"]