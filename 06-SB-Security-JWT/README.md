POST: http://localhost:8080/auth/addNewUser

RequestData:
{
    "username":"pavan",
    "password":"pavan@123",
    "email":"pavan@gmail.com",
    "roles":"ROLE_ADMIN"
}

POST: http://localhost:8080/auth/generateToken

RequestData:
{
    "username":"pavan",
    "password":"pavan@123"
}


GET: http://localhost:8080/auth/admin/adminProfile

Goto Auth -> Select the Bearer Token: pass the generated token