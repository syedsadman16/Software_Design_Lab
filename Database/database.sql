
CREATE DATABASE `Software_design`;
USE `Software_design`;

CREATE TABLE IF NOT EXISTS `students`( 
    studentID INT UNSIGNED NOT NULL AUTO_INCREMENT,   
    PRIMARY KEY(studentID),  
    lastName VARCHAR(255),   
    firstName VARCHAR(255),   
    sex ENUM('M', 'F') NOT NULL 
); 
 
CREATE TABLE IF NOT EXISTS `courses` ( 
    courseID INT UNSIGNED NOT NULL AUTO_INCREMENT,    
    PRIMARY KEY(courseID),    
    courseTitle VARCHAR(200) NOT NULL,  
    department VARCHAR(200) NOT NULL 
); 

CREATE TABLE IF NOT EXISTS `classes` ( 
    section VARCHAR(255) NOT NULL, 
    courseID INT UNSIGNED NOT NULL,     
	studentID INT UNSIGNED NOT NULL, 
    year YEAR(4) NOT NULL, 
    semester VARCHAR(255) NOT NULL, 
    GPA ENUM('A', 'B', 'C', 'D', 'F', 'W') NOT NULL, 
    FOREIGN KEY(studentID) REFERENCES `students`(studentID), 
    FOREIGN KEY(courseID) REFERENCES `courses`(courseID),
    PRIMARY KEY(studentID, courseID, section)
); 

INSERT INTO `students` (studentID, firstName, lastName, sex)  VALUES
(3894, 'Naomi', 'Campbell', 'F'),
(4762, 'Michelle', 'Garcia', 'F'),
(6537, 'Dave', 'Fuller', 'M'),
(2182, 'Matt', 'Cooper', 'M'),
(7639, 'Olivia', 'Walker', 'F'),
(4070, 'George', 'Griego', 'M');

INSERT INTO `courses` (courseID, courseTitle, department)  VALUES
(22100, 'Software Design', 'Computer Science'),
(24100, 'Electronics 1', 'Electrical Engineering');

INSERT INTO `classes` (courseID, studentID, section, year, semester, GPA)  VALUES
(22100, 3894, 'G4',  2020, 'Spring', 'A'),
(22100, 4762, 'G4',  2020, 'Spring', 'C'),
(22100, 6537, 'H6',  2020, 'Spring', 'B'),
(22100, 2182, 'H6',  2020, 'Spring', 'D'),
(22100, 7639, 'H6',  2020, 'Spring', 'C'),
(22100, 4070, 'G4',  2020, 'Spring', 'C');

