create table cafeUser(
    user_ID varchar2(20) primary key,
    user_name varchar2(20) not null,
    user_phone varchar2(20) not null,
    user_point number(10) default 1000
);
select * from cafeUser;