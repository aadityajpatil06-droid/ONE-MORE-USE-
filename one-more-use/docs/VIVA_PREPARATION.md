# College Viva Preparation Guide — One More Use

This guide gives simple, clear, and confident answers to the most common questions college teachers and external examiners ask during project vivas.

---

## 1. High-Level Project Questions

### Q1: What is the objective of "One More Use"?
**Answer:**  
"One More Use is a community-driven reuse and sharing platform designed to promote sustainable consumption and reduce household waste. Instead of throwing away unused items—such as wooden crates, study chairs, table lamps, or bicycles—users can upload a photo and description of the item. The platform analyzes the item and generates 5 actionable reuse/upcycling suggestions. If the user doesn't want to upcycle it themselves, they can list it for the local community as **Give for Free** or **Exchange for another item**."

### Q2: What is your technology stack and why did you choose it?
**Answer:**  
- **Frontend**: HTML5, CSS3, and Vanilla JavaScript (Fetch API).  
  *Why*: Keeps the client lightweight, easy to run without heavy Node/React build steps, and easy to explain to examiners.
- **Backend**: Java with Spring Boot 3.  
  *Why*: Industry-standard framework offering embedded Tomcat, dependency injection, and clean layered architecture.
- **Database**: MySQL 8.0 with Spring Data JPA and Hibernate.  
  *Why*: Reliable relational database with ACID compliance, and Hibernate handles object-relational mapping without writing verbose SQL JDBC code.
- **Build Tool**: Apache Maven for managing dependencies and building executable JARs.

---

## 2. Architecture & Design Questions

### Q3: Explain the layered architecture of your backend.
**Answer:**  
"We follow the standard 3-Tier Layered Architecture:
1. **Controller Layer (`@RestController`)**: Handles incoming HTTP REST requests, parses request bodies, validates input, and returns JSON responses with appropriate HTTP status codes.
2. **Service Layer (`@Service`)**: Encapsulates core business logic, such as password verification, rule-based AI suggestion generation, and validating that users cannot request their own items.
3. **Repository Layer (`@Repository`)**: Extends Spring Data JPA's `JpaRepository` to perform CRUD queries and custom queries on MySQL without writing manual SQL.
4. **Entity Layer (`@Entity`)**: Plain Java objects mapped to relational database tables using JPA annotations."

```
Client (Browser) ──HTTP REST──> Controller ────> Service ────> Repository (JPA) ────> MySQL DB
```

---

## 3. Frontend Questions (HTML, CSS, JavaScript)

### Q4: How does your frontend communicate with the backend?
**Answer:**  
"We use modern JavaScript's native `fetch()` API with `async/await`. For JSON requests (like login and requests), we set the `Content-Type: application/json` header and serialize the JavaScript object using `JSON.stringify()`. For file uploads with item details, we construct a `FormData` object and send a `multipart/form-data` POST request."

### Q5: How do you handle styling without Bootstrap or Tailwind?
**Answer:**  
"We built a custom stylesheet in `frontend/css/style.css` using CSS3 Custom Properties (variables like `--forest`, `--moss`, `--paper`) for a unified design identity. We use CSS Grid (`grid-template-columns: repeat(3, 1fr)`) for responsive item displays and Flexbox for navigation, aligned headers, and request rows."

### Q6: How is user login state remembered in the browser?
**Answer:**  
"When the backend authenticates the user, it returns user metadata and a session token. The frontend saves this object in browser `localStorage` under `omu_user`. On page load, `DOMContentLoaded` checks `localStorage`. If found, it populates the user's name, avatar initials, and skips the login screen."

---

## 4. Backend & Spring Boot Questions

### Q7: What annotations did you use in Spring Boot and what do they mean?
**Answer:**  
- `@SpringBootApplication`: Combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`. Boots up the application.
- `@RestController`: Marks the class as a web controller where every method returns a domain object directly serialized into JSON.
- `@RequestMapping("/api/...")`: Maps HTTP URLs to specific controller classes or methods.
- `@GetMapping`, `@PostMapping`, `@PutMapping`: Shorthand annotations for specific HTTP request methods.
- `@RequestBody`: Binds the incoming JSON request body to a Java DTO object.
- `@PathVariable`: Extracts dynamic values from URI path segments (e.g. `/api/items/{id}`).
- `@Autowired`: Injects dependencies (e.g., repositories into services) automatically via Spring's Inversion of Control (IoC) container.

### Q8: How does your honest rule-based AI suggestion engine work?
**Answer:**  
"In our college demonstration, we implement an honest, keyword-based analysis service in `ReuseIdeaService.java`. It parses the entered item name and description for keywords (e.g., *box*, *wood*, *chair*, *lamp*, *cycle*, *jar*). It returns detection attributes (material, category, condition, confidence score) and 5 distinct upcycling suggestions with emojis. Because this service is cleanly decoupled, it can be extended to call a live vision API (like Google Gemini Vision) in the future simply by modifying that single service."

---

## 5. Database, JPA & Hibernate Questions

### Q9: What is the difference between JPA and Hibernate?
**Answer:**  
- **JPA (Jakarta/Java Persistence API)** is a *specification* or set of interfaces that defines standard ORM in Java.
- **Hibernate** is the *actual implementation* (the ORM provider) that implements the JPA interfaces and generates SQL queries for MySQL behind the scenes.

### Q10: What are the main tables and relationships in your database?
**Answer:**  
- **`users`**: Stores user accounts (name, email, password hash, location).
- **`items`**: Linked to `users` via `@ManyToOne` (one user can post many items).
- **`item_requests`**: Connects `items` (which item), `users` (requester), and `users` (owner) with `@ManyToOne` relationships. Tracks status as `PENDING`, `ACCEPTED`, or `REJECTED`.

### Q11: How do you prevent SQL injection?
**Answer:**  
"Spring Data JPA and Hibernate use **Prepared Statements** with parameterized queries (e.g., `:query` in `@Query`). User input is always treated as literal data rather than executable SQL code, preventing SQL injection."

---

## 6. Security Questions

### Q12: How do you store user passwords securely?
**Answer:**  
"We never store passwords in plain text. We use Spring Security's `BCryptPasswordEncoder`. BCrypt uses a salted, slow cryptographic hashing algorithm. Even if the database is exposed, the original plain text password cannot be reversed or retrieved using rainbow tables."

### Q13: What authorization checks are enforced on requests?
**Answer:**  
"In `ItemRequestService.java`, the backend enforces three critical authorization and business checks:
1. **Self-request prevention**: A user cannot request an item they posted (`owner.id == requester.id`).
2. **Duplicate request prevention**: A user cannot submit multiple active requests for the same item.
3. **Owner-only resolution**: Only the item's true owner can change the request status to `ACCEPTED` or `REJECTED`."

---

## 7. Viva Confidence Tips

1. **Be honest about demo features**: If asked *"Is this a real neural network running on the image?"*, answer confidently: *"For this college demonstration, we implemented an honest rule-based keyword matching service in Java to showcase the end-to-end user workflow without requiring paid cloud API keys. The architecture is modular so an external vision API can be connected seamlessly."*
2. **Explain the donation wording decision**: If asked about donating, explain: *"Per project requirements, our platform focuses strictly on **Give for Free** and **Exchange for another item**, promoting direct peer-to-peer sharing rather than charity-based donation models."*
3. **Show the database**: Open MySQL Workbench and execute `SELECT * FROM items;` or `SELECT * FROM item_requests;` during the viva to prove your application persists data into MySQL.
