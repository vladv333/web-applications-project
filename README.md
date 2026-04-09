<<<<<<< HEAD
# Vlad's Super Shop

An online electronics shop built as a course project for Web Applications.

## Tech Stack

- **Backend:** Java 21, Spring Boot, Spring Security
- **Frontend:** React 18, Bootstrap 5, Axios
- **Database:** PostgreSQL (Docker)

## How to Run

**1. Start the database**

docker-compose up -d


**2. Start the backend**

mvn spring-boot:run

**3. Start the frontend**

npm start


Open http://localhost:3000

## User Stories

**US1 — Browse products**
As a visitor, I want to see a list of all available electronics products, so that I can explore what the shop offers and click on any product to see its full details.

**US2 — Shopping cart**
As a visitor, I want to add products to a cart and see the total price before confirming, so that I can review my order before placing it.

**US3 — Register and login**
As a new user, I want to create an account with my username, email and password, so that my cart and order history are saved to the database and available next time I log in.

**US4 — Place an order**
As a logged-in user, I want to place an order from my cart, so that my purchase is recorded in the system. After placing the order, the cart is cleared automatically.

**US5 — Manage my profile**
As a logged-in user, I want to view and update my username, email and password from a profile page, so that I can keep my account information up to date and see my full order history.

## Features

- Browse electronics products (mice, keyboards, headphones, monitors and more)
- Shopping cart (localStorage for guests, database for logged-in users)
- User registration and login
- Order placement and order history
- User profile with editable info and password change
- Admin panel to manage products
=======
# web-applications-project
Super Shop — Web Applications Course Project
An electronics online shop built with Spring Boot, React, and PostgreSQL.

Tech Stack

Backend: Java 21, Spring Boot, Spring Security (BCrypt)
Frontend: React 18, Bootstrap 5, Axios
Database: PostgreSQL (Docker)

User Stories
US1 — Browse products
As a visitor, I want to see a list of all available electronics products, so that I can explore what the shop offers and click on any product to see its full details.
US2 — Shopping cart
As a visitor, I want to add products to a cart and see the total price before confirming, so that I can review my order before placing it.
US3 — Register and login
As a new user, I want to create an account with my username, email and password, so that my cart and order history are saved to the database and available next time I log in.
US4 — Place an order
As a logged-in user, I want to place an order from my cart, so that my purchase is recorded in the system. After placing the order, the cart is cleared automatically.
US5 — Manage my profile
As a logged-in user, I want to view and update my username, email and password from a profile page, so that I can keep my account information up to date and see my full order history.
>>>>>>> 3b7663922f0ba1adc0396aa8100ea4b0b7a9fee5
