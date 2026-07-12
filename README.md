# Student Management System

## Description
A comprehensive Student Management System built using Java Swing, JDBC, and MySQL. It features a modern graphical user interface utilizing FlatLaf for styling, LGoodDatePicker for date inputs, and Ikonli for modern icons.

## Features
- Manage student records (Add, Update, Delete, View)
- Modern and responsive GUI
- Database connectivity with MySQL
- Date picking functionality

## Technologies Used
- **Java 17**
- **Java Swing** for GUI
- **JDBC (MySQL Connector)** for Database Connectivity
- **Maven** for Dependency Management
- **FlatLaf** for modern Look and Feel
- **LGoodDatePicker** for date selection
- **Ikonli** for modern icons

## Screenshots
*(Add screenshots here)*

## Project Structure
- `src/` - Contains all Java source files
- `pom.xml` - Maven configuration file
- `database_setup.sql` - SQL script to initialize the database
- `README.md` - Project documentation

## Installation Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/SakshiMane710/Student_leave_managment_System.git
   ```
2. Navigate to the project directory:
   ```bash
   cd Student_leave_managment_System
   ```
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the application (specify the main class if needed).

## Database Setup Instructions
1. Ensure you have MySQL Server installed and running.
2. Execute the `database_setup.sql` script in your MySQL environment to create the necessary database and tables.
3. Update database credentials in the application configuration/source if necessary.
