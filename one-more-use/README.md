# One More Use — AI-Based Reuse & Community Sharing Platform

A complete, beginner-friendly full-stack web application designed for a college project. The platform helps people reuse unwanted objects instead of throwing them away.

---

## 1. Technology Stack
- **Frontend**: HTML5, CSS3, Vanilla JavaScript (ES6+ Fetch API) — *No React or heavy frameworks*.
- **Backend**: Java 17+, Spring Boot 3.2.5 (Spring Web, Spring Data JPA, Hibernate).
- **Database**: MySQL 8.0+.
- **Build Tool**: Apache Maven.
- **API Testing**: Postman.
- **Security**: BCrypt password hashing via Spring Security Crypto.

---

## 2. Project Directory Structure

```
one-more-use/
├── README.md                      # Main beginner guide & run instructions
├── PROJECT_STATUS.md              # Test execution matrix & verification report
├── database_setup.sql             # Complete SQL script to set up MySQL database
├── .gitignore                     # Git ignore rules for passwords and uploads
├── uploads/                       # Directory where user uploaded photos are stored
│   └── .gitkeep
├── frontend/                      # Pure client-side application
│   ├── index.html                 # Main Single Page Application HTML
│   ├── css/
│   │   └── style.css              # Custom responsive stylesheet
│   └── js/
│       └── app.js                 # API caller, router, and UI renderer
├── backend/                       # Spring Boot 3 Maven Project
│   ├── pom.xml                    # Maven configuration and dependencies
│   └── src/
│       ├── main/
│       │   ├── java/com/onemoreuse/
│       │   │   ├── OneMoreUseApplication.java   # Main Spring Boot entry point
│       │   │   ├── config/                      # CORS, Security, Exception Handler
│       │   │   ├── controller/                  # REST Controllers
│       │   │   ├── dto/                         # Request & Response Data Objects
│       │   │   ├── entity/                      # JPA Database Entities
│       │   │   ├── repository/                  # Spring Data JPA Repositories
│       │   │   └── service/                     # Business Logic & AI Engine
│       │   └── resources/
│       │       ├── application.properties       # Database and server settings
│       │       ├── application.properties.example # Safe configuration template
│       │       ├── schema.sql                   # Table DDL
│       │       └── data.sql                     # Seed data
│       └── test/
└── docs/                          # Viva & Testing Documentation
    ├── DATABASE.md                # Tables, relationships, and ERD
    ├── API_TESTING.md             # Endpoint catalog and curl examples
    ├── One_More_Use.postman_collection.json # Importable Postman test collection
    └── VIVA_PREPARATION.md        # Comprehensive college viva Q&A
```

---

## 3. Step-by-Step Windows Setup Guide

### Step 1: Start MySQL Server
1. Press `Win + R`, type `services.msc`, and press **Enter**.
2. Scroll down to find **MySQL80** (or **MySQL**).
3. If it says *Stopped*, right-click and select **Start**.
*(Alternatively, in an Administrator PowerShell, run: `Start-Service MySQL80`)*.

---

### Step 2: Initialize Database and Seed Data
1. Open **MySQL Workbench** (or your preferred MySQL client).
2. Connect to your local MySQL instance.
3. Open the file:
   `one-more-use/database_setup.sql`
4. Click the **Execute (Lightning Bolt)** icon.
5. This creates the database `one_more_use`, builds the tables (`users`, `items`, `item_requests`), and seeds sample users and items.

---

### Step 3: Check Database Credentials in Backend
Open `backend/src/main/resources/application.properties` and verify your MySQL password:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/one_more_use?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root   <-- Change to your MySQL password if different
```

---

### Step 4: Run the Spring Boot Backend

#### Option A: Using IntelliJ IDEA (Easiest)
1. Open **IntelliJ IDEA**.
2. Click **File > Open** and select the folder:
   `one-more-use/backend`
3. Wait a moment for IntelliJ to import Maven dependencies.
4. Open `src/main/java/com/onemoreuse/OneMoreUseApplication.java`.
5. Click the green **Run (Play)** button next to `main()`.
6. Look at the console. When you see:
   `One More Use Backend API is running on http://localhost:8080`
   your backend is successfully live!

#### Option B: Using Command Line (Maven)
1. Open Command Prompt or PowerShell in the `backend` folder:
   ```cmd
   cd backend
   mvn spring-boot:run
   ```

---

### Step 5: Launch the Frontend
1. Navigate to the `frontend/` folder in Windows File Explorer.
2. Double-click **`index.html`** to open it in your web browser (Chrome, Edge, Firefox).
3. The platform is now fully connected to your Spring Boot backend!

---

## 4. End-to-End Demonstration Walkthrough

Follow this script to demonstrate the platform to your college teacher:

### 1. Sign In
- Use the pre-seeded account:
  - **Email**: `priya.kulkarni@gmail.com`
  - **Password**: `password123`
- Click **Sign In**. The dashboard loads with a personalized greeting: *"Give it one more use, Priya."*

### 2. Upload an Object & Run AI Analysis
- Click **Upload an Object** in the navigation bar.
- Click the upload zone to pick an image from your computer (e.g. a photo of a wooden crate or study desk).
- Enter description: `Old wooden storage box, slightly worn but sturdy`.
- Click **Analyze Object**.
- The platform contacts the backend suggestion engine (`/api/reuse-ideas/analyze`) and displays:
  - Detected item: *Old Wooden Box*
  - Category: *Furniture / Storage*
  - Material: *Solid wood, natural finish*
  - Condition: *Good — minor wear*
  - Confidence score: *94%*
- Click **Show 5 Reuse Ideas** to view:
  1. *Storage Box*
  2. *Plant Holder*
  3. *Mini Bookshelf*
  4. *Decorative Furniture*
  5. *Craft Organiser*

### 3. List the Item for Community
- Click **I Don't Need It — Pass It On**.
- The form opens pre-filled from your analysis.
- Choose **Give for Free** or **Exchange for another item** (if Exchange, specify what you'd like, e.g., *Small bookshelf*).
- Click **Save & List Item**.
- The item is saved directly into the MySQL `items` table!

### 4. Search and Browse Items
- Go to **Browse & Nearby**.
- Type `wood` or `chair` in the search bar.
- The list filters dynamically using the backend search query.
- Click any card to open the **Item Details** screen with owner info and pickup location.

### 5. Send and Accept Requests
- Click on an item owned by someone else (e.g. *Cane Study Chair* posted by Rohan Patil).
- Click **Send Request**. Type a pickup message and submit.
- The request is saved in the MySQL `item_requests` table with status `PENDING`.
- Switch accounts (Sign out, log in as `rohan.patil@gmail.com` with `password123`).
- Go to **Requests**. Rohan sees your incoming request and can click **Accept** or **Reject**.
- When accepted, the status updates to `ACCEPTED` and the direct message handover box unlocks.

### 6. View Profile Statistics
- Click **Profile** in the sidebar.
- Displays live statistics calculated from the database: *Items Posted*, *Items Given*, *Items Received*, and *Active Requests*, plus items you have listed.

---

## 5. Troubleshooting Common Issues

### 1. "Cannot connect to backend server" error on login
- **Cause**: Spring Boot is not running on port 8080.
- **Fix**: Check IntelliJ console or terminal to ensure Spring Boot has started without errors.

### 2. "Communications link failure" in Spring Boot console
- **Cause**: MySQL service is not running.
- **Fix**: Open Windows Services (`services.msc`) and start `MySQL80`.

### 3. "Access denied for user 'root'@'localhost'"
- **Cause**: Password in `application.properties` does not match your MySQL root password.
- **Fix**: Open `application.properties` and update `spring.datasource.password=YOUR_PASSWORD`.

### 4. Port 8080 already in use
- **Cause**: Another service or previous run is occupying port 8080.
- **Fix**: Open PowerShell as Administrator and run:
  ```powershell
  Stop-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess -Force
  ```
  Then restart Spring Boot.
