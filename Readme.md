## Build && Run

```
cd dependencies
docker compose -f all-compose.yml build
docker compose -f all-compose.yml up
```

As db is mapped on the host machine - to clean the DB:
```
docker compose -f all-compose.yml down
rm -rf db/postgres/*
```

## Test cases
1. To get all devices:
```
curl -X GET -H 'Content-Type: application/json' http://localhost:8080/phone-bookings
```

Response should be:
```
[
  {
    "id": 1,
    "name": "Samsung Galaxy S9",
    "status": "BOOKED",
    "bookedBy": "fan",
    "lastBookTime": "2021-06-23 23:36:32 +0300"
  },
  {
    "id": 2,
    "name": "Samsung Galaxy S8",
    "status": "BOOKED",
    "bookedBy": "fan",
    "lastBookTime": "2021-06-23 23:54:09 +0300"
  },
  {
    "id": 3,
    "name": "Samsung Galaxy S7",
    "status": "BOOKED",
    "bookedBy": "fan",
    "lastBookTime": "2021-06-23 23:54:10 +0300"
  },
  {
    "id": 4,
    "name": "Motorola Nexus 6",
    "status": "BOOKED",
    "bookedBy": "fan",
    "lastBookTime": "2021-06-23 23:54:12 +0300"
  },
  {
    "id": 5,
    "name": "LG Nexus 5X",
    "status": "BOOKED",
    "bookedBy": "fan",
    "lastBookTime": "2021-06-23 23:54:13 +0300"
  },
  {
    "id": 6,
    "name": "Huawei Honor 7X",
    "status": "BOOKED",
    "bookedBy": "fan",
    "lastBookTime": "2021-06-23 23:54:14 +0300"
  },
  {
    "id": 7,
    "name": "Apple iPhone X",
    "status": "BOOKED",
    "bookedBy": "fan",
    "lastBookTime": "2021-06-23 23:54:16 +0300"
  },
  {
    "id": 8,
    "name": "Apple iPhone 8",
    "status": "BOOKED",
    "bookedBy": "fan",
    "lastBookTime": "2021-06-23 23:54:17 +0300"
  },
  {
    "id": 9,
    "name": "Apple iPhone 4s",
    "status": "BOOKED",
    "bookedBy": "fan",
    "lastBookTime": "2021-06-23 23:54:18 +0300"
  }, 
  {
    "id": 10,
    "name": "Nokia 3310",
    "status": "AVAILABLE",
    "bookedBy": null,
    "lastBookTime": "2021-06-23 23:59:50 +0300"
  }
]
```

2. To get all available devices:
```
curl -X GET -H 'Content-Type: application/json' http://localhost:8080/phone-bookings?onlyAvailable=true
```

Response should be:
```
[
  {
    "id": 10,
    "name": "Nokia 3310",
    "status": "AVAILABLE",
    "bookedBy": null,
    "lastBookTime": "2021-06-23 23:59:50 +0300"
  }
]
```

3. To reserve a devices. The user is in a headers for simplicity:
```
curl -X PUT -H 'user: fan' -H 'Content-Type: application/json' http://localhost:8080/phone-bookings/10
```

Response should be:
```
{
  "id": 10,
  "name": "Nokia 3310",
  "status": "BOOKED",
  "bookedBy": "fan",
  "lastBookTime": "2021-06-24 00:04:11 +0300"
}
```

4. To reserve again the same device (or any already reserved). The user is in a headers for simplicity:
```
curl -X PUT -H 'user: fan' -H 'Content-Type: application/json' http://localhost:8080/phone-bookings/10
```

Error response should be `400 Bad Request` with the message:
```
{
  "error": "Phone 'Nokia 3310' was reserved at '2021-06-24T00:04:11.374374+03:00' by 'fan'"
}
```

5. To reserve unknown device. The user is in a headers for simplicity:
```
curl -X PUT -H 'user: fan' -H 'Content-Type: application/json' http://localhost:8080/phone-bookings/13
```

Error response should be `404 Not Found` with the message:
```
{
  "error": "The phone with id '13' was not found"
}
```

6. To release a devices. The user is in a headers for simplicity:
```
curl -X DELETE -H 'user: fan' -H 'Content-Type: application/json' http://localhost:8080/phone-bookings/10
```

Response should be:
```
{
  "id": 10,
  "name": "Nokia 3310",
  "status": "AVAILABLE",
  "bookedBy": null,
  "lastBookTime": "2021-06-24 00:11:43 +0300"
}
```

7. To release again the same device (or any already released). The user is in a headers for simplicity:
```
curl -X DELETE -H 'user: fan' -H 'Content-Type: application/json' http://localhost:8080/phone-bookings/10
```

Error response should be `400 Bad Request` with the message:
```
{
  "error": "Phone 'Nokia 3310' is not reserved"
}
```

5. To release unknown device. The user is in a headers for simplicity:
```
curl -X DELETE -H 'user: fan' -H 'Content-Type: application/json' http://localhost:8080/phone-bookings/13
```

Error response should be `404 Not Found` with the message:
```
{
  "error": "The phone with id '13' was not found"
}
```

6. To release device reserved by other person. The user is in a headers for simplicity:
```
curl -X DELETE -H 'user: not-fan' -H 'Content-Type: application/json' http://localhost:8080/phone-bookings/1
```

Error response should be `400 Bad Request` with the message:
```
{
  "error": "Phone 'Samsung Galaxy S9' is not reserved on you"
}
```
