# Car Park Management API


## Overview
Car park management API with authentication and role-based access control.

### Tech
- Lang: Java 25
- Framework: Spring Boot (Web MVC)
- Security: Spring Security + Keycloak (OAuth2 / JWT)
- ORM: Spring Data JPA
- Database: H2 (in-memory, includes console)
- Integration Test Runner: SpringBootTest
- Unit Test Runner: JUnit5
- Mocking: Mockito


### What it does
- Allocating vehicles to the first available parking space
- Determine the number of available and full spaces
- Determine the parking charge on vehicle exit
- De-allocate a parking space on vehicle exit
- Vehicles are charged per minute they are parked:
    - Small Car — £0.10/minute (type 1)
    - Medium Car — £0.20/minute (type 2)
    - Large Car — £0.40/minute (type 3)
- Every 5 minutes an additional charge of £1 is added
- The `vehicleType` request field must be `1`, `2`, or `3`


### Authentication & Authorisation
All `/api/**` endpoints require a valid JWT issued by the Keycloak realm `carparkapi`.

| Role    | Access                                      |
|---------|---------------------------------------------|
| `USER`  | `GET /api/parking`, `POST /api/parking`, `POST /api/parking/parkingBill` |
| `ADMIN` | Everything above + `GET /api/admin/**`      |

Tokens are obtained from Keycloak using the Resource Owner Password Credentials (ROPC) grant or the PKCE Authorization Code flow via the `carparkapi-client` client.

> **Note:** `directAccessGrantsEnabled` is `true` on the client for local development convenience. Disable this before deploying to a shared or production environment.


### Data Model

- **ParkingSpace**
  - `spaceNumber` : Integer | Primary Key

- **OccupiedParkingSpace**
  - `spaceNumber` : Integer | Primary Key
  - `vehicleReg` : String | Index
  - `vehicleType` : Integer
  - `timeIn` : Datetime

- **ParkingBill**
  - `billId` : String | Primary Key
  - `vehicleReg` : String | Index
  - `vehicleCharge` : Double
  - `timeIn` : Datetime
  - `timeOut` : Datetime


### Improvement Notes
- User input sanitising (code/SQL injection prevention)
- Rate limiting
- Improved error handling
- Improved test coverage/quality/efficiency
- Use of Lombok to further reduce boilerplate
- Add a GUI
- Endpoint expansion (e.g. managing parking spaces, accessing bills, multiple car parks)


### Endpoints

All endpoints are prefixed with `/api`.

#### `GET /api/parking`
Returns the count of available and occupied spaces. Requires `USER` role.

**Response**
```json
{
  "availableSpaces": 0,
  "occupiedSpaces": 0
}
```

#### `POST /api/parking`
Parks a vehicle in the first available space. Requires `USER` role.

**Request**
```json
{
  "vehicleReg": "AB12CDE",
  "vehicleType": 1
}
```

**Response**
```json
{
  "vehicleReg": "AB12CDE",
  "spaceNumber": 1,
  "timeIn": "2024-01-01T10:00:00"
}
```

#### `POST /api/parking/parkingBill`
Frees the vehicle's space and returns its parking charge. Requires `USER` role.

**Request**
```json
{
  "vehicleReg": "AB12CDE"
}
```

**Response**
```json
{
  "billId": "...",
  "vehicleReg": "AB12CDE",
  "vehicleCharge": 1.50,
  "timeIn": "2024-01-01T10:00:00",
  "timeOut": "2024-01-01T10:15:00"
}
```

#### `GET /api/admin/parking/summary`
Returns a full breakdown of occupied spaces. Requires `ADMIN` role.


---

## Setup

### Prerequisites
- Java 25+
- Maven (or use the included `./mvnw` wrapper)
- Docker (for Keycloak + Postgres)
- A `dockerlocal` repo providing the Keycloak-Postgres compose file at `providers/keycloak-postgres/compose.yml`


### 1. Environment

Copy the example env file and fill in all blank values:

```bash
cp .env.example .env
```

| Variable                | Purpose                                         |
|-------------------------|-------------------------------------------------|
| `DOCKER_LOCAL`          | Absolute path to your `dockerlocal` repo        |
| `KC_DB_ROOT_PASSWORD`   | Postgres root password for Keycloak's DB        |
| `KC_DB_DATABASE`        | Keycloak database name                          |
| `KC_DB_USER`            | Keycloak database user                          |
| `KC_DB_PASSWORD`        | Keycloak database password                      |
| `KC_USER`               | Keycloak admin username                         |
| `KC_PASSWORD`           | Keycloak admin password                         |
| `H2_USER`               | H2 console username                             |
| `H2_PASSWORD`           | H2 console password                             |
| `KC_TEST_USER_PASSWORD` | Initial password for the `testuser` test account |
| `KC_TEST_ADMIN_PASSWORD`| Initial password for the `testadmin` test account|

> The `.env` file is gitignored. Never commit credentials.


### 2. Start Keycloak

```bash
docker compose up -d
```

Keycloak starts on **port 7080** and auto-imports the realm from `keycloak/carparkapi-realm.json`.  
The test user passwords are injected from the env vars `KC_TEST_USER_PASSWORD` / `KC_TEST_ADMIN_PASSWORD` at import time.

Both test accounts have `temporary: true` on their passwords — Keycloak will require a password change on first login via the browser. For direct grant (ROPC) usage in Postman/curl, update the passwords in the Keycloak admin console first.

Keycloak admin console: `http://localhost:7080`


### 3. Configure the App

In `src/main/resources/application.properties`:

| Property | Default | Description |
|----------|---------|-------------|
| `spring.security.oauth2.resourceserver.jwt.issuer-uri` | `http://localhost:7080/realms/carparkapi` | Keycloak issuer URI |
| `app.config.totalSpaces` | `20` | Number of parking spaces to initialise |
| `spring.datasource.username` | _(blank)_ | H2 database user |
| `spring.datasource.password` | _(blank)_ | H2 database password |


### 4. Run Tests
```bash
# Unit tests only
./mvnw test -Punit

# Integration tests only
./mvnw test -Pintegration

# All tests
./mvnw test
```


### 5. Run the App
```bash
./mvnw spring-boot:run
```

The API starts on **port 8080**.  
H2 console: `http://localhost:8080/h2-console`


### 6. Obtain a Token

Using the ROPC grant (requires `directAccessGrantsEnabled: true` on the client — dev only):

```bash
curl -s -X POST http://localhost:7080/realms/carparkapi/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=carparkapi-client" \
  -d "username=testuser" \
  -d "password=<your-password>" \
  | jq -r '.access_token'
```

Pass the token as a `Bearer` header on subsequent API calls:

```
Authorization: Bearer <token>
```


### Postman

A Postman collection is in the `docs/` directory covering all endpoints.  
Set a Postman environment variable `baseUrl` to `http://localhost:8080` and `token` to the access token obtained above.