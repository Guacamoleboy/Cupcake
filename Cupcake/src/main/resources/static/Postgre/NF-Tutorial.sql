/*

    1NF, 2NF & 3NF Tutorial

    Updated by: Guacamoleboy
    Updated: 28/10-2025

*/

-- =====================================================================
-- 1NF:
--
-- Hver kolonne skal indeholde enkelte, udelelige værdier,
-- og der må ikke være lister eller gentagelser inden for samme kolonne.
-- =====================================================================

CREATE TABLE Students (
StudentID INT SERIAL PRIMARY KEY,
StudentName VARCHAR(50),
Course VARCHAR(50)
)

INSERT INTO Students (StudentName, Course) VALUES
('Janus', 'Java'),
('Diddy', 'HTML'),
('Andreas', 'CSS'),
('Jesper', 'Narko');

-- FORKLARING
--
-- Denne er i 1NF fordi feltet "Course" kun kan have én værdi pr student. Det er ikke smart..
-- Man ville dele det ud i sin egen table, så en elev kan have flere kurser.

-- =====================================================================
-- 2NF:
--
-- Tabellen er i 2NF, hvis den allerede er i 1NF.
-- I 2NF flytter vi ting til separate tabeller, så vi kun gemmer hver information én gang, og dermed undgår unødvendige gentagelse.
-- =====================================================================

CREATE TABLE Students (
StudentID INT SERIAL PRIMARY KEY,
StudentName VARCHAR(50)
);

CREATE TABLE Courses (
CourseID INT SERIAL PRIMARY KEY,
CourseName VARCHAR(50)
);

CREATE TABLE StudentCourses (
StudentID INT REFERENCES Students(StudentID),
CourseID INT REFERENCES Courses(CourseID),
PRIMARY KEY (StudentID, CourseID)
);

-- FORKLARING
--
-- Tabellen er i 2NF, fordi vi har delt informationen op, så hver ting kun står ét sted.
-- StudentNavne står kun i Students-tabellen, kursusnavne kun i Courses-tabellen,
-- og StudentCourses fortæller bare, hvem der tager hvilket kursus.
-- På den måde undgår vi gentagelser og holder dataen ryddelig.
-- Denne er allerede i 3NF, men som eksempel er det fint.

-- =====================================================================
-- 3NF:
--
-- Tabellen er i 3NF, hvis den allerede er i 2NF.
-- I 3NF sørger vi for, at kolonner kun afhænger af deres egen tabels primærnøgle,
-- og ikke af andre kolonner, så vi undgår gentagelser.
-- =====================================================================

CREATE TABLE Courses (
CourseID INT SERIAL PRIMARY KEY,
CourseName VARCHAR(50),
Location VARCHAR(50)
);

CREATE TABLE Students (
StudentID INT SERIAL PRIMARY KEY,
StudentName VARCHAR(50)
);

CREATE TABLE StudentCourses (
StudentID INT REFERENCES Students(StudentID),
CourseID INT REFERENCES Courses(CourseID),
PRIMARY KEY (StudentID, CourseID)
);

-- FORKLARING
--
-- Tabellen er i 3NF, fordi alle kolonner kun handler om den ting, de hører til.
-- Location afhænger af kurset, ikke af studenten, så det står i Courses-tabellen.
-- StudentName afhænger kun af StudentID.
-- StudentCourses viser kun, hvilken student der tager hvilket kursus.
-- På den måde undgår vi gentagelser og holder dataen helt ryddelig.