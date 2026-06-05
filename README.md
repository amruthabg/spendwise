# SpendWise — Personal Expense Tracker API

SpendWise is a lightweight REST API built with **Spring Boot** and **MySQL** designed to help users track their daily expenditures, organize them by category, and generate monthly spending reports.

## Features
- **User Management**: Simple registration endpoint.
- **Category Management**: Create custom spending categories (e.g., Food, Travel, Utilities).
- **Expense Logging**: Add, update, and delete expenses with validations (no zero or negative values allowed).
- **Monthly Summary**: Get a monthly summary of total expenses grouped by category in a given month.

## Tech Stack
- **Backend**: Java 17, Spring Boot (REST API), Maven
- **Database**: H2 (in-memory for local running) / MySQL (production/dev)
- **ORM & Validation**: Spring Data JPA, Hibernate, Jakarta Bean Validation

---

## Database Design

```
users                    categories              expenses
─────────────           ─────────────           ─────────────
id (PK)                 id (PK)                 id (PK)
name                    name                    amount
email                   user_id (FK→users)      description
password                                        expense_date
created_at                                      category_id (FK→categories)
                                                user_id (FK→users)
                                                created_at
```

---

## Running the Application

### Option 1: Running with H2 (Default)
By default, the application is configured to run with H2 (an in-memory database). You don't need any database installation.
1. Import the project into your IDE (such as IntelliJ IDEA).
2. Run the main class `com.gaonkar.spendwise.SpendWiseApplication`.
3. The database console is accessible at: `http://localhost:8081/h2-console`
   - **JDBC URL**: `jdbc:h2:mem:spendwisedb`
   - **User**: `sa` / **Password**: *(leave blank)*

### Option 2: Running with MySQL
1. Create a MySQL database:
   ```sql
   CREATE DATABASE spendwise_db;
   ```
2. Open `src/main/resources/application.properties` and:
   - Comment out the H2 Configuration section.
   - Uncomment the MySQL Configuration section and update your database credentials.
3. Run the application. Hibernate will automatically create the tables.

---

## API Documentation & Usage

Here are some example HTTP request `curl` commands to test the API:

### 1. Register a User
- **Method**: `POST`
- **URL**: `http://localhost:8081/api/users/register`
- **Body**:
  ```json
  {
    "name": "Amrutha Bhaskar",
    "email": "amrutha@example.com",
    "password": "securepassword123"
  }
  ```
- **Curl Command**:
  ```bash
  curl -X POST http://localhost:8081/api/users/register \
    -H "Content-Type: application/json" \
    -d '{"name": "Amrutha Bhaskar", "email": "amrutha@example.com", "password": "securepassword123"}'
  ```

### 2. Create a Category
- **Method**: `POST`
- **URL**: `http://localhost:8081/api/categories`
- **Body**:
  ```json
  {
    "name": "Food",
    "userId": 1
  }
  ```
- **Curl Command**:
  ```bash
  curl -X POST http://localhost:8081/api/categories \
    -H "Content-Type: application/json" \
    -d '{"name": "Food", "userId": 1}'
  ```

### 3. Log an Expense
- **Method**: `POST`
- **URL**: `http://localhost:8081/api/expenses`
- **Body**:
  ```json
  {
    "amount": 150.0,
    "description": "Lunch at restaurant",
    "expenseDate": "2026-06-03",
    "categoryId": 1,
    "userId": 1
  }
  ```
- **Curl Command**:
  ```bash
  curl -X POST http://localhost:8081/api/expenses \
    -H "Content-Type: application/json" \
    -d '{"amount": 150.0, "description": "Lunch at restaurant", "expenseDate": "2026-06-03", "categoryId": 1, "userId": 1}'
  ```

### 4. Fetch Monthly Spending Summary
Returns the total spending grouped by category for a specific month/year using **Java 8 Streams**.
- **Method**: `GET`
- **URL**: `http://localhost:8081/api/expenses/summary?userId=1&year=2026&month=6`
- **Curl Command**:
  ```bash
  curl -X GET "http://localhost:8081/api/expenses/summary?userId=1&year=2026&month=6"
  ```
- **Example Response**:
  ```json
  [
    {
      "categoryName": "Food",
      "totalAmount": 150.0
    }
  ]
  ```

---

## Technical Highlights
- **Streams API for Summary**: Implemented data grouping using `Collectors.groupingBy()` and `Collectors.summingDouble()` to process SQL entities entirely in Java stream pipelines.
- **Robust Validation**: Uses Spring Boot validation annotations like `@NotNull` and `@Min` to safeguard API requests.
- **Central Exception Handling**: Uses a global `@ControllerAdvice` controller to intercept domain errors (`ResourceNotFoundException`, validation failures) and return standardized JSON error bodies with correct HTTP status codes.
