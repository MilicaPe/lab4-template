FROM maven:3.8.6-eclipse-temurin-17 as builder
WORKDIR /opt/identity-provider
COPY mvnw pom.xml ./
COPY ./src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /opt/identity-provider
EXPOSE 8040
COPY --from=builder /opt/identity-provider/target/*.jar /opt/identity-provider/*.jar
ENTRYPOINT ["java", "-jar", "/opt/identity-provider/*.jar"]

