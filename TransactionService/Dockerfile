FROM eclipse-temurin:17-jdk-alpine
#ARG JAR_FILE=out/artifacts/neoflexgraduation_TransactionService_jar/neoflexgraduation.TransactionService.jar

WORKDIR /app
COPY build/libs/TransactionService-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]