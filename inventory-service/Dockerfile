FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . .
RUN mvn -pl inventory-service -am -q -DskipTests clean package spring-boot:repackage

FROM eclipse-temurin:21-jre
RUN apt-get update && apt-get install -y --no-install-recommends curl \
	&& rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /workspace/inventory-service/target/*inventory-service*.jar app.jar
EXPOSE 8082
HEALTHCHECK --interval=10s --timeout=3s --retries=10 CMD curl -fsS http://localhost:8082/actuator/health || exit 1
ENTRYPOINT ["java","-jar","/app/app.jar"]
