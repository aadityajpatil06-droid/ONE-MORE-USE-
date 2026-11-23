# API Reference and Postman Testing Guide — One More Use

This document lists all REST endpoints exposed by the Spring Boot backend, sample payloads, and instructions for testing them using Postman or cURL.

**Base URL**: `http://localhost:8080/api`

---

## 1. Authentication Endpoints

### 1.1 Register New User
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/auth/register`
- **Headers**: `Content-Type: application/json`
- **Request Body**:
```json
{
  "name": "Tanvi Sharma",
  "email": "tanvi.sharma@gmail.com",
  "password": "password123",
  "location": "Kothrud, Pune"
}
```
- **Success Response (`201 Created`)**:
```json
{
  "success": true,
  "message": "Authentication successful",
  "id": 7,
  "name": "Tanvi Sharma",
  "email": "tanvi.sharma@gmail.com",
  "location": "Kothrud, Pune",
  "token": "omu_token_7_1729876543210"
}
```
- **Duplicate Email Error (`400 Bad Request`)**:
```json
{
  "success": false,
  "message": "An account with this email address already exists."
}
```

---

### 1.2 User Login
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/auth/login`
- **Headers**: `Content-Type: application/json`
- **Request Body**:
```json
{
  "email": "priya.kulkarni@gmail.com",
  "password": "password123"
}
```
- **Success Response (`200 OK`)**:
```json
{
  "success": true,
  "message": "Authentication successful",
  "id": 1,
  "name": "Priya Kulkarni",
  "email": "priya.kulkarni@gmail.com",
  "location": "Kothrud, Pune",
  "token": "omu_token_1_1729876543210"
}
```
- **Invalid Password Error (`401 Unauthorized`)**:
```json
{
  "success": false,
  "message": "Invalid email or password."
}
```

---

## 2. Items Endpoints

### 2.1 Get All Available Items
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/items`
- **Response (`200 OK`)**:
```json
[
  {
    "id": 1,
    "ownerId": 2,
    "ownerName": "Aditi Deshmukh",
    "ownerLocation": "Kothrud, Pune",
    "title": "Old Wooden Box",
    "description": "A sturdy wooden storage box, slightly worn on the edges but structurally solid.",
    "category": "Furniture / Storage",
    "itemCondition": "Good — minor wear",
    "approxSize": "Medium (fits in a bag)",
    "location": "Kothrud, Pune",
    "imageUrl": null,
    "emoji": "🪵",
    "exchangeType": "FREE",
    "preferredExchangeItem": null,
    "isAvailable": true,
    "createdAt": "2026-10-25T23:05:00"
  }
]
```

---

### 2.2 Search Available Items
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/items/search?q=wood`
- **Response (`200 OK`)**: Returns matching items where query string matches title, category, description, or location.

---

### 2.3 Create Item Listing (JSON)
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/items`
- **Headers**: `Content-Type: application/json`
- **Request Body**:
```json
{
  "ownerId": 1,
  "title": "Ceramic Tea Cups Set",
  "description": "Set of 4 handmade ceramic mugs in excellent condition.",
  "category": "Kitchen & Home",
  "itemCondition": "Like new",
  "approxSize": "Small (fits in hand)",
  "location": "Kothrud, Pune",
  "exchangeType": "EXCHANGE",
  "preferredExchangeItem": "Indoor succulent planter"
}
```
- **Response (`201 Created`)**: Returns the persisted `ItemResponseDto`.

---

### 2.4 Create Item Listing with Image File (Multipart)
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/items`
- **Body**: `form-data`
  - `ownerId`: `1`
  - `title`: `Study Table Lamp`
  - `itemCondition`: `Good — minor wear`
  - `location`: `Kothrud, Pune`
  - `exchangeType`: `FREE`
  - `image`: *(Select image file from computer)*

---

## 3. Rule-Based AI Analysis & Suggestions

### 3.1 Analyze Object Keywords
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/reuse-ideas/analyze`
- **Headers**: `Content-Type: application/json`
- **Request Body**:
```json
{
  "objectName": "Old Wooden Box",
  "condition": "Good — minor wear"
}
```
- **Response (`200 OK`)**:
```json
{
  "detectedObject": "Old Wooden Box",
  "category": "Furniture / Storage",
  "material": "Solid wood, natural finish",
  "condition": "Good — minor wear",
  "confidenceScore": 94,
  "emoji": "🪵",
  "ideas": [
    {
      "title": "Storage Box",
      "description": "Use it to organise stationery, tools, books or winter clothes inside a cupboard.",
      "icon": "📦"
    },
    {
      "title": "Plant Holder",
      "description": "Line it with plastic sheeting and turn it into a rustic planter for herbs on your balcony.",
      "icon": "🪴"
    },
    {
      "title": "Mini Bookshelf",
      "description": "Mount it sideways on a wall to display paperbacks, photos or indoor succulents.",
      "icon": "🗄️"
    },
    {
      "title": "Decorative Furniture",
      "description": "Sand and repaint or polish it to serve as a rustic side table or footstool.",
      "icon": "🛋️"
    },
    {
      "title": "Craft Organiser",
      "description": "Add cardboard dividers to neatly store art supplies, cables or sewing kits.",
      "icon": "🎨"
    }
  ]
}
```

---

## 4. Requests & Handover Endpoints

### 4.1 Submit Item Request
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/requests`
- **Headers**: `Content-Type: application/json`
- **Request Body**:
```json
{
  "itemId": 2,
  "requesterId": 1,
  "message": "Hi Rohan, I would love to exchange for this study chair!"
}
```
- **Response (`201 Created`)**: Returns `RequestResponseDto` with status `PENDING`.
- **Validation Note**: If `requesterId` matches the item's `ownerId`, the backend returns `400 Bad Request` ("You cannot request an item that you posted yourself").

---

### 4.2 Get Incoming Requests for an Owner
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/requests/incoming/2`
- **Response (`200 OK`)**: Returns list of incoming requests on items posted by user 2.

---

### 4.3 Accept or Reject Request
- **Method**: `PUT`
- **URL**: `http://localhost:8080/api/requests/1/status`
- **Headers**: `Content-Type: application/json`
- **Request Body**:
```json
{
  "ownerId": 2,
  "status": "ACCEPTED"
}
```
- **Response (`200 OK`)**: Returns updated request object with status `"ACCEPTED"`.

---

## 5. User Profile & Stats

### 5.1 Get User Statistics
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/users/1/stats`
- **Response (`200 OK`)**:
```json
{
  "itemsPosted": 0,
  "itemsGiven": 0,
  "itemsReceived": 1,
  "activeRequests": 2
}
```

---

## 6. How to Import the Postman Collection

1. Open Postman.
2. Click **Import** in the upper left corner.
3. Select the file: `docs/One_More_Use.postman_collection.json`.
4. Click **Import**.
5. All 12 requests will appear in a folder named **One More Use Platform APIs**, ready to execute against `http://localhost:8080`.
