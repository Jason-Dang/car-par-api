# Car Park Managenment API

## Overview
Simple car park management API with in-memory storage

### What it does:
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
- 
