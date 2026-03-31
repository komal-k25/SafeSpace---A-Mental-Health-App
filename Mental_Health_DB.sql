drop database  mental_health_db;
create database mental_health_db;
use  mental_health_db;
-- Drop existing tables if they exist
DROP TABLE IF EXISTS MEDICINE;
DROP TABLE IF EXISTS ALERT;
DROP TABLE IF EXISTS FEEDBACK;
DROP TABLE IF EXISTS APPOINTMENT;
DROP TABLE IF EXISTS MOODLOG;
DROP TABLE IF EXISTS STUDENT_VOLUNTEER;
DROP TABLE IF EXISTS CLINICAL_PSYCHOLOGIST;
DROP TABLE IF EXISTS COUNSELLOR;
DROP TABLE IF EXISTS PROFESSIONAL;
DROP TABLE IF EXISTS STUDENT;
DROP TABLE IF EXISTS HELPSEEKER;
DROP TABLE IF EXISTS PERSON;

-- Create PERSON table (Base table for all users)
CREATE TABLE PERSON (
    PID INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(100) NOT NULL,
    CONTACT VARCHAR(15) NOT NULL UNIQUE,
    PSWD VARCHAR(255) NOT NULL
);

-- Create HELPSEEKER table (Users seeking mental health support)
CREATE TABLE HELPSEEKER (
    SEID INT PRIMARY KEY AUTO_INCREMENT,
    PID INT NOT NULL,
    ECONTACT VARCHAR(15),
    FOREIGN KEY (PID) REFERENCES PERSON(PID) ON DELETE CASCADE
);

-- Create STUDENT table (Students seeking help)
CREATE TABLE STUDENT (
    SID INT PRIMARY KEY AUTO_INCREMENT,
    SEID INT NOT NULL,
    INSNAME VARCHAR(100),
    COURSE VARCHAR(100),
    FOREIGN KEY (SEID) REFERENCES HELPSEEKER(SEID) ON DELETE CASCADE
);

-- Create PROFESSIONAL table (Working professionals seeking help)
CREATE TABLE PROFESSIONAL (
    PRID INT PRIMARY KEY AUTO_INCREMENT,
    SEID INT NOT NULL,
    INDNAME VARCHAR(100),
    FOREIGN KEY (SEID) REFERENCES HELPSEEKER(SEID) ON DELETE CASCADE
);

-- Create COUNSELLOR table (Mental health professionals)
CREATE TABLE COUNSELLOR (
    CID INT PRIMARY KEY AUTO_INCREMENT,
    PID INT NOT NULL,
    EMAIL VARCHAR(100) NOT NULL UNIQUE,
    SPEC VARCHAR(100),
    LICNO VARCHAR(50) UNIQUE,
    FOREIGN KEY (PID) REFERENCES PERSON(PID) ON DELETE CASCADE
);

-- Create CLINICAL_PSYCHOLOGIST table
CREATE TABLE CLINICAL_PSYCHOLOGIST (
    CPID INT PRIMARY KEY AUTO_INCREMENT,
    CID INT NOT NULL,
    FOREIGN KEY (CID) REFERENCES COUNSELLOR(CID) ON DELETE CASCADE
);

-- Create STUDENT_VOLUNTEER table
CREATE TABLE STUDENT_VOLUNTEER (
    SVID INT PRIMARY KEY AUTO_INCREMENT,
    CID INT NOT NULL,
    FOREIGN KEY (CID) REFERENCES COUNSELLOR(CID) ON DELETE CASCADE
);

-- Create MOODLOG table (Daily mood tracking)
CREATE TABLE MOODLOG (
    LOGID INT PRIMARY KEY AUTO_INCREMENT,
    SEID INT NOT NULL,
    SCORE INT NOT NULL CHECK (SCORE BETWEEN 1 AND 10),
    DATE DATE NOT NULL,
    TIME TIME NOT NULL,
    FOREIGN KEY (SEID) REFERENCES HELPSEEKER(SEID) ON DELETE CASCADE
);

-- Create APPOINTMENT table (Counseling sessions)
CREATE TABLE APPOINTMENT (
    AID INT PRIMARY KEY AUTO_INCREMENT,
    SEID INT NOT NULL,
    CID INT,
    DATE DATE NOT NULL,
    TIME TIME NOT NULL,
    STATUS VARCHAR(20) DEFAULT 'Pending',
    FOREIGN KEY (SEID) REFERENCES HELPSEEKER(SEID) ON DELETE CASCADE,
    FOREIGN KEY (CID) REFERENCES COUNSELLOR(CID) ON DELETE SET NULL
);

-- Create FEEDBACK table (Session ratings)
CREATE TABLE FEEDBACK (
    FID INT PRIMARY KEY AUTO_INCREMENT,
    AID INT NOT NULL,
    RATING INT CHECK (RATING BETWEEN 1 AND 5),
    COMMENTS TEXT,
    STATUS VARCHAR(20) DEFAULT 'Submitted',
    FOREIGN KEY (AID) REFERENCES APPOINTMENT(AID) ON DELETE CASCADE
);

-- Create ALERT table (Emergency notifications)
CREATE TABLE ALERT (
    ALID INT PRIMARY KEY AUTO_INCREMENT,
    LOGID INT NOT NULL,
    ECONTACT VARCHAR(15),
    STATUS VARCHAR(20) DEFAULT 'Active',
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (LOGID) REFERENCES MOODLOG(LOGID) ON DELETE CASCADE
);

-- Create MEDICINE table
CREATE TABLE MEDICINE (
    MID INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(100) NOT NULL,
    DOSAGE VARCHAR(50),
    PRICE DECIMAL(10,2)
);

-- ============================================================================
-- CREATE TRIGGERS
-- ============================================================================

-- Trigger to automatically create alerts for critical mood scores
DELIMITER //
CREATE TRIGGER create_alert_on_critical_mood
AFTER INSERT ON MOODLOG
FOR EACH ROW
BEGIN
    DECLARE emergency_contact VARCHAR(15);
    
    -- If mood score is critically low (1-3), create an alert
    IF NEW.SCORE <= 3 THEN
        -- Get emergency contact from HELPSEEKER
        SELECT ECONTACT INTO emergency_contact
        FROM HELPSEEKER
        WHERE SEID = NEW.SEID;
        
        -- Create alert
        INSERT INTO ALERT (LOGID, ECONTACT, STATUS)
        VALUES (NEW.LOGID, emergency_contact, 'Critical');
    END IF;
END//
DELIMITER ;

-- Create view for active sessions
CREATE VIEW ActiveSessions AS
SELECT 
    A.AID,
    A.DATE,
    A.TIME,
    A.STATUS,
    P1.NAME AS HelpSeekerName,
    P2.NAME AS CounsellorName,
    C.EMAIL AS CounsellorEmail,
    C.SPEC AS Specialization
FROM APPOINTMENT A
INNER JOIN HELPSEEKER H ON A.SEID = H.SEID
INNER JOIN PERSON P1 ON H.PID = P1.PID
LEFT JOIN COUNSELLOR C ON A.CID = C.CID
LEFT JOIN PERSON P2 ON C.PID = P2.PID
WHERE A.STATUS IN ('Pending', 'Scheduled', 'In Progress');

-- Create view for mood trends (7-day moving average)
CREATE VIEW MoodTrends AS
SELECT 
    H.SEID,
    P.NAME,
    M.DATE,
    M.SCORE,
    AVG(M.SCORE) OVER (
        PARTITION BY H.SEID 
        ORDER BY M.DATE 
        ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
    ) AS SevenDayAvg
FROM MOODLOG M
INNER JOIN HELPSEEKER H ON M.SEID = H.SEID
INNER JOIN PERSON P ON H.PID = P.PID
ORDER BY H.SEID, M.DATE DESC;

-- ============================================================================
-- INSERT SAMPLE DATA
-- ============================================================================

-- Insert sample persons
INSERT INTO PERSON (NAME, CONTACT, PSWD) VALUES
('Kaushiki', '+919876543210', 'password123'),
('Komal', '+19876543211', 'password123'),
('Akanksha Nandy', '+919876543212', 'password123'),
('Krish Kumar', '+19876543213', 'password123');

-- Insert sample help seekers
INSERT INTO HELPSEEKER (PID, ECONTACT) VALUES
(1, '9999999991'),
(2, '9999999992');

-- Insert sample student
INSERT INTO STUDENT (SEID, INSNAME, COURSE) VALUES
(1, 'Symbiosis Institute of Technology', 'Computer Science');

-- Insert sample professional
INSERT INTO PROFESSIONAL (SEID, INDNAME) VALUES
(2, 'Tech Corp India');

-- Insert sample counsellors
INSERT INTO COUNSELLOR (PID, EMAIL, SPEC, LICNO) VALUES
(3, 'akanksha.nandy@therapy.com', 'Clinical Psychology', 'LIC12345'),
(4, 'krish.kumar@therapy.com', 'Anxiety & Depression', 'LIC12346');

-- Insert clinical psychologists
INSERT INTO CLINICAL_PSYCHOLOGIST (CID) VALUES (1), (2);

-- Insert sample medicines
INSERT INTO MEDICINE (NAME, DOSAGE, PRICE) VALUES
('Sertraline', '50mg', 250.00),
('Fluoxetine', '20mg', 180.00),
('Escitalopram', '10mg', 320.00);

-- Insert sample mood logs
INSERT INTO MOODLOG (SEID, SCORE, DATE, TIME) VALUES
(1, 7, '2026-02-10', '09:00:00'),
(1, 6, '2026-02-11', '09:15:00'),
(1, 8, '2026-02-12', '09:30:00'),
(2, 6, '2026-02-10', '10:00:00');

-- Insert sample appointments
INSERT INTO APPOINTMENT (SEID, CID, DATE, TIME, STATUS) VALUES
(1, 1, '2026-02-20', '14:00:00', 'Scheduled'),
(2, 1, '2026-02-22', '16:00:00', 'Scheduled');

-- ============================================================================
-- VERIFICATION
-- ============================================================================

SELECT 'Database setup complete!' AS Status;
SELECT '12 tables created' AS Info;
SELECT '1 trigger created (automatic alert system)' AS Info;
SELECT '2 views created' AS Info;
SELECT 'Sample data inserted' AS Info;

-- Show tables
SHOW TABLES;
