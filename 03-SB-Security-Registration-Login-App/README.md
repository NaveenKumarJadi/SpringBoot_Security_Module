
show databases;

use JRTP;

select * from customer;

drop table customer;


POST: http://localhost:9090/register

Request Data:
{
    "name": "Navin",
    "email": "admin@gmail.com",
    "pwd": "admin@123",
    "phno": 9223334444
}


POST: http://localhost:9090/login

{
    "email": "admin@gmail.com",
    "pwd": "admin@123"
}
