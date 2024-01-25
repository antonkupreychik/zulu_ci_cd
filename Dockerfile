FROM openjdk:17

WORKDIR /
COPY target/zulu_ci_cd-1.0-SNAPSHOT.jar zulu_ci_cd-1.0-SNAPSHOT.jar
ENV PORT=8080
EXPOSE 8080
CMD ["java","-jar","zulu_ci_cd-1.0-SNAPSHOT.jar"]
