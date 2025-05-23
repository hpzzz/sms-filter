FROM openjdk:23-jdk-slim AS builder
RUN apt-get update && apt-get install -y wget unzip
RUN wget https://services.gradle.org/distributions/gradle-8.10-bin.zip && \
    unzip gradle-8.10-bin.zip -d /opt && \
    ln -s /opt/gradle-8.10/bin/gradle /usr/bin/gradle

COPY . /app
WORKDIR /app
RUN gradle bootJar --no-daemon

FROM openjdk:23-jdk-slim
RUN ls 
# will fail if multiple jars i guess?
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]