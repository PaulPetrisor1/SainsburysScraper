In order to build and run the project:
 
 1) ###./gradlew clean build
 
 2) ### ./gradlew test
 
 3) ### ./gradlew bootRun

The results will be printed in the console as well as on /getProducts endpoint which returns json.

To hit the endpoint : localhost:8080/getProducts


Build docker image

1) docker build . --tag sainsburys-scraper:latest
2) docker run sainsburys-scraper
