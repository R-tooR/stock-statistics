FROM openjdk:23-ea-21-jdk-slim

WORKDIR /app


COPY build/libs/stock-statistics-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar", "-Xmx4096m", "-Xms256m", "-XX:+ZGenerational"]
#CMD ["java", "-jar", "app.jar"]
