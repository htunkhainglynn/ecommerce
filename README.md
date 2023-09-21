## Project Documentation

This is a comprehensive documentation for the Ecommerce project, a web application that allows users to buy and manage products, place orders, and receive notifications. The project is built using Spring Boot, Spring Security, Spring Data JPA, Hibernate, RabbitMQ for notifications, Stripe for payment processing, Cloudinary for image storage, GitHub Actions for continuous integration, and Swagger for API documentation.

### Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Usage](#usage)
    - [Admin Features](#admin-features)
    - [User Features](#user-features)
    - [Dashboard](#dashboard)
    - [Cron Jobs](#cron-jobs)
    - [API Documentation](#api-documentation)

## Features

### Authentication and Authorization

Users can register, log in, and perform actions based on their roles (admin or user). Spring Security ensures secure access.

### Product Management

Admins can create, update, delete, and read products and their variants. Products can be associated with categories.

### Order Management

Admins can manage orders, view order details, and receive notifications when orders are placed.

### Category Management

Admins can manage product categories.

### Payment Gateway

Payment processing is handled through Stripe, making it easy for users to pay for their orders securely.

### Image Storage

Cloudinary is used to store and serve product images.

### Continuous Integration

GitHub Actions is set up for continuous integration, ensuring that the codebase is always up to date and building successfully.

### Push Notifications

RabbitMQ is used to send push notifications, including order placement and product delivery notifications.

### Dashboard

Users and admins can access a dashboard displaying essential information about orders, users, and products.

### Cron Jobs

The system automatically generates balance reports, including income, expenses, and profits, using cron jobs.

### API Documentation

Swagger is integrated for easy access to API documentation.

## Technologies Used

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5.4-brightgreen) ![Spring Security](https://img.shields.io/badge/Spring%20Security-5.5.1-brightgreen) ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-2.5.4-brightgreen) ![Hibernate](https://img.shields.io/badge/Hibernate-5.5.7-brightgreen) ![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.9.7-brightgreen) ![Stripe](https://img.shields.io/badge/Stripe-API-brightgreen) ![Cloudinary](https://img.shields.io/badge/Cloudinary-Image%20Storage-brightgreen) ![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-CI/CD-brightgreen) ![Swagger](https://img.shields.io/badge/Swagger-API%20Documentation-brightgreen) ![MySQL](https://img.shields.io/badge/MySQL-8.0.26-brightgreen) ![Maven](https://img.shields.io/badge/Maven-3.8.2-brightgreen) ![Java](https://img.shields.io/badge/Java-11-brightgreen) ![MongoDB](https://img.shields.io/badge/MongoDB-5.0.3-brightgreen) ![JUnit](https://img.shields.io/badge/JUnit-5.8.0-brightgreen) ![Mockito](https://img.shields.io/badge/Mockito-4.0.0-brightgreen)


## Getting Started

1. Clone the repository from GitHub.
2. Configure your application properties, including database settings, Stripe API keys, Cloudinary credentials, and RabbitMQ configuration.
3. Run the application using `mvn spring-boot:run`.

## Usage

Ensure you have set up the project, and the application is running. Users and admins can access the application and its features as described below:

### Admin Features

- **Product Management:** Create, update, delete, and read products and their variants. Manage product categories.

- **Order Management:** View and manage orders, receive order placement notifications.

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
