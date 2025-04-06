# Use a base image with Java 17
FROM amazoncorretto:17.0.6-alpine

VOLUME /tmp

# Copy the built jar into the container
COPY target/*-exec.jar app.jar

# Expose the port your Spring Boot app runs on (default is 8080)
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
