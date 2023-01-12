-- CREATION

DROP DATABASE IF EXISTS sales_warehouse;

CREATE DATABASE IF not EXISTS sales_warehouse;

USE sales_warehouse;

DROP TABLE IF EXISTS consumer;

CREATE TABLE IF NOT EXISTS consumer
(
    id        INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    c_name    VARCHAR(15)  NOT NULL,
    c_surname varchar(15)  NOT NULL,
    email     VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS goods;

CREATE TABLE IF NOT EXISTS goods
(
    id      INT           NOT NULL PRIMARY KEY AUTO_INCREMENT,
    g_name  varchar(50)   NOT NULL,
    price   DECIMAL(9, 2) NOT NULL,
    comment varchar(255) DEFAULT 'No comment is provided'
);

DROP TABLE IF EXISTS waybill;

CREATE TABLE IF NOT EXISTS waybill
(
    id                INT                    NOT NULL PRIMARY KEY AUTO_INCREMENT,
    car_number        VARCHAR(15)            NOT NULL,
    driver_name       VARCHAR(20)            NOT NULL,
    driver_surname    VARCHAR(20)            NOT NULL,
    driver_patronymic VARCHAR(20)            NOT NULL,
    order_date        DATE DEFAULT '2022-01-08',
    service_price     DECIMAL(9, 2) UNSIGNED NOT NULL,
    consumer_id       INT,
    CONSTRAINT fk_consumer_id
        FOREIGN KEY (consumer_id) REFERENCES consumer (id)
            ON DELETE CASCADE
);

DROP TABLE IF EXISTS waybill_has_goods;

CREATE TABLE IF NOT EXISTS waybill_has_goods
(
    waybill_id INT          NOT NULL,
    goods_id   INT          NOT NULL,
    amount     INT UNSIGNED NOT NULL,
    PRIMARY KEY (waybill_id, goods_id),
    CONSTRAINT fk_in_goods_waybill_id
        FOREIGN KEY (waybill_id) REFERENCES waybill (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_goods_id
        FOREIGN KEY (goods_id) REFERENCES goods (id)
            ON DELETE CASCADE
);

-- INSERTION

INSERT consumer(c_name, c_surname, email)
VALUES ('Olya', 'Onopriienko', 'email_of_consumer@gmail.com'),
       ('Dima', 'Onopriienko', 'email_of_consumer1@gmail.com'),
       ('Oleh', 'Onopriienko', 'email_of_consumer2@gmail.com'),
       ('Anna', 'Onopriienko', 'email_of_consumer3@gmail.com');

INSERT goods(g_name, price, comment)
VALUES ('Square pants', 10.99, 'Sponge Bob accessoire'),
       ('Shawarma mini', 14.99, 'food'),
       ('Shawarma original', 19.99, 'food'),
       ('Shawarma XL', 25.49, 'food'),
       ('Shawarma XXXL', 30.99, 'food'),
       ('HotWheels toy', 5.49, 'car toy'),
       ('Kozacka Shashka', 10.49, 'Ukrainian power');

INSERT waybill(car_number, driver_name, driver_surname, driver_patronymic, service_price, consumer_id, order_date)
VALUES ('AA 5703 HM', 'Sereha', 'Ivanov', 'Petrovych', 64.49, 1, '2021-12-25'),
       ('AA 5703 HM', 'Sereha', 'Ivanov', 'Petrovych', 64.49, 2, '2021-12-23'),
       ('AA 5703 HM', 'Sereha', 'Ivanov', 'Petrovych', 64.49, 1, '2021-12-27'),
       ('AA 4053 TP', 'Keanu', 'Reeves', 'Alexandrovich', 15.99, 1, '2020-12-17'),
       ('AH 9146 BB', 'Maksym', 'Veres', 'Petrovych', 18.99, 1, '2022-12-17');

INSERT waybill_has_goods(waybill_id, goods_id, amount)
VALUES (1, 1, 5),
       (1, 5, 2),
       (1, 3, 4);

INSERT waybill_has_goods(waybill_id, goods_id, amount)
VALUES (2, 1, 4),
       (2, 5, 1),
       (2, 3, 3);

INSERT waybill_has_goods(waybill_id, goods_id, amount)
VALUES (3, 1, 5),
       (3, 5, 7),
       (3, 3, 1);

-- UPDATING

SET SQL_SAFE_UPDATES = 0;

# UPDATE goods
# SET comment = 'Sponge Bob accessoire'
# WHERE g_name = 'Square pants';
#
# UPDATE goods
# SET comment = 'food'
# WHERE g_name LIKE 'Shawarma%';
#
# UPDATE goods
# SET comment = 'car toy'
# WHERE g_name LIKE 'HotWheels%';
#
# UPDATE goods
# SET comment = 'Ukrainian power'
# WHERE g_name LIKE '%Shashka';
