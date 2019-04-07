use WolfHospital;

CREATE TABLE staff(SID INT PRIMARY KEY AUTO_INCREMENT,name VARCHAR(128) NOT NULL,age INT NOT NULL,gender VARCHAR(128),job_title VARCHAR(128) NOT NULL,professional_title VARCHAR(128),phone_number VARCHAR(128) NOT NULL,address VARCHAR(128) NOT NULL,department VARCHAR(128) NOT NULL);

INSERT INTO staff values(100, 'Mary', 40, 'Female', 'Doctor', 'senior', '654', '90 ABC St , Raleigh NC 27', 'Neurology');
INSERT INTO staff values(101, 'John', 45, 'Male', 'Billing Staff', NULL, '564', '798 XYZ St , Rochester NY 54', 'Office');
INSERT INTO staff values(102, 'Carol', 55, 'Female', 'Nurse', NULL, '911', '351 MH St , Greensboro NC 27', 'ER');
INSERT INTO staff values(103, 'Emma', 55, 'Female', 'Doctor', 'Senior surgeon', '546', '49 ABC St , Raleigh NC 27', 'Oncological Surgery');
INSERT INTO staff values(104, 'Ava', 55, 'Female', 'Front Desk Staff', NULL, '777', '425 RG St , Raleigh NC 27', 'Office');
INSERT INTO staff values(105, 'Peter', 52, 'Male', 'Doctor', 'Anesthetist', '724', '475 RG St , Raleigh NC 27', 'Oncological Surgery');
INSERT INTO staff values(106, 'Olivia', 27, 'Female', 'Nurse', NULL, '799', '325 PD St , Raleigh NC 27', 'Neurology');

CREATE TABLE doctor(SID INT PRIMARY KEY,CONSTRAINT doctor_fk FOREIGN KEY(SID) REFERENCES staff(SID)ON DELETE CASCADE);
CREATE TABLE operator(SID INT PRIMARY KEY,CONSTRAINT operator_fk FOREIGN KEY(SID) REFERENCES staff(SID)ON DELETE CASCADE);
CREATE TABLE nurse(SID INT PRIMARY KEY,CONSTRAINT nurse_fk FOREIGN KEY(SID) REFERENCES staff(SID)ON DELETE CASCADE);

INSERT INTO doctor (SID) VALUES (100);
INSERT INTO doctor (SID) VALUES (103);
INSERT INTO doctor (SID) VALUES (105);
INSERT INTO nurse (SID) VALUES (102);
INSERT INTO nurse (SID) VALUES (106);
INSERT INTO operator (SID) VALUES (101);
INSERT INTO operator (SID) VALUES (104);
 
 CREATE TABLE patient(PID INT PRIMARY KEY AUTO_INCREMENT,name VARCHAR(128) NOT NULL,SSN VARCHAR(11),DOB DATE NOT NULL,phone_number VARCHAR(128) NOT NULL,address VARCHAR(128) NOT NULL,age INT NOT NULL,gender VARCHAR(128),processing_treatment_plan INT,completing_treatment VARCHAR(128)  NOT NULL);
INSERT INTO patient values(1001, 'David', '000-01-1234', '1980-01-30', '919-123-3324', '69 ABC St , Raleigh NC 27730', 39, 'Male',20,  'no');
INSERT INTO patient values(1002, 'Sarah', '000-02-1234', '1971-01-30', '919-563-3478', '81 DEF St , Cary NC 27519', 48, 'Female',20,  'no');
INSERT INTO patient values(1003, 'Joseph', '000-03-1234', '1987-01-30','919-957-2199', '31 OPG St , Cary NC 27519', 32, 'Male',10,  'no');
INSERT INTO patient values(1004, 'Lucy', '000-04-1234', '1985-01-30', '919-838-7123', '10 TBC St , Raleigh NC 27730', 34, 'Female',5, 'yes');
 
CREATE TABLE ward(WID INT PRIMARY KEY,SID INT NOT NULL,charges INT NOT NULL,CONSTRAINT ward_fk FOREIGN KEY(SID) REFERENCES nurse(SID)ON DELETE CASCADE);


INSERT INTO ward values(001,102,50);
INSERT INTO ward values(002,102,50); 
INSERT INTO ward values(003,106,100);
INSERT INTO ward values(004,106,100);

CREATE TABLE bed(WID INT,Bed_ID INT,availability INT NOT NULL,CONSTRAINT bed_PK PRIMARY KEY(WID,Bed_ID),CONSTRAINT bed_fk FOREIGN KEY(WID) REFERENCES ward(WID) ON DELETE CASCADE);

INSERT into bed values(001,1,1);
INSERT into bed values(001,2,1);
INSERT into bed values(001,3,0);
INSERT into bed values(001,4,0);

INSERT into bed values(002,1,1);
INSERT into bed values(002,2,0);
INSERT into bed values(002,3,0);
INSERT into bed values(002,4,0);

INSERT into bed values(003,1,1);
INSERT into bed values(003,2,0);

INSERT into bed values(004,1,0);
INSERT into bed values(004,2,0);

CREATE TABLE patient_is_assigned_bed(PID INT,WID INT,Bed_ID INT,CONSTRAINT patient_is_assigned_bed_fk1 FOREIGN KEY(PID) REFERENCES patient(PID) ON DELETE CASCADE,CONSTRAINT patient_is_assigned_bed_fk2 FOREIGN KEY(WID,Bed_ID) REFERENCES bed(WID,Bed_ID) ON DELETE CASCADE,start_date DATE NOT NULL,end_date DATE);

INSERT INTO patient_is_assigned_bed values(1001, 001, 1, '2019-03-01', NULL);
INSERT INTO patient_is_assigned_bed values(1002, 002, 1, '2019-03-10', NULL);
INSERT INTO patient_is_assigned_bed values(1003, 001, 2, '2019-03-15', NULL);
INSERT INTO patient_is_assigned_bed values(1004, 003, 1, '2019-03-17', '2019-03-21');

CREATE TABLE billing_account(Bill_ID INT PRIMARY KEY AUTO_INCREMENT,PID INT NOT NULL,payment_method VARCHAR(128) NOT NULL,card_number VARCHAR(128),SSN_payer VARCHAR(128) NOT NULL,billing_address VARCHAR(128) NOT NULL,registration_fee DOUBLE NOT NULL,accomodation_fee DOUBLE NOT NULL,medication_prescribed VARCHAR(10) not null,visit_date DATE NOT NULL,CONSTRAINT billing_account_fk FOREIGN KEY(PID) REFERENCES patient(PID)ON DELETE CASCADE);

INSERT INTO billing_account(PID,payment_method,card_number,SSN_payer,billing_address,registration_fee,accomodation_fee,medication_prescribed,visit_date) 
values (1004, 'Credit card', '4044987612349123', '000-04-1234', '10 TBC St. Raleigh NC 27730', 100, 400, 'yes', '2019-03-17');

CREATE TABLE treats(PID INT, SID INT,CONSTRAINT treats_fk1 FOREIGN KEY(SID) REFERENCES doctor(SID)ON DELETE CASCADE,CONSTRAINT treats_fk2 FOREIGN KEY(PID) REFERENCES patient(PID)ON DELETE CASCADE);

INSERT INTO treats values(1001,100);
INSERT INTO treats values(1002,100);
INSERT INTO treats values(1003,100);
INSERT INTO treats values(1004,103);
INSERT INTO treats values(1004,105);

CREATE TABLE medical_records(RID INT PRIMARY KEY  AUTO_INCREMENT,PID INT,start_date DATE NOT NULL,end_date DATE,prescription VARCHAR(128),diagnosis_details VARCHAR(128),responsible_doctor varchar(128),CONSTRAINT rec_fk FOREIGN KEY(PID) REFERENCES patient(PID)ON DELETE CASCADE);

INSERT INTO medical_records(PID,start_date,end_date,prescription,diagnosis_details,responsible_doctor) values(1001,'2019-03-01', NULL, 'nervine', 'Hospitalization','100');
INSERT INTO medical_records(PID,start_date,end_date,prescription,diagnosis_details,responsible_doctor) values(1002,'2019-03-10', NULL, 'nervine', 'Hospitalization','100');
INSERT INTO medical_records(PID,start_date,end_date,prescription,diagnosis_details,responsible_doctor) values(1003,'2019-03-15', NULL, 'nervine', 'Hospitalization','100');
INSERT INTO medical_records(PID,start_date,end_date,prescription,diagnosis_details,responsible_doctor) values(1004,'2019-03-01', '2019-03-21', 'analgesic', 'Surgeon, Hospitalization','103,104');

/*CREATE TABLE test(TID INT PRIMARY KEY  AUTO_INCREMENT,recommended_SID INT,performed_SID INT,CONSTRAINT test_fk1 FOREIGN KEY(recommended_SID) REFERENCES doctor(SID) ON DELETE CASCADE,CONSTRAINT test_fk2 FOREIGN KEY(performed_SID) REFERENCES doctor(SID) ON DELETE CASCADE,name VARCHAR(128) NOT NULL,result VARCHAR(128));
CREATE TABLE registrations(SID INT,PID INT,CONSTRAINT reg_fk1 FOREIGN KEY(SID) REFERENCES operator(SID)ON DELETE CASCADE,CONSTRAINT reg_fk2 FOREIGN KEY(PID) REFERENCES patient(PID)ON DELETE CASCADE);
CREATE TABLE nurse_assigned_to_patient(PID INT,SID INT,CONSTRAINT np_fk1 FOREIGN KEY(PID) REFERENCES patient(PID)ON DELETE CASCADE,CONSTRAINT np_fk2 FOREIGN KEY(SID) REFERENCES nurse(SID)
ON DELETE CASCADE);
CREATE TABLE record_has_test(RID INT,TID INT,CONSTRAINT record_has_test_fk1 FOREIGN KEY(RID) REFERENCES medical_records(RID) ON DELETE CASCADE,CONSTRAINT record_has_test_fk2 FOREIGN KEY(TID) REFERENCES test(TID) ON DELETE CASCADE);
*/
