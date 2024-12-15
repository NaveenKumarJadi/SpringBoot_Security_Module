POST: http://localhost:8080/api/auth/register
request Data:

{
    "firstName": "Sri Ram",
    "lastName": "Seetha",
    "email": "naveenkumar.jadi@gmail.com",
    "password": "ram@123"
}

POST: http://localhost:8080/api/auth/login

request Data:

{
    "email": "naveenkumar.jadi@gmail.com",
    "password": "ram@123"
}



GET: http://localhost:8080/api/test/secured

-> Authorization -> Bearer <Generated JWT Token>