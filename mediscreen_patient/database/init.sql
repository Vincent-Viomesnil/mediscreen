DROP TABLE IF EXISTS patient;
CREATE TABLE patient
(
    id     BIGINT   NOT NULL AUTO_INCREMENT,
    firstname   VARCHAR(50) NOT NULL,
    lastname  VARCHAR(50) NOT NULL,
    birthdate   DATE  NOT NULL,
    gender   VARCHAR(1)   NOT NULL,
    address VARCHAR(255),
    phonenumber  VARCHAR(20),



    PRIMARY KEY (id)
);



insert into patient(lastname, firstname, birthdate, gender, address, phonenumber) values ('TestNone', 'Test', '1966-12-31', 'F', '1 Brookside St', '100-222-3333');
insert into patient(lastname, firstname, birthdate, gender, address, phonenumber) values ('TestBorderline', 'Test', '1945-06-24', 'M', '2 High St', '200-333-4444');
insert into patient(lastname, firstname, birthdate, gender, address, phonenumber) values ('TestInDanger', 'Test', '2004-06-18', 'M', '3 Club Road', '300-444-5555');
insert into patient(lastname, firstname, birthdate, gender, address, phonenumber) values ('TestEarlyOnset', 'Test', '2002-06-28', 'F', '4 Valley Dr', '400-555-6666');
insert into patient(lastname, firstname, birthdate, gender, address, phonenumber) values ('Ferguson','Lucas','1968-06-22','M','2 Warren Street','387-866-1399');
insert into patient(lastname, firstname, birthdate, gender, address, phonenumber) values ('Rees','Pippa','1952-09-27','F','745 West Valley Farms Drive','628-423-0993');
insert into patient(lastname, firstname, birthdate, gender, address, phonenumber) values ('Arnold','Edward','1952-11-11','M','599 East Garden Ave','123-727-2779');
insert into patient(lastname, firstname, birthdate, gender, address, phonenumber) values ('Sharp','Anthony','1946-11-26','M','894 Hall Street','451-761-8383');
insert into patient(lastname, firstname, birthdate, gender, address, phonenumber) values ('Ince','Wendy','1958-06-29','F','4 Southampton Road','802-911-9975');
insert into patient(lastname, firstname, birthdate, gender, address, phonenumber) values ('Ross','Tracey','1949-12-07','F','40 Sulphur Springs Dr','131-396-5049');
insert into patient(lastname, firstname, birthdate, gender, address, phonenumber) values ('Wilson','Claire','1966-12-31','F','12 Cobblestone St','300-452-1091');
insert into patient(lastname, firstname, birthdate, gender, address, phonenumber) values ('Buckland','Max','1945-06-24','M','193 Vale St','833-534-0864');
insert into patient(lastname, firstname, birthdate, gender, address, phonenumber) values ('Clark','Natalie','1964-06-18','F','12 Beechwood Road','241-467-9197');
insert into patient(lastname, firstname, birthdate, gender, address, phonenumber) values ('Bailey','Piers','1959-06-28','M','1202 Bumble Dr','747-815-0557');