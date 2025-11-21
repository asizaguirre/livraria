# 📚 Livraria: Modern Library Management System

## Overview

The `Livraria` project is a comprehensive and modern library management system, meticulously designed with a focus on robust architecture, clean code, and best practices. It provides a seamless experience for managing books, authors, and subjects through a powerful backend API and an intuitive Angular frontend. The entire system is containerized using Docker, ensuring easy setup, consistent deployment, and scalability.

This system embodies a commitment to high-quality software development, utilizing a microservices-inspired approach to deliver a functional, maintainable, and extensible solution.

## 🚀 Architecture

The system follows a decoupled, microservices-inspired architecture, composed of a robust frontend, a powerful backend API, and a dedicated PostgreSQL database, all orchestrated via Docker Compose and exposed through Nginx. This design promotes modularity, scalability, and ease of maintenance.

### Components and Technologies

*   **Frontend (Web Application)**
    *   **Framework:** Angular v21.0.0
    *   **Language:** TypeScript
    *   **Styling:** Bootstrap v5.3.8 for a responsive and modern user interface, complemented by SCSS for custom styling.
    *   **User Experience Enhancements:** `ngx-mask` v20.0.3 for intuitive input masking.
    *   **Server-Side Rendering (SSR):** Implemented using Angular SSR to enhance performance, improve SEO, and provide a faster initial load experience.
    *   **Deployment:** Packaged into a lightweight Docker image and served efficiently by Nginx.

*   **Backend (RESTful API)**
    *   **Framework:** Spring Boot v3.2.0, providing a solid foundation for enterprise-grade applications.
    *   **Language:** Java 17, leveraging modern language features for clean and efficient code.
    *   **Build Tool:** Maven, managing project dependencies and the build lifecycle.
    *   **Data Persistence:** Spring Data JPA with Hibernate for robust Object-Relational Mapping (ORM), facilitating seamless interaction with the database.
    *   **Database Migrations:** Flyway for reliable and version-controlled database schema evolution, ensuring consistency across environments.
    *   **API Documentation:** Springdoc OpenAPI (integrated with Swagger UI) provides interactive documentation for all API endpoints, simplifying development and integration.
    *   **Object Mapping:** MapStruct for high-performance and type-safe Data Transfer Object (DTO) to Entity mapping, minimizing boilerplate code.
    *   **Reporting:** JasperReports for dynamic and customizable report generation (e.g., book listings by author), fulfilling complex business intelligence needs.
    *   **Validation:** Jakarta Bean Validation ensures data integrity and consistency across all API requests.
    *   **Code Quality & Productivity:** Lombok is used to reduce boilerplate code (e.g., getters, setters, constructors), promoting cleaner and more readable classes.
    *   **Development Tools:** Spring Boot DevTools for hot reloading and faster feedback cycles during development.

*   **Database**
    *   **Type:** PostgreSQL v13-alpine, a powerful, open-source relational database, chosen for its reliability and advanced features.
    *   **Purpose:** The central repository for all application data, including books, authors, and subjects.

*   **Reverse Proxy / Web Server**
    *   **Type:** Nginx (alpine), a high-performance web server and reverse proxy.
    *   **Purpose:** Efficiently serves the static assets of the Angular frontend. It manages URL routing, ensuring that all client-side routes are correctly handled by the `index.html` file (Single Page Application routing).

*   **Containerization & Orchestration**
    *   **Tool:** Docker & Docker Compose
    *   **Purpose:** Each application component (frontend, backend, database) is encapsulated within its own Docker container, ensuring environmental consistency from development to production. Docker Compose orchestrates these containers, managing their lifecycle, network configurations, and inter-communication, simplifying the deployment of the entire multi-service application.

### Communication Flow

1.  **User Access:** Users interact with the application through their web browser, which connects to the Nginx web server. Nginx efficiently serves the Angular frontend's static files.
2.  **Frontend-Backend Interaction:** The Angular frontend makes asynchronous (AJAX) calls to the Spring Boot backend API to retrieve, create, update, and delete data (e.g., fetching a list of books, adding a new author).
3.  **Backend-Database Interaction:** The Spring Boot backend processes these API requests, interacting with the PostgreSQL database via Spring Data JPA to perform necessary data operations.
4.  **Internal Networking:** Within the Docker Compose environment, services communicate securely over an internal Docker network. The frontend, when making API calls, addresses the backend service using its Docker service name (`api`).

## ⚙️ Setup and Running

This project leverages Docker for containerization and Docker Compose for orchestration, providing a consistent development and deployment environment. You can run the entire application stack with a single command, or set up individual components locally for development.

### Prerequisites

Ensure you have the following installed on your system:

*   **Git:** For cloning the repository.
*   **Docker & Docker Compose:** Essential for running the application stack in containers.
    *   [Install Docker Engine](https://docs.docker.com/engine/install/)
    *   [Install Docker Compose](https://docs.docker.com/compose/install/)
*   **Java Development Kit (JDK) 17 or higher:** Required for local backend development.
    *   [Download JDK](https://adoptium.net/temurin/releases/)
*   **Maven:** Build tool for the Java backend.
    *   [Install Maven](https://maven.apache.org/install.html)
*   **Node.js (LTS version, e.g., 20.x) & npm:** Required for local frontend development.
    *   [Download Node.js](https://nodejs.org/en/download/)
*   **Angular CLI:** Command-line interface for Angular projects.
    ```bash
    npm install -g @angular/cli
    ```

### Running with Docker Compose (Recommended)

This method builds and runs the entire application stack (PostgreSQL database, Spring Boot API, and Nginx-served Angular frontend) using Docker containers, providing the most consistent environment.

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/asizaguirre/livraria.git
    cd livraria
    ```
2.  **Build the Spring Boot Backend JAR:**
    The `api` service in `docker-compose.yml` expects a pre-built JAR file. Navigate to the project root and build the backend:
    ```bash
    mvn clean install -DskipTests
    ```
    This command will create the `biblioteca-0.0.1-SNAPSHOT.jar` (or similar) in the `target/` directory.

3.  **Build and Start the Docker Compose Stack:**
    From the project root directory where `docker-compose.yml` is located:
    ```bash
    docker-compose up --build -d
    ```
    *   `--build`: Rebuilds images if changes have been made to Dockerfiles or contexts.
    *   `-d`: Runs containers in detached mode (in the background).

4.  **Verify Application Status:**
    Check the status of running containers:
    ```bash
    docker-compose ps
    ```
    Ensure all services (`db`, `api`, `web`) are healthy and running.

5.  **Access the Application:**
    *   **Frontend:** Open your web browser and navigate to `http://localhost:4200`
    *   **Backend API (Swagger UI):** Access the API documentation at `http://localhost:8080/swagger-ui/index.html`

6.  **Stop the Application:**
    To stop and remove all containers, networks, and volumes created by `docker-compose up`:
    ```bash
    docker-compose down -v
    ```
    *   `-v`: Removes named volumes declared in the `volumes` section of the `docker-compose.yml` file (e.g., `db_data`), which is useful for a clean database slate, though use with caution as it deletes all persistent data.

### Running Components Locally (Development Mode)

This section details how to run the frontend and backend services independently without Docker Compose, which can be useful for focused development and debugging.

#### 1. Start the PostgreSQL Database (via Docker)

It's highly recommended to run the database in Docker even for local development to maintain consistency with the production environment and simplify setup.

```bash
docker-compose up -d db
```
This will start only the `db` service. You can stop it with `docker-compose down db`.

#### 2. Run the Spring Boot Backend Locally

1.  **Ensure database is running** (either locally or via Docker as above).
2.  **Navigate to the backend project root:**
    ```bash
    cd /home/alam/Área de trabalho/Workspace/Livraria/ # (or the directory containing pom.xml)
    ```
3.  **Build and run the application:**
    ```bash
    mvn spring-boot:run
    ```
    The Spring Boot application will start and be accessible at `http://localhost:8080`.

#### 3. Run the Angular Frontend Locally

1.  **Navigate to the frontend project directory:**
    ```bash
    cd biblioteca-web
    ```
2.  **Install dependencies (if not already done):**
    ```bash
    npm install
    ```
3.  **Start the Angular development server:**
    ```bash
    ng serve
    ```
    The Angular development server will launch, and the application will be accessible at `http://localhost:4200`.

## 🧪 Testing

This project adheres to high quality standards through a comprehensive testing strategy for both its backend API and frontend application. Automated tests ensure the reliability, correctness, and maintainability of the codebase.

### Backend Testing

The Spring Boot backend utilizes a multi-layered testing approach, including unit, integration, and repository tests, leveraging standard Java testing frameworks. This ensures thorough validation of business logic, data access, and API endpoints.

*   **Frameworks:** JUnit 5 (Jupiter) for writing tests, Mockito for mocking dependencies, and Spring Boot Test for integration testing with the Spring context.
*   **Tools:** JaCoCo for comprehensive code coverage analysis, providing insights into test effectiveness.

#### How to Run Backend Tests

Navigate to the project root directory (containing `pom.xml`):

1.  **Run all tests (unit and integration):**
    ```bash
    mvn test
    ```
    This command executes all tests found in the project.
2.  **Run tests and generate JaCoCo code coverage report:**
    ```bash
    mvn clean verify
    ```
    After execution, a detailed JaCoCo report will be generated. You can view it by opening `target/site/jacoco/index.html` in your web browser. This report highlights lines and branches covered by tests.
3.  **Skip tests during build (e.g., for faster Docker image creation or deployment):**
    ```bash
    mvn clean install -DskipTests
    ```

### Frontend Testing

The Angular frontend is equipped with a modern testing setup to ensure component reliability, user interface integrity, and overall application functionality.

*   **Framework:** Vitest, a fast and modern testing framework, is utilized (as indicated by `package.json`).
*   **Tools:** Vitest typically integrates with environments like JSDOM to simulate a browser environment in Node.js, allowing for efficient component and service testing without a full browser.

#### How to Run Frontend Tests

Navigate to the `biblioteca-web` directory:

```bash
cd biblioteca-web
```

1.  **Run all frontend tests:**
    ```bash
    npm test
    ```
    This command executes the tests configured in the `test` script of `package.json`, typically launching Vitest in watch mode, providing immediate feedback on code changes.

## 🤝 Contributing

(Optional section: Add guidelines for contributing to the project, e.g., coding standards, pull request process, etc.)

## 📄 License

(Optional section: Specify the project's licensing information.)
