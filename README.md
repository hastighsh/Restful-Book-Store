# Restful Book Store

The **Restful Book Store** is a Java-based RESTful web service that provides an API for querying books stored in an SQLite database. The service allows users to search for books by category or keyword and retrieve a list of all available books.

## Overview

This project is a refactored version of a book database application, initially built using servlets and JSP. The original implementation used an MVC and DAO pattern but was converted into a RESTful web service to enhance scalability and maintainability.

The new architecture follows the RESTful paradigm, removing the need for servlets and JSPs, and directly exposing endpoints for querying book data.

## Technologies Used

- **Java** (JAX-RS for RESTful API)
- **Spring Boot** (optional for dependency management)
- **SQLite** (lightweight database)
- **Maven** (dependency and build management)
- **Jersey** (for RESTful services)

## Database Structure

The application utilizes an SQLite database (`Books.db`) that contains three tables:

- **BOOK** – Stores book details such as title, author, category, price, and description.
- **AUTHOR** – Stores author details.
- **CATEGORY** – Stores different book categories.

## REST API Endpoints

The web service is structured under the `/Books` resource path, with the following endpoints:

### Retrieve all books
```http
GET /rest/Books
```

### Retrieve all categories
```http
GET /rest/Books/category
```

### Search books by category
```http
GET /rest/Books/searchByCat/{category}
```
**Example:**
```http
GET /rest/Books/searchByCat/Fiction
```

### Search books by keyword
```http
GET /rest/Books/searchByKey/{keyword}
```
**Example:**
```http
GET /rest/Books/searchByKey/Java
```

## Implementation Details

- **DAO (Data Access Object):** The `BookDAOImpl` class handles database queries, requiring a connection to the SQLite database.
- **REST Service:** The `BookService` class acts as the front controller, handling all HTTP requests.
- **Database Access:** The SQLite database file (`Books.db`) is placed in the `WebContent` folder, and its absolute path is resolved using `ServletContext` via the `@Context` annotation.
- **Maven Dependency:** Instead of manually adding the SQLite JDBC driver, it is included via the Maven dependency:

```xml
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.34.0</version>
</dependency>
```

## Running the Project

### Clone the repository:
```sh
git clone https://github.com/hastighsh/Restful-Book-Store.git
cd Restful-Book-Store
```

### Build the project using Maven:
```sh
mvn clean package
```

### Run the application:
```sh
java -jar target/server-1.0-RELEASE.jar
```

### Use `curl` or a REST client to interact with the API:
```sh
curl http://localhost:8080/booksREST/rest/Books
```

## Notes

- The application follows a RESTful design, making it suitable for integration with frontend applications or other services.
- The SQLite database provides a simple, file-based storage solution that can be easily migrated to a more robust system if needed.
