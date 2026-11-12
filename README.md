# API Contract Gardain

A Java + Spring Boot project for learning API contract comparison. Detects breaking and non-breaking changes between two versions of an API endpoint.

## 🎯 Purpose

This project helps you understand:
- How API contracts change between versions
- What constitutes a breaking change (field removal, type change, becoming required)
- How to build a REST API with Spring Boot
- How to validate incoming requests with Bean Validation
- How to write unit tests with JUnit 5

## 🛠 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Programming language |
| Spring Boot | 4.1.2 | Application framework |
| Spring Web MVC | — | REST controller |
| Bean Validation (Jakarta) | — | Input validation |
| JUnit 5 | — | Unit testing |
| Maven | 3.10.0 | Build tool |

## 📁 Project Structure

```
src/main/java/com/apiguardian/api_contract_gardain/
├── ApiContractGardainApplication.java   # Spring Boot entry point
├── ApiEndpoint.java                     # Record: endpoint path, method, fields
├── ApiField.java                        # POJO: field name, type, required flag
├── ApiFieldRequest.java                 # DTO: mutable field for JSON input
├── ComparisonRequest.java               # DTO: old + new endpoint data
├── ComparisonResult.java                # DTO: compatible flag + changes list
├── ContractComparator.java              # Core: compares old vs new endpoints
└── ContractComparisonController.java    # REST: POST /compare, GET /health

src/test/java/com/apiguardian/api_contract_gardain/
├── ApiContractGardainApplicationTests.java  # Context load test
├── ContractComparatorTest.java              # 18 unit tests for comparator
└── ContractComparisonControllerTest.java    # Controller tests
```

## 🏗 Build & Run

### Prerequisites
- Java 21+
- Maven Wrapper (`./mvnw`) is included

### Compile
```bash
./mvnw clean compile
```

### Run Tests
```bash
./mvnw clean test
```
All 21 tests should pass with **BUILD SUCCESS**.

### Run the Application
```bash
./mvnw spring-boot:run
```
The app starts on `http://localhost:8080`

## 🌐 API Endpoints

### POST /api/contracts/compare
Compares two API endpoints and returns compatibility status.

**Request Body:**
```json
{
  "oldPath": "/users",
  "oldMethod": "GET",
  "oldFields": [
    { "fieldName": "id", "fieldType": "INTEGER", "required": true },
    { "fieldName": "name", "fieldType": "STRING", "required": true }
  ],
  "newPath": "/users",
  "newMethod": "GET",
  "newFields": [
    { "fieldName": "id", "fieldType": "STRING", "required": true },
    { "fieldName": "name", "fieldType": "STRING", "required": true },
    { "fieldName": "phone", "fieldType": "STRING", "required": false }
  ]
}
```

**Response:**
```json
{
  "compatible": false,
  "changes": [
    "TYPE_CHANGED: id [ INTEGER -> STRING ]",
    "FIELD_ADDED: phone"
  ]
}
```

### GET /api/contracts/health
Health check endpoint.

**Response:**
```
API Contract Gardain is running
```

## 🔍 Change Types Detected

| Change Type | Description | Breaking? |
|-------------|-------------|-----------|
| `FIELD_REMOVED` | Field exists in old but not in new | ✅ Yes |
| `TYPE_CHANGED` | Field's data type changed (e.g. INTEGER → STRING) | ✅ Yes |
| `BECAME_REQUIRED` | Field was optional, now mandatory | ✅ Yes |
| `BECAME_OPTIONAL` | Field was required, now optional | ❌ No |
| `FIELD_ADDED` | New field introduced | ❌ No |

An endpoint pair is **compatible** only if no breaking changes are detected.

## ✅ Validation Rules

- `oldPath`, `oldMethod`, `newPath`, `newMethod` — must not be blank (`@NotBlank`)
- `oldFields`, `newFields` — must not be null and must contain at least 1 item (`@NotNull`, `@Size(min=1)`)
- Each field's `fieldName` and `fieldType` — must not be blank (`@NotBlank`)
- Each field's `required` — must not be null (`@NotNull`)

Invalid requests return **HTTP 400** with validation error details.

## 🧪 Test Coverage

| Test Class | Tests | What's Tested |
|------------|-------|---------------|
| `ContractComparatorTest` | 18 | Type changes, required changes (both directions), field add/remove, compatible/incompatible scenarios |
| `ContractComparisonControllerTest` | 2 | POST /compare returns changes, GET /health returns status |
| `ApiContractGardainApplicationTests` | 1 | Spring context loads |

Total: **21 tests, all passing**

## 📚 Learning Notes

### Key Concepts Demonstrated

- **Java Records** — `ApiEndpoint` is an immutable data carrier
- **Immutability** — `ApiField` has `final` fields with no setters
- **DTO Pattern** — `ApiFieldRequest` is mutable for JSON deserialization, separate from immutable domain model
- **HashMap Lookup** — Fields are matched by name using `HashMap` for O(1) average lookup
- **Bean Validation** — `@Valid` on `@RequestBody` triggers automatic validation
- **ResponseEntity** — Wraps response body with HTTP status code

## 📝 License

This is a learning project. No license specified.