insert into user_type values(1, 'ADMIN');
insert into user_type values(2, 'EMPLOYEE');
insert into user_type values(3, 'CUSTOMER');

insert into credential (id, username, password, type_id) values(1, 'admin', '$2a$12$ev0Y4Wu5r7zRCkNWoiSYZukZVLtfsK.HDSHorw92nLXvyknFIDYhu', 1);
