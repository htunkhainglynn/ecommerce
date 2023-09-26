## Ecommerce

This is a comprehensive documentation for the Ecommerce project, a web application that allows users to buy and manage products, place orders, and receive notifications. The project is built using Spring Boot, Spring Security, Spring Data JPA, Hibernate, RabbitMQ for notifications, Stripe for payment processing, Cloudinary for image storage, GitHub Actions for continuous integration, and Swagger for API documentation.

### TODOs

- [ ] Build frontend with React
- [x] Implement user authentication and authorization
- [x] Add product management functionality
- [x] Configure payment gateway with Stripe
- [x] Create API documentation using Swagger
- [x] Implement push notifications with RabbitMQ
- [x] Implement cron jobs for automatic balance reports
- [x] Set up Docker containerization for the application

### Table of Contents
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Usage](#usage)
    - [Admin Features](#admin-features)
    - [User Features](#user-features)
    - [Dashboard](#dashboard)
    - [Cron Jobs](#cron-jobs)
    - [API Documentation](#api-documentation)
## Technologies Used

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.2-brightgreen) ![Spring Security](https://img.shields.io/badge/Spring%20Security-6.0.0-brightgreen) ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-4.0.0-brightgreen) ![Hibernate](https://img.shields.io/badge/Hibernate-6.2.5-brightgreen) ![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.9.7-brightgreen) ![Stripe](https://img.shields.io/badge/Stripe-Payment%20Gateway-brightgreen) ![Cloudinary](https://img.shields.io/badge/Cloudinary-Image%20Storage-brightgreen) ![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-CI/CD-brightgreen) ![Swagger](https://img.shields.io/badge/Swagger-API%20Documentation-brightgreen) ![MySQL](https://img.shields.io/badge/MySQL-8.0.26-brightgreen) ![Maven](https://img.shields.io/badge/Maven-4.0.0-brightgreen) ![Java](https://img.shields.io/badge/Java-17-brightgreen) ![MongoDB](https://img.shields.io/badge/MongoDB-5.0.3-brightgreen) ![JUnit](https://img.shields.io/badge/JUnit-5.8.0-brightgreen) ![Mockito](https://img.shields.io/badge/Mockito-4.0.0-brightgreen) ![Hamcrest](https://img.shields.io/badge/Hamcrest-2.2-brightgreen)


## Getting Started

#### Clone the Repository

1. Clone the repository from GitHub.

   ```shell
   git clone https://github.com/htunkhainglynn/ecommerce.git
2. Configure your application properties, including database settings, Stripe API keys, Cloudinary credentials, and RabbitMQ configuration.
3. Run the application using `mvn spring-boot:run`.
4. Access the application at `http://localhost:8080`.
5. Login as Admin using admin@gmail.com, password.
6. Login as User using user@gmail.com, password.

#### Using Docker
1. Clone the repository from GitHub.

   ```shell
   git clone https://github.com/htunkhainglynn/ecommerce.git
   ```
2. Create a Docker images using the Dockerfiles.

   ```shell
   cd ecommerce
   cd custom-rabbitmq-image
   docker build -t custom-rabbitmq-image .
   cd ../
   docker build -t ecommerce-image .
   ```
3. Run the docker compose file.

   ```shell
    docker-compose up
    ```
4. Access the application at `http://localhost:8080`.
5. Login as Admin using admin@gmail.com, password.
6. Login as User using user@gmail.com, password.

## Usage

Ensure you have set up the project, and the application is running. Users and admins can access the application and its features as described below:

### Admin Features

- **Product Management:** Create, update, delete, and read products and their variants. Manage product categories.

- **Order Management:** View and manage orders. receive order placement notifications.

- **Notification Management:** Receive order placement notifications.

- **User Management:** View and manage users.

- **Dashboard:** Access the dashboard to view order statistics, user statistics, product information, and purchase price timeline.

### User Features

- **Order Placement:** Create orders, select products, and pay using Stripe.

- **Order History:** View past orders and order details.

- **Notifications:** Receive notifications when products are delivered.

### Dashboard

The dashboard provides a visual overview of key statistics:

- Total orders placed.
- Active user count.
- Total products available.
- Product purchase price timeline to aid in decision-making.

### Cron Jobs

Cron jobs automatically generate balance reports, including income, expenses, and profits, at scheduled intervals.

### API Documentation

API documentation is available using Swagger. Access the documentation at `/swagger-ui.html` when the application is running.
