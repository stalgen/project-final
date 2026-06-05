## [REST API](http://localhost:8080/doc)

## Концепция:

- Spring Modulith
    - [Spring Modulith: достигли ли мы зрелости модульности](https://habr.com/ru/post/701984/)
    - [Introducing Spring Modulith](https://spring.io/blog/2022/10/21/introducing-spring-modulith)
    - [Spring Modulith - Reference documentation](https://docs.spring.io/spring-modulith/docs/current-SNAPSHOT/reference/html/)

```
  url: jdbc:postgresql://localhost:5432/jira
  username: jira
  password: JiraRush
```

- Есть 2 общие таблицы, на которых не fk
    - _Reference_ - справочник. Связь делаем по _code_ (по id нельзя, тк id привязано к окружению-конкретной базе)
    - _UserBelong_ - привязка юзеров с типом (owner, lead, ...) к объекту (таска, проект, спринт, ...). FK вручную будем
      проверять

## Аналоги

- https://java-source.net/open-source/issue-trackers

## Тестирование

- https://habr.com/ru/articles/259055/

## Completed tasks:

1. Removed obsolete social media integrations (VK, Yandex).

2. Extracted sensitive configuration (database credentials, OAuth identifiers, mail settings) into a separate properties file. Configured the application to read these values from environment variables.

3. Configured an in-memory H2 database for the test environment. Defined separate DataSource beans dynamically loaded via Spring profiles (prod and test). Adapted Liquibase scripts to support both PostgreSQL and H2 dialects.

4. Implemented comprehensive integration tests for ProfileRestController covering all success and failure paths (unauthorized access, payload validation).

5. Refactored attachment upload logic in FileUtil. Enhanced security by adding strict protection against Path Traversal vulnerabilities and significantly improved memory performance by replacing full-file byte array loading with Java NIO Streams.

6. Implemented Task Tags management via REST API. Added endpoints for dynamically adding and removing tags, mapped the task_tag table using @ElementCollection in the Task entity, and optimized database fetch queries to ensure seamless data retrieval.

7. Added Time-in-Status tracking. Implemented service-level logic (getTimeInWork and getTimeInTesting in TaskService) to calculate the exact duration a task spends in specific development phases based on its activity history. Inserted mock transition data into the database initialization script for testing.

8. Containerized the application. Created a lightweight Dockerfile based on eclipse-temurin Alpine image to package the Spring Boot main server for easy deployment and distribution.

9. Orchestrated the application infrastructure using Docker Compose. Created a docker-compose.yaml file to simultaneously run the PostgreSQL database, the Spring Boot backend, and an Nginx reverse proxy within an isolated container network. Refactored the config/nginx.conf file to correctly route requests, configure essential X-Forwarded-* headers (resolving infinite redirect loops with Spring Security), and mapped static resources via Docker volumes for seamless UI and email template rendering.

10. Implemented Internationalization (i18n) to support English and Ukrainian languages across the application. Configured Spring's LocaleResolver and LocaleChangeInterceptor to switch languages dynamically via URL parameters and persist user preferences in cookies. Extracted hardcoded text from the UI (index, header, footer) and email templates into dedicated resource bundles (messages.properties, messages_uk.properties). Refactored MailService to remove hardcoded locales and generate emails dynamically based on the current context using Thymeleaf message expressions.

11. Migrated the application's authentication mechanism from stateful JSESSIONID to stateless JWT (JSON Web Token). Integrated jjwt library and implemented JwtUtil for cryptographic token generation and validation. Developed a custom JwtFilter to intercept HTTP requests and authenticate users via tokens stored in cookies or Authorization headers. Refactored SecurityConfig to enforce STATELESS session management and securely distribute JWTs upon successful login.