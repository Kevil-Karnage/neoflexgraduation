FROM eclipse-temurin:17-jdk-alpine
#ARG JAR_FILE=out/artifacts/neoflexgraduation_AccountService_jar/neoflexgraduation.AccountService.jar

WORKDIR /app
COPY build/libs/AccountService-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8585
ENTRYPOINT ["java","-jar","app.jar"]