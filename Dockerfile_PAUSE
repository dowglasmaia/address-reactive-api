FROM amazoncorretto:17.0.7-alpine

WORKDIR /app

COPY target/*.jar /app/address-api.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "address-api.jar" ]