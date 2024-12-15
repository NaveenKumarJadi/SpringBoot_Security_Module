Step 1: Register with user Details

Step 2: Login with username and Password it will generate token

Step 3: use the token and fetch the data based on roles

Step 4: If token is expired use refreshTOken API and get New token

Database: 

	create database Security_DB;

	use Security_DB;

	show tables;

	select * from userinfo;

	select * from refreshtoken;



POST: http://localhost:9097/products/signUp

Request Data:
{
    "name": "ravi",
    "email": "ravi@gmail.com",
    "password": "ravi123",
    "roles": "ROLE_USER"
}

Request Data:
{
    "name": "ram",
    "email": "ram@gmail.com",
    "password": "ram123",
    "roles": "ROLE_ADMIN"
}


POST: http://localhost:9097/products/login

Request Data:
{
    "name": "ravi",
    "password": "ravi123"
}


GET: http://localhost:9097/products/1

GET: http://localhost:9097/products/all


POST: http://localhost:9097/products/refreshToken

Request Data:
{
    "token":"ea180767-531e-4c10-8e80-883fc07e63f2"
}