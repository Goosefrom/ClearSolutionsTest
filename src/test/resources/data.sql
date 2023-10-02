INSERT INTO users(id, email, first_name, last_name, birth_date)
values (0, 'user@mail.com', 'firstName', 'lastName', to_date('2001-01-01', 'YYYY-MM-DD'));

INSERT INTO users(id, email, first_name, last_name, birth_date, address, phone_number)
values (1, 'user1@mail.com', 'firstName1', 'lastName1', to_date('2002-02-02', 'YYYY-MM-DD'), 'someAddress1', '+380999990');