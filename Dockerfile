FROM maven:3.6.2-jdk-8-slim AS MAVEN_BUILD

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/

RUN mvn package

FROM java:8-jre-alpine

WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/finances-api-0.0.1-SNAPSHOT.jar /app/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "finances-api-0.0.1-SNAPSHOT.jar"]
