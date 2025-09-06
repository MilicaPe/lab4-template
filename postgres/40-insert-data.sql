-- 40-insert-data.sql

\c users;

insert into all_users(username, password, email, role)  -- 123
values ('Test Max', '$2a$12$PrqxxYcfbivngdE.2XplBOh1n8AnloLHv4wnzE3qc8m6fPDRsTP1W', 'max@gmail.com', 'USER')
ON CONFLICT (username) DO NOTHING;

insert into all_users(username, password, email, role)
values ('admin', '$2a$12$PrqxxYcfbivngdE.2XplBOh1n8AnloLHv4wnzE3qc8m6fPDRsTP1W', 'admin@gmail.com', 'ADMIN' )
ON CONFLICT (username) DO NOTHING;


\c reservations;
insert into hotels (hotel_uid, name, country, city, address, stars, price)
values ('049161bb-badd-4fa8-9d90-87c9a82b0668', 'Ararat Park Hyatt Moscow', 'Россия', 'Москва', 'Неглинная ул., 4', 5, 10000)
ON CONFLICT (hotel_uid) DO NOTHING;


\c loyalties;
insert into loyalty (discount, reservation_count, username, status)
values (10, 25, 'Test Max', 'GOLD')
ON CONFLICT (username) DO NOTHING;
