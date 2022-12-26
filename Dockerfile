FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean 
RUN mvn -f /home/app/pom.xml package
RUN mvn -f /home/app/pom.xml install assembly:assembly

FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/ontoseer-1.0.jar /usr/local/lib/ontoseer-1.0.jar
COPY --from=build /home/app/target/ontoseer-1.0-jar-with-dependencies.jar /usr/local/lib/ontoseer-1.0-jar-with-dependencies.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/ontoseer-1.0-jar-with-dependencies.jar"]
