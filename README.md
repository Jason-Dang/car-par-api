# Car Park Managenment API


## Overview
Simple car park management API with in-memory storage

### Tech
- Lang: Java 25
- Framework: Spring Boot (Web MVC)
- ORM: Spring Data JPA
- Database: H2 (In-Memory + includes console) 
- Integration Test Runner: SpringBootTest
- Unit Test Runner: JUnit5
- Mocking: Mockito (surefire maven plugin for mockito core to avoid self attaching warning)
  - > Mockito is currently self-attaching to enable the inline-mock-maker. This will no longer work in future releases of the JDK. Please add Mockito as an agent to your build what is described in Mockito's documentation: https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#0.3


### What it does
- Allocating vehicles to the first available parkingSpace
- Determine the number of available and full spaces
- Determine the parking charge on vehicle exit
- De-Allocate a parkingSpace on vehicle exit
- Vehicles will be charged per minute they are parked
- The parking charges are:
    - Small Car - £0.10/minute (1)
    - Medium Car - £0.20/minute (2)
    - Large Car £0.40/minute (3)
- Every 5 minutes an additional charge of £1 will be added
- The Vehicle Type argument should be either 1, 2 or 3


### Data Model Notes

- Spaces
  - availableSpaces : Collection<availableSpace>
    - availableSpace : Entity
      - spaceNumber : Integer | Primary Key
  - occupiedSpaces : Collection<occupiedSpace>
    - occupiedSpace : Entity
      - spaceNumber : Integer | Primary Key
      - vehicleReg : String | Index
      - vehicleType : Integer
      - timeIn : Datetime

- Bill
  - billId - String | Primary Key
  - vehicleReg - String | Index
  - vehicleCharge - Double | BigDecimal?
  - timeIn - Datetime
  - timeOut - Datetime


### Improvement Notes
- Auth
- Rate Limiting
- Improved error handling
- Improved test coverage/quality/efficiency
- Use of lombok to further reduce boilerplate
- Add a GUI
- Endpoint expansion (e.g. managing parking spaces and accessing bills + multiple car parks etc.)


### Endpoints
#### 1. `GET /parking`

- Description: Gets available and occupied number of spaces
- Response:
```json
{
  "availableSpaces": int,
  "occupiedSpaces": int
}
```

#### 2. `POST /parking`

- Description: Parks a given vehicle in the first available parkingSpace and returns the vehicle and its parkingSpace number
- Request Body:
```json
{
  "vehicleReg": string,
  "vehicleType": int
}
```
- Response:
```json
{
  "vehicleReg": string,
  "spaceNumber": int,
  "timeIn": date-time
}
```


#### 3. `POST /parking/parkingBill`

- Description: Frees up this vehicles parkingSpace and return its final charge from its parking time until now
- Request Body:
```json
{
  "vehicleReg": string,
}
```
- Response:
```json
{
  "billId": string,
  "vehicleReg": string,
  "vehicleCharge": double,
  "timeIn": date-time,
  "timeOut": date-time
}
```



## Instructions

### Configuration

In `src/main/resources/application.properties`
Configure:
  - `app.config.totalSpaces` Initialise number of parking spaces
  - `spring.datasource.username` Database user (defaults to `sa`)
  - `spring.datasource.password` Database password (defaults to blank)


### Run Tests
- Unit: `./mvnw test -Punit`
- Integration: `./mvnw test -Pintegration`
- All: `./mvnw test`


### Run App
- Maven: `./mvnw spring-boot:run`


### Postman
There is a postman collection located in the `docs/` directory.
This contains requests for the 3 endpoints outline in the [Endpoints](#Endpoints) section.
A Postman environment can be configured to populate the `{{baseUrl}}` variable with `localhost:8080` (or just hardcode it).
