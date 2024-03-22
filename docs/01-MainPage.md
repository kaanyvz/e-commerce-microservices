# E-Commerce Microservice Application

This application is built using Spring Boot and leverages various technologies such as security, JWT, Elasticsearch, Feign, Vault, Mail Sender, RabbitMQ Docker, API Gateway, Grafana, and Prometheus to provide a robust and scalable e-commerce platform.
Technologies Used

    Spring Boot: Provides the framework for building our microservices efficiently.
    Security: Ensures secure communication and access control within the application.
    JWT (JSON Web Tokens): Used for authentication and authorization.
    Elasticsearch: Enables fast and flexible search capabilities for product data.
    Feign: Simplifies HTTP client creation and integrates seamlessly with other Spring components.
    Vault: Manages secrets and sensitive data securely.
    Mail Sender: Facilitates sending emails for order notifications and user communication.
    RabbitMQ Docker: Enables asynchronous communication between microservices.
    API Gateway: Acts as a single entry point for client requests, providing routing, filtering, and load balancing.
   
    (continues)
    Grafana: Provides visualization and monitoring capabilities for system metrics.
    Prometheus: Collects and stores metrics data, which can be queried and analyzed.

### Services

This app, consists of several microservices:

    User Service: Handles role based authentication, registration, and profile management.
    Document Service: Manages documents such as profile images or product images.
    Product Service: Manages product data, including CRUD operations and search functionalities.
    Order Service: Manages the order lifecycle, including creation, processing, and fulfillment.
    Notification Service: Sends notifications to users via email.
    Rabbit Service: Handles asynchronous communication between microservices using RabbitMQ.

### How It Works

    User Management: Users can register, login, and manage their profiles through the User Service. JWT tokens are used for authentication, and sensitive user data is stored securely using Vault.
    Product Catalog: Product data is stored and managed through the Product Service. Elasticsearch enables fast and efficient search capabilities, allowing users to search and browse products easily.
    Order Processing: Users can create orders through the Order Service. Once an order is placed, notifications are sent to users using the Notification Service. Order processing is done asynchronously using RabbitMQ Docker to ensure scalability and fault tolerance.
    Monitoring and Analytics: Grafana and Prometheus provide monitoring and visualization for system metrics, allowing administrators to track performance, diagnose issues, and optimize resource utilization.

### Getting Started

To run the application locally, follow these steps:

    Clone the repository.
    Navigate to each microservice directory and run mvn spring-boot:run to start the services.
    Ensure all required dependencies are installed and configured, including Docker for RabbitMQ Docker.
    Access the API endpoints through the API Gateway for testing and interaction.

