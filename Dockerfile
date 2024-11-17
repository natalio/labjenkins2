FROM openjdk:17
EXPOSE 8080
ADD target/course-0.0.1-SNAPSHOT.jar course-0.0.1.jar
ENTRYPOINT ["java","-jar", "course-0.0.1.jar"]