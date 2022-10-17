# Meetings_API
This repository contains REST API for managing Meetings.    
You can start project by opening terminal and going to root directory and executing command:
```
./mvnw spring-boot:run
```
JDK version 11.0.16.1

### Add a meeting
POST localhost:8080/meetings    
BODY schema:
```
{
    name: String,
    responsiblePerson: {id: String, name: String},
    description: String,
    category: Enum:[CodeMonkey, Hub, Short, TeamBuilding],
    type: Enum: [Live, InPerson],
    startDate: String(yyyy-MM-dd'T'HH:mm:ss),
    endDate: String(yyyy-MM-dd'T'HH:mm:ss)
}
```

example body:
```
{
    "name": "Meeting1",
    "responsiblePerson": {"id":"id3", "name":"Joe Doe"},
    "description": "Long text all day ",
    "category":"TeamBuilding",
    "type": "InPerson",
    "startDate": "2022-10-14T11:45:00",
    "endDate": "2022-10-14T12:45:00"
}
```
example Response:
```
{
    "id": "b063396b-7f30-47ce-8db6-70b33070d596",
    "name": "Meeting1",
    "responsiblePerson": {
        "id": "id3",
        "name": "Joe Doe"
    },
    "description": "Long text all day ",
    "category": "TeamBuilding",
    "type": "InPerson",
    "startDate": "2022-10-14T11:45:00.000+00:00",
    "endDate": "2022-10-14T12:45:00.000+00:00",
    "attendees": [
        {
            "id": "a33c1b88-d6ed-405e-9160-9af44a57c610",
            "person": {
                "id": "id3",
                "name": "Joe Doe"
            },
            "added": "2022-10-17T13:01:06.692+00:00"
        }
    ]
}
```
### Remove a meeting

DELETE localhost:8080/meetings/{id}    
HEADERS Authorization: Bearer {generatedJWTToken}

###### Prerequisites
1. You need to create a meeting
2. Generate JWT token which contains responsible person's id in payload e.g.    
```
{
  "id": "id3",
  "name": "Joe Doe",
  "iat": 1516239022
}
```
3. Generate .env file at root directory of application and add JWT secret to .env file 
```
SECRET=your-256-bit-secret
```

Responses
- Success: 
    - Status: 204 No Content   
    - Body: No content
- Failure: 
     - Body: {errorMessage: String, timestamp: String}


### Add attendee to a meeting
POST localhost:8080/meetings/{meetingId}/attendees

BODY schema
```
{
    id : String,
    name : String
}
```
Example Body
```
{
    "id":"id1",
    "name":"John Lucas"
}
```
Example Response
```
{
    "id": "b063396b-7f30-47ce-8db6-70b33070d596",
    "name": "Meeting1",
    "responsiblePerson": {
        "id": "id3",
        "name": "Joe Doe"
    },
    "description": "Long text all day ",
    "category": "TeamBuilding",
    "type": "InPerson",
    "startDate": "2022-10-14T11:45:00.000+00:00",
    "endDate": "2022-10-14T12:45:00.000+00:00",
    "attendees": [
        {
            "id": "a33c1b88-d6ed-405e-9160-9af44a57c610",
            "person": {
                "id": "id3",
                "name": "Joe Doe"
            },
            "added": "2022-10-17T13:01:06.692+00:00"
        },
        {
            "id": "779c18e2-50ad-42af-8c97-19f1a58fb4f3",
            "person": {
                "id": "id1",
                "name": "John Lucas"
            },
            "added": "2022-10-17T13:36:44.838+00:00"
        }
    ]
}
```
### Remove attendee from a meeting
DELETE localhost:8080/meetings/{meetingId}/attendees/{attendeeId}    
Responses
- Success: 
    - Status: 204 No Content   
    - Body: No content
- Failure: 
     - Body: {errorMessage: String, timestamp: String}
     
Validations:    
- Responsible person cannot be removed as attendee

### Get all meetings
GET localhost:8080/meetings?start=2022-10-14&end=2022-10-14&responsiblePersonId=id3&description=Some description&category=Hub&type=InPerson&attendees=10.  
Query Parameters:
1. start
     - type: String
     - format: yyyy-MM-dd
     - optional: true
     - description: filters meetings which starts on provided day or later
2. end
     - type: String
     - format: yyyy-MM-dd
     - optional: true
     - description: filters meetings which ends on provided day or earlier
3. responsiblePersonId
     - type: String
     - optional : true
     - description: filters meetings which have responsible person with matching id
4. description
     - type: String
     - optional : true
     - description: filters meetings which contain provided description (Case insensitive)
5. category
     - type: Enum
     - values: [CodeMonkey, Hub, Short, TeamBuilding]
     - optional : true
     - description: filters meetings which have provided category
6. type
     - type: Enum
     - values: [Live, InPerson]
     - optional : true
     - description: filters meetings which have provided type
7. attendees
     - type: int
     - optional : true
     - description: filters meetings which have larger or equal provided attendees count    

Example response:
```
[
    {
        "id": "b063396b-7f30-47ce-8db6-70b33070d596",
        "name": "Meeting1",
        "responsiblePerson": {
            "id": "id3",
            "name": "Joe Doe"
        },
        "description": "Long text all day ",
        "category": "TeamBuilding",
        "type": "InPerson",
        "startDate": "2022-10-14T11:45:00.000+00:00",
        "endDate": "2022-10-14T12:45:00.000+00:00",
        "attendees": [
            {
                "id": "a33c1b88-d6ed-405e-9160-9af44a57c610",
                "person": {
                    "id": "id3",
                    "name": "Joe Doe"
                },
                "added": "2022-10-17T13:01:06.692+00:00"
            },
            {
                "id": "779c18e2-50ad-42af-8c97-19f1a58fb4f3",
                "person": {
                    "id": "id1",
                    "name": "John Lucas"
                },
                "added": "2022-10-17T13:36:44.838+00:00"
            }
        ]
    }
]
```
