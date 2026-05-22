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
- Docker and Docker Compose
- Java 25+ and Maven — only needed for the **standalone** workflow (the devcontainer provides them automatically)


### Environment

Copy the example env file and fill in all blank values before starting either workflow:

```bash
cp .env.example .env
```

| Variable                 | Purpose                                                    |
|--------------------------|------------------------------------------------------------|
| `KC_DB_ROOT_PASSWORD`    | Postgres superuser password                                |
| `KC_DB_DATABASE`         | Keycloak database name (created by `postgres/init.sh`)     |
| `KC_DB_USER`             | Keycloak database user (created by `postgres/init.sh`)     |
| `KC_DB_PASSWORD`         | Keycloak database user password                            |
| `KC_USER`                | Keycloak admin username                                    |
| `KC_PASSWORD`            | Keycloak admin password                                    |
| `H2_USER`                | H2 console username                                        |
| `H2_PASSWORD`            | H2 console password                                        |
| `KC_TEST_USER_PASSWORD`  | Initial password for the `testuser` test account           |
| `KC_TEST_ADMIN_PASSWORD` | Initial password for the `testadmin` test account          |

> `.env` is gitignored — never commit credentials.


---

## Dev Container (recommended)

Opens the full stack — Postgres, Keycloak, and a Java 25 dev container — in one step. No local JDK or Maven installation required.

### VS Code

1. Install the [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)
2. Open the repo, then run **Dev Containers: Reopen in Container** from the command palette

### Dev Container CLI

```bash
npm install -g @devcontainers/cli
devcontainer up --workspace-folder .
devcontainer exec --workspace-folder . bash
```

### What starts automatically

| Service   | Internal address       | Host port     |
|-----------|------------------------|---------------|
| Postgres  | `postgres:5432`        | —             |
| Keycloak  | `keycloak:8080`        | `7080`        |
| App shell | `/workspace` container | `8080` (when you run the app) |

Keycloak auto-imports the realm from `keycloak/carparkapi-realm.json` and injects test user passwords from `KC_TEST_USER_PASSWORD` / `KC_TEST_ADMIN_PASSWORD`.

> **Keycloak JWT in the devcontainer**: the app container sets  
> `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI=http://keycloak:8080/...`  
> which overrides `issuer-uri` from `application.properties`. The JWK decoder fetches public keys from the internal Docker hostname and skips issuer claim validation — this is intentional for the devcontainer environment.

### Running inside the container

```bash
# Tests
./mvnw test -Punit
./mvnw test -Pintegration

# Run the app (accessible at http://localhost:8080 on your host)
./mvnw spring-boot:run
```

### Obtain a token (from your host machine)

```bash
curl -s -X POST http://localhost:7080/realms/carparkapi/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=carparkapi-client" \
  -d "username=testuser" \
  -d "password=<KC_TEST_USER_PASSWORD>" \
  | jq -r '.access_token'
```

> Both test accounts have `temporary: true` on their passwords. For ROPC (Postman/curl) use, update the passwords via the Keycloak admin console at `http://localhost:7080` before first use.


---

## Standalone (local JDK)

Run Keycloak + Postgres in Docker while the Spring Boot app runs directly on your machine.

### 1. Start services

```bash
docker compose up -d
```

Keycloak is available at **`http://localhost:7080`** once healthy (~30 s).

### 2. Configure the app

`src/main/resources/application.properties` is pre-configured for this mode (`issuer-uri=http://localhost:7080/realms/carparkapi`). No changes needed.

| Property | Default | Description |
|----------|---------|-------------|
| `spring.security.oauth2.resourceserver.jwt.issuer-uri` | `http://localhost:7080/realms/carparkapi` | Must match Keycloak `KC_HOSTNAME_URL` |
| `app.config.totalSpaces` | `20` | Number of parking spaces to initialise |

### 3. Run tests

```bash
./mvnw test -Punit
./mvnw test -Pintegration
./mvnw test
```

### 4. Run the app

```bash
./mvnw spring-boot:run
```

API on **port 8080** · H2 console at `http://localhost:8080/h2-console`

### 5. Obtain a token

```bash
curl -s -X POST http://localhost:7080/realms/carparkapi/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=carparkapi-client" \
  -d "username=testuser" \
  -d "password=<KC_TEST_USER_PASSWORD>" \
  | jq -r '.access_token'
```

Pass the token as a `Bearer` header on subsequent API calls:

```
Authorization: Bearer <token>
```


---

## Postman

A Postman collection is in the `docs/` directory covering all endpoints.  
Set a Postman environment variable `baseUrl` to `http://localhost:8080` and `token` to the access token obtained above.