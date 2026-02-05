FROM maven:3.9-eclipse-temurin-21-jammy AS builder
WORKDIR /app

COPY . .

RUN mvn dependency:go-offline  -B -e -X

RUN mvn clean package -DskipTests -pl payment-service-app -am

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/payment-service-app/target/payment-service-app-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
