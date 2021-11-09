mvnw.cmd clean install

mkdir ArticleService\articles
copy test-articles\gilmour.zip ArticleService\articles\
copy test-articles\strat.zip ArticleService\articles\
copy test-articles\drz.zip ArticleService\articles\

mkdir ArticleService\test-articles
xcopy test-articles ArticleService\test-articles\ \s

copy target\servicearticles-0.0.1-SNAPSHOT.jar ArticleService
copy src\main\resources\application.properties ArticleService

copy run.bat ArticleService\