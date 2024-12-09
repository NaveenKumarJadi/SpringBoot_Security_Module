MySQL: 

create database JRTP;

use JRTP;

select * from customer;

drop table customer;

-------------------------------------------------------


POST: http://localhost:9090/api/register

Request Data:
{
    "uname": "Ram",
    "pwd": "ram@123",
    "phno": 9569562271
}

-------------------------------------------------------

POST: http://localhost:9090/api/login

Request Data:
{
    "uname": "Ram",
    "pwd": "ram@123"
}

will get a response as JWT Token
-------------------------------------------------------

GET: http://localhost:9090/api/welcome

Headers > Authorization : Generated Token