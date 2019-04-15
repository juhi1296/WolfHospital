create database WolfHospital;
use WolfHospital;

CREATE TABLE USER_LOGIN(
username VARCHAR(128) PRIMARY KEY,
pwd VARCHAR(128) NOT NULL,
person_id int not null,
role varchar(128) not null
);

insert into USER_LOGIN values ('Mary','root','100','D');
insert into USER_LOGIN values ('Emma','root','103','D');
insert into USER_LOGIN values ('Peter','root','105','D');

insert into USER_LOGIN values ('David','root','1001','P');
insert into USER_LOGIN values ('Sarah','root','1002','P');
insert into USER_LOGIN values ('Joseph','root','1003','P');
insert into USER_LOGIN values ('Lucy','root','1004','P');

insert into USER_LOGIN values('Ava','root','104','O');
insert into USER_LOGIN values('John','root','101','O');

insert into USER_LOGIN values('Olivia','root','106','N');
insert into USER_LOGIN values('Carol','root','102','N');

insert into USER_LOGIN values('admin','root','000','M');

CREATE TABLE STAFF(SID INT PRIMARY KEY AUTO_INCREMENT,name VARCHAR(128) NOT NULL,age INT NOT NULL,gender VARCHAR(128),job_title VARCHAR(128) NOT NULL,professional_title VARCHAR(128),phone_number VARCHAR(128) NOT NULL,address VARCHAR(128) NOT NULL,department VARCHAR(128) NOT NULL);

INSERT INTO STAFF values(100, 'Mary', 40, 'Female', 'Doctor', 'senior', '654', '90 ABC St , Raleigh NC 27', 'Neurology');
INSERT INTO STAFF values(101, 'John', 45, 'Male', 'Billing Staff', NULL, '564', '798 XYZ St , Rochester NY 54', 'Office');
INSERT INTO STAFF values(102, 'Carol', 55, 'Female', 'Nurse', NULL, '911', '351 MH St , Greensboro NC 27', 'ER');
INSERT INTO STAFF values(103, 'Emma', 55, 'Female', 'Doctor', 'Senior surgeon', '546', '49 ABC St , Raleigh NC 27', 'Oncological Surgery');
INSERT INTO STAFF values(104, 'Ava', 55, 'Female', 'Front Desk Staff', NULL, '777', '425 RG St , Raleigh NC 27', 'Office');
INSERT INTO STAFF values(105, 'Peter', 52, 'Male', 'Doctor', 'Anesthetist', '724', '475 RG St , Raleigh NC 27', 'Oncological Surgery');
INSERT INTO STAFF values(106, 'Olivia', 27, 'Female', 'Nurse', NULL, '799', '325 PD St , Raleigh NC 27', 'Neurology');

CREATE TABLE DOCTOR(SID INT PRIMARY KEY,CONSTRAINT doctor_fk FOREIGN KEY(SID) REFERENCES STAFF(SID)ON DELETE CASCADE);
CREATE TABLE OPERATOR(SID INT PRIMARY KEY,CONSTRAINT operator_fk FOREIGN KEY(SID) REFERENCES STAFF(SID)ON DELETE CASCADE);
CREATE TABLE NURSE(SID INT PRIMARY KEY,CONSTRAINT nurse_fk FOREIGN KEY(SID) REFERENCES STAFF(SID)ON DELETE CASCADE);

INSERT INTO DOCTOR (SID) VALUES (100);
INSERT INTO DOCTOR (SID) VALUES (103);
INSERT INTO DOCTOR (SID) VALUES (105);
INSERT INTO NURSE (SID) VALUES (102);
INSERT INTO NURSE (SID) VALUES (106);
INSERT INTO OPERATOR (SID) VALUES (101);
INSERT INTO OPERATOR (SID) VALUES (104);
 
CREATE TABLE PATIENT(PID INT PRIMARY KEY AUTO_INCREMENT,name VARCHAR(128) NOT NULL,SSN VARCHAR(11),DOB DATE NOT NULL,phone_number VARCHAR(128) NOT NULL,address VARCHAR(128) NOT NULL,age INT NOT NULL,gender VARCHAR(128));
INSERT INTO PATIENT values(1001, 'David', '000-01-1234', '1980-01-30', '919-123-3324', '69 ABC St , Raleigh NC 27730', 39, 'Male');
INSERT INTO PATIENT values(1002, 'Sarah', '000-02-1234', '1971-01-30', '919-563-3478', '81 DEF St , Cary NC 27519', 48, 'Female');
INSERT INTO PATIENT values(1003, 'Joseph', '000-03-1234', '1987-01-30','919-957-2199', '31 OPG St , Cary NC 27519', 32, 'Male');
INSERT INTO PATIENT values(1004, 'Lucy', '000-04-1234', '1985-01-30', '919-838-7123', '10 TBC St , Raleigh NC 27730', 34, 'Female');
 
CREATE TABLE WARD(WID INT PRIMARY KEY AUTO_INCREMENT,SID INT NOT NULL,charges INT NOT NULL,CONSTRAINT ward_fk FOREIGN KEY(SID) REFERENCES NURSE(SID)ON DELETE CASCADE);


INSERT INTO WARD values(001,102,50);
INSERT INTO WARD values(002,102,50); 
INSERT INTO WARD values(003,106,100);
INSERT INTO WARD values(004,106,100);

CREATE TABLE BED(WID INT,Bed_ID INT,availability INT NOT NULL,CONSTRAINT bed_PK PRIMARY KEY(WID,Bed_ID),CONSTRAINT bed_fk FOREIGN KEY(WID) REFERENCES WARD(WID) ON DELETE CASCADE);

INSERT into BED values(001,1,1);
INSERT into BED values(001,2,1);
INSERT into BED values(001,3,0);
INSERT into BED values(001,4,0);

INSERT into BED values(002,1,1);
INSERT into BED values(002,2,0);
INSERT into BED values(002,3,0);
INSERT into BED values(002,4,0);

INSERT into BED values(003,1,1);
INSERT into BED values(003,2,0);

INSERT into BED values(004,1,0);
INSERT into BED values(004,2,0);

CREATE TABLE PATIENT_IS_ASSIGNED_BED(PID INT,WID INT,Bed_ID INT,CONSTRAINT patient_is_assigned_bed_fk1 FOREIGN KEY(PID) REFERENCES PATIENT(PID) ON DELETE CASCADE,CONSTRAINT patient_is_assigned_bed_fk2 FOREIGN KEY(WID,Bed_ID) REFERENCES BED(WID,Bed_ID) ON DELETE CASCADE,start_date DATE NOT NULL,end_date DATE);

INSERT INTO PATIENT_IS_ASSIGNED_BED values(1001, 001, 1, '2019-03-01', NULL);
INSERT INTO PATIENT_IS_ASSIGNED_BED values(1002, 002, 1, '2019-03-10', NULL);
INSERT INTO PATIENT_IS_ASSIGNED_BED values(1003, 001, 2, '2019-03-15', NULL);


CREATE TABLE BILLING_ACCOUNT(Bill_ID INT PRIMARY KEY AUTO_INCREMENT,PID INT NOT NULL,payment_method VARCHAR(128) NOT NULL,card_number VARCHAR(128),SSN_payer VARCHAR(128) NOT NULL,billing_address VARCHAR(128) NOT NULL,registration_fee DOUBLE NOT NULL,accomodation_fee DOUBLE ,medication_prescribed VARCHAR(10),visit_date DATE NOT NULL,CONSTRAINT billing_account_fk FOREIGN KEY(PID) REFERENCES PATIENT(PID)ON DELETE CASCADE);

INSERT INTO BILLING_ACCOUNT(PID,payment_method,card_number,SSN_payer,billing_address,registration_fee,accomodation_fee,medication_prescribed,visit_date) 
values (1001, 'Credit card', '4044875409613234', '000-01-1234', '69 ABC St , Raleigh NC 27730', 100, null , 'yes', '2019-03-01');

INSERT INTO BILLING_ACCOUNT(PID,payment_method,card_number,SSN_payer,billing_address,registration_fee,accomodation_fee,medication_prescribed,visit_date) 
values (1002, 'Credit card', '4401982398541143', '000-02-1234', '81 DEF St , Cary NC 27519', 100, null, 'yes', '2019-03-10');

INSERT INTO BILLING_ACCOUNT(PID,payment_method,card_number,SSN_payer,billing_address,registration_fee,accomodation_fee,medication_prescribed,visit_date) 
values (1003, 'Check', null , '000-03-1234', '31 OPG St , Cary NC 27519', 100, null , 'yes', '2019-03-15');

INSERT INTO BILLING_ACCOUNT(PID,payment_method,card_number,SSN_payer,billing_address,registration_fee,accomodation_fee,medication_prescribed,visit_date) 
values (1004, 'Credit card', '4044987612349123', '000-04-1234', '10 TBC St. Raleigh NC 27730', 100, 400, 'yes', '2019-03-17');


CREATE TABLE TREATS(PID INT, SID INT,CONSTRAINT treats_fk1 FOREIGN KEY(SID) REFERENCES DOCTOR(SID)ON DELETE CASCADE,CONSTRAINT treats_fk2 FOREIGN KEY(PID) REFERENCES PATIENT(PID)ON DELETE CASCADE);

INSERT INTO TREATS values(1001,100);
INSERT INTO TREATS values(1002,100);
INSERT INTO TREATS values(1003,100);


CREATE TABLE MEDICAL_RECORDS(RID INT PRIMARY KEY  AUTO_INCREMENT,PID INT,start_date DATE NOT NULL,end_date DATE,prescription VARCHAR(128),diagnosis_details VARCHAR(128),processing_treatment_plan int, responsible_doctor int,CONSTRAINT rec_fk FOREIGN KEY(PID) REFERENCES PATIENT(PID)ON DELETE CASCADE);

INSERT INTO MEDICAL_RECORDS(PID,start_date,end_date,prescription,diagnosis_details,processing_treatment_plan) values(1001,'2019-03-01', NULL, 'nervine', 'Hospitalization','20');
INSERT INTO MEDICAL_RECORDS(PID,start_date,end_date,prescription,diagnosis_details,processing_treatment_plan) values(1002,'2019-03-10', NULL, 'nervine', 'Hospitalization','20');
INSERT INTO MEDICAL_RECORDS(PID,start_date,end_date,prescription,diagnosis_details,processing_treatment_plan) values(1003,'2019-03-15', NULL, 'nervine', 'Hospitalization', '10');
INSERT INTO MEDICAL_RECORDS(PID,start_date,end_date,prescription,diagnosis_details,processing_treatment_plan,responsible_doctor) values(1004,'2019-03-01', '2019-03-21', 'analgesic', 'Surgeon, Hospitalization','5','103');



CREATE TABLE TEST(TID INT PRIMARY KEY  AUTO_INCREMENT,recommended_SID INT,performed_SID INT,CONSTRAINT test_fk1 FOREIGN KEY(recommended_SID) REFERENCES DOCTOR(SID) ON DELETE CASCADE,CONSTRAINT test_fk2 FOREIGN KEY(performed_SID) REFERENCES DOCTOR(SID) ON DELETE CASCADE,name VARCHAR(128) NOT NULL,result VARCHAR(128), TEST_DATE DATE);

INSERT INTO TEST(recommended_SID,performed_SID,name,test_date) values(103,100,'Thyroid Test','2019-03-15');
INSERT INTO TEST(recommended_SID,performed_SID,name,test_date) values(103,105,'Xray','2019-03-19');


CREATE TABLE RECORD_HAS_TEST(RID INT,TID INT,CONSTRAINT record_has_test_fk1 FOREIGN KEY(RID) REFERENCES MEDICAL_RECORDS(RID) ON DELETE CASCADE,CONSTRAINT record_has_test_fk2 FOREIGN KEY(TID) REFERENCES TEST(TID) ON DELETE CASCADE);

INSERT INTO RECORD_HAS_TEST VALUES(4,1);
INSERT INTO RECORD_HAS_TEST VALUES(4,2);


CREATE TABLE REGISTRATIONS(SID INT,PID INT,CONSTRAINT reg_fk1 FOREIGN KEY(SID) REFERENCES OPERATOR(SID)ON DELETE CASCADE,CONSTRAINT reg_fk2 FOREIGN KEY(PID) REFERENCES PATIENT(PID)ON DELETE CASCADE);

/*CREATE TABLE nurse_assigned_to_patient(PID INT,SID INT,CONSTRAINT np_fk1 FOREIGN KEY(PID) REFERENCES patient(PID)ON DELETE CASCADE,CONSTRAINT np_fk2 FOREIGN KEY(SID) REFERENCES nurse(SID)
ON DELETE CASCADE);
*/

/*
SELECT * FROM medical_records;
select * from billing_account;
select * from registrations;
INSERT INTO patient_is_assigned_bed values(104,1,3,curdate(),null)
select * from user_login;
select * from bed;
select * from treats;
select * from patient;
select * from patient_is_assigned_bed;
select * from staff;


DELETE FROM TREATS WHERE PID = 1004;
drop database WolfHospital;
*/
