## My Spring Boot Blog v.2.0

A simple educational blog web application built with Java 21 and Spring Boot 3.2+.  
Supports full CRUD for posts, image upload, likes, tags, comments, pagination — all deployed as an executable JAR using embedded Tomcat.

## Technologies

- Java 21  
- Spring Boot 3.2+ (Spring Web, Spring Data JPA)  
- Thymeleaf 3  
- Gradle  
- Embedded Tomcat (via Spring Boot)  
- H2 Database 
- JUnit 5 + Mockito + Spring Boot Test

## Project Structure

- MVC layered architecture (Controller, Service, DAO)  
- Thymeleaf templates for UI  
- Image upload support (MultipartFile)  
- Full CRUD for blog posts, with tags, likes, comments, and pagination  
- Embedded Tomcat for running the app via JAR  

##  Features

- Homepage and post list  
- Full CRUD: create, edit, delete blog posts  
- Add/edit/delete comments  
- Like/dislike system  
- Tag support  
- Pagination and search  
- Image upload 

## Build Executable JAR

- Use Gradle to compile and package the application:
./gradlew clean build
- The executable JAR file will be located at:
``build/libs/myblog-springboot.jar``
- You can then run the application using:
``java -jar build/libs/myblog-springboot.jar``
- The application will be available at:
``http://localhost:8080/``

## Run the Application

- Run the application using the generated JAR file:  
`` java -jar build/libs/springboot-blog.jar``
- Open the app in your browser: `` http://localhost:8080/``
- To change the server port or context path, configure ``application.properties`` or use command-line arguments.

## Running Tests

This project includes unit and integration tests for: 
- controllers (Spring MVC with MockMvc)
- service layer logic (using JUnit 5 and Mockito)
- DAO/model layer (tested with Spring Boot Test and an in-memory H2 database). 
To execute all tests, run the following command from the project root:`` ./gradlew test``

## Usage

- Navigate to ``/posts`` to see the blog post list
- Click “Add Post” to create a new blog entry
- Edit or delete posts and comments via the UI
- Like/dislike posts
- Use the search bar and pagination controls
- Upload images when creating or editing posts