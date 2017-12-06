# Message CRUD App 

__POST /message__

Create a new message with e.g following body

Example request body

`
{
 	"id": 1,
	"message":"hello"
}
 `
 
Example response 
 
`
{
   "data": {
     "id": 1,
     "message": "hello"
   },
   "status_code": 200,
   "message": "Succesfully persisted message"
}
`
 
 __PUT /message/{id}__
 
 Update a currently existing message with e.g following body
 
 
 Example request body:
 
 
 `
 {
 	"message":"hello_update"
 }
  `
  
Example Response


`
{
  "data": {
    "id": 1,
    "message": "hello_update"
  },
  "status_code": 200,
  "message": "Succesfully updated message"
}
`

  
  
 __DELETE /message/{id}__
 
 Delete a currently existing message:
 
 
 Example response
 
` 
{
   "status_code": 200,
   "message": "Succesfully deleted message with id: 1"
}
`

__GET /message/{id}__
 
 Retrieve a currently existing message:
  
 Example response

` 
{
  "data": {
    "id": 1,
    "message": "hello"
  },
  "status_code": 200,
  "message": "Succesfully retrieved message with id: 1"
}
`

__GET /message__
 
 Retrieve all currently existing messages:
  
 Example response

` 
{
  "data": [
    {
      "id": 1,
      "message": "hello"
    },
    {
      "id": 2,
      "message": "hello"
    },
    {
      "id": 5,
      "message": "hello"
    }
  ],
  "status_code": 200,
  "message": "Succesfully retrieved all messages!"
}
`
 