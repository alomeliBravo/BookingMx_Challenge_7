# BookingMx

A hotel reservation management system built with Spring Boot (backend) and vanilla JavaScript (frontend). This project demonstrates REST API development, graph algorithms for nearby city searches, and comprehensive testing practices including unit tests, integration tests, and BDD with Cucumber.

## Project Overview

BookingMx is a full-stack application designed for managing hotel reservations in Mexico. The system provides:

- **Reservation Management**: Create, read, update, and cancel hotel reservations
- **RESTful API**: Spring Boot backend with proper error handling and validation
- **Nearby Cities Feature**: Graph-based algorithm to find hotels in nearby cities within a specified distance
- **Status Tracking**: Monitor reservation states (ACTIVE/CANCELED)
- **Input Validation**: Ensure data integrity with business rule validation (future dates, check-out after check-in, etc.)

### Key Features

- CRUD operations for hotel reservations
- Graph data structure for modeling city distances
- Nearby city search algorithm (currently supports direct connections within 250km)
- In-memory data storage for rapid development and testing
- Cross-origin resource sharing (CORS) enabled for frontend integration
- Comprehensive test coverage with JUnit, Cucumber, and Jest

## Architecture

### Backend (Spring Boot)
- **Controller Layer**: REST endpoints for reservation operations
- **Service Layer**: Business logic and validation
- **Repository Layer**: In-memory data persistence
- **Exception Handling**: Custom exceptions with proper HTTP status codes
- **DTOs**: Separate request/response objects for clean API contracts

### Frontend (Vanilla JavaScript)
- **API Client**: Modular HTTP client for backend communication
- **Graph Module**: Pure functions for city distance calculations
- **Sample Data**: Pre-configured cities and distances in Jalisco region

## Installation Instructions

### Prerequisites

- **Java**: JDK 17 or higher
- **Maven**: 3.6+ (or use the Maven wrapper included)
- **Node.js**: 16+ and npm
- **Git**: For cloning the repository

### Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Install dependencies and run the application:
```bash
mvn clean install
mvn spring-boot:run
```

3. The API will be available at `http://localhost:8080`

4. Verify the server is running:
```bash
curl http://localhost:8080/api/reservations
```

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm run serve
```

4. Access the application at `http://localhost:5173`

### Running Both Services

For development, you'll need two terminal windows:

**Terminal 1 (Backend):**
```bash
cd backend && mvn spring-boot:run
```

**Terminal 2 (Frontend):**
```bash
cd frontend && npm run serve
```

## Testing

This project includes comprehensive testing implemented across multiple sprints:
- **Sprint 1**: Unit tests for service layer and repository (JUnit + Mockito)
- **Sprint 2**: Frontend unit tests for API client and graph algorithms (Jest)
- **Sprint 3**: Integration tests with Cucumber BDD and exception handling tests

### Backend Tests

The backend test suite includes:

#### 1. Unit Tests (JUnit + Mockito)
- **ReservationTest**: Tests for reservation business logic, validation, and model behavior
  - Date validation (check-in in future, check-out after check-in)
  - Null date handling
  - Reservation equality and hash code
  - Getters and setters
  - Status checking (ACTIVE/CANCELED)

#### 2. Exception Handling Tests (MockMvc)
- **ApiExceptionHandlerTest**: Tests for proper HTTP error responses
  - 404 Not Found responses
  - 400 Bad Request responses
  - 500 Internal Server Error responses
  - JSON error body structure validation

#### 3. Integration Tests (Cucumber BDD)
- **Feature: Reservation Module** with 7 scenarios:
  - Get empty list of reservations
  - Create and verify reservation exists
  - Delete/cancel reservation
  - Delete non-existent reservation (error case)
  - Update reservation
  - Update non-existent reservation (error case)
  - Find non-existent reservation (error case)

#### Running Backend Tests

**Run all tests:**
```bash
cd backend
mvn test
```

**Run only unit tests:**
```bash
mvn test -Dtest=ReservationTest
```

**Run only Cucumber tests:**
```bash
mvn test -Dtest=CucumberTestRunner
```

#### Example Backend Test Output

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.bookingmx.reservations.steps.ReservationTest
[INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.482 s
[INFO] 
[INFO] Running com.bookingmx.reservations.steps.ApiExceptionHandlerTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.321 s
[INFO]
[INFO] Running com.bookingmx.reservations.CucumberTestRunner
[INFO] 
Feature: Reservation Module

  Scenario: Get Empty List of Reservations                    # features/reservation.feature:6
    Given The API is running                                   # PASSED
    When I call GET reservations endpoint                      # PASSED
    Then the response status should be 200 and Content-Type JSON # PASSED
    Then the response must be an empty array                   # PASSED

  Scenario: Create a new Reservation correctly and check if Exists # features/reservation.feature:11
    When I create a new reservation                            # PASSED
    Then the response status should be 200                     # PASSED
    When I call GET reservations endpoint                      # PASSED
    Then the response must be an array with 1 reservations     # PASSED

  Scenario: Delete a Reservation correctly                     # features/reservation.feature:17
    When I create a new reservation                            # PASSED
    Then the response status should be 200                     # PASSED
    When I call DELETE reservation endpoint with id 1          # PASSED
    Then the response status should be 200                     # PASSED
    Then the Reservation with id 1 must be CANCELED            # PASSED
    Then the response status should be 200                     # PASSED

  [... 4 more scenarios ...]

[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.235 s
[INFO]
[INFO] Results:
[INFO] 
[INFO] Tests run: 24, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[SUCCESS] BUILD SUCCESS
```

#### Sample Backend Test Cases

**Unit Test Example:**
```java
@Test
void create_withCheckOutBeforeCheckIn_throwsException() {
    ReservationRequest req = new ReservationRequest();
    req.setGuestName("John");
    req.setHotelName("Hotel A");
    req.setCheckIn(LocalDate.now().plusDays(5));
    req.setCheckOut(LocalDate.now().plusDays(3));

    BadRequestException ex = assertThrows(
        BadRequestException.class, 
        () -> service.create(req)
    );
    assertEquals("Check-out must be after check-in", ex.getMessage());
}

@Test
void testEqualsAndHashCode() {
    Reservation r1 = new Reservation(1L, "Guest", "Hotel", 
        LocalDate.now(), LocalDate.now().plusDays(1));
    Reservation r2 = new Reservation(1L, "Guest2", "Hotel2", 
        LocalDate.now(), LocalDate.now().plusDays(2));
    Reservation r3 = new Reservation(2L, "Guest", "Hotel", 
        LocalDate.now(), LocalDate.now().plusDays(1));

    assertEquals(r1, r2);
    assertEquals(r1.hashCode(), r2.hashCode());
    assertNotEquals(r1, r3);
}
```

**Exception Handler Test Example:**
```java
@Test
void getReservation_notFoundException_returns404() throws Exception {
    when(service.findById(1L))
        .thenThrow(new NotFoundException("Reservation not found"));

    mockMvc.perform(get("/api/reservations/1"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.message").value("Reservation not found"))
        .andExpect(jsonPath("$.timestamp").exists());
}
```

**Cucumber Feature File:**
```gherkin
Feature: Reservation Module

    Background: The server is running
        Given The API is running

    Scenario: Create a new Reservation correctly and check if Exists
      When I create a new reservation
      Then the response status should be 200
      When I call GET reservations endpoint
      Then the response must be an array with 1 reservations

    Scenario: Delete a Reservation which doesn't exist
      When I call DELETE reservation endpoint with id 5
      Then the response status should be 404
```

### Frontend Tests

The frontend test suite includes:

#### 1. Graph Module Tests (Jest)
Tests for the graph data structure and nearby cities algorithm:
- **Graph class operations**: adding cities, creating edges, getting neighbors
- **Input validation**: invalid city names, unknown cities, invalid distances
- **Data validation**: detecting duplicates, invalid edges, malformed data
- **Nearby cities algorithm**: distance filtering, sorting, edge cases
- **Sample data validation**: ensuring pre-configured data is valid

#### 2. API Client Tests (Jest)
Tests for HTTP communication with the backend:
- **listReservations**: Successful fetching and error handling
- **createReservation**: POST requests with payload validation
- **updateReservation**: PUT requests and error responses
- **cancelReservation**: DELETE requests and error handling
- **Error scenarios**: Network failures, validation errors, server errors

#### Running Frontend Tests

**Run all tests:**
```bash
cd frontend
npm test
```

**Run tests in watch mode:**
```bash
npm test -- --watch
```

**Run tests with coverage:**
```bash
npm test -- --coverage
```

#### Example Frontend Test Output

```
PASS  tests/graph.test.js
  Graph class
    ✓ addCity adds a new city (3ms)
    ✓ addCity ignores duplicates (2ms)
    ✓ addCity throws on invalid name (4ms)
    ✓ addEdge creates undirected edges (3ms)
    ✓ addEdge throws if cities unknown or distance invalid (5ms)
    ✓ neighbors returns adjacent nodes (2ms)
    ✓ neighbors throws on unknown city (2ms)
  validateGraphData
    ✓ valid data returns ok:true (2ms)
    ✓ rejects non-array inputs (3ms)
    ✓ rejects duplicate or invalid cities (2ms)
    ✓ rejects edges with unknown cities or invalid distance (3ms)
  buildGraph
    ✓ creates graph with correct connections (4ms)
  getNearbyCities
    ✓ returns nearby cities sorted by distance (3ms)
    ✓ returns empty for destination not in graph (2ms)
    ✓ returns empty if graph invalid (3ms)
  sampleData
    ✓ sampleData structure looks valid (2ms)

PASS  tests/api.test.js
  API module
    ✓ listReservations: returns reservations when fetch succeeds (15ms)
    ✓ listReservations: throws error when fetch fails (8ms)
    ✓ createReservation: posts data and returns response (10ms)
    ✓ createReservation: throws error when API returns error (9ms)
    ✓ updateReservation: updates reservation successfully (11ms)
    ✓ updateReservation: throws error when API returns error (8ms)
    ✓ cancelReservation: deletes reservation successfully (10ms)
    ✓ cancelReservation: throws error when API fails (9ms)

Test Suites: 2 passed, 2 total
Tests:       24 passed, 24 total
Snapshots:   0 total
Time:        2.841s
Ran all test suites.
```

#### Sample Frontend Test Cases

**Graph Module Test:**
```javascript
describe('getNearbyCities', () => {
    let g;
    beforeEach(() => {
        g = new Graph();
        ['A', 'B', 'C'].forEach(c => g.addCity(c));
        g.addEdge('A', 'B', 100);
        g.addEdge('A', 'C', 300);
    });

    test('returns nearby cities sorted by distance', () => {
        const result = getNearbyCities(g, 'A', 250);
        expect(result).toEqual([{ city: 'B', distance: 100 }]);
    });

    test('returns empty for destination not in graph', () => {
        expect(getNearbyCities(g, 'Z')).toEqual([]);
    });
});
```

**API Client Test:**
```javascript
test('createReservation: posts data and returns response', async () => {
    const payload = { guestName: 'Jane Doe' };
    const mockResponse = { id: 2, ...payload };

    fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockResponse
    });

    const result = await createReservation(payload);
    expect(fetch).toHaveBeenCalledWith(BASE_URL, expect.objectContaining({
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    }));
    expect(result).toEqual(mockResponse);
});

test('cancelReservation: throws error when API fails', async () => {
    fetch.mockResolvedValueOnce({
        ok: false,
        json: async () => ({ message: 'Cancel failed: not found' })
    });

    await expect(cancelReservation(4))
        .rejects.toThrow('Cancel failed: not found');
});
```

### Test Coverage

Generate detailed coverage reports:

**Backend (JaCoCo):**
```bash
cd backend
mvn test jacoco:report
```
View report: `target/site/jacoco/index.html`

**Frontend (Jest):**
```bash
cd frontend
npm test -- --coverage
```
View report: `coverage/lcov-report/index.html`

**Expected Coverage:**
- Backend Service Layer: >90%
- Backend Controller Layer: >85%
- Frontend API Module: 100%
- Frontend Graph Module: >95%

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api/reservations
```

### Endpoints

#### List All Reservations
```http
GET /api/reservations
```

**Response:**
```json
[
  {
    "id": 1,
    "guestName": "Juan Pérez",
    "hotelName": "Hotel Guadalajara",
    "checkIn": "2025-12-01",
    "checkOut": "2025-12-05",
    "status": "ACTIVE"
  }
]
```

#### Get Reservation by ID
```http
GET /api/reservations/{id}
```

**Success Response (200):**
```json
{
  "id": 1,
  "guestName": "Juan Pérez",
  "hotelName": "Hotel Guadalajara",
  "checkIn": "2025-12-01",
  "checkOut": "2025-12-05",
  "status": "ACTIVE"
}
```

**Error Response (404):**
```json
{
  "timestamp": "2025-11-08T10:30:00Z",
  "status": 404,
  "message": "Reservation not found"
}
```

#### Create Reservation
```http
POST /api/reservations
Content-Type: application/json

{
  "guestName": "María García",
  "hotelName": "Hotel Zapopan",
  "checkIn": "2025-12-10",
  "checkOut": "2025-12-15"
}
```

**Success Response (200):**
```json
{
  "id": 2,
  "guestName": "María García",
  "hotelName": "Hotel Zapopan",
  "checkIn": "2025-12-10",
  "checkOut": "2025-12-15",
  "status": "ACTIVE"
}
```

**Validation Error (400):**
```json
{
  "timestamp": "2025-11-08T10:30:00Z",
  "status": 400,
  "message": "Check-out must be after check-in"
}
```

#### Update Reservation
```http
PUT /api/reservations/{id}
Content-Type: application/json

{
  "guestName": "María García",
  "hotelName": "Hotel Chapala",
  "checkIn": "2025-12-10",
  "checkOut": "2025-12-15"
}
```

**Note:** Cannot update a CANCELED reservation (returns 400).

#### Cancel Reservation
```http
DELETE /api/reservations/{id}
```

**Success Response (200):**
```json
{
  "id": 1,
  "guestName": "Juan Pérez",
  "hotelName": "Hotel Guadalajara",
  "checkIn": "2025-12-01",
  "checkOut": "2025-12-05",
  "status": "CANCELED"
}
```

### Business Rules

- **Check-in date** must be in the future
- **Check-out date** must be after check-in date
- Both dates are required (cannot be null)
- Canceled reservations cannot be updated
- Guest name and hotel name are required

## 🗺️ City Graph Feature

The application includes a graph-based system for finding nearby cities, useful for suggesting alternative hotel locations.

### Sample Cities (Jalisco Region)

- Guadalajara (hub)
- Zapopan (12 km from Guadalajara)
- Tlaquepaque (10 km from Guadalajara)
- Tepatitlán (78 km from Guadalajara)
- Lagos de Moreno (85 km from Tepatitlán)
- Tala (35 km from Zapopan)
- Tequila (60 km from Guadalajara)

### Usage Example

```javascript
import { buildGraph, getNearbyCities, sampleData } from './graph.js';

const graph = buildGraph(sampleData.cities, sampleData.edges);
const nearby = getNearbyCities(graph, 'Guadalajara', 50);

console.log(nearby);
// [
//   { city: 'Tlaquepaque', distance: 10 },
//   { city: 'Zapopan', distance: 12 }
// ]
```

## 🛠️ Development

### Project Structure

```
bookingmx/
├── backend/
│   ├── src/
│   │   ├── main/java/com/bookingmx/reservations/
│   │   │   ├── controller/        # REST endpoints
│   │   │   ├── dto/               # Request/Response objects
│   │   │   ├── exception/         # Custom exceptions & handlers
│   │   │   ├── model/             # Domain entities
│   │   │   ├── repo/              # Data access layer
│   │   │   └── service/           # Business logic
│   │   └── test/java/com/bookingmx/reservations/
│   │       └── steps/             # JUnit & Cucumber tests
│   ├── src/test/resources/
│   │   └── features/              # Cucumber feature files
│   └── pom.xml
├── frontend/
│   ├── js/
│   │   ├── api.js                 # API client
│   │   └── graph.js               # Graph algorithms
│   ├── tests/
│   │   ├── api.test.js            # API tests
│   │   └── graph.test.js          # Graph tests
│   └── package.json
└── README.md
```

### Adding New Cities/Edges

Modify the `sampleData` object in `frontend/js/graph.js`:

```javascript
export const sampleData = {
  cities: ["City1", "City2", "City3"],
  edges: [
    { from: "City1", to: "City2", distance: 25 },
    { from: "City2", to: "City3", distance: 40 }
  ]
};
```

### Extending the Tests

**Adding a new Cucumber scenario:**
1. Add scenario to `backend/src/test/resources/features/reservation.feature`
2. Implement step definitions in `backend/src/test/java/.../steps/`
3. Run: `mvn test -Dtest=CucumberTestRunner`

**Adding a new Jest test:**
1. Add test case to appropriate file in `frontend/tests/`
2. Run: `npm test`

## 🐛 Troubleshooting

### Port Already in Use

**Backend:**
```bash
# Change port in application.properties
server.port=8081
```

**Frontend:**
```bash
# Vite will automatically suggest next available port
```

### CORS Issues

The backend is configured to accept requests from `localhost:5173` and `127.0.0.1:5173`. If using a different port, update `@CrossOrigin` in `ReservationController.java`.

### Tests Failing

1. Ensure all dependencies are installed:
```bash
# Backend
mvn clean install

# Frontend
npm install
```

2. Check that no services are running on test ports
3. Clear cache if needed:
```bash
# Maven
mvn clean

# npm
npm cache clean --force
```

4. For Cucumber tests, verify feature files are in `src/test/resources/features/`

### Cucumber Reports

After running tests, view the HTML report:
```bash
open backend/target/cucumber-reports/cucumber.html
```

## 📝 Testing Best Practices Demonstrated

This project showcases several testing best practices:

1. **Test Pyramid**: Unit tests (majority) → Integration tests → End-to-end scenarios
2. **BDD with Cucumber**: Human-readable test scenarios for stakeholder communication
3. **Mocking**: Using Mockito and Jest mocks to isolate units under test
4. **Test Organization**: Clear separation of unit, integration, and BDD tests
5. **Coverage**: Comprehensive coverage of happy paths and error cases
6. **Pure Functions**: Graph module uses pure functions for easy testing
7. **Test Fixtures**: Reusable test data and setup methods
8. **Descriptive Names**: Test names clearly describe what is being tested

## 📝 License

This project is for educational purposes.

## 👥 Contributors

Developed as part of a software testing and quality assurance course focusing on:
- Unit testing with JUnit and Jest
- Behavior-Driven Development with Cucumber
- Test doubles with Mockito
- Integration testing with Spring Boot Test
- Frontend testing with Jest

---

**Note**: This is an MVP with in-memory storage. Data will be lost on server restart. Future enhancements may include database persistence, authentication, and multi-hop city search algorithms.