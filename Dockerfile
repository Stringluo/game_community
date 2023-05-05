FROM openjdk:11
COPY target/game_community_java.jar game_community_java.jar
EXPOSE 10041
ENTRYPOINT ["java", "-jar", "game_community_java.jar"]