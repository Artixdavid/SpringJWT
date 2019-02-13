INSERT INTO productos (nombre,precio,create_at) VALUES ('TV Plasma',1500.20,NOW());
INSERT INTO productos (nombre,precio,create_at) VALUES ('Bolso',300,NOW());
INSERT INTO productos (nombre,precio,create_at) VALUES ('Audifonos',75.90,NOW());
INSERT INTO productos (nombre,precio,create_at) VALUES ('Baterias',99.90,NOW());
INSERT INTO productos (nombre,precio,create_at) VALUES ('PC laptop',1490.90,NOW());
INSERT INTO productos (nombre,precio,create_at) VALUES ('Telefono',890.90,NOW());
INSERT INTO productos (nombre,precio,create_at) VALUES ('Nevera Cartelua',780.90,NOW());
INSERT INTO productos (nombre,precio,create_at) VALUES ('Telescopio peluo',490.90,NOW());
INSERT INTO productos (nombre,precio,create_at) VALUES ('Mesa de Techo',2000,NOW());



insert into clientes (nombre,apellido, email,create_at,foto) values ('andres','guzman','profesion@profesion.cl', NOW(),'');
insert into clientes (nombre,apellido, email,create_at,foto) values ('David','Leones','david@profesion.cl', NOW(),'');

insert into facturas (descripcion, observacion, cliente_id, create_at) VALUES ('Factura de pruebas', null, 1, now());

insert into factura_item (cantidad, factura_id, producto_id) VALUES (1,1,1);
insert into factura_item (cantidad, factura_id, producto_id) VALUES (2,1,4);
insert into factura_item (cantidad, factura_id, producto_id) VALUES (1,1,5);




/* USUARIOS */


insert into users (username, password, enabled) values ('admin', '$2a$10$0M61aGvS506iGO7oiJrKsOMC2GFTbr9Icsh2x9ZvL2Ykk7VLCx0X.', true);
insert into users (username, password, enabled) values ('david', '$2a$10$0M61aGvS506iGO7oiJrKsOMC2GFTbr9Icsh2x9ZvL2Ykk7VLCx0X.', true);


/* ROLEs */

insert into authorities  (user_id, authority) values (1,'ROLE_USER');
insert into authorities  (user_id, authority) values (1,'ROLE_ADMIN');
insert into authorities  (user_id, authority) values (2,'ROLE_USER');

