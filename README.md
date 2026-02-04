# API Test Automation (RestAssured + JUnit + DDT)

## Project description
This project contains automated API tests built with:
- Java
- JUnit 5
- RestAssured
- Data Driven Testing (CSV)

## API Source (Swagger)
https://petstore.swagger.io

## Project structure
src  
└── test  
  ├── java  
  │  ├── SmokeApiTest.java  
  │  └── DdtApiTest.java  
  └── resources  
    └── testdata  
      └── requests.csv

## How to run tests
```bash
mvn test
