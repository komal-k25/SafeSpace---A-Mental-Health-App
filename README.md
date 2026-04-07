<p align="center">
  <img src="https://img.shields.io/badge/SafeSpace-Mental_Health_Support-FF6B6B?style=for-the-badge&labelColor=1a1a2e&logo=heart&logoColor=white" alt="SafeSpace Badge" />
</p>

<h1 align="center">
  🌿 SafeSpace — A Mental Health Support Platform
</h1>

<p align="center">
  <strong>A secure, empathetic bridge between those seeking support and professional guidance.</strong><br/>
  An end-to-end platform featuring mood tracking, automated crisis alerts, and seamless counsellor-seeker connectivity — designed to prioritize your peace of mind.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-Swing-007396?style=flat-square&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-Connector-4479A1?style=flat-square&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/JDBC-Database_API-orange?style=flat-square" />
  <img src="https://img.shields.io/badge/Architecture-DAO_Pattern-lightgrey?style=flat-square" />
</p>

---

## 📋 Table of Contents

- [🎯 What is SafeSpace?](#-what-is-safespace)
- [✨ Key Features](#-features)
- [🧰 Tech Stack](#-tech-stack)
- [🚀 Quick Start](#-quick-start)
- [📁 Project Structure](#-project-structure)
- [🤝 Contributing](#-contributing)

---

## 🎯 What is SafeSpace?

**SafeSpace** is a comprehensive mental health management system built to provide immediate support to individuals in distress. By combining daily **mood tracking** with an automated **crisis alert system**, it ensures that "Critical" emotional states are never ignored.

The application serves two distinct roles:
1. **Help Seeker:** Logs daily moods, views avg score, and books professional appointments.
2. **Counsellor:** Manages assigned patients, provides prescriptions, and generates session codes.
   
---

## ✨ Features

<table>
<tr>
<td width="50%">

### 📊 Intelligent Mood Tracking
- Daily mood logging with a scale of 1-10
- **Visual Statistics:** Track average mood and total logs
- Categorized status: Critical, Moderate, or Good

</td>
<td width="50%">

### 🚨 Automated Crisis Alerts
- **Real-time Triggers:** SQL triggers detect critical scores (≤ 3)
- **Emergency Contact Sync:** Automatically alerts designated contacts

</td>
</tr>
<tr>
<td width="50%">

### 📅 Appointment Management
- Seeker-side booking with specific counsellors
- **Session Codes:** Secure meeting codes for telehealth sessions
- **Medical Advice:** Integrated field for counsellor prescriptions/notes
- Real-time status updates (Pending, Scheduled, Resolved)

</td>
<td width="50%">

### 🔐 Multi-Role Dashboards
- **Seeker:** Personalized stats and mood history
- **Counsellor:** Patient list and clinical appointment management
- Secure login with role-based access control (RBAC)

</td>
</tr>
</table>

---

## 🧰 Tech Stack

| Layer | Technology | Purpose |
|:------|:-----------|:--------|
| **Frontend** | Java Swing | Desktop GUI with modern Look & Feel |
| **Language** | Java 17+ | Core application logic |
| **Database** | MySQL 8.x | User data, mood history, and appointment storage |
| **Connectivity**| JDBC | Database-to-Application communication |
| **Design Pattern**| DAO (Data Access Object) | Separation of data logic and UI |
| **Architecture**| Model-View-Controller (MVC) | Scalable project organization |
| **Trigger Logic**| MySQL Triggers | Automated crisis alert generation |

---

## 🚀 Quick Start

### Prerequisites

| Tool | Version | Download |
|:-----|:--------|:---------|
| **JDK** | ≥ 17.x | [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) |
| **IDE** | IntelliJ / Eclipse | [IntelliJ IDEA](https://www.jetbrains.com/idea/) |
| **MySQL** | ≥ 8.x | [MySQL Community Server](https://dev.mysql.com/downloads/mysql/) |
| **Connector**| mysql-connector-j | Included in lib folder |

1️⃣ Clone the Repository
```
git clone https://github.com/MalevolentMist/SafeSpace---A-Mental-Health-App.git
cd SafeSpace---A-Mental-Health-App
```

2️⃣ Database Setup

Import the provided SQL schema into your MySQL instance:
```
CREATE DATABASE mental_health_db;
-- Import the mental_health_db.sql file using your IDE or Workbench
```

3️⃣ Configure Connection

Edit com.mentalhealthapp.util.DatabaseConnection.java:
```
private static final String URL = "jdbc:mysql://localhost:3306/mental_health_db";
private static final String USER = "your_username";
private static final String PASS = "your_password";
```

4️⃣ Run the Application

Locate LoginFrame.java and run it as a Java Application.

---

## 📁 Project Structure

```
SafeSpace/
├── 📂 src/
│   └── 📂 com.mentalhealthapp/
│       ├── 📂 dao/                   # Data Access Objects (Database logic)
│       │   ├── AdminDAO.java
│       │   ├── AlertDAO.java
│       │   ├── AppointmentDAO.java
│       │   ├── CounsellorDAO.java
│       │   ├── HelpSeekerDAO.java
│       │   └── MoodLogDAO.java
│       ├── 📂 gui/                   # Swing Graphical User Interface
│       │   ├── AdminDashboard.java
│       │   ├── CounsellorDashboard.java
│       │   ├── HelpSeekerDashboard.java
│       │   ├── LoginFrame.java
│       │   └── RegisterFrame.java
│       ├── 📂 model/                 # Data Models 
│       │   ├── Admin.java
│       │   ├── Alert.java
│       │   ├── Appointment.java
│       │   ├── Counsellor.java
│       │   ├── HelpSeeker.java
│       │   └── MoodLog.java
│       ├── 📂 util/                  # Database connectivity & Utilities
│       │   └── DatabaseConnection.java
│       └── Main.java                 # Application Entry Point
├── 📂 lib/                       # External Libraries (mysql-connector-j)
└── 📂 sql/                       # SQL Scripts & Trigger Definitions
```

---

## 🤝 Contributing
1. Fork the repo.

2. Create your feature branch:
   ```
   git checkout -b feature/NewFeature
   ```

3. Commit changes 
   ```
   git commit -m 'Add some NewFeature'
   ```

4. Push to the branch
   ```
   git push origin feature/NewFeature
   ```

5. Open a Pull Request.

---

## 👥 Authors

<div align="center">
  
<table>
<tr>
<td align="center">
  <strong>Kaushiki Jha</strong><br/>
  <a href="https://github.com/MalevolentMist">@MalevolentMist</a><br/>
</td>
<td align="center">
  <strong>Komal Kumar</strong><br/>
  <a href="https://github.com/komal-k25">@komal-k25</a><br/>
</td>
<td align="center">
  <strong>Karina Muley</strong><br/>
  <a href="https://github.com/rina136">@rina136</a><br/>
</td>
<td align="center">
  <strong>Karen Cynthia</strong><br/>
  <a href="https://github.com/kcyn00261-lgtm">@kcyn00261-lgtm</a><br/>
</td>
</tr>
</table>

</div>


<p align="center">
⭐ <strong>Mental health matters.</strong> ⭐
</p>
