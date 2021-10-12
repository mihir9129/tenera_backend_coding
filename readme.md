Project Name: City Weather Information

Description: This project provides the information for the current as well as historical weather information 
for the given city.

Technologies used for the project:
IDE - Intellij
JDK - 11
H2 - In memory database
   The reason of selecting the in memory database is the ease to start the application without setting up any 
   database in local without wasting much time.
   Url to access it once the application is started - http://localhost:8080/h2
   All the information related to the h2 database is present in the application.properties file.
Build tool - Maven
We keep all the dependency in the pom.xml file which is used in the entire project.
Application Development Framework - Spring boot
Server - tomcat (embedded)
Test case framework - junit and Mockito

Steps to start the application:
1. We would require JDK 11, for running the application.
2. Take the clone from the git repository - https://github.com/mihir9129/tenera_backend_coding (Master branch)
   in Intellij IDE.
3. Select the pom.xml and open it as the project.
4. Let all the dependencies to be downloaded from the maven repo.   
5. The starting file is CityWeatherInformationApplication.java, which contains the main method, where we can 
   start the application just by clicking the run button in the main method.

Once the application is started, it would automatically start with the port number 8080.
We would be able to access all the apis by using the swagger-ui using the URL - 
http://localhost:8080/swagger-ui.html.
The reason for creating a swagger ui is the ease to access the APIs present in the application, and get all the 
APIs in the same file.

The Apis are as follows:
1. GET - http://localhost:8080/api/v1/weather-information/current?location={cityName}
   Based on the provided city name, we get the current temperature in celsius, pressure in Hpa and check 
   whether the umbrella is required or not.
   Example GET request - http://localhost:8080/api/v1/weather-information/current?location=london
   It would fetch the required records from the third party api - https://api.openweathermap.org/
   The response format for the same is mentioned in CurrentWeatherInformation.java file.
   It would also store the corresponding data in the H2 database, which can be accessed through
   H2 database URL link - http://localhost:8080/h2
   Database name - weather_information_db
   username - test
   password - test.
   This information is present in weather_historical_data table, which can be accessed through simple SQL query.

2. GET - http://localhost:8080/api/v1/weather-information/history?location={cityName}
   Based on the data stored for the same city name in the /current API (mentioned above), we provide the 
   average temperature and pressure of the last 5 most recent records, along with providing the details of the last
   five records stored in the database.
   The response format for the same is provided in the HistoricalWeatherInformation.java file.
   
Project Architecture:
   Here, the entire project is developed using the Spring boot based microservice, which has an embedded tomcat
   on which entire application would be running, and don't require any special server requirements.
   Also, while developing the project, I have taken care of basic SOLID principles, where each class has a 
   single responsibility and loosely coupled among others.
   
Test cases:
   Here, I have developed both the unit and controller API test cases covering all the basics scenarios,
   which are present in the test package. 
   Unit test cases are present in the file - CityWeatherInformationUnitTests.java
   Controller test cases are present in the file - CityWeatherInformationControllerTests.java
   I have used the mockito and junit to cover the basic test cases of almost entire application.

Deployment:
   As I have developed an individual microservice, so we can do the deployment of the same using the containers 
   based approach.
   The tools and technologies used for the deployment are docker, kubernetes, AWS and terraform.
   The entire process description of deployment is as follows:
1. Firstly, we would create a kubernetes cluster using the Terraform cloud service.
2. Build a docker image of the application and push that image to the Docker hub repository. I have created a 
   sample dockerfile in this application, which would be executed using the docker build command and we can 
   setup an latest image based container for the same. It is just for reference purpose for now.
3. We would be deploying the docker image container to the kubernetes engine, and setup a terraform kubernetes
   deployment using the Terraform kubernetes provider.
4. For this setup, we would need to create multiple terraform tf files (like providers.tf, variable.tf, 
   main.tf (starting file) etc.) which would help us in defining, creating and configuring our application 
   on the kubernetes cluster.
5. Once the setup is done, we need to create a CI/CD based automatic or manual deployments, which can be 
   configured in jenkins, so that whenever any code merge is completed, we can do the deployments of the same
   continously and automatically as per the requirements.
6. It's a good step to destroy all unwanted resources created using Terraform.

Note: This is just a basic idea about the entire deployment, there might be some changes in the real deployment, 
   as I am not much used to Terraform based deployment.

Future scope for production ready development:
   Although I tried to cover many scenarios in the development, but due to time constraints, not able to cover 
   some of the topics which would be good for the production level deployment and development. Some of the 
   additional things which we can cover here are as follows:
1. Add the proper files and setup for the actual deployment to the cloud service using docker and kubernetes 
   engine
2. Add a generic exception handler, with the ControllerAdvice, which would help us in covering many failure
   scenarios effectively.
3. Currently, in application.properties, we have provided the appid key directly, which should be encrypted
   in the real application.
4. Need to develop a performance testing of the entire application, and check the load of the application, so 
   that we can monitor it properly in case of high volume of data. 
   One of the tool to maintain it properly is jmeter.
5. Currently, I am using an in memory database, it would be better to replace with the external database, so 
   that the persistence storage gets effective and can handle large volumes of data. The code structure
   would remain same, only the database would be changed.
6. It is better to add the spring security using OAuth2, so that we can secure our application easily. 
   
