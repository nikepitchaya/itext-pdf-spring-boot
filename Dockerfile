# Use official base image of Java Runtime
FROM openjdk:16-alpine

# Who is MAINTAINER
MAINTAINER Pitchaya Kacharatpaisal, kacharatpaisal.p@gmail.com

# Copy jar file to container
COPY ./target/backend-api.jar backend-api.jar

# Set port
EXPOSE 8080
