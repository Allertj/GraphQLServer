FROM gradle:7.5.0-jdk18-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:18-alpine

EXPOSE ${INTERNALBACKEND}

RUN mkdir /app

COPY .env /.env
COPY --from=build /home/gradle/src/build/libs/graphqlserver-0.0.1-SNAPSHOT.jar /app/spring-boot-application.jar

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/spring-boot-application.jar"]