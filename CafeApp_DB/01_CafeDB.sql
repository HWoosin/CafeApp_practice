create table cafeUser(
    user_ID varchar2(20) primary key,
    user_PW varchar2(20) ,
    user_name varchar2(20) not null,
    user_phone varchar2(20) not null,
    user_point number(10) default 1000
);
drop table cafeUser;

select * from cafeUser;