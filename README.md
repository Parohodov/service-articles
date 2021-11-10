To run this application the JRE 8+ is needed to be installed.

Deploying (Windows):
Using .bat files:
- clone project
- open project root directory
- run mvn-clean-install.bat - to build project
- run setup-app.bat file to set up application. This command will create and populate ArticleService directory
- to run an application => ArticleService\run.bat
- link to localhost:8080/articles

Handle:
- clone project
- cd project root directory
- make "mvnw.cmd clean install" command from a root project directory
- copy servicearticles-0.0.1-SNAPSHOT.jar, application properties and test-articles directory (optionally) file to a new directory
- make "java -jar servicearticles-0.0.1-SNAPSHOT.jar" from the new directory 
- link to localhost:8080/articles