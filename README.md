# Search GitHub projects by language.

### Steps to run this project
Checkout/download this maven project from github (https://github.com/sanjaykushwaha83/informaticatest.git)

1. Open command prompt and go to project checkout/download directory 
2. Clean and build this Maven project using below command
   mvn clean install
3. Run project using below command
   mvn spring-boot:run
4. Go to browser and open swagger url: http://localhost:8888/informatica-test/swagger-ui.html
5. Once the swagger url opens, click on "/GET/ /githubprojects/search/{language}" and then click on "try out" and input language and click on the execute button. Now you will be able to see a list of projects in response.