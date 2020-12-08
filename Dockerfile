FROM maven:3.6.3-jdk-11-slim as build-hapi
WORKDIR /tmp/fhir-console

COPY pom.xml .
RUN mvn -ntp dependency:go-offline

COPY src/ /tmp/fhir-console/src/
RUN  mvn clean package spring-boot:repackage -Pboot

FROM openjdk:11-jdk

WORKDIR /app
COPY --from=build-hapi /tmp/fhir-console/target/*.war /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/ROOT.war" ]
