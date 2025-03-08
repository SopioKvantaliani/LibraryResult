select * from books;
select * from book_categories;

select isbn, name, author, year  from books
where id=36462;

select * from books where id = 36462;

select * from users where email='Angle61@library';

select id, users.full_name, users.email,
       users.password, users.user_group_id,
       users.image, users.extra_data, users.status, users.is_admin,
       users.start_date, users.end_date, users.address from users
        where id = 22550;

select * from users where id= 22550;

select * from users where email like 'kseniasopo@library';

