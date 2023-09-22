# World API Service

World API Service is a project that provides information about countries, cities, populations, and currency conversions. This README.md file serves as documentation for the project.

## Table of Contents

- [Project Overview](#project-overview)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Tests](#tests)
- [Deployment](#deployment)
- [Built With](#built-with)
- [Acknowledgments](#Acknowledgments)

## Project Overview

World API Service is designed to provide easy access to data related to countries and cities. It offers functionalities such as retrieving the most populous cities, details about countries, states, and cities, and currency conversion. This project aims to provide valuable information to developers and users interested in geographical and currency-related data.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) installed (version 11)
- Maven build tool installed (version 3.9.2)
- Spring Boot framework (version 2.7.15)
- Other project-specific dependencies

For detailed instructions on setting up your development environment, refer to the Installation Guide in the Getting Started section.

## Getting Started

To set up and run the project locally, follow these steps:

1. Clone the repository to your local machine:

   ```bash
   git clone https://github.com/mystevotgit/world-api-service.git
   ```
2. Navigate to the project directory:
   ```bash
   cd world-api-service
   ```
3. Build and run the project using Maven:
   ```bash
   mvn spring-boot:run
   ```
## Usage

To use World API Service, follow these guidelines:

- Visit the project's web interface at http://localhost:8080/swagger-ui.html#/ for interactive access to API endpoints.
- Utilize the API endpoints programmatically by sending HTTP requests to the relevant endpoints.
- Use tools like Postman.

## API Endpoints

World API Service offers the following API endpoints:

- `/api/v1/world/most-populated-cities`: Retrieve the most populous cities.
- `/api/v1/world/country/details`: Get details about a specific country.
- `/api/v1/world/states/cities`: Get information about states and their cities.
- `/api/v1/world/currency/convert`: Perform currency conversion.

Refer to the [API Documentation](http://localhost:8080/swagger-ui.html#/) for detailed information on each endpoint, including request and response formats.

## Tests
To run tests for World API Service, follow these steps:

Navigate to the project directory:

   ```bash
   cd world-api-service
   ```

Run tests using Maven:

   ```bash
   mvn test
   ```
Ensure that all tests pass successfully before contributing to the project or deploying it to a production environment.

## Deployment
If you intend to deploy World API Service to a production environment, follow these steps:

1. Set up a production-ready server environment.
2. Configure environment-specific properties and configurations.
3. Build the project with Maven.

## Built With
World API Service is built using the following technologies and frameworks:

- Java
- Spring Boot
- Maven
- RESTful API design
- Mockito
- Junit5
- Swagger2
- swagger-ui
- Hibernate Validator
- Lombok
- Countries & Cities API

## Acknowledgments
It important to acknowledge that this project was built by integrating with Countries & Cities API.
