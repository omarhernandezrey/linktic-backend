FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . .
RUN mvn -pl inventory-service -am -q -DskipTests clean package spring-boot:repackage

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/inventory-service/target/*inventory-service*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]
