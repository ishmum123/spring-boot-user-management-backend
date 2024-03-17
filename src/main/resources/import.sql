insert into user_type values(1, 'ADMIN');
insert into user_type values(2, 'CHEF');
insert into user_type values(3, 'SALES');
insert into user_type values(4, 'CUSTOMER');

--insert into "user" (id, name, username, password, im) values(1, 'Admin', 'admin', '$2a$12$ev0Y4Wu5r7zRCkNWoiSYZukZVLtfsK.HDSHorw92nLXvyknFIDYhu');
insert into "user" (id, name, username, password, image_location) values(1, 'Admin', 'admin', '$2a$12$ev0Y4Wu5r7zRCkNWoiSYZukZVLtfsK.HDSHorw92nLXvyknFIDYhu', 'resources/1710587661385.png');

insert into "user_types" values(1, 1);
