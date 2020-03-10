Build SpringBoot application that returns the results in the console. 
Also the app exposes a REST endpoint that can be hit on /getProducts that
returns JSON response for the result. 



In order to build and run the project:
 
 1) ###  gradlew clean build
 
 2) ###  gradlew test
 
 3) ###  gradlew bootRun


To hit the endpoint : localhost:8080/getProducts


Build and run docker image

1) docker build . --tag sainsburys-scraper:latest
2) docker run sainsburys-scraper
