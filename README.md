## Project documentation

This project implements a simple login system for a web application, which includes user authentication and registration. 
The system provides an API that accepts email and password inputs, returning an access token upon successful login or appropriate error messages when the credentials are incorrect. New users can sign up by providing their email and password, which are then stored in a persistent data store.
The access token is a JSON Web Token (JWT) that can be used to authenticate future requests to protected endpoints.

### Features

    Login API: Accepts email and password, returns an access token if the credentials are correct.
    Registration API: Allows new users to sign up with their email and password.
    Frontend: A simple user interface where users can log in or sign up.
    Data Persistence: User data (email and password) is stored for future authentication.

### Technologies Used

    Backend: Java with Spring Boot
    Frontend: HTML, CSS, JavaScript
    Database: SQL Server
    Build Tool: Maven

### Before Running the Application
A datasource url, username, and password must be provided in the application.properties file to connect to the SQL Server database.
Alternatively, an H2 in-memory database can be used for development purposes by adjusting the database configuration in the application.properties file.

### How It Works

+ Login: When users enter their email and password, the frontend sends a POST request to the backend API. If the credentials are valid, the backend returns an access token. If the credentials are invalid, the backend sends an error message. 
+ Register: New users can register by providing an email and password. The backend stores the credentials in the database and returns a success message upon successful registration.

+ The paths /api/auth/login, /api/auth/register, /login, and /register are unprotected and do not require a JWT token. Other paths are protected and require a valid JWT token for access. In this project, the only protected endpoint is /api/protected.

### Classes
+ **JWTUtil:** Handles JWT creation and validation.

+ **PasswordEncoder**: Defines the password encoder used to hash passwords before storing them in the database.

+ **JWTAuthFilter:** Filters requests to protected paths, checking if the Authorization header contains a valid JWT token, if not, it returns an error status code.

+ **SecurityConfig:** Configures security settings for the application, adding beans and filters to the Spring context.

+ **UserDetailServiceImpl:** Handles user-related operations, including registration and user detail retrieval for authentication purposes.

+ **UserDetails:** Represents the user entity, storing the user's email and password. This class is annotated with @Entity, indicating it is a JPA entity persisted in the database.

+ **UserRepository:** A Spring Data JPA repository that provides CRUD operations for the UserDetails entity.

### Database configuration
This application uses a SQL Server database with Hibernate as the ORM framework to store user emails and passwords. The configuration for the database connection is specified in the application.properties file. Alternatively, it can be switched to use an H2 in-memory database for simplicity during development by adjusting the database configuration in the application.properties file.

## API Documentation

#### 1. **POST `/api/auth/login`**
- **Description:** Authenticates a user with email and password. If the credentials are valid, the server returns an access token.
- **Request Body:**
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```

- **Response (Success):**
    - **Status Code:** 200 OK
    - **Body:**
  ```json
  {
    "access_token": "generated_JWT_Token",
    "message": "Login successful"
  }
  ```

- **Response (Failure - Invalid Credentials):**
    - **Status Code:** 401 Unauthorized
    - **Body:**
  ```json
  {
    "message": "Wrong email or password"
  }
  ```

- **Response (Failure - Validation Errors):**
    - **Status Code:** 400 Bad Request
    - **Body:**
  ```json
    {
      "message": [
        "Email cannot be empty",
        "Invalid email address",
        "Password cannot be empty",
        "Password must be at least 8 characters long"
      ]
    }
  ```

#### 2. **POST `/api/auth/register`**
- **Description:** Registers a new user by providing an email and password.
- **Request Body:**
  ```json
  {
    "email": "newuser@example.com",
    "password": "password123"
  }
  ```

- **Response (Success):**
    - **Status Code:** 201 Created
    - **Body:**
  ```json
  {
    "message": "User registered successfully"
  }
  ```

- **Response (Failure - Email Already Exists):**
    - **Status Code:** 409 Conflict
    - **Body:**
  ```json
  {
    "message": "Email already exists"
  }
  ```

- **Response (Failure - Validation Errors):**
    - **Status Code:** 400 Bad Request
    - **Body:**
  ```json
    {
      "message": [
        "Email cannot be empty",
        "Invalid email address",
        "Password cannot be empty",
        "Password must be at least 8 characters long"
      ]
    }
  ``` 

### Protected API Endpoint
#### 1.  **GET `/api/protected`**

- **Response (Failure - Invalid JWT Token):**
    - **Status Code:** 200 Ok
    - **Body:**
  ```json
    {
      "message": "Protected endpoint accessed"
    }
  ``` 

- **Response (Failure - Missing or malformed Authorization header):**
    - **Status Code:** 400 Bad Request


- **Response (Failure - Invalid JWT Token):**
    - **Status Code:** 401 Unauthorized


## Frontend Endpoints

#### 1. Get Login Page Endpoint

   + URL: /login
   + Method: GET
   + Response: Returns the login page (HTML view).

#### 2. Get Register Page Endpoint

   + URL: /register
   + Method: GET
   + Response: Returns the registration page (HTML view).