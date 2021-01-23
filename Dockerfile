FROM openjdk:8
ADD target/user.jar user.jar
ENTRYPOINT ["java","-jar","/user.jar"]