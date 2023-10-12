FROM maven:3.5-jdk-8-alpine as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY target /app/


# Run the web service on container startup.
CMD ["java","-jar","/app/target/emonapi-backend-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]