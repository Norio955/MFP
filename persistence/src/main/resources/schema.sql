drop table if exists comments;
drop table if exists books;

create table if not exists books (
    id identity primary key,
    title varchar(255) not null,
    author varchar(255) not null,
    pub_year int not null
    );

create table if not exists comments (
    id identity primary key,
    book_id bigint not null,
    author varchar(64) not null,
    text varchar(1000) not null,
    created_at timestamp not null default current_timestamp,
    constraint fk_book foreign key (book_id) references books(id) on delete cascade
    );

-- Додаємо тестові книги для каталогу:
insert into books (title, author, pub_year) values ('Кобзар', 'Тарас Шевченко', 1840);
insert into books (title, author, pub_year) values ('Тигролови', 'Іван Багряний', 1944);
insert into books (title, author, pub_year) values ('Місто', 'Валер''ян Підмогильний', 1928);