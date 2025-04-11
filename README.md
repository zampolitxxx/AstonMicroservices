### About Project
This training project consists of two microservices. The first one accepts requests from the user at the address and calls the second microservice via OpenFeign. The second microservice puts the data in kafka and retrieves it from kafka. Returns the response to the first microservice, enriching the response with the data of the last request, getting them from the database of the second microservice.

### Environmental requirements
1. Java (21 version)
2. Gradle (8.8)
3. Docker

### Deploying the application.
1. Clone the project to your own computer:
```bash
git clone https://github.com/zampolitxxx/AstonMicroservices.git 
```
2. Go to the root directory
```bash
cd AstonMicroservices
```
3. Generate gradle wrapper:
```bash
gradle wrapper
```
4. Build the application:
```bash
./gradlew :service1:build
./gradlew :service2:build
```

5. Start the application
```bash
docker compose up --build 
```