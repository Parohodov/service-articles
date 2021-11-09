mvnw.cmd clean install
mkdir ArticleService\articles
xcopy test-articles ArticleService\test-articles\
copy servicearticles-0.0.1-SNAPSHOT.jar ArticleService
copy src\main\resources\application.properties ArticleService
java -jar servicearticles-0.0.1-SNAPSHOT.jar
curl localhost:8080